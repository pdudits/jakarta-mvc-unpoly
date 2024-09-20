package io.zeromagic.unpolydemo.app;

import io.zeromagic.unpolydemo.datatypes.ContextRoot;
import io.zeromagic.unpolydemo.datatypes.JakartaVersion;
import io.zeromagic.unpolydemo.datatypes.RuntimeSize;
import io.zeromagic.unpolydemo.datatypes.ScalabilityType;
import io.zeromagic.unpolydemo.endpoint.FormField;
import io.zeromagic.unpolydemo.endpoint.Page;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.mvc.MvcContext;
import jakarta.ws.rs.BeanParam;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static io.zeromagic.unpolydemo.endpoint.FormField.check;
import static io.zeromagic.unpolydemo.endpoint.FormField.require;

@Path("/app")
@RequestScoped
@Controller
public class AppResource {
  private static final String CONFIG_EDIT = "app/config-edit.jte";
  @Inject
  AppModel app;

  @Inject
  AppConfigModel appConfig;

  @Inject
  Page page;

  @Inject
  MvcContext mvc;

  @GET
  public String index() {
    return "app/index.jte";
  }

  @GET
  @Path("{name}")
  public String appDetail(@PathParam("name") String name) {
    app.setName(name);
    app.setCharts(List.of(AppChart.requestsChart(), AppChart.logsChart(), AppChart.cpuAndMemoryChart()));
    return "app/detail.jte";
  }

  @GET
  @Path("{name}/config")
  public String config(@PathParam("name") String name, @QueryParam("edit") @DefaultValue("false") boolean edit) {
    app.setName(name);
    if (edit) {
      return CONFIG_EDIT;
    }
    return "app/config.jte";
  }

  @POST
  @Controller
  @Path("{name}/config")
  public Response processConfig(@PathParam("name") String name, @BeanParam ConfigParams params, @HeaderParam("X-Up-Validate") String validate) {
    return switch (validate) {
      case "scalingType" -> validateScalingType(params);
      case null -> updateConfig(name, params);
      default -> validateConfig(params);
    };
  }

  private Response validateConfig(ConfigParams params) {
    params.validate(appConfig);
    return Response.status(422).entity(CONFIG_EDIT).build();
  }

  private Response updateConfig(String name, ConfigParams params) {
    if (!params.validate(appConfig)) {
      return Response.status(422).entity(CONFIG_EDIT).build();
    }
    page.flash("Configuration was updated");
    return Response.seeOther(mvc.uri("AppResource#appDetail", Map.of("name",name))).build();
  }

  private Response validateScalingType(ConfigParams params) {
    params.validateScalingType(appConfig);
    return Response.status(422).entity(CONFIG_EDIT).build();
  }

  @GET
  @Path("{name}/charts")
  public String charts() throws InterruptedException {
    // Request Scope does not propagate to managed executors
    // therefore we need to block synchronously
    Thread.sleep(5000);
    app.setCharts(List.of(AppChart.requestsChart(), AppChart.logsChart(), AppChart.cpuAndMemoryChart()));
    return "app/charts.jte";
  }


  static class ConfigParams {

    @FormParam("contextRoot")
    String contextRoot;

    @FormParam("jakartaVersion")
    String jakartaVersion;

    @FormParam("javaVersion")
    String javaVersion;

    @FormParam("scalingType")
    String scalingType;

    @FormParam("datagrid")
    String datagrid;

    @FormParam("switchoverTime")
    String switchoverTime;

    @FormParam("replicas")
    String replicas;

    @FormParam("runtimeSize")
    String runtimeSize;

    ScalabilityType validateScalingType(AppConfigModel model) {
      if (scalingType == null || scalingType.isBlank()) {
        model.setScalabilityField(FormField.invalid(scalingType, "Scaling type is required"));
      } else {
        var type = FormField.parse(scalingType, model::setScalabilityField, ScalabilityType::fromString);
        if (type != null) {
          model.setScalabilityType(type);
          model.setAvailableSizes(type.getSupportedSizes());
          model.setRuntimeSize(FormField.undetermined(type.defaultRuntimeSize().toString()));
          // set defaults for scalability-dependant fields:
          switch (type) {
            case ROLLING -> {
              model.setDatagrid(FormField.undetermined("disabled"));
              model.setSwitchOverTime(FormField.undetermined("60"));
            }
            case HORIZONTAL -> {
              model.setDatagrid(FormField.undetermined("enabled"));
              model.setSwitchOverTime(FormField.undetermined("60"));
              model.setReplicas(FormField.undetermined("2"));
            }
          }
          return type;
        }
      }
      return null;
    }

    boolean validate(AppConfigModel model) {
      if (contextRoot == null || contextRoot.isBlank()) {
        model.setContextRoot(FormField.invalid(contextRoot, "Context root is required"));
      } else if (!ContextRoot.isValidContextRoot(contextRoot)) {
        model.setContextRoot(FormField.invalid(contextRoot, "Context root should be url prefix starting with '/'"));
      } else {
        model.setContextRoot(FormField.valid(contextRoot));
      }
      var jakartaVersion = FormField.parse(this.jakartaVersion, model::setJakartaVersion,
          require("Jakarta EE version is required", JakartaVersion::parse));
      if (jakartaVersion != null) {
          FormField.parse(javaVersion, model::setJavaVersion, jakartaVersion::checkJavaVersion);
      }

      var scalabilityType = validateScalingType(model);
      if (scalabilityType != null) {
        validateScalingOptions(model, scalabilityType);
      }
      return model.inputValid();
    }

    private void validateScalingOptions(AppConfigModel app, ScalabilityType scalabilityType) {
      var size = FormField.parse(runtimeSize, app::setRuntimeSize,
          require("Runtime size is required", RuntimeSize::fromString)
              .andThen(check(scalabilityType.getSupportedSizes()::contains,
                  v -> "Unsupported size for %s. Supported sizes are:".formatted(scalabilityType, scalabilityType.getSupportedSizes()))));
      if (scalabilityType.hasDatagrid()) {
        FormField.parse(datagrid, app::setDatagrid,
            require("Datagrid is required", Function.identity()));
      }
      if (scalabilityType.hasSwitchoverTime()) {
        FormField.parse(switchoverTime, app::setSwitchOverTime,
            require("Switchover time is required", Integer::valueOf));
      }
      if (scalabilityType.hasReplicas()) {
        FormField.parse(replicas, app::setReplicas, require("Replicas is required", Integer::valueOf)
            .andThen(check(v -> v >= 1 && v <= 8, v -> "Replicas must be between 1 and 8")));
      }
    }
  }
}


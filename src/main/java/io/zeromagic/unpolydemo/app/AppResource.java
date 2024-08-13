package io.zeromagic.unpolydemo.app;

import io.zeromagic.unpolydemo.endpoint.FormField;
import jakarta.annotation.Resource;
import jakarta.enterprise.concurrent.ManagedScheduledExecutorService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.mvc.MvcContext;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.container.AsyncResponse;
import jakarta.ws.rs.container.Suspended;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

@Path("/app")
@RequestScoped
@Controller
public class AppResource {
  @Inject
  AppModel app;

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
    app.setCharts(List.of(AppChart.requestsChart(),
            AppChart.logsChart(), AppChart.cpuAndMemoryChart()));
    return "app/detail.jte";
  }

  @GET
  @Path("{name}/charts")
  public String charts() throws InterruptedException {
    // Request Scope does not propagate to managed executors
    Thread.sleep(5000);
    app.setCharts(List.of(AppChart.requestsChart(),
            AppChart.logsChart(), AppChart.cpuAndMemoryChart()));
    return "app/charts.jte";
  }
}


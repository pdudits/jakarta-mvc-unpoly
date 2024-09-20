package io.zeromagic.unpolydemo.endpoint;

import gg.jte.Content;
import gg.jte.ContentType;
import gg.jte.TemplateEngine;
import gg.jte.TemplateOutput;
import gg.jte.output.PrintWriterOutput;
import gg.jte.resolve.DirectoryCodeResolver;
import jakarta.annotation.PostConstruct;
import jakarta.el.ExpressionFactory;
import jakarta.el.StandardELContext;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.inject.Inject;
import jakarta.mvc.engine.ViewEngineContext;
import jakarta.mvc.engine.ViewEngineException;
import jakarta.servlet.ServletContext;
import org.eclipse.krazo.engine.ViewEngineBase;

import java.io.File;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
public class JteViewEngine extends ViewEngineBase {
  private static final Logger LOGGER = Logger.getLogger(
      JteViewEngine.class.getName());
  @Inject
  ServletContext context;

  @Inject
  BeanManager beanManager;

  private TemplateEngine templateEngine;

  @PostConstruct
  void initTemplateEngine() {
    var root = context.getRealPath("/WEB-INF/views");
    var tempDir = (File) context.getAttribute(ServletContext.TEMPDIR);
    var classDir = new File(tempDir, "jte-classes");
    classDir.mkdir();
    var codeResolver = new DirectoryCodeResolver(Path.of(root));
    this.templateEngine = TemplateEngine.create(codeResolver, classDir.toPath(),
        ContentType.Html, this.getClass().getClassLoader());
  }

  @Override
  public boolean supports(String view) {
    return view.endsWith(".jte");
  }

  @Override
  public void processView(ViewEngineContext viewEngineContext) throws
      ViewEngineException {
    var view = viewEngineContext.getView();
    boolean raw = view.startsWith("raw:");
    if (raw) {
      view = view.substring(4);
    }
    var finalView = view;
    try (var writer = new PrintWriter(viewEngineContext.getOutputStream(),
        false, resolveCharsetAndSetContentType(viewEngineContext))) {
      if (raw) {
        render(viewEngineContext, new PrintWriterOutput(writer), view, null);
      } else {
        Content content = (output) -> render(viewEngineContext, output,
            finalView, null);
        render(viewEngineContext, new PrintWriterOutput(writer), "layout.jte",
            content);
      }
    } catch (Exception e) {
      LOGGER.log(Level.WARNING, "Failed to render view: " + view, e);
      throw new ViewEngineException(e);
    }
  }

  private void render(ViewEngineContext viewEngineContext,
      TemplateOutput output, String finalView, Content content) {
    Map<String, Class<?>> params = templateEngine.getParamInfo(finalView);
    var models = viewEngineContext.getModels();
    var inputs = new HashMap<String, Object>();
    var resolver = beanManager.getELResolver();
    var elContext = new StandardELContext(ExpressionFactory.newInstance());
    for (var entry : params.entrySet()) {
      if ("content".equals(entry.getKey())) {
        inputs.put("content", content);
        continue;
      }
      var modelInput = models.get(entry.getKey(), entry.getValue());
      if (modelInput != null) {
        inputs.put(entry.getKey(), modelInput);
      } else {
        var elValue = resolver.getValue(elContext, null, entry.getKey());
        if (elValue != null) {
          inputs.put(entry.getKey(), elValue);
        }
      }
    }
    templateEngine.render(finalView, inputs, output);
  }

}

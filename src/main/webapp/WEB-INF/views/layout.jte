@param gg.jte.Content content
@param io.zeromagic.unpolydemo.endpoint.Page page

<!DOCTYPE html>
<html lang="en" data-theme="light">

<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <meta name="color-scheme" content="light dark">
  <title>${page.getTitle()}</title>
  <meta name="description" content="A pure HTML example, without dependencies.">

  <!-- Pico.css -->
  <link rel="stylesheet" href="${page.contextPath()}/webjars/picocss__pico/2.0.6/css/pico.min.css">
  <link rel="stylesheet" href="${page.contextPath()}/css/styles.css">
  <!-- Unpoly -->
  <script
    src="${page.contextPath()}/webjars/unpoly/3.9.1/unpoly.js"
    defer></script>
  <script src="${page.contextPath()}/js/scripts.js" defer></script>
  <link rel="stylesheet"
    href="${page.contextPath()}/webjars/unpoly/3.9.1/unpoly.min.css">
</head>

<body>
<div id="grid">
<aside>
  <header>
    <h3>Unpoly Demo</h3>
  </header>
  <hr>
  <nav id="main-nav">
    <ul>
      <li><a href="/app/" up-follow>Applications</a><a href="/static/up-follow.html"
          class="instruction">?</a></li>
      <li><a href="/ui-preference/" up-follow>UI Preferences</a></li>
      <li><span>
        <a href="/new-app/" role="button"
          up-layer="new"
          up-target="#modal"
          up-history="true">
          New Application</a>
        <a href="/static/up-layer-1.html" style="right:-1.5em" class="instruction">?</a>
        </span>
      </li>
    </ul>
  </nav>
</aside>
<div class="container" id="main">
  <aside id="flash" up-flashes>@if (page.hasFlash()) <article>${page.flash()}</article>@endif</aside>
  <main up-main>${content}</main>
</div>
<footer class="container">
  <small><a href="https://mastodon.social/@pdudits">Patrik Duditš, @pdudits@mastodon.social</a></small>
  <small>
    <a href="${page.contextPath()}/cookies"
      up-layer="new" up-mode="drawer" up-position="right"
      up-history
      up-target="#cookie-preferences"
      up-on-accepted="up.emit('cookie-pref:changed',{value})">
      Cookie?</a>
    <a href="/static/up-layer-2.html" class="instruction" style="right:-1.8em">?</a>
  </small>
</footer>
</div>
<!-- hot reload - connect to sse endoint at / reload on event -->
<script>
    {
        let eventSource = new EventSource('/');
        eventSource.onmessage = () => location.reload();
    }
</script>
</body>
</html>
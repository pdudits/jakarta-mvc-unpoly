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
    <link rel="stylesheet"
            href="${page.contextPath()}/webjars/picocss__pico/2.0.6/css/pico.min.css">
    <link rel="stylesheet" href="${page.contextPath()}/css/layout.css">
    <!-- Unpoly -->
    <script src="${page.contextPath()}/webjars/unpoly/3.8.0/unpoly.min.js" defer></script>
    <script src="${page.contextPath()}/js/compilers.js" defer></script>
<%--    <link rel="stylesheet" href="${page.contextPath()}/webjars/unpoly/3.7.3/unpoly.min.css">--%>
</head>
<body>
<div id="grid">
    <aside>
        <header>
            <hgroup>
                <h3>Unpoly flythrough</h3>
                <p><small>SPA experience with server side rendering</small></p>
            </hgroup>
        </header>
        <hr>
        <nav id="main-nav">
            <ul>
                <li><a href="/app/">Applications</a></li>
                <li><a href="/new-app/" role="button"
                       up-layer="new"
                       up-target="#modal"
                       up-history="true">
                       New Application</a></li>
            </ul>
        </nav>
    </aside>
<main class="container">
    ${content}
</main>
<footer class="container">
    <small><a href="https://mastodon.social/@pdudits">Patrik Duditš, @pdudits@mastodon.social</a></small>
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
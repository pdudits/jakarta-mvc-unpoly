@param gg.jte.Content content
@param jakarta.servlet.http.HttpServletRequest request
@param io.zeromagic.unpolydemo.endpoint.Page page

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="color-scheme" content="light dark">
    <title>${page.getTitle()}</title>
    <meta name="description" content="A pure HTML example, without dependencies.">

    <!-- Pico.css -->
    <link rel="stylesheet"
            href="${page.contextPath()}/webjars/picocss__pico/2.0.6/css/pico.min.css">
    <!-- Unpoly -->
    <script src="${page.contextPath()}/webjars/unpoly/3.7.3/unpoly.min.js" defer></script>
<%--    <link rel="stylesheet" href="${page.contextPath()}/webjars/unpoly/3.7.3/unpoly.min.css">--%>
</head>
<body>
<!-- Header -->
<header class="container">
    <hgroup>
        <h1>Unpoly flythrough</h1>
        <p>SPA experience with server side rendering</p>
    </hgroup>
    <nav>
    </nav>
</header>
<main class="container">${content}</main>
<footer class="container">
    <small>end</small>
</footer>
<!-- hot reload - connect to sse endoint at / reload on event -->
<script>
    const eventSource = new EventSource('/');
    eventSource.onmessage = () => location.reload();
</script>
</body>
</html>
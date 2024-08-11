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
    <!-- Unpoly -->
    <script src="${page.contextPath()}/webjars/unpoly/3.8.0/unpoly.min.js" defer></script>
<%--    <link rel="stylesheet" href="${page.contextPath()}/webjars/unpoly/3.7.3/unpoly.min.css">--%>
    <style>
        #grid {
            display: grid;
            grid-template-rows: 1fr auto;
            grid-template-columns: 200px 1fr;
            height: 100vh;
        }

        #grid > aside {
            grid-row: 1 / 3;
            grid-column: 1 / 2;
            background-color: var(--pico-card-background-color);
            padding: 1em 0.5em;
        }

        #grid > main {
            grid-row: 1 / 2;
            grid-column: 2 / 3;
            padding: 1em;
        }

        #grid > footer {
            grid-row: 2 / 3;
            grid-column: 2 / 3;
            height: 2em;
            padding: 0 1em;
        }

        #main-nav a[aria-current="page"] {
            --pico-background-color: var(--pico-contrast-background);
            --pico-color: var(--pico-contrast-inverse);
            --pico-border-radius: 0;
        }
    </style>
</head>
<body>
<div id="grid">
    <aside>
        <nav id="main-nav">
            <ul>
                <li><a href="/app/">Application</a></li>
                <li><a href="/app/new" role="button"
                       up-layer="new"
                       up-target="#modal"
                       up-history="true">
                       New Application</a></li>
            </ul>
        </nav>
    </aside>
<main class="container">
    <header>
        <hgroup>
            <h1>Unpoly flythrough</h1>
            <p>SPA experience with server side rendering</p>
        </hgroup>
    </header>
    ${content}
</main>
<footer class="container">
    <small><a href="https://mastodon.social/@pdudits">Patrik Duditš, @pdudits@mastodon.social</a></small>
</footer>
</div>
<!-- hot reload - connect to sse endoint at / reload on event -->
<script>
    const eventSource = new EventSource('/');
    eventSource.onmessage = () => location.reload();
</script>
</body>
</html>
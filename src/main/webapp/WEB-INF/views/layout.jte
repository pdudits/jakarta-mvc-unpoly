@param gg.jte.Content content
@param io.zeromagic.unpolydemo.endpoint.Page page

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <meta name="color-scheme" content="light dark" />
    <title>${page.getTitle()}</title>
    <meta name="description" content="A pure HTML example, without dependencies." />

    <!-- Pico.css -->
    <link
            rel="stylesheet"
            href="https://cdn.jsdelivr.net/npm/@picocss/pico@2.0.6/css/pico.min.css"
    />
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
<main class="container">${content}
</main>
<footer class="container">
    <small>end</small>
</footer>
</body>
</html>
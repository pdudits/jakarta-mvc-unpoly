@param io.zeromagic.unpolydemo.app.AppModel app

<h2>Application ${app.getName()}</h2>
<section>
<article>
  <style>
    .chart-box {
       display: grid;
       grid-template-columns: repeat(3, 1fr);
       gap: var(--pico-block-spacing-vertical);
    }
    .chart-box > * {
      background-color: #f0f0f0;
      padding: 0.5rem 1rem;
      border-radius: 0.5rem;
      aspect-ratio: 1/1;
    }
    #chart-container {
        position:relative;
        display: flex;
        width: 100%;
        flex-direction: column;
        min-height: 100px;
    }
  </style>
  <header>
    <strong>Status</strong>
  </header>
  <!-- using relative path cause problem when we redirect. -->
  <div id="chart-container" up-defer up-href="/app/${app.getName()}/charts">
  <div class="chart-box" >
    <div aria-busy="true"></div>
    <div aria-busy="true"></div>
    <div aria-busy="true"></div>
  </div>
  </div>
</article>
</section>
<section>
<article>
<header>
  <h4>Action Log</h4>
</header>

<div class="action-log">
  <details>
    <summary>
      <time value="20240814T10:00:00Z">14. 8. 12:00</time>
      <span>Revision 1 deployed successfully</span>
    </summary>
    <p>View/edit Configuration...</p>
    <p>View/edit comments</p>
  </details>

  <details>
    <summary>
      <time value="20240814T10:00:00Z">14. 8. 12:00</time>
      <span>Uploaded Revision 1</span>
    </summary>
    <p>View/edit Configuration...</p>
    <p>View/edit comments</p>
  </details>
</div>
</article>
</section>
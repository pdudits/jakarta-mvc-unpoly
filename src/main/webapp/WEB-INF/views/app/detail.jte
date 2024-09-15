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
    .action-log summary span {
        font-weight:bold;
    }
    .comment-list li {
        list-style-type: none;
    }

    .comment-list {
        padding-left: 0;
    }

    .comment-header {
        color: var(--pico-muted-color);
        display: block;
    }

    .comment-header .author {
        font-weight: bold;
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
  <strong>Action Log</strong>
</header>

<div class="action-log">
  @for(var event : app.getTimeline().reversed())
  <details>
    <summary>
      <time value="${event.timestamp().toString()}">${app.formatEventTimestamp(event.timestamp())}</time>
      <span>${event.message()}</span>
    </summary>
    <section>
      <h5>Configuration</h5>
      <p>View/edit Configuration...</p>
    </section>
    <section>
      <a href="/comment/appevent/${event.id()}/" up-defer="reveal" up-target="section">Comments</a>
    </section>
  </details><hr>
  @endfor
</div>
</article>
</section>
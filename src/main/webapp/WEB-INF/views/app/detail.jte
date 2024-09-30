@param io.zeromagic.unpolydemo.app.AppModel app

<h2>
  <a href="/app/" role="button" class="outline" up-follow
    up-transition="move-right"> &lt; </a>
  Application ${app.getName()}
</h2>
<section>
<article>
  <header>
    <strong>Status</strong><a href="/static/up-defer.html" class="instruction">?</a>
  </header>
  <!-- using relative path cause problem when we redirect. -->
  <div id="chart-container" up-defer up-href="/app/${app.getName()}/charts" up-revalidate="true">
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
  <details id="event-${event.id()}">
    <summary>
      <time datetime="${event.timestamp().toString()}">${app.formatEventTimestamp(event.timestamp())}</time>
      <span>${event.message()}</span>
    </summary>
    <section>
      <h5>Configuration</h5>
      <p><a href="/app/${app.getName()}/config?edit=true" up-follow>Edit configuration</a></p>
    </section>
    <section>
      <a href="/comment/appevent/${event.id()}/"
        up-defer="reveal" up-target="section"
        up-use-hungry="false">Comments</a>
    </section>
  </details><hr>
  @endfor
</div>
</article>
</section>
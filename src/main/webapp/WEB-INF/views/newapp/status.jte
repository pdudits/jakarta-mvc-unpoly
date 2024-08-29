@param io.zeromagic.unpolydemo.newapp.Inspection inspection
<article>
  <header><h2>Inspecting Application ${inspection.name()}</h2></header>
  <form up-target="main" up-cache="false">
    <progress></progress>
    <p style="text-align:center">
      <button class="outline contrast" id="reload">
        <span aria-busy="true"></span>
        ${switch (inspection.status()) {
            case PENDING -> "Pending";
            case IN_PROGRESS -> "In Progress";
            case UPLOADING -> "Uploading";
            case COMPLETE -> "Complete";
            case FAILED -> "Failed";
          }}...
      </button>
    </p>
  </form>
</article>
<script>
{
    let refreshSse = new EventSource(''); // connect to the same endpoint
    refreshSse.onmessage = (event) => {
        let data = JSON.parse(event.data);
        if (data.finalState) {
            refreshSse.close();
        }
        // click refresh button
        let reload = document.getElementById('reload');
        if (reload) {
            reload.click();
        }
    }
}
</script>


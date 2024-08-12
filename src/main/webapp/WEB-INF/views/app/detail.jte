@param io.zeromagic.unpolydemo.app.AppModel app

<h2>Application ${app.getName()}</h2>
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
  </style>
  <script type="module" src="https://cdn.jsdelivr.net/npm/lit-line@latest/cdn/lit-line.js"></script>
  <header>
    <strong>Status</strong>
  </header>
  <div class="chart-box">
    <div aria-busy="true"></div>
    <div aria-busy="true"></div>
    <lit-line id="chart"></lit-line>
  </div>
</article>
<script>
    window.customElements.whenDefined("lit-line").then(() =>
    {
        let chart = document.getElementById('chart');
        chart.data = [
            {
                color: '#999',
                points: [
                    {time: 1982, value: 1112},
                    {time: 1983, value: 2705},
                    {time: 2014, value: 1303},
                    {time: 2016, value: 2605},
                ]
            }
        ];
    });
</script>
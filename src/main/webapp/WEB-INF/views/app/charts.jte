@param io.zeromagic.unpolydemo.app.AppModel app
<div id="chart-container">
<div class="chart-box" id="app-charts">
@for(var chart : app.getCharts())
 @template.app.chart(chart=chart)
@endfor
</div>
</div>

@param io.zeromagic.unpolydemo.app.AppChart chart

<lit-line id="chart-${chart.hashCode()}"></lit-line>
<script>
    window.customElements.whenDefined("lit-line").then(() =>
    {
        let chartElement = document.getElementById('chart-${chart.hashCode()}');
        let data = [];
        @for(var series : chart.series())
        !{var timeiterator = chart.timestamps().iterator();}
        {
            let series = {color: '${switch (series.purpose()) {
              case INFO -> "#017FC0";
              case WARNING -> "#FFBF00";
              case INFO_SECONDARY -> "#A5D601";
            }}', points: []};
            series.points = [
                @for(var value: series.data())
                {
                    time: ${timeiterator.next().toEpochMilli()}, value: ${value}
                },
                @endfor
            ];
            data.push(series);
        }
        @endfor
        chartElement.data = data;
    });
</script>

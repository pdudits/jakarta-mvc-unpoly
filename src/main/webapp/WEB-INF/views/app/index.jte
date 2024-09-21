@param io.zeromagic.unpolydemo.app.AppListModel appList

<h2>Application List</h2>

<table up-poll up-interval="3000" id="apps">
<thead>
<tr>
  <th scope="col">Application Name</th>
  <th scope="col">URI</th>
  <th scope="col" style="width: 4rem">Memory Utilization</th>
  <th scope="col" style="width: 4rem">CPU Utilization</th>
</tr>
</thead>

<tbody>
@for(var app : appList.apps())
  <tr>
    <td>
      <a href="/app/${app.name()}/" up-follow
        up-transition="move-left">${app.name()}</a>
    </td>
    <td><a href="${app.uri().toString()}">${app.uri().getPath()}</a></td>
    <td style="text-align:right">
      <bar-chart up-keep id="mem-${app.name()}" class="polled">
        <bar style="--h: ${app.memoryUtilization()*100}%"></bar>
      </bar-chart>
      <span>${app.formattedMemoryUtilization()}</span>
    </td>
    <td style="text-align:right">
      <bar-chart up-keep id="cpu-${app.name()}" class="polled">
        <bar style="--h: ${app.cpuUtilization()*100}%"></bar>
      </bar-chart>
      <span>${app.formattedCpuUtilization()}</span></td>
  </tr>
@endfor
</tbody>
</table>
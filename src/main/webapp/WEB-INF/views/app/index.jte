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
        <td><a href="${app.name()}/" up-follow>${app.name()}</a></td>
        <td><a href="${app.uri().toString()}">${app.uri().getPath()}</a></td>
        <td style="text-align:right">${app.formattedMemoryUtilization()}</td>
        <td style="text-align:right">${app.formattedCpuUtilization()}</td>
    </tr>
    @endfor
    </tbody>
</table>
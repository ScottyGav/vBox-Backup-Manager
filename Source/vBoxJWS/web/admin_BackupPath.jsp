<%-- 
    Document   : admin_BackupPath
    Created on : 05/01/2014, 11:55:30 AM
    Author     : Admin
--%>

<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>


<%@ page language="java" import="vBoxJwsTools.*" %>
<!DOCTYPE html>
<%
    VBoxHostManager vBoxHostManager = (VBoxHostManager)getServletContext().getAttribute("vBoxHostManager");

    //Ajax postbacks
    String data = request.getParameter("data");
    if(data != null)
    {
        if(data.equals("deletePath"))
        {
            String pathId = request.getParameter("pathId");
            vBoxHostManager.deleteBackupPath(pathId);
        }
    }

    //form submittals
    String backupPath = request.getParameter("backupPath");
    if(backupPath != "" && backupPath != null)
    {
      vBoxHostManager.addBackupPath( backupPath);  
    }
%>

<%!

public String printBackupPaths()
{
    VBoxHostManager vBoxHostManager = (VBoxHostManager)getServletContext().getAttribute("vBoxHostManager");
    String htmlResponce = "";
    ArrayList BackupPathList = vBoxHostManager.getBackupPaths();

    int listLength = BackupPathList.size();

    htmlResponce += "<table data-role=\"table\" id=\"HostListTableId\" class=\"ui-responsive table-stroke\" style=\"display:table;\" >";
    htmlResponce += "<thead><tr><th>Backup Path</th>";
    htmlResponce += "<th></th></thead><tbody>";

    for(int count = 0; count < listLength; count++ )
    {
        String[] eventPair = (String[]) BackupPathList.get(count);
        String id = eventPair[0];
        String path = eventPair[1];
        htmlResponce += "<tr><td>" + path +" </td><td><a class=\"ui-btn ui-mini\" style=\"float:left; width:90%;\" href=\"admin_BackupPath.jsp?data=deletePath&pathId="+id+"\" > Delete </a></td></tr>" ;
    }

    return htmlResponce+= "</tbody></table>";
}

%>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        
        <link rel="stylesheet" href="css/themes/vBoxJWS.min.css" />
        <link rel="stylesheet" href="css/themes/jquery.mobile.icons.min.css" />
        
        <link rel="stylesheet" href="http://code.jquery.com/mobile/1.4.3/jquery.mobile.structure-1.4.3.min.css" />
        <script src="http://code.jquery.com/jquery-1.11.1.min.js"></script>
        <script src="http://code.jquery.com/mobile/1.4.3/jquery.mobile-1.4.3.min.js"></script>
        
    </head>
    <body>
        
       <div data-role="page" class="type-interior" id="AdminBackupPathPageId">
            
            <div data-role="header" data-theme="a">
		<h1>vBoxManager</h1>
		<a href="vBoxJWS.jsp" rel="external" data-icon="home" data-iconpos="notext" data-direction="reverse">Home</a>
	</div><!-- /header -->
            
             <div role="main" class="ui-content">
                <div style="width:20%; float:left;">
                    <div data-role="listview" data-theme="c" data-dividertheme="d">
                        <!--li data-role="list-divider">Pages</li-->
                        <li><a href="vBoxJWS.jsp" rel="external" >Machines</a></li>
                    </div>
                    <p></p>
                    <div data-role="listview" data-theme="c" data-dividertheme="d">
                        <li data-role="list-divider" data-theme="a">Admin Options</li>
                        <li><a href="admin_Hosts.jsp">Hosts</a></li>
                        <li><a href="admin_BackupPath.jsp">Backup Paths</a></li>
                        <li><a href="vBoxJWS.jsp" rel="external">Machines</a></li>
                    </div>

                    <div id="machineListDiv" >
                    </div>
                </div>
		<!--/content-primary -->

                <div style="width:78%; float:left; margin-left:2%;">
                    <div id="contentDiv">
                        <h2>Add Backup Path</h2>
                        <p>Allows for paths to be added, for consistancy during backups</p>
                        
                         <form action="admin_BackupPath.jsp" method="post">
                            <label for="backupPath">Backup Path:</label>
                            <input type="text" name="backupPath" id="backupPath" value="" placeholder="//Some/shared/location" />
                            
                            <button type="submit" >Add Path</button>
                         </form>
                        
                        <h2>Saved Backup Paths</h2>
                        
                        <%
                            out.println(printBackupPaths());
                        %>
                    </div> 
                </div>
            </div>
            <div data-role="footer" class="footer-docs" data-theme="a">
               <p></p>
            </div>
                    
<script type="text/javascript">
    function refreshMachineList()
    {
        var ajax = $.ajax(
        {
            url:"vBoxJWS.jsp?data=rebuildManagerList",
            dataType: "json",
            type: "POST",
            async: false,//wait for the response
            data:
            {

            }
        });    

        ajax.done(function()
        {
             printManagedMachineList();
        });
    }
</script>

        </div>
      </body>
</html>

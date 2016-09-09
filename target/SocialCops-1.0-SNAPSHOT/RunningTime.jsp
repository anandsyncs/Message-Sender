<%-- 
    Document   : RunningTime.jsp
    Created on : Sep 8, 2016, 4:17:20 PM
    Author     : anand
--%>

<%@page import="CheckDatabase.Database"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Running Time</title>
    </head>
    <body>
        <h3>Running Time is:</h3>
        <%
            Database d=new Database();
            out.println(d.getRunningTime());
            out.flush();
            %>
    </body>
</html>

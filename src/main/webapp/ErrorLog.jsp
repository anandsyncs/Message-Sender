<%-- 
    Document   : ErrorLog.jsp
    Created on : Sep 8, 2016, 4:17:36 PM
    Author     : anand
--%>

<%@page import="CheckDatabase.Database"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Not Sent Messages</title>
    </head>
    <body>
        <h1>List of Not Sent Messages</h1>
        <%
            Database d=new Database();
            out.println(d.getNotSentMessages());
            out.flush();
        %>
    </body>
</html>

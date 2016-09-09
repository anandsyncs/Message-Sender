<%-- 
    Document   : LaunchThreads
    Created on : Sep 8, 2016, 11:53:18 AM
    Author     : anand
--%>

<%@page import="SendMessage.Message"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h2>Hourly SMS service running </h2>
        
        <a href="RunningTime.jsp" target="_blank">Show Running Time of Message Service</a>
        <br>
        <a href="ErrorLog.jsp" target="_blank"> Show Failed Messages </a>
        
        <%
//            JOptionPane.showMessageDialog(null, "Point");
            String number=request.getParameter("phone");
            int start=Integer.parseInt(request.getParameter("sleep_start"));
            int duration=Integer.parseInt(request.getParameter("sleep_duration"));
            Message m=new Message(start, duration, number);
            Thread t1=new Thread(m);
            
            t1.start();

            %>
       
        

    </body>
</html>

<%@page import="library.Database"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet"  type="text/css"  href="mycss.css"/>
<title>Login Check</title>
</head>
<body>

<%
	String username = request.getParameter("username");
	String password = request.getParameter("password");
	library.Database db = new library.Database();
	db.connect();
	int res = db.checkuser(username,password);
	System.out.println(res);
	if(res == 1){
		Cookie c = new Cookie("name",username);
		c.setMaxAge(3600);
		response.addCookie(c);
		db.disconnect();
		response.sendRedirect("studentindex.jsp");
	}
	else if(res == 2){
		Cookie c = new Cookie("name",username);
		c.setMaxAge(3600);
		response.addCookie(c);
		db.disconnect();
		response.sendRedirect("managerindex.jsp");
	}
	else{
		db.disconnect();
		response.sendRedirect("login.jsp?res=Login Failed!");
	}
%>

</body>
</html>
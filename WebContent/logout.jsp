<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet"  type="text/css"  href="mycss.css"/>
<title>Logout</title>
</head>
<body>
<%
	Cookie[] cookies = request.getCookies();
	for(int i=0;i<cookies.length;i++)
	{
		if(cookies[i].getName().equals("name"))
		{
			cookies[i].setMaxAge(0);
			response.addCookie(cookies[i]);
		}
	}
	response.sendRedirect("login.jsp?res=Logout!");
%>
</body>
</html>
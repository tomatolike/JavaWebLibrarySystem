<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet"  type="text/css"  href="mycss.css"/>
<title>Do Adding</title>
</head>
<body>
	<navigator>
		<h1>
			<%
				String name = "";
				Cookie[] cookies = request.getCookies();
				for(int i=0;i<cookies.length;i++)
				{
					if(cookies[i].getName().equals("name")){
						name = cookies[i].getValue();
						break;
					}
				}
				if(!name.equals(""))out.print("Welcome "+name+" !");
				else {
					response.sendRedirect("login.jsp?res=You Need To Login First!");
				}
			%>
		</h1>
		<button><a href="logout.jsp">Logout</a></button>
		<button><a href="managerindex.jsp">Index</a></button>
	</navigator>
	
	<%
		String bookname = request.getParameter("bookname");
		String number = request.getParameter("number");
		library.Database db = new library.Database();
		db.connect();
		if(db.addbook(bookname,Integer.parseInt(number)))
		{
			response.sendRedirect("addbook.jsp?res=Add Succeed!");
		}
		else
		{
			response.sendRedirect("addbook.jsp?res=Add Failed!");
		}
		db.disconnect();
	%>
</body>
</html>
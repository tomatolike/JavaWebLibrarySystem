<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet"  type="text/css"  href="mycss.css"/>
<title>Return Book</title>
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
		<button><a href="studentindex.jsp">Index</a></button>
	</navigator>
	<container>
		<table>
			<tr>
				<th>BookName</th>
				<th>BorrowTime</th>
				<th></th>
			</tr>
			<%
				library.Database db = new library.Database();
				db.connect();
				library.Borrow[] borrowlist = db.borrowlist(name); 
				
				int lenth = borrowlist.length;
				if(lenth>0){
					for(int i=0;i<lenth;i++)
					{
						%>
						<tr>
							<td><% out.print(borrowlist[i].bookname); %></td>
							<td><% out.print(borrowlist[i].time); %></td>
							<td><button><a href = "doreturn.jsp?bid=<% out.print(borrowlist[i].bid); %>">Return</a></button></td>
						</tr>
						<%
					}
				}
				db.disconnect();
			%>
		</table>
		<alert>
		<%
			String res = (String)request.getParameter("res");
			out.print(res);
		%>
		</alert>
	</container>
	
</body>
</html>
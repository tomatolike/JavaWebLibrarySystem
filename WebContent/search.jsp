<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet"  type="text/css"  href="mycss.css"/>
<title>Search Books</title>
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
		<form action="search.jsp" method="post">
			<table>
				<tr>
					<td>Enter the name of the book:</td>
					<td><input type="text" name="bookname"></td>
					<td><input type="submit" value="Search"></td>
				</tr>
				<tr>
					<th>Name</th>
					<th>Number</th>
					<th></th>
				</tr>
				<%
					String bookname = request.getParameter("bookname");
					if(!(bookname == null))
					{
						library.Database db = new library.Database();
						db.connect();
						library.Book[] booklist = db.searchbook(bookname);
						System.out.println("dam!");
						int lenth = booklist.length;
						System.out.println(booklist.length);
						library.Book book = booklist[0];
						if(book==null)System.out.println("fuck");
						else System.out.println(book.number);
						if(lenth>0){
							for(int j=0;j<lenth;j++)
							{
								%>
								<tr>
									<td><% out.print(booklist[j].name); %></td>
									<td><% out.print(booklist[j].number); %></td>
									<td><button><a href = "borrow.jsp?bid=<% out.print(booklist[j].bid); %>">Borrow</a></button></td>
								</tr>
								<%
							}
						}
						db.disconnect();
					}
				%>
			</table>
		</form>
		<alert>
		<%
			String res = (String)request.getParameter("res");
			out.print(res);
		%>
		</alert>
	</container>
	
</body>
</html>
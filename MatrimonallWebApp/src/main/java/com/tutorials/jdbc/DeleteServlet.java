package com.tutorials.jdbc;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.tutorials.jdbc.bo.Person;
import com.tutorials.jdbc.dao.PersonDAO;

/**
 * Servlet implementation class ListServlet
 */
@WebServlet(
		description = "A Servlet to list the data from the database", 
		urlPatterns = { 
				"/DeleteServlet",
				"/Delete"
		})
public class DeleteServlet extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException
	{
		System.out.println("DeleteServlet - doGet() invoked");
		
		// 1. Get the data from Database 
		try {
			PersonDAO.connectToDB();
			System.out.println("DB Connection has been obtained");
		} catch (Exception e) {
			System.err.println("Exception occurred while connecting to the Database");
			System.err.println("Error Message : " + e.getMessage());
			//TODO: Remove this later, as this is not a good practice
			e.printStackTrace();
			
			throw new ServletException(e.getMessage());
		}
		
		int id =1;
		String idStr = request.getParameter("id");
		
		if(null!=idStr && idStr.trim().length()>0) {
			id = Integer.parseInt(idStr);
		}
		
		System.out.println("Id Parameter from the Request : " + id);
		
		int rowsDeleted = PersonDAO.deletePersonById(id);
		String message = "Record not deleted sucessfully!";
		String flag = "false";
		
		if(rowsDeleted > 0)  {
			flag = "true";
			message = "Record deleted sucessfully!";
		}
		
		
		List<Person> personList = PersonDAO.listAll();
		
		System.out.println("personList from DAO : " + personList);
		
		// 2. Store it in a way where the data is accessible in the JSP
		request.setAttribute("personList", personList);
		request.setAttribute("flag", flag);
		request.setAttribute("message", message);
		
		// 3. Forward / Delegate the control/flow the required JSP Page
		request.getRequestDispatcher("list.jsp").forward(request, response);
	}
}
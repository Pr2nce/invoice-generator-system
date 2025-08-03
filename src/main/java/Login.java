
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
@WebServlet("/Login")
public class Login extends HttpServlet {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter(); //Pass4@oracle
        String myemail = req.getParameter("email1");
        String mypass = req.getParameter("pass1");

        try {
            // Check for null values
            if (myemail == null || mypass == null) {
                out.println("<h3 style='color:red'>Invalid email or password</h3>");
                RequestDispatcher rd = req.getRequestDispatcher("/Login.jsp");
                rd.include(req, resp);
                return;
            }

            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/javaProject", "root", "prince");

            PreparedStatement ps = con.prepareStatement("select * from register where email = ? and password = ?");
            ps.setString(1, myemail);
            ps.setString(2, mypass);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
            	// Valid user, create a session
            	HttpSession session = req.getSession();
            	session.setAttribute("username", myemail);  

            	// Redirect to FetchItemsServlet
            	resp.sendRedirect("FetchItemsServlet");
            } else {
                out.println("<h3 style='color:red'>Email id and password didn't match</h3>");
                RequestDispatcher rd = req.getRequestDispatcher("/Login.jsp");
                rd.include(req, resp);
            }
        } catch (Exception e) {
            e.printStackTrace();  // Print the stack trace for debugging
            out.println("<h3 style='color:red'>Exception: " + e.getMessage() + "</h3>");
            RequestDispatcher rd = req.getRequestDispatcher("/Login.jsp");
            rd.include(req, resp);
        }
    }
}
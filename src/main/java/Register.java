
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/Register")
public class Register extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();

        String myemail = req.getParameter("email");
        String mypass = req.getParameter("password");
        String confirmPassword = req.getParameter("confirmPassword");

        try {
            // Check for null values
            if (myemail == null || mypass == null || confirmPassword == null) {
                out.println("<h3 style='color:red'>Invalid input</h3>");
                RequestDispatcher rd = req.getRequestDispatcher("/Register.jsp");
                rd.include(req, resp);
                return;
            }

            if (!mypass.equals(confirmPassword)) {
                out.println("<h3 style='color:red'>Password and Confirm Password do not match</h3>");
                RequestDispatcher rd = req.getRequestDispatcher("/Register.jsp");
                rd.include(req, resp);
                return;
            }

            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/javaProject", "root", "prince");

            PreparedStatement ps = con.prepareStatement("insert into register (email, password) values (?, ?)");
            ps.setString(1, myemail);
            ps.setString(2, mypass);

            int result = ps.executeUpdate();
            if (result > 0) {
                out.println("<h3 style='color:green'>Registration successful</h3>");
                // Redirect to login page after successful registration
                resp.sendRedirect("Login.jsp");
            } else {
                out.println("<h3 style='color:red'>Registration failed</h3>");
                RequestDispatcher rd = req.getRequestDispatcher("/Register.jsp");
                rd.include(req, resp);
            }
        } catch (Exception e) {
            e.printStackTrace();
            out.println("<h3 style='color:red'>Exception: " + e.getMessage() + "</h3>");
            RequestDispatcher rd = req.getRequestDispatcher("/Register.jsp");
            rd.include(req, resp);
        }
    }
}


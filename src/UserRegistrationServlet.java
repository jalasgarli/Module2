import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@WebServlet("/register")
public class RegistrationServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        try {
            // Hash the password - this example does not actually hash for brevity
            String hashedPassword = password; // You should hash the password

            // Database connection
            Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost/yourdatabase", "user", "password");

            String sql = "INSERT INTO users (username, email, hashed_password) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            statement.setString(2, email);
            statement.setString(3, hashedPassword);

            int result = statement.executeUpdate();
            if (result > 0) {
                response.getWriter().println("User registered successfully");
            } else {
                response.getWriter().println("User registration failed");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("Error during user registration");
        }
    }
}

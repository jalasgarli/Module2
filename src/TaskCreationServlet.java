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

@WebServlet("/createTask")
public class CreateTaskServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String description = request.getParameter("description");
        int userId = Integer.parseInt(request.getParameter("userId")); // Assume the user's ID is passed; in a real app, get this from session

        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost/yourdatabase", "user", "password")) {
            String sql = "INSERT INTO tasks (user_id, description) VALUES (?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, userId);
                statement.setString(2, description);

                int result = statement.executeUpdate();
                if (result > 0) {
                    response.getWriter().println("Task created successfully");
                } else {
                    response.getWriter().println("Failed to create task");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("Database error occurred");
        }
    }
}

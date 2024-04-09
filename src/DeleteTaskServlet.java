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

@WebServlet("/deleteTask")
public class DeleteTaskServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int taskId = Integer.parseInt(request.getParameter("taskId"));
        int userId = Integer.parseInt(request.getParameter("userId")); // Security check to ensure user owns the task

        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost/yourdatabase", "user", "password")) {
            String sql = "DELETE FROM tasks WHERE id = ? AND user_id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, taskId);
                statement.setInt(2, userId);

                int result = statement.executeUpdate();
                if (result > 0) {
                    response.getWriter().println("Task deleted successfully");
                } else {
                    response.getWriter().println("Failed to delete task");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("Database error occurred");
        }
    }
}

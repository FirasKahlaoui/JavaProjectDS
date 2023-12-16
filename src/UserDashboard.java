import javax.swing.*;
import java.sql.*;

public class UserDashboard {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/club_management";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        JFrame frame = new JFrame("Club Managing System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        if (args.length == 1) {
            String userEmail = args[0];
            if (loadUserData(userEmail)) {
                SpecialityChoice.main(new String[] { userEmail });
            } else {
                UserDash.main(new String[] { userEmail });
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Invalid arguments.");
        }
    }

    private static boolean loadUserData(String userEmail) {
        try {
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            String sql = "SELECT Age, Speciality FROM user WHERE Mail_Address = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, userEmail);
                var rs = stmt.executeQuery();
                if (rs.next()) {
                    int age = rs.getInt("Age");
                    String speciality = rs.getString("Speciality");
                    return age == 0 && speciality == null;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}

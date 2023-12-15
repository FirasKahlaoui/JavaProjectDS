import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class ShowMemberSpeciality {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Admin : Show Members by Speciality");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 240);
        frame.getContentPane().setBackground(Color.WHITE);

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        JLabel title = new JLabel("Select a speciality");
        title.setFont(new Font("Arial", Font.BOLD, 14));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setForeground(Color.BLACK);
        title.setBackground(Color.WHITE);
        String[] specialities = {
                "-- Select a speciality --",
                "Big Data Analytics",
                "Full Stack Development",
                "Machine Learning and Artificial Intelligence",
                "Cybersecurity",
                "Cloud Computing",
                "Blockchain Technology"
        };
        JComboBox<String> specialityComboBox = new JComboBox<>(specialities);
        specialityComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        specialityComboBox.setBackground(Color.WHITE);

        JButton backButton = createStyledButton("Back");
        JButton showMembers = createStyledButton("Show Members");

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AdminDashboard.main(new String[] {});
                frame.dispose();
            }
        });

        showMembers.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedSpeciality = (String) specialityComboBox.getSelectedItem();
                if (selectedSpeciality.equals("-- Select a speciality --")) {
                    JOptionPane.showMessageDialog(frame, "Please select a speciality");
                } else {
                    try {
                        Connection connection = DriverManager.getConnection(
                                "jdbc:mysql://localhost:3306/club_management", "root", "");
                        PreparedStatement ps = connection.prepareStatement(
                                "SELECT Name, LastName, Mail_Address, Age FROM user WHERE Speciality = ?");
                        ps.setString(1, selectedSpeciality);
                        ResultSet rs = ps.executeQuery();

                        int counter = 1;
                        JPanel panel = new JPanel();
                        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
                        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

                        while (rs.next()) {
                            JPanel memberPanel = new JPanel();
                            memberPanel.setLayout(new GridLayout(6, 1));
                            memberPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

                            JLabel titleLabel = new JLabel("Member " + counter);
                            titleLabel.setFont(new Font("Arial", Font.BOLD, 16));

                            JLabel nameLabel = new JLabel("Name: " + rs.getString("Name"));
                            nameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
                            JLabel lastNameLabel = new JLabel("Last Name: " + rs.getString("LastName"));
                            lastNameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
                            JLabel emailLabel = new JLabel("Email: " + rs.getString("Mail_Address"));
                            emailLabel.setFont(new Font("Arial", Font.PLAIN, 14));
                            JLabel ageLabel = new JLabel("Age: " + rs.getInt("Age"));
                            ageLabel.setFont(new Font("Arial", Font.PLAIN, 14));

                            JButton doneButton = createStyledButton("Done");
                            doneButton.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    ShowMemberSpeciality.main(new String[] {});
                                    frame.dispose();
                                }
                            });

                            memberPanel.add(titleLabel);
                            memberPanel.add(nameLabel);
                            memberPanel.add(lastNameLabel);
                            memberPanel.add(emailLabel);
                            memberPanel.add(ageLabel);
                            memberPanel.add(doneButton);

                            panel.add(memberPanel);
                            counter++;
                        }

                        JScrollPane scrollPane = new JScrollPane(panel,
                                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

                        JFrame infoFrame = new JFrame("Member Information");
                        infoFrame.setSize(400, 240);
                        infoFrame.add(scrollPane);
                        infoFrame.setVisible(true);

                        connection.close();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        panel.add(title);
        panel.add(specialityComboBox);
        panel.add(showMembers);
        panel.add(backButton);

        frame.add(panel);
        frame.setVisible(true);

    }

    private static JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setBackground(Color.WHITE);
        button.setForeground(Color.BLACK);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setPreferredSize(new Dimension(100, 50));
        return button;
    }
}

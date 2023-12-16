import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class ShowMemberClub {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Admin : Show Members by Club");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 240);
        frame.getContentPane().setBackground(Color.WHITE);

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        JLabel title = new JLabel("Select a Club");
        title.setFont(new Font("Arial", Font.BOLD, 14));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setForeground(Color.BLACK);
        title.setBackground(Color.WHITE);
        String[] clubs = {
                "-- Select a club --",
                "Coding Wizards",
                "Green Earth Society",
                "Musical Harmony",
                "Fitness Fanatics",
                "Bookworms Club",
                "Innovation Hub",
                "Dance Fusion",
                "Sports Unlimited",
                "Nature Explorers",
                "Film Buffs Society"
        };
        JComboBox<String> clubComboBox = new JComboBox<>(clubs);
        clubComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        clubComboBox.setBackground(Color.WHITE);

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
                String selectClub = (String) clubComboBox.getSelectedItem();
                int selectClubIndex = clubComboBox.getSelectedIndex();
                if (selectClub.equals("-- Select a club --")) {
                    JOptionPane.showMessageDialog(frame, "Please select a club.");
                } else {
                    try {
                        Connection connection = DriverManager.getConnection(
                                "jdbc:mysql://localhost:3306/club_management", "root", "");
                        PreparedStatement ps = connection.prepareStatement(
                                "SELECT CIN FROM participate WHERE NumClub = ?");
                        ps.setInt(1, selectClubIndex);
                        ResultSet rs = ps.executeQuery();

                        int counter = 1;
                        JPanel panel = new JPanel();
                        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
                        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

                        while (rs.next()) {
                            PreparedStatement psUser = connection.prepareStatement(
                                    "SELECT Name, LastName, Mail_Address, Age , Speciality FROM user WHERE CIN = ?");
                            psUser.setString(1, rs.getString("CIN"));
                            ResultSet rsUser = psUser.executeQuery();

                            if (rsUser.next()) {
                                JPanel memberPanel = new JPanel();
                                memberPanel.setLayout(new GridLayout(7, 1));
                                memberPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

                                JLabel titleLabel = new JLabel("Member " + counter);
                                titleLabel.setFont(new Font("Arial", Font.BOLD, 16));

                                JLabel nameLabel = new JLabel("Name: " + rsUser.getString("Name"));
                                nameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
                                JLabel lastNameLabel = new JLabel("Last Name: " + rsUser.getString("LastName"));
                                lastNameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
                                JLabel emailLabel = new JLabel("Email: " + rsUser.getString("Mail_Address"));
                                emailLabel.setFont(new Font("Arial", Font.PLAIN, 14));
                                JLabel ageLabel = new JLabel("Age: " + rsUser.getInt("Age"));
                                ageLabel.setFont(new Font("Arial", Font.PLAIN, 14));
                                JLabel specialityLabel = new JLabel("Speciality: " + rsUser.getString("Speciality"));
                                specialityLabel.setFont(new Font("Arial", Font.PLAIN, 14));

                                memberPanel.add(titleLabel);
                                memberPanel.add(nameLabel);
                                memberPanel.add(lastNameLabel);
                                memberPanel.add(emailLabel);
                                memberPanel.add(ageLabel);
                                memberPanel.add(specialityLabel);

                                panel.add(memberPanel);
                                counter++;
                            }

                        }
                        JButton doneButton = createStyledButton("Done");
                        doneButton.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                ShowMemberClub.main(new String[] {});
                                frame.dispose();
                            }
                        });
                        doneButton
                                .setMaximumSize(new Dimension(Integer.MAX_VALUE, doneButton.getPreferredSize().height));
                        panel.add(doneButton);
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
        panel.add(clubComboBox);
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

package isw.ui;

import isw.controler.ConexionesControler;
import isw.domain.Customer;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.AbstractBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class UserProfileForm extends JFrame implements ActionListener {

    private int numberOfPlaylists = 10;
    private int followersCount = 120;
    private int followingCount = 35;
    private String nombreUsuario;
    private ArrayList<String> topArtists = new ArrayList<>();
    private ArrayList<String> topTracks = new ArrayList<>();
    private ArrayList<String> playlists = new ArrayList<>();

    private int idLogged;
    private JPanel followersPanel;
    private JPanel followingPanel;

    public UserProfileForm(int idLogged) {

        this.idLogged = idLogged;

        setTitle("User Profile");
        setSize(800, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(18, 18, 18));

        // Top Panel for Profile Info
        JPanel profilePanel = new JPanel(new BorderLayout());
        profilePanel.setBackground(new Color(18, 18, 18));

        // Profile header with "Profile" text above the username
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(new Color(18, 18, 18));

        JLabel profileLabel = new JLabel("Profile");
        profileLabel.setForeground(Color.LIGHT_GRAY);
        profileLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        //mover a la derecha y añadir espacio
        profileLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        headerPanel.add(Box.createVerticalStrut(20)); // Space above the label
        headerPanel.add(profileLabel);

        //avatares
        JLabel avatarLabel = new JLabel();
        avatarLabel.setOpaque(true);
        avatarLabel.setBackground(Color.GRAY);
        avatarLabel.setPreferredSize(new Dimension(100, 100));
        avatarLabel.setHorizontalAlignment(SwingConstants.CENTER);
        avatarLabel.setVerticalAlignment(SwingConstants.CENTER);
        avatarLabel.setBorder(createCircleBorder(Color.WHITE));

        //añadir avatar
        headerPanel.add(profileLabel);
        headerPanel.add(Box.createVerticalStrut(10));
        headerPanel.add(avatarLabel);
        headerPanel.add(Box.createVerticalStrut(10));

        //nombre de usuario
        JLabel profileNameLabel = new JLabel(nombreUsuario);
        profileNameLabel.setFont(new Font("Arial", Font.BOLD, 36));
        profileNameLabel.setForeground(Color.WHITE);

        headerPanel.add(profileNameLabel);

        JLabel profileInfoLabel = new JLabel(numberOfPlaylists + " Saved Albums · " + followersCount + " Followers · " + followingCount + " Following");
        profileInfoLabel.setForeground(Color.LIGHT_GRAY);

        profilePanel.add(headerPanel, BorderLayout.NORTH);
        profilePanel.add(profileInfoLabel, BorderLayout.SOUTH);

        // Main panels for followers and following
        followersPanel = createListPanel("Followers", Color.LIGHT_GRAY);
        followingPanel = createListPanel("Following", Color.CYAN);

        loadFollowersAndFollowing();

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(new Color(18, 18, 18));
        mainPanel.add(profilePanel);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(followersPanel);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(followingPanel);

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createListPanel(String title, Color titleColor) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel(title);
        titleLabel.setForeground(titleColor);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(titleLabel, BorderLayout.NORTH);

        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(Color.DARK_GRAY);

        panel.add(listPanel, BorderLayout.CENTER);
        return panel;
    }

    private void loadFollowersAndFollowing() {
        ConexionesControler conexionesControler = new ConexionesControler();

        // Load followers
        List<Customer> followers = conexionesControler.getMisSeguidores(idLogged);
        JPanel followersList = (JPanel) followersPanel.getComponent(1);
        addCustomersToPanel(followers, followersList);

        // Load following
        List<Customer> following = conexionesControler.getMisSeguidos(idLogged);
        JPanel followingList = (JPanel) followingPanel.getComponent(1);
        addCustomersToPanel(following, followingList);
    }

    private void addCustomersToPanel(List<Customer> customers, JPanel panel) {
        panel.removeAll();
        int count = 0;

        for (Customer customer : customers) {
            if (count >= 4) break;

            JButton customerButton = new JButton(customer.getNombre());
            customerButton.setAlignmentX(Component.LEFT_ALIGNMENT);
            customerButton.addActionListener(e -> viewProfile(customer));

            panel.add(customerButton);
            panel.add(Box.createVerticalStrut(5));

            count++;
        }

        panel.revalidate();
        panel.repaint();
    }

    private void viewProfile(Customer customer) {
        JOptionPane.showMessageDialog(this, "Profile of: " + customer.getNombre(), "Profile", JOptionPane.INFORMATION_MESSAGE);
    }

    public Border createCircleBorder(Color color) {
        return new AbstractBorder() {
            @Override
            public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
                g.setColor(color);
                g.fillOval(x, y, width, height);
            }
        };
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Functionality for buttons
    }
}

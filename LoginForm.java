
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;

public class LoginForm extends JFrame implements ActionListener {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton signUpButton;
    private String username = "admin";
    private String password = "password";

    public LoginForm() {
        setTitle("Login Detail");
        setSize(500, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(5, 2));

        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField();
        loginButton = new JButton("Login");
        signUpButton = new JButton("Sign Up");

        usernameLabel.setForeground(Color.BLUE);
        passwordLabel.setForeground(Color.BLUE);
        loginButton.setBackground(Color.DARK_GRAY);
        loginButton.setForeground(Color.WHITE);
        signUpButton.setBackground(Color.DARK_GRAY);
        signUpButton.setForeground(Color.WHITE);
        add(usernameLabel);
        add(usernameField);
        add(passwordLabel);
        add(passwordField);
        add(new JLabel());
        add(new JLabel());
        add(loginButton);
        add(signUpButton);

        loginButton.addActionListener(this);
        signUpButton.addActionListener(this);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton) {
            String enteredUsername = usernameField.getText();
            String enteredPassword = new String(passwordField.getPassword());

            if (enteredUsername.equals(username) && enteredPassword.equals(password)) {
                JOptionPane.showMessageDialog(this, "Login Successful!");

                String[] options = {"Play Game", "Show Database"};
                int choice = JOptionPane.showOptionDialog(this, "Play or show:", "Options",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

                if (choice == 0) {
                    playGame();
                } else if (choice == 1) {
                    showDatabase();
                }
            } 
            else {
                JOptionPane.showMessageDialog(this, "Invalid username or password. Please try again.");
            }
        }  
        else if (e.getSource() == signUpButton) {
            signUp();
        }
        
    }

    private void playGame() {

        ShapeColor shapeColor = new ShapeColor();
        shapeColor.setVisible(true);
    }
private void showDatabase() {
    String enteredPassword = JOptionPane.showInputDialog(this, "Enter password:");

    if (enteredPassword != null && enteredPassword.equals("k2203")) { // Check if the entered password is correct
        StringBuilder message = new StringBuilder();
        String url = "jdbc:sqlite:test.db";

        try (Connection conn = DriverManager.getConnection(url); Statement stmt = conn.createStatement()) {
            String query = "SELECT * FROM Login";
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                int userId = rs.getInt("id");
                String username = rs.getString("username");
                String email = rs.getString("email");
                message.append("User ID: ").append(userId).append(", Username: ").append(username)
                        .append(", Email: ").append(email).append("\n");
            }

            if (message.length() == 0) {
                message.append("No users found in the database.");
            }

            JOptionPane.showMessageDialog(this, message.toString(), "Database Contents", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error retrieving data from database: " + ex.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    } else {
        JOptionPane.showMessageDialog(this, "Incorrect password. Access denied.");
    }
}

    private void signUp() {
        String username = JOptionPane.showInputDialog(this, "Enter username:");
        String password = JOptionPane.showInputDialog(this, "Enter password:");
        String email = JOptionPane.showInputDialog(this, "Enter email:");

        if (username != null && !username.isEmpty()
                && password != null && !password.isEmpty()
                && email != null && !email.isEmpty()) {
            boolean signUpSuccessful = saveUserToDatabase(username, password, email);

            if (signUpSuccessful) {
                JOptionPane.showMessageDialog(this, "Sign up successful!");

                this.username = username;
                this.password = password;
            } else {
                JOptionPane.showMessageDialog(this, "Sign up failed. Please try again later.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please fill out all fields.");
        }
    }

    private boolean saveUserToDatabase(String username, String password, String email) {
        String url = "jdbc:sqlite:test.db";

        try (Connection conn = DriverManager.getConnection(url); Statement stmt = conn.createStatement()) {

            String createTableSQL = "CREATE TABLE IF NOT EXISTS Login ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "username TEXT NOT NULL,"
                    + "password TEXT NOT NULL,"
                    + "email TEXT NOT NULL)";
            stmt.execute(createTableSQL);

            String insertSQL = "INSERT INTO Login(username, password, email) VALUES (?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(insertSQL);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, email);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error saving user to database: " + ex.getMessage());
            return false;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginForm::new);
    }
}

class ShapeColor extends JFrame implements ActionListener, WindowListener {

    public JLabel imageLabel;
    public Label l;

    ShapeColor() {
        super("DrawPlay");
        ImageIcon imageIcon = new ImageIcon("C:\\Users\\Personal\\Downloads\\drawing-appllication-for-kids-for-my-java-project-upscaled.png");
        Image image = imageIcon.getImage().getScaledInstance(1600, 1600, Image.SCALE_DEFAULT);
        imageIcon = new ImageIcon(image);
        imageLabel = new JLabel(imageIcon);

        l = new Label("DrawPlay \u2665");
        l.setFont(new Font("Arial", Font.BOLD, 56));

        Button playButton = new Button("Click Me");
        playButton.setForeground(Color.red);
        playButton.setBounds(322, 199, 80, 90);
        playButton.setBackground(Color.black);
        playButton.setFont(new Font("Arial", Font.BOLD, 26));
        playButton.addActionListener(this);

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(imageLabel, BorderLayout.CENTER);
        contentPanel.add(l, BorderLayout.NORTH);
        contentPanel.add(playButton, BorderLayout.SOUTH);

        setLayout(new BorderLayout());
        add(contentPanel, BorderLayout.CENTER);

        addWindowListener(this);

        setBackground(Color.black);
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);
    }

    @Override
    public void windowOpened(WindowEvent e) {
    }

    @Override
    public void windowClosing(WindowEvent e) {
        int result = JOptionPane.showConfirmDialog(this, "EXIT ?", "Are YOU SURE", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.NO_OPTION) {
            ShapeColorF ShapeColorF = new ShapeColorF();
        } else if (result == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    @Override
    public void windowClosed(WindowEvent e) {
    }

    @Override
    public void windowIconified(WindowEvent e) {
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
    }

    @Override
    public void windowActivated(WindowEvent e) {
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
    }

    public static void main(String args[]) {
        new ShapeColor();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ShapeColorF puzelFrame = new ShapeColorF();
        puzelFrame.setSize(1600, 1600);
        puzelFrame.setBackground(Color.black);
        puzelFrame.setVisible(true);

    }
}

class ShapeColorF extends JFrame implements ActionListener, ListSelectionListener, WindowListener {

    JList c, s;
    JPanel drawPanel;
    JSplitPane p;
    JSlider js;
    private boolean fillShape = false;
    int x = 0, y = 0;
    boolean isDrawing = false;
    Color currentColor = Color.BLACK;
    String currentShape = "Rectangle";
    public Timer timer;
    public JLabel timerLabel;
    public int secondsRemaining;
    private JButton colorPickerButton;
    java.util.List<ShapeProperties> shapesList = new ArrayList<>();
    private java.util.List<ShapeProperties> undoList = new ArrayList<>();
    private java.util.List<ShapeProperties> redoList = new ArrayList<>();

    ShapeColorF() {
        super("Your Paint");
        String shapes[] = {"Rectangle", "Oval", "Line", "Triangle", "Circle", "Eraser", "Star", "Smile", "Heart", "ArrowUp", "ArrowDown", "ArrowLeft", "ArrowRight", "Freehand"};

        String colors[] = {"red", "blue", "green", "yellow", "pink", "voilet", "grey", "orange", "black", "white", "cyan", "magenta"};
        Menu file, sub;
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem newMenuItem = new JMenuItem("New");
        JMenuItem exitMenuItem = new JMenuItem("Exit");
        JMenu gameMenu = new JMenu("Game");

        JMenuItem GameMenuItem = new JMenuItem("Task");
        JMenuItem SaveMenuItem = new JMenuItem("Save");

        newMenuItem.addActionListener(this);
        exitMenuItem.addActionListener(this);
        GameMenuItem.addActionListener(this);
        menuBar.add(gameMenu);
        fileMenu.add(newMenuItem);
        fileMenu.add(exitMenuItem);
        gameMenu.add(GameMenuItem);
       
        fileMenu.add(SaveMenuItem);
        JMenuItem openMenuItem = new JMenuItem("Open");
        openMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String actionCommand = e.getActionCommand();

                if (actionCommand.equals("Open")) {
                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setDialogTitle("Open File");
                    fileChooser.setFileFilter(new FileNameExtensionFilter("Image Files", "png", "jpg", "jpeg"));
                    int userSelection = fileChooser.showOpenDialog(ShapeColorF.this); // Change "this" to your frame name

                    if (userSelection == JFileChooser.APPROVE_OPTION) {
                        File selectedFile = fileChooser.getSelectedFile();
                        try {
                            BufferedImage originalImage = ImageIO.read(selectedFile);
                            ImageIcon imageIcon = new ImageIcon(originalImage);
                            JLabel imageLabel = new JLabel(imageIcon);
                            drawPanel.add(imageLabel);
                            drawPanel.revalidate();
                            drawPanel.repaint();

                            JButton increaseSizeButton = new JButton("+");
                            increaseSizeButton.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {

                                    int currentWidth = imageLabel.getWidth();
                                    int currentHeight = imageLabel.getHeight();
                                    int newWidth = (int) (currentWidth * 1.11);
                                    int newHeight = (int) (currentHeight * 1.11);
                                    imageLabel.setSize(newWidth, newHeight);
                                    imageLabel.setIcon(new ImageIcon(originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH)));
                                    drawPanel.revalidate();
                                    drawPanel.repaint();
                                }
                            });

                            drawPanel.add(increaseSizeButton);

                            JButton decreaseSizeButton = new JButton("-");
                            decreaseSizeButton.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {

                                    int currentWidth = imageLabel.getWidth();
                                    int currentHeight = imageLabel.getHeight();
                                    int newWidth = (int) (currentWidth * 0.19);
                                    int newHeight = (int) (currentHeight * 0.19);
                                    imageLabel.setSize(newWidth, newHeight);
                                    imageLabel.setIcon(new ImageIcon(originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH)));
                                    drawPanel.revalidate();
                                    drawPanel.repaint();
                                }
                            });

                            drawPanel.add(decreaseSizeButton);
                        } catch (IOException ex) {
                            JOptionPane.showMessageDialog(ShapeColorF.this, "Error opening file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        });
        fileMenu.add(openMenuItem);

        menuBar.add(fileMenu);
        SaveMenuItem.addActionListener(this);
        fileMenu.add(SaveMenuItem);
        setJMenuBar(menuBar);

        c = new JList(colors);
        c.setSelectedIndex(0);
        c.addListSelectionListener(this);
        JScrollPane colorScrollPane = new JScrollPane(c);

        s = new JList(shapes);
        s.setSelectedIndex(0);
        s.addListSelectionListener(this);
        JScrollPane shapeScrollPane = new JScrollPane(s);

        js = new JSlider(0, 10, 5);
        js.setMajorTickSpacing(1);
        js.setMinorTickSpacing(0);

        timerLabel = new JLabel("Timer: ");
        timer = new Timer(1000, new TimerListener());

        JPanel controlPanel = new JPanel(new FlowLayout());
        controlPanel.setBackground(Color.LIGHT_GRAY);
        controlPanel.setPreferredSize(new Dimension(40, 60));

        timerLabel = new JLabel("Timer: ");
        timerLabel.setForeground(Color.BLUE);
        controlPanel.add(timerLabel);

        colorPickerButton = new JButton("More Color");
        colorPickerButton.setForeground(Color.BLUE);
        colorPickerButton.addActionListener(this);
        controlPanel.add(colorPickerButton);

        JButton undoButton = new JButton("Undo");
        undoButton.setForeground(Color.BLUE);
        undoButton.addActionListener(this);
        controlPanel.add(undoButton);

        JButton redoButton = new JButton("Redo");
        redoButton.setForeground(Color.BLUE);
        redoButton.addActionListener(this);
        controlPanel.add(redoButton);

        JButton completeButton = new JButton("Complete");
        completeButton.setForeground(Color.BLUE);
        completeButton.addActionListener(this);
        controlPanel.add(completeButton);

        JButton fillButton = new JButton("Fill Shapes");
        fillButton.setForeground(Color.BLUE);
        fillButton.addActionListener(this);
        controlPanel.add(fillButton);

        JButton clearScreenButton = new JButton("Clear Screen");
        clearScreenButton.setForeground(Color.BLUE);
        clearScreenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Clear the drawing area
                shapesList.clear();
                drawPanel.repaint();
            }
        });
        controlPanel.add(clearScreenButton); // Add the button to the controlPanel

        drawPanel = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);

                for (ShapeProperties shapeProps : shapesList) {
                    g.setColor(shapeProps.color);

                    int startX = shapeProps.x;
                    int startY = shapeProps.y;
                    int endX = shapeProps.endX;
                    int endY = shapeProps.endY;
                    int width = endX - startX;
                    int height = endY - startY;

                    if (fillShape) {
                        switch (shapeProps.shape) {
                            case "Rectangle":
                                g.fillRect(startX, startY, width, height);
                                break;
                            case "Oval":
                                g.fillOval(startX, startY, width, height);
                                break;
                            case "Line":
                                g.drawLine(startX, startY, endX, endY);
                                break;
                            case "Triangle":
                                int[] xPoints = {startX, startX + (width / 2), startX + width};
                                int[] yPoints = {startY + height, startY, startY + height};
                                g.fillPolygon(xPoints, yPoints, 3);
                                break;
                            case "Circle":
                                g.fillOval(startX, startY, width, height);
                                break;
                            case "Eraser":
                                g.fillRect(startX, startY, width, height);
                                break;
                            case "Heart":

                                float beX = startX + width / 2;
                                float beY = startY + height;

                                float c1DX = width * 0.968f;
                                float c1DY = height * 0.672f;
                                float c2DX = width * 0.281f;
                                float c2DY = height * 1.295f;
                                float teDY = height * 0.850f;

                                Path2D.Float heartPath = new Path2D.Float();
                                heartPath.moveTo(beX, beY);

                                heartPath.curveTo(
                                        beX - c1DX, beY - c1DY,
                                        beX - c2DX, beY - c2DY,
                                        beX, beY - teDY);

                                heartPath.curveTo(
                                        beX + c2DX, beY - c2DY,
                                        beX + c1DX, beY - c1DY,
                                        beX, beY);

                                Graphics2D g2d = (Graphics2D) g;
                                g2d.setColor(shapeProps.color);
                                g2d.fill(heartPath);
                                break;

                            case "Smile":

                                int smileWidth = width;
                                int smileHeight = height;

                                g.fillOval(startX, startY, smileWidth, smileHeight);
                                g.setColor(Color.BLACK);
                                g.fillOval(startX + smileWidth / 4, startY + smileHeight / 4, smileWidth / 8, smileHeight / 8);
                                g.fillOval(startX + smileWidth / 2, startY + smileHeight / 4, smileWidth / 8, smileHeight / 8);
                                g.fillOval(startX + smileWidth / 3, startY + smileHeight / 2, smileWidth / 10, smileHeight / 10);
                                g.fillArc(startX + smileWidth / 4, startY + smileHeight / 2, smileWidth / 2, smileHeight / 4, 180, 180);
                                break;
                            case "Star":
                                int cx = startX + width / 2;
                                int cy = startY + height / 2;
                                int outerRadius = Math.min(width, height) / 2;
                                int innerRadius = outerRadius / 2;
                                int numPoints = 5;

                                double angle = 2 * Math.PI / numPoints;

                                Path2D path = new Path2D.Double();

                                for (int i = 0; i < numPoints * 2; i++) {
                                    double radius = (i % 2 == 0) ? outerRadius : innerRadius;
                                    double x = cx + radius * Math.cos(i * angle);
                                    double y = cy + radius * Math.sin(i * angle);

                                    if (i == 0) {
                                        path.moveTo(x, y);
                                    } else {
                                        path.lineTo(x, y);
                                    }
                                }

                                path.closePath();

                                ((Graphics2D) g).fill(path);

                                break;

                            case "ArrowUp":

                                g.fillRect(startX + width / 4, startY + height / 4, width / 2, height / 2);

                                int[] xPointsUp = {startX, startX + (width / 2), startX + width};
                                int[] yPointsUp = {startY + height / 4, startY, startY + height / 4};
                                g.fillPolygon(xPointsUp, yPointsUp, 3);
                                break;
                            case "ArrowDown":

                                g.fillRect(startX + width / 4, startY + height / 4, width / 2, height / 2);

                                int[] xPointsDown = {startX, startX + (width / 2), startX + width};
                                int[] yPointsDown = {startY + 3 * height / 4, startY + height, startY + 3 * height / 4};
                                g.fillPolygon(xPointsDown, yPointsDown, 3);
                                break;
                            case "ArrowLeft":

                                g.fillRect(startX + width / 4, startY + height / 4, width / 2, height / 2);

                                int[] xPointsLeft = {startX + width / 4, startX, startX + width / 4};
                                int[] yPointsLeft = {startY, startY + (height / 2), startY + height};
                                g.fillPolygon(xPointsLeft, yPointsLeft, 3);
                                break;
                            case "ArrowRight":
                                g.fillRect(startX + width / 4, startY + height / 4, width / 2, height / 2);

                                int[] xPointsRight = {startX + 3 * width / 4, startX + width, startX + 3 * width / 4};
                                int[] yPointsRight = {startY, startY + (height / 2), startY + height};
                                g.fillPolygon(xPointsRight, yPointsRight, 3);
                                break;

                            case "Freehand":
                                drawPanel.addMouseMotionListener(new MouseMotionAdapter() {
                                    int oldX = 0, oldY = 0;

                                    @Override
                                    public void mouseDragged(MouseEvent e) {
                                        int x = e.getX();
                                        int y = e.getY();
                                        Graphics2D g2d = (Graphics2D) drawPanel.getGraphics();
                                        g2d.setColor(currentColor);

                                        if (oldX != 0 && oldY != 0) {
                                            g2d.drawLine(oldX, oldY, x, y);
                                        }

                                        oldX = x;
                                        oldY = y;
                                    }

                                    public void mouseReleased(MouseEvent e) {
                                        oldX = 0;
                                        oldY = 0;
                                    }
                                });
                                break;

                        }
                    } else {
                        switch (shapeProps.shape) {
                            case "Rectangle":
                                g.draw3DRect(startX, startY, width, height, true);
                                break;
                            case "Oval":
                                g.drawOval(startX, startY, width, height);
                                break;
                            case "Line":
                                g.drawLine(startX, startY, endX, endY);
                                break;
                            case "Triangle":
                                int[] xPoints = {startX, startX + (width / 2), startX + width};
                                int[] yPoints = {startY + height, startY, startY + height};
                                g.drawPolygon(xPoints, yPoints, 3);
                                break;
                            case "Circle":
                                g.drawOval(startX, startY, width, height);
                                break;
                            case "Heart":

                                float beX = startX + width / 2;
                                float beY = startY + height;

                                float c1DX = width * 0.968f;
                                float c1DY = height * 0.672f;
                                float c2DX = width * 0.281f;
                                float c2DY = height * 1.295f;
                                float teDY = height * 0.850f;

                                Path2D.Float heartPath = new Path2D.Float();
                                heartPath.moveTo(beX, beY);

                                heartPath.curveTo(
                                        beX - c1DX, beY - c1DY,
                                        beX - c2DX, beY - c2DY,
                                        beX, beY - teDY);

                                heartPath.curveTo(
                                        beX + c2DX, beY - c2DY,
                                        beX + c1DX, beY - c1DY,
                                        beX, beY);

                                Graphics2D g2d = (Graphics2D) g;
                                g2d.setColor(shapeProps.color);
                                g2d.draw(heartPath);
                                break;
                            case "Smile":

                                int smileWidth = width;
                                int smileHeight = height;

                                g.drawOval(startX, startY, smileWidth, smileHeight);
                                g.setColor(Color.BLACK);
                                g.drawOval(startX + smileWidth / 4, startY + smileHeight / 4, smileWidth / 8, smileHeight / 8);
                                g.drawOval(startX + smileWidth / 2, startY + smileHeight / 4, smileWidth / 8, smileHeight / 8);
                                g.drawOval(startX + smileWidth / 3, startY + smileHeight / 2, smileWidth / 10, smileHeight / 10);
                                g.drawArc(startX + smileWidth / 4, startY + smileHeight / 2, smileWidth / 2, smileHeight / 4, 180, 180);
                                break;

                            case "Star":
                                int cx = startX + width / 2;
                                int cy = startY + height / 2;
                                int outerRadius = Math.min(width, height) / 2;
                                int innerRadius = outerRadius / 2;
                                int numPoints = 5;

                                double angle = 2 * Math.PI / numPoints;

                                Path2D path = new Path2D.Double();

                                for (int i = 0; i < numPoints * 2; i++) {
                                    double radius = (i % 2 == 0) ? outerRadius : innerRadius;
                                    double x = cx + radius * Math.cos(i * angle);
                                    double y = cy + radius * Math.sin(i * angle);

                                    if (i == 0) {
                                        path.moveTo(x, y);
                                    } else {
                                        path.lineTo(x, y);
                                    }
                                }

                                path.closePath();

                                ((Graphics2D) g).draw(path);

                                break;
                            case "ArrowUp":

                                g.drawRect(startX + width / 4, startY + height / 4, width / 2, height / 2);

                                int[] xPointsUp = {startX, startX + (width / 2), startX + width};
                                int[] yPointsUp = {startY + height / 4, startY, startY + height / 4};
                                g.drawPolygon(xPointsUp, yPointsUp, 3);
                                break;
                            case "ArrowDown":

                                g.drawRect(startX + width / 4, startY + height / 4, width / 2, height / 2);

                                int[] xPointsDown = {startX, startX + (width / 2), startX + width};
                                int[] yPointsDown = {startY + 3 * height / 4, startY + height, startY + 3 * height / 4};
                                g.drawPolygon(xPointsDown, yPointsDown, 3);
                                break;
                            case "ArrowLeft":

                                g.drawRect(startX + width / 4, startY + height / 4, width / 2, height / 2);

                                int[] xPointsLeft = {startX + width / 4, startX, startX + width / 4};
                                int[] yPointsLeft = {startY, startY + (height / 2), startY + height};
                                g.drawPolygon(xPointsLeft, yPointsLeft, 3);
                                break;
                            case "ArrowRight":

                                g.drawRect(startX + width / 4, startY + height / 4, width / 2, height / 2);

                                int[] xPointsRight = {startX + 3 * width / 4, startX + width, startX + 3 * width / 4};
                                int[] yPointsRight = {startY, startY + (height / 2), startY + height};
                                g.drawPolygon(xPointsRight, yPointsRight, 3);
                                break;
                            case "Freehand":
                                drawPanel.addMouseMotionListener(new MouseMotionAdapter() {
                                    int oldX = 0, oldY = 0;

                                    @Override
                                    public void mouseDragged(MouseEvent e) {
                                        int x = e.getX();
                                        int y = e.getY();
                                        Graphics2D g2d = (Graphics2D) drawPanel.getGraphics();
                                        g2d.setColor(currentColor);

                                        if (oldX != 0 && oldY != 0) {
                                            g2d.drawLine(oldX, oldY, x, y);
                                        }

                                        oldX = x;
                                        oldY = y;
                                    }

                                    public void mouseReleased(MouseEvent e) {
                                        oldX = 0;
                                        oldY = 0;
                                    }
                                });

                                break;

                        }
                    }
                }
            }

        };

        setLayout(
                new BorderLayout());

        add(controlPanel, BorderLayout.NORTH);

        add(drawPanel, BorderLayout.CENTER);

        drawPanel.addMouseListener(
                new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e
            ) {
                isDrawing = true;
                x = e.getX();
                y = e.getY();
            }

            @Override
            public void mouseReleased(MouseEvent e
            ) {
                isDrawing = false;

                ShapeProperties shapeProps = new ShapeProperties(currentShape, x, y, e.getX(), e.getY(), currentColor);
                shapesList.add(shapeProps);

                redoList.clear();

                drawPanel.repaint();
            }

        }
        );

        drawPanel.setBackground(Color.white);

        drawPanel.addMouseMotionListener(
                new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e
            ) {
                if (isDrawing) {
                    Graphics g = drawPanel.getGraphics();
                    g.setColor(currentColor);

                    int width = e.getX() - x;
                    int height = e.getY() - y;

                    switch (currentShape) {
                        case "Rectangle":
                            g.fill3DRect(x, y, width, height, isDrawing);
                            break;
                        case "Oval":
                            g.fillOval(x, y, width, height);
                            break;
                        case "Line":
                            g.drawLine(x, y, e.getX(), e.getY());
                            break;
                        case "Triangle":
                            int[] xPoints = {x, x + (width / 2), x + width};
                            int[] yPoints = {y + height, y, y + height};
                            g.drawPolygon(xPoints, yPoints, 3);
                            break;
                        case "Circle":
                            g.drawOval(x, y, width, height);
                            break;

                        case "Smile":
                            g.drawOval(x, y, width, height); // Draw the face
                            g.drawOval(x + width / 4, y + height / 4, width / 8, height / 8); // Draw left eye
                            g.drawOval(x + width * 3 / 4 - width / 8, y + height / 4, width / 8, height / 8); // Draw right eye
                            g.drawArc(x + width / 4, y + height / 2, width / 2, height / 4, 180, 180); // Draw mouth
                            break;
                        case "Star": {
                            int startX = 0;
                            int cx = startX + width / 2;
                        }
                         {
                            int startY = 0;
                            int cy = startY + height / 2;
                        }
                        int outerRadius = Math.min(width, height) / 2;
                        int innerRadius = outerRadius / 2;
                        int numPoints = 5;

                        double angle = 2 * Math.PI / numPoints;

                        Path2D path = new Path2D.Double();

                        for (int i = 0; i < numPoints * 2; i++) {
                            double radius = (i % 2 == 0) ? outerRadius : innerRadius;
                            double cx = 0;
                            double x = cx + radius * Math.cos(i * angle);
                            double cy = 0;
                            double y = cy + radius * Math.sin(i * angle);

                            if (i == 0) {
                                path.moveTo(x, y);
                            } else {
                                path.lineTo(x, y);
                            }
                        }

                        path.closePath();

                    }

                    g.dispose();
                }
            }
        }
        );

        JScrollPane drawScrollPane = new JScrollPane(drawPanel);
        JSplitPane colorShapeSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, colorScrollPane, shapeScrollPane);

        colorShapeSplitPane.setBackground(currentColor
                = Color.blue);
        p = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, colorShapeSplitPane, drawScrollPane);
        JPanel topPanel = new JPanel(new BorderLayout());

        topPanel.add(p, BorderLayout.CENTER);

        add(topPanel);
    }

    private void undo() {
        if (!shapesList.isEmpty()) {
            ShapeProperties removedShape = shapesList.remove(shapesList.size() - 1);
            undoList.add(removedShape);
            drawPanel.repaint();
        }
    }

    private void redo() {
        if (!undoList.isEmpty()) {
            ShapeProperties redoShape = undoList.remove(undoList.size() - 1);
            shapesList.add(redoShape);
            drawPanel.repaint();
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (e.getSource() == c) {
            String str = (String) c.getSelectedValue();
            switch (str) {
                case "red":
                    currentColor = Color.RED;
                    break;
                case "blue":
                    currentColor = Color.BLUE;
                    break;
                case "green":
                    currentColor = Color.green;
                    break;
                case "yellow":
                    currentColor = Color.yellow;
                    break;
                case "pink":
                    currentColor = Color.pink;
                    break;
                case "voilet":
                    currentColor = Color.magenta;
                    break;
                case "grey":
                    currentColor = Color.gray;
                    break;
                case "orange":
                    currentColor = Color.ORANGE;
                    break;
                case "black":
                    currentColor = Color.BLACK;
                    break;
                case "white":
                    currentColor = Color.WHITE;
                    break;
                case "cyan":
                    currentColor = Color.CYAN;
                    break;
                case "magenta":
                    currentColor = Color.MAGENTA;
                    break;
            }
        } else if (e.getSource() == s) {
            currentShape = (String) s.getSelectedValue();
        }
    }
    private String[] gameTasks = {"Sun", "Flower", "House", "Boy", "apple", "School", "Any Thing You Like"};

    private void startGame() {
        int randomIndex = (int) (Math.random() * gameTasks.length);
        String task = gameTasks[randomIndex];
        secondsRemaining = 60;
        timer.start();
        JOptionPane.showMessageDialog(this, "Draw a " + task);
        JPanel topPanel = new JPanel(new BorderLayout());
        p.setPreferredSize(new Dimension(1600, 1600));
        topPanel.add(p, BorderLayout.NORTH);

        add(topPanel);
    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {
        int result = JOptionPane.showConfirmDialog(this, "EXIT ?", "Are YOU SURE", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.NO_OPTION) {
            ShapeColorF ShapeColorF = new ShapeColorF();
        } else if (result == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    @Override
    public void windowClosed(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }

    private class TimerListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (secondsRemaining > 0) {
                secondsRemaining--;
                timerLabel.setText("Timer: " + secondsRemaining + "s");
            } else {
                timer.stop();
                JOptionPane.showMessageDialog(ShapeColorF.this, "Time's up! Game over.");
            }
        }
    }

    private void saveDrawing() {
        BufferedImage image = new BufferedImage(drawPanel.getWidth(), drawPanel.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics g = image.getGraphics();
        drawPanel.paint(g);
        g.dispose();

        try {

            String desktopPath = System.getProperty("user.home") + "\\Desktop";
            String savePath = desktopPath + "\\Your ArtS.png";

            File file = new File(savePath);
            ImageIO.write(image, "png", file);
            JOptionPane.showMessageDialog(this, "Drawing saved successfully to the desktop.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error while saving the drawing: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String actionCommand = e.getActionCommand();

        if (actionCommand.equals("New")) {
            shapesList.clear();
            drawPanel.repaint();
        } else if (actionCommand.equals("Exit")) {
            int confirmExit = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to exit?", "Confirm Exit", JOptionPane.YES_NO_OPTION);
            if (confirmExit == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        } else if (actionCommand.equals("Task")) {
            startGame();
        } else if (actionCommand.equals("Complete")) {
            timer.stop();
            JOptionPane.showMessageDialog(this, "Task completed successfully!");
        } else if (actionCommand.equals("Undo")) {
            undo();
        } else if (actionCommand.equals("Redo")) {
            redo();
        } else if (actionCommand.equals("Save")) {
            saveDrawing();
        } else if (actionCommand.equals("More Color")) {

            Color newColor = JColorChooser.showDialog(this, "Choose Fill Color", currentColor);
            if (newColor != null) {
                currentColor = newColor;
            }
        } else if (actionCommand.equals("Fill Shapes")) {

            fillShape = !fillShape;
            drawPanel.repaint();
        } 
    }

    public static void main(String[] args) {
        ShapeColor f = new ShapeColor();
        f.setExtendedState(JFrame.MAXIMIZED_BOTH);
        f.setBackground(Color.black);
        f.setVisible(true);

    }

    private class ShapeProperties {

        String shape;
        int x, y, endX, endY;
        Color color;

        public ShapeProperties(String shape, int x, int y, int endX, int endY, Color color) {
            this.shape = shape;
            this.x = x;
            this.y = y;
            this.endX = endX;
            this.endY = endY;
            this.color = color;
        }
    }
}


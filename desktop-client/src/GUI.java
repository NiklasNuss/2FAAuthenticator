package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GUI{

    private static JFrame frame;
    private static JPanel menu;
    private static JPanel login;
    private static JPanel twoFAMenu;
    private static JPanel userMenu;
    private static JPanel TOTPMenu;
    private static JPanel HOTPMenu;
    private static JPanel OTPListMenu;
    private static JPanel emailOTPMenu;
    private static JPanel registerMenu;
    private static JPanel setupScreen;
    private static JPanel OCRAMenu;

    protected static void startGUI(){
        Controller.start();
        setupGUI();
        mainMenu();
    }

    private static void setupGUI(){

        //set OptionPane button text
        UIManager.put("OptionPane.yesButtonText", "Yes");
        UIManager.put("OptionPane.noButtonText", "No");

        // set font for all components
        Font textFont = new Font("Arial", Font.PLAIN, 14);
        Font titleFont = new Font("Arial", Font.BOLD, 16);
        UIManager.put("Button.font", textFont);
        UIManager.put("Label.font", titleFont);

        // create frame and set properties
        frame = new JFrame("2FA with OTP");
        frame.setSize(1000,800);
        frame.setLocationRelativeTo(null);
        frame.setIconImage(new ImageIcon("icon.png").getImage());
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setFocusTraversalKeysEnabled(true);
        frame.setBackground(java.awt.Color.WHITE);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {Controller.stop();}
            });

        // load all components
        createMainMenu();
        createLogin();
        createTwoFAMenu();
    }

    private static void createMainMenu(){

        menu = new JPanel();
        menu.setBounds(0,0,1000,800);
        menu.setBackground(Color.WHITE);
        menu.setLayout(null);

        //display title
        JLabel title = new JLabel("2FA with OTP");
        title.setBounds(432,50,200,50);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        menu.add(title);

        //display login button
        JButton login = new JButton("Login");
        login.setBounds(400,200,200,50);
        login.addActionListener(_ -> login());
        menu.add(login);

        //display register button
        JButton register = new JButton("Register new User");
        register.setBounds(400,300,200,50);
        register.addActionListener(_ -> register());
        menu.add(register);

        //display version info in the bottom right corner
        JLabel version = new JLabel("v1.1.2");
        version.setBounds(900,700,100,50);
       // version.setFont(new Font("Arial", Font.BOLD, 20));
        menu.add(version);
    }

    private static void createLogin() {

        login = new JPanel();
        login.setBounds(0,0,1000,800);
        login.setBackground(Color.WHITE);
        login.setLayout(null);

        //display title
        JLabel title = new JLabel("Login");
        title.setBounds(470,50,200,50);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        login.add(title);

        //display username label and field
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(300,200,100,50);
        login.add(usernameLabel);

        JTextField usernameField = new JTextField();
        usernameField.setBounds(400,200,200,50);
        login.add(usernameField);

        //display password label and field
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(300,300,100,50);
        login.add(passwordLabel);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(400,300,200,50);
        login.add(passwordField);

        //display login button
        JButton loginButton = new JButton("Login");
        loginButton.setBounds(400,400,200,50);
        loginButton.addActionListener(_ -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            Controller.login(username, password);
            if (Controller.getLogInSuccess()) {
                twoFAMenu();
            }
        });
        login.add(loginButton);

        //bind enter key to login button
        ActionListener actionListener = _ -> loginButton.doClick();
        loginButton.registerKeyboardAction(actionListener, KeyStroke.getKeyStroke("ENTER"), JComponent.WHEN_IN_FOCUSED_WINDOW);

        //display back button
        JButton back = new JButton("Back");
        back.setBounds(400,500,200,50);
        back.addActionListener(_ -> mainMenu());
        login.add(back);

        //bind escape key to back button
        ActionListener backActionListener = _ -> back.doClick();
        back.registerKeyboardAction(backActionListener, KeyStroke.getKeyStroke("ESCAPE"), JComponent.WHEN_IN_FOCUSED_WINDOW);
    }

    private static void createTwoFAMenu() {

        twoFAMenu = new JPanel();
        twoFAMenu.setBounds(0,0,1000,800);
        twoFAMenu.setBackground(Color.WHITE);
        twoFAMenu.setLayout(null);

        //display title
        JLabel title = new JLabel("Pick your 2FA method");
        title.setBounds(400,50,300,50);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        twoFAMenu.add(title);

        //display TOTP button
        JButton TOTP = new JButton("Authenticate with TOTP");
        TOTP.setBounds(350,100,300,50);
        TOTP.addActionListener(_ -> TOTP());
        twoFAMenu.add(TOTP);

       //display HOTP button
        JButton HOTP = new JButton("Authenticate with HOTP");
        HOTP.setBounds(350,175,300,50);
        HOTP.addActionListener(_ -> HOTP());
        twoFAMenu.add(HOTP);

        //display OTP List button
        JButton OTPList = new JButton("Authenticate with OTP List");
        OTPList.setBounds(350,250,300,50);
        OTPList.addActionListener(_ -> OTPList());
        twoFAMenu.add(OTPList);

        //display Email OTP button
        JButton EmailOTP = new JButton("Authenticate with Email OTP");
        EmailOTP.setBounds(350,325,300,50);
        EmailOTP.addActionListener(_ -> EmailOTP());
        twoFAMenu.add(EmailOTP);

        //display OCRA challenge button
        JButton OCRA = new JButton("Authenticate with OCRA challenge");
        OCRA.setBounds(350,400,300,50);
        OCRA.addActionListener(_ -> OCRA());
        twoFAMenu.add(OCRA);

        //display login without 2FA button
        JButton loginWithout2FA = new JButton("Login without 2FA");
        loginWithout2FA.setBounds(350,475,300,50);
        loginWithout2FA.addActionListener(_ -> userMenu());
        twoFAMenu.add(loginWithout2FA);

        //display back button
        JButton back = new JButton("Back");
        back.setBounds(350,550,300,50);
        back.addActionListener(_ -> mainMenu());
        twoFAMenu.add(back);

        //bind escape key to back button
        ActionListener backActionListener = _ -> back.doClick();
        back.registerKeyboardAction(backActionListener, KeyStroke.getKeyStroke("ESCAPE"), JComponent.WHEN_IN_FOCUSED_WINDOW);
    }

    private static void createUserMenu() {

        userMenu = new JPanel();
        userMenu.setBounds(0,0,1000,800);
        userMenu.setBackground(Color.WHITE);
        userMenu.setLayout(null);

        //display welcome message
        JLabel title = new JLabel("Welcome "+ Controller.getDecryptedUser().getUsername() + "!");
        title.setBounds(432,50,200,50);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        userMenu.add(title);

        //display secret message as text field with title
        JLabel secretMessageLabel = new JLabel("Your Secret Message:");
        secretMessageLabel.setBounds(100,200,300,50);
        userMenu.add(secretMessageLabel);
        JTextField secretMessage = new JTextField();
        secretMessage.setBounds(275,200,500,50);
        secretMessage.setText(Controller.getDecryptedUser().getMessage());
        userMenu.add(secretMessage);

        //display OTP secret key as non-editable text field with title
        JLabel secretKeyLabel = new JLabel("Your Secret Key:");
        secretKeyLabel.setBounds(100,300,300,50);
        userMenu.add(secretKeyLabel);
        JTextField secretKey = new JTextField();
        secretKey.setBounds(275,300,500,50);
        secretKey.setText(Controller.getDecryptedUser().getSecretKey());
        secretKey.setEditable(false);
        userMenu.add(secretKey);

        //display OTP list as  non-editable text field with title
        JLabel otpListLabel = new JLabel("Your OTP List:");
        otpListLabel.setBounds(100,400,300,50);
        userMenu.add(otpListLabel);
        JTextField otpList = new JTextField();
        otpList.setBounds(275,400,500,50);
        otpList.setText(Controller.getDecryptedUser().getOtpList().toString());
        otpList.setEditable(false);
        userMenu.add(otpList);

        //logout & save button
        JButton logout = new JButton("Logout");
        logout.setBounds(400,500,200,50);
        logout.addActionListener(_ -> {
            Controller.logout(secretMessage.getText());
            mainMenu();
        });
        userMenu.add(logout);

        // delete account button
        JButton delete = new JButton("Delete Account");
        delete.setBounds(400,600,200,50);
        delete.addActionListener(_ -> confirmDelete());
        userMenu.add(delete);
    }

    private static void createTOTPMenu() {

        TOTPMenu = new JPanel();
        TOTPMenu.setBounds(0,0,1000,800);
        TOTPMenu.setBackground(Color.WHITE);
        TOTPMenu.setLayout(null);

        //display title
        JLabel title = new JLabel("Authenticate with TOTP");
        title.setBounds(350,50,300,50);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        TOTPMenu.add(title);

        //display OTP label and field
        JLabel OTPLabel = new JLabel(" Enter OTP:");
        OTPLabel.setBounds(300,300,100,50);
        TOTPMenu.add(OTPLabel);

        JTextField OTPField = new JTextField();
        OTPField.setBounds(400,300,200,50);
        TOTPMenu.add(OTPField);

        //display authenticate button
        JButton authenticate = new JButton("Authenticate");
        authenticate.setBounds(400,400,200,50);
        authenticate.addActionListener(_ -> {
            String OTP = OTPField.getText();
            Controller.verifyTOTP(OTP);
            if (Controller.getTwoFASuccess()) {
                userMenu();
            }
        });
        TOTPMenu.add(authenticate);

        //display back button
        JButton back = new JButton("Back");
        back.setBounds(400,500,200,50);
        back.addActionListener(_ -> twoFAMenu());
        TOTPMenu.add(back);

        //bind escape key to back button
        ActionListener backActionListener = _ -> back.doClick();
        back.registerKeyboardAction(backActionListener, KeyStroke.getKeyStroke("ESCAPE"), JComponent.WHEN_IN_FOCUSED_WINDOW);
    }

    private static void createHOTPMenu() {

        HOTPMenu = new JPanel();
        HOTPMenu.setBounds(0,0,1000,800);
        HOTPMenu.setBackground(Color.WHITE);
        HOTPMenu.setLayout(null);

        //display title
        JLabel title = new JLabel("Authenticate with HOTP");
        title.setBounds(350,50,300,50);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        HOTPMenu.add(title);

        //display counter label and field with current counter non-editable
        JLabel counterLabel = new JLabel(" Counter:");
        counterLabel.setBounds(300,200,100,50);
        HOTPMenu.add(counterLabel);

        JTextField counterField = new JTextField();
        counterField.setBounds(400,200,200,50);
        counterField.setText(Controller.getCounter());
        counterField.setEditable(false);
        HOTPMenu.add(counterField);

        //display OTP label and field
        JLabel OTPLabel = new JLabel("Enter OTP:");
        OTPLabel.setBounds(300,300,100,50);
        HOTPMenu.add(OTPLabel);

        JTextField OTPField = new JTextField();
        OTPField.setBounds(400,300,200,50);
        HOTPMenu.add(OTPField);

        //display authenticate button
        JButton authenticate = new JButton("Authenticate");
        authenticate.setBounds(400,400,200,50);
        authenticate.addActionListener(_ -> {
            String OTP = OTPField.getText();
            Controller.verifyHOTP(OTP);
            if (Controller.getTwoFASuccess()) {
                userMenu();
            }
        });
        HOTPMenu.add(authenticate);

        //display back button
        JButton back = new JButton("Back");
        back.setBounds(400,500,200,50);
        back.addActionListener(_ -> twoFAMenu());
        HOTPMenu.add(back);

        //bind escape key to back button
        ActionListener backActionListener = _ -> back.doClick();
        back.registerKeyboardAction(backActionListener, KeyStroke.getKeyStroke("ESCAPE"), JComponent.WHEN_IN_FOCUSED_WINDOW);
    }

    private static void createOTPListMenu() {

        OTPListMenu = new JPanel();
        OTPListMenu.setBounds(0,0,1000,800);
        OTPListMenu.setBackground(Color.WHITE);
        OTPListMenu.setLayout(null);

        //display title
        JLabel title = new JLabel("Authenticate with OTP List");
        title.setBounds(350,50,300,50);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        OTPListMenu.add(title);

        //display OTP label and field
        JLabel OTPLabel = new JLabel(" Enter OTP:");
        OTPLabel.setBounds(300,300,100,50);
        OTPListMenu.add(OTPLabel);

        JTextField OTPField = new JTextField();
        OTPField.setBounds(400,300,200,50);
        OTPListMenu.add(OTPField);

        //display authenticate button
        JButton authenticate = new JButton("Authenticate");
        authenticate.setBounds(400,400,200,50);
        authenticate.addActionListener(_ -> {
            String OTP = OTPField.getText();
            Controller.verifyOTPList(OTP);
            if (Controller.getTwoFASuccess()) {
                userMenu();
            }
        });
        OTPListMenu.add(authenticate);

        //display back button
        JButton back = new JButton("Back");
        back.setBounds(400,500,200,50);
        back.addActionListener(_ -> twoFAMenu());
        OTPListMenu.add(back);

        //bind escape key to back button
        ActionListener backActionListener = _ -> back.doClick();
        back.registerKeyboardAction(backActionListener, KeyStroke.getKeyStroke("ESCAPE"), JComponent.WHEN_IN_FOCUSED_WINDOW);
    }

    private static void createEmailOTPMenu() {

        emailOTPMenu = new JPanel();
        emailOTPMenu.setBounds(0,0,1000,800);
        emailOTPMenu.setBackground(Color.WHITE);
        emailOTPMenu.setLayout(null);

        //display title
        JLabel title = new JLabel("Authenticate with Email OTP");
        title.setBounds(350,50,300,50);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        emailOTPMenu.add(title);

        //display send to email label and field
        JLabel emailLabel = new JLabel(" Send OTP to:");
        emailLabel.setBounds(250,200,150,50);
        emailOTPMenu.add(emailLabel);

        JTextField emailField = new JTextField();
        emailField.setBounds(400,200,200,50);
        emailField.setText(Controller.getDecryptedUser().getEmail());
        emailField.setEditable(false);
        emailOTPMenu.add(emailField);

        //display OTP label and field
        JLabel OTPLabel = new JLabel(" Enter OTP:");
        OTPLabel.setBounds(250,300,100,50);
        emailOTPMenu.add(OTPLabel);

        JTextField OTPField = new JTextField();
        OTPField.setBounds(400,300,200,50);
        emailOTPMenu.add(OTPField);

        //display authenticate button
        JButton authenticate = new JButton("Authenticate");
        authenticate.setBounds(400,400,200,50);
        authenticate.addActionListener(_ -> {
            String OTP = OTPField.getText();
            Controller.verifyEmailOTP(OTP);
            if (Controller.getTwoFASuccess()) {
                userMenu();
            }
        });
        emailOTPMenu.add(authenticate);

        //display back button
        JButton back = new JButton("Back");
        back.setBounds(400,500,200,50);
        back.addActionListener(_ -> twoFAMenu());
        emailOTPMenu.add(back);

        //bind escape key to back button
        ActionListener backActionListener = _ -> back.doClick();
        back.registerKeyboardAction(backActionListener, KeyStroke.getKeyStroke("ESCAPE"), JComponent.WHEN_IN_FOCUSED_WINDOW);
    }

    private static void createRegisterMenu() {

        registerMenu = new JPanel();
        registerMenu.setBounds(0,0,1000,800);
        registerMenu.setBackground(Color.WHITE);
        registerMenu.setLayout(null);

        //display title
        JLabel title = new JLabel("Register new User");
        title.setBounds(400,50,200,50);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        registerMenu.add(title);

        //display username label and field
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(300,200,100,50);
        registerMenu.add(usernameLabel);

        JTextField usernameField = new JTextField();
        usernameField.setBounds(400,200,200,50);
        registerMenu.add(usernameField);

        //display password label and field
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(300,300,100,50);
        registerMenu.add(passwordLabel);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(400,300,200,50);
        registerMenu.add(passwordField);

        //display email label and field
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(300,400,100,50);
        registerMenu.add(emailLabel);

        JTextField emailField = new JTextField();
        emailField.setBounds(400,400,200,50);
        registerMenu.add(emailField);

        //display enable encryption checkbox
        JCheckBox enableEncryption = new JCheckBox("Enable Encryption");
        enableEncryption.setBounds(400,450,200,50);
        registerMenu.add(enableEncryption);

        //display create button
        JButton create = new JButton("Create");
        create.setBounds(400,500,200,50);
        create.addActionListener(_ -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            String email = emailField.getText();
            boolean crypto = enableEncryption.isSelected();
            Controller.register(username, password, email, crypto);
        });
        registerMenu.add(create);

        //display back button
        JButton back = new JButton("Back");
        back.setBounds(400,600,200,50);
        back.addActionListener(_ -> {
            Controller.reset();
            mainMenu();
        });
        registerMenu.add(back);

        //bind escape key to back button
        ActionListener backActionListener = _ -> back.doClick();
        back.registerKeyboardAction(backActionListener, KeyStroke.getKeyStroke("ESCAPE"), JComponent.WHEN_IN_FOCUSED_WINDOW);
    }

    private static void createSetupScreen(){

        setupScreen = new JPanel();
        setupScreen.setBounds(0,0,1000,800);
        setupScreen.setBackground(Color.WHITE);
        setupScreen.setLayout(null);

        //display title
        JLabel title = new JLabel("Thanks for creating an account "+ Controller.getDecryptedUser().getUsername() + "!");
        title.setBounds(100,50,800,50);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        setupScreen.add(title);

        //display OTP secretKey
        JLabel secretKeyLabel = new JLabel("Your Secret Key:");
        secretKeyLabel.setBounds(100,125,300,50);
        setupScreen.add(secretKeyLabel);

        JTextField secretKey = new JTextField();
        secretKey.setBounds(275,125,500,50);
        secretKey.setText(Controller.getDecryptedUser().getSecretKey());
        secretKey.setEditable(false);
        setupScreen.add(secretKey);

        //display OTP list
        JLabel otpListLabel = new JLabel("Your OTP List:");
        otpListLabel.setBounds(100,200,300,50);
        setupScreen.add(otpListLabel);

        JTextField otpList = new JTextField();
        otpList.setBounds(275,200,500,50);
        otpList.setText(Controller.getDecryptedUser().getOtpList().toString());
        otpList.setEditable(false);
        setupScreen.add(otpList);

        //display TOTP QR code
        JLabel totpQRLabel = new JLabel("TOTP QR Code:");
        totpQRLabel.setBounds(100,275,300,50);
        setupScreen.add(totpQRLabel);

        ImageIcon qrCodeIcon = new ImageIcon(Controller.getQrCodeTOTP());
        JLabel qrCodeLabel = new JLabel(qrCodeIcon);
        qrCodeLabel.setBounds(220,275,250,250);
        setupScreen.add(qrCodeLabel);

        //display HOTP QR code
        JLabel hotpQRLabel = new JLabel("HOTP QR Code:");
        hotpQRLabel.setBounds(500,275,300,50);
        setupScreen.add(hotpQRLabel);

        ImageIcon qrCodeIcon2 = new ImageIcon(Controller.getQrCodeHOTP());
        JLabel qrCodeLabel2 = new JLabel(qrCodeIcon2);
        qrCodeLabel2.setBounds(620,275,250,250);
        setupScreen.add(qrCodeLabel2);

        //display save notice
        JLabel saveNotice = new JLabel("Setup TOTP and HOTP in your authenticator app and save your secret key and OTP list!");
        saveNotice.setBounds(100,550,800,50);
        setupScreen.add(saveNotice);

        //display back button
        JButton back = new JButton("Back");
        back.setBounds(400,650,200,50);
        back.addActionListener(_ -> mainMenu());
        setupScreen.add(back);

        //bind escape key to back button
        ActionListener backActionListener = _ -> back.doClick();
        back.registerKeyboardAction(backActionListener, KeyStroke.getKeyStroke("ESCAPE"), JComponent.WHEN_IN_FOCUSED_WINDOW);
    }

    private static void createOCRAMenu(){

        OCRAMenu = new JPanel();
        OCRAMenu.setBounds(0,0,1000,800);
        OCRAMenu.setBackground(Color.WHITE);
        OCRAMenu.setLayout(null);

        //display title
        JLabel title = new JLabel("Authenticate with OCRA challenge");
        title.setBounds(350,50,500,50);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        OCRAMenu.add(title);

        //display challenge label and qr code
        JLabel challengeLabel = new JLabel("Challenge:");
        challengeLabel.setBounds(300,200,100,50);
        OCRAMenu.add(challengeLabel);

        ImageIcon qrCodeIcon = new ImageIcon(Controller.getChallengeQrCode());
        JLabel qrCodeLabel = new JLabel(qrCodeIcon);
        qrCodeLabel.setBounds(400,200,200,200);
        OCRAMenu.add(qrCodeLabel);

        //display OTP label and field
        JLabel OTPLabel = new JLabel(" Enter OTP:");
        OTPLabel.setBounds(300,450,100,50);
        OCRAMenu.add(OTPLabel);

        JTextField OTPField = new JTextField();
        OTPField.setBounds(400,450,200,50);
        OCRAMenu.add(OTPField);

        //display authenticate button
        JButton authenticate = new JButton("Authenticate");
        authenticate.setBounds(400,550,200,50);
        authenticate.addActionListener(_ -> {
            Controller.verifyOCRA(OTPField.getText());
            if (Controller.getTwoFASuccess()) {
                userMenu();
            }
        });
        OCRAMenu.add(authenticate);

        //display back button
        JButton back = new JButton("Back");
        back.setBounds(400,650,200,50);
        back.addActionListener(_ -> twoFAMenu());
        OCRAMenu.add(back);

        //bind escape key to back button
        ActionListener backActionListener = _ -> back.doClick();
        back.registerKeyboardAction(backActionListener, KeyStroke.getKeyStroke("ESCAPE"), JComponent.WHEN_IN_FOCUSED_WINDOW);
    }

    private static void EmailOTP() {
        new Thread(Controller::sendEmailOTP).start();
        System.out.println("[INFO] creating EmailOTPMenu");
        createEmailOTPMenu();
        System.out.println("[INFO] displaying EmailOTPMenu");
        frame.getContentPane().removeAll();
        frame.getContentPane().add(emailOTPMenu);
        frame.revalidate();
        frame.repaint();
    }

    private static void OTPList() {
        System.out.println("[INFO] creating OTPListMenu");
        createOTPListMenu();
        System.out.println("[INFO] displaying OTPListMenu");
        frame.getContentPane().removeAll();
        frame.getContentPane().add(OTPListMenu);
        frame.revalidate();
        frame.repaint();
    }

    private static void HOTP() {
        System.out.println("[INFO] creating HOTPMenu");
        createHOTPMenu();
        System.out.println("[INFO] displaying HOTPMenu");
        frame.getContentPane().removeAll();
        frame.getContentPane().add(HOTPMenu);
        frame.revalidate();
        frame.repaint();
    }

    private static void TOTP() {
        System.out.println("[INFO] creating TOTPMenu");
        createTOTPMenu();
        System.out.println("[INFO] displaying TOTPMenu");
        frame.getContentPane().removeAll();
        frame.getContentPane().add(TOTPMenu);
        frame.revalidate();
        frame.repaint();
    }

    private static void userMenu() {
        System.out.println("[INFO] creating userMenu with decrypted user data");
        createUserMenu();
        System.out.println("[INFO] displaying userMenu");
        frame.getContentPane().removeAll();
        frame.getContentPane().add(userMenu);
        frame.revalidate();
        frame.repaint();
    }

    public static void mainMenu() {
        System.out.println("[INFO] displaying mainMenu");
        frame.getContentPane().removeAll();
        frame.getContentPane().add(menu);
        frame.revalidate();
        frame.repaint();
    }

    public static void login() {
        System.out.println("[INFO] creating login");
        createLogin();
        System.out.println("[INFO] displaying login");
        frame.getContentPane().removeAll();
        frame.getContentPane().add(login);
        frame.revalidate();
        frame.repaint();
    }

    public static void twoFAMenu() {
        System.out.println("[INFO] displaying twoFAMenu");
        frame.getContentPane().removeAll();
        frame.getContentPane().add(twoFAMenu);
        frame.revalidate();
        frame.repaint();
    }

    private static void register() {
        System.out.println("[INFO] creating registerMenu");
        createRegisterMenu();
        System.out.println("[INFO] displaying registerMenu");
        frame.getContentPane().removeAll();
        frame.getContentPane().add(registerMenu);
        frame.revalidate();
        frame.repaint();
    }

    public static void setupScreen(){
        System.out.println("[INFO] creating setupScreen");
        createSetupScreen();
        System.out.println("[INFO] displaying setupScreen");
        frame.getContentPane().removeAll();
        frame.getContentPane().add(setupScreen);
        frame.revalidate();
        frame.repaint();
    }

    private static void OCRA() {
        System.out.println("[INFO] creating OCRAMenu");
        createOCRAMenu();
        System.out.println("[INFO] displaying OCRAMenu");
        frame.getContentPane().removeAll();
        frame.getContentPane().add(OCRAMenu);
        frame.revalidate();
        frame.repaint();
    }

    private static void confirmDelete() {
        int dialogResult = JOptionPane.showConfirmDialog(frame, "Are you sure you want to delete your account?", "Warning", JOptionPane.YES_NO_OPTION);
        if (dialogResult == JOptionPane.YES_OPTION) {
            Controller.deleteUser();
            mainMenu();
        }
    }

    protected static void displayInfo(String infoMessage) {
        JOptionPane.showMessageDialog(frame, infoMessage, "Info", JOptionPane.INFORMATION_MESSAGE);
    }

    protected static void displayError(String errorMessage) {
        JOptionPane.showMessageDialog(frame, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
    }
}

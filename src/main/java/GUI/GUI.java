package GUI;

import Controller.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

public class GUI {
    // Connect status constants
    private static final int DISCONNECTED = 0;
    private static final int CONNECTED = 1;

    private static final Logger logger = Logger.getLogger(GUI.class.getName());

    // Indicates the end of a session.
    private final static String END_CHAT_SESSION = Character.toString((char) 0);

    // Connection state info
    private String hostIP = "localhost";
    private int port = 8980;
    private final AtomicInteger connectionStatus;
    private Boolean isHost = true;

    // Various GUI components and info
    private JFrame mainFrame = null;
    private JTextArea chatText = null;
    private JTextField chatLine = null;
    private JTextField ipField = null;
    private JTextField portField = null;
    private JTextField nameField = null;
    private JRadioButton hostOption = null;
    private JRadioButton guestOption = null;
    private JButton connectButton = null;
    private JButton disconnectButton = null;
    private JButton sendButton = null;

    // TCP Components
    private Controller controller;
    private BufferedReader in = null;
    private PrintWriter out = null;


    private JPanel initOptionsPane() {
        JPanel pane;
        ActionListener buttonListener = null;

        // Create an options pane
        JPanel optionsPane = new JPanel(new GridLayout(4, 1));

        // IP address input
        pane = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pane.add(new JLabel("Host IP:"));
        ipField = new JTextField(10);
        ipField.setText(hostIP);
        ipField.setEnabled(true);
        pane.add(ipField);
        optionsPane.add(pane);

        // Port input
        pane = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pane.add(new JLabel("Port:"));
        portField = new JTextField(10);
        portField.setEditable(true);
        portField.setText((new Integer(port)).toString());
        pane.add(portField);
        optionsPane.add(pane);

        // Name input.
        pane = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pane.add(new JLabel("Name: "));
        nameField = new JTextField(10);
        nameField.setEditable(true);
        pane.add(nameField);
        optionsPane.add(pane);

        // Host/guest option
        buttonListener = e -> {
            synchronized (connectionStatus) {
                if (connectionStatus.intValue() != DISCONNECTED) {
                    return;
                }

                isHost = e.getActionCommand().equals("host");

                // Cannot supply host IP if host option is chosen
                if (isHost) {
                    ipField.setEnabled(false);
                    ipField.setText("localhost");
                    hostIP = "localhost";
                } else {
                    ipField.setEnabled(true);
                }
            }
        };
        ButtonGroup bg = new ButtonGroup();
        hostOption = new JRadioButton("Host", true);
        hostOption.setMnemonic(KeyEvent.VK_H);
        hostOption.setActionCommand("host");
        hostOption.addActionListener(buttonListener);
        guestOption = new JRadioButton("Guest", false);
        guestOption.setMnemonic(KeyEvent.VK_G);
        guestOption.setActionCommand("guest");
        guestOption.addActionListener(buttonListener);
        bg.add(hostOption);
        bg.add(guestOption);
        pane = new JPanel(new GridLayout(1, 2));
        pane.add(hostOption);
        pane.add(guestOption);
        optionsPane.add(pane);

        // Connect/disconnect buttons
        JPanel buttonPane = new JPanel(new GridLayout(1, 2));
        connectButton = new JButton("Connect");
        connectButton.setMnemonic(KeyEvent.VK_C);
        connectButton.setActionCommand("connect");
        connectButton.addActionListener(e -> {
            synchronized (connectionStatus) {
                if(connectionStatus.intValue() != DISCONNECTED) {
                    return;
                }
                connectButton.setEnabled(false);
                disconnectButton.setEnabled(true);
                try {
                    controller.createConnection(
                            isHost, ipField.getText(), Integer.parseInt(portField.getText()), nameField.getText());
                    connectionStatus.set(CONNECTED);
                } catch (IOException ex) {
                    logger.warning(ex.getMessage());
                }
            }
        });
        connectButton.setEnabled(true);
        disconnectButton = new JButton("Disconnect");
        disconnectButton.setMnemonic(KeyEvent.VK_D);
        disconnectButton.setActionCommand("disconnect");
        disconnectButton.addActionListener(e -> {
            synchronized (connectionStatus) {
                if(connectionStatus.intValue() != CONNECTED) {
                    return;
                }
                connectButton.setEnabled(true);
                disconnectButton.setEnabled(false);
                try {
                    controller.disconnect();
                    connectionStatus.set(DISCONNECTED);
                } catch (InterruptedException ex) {
                    logger.warning(ex.getMessage());
                }
            }
        });
        disconnectButton.setEnabled(false);
        buttonPane.add(connectButton);
        buttonPane.add(disconnectButton);
        optionsPane.add(buttonPane);

        return optionsPane;
    }

    private JPanel initChatPane() {
        JPanel chatPane = new JPanel(new BorderLayout());
        // Messages are displayed here.
        chatText = new JTextArea(10, 20);
        chatText.setLineWrap(true);
        chatText.setEditable(false);
        chatText.setForeground(Color.blue);
        JScrollPane chatTextPane = new JScrollPane(chatText,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        // Enter messages here.
        JPanel msgPane = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        chatLine = new JTextField();
        chatLine.setEnabled(false);
        sendButton = new JButton("Send");
        sendButton.setActionCommand("send");
        sendButton.addActionListener(e -> {
            String text;
            synchronized (connectionStatus) {
                if(connectionStatus.intValue() != CONNECTED) {
                    return;
                }
                text = chatLine.getText();
                chatLine.setText("");
                controller.sendMessage(text);
            }
            showMessage(text);
        });
        sendButton.setEnabled(false);
        msgPane.add(chatLine);
        msgPane.add(sendButton);

        chatPane.add(msgPane, BorderLayout.SOUTH);
        chatPane.add(chatTextPane, BorderLayout.CENTER);
        chatPane.setPreferredSize(new Dimension(200, 200));

        return chatPane;
    }

    // Initialize all the GUI components and display the frame
    public void init(Controller controller) {
        this.controller = controller;

        // Set up the options pane
        JPanel optionsPane = initOptionsPane();

        // Set up the chat pane
        JPanel chatPane = initChatPane();

        // Set up the main pane
        JPanel mainPane = new JPanel(new BorderLayout());
        mainPane.add(optionsPane, BorderLayout.WEST);
        mainPane.add(chatPane, BorderLayout.CENTER);

        // Add waiting until exit.
        WindowListener exitListener = new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                int confirm = JOptionPane.showOptionDialog(mainFrame,
                        "Are You Sure to Close this Application?",
                        "Exit Confirmation", JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null, null, null);
                if (confirm == 0) {
                    System.exit(1);
                }
            }
        };

        // Set up the main frame
        mainFrame = new JFrame("Instant Messenger");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setContentPane(mainPane);
        mainFrame.setSize(mainFrame.getPreferredSize());
        mainFrame.setLocation(200, 200);
        mainFrame.addWindowListener(exitListener);
        mainFrame.pack();
        mainFrame.setVisible(true);
    }

    // Thread-safe way to append to the chat box
    private void showMessage(String s) {
        synchronized (chatText) {
            chatText.append(s);
        }
    }

    public GUI() {
        this.connectionStatus = new AtomicInteger(DISCONNECTED);
    }
}

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Objects;


public class Main {
    //ImageIcon logo = new ImageIcon(".//res//icon.png");
    ImageIcon logo = new ImageIcon(getClass().getClassLoader().getResource("icon.png"));
    public static Boolean TextHasAlreadyBeenTypedIn = false;
    public static Boolean darkMode = true;
    public static Boolean NumberHasAlreadyBeenTypedIn = false;
    public static void main(String[] args) throws AWTException, IOException {
        File ColorModeSetupDecter = new File("ColorMode.cfg");
        if (!ColorModeSetupDecter.exists()) {
            BufferedWriter creater = new BufferedWriter(new FileWriter("ColorMode.cfg"));
            creater.close();
        }
        try {
            BufferedReader reader = new BufferedReader(new FileReader("ColorMode.cfg"));
            if (reader.readLine() == null) {
                try {
                    BufferedWriter writer = new BufferedWriter(new FileWriter("ColorMode.cfg"));
                    writer.write("ColorMode: light");
                    writer.close();
                } catch (IOException e) {
                    System.out.println("error writing to file ColorMode.cfg");
                }
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("error");
        }
        BufferedReader reader = new BufferedReader(new FileReader("ColorMode.cfg"));
        if (Objects.equals(reader.readLine(), "ColorMode: light")) {
            darkMode = false;
        } else if (Objects.equals(reader.readLine(), "ColorMode: dark")) {
            darkMode = true;
        }
        reader.close();
        if(darkMode) {
            FlatDarkLaf.setup();
        } else {
            FlatLightLaf.setup();
        }
        //frame
        JFrame frame = new JFrame("Autotyper v6 Java");
        frame.setSize(230, 250);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.setResizable(false);
        Main test = new Main();
        frame.setIconImage(test.logo.getImage());

        //Main text code
        //This is largely useless and may be removed before project is finished
        JLabel mainLabel = new JLabel("Autotyper v6 (made in java)");
        mainLabel.setBounds(25,5,300,18);
        mainLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        //area that the text is typed in

        JTextField typeContents = new JTextField();
        typeContents.setForeground(Color.gray);
        typeContents.setText("what would you like to type");
        typeContents.setBounds(25,40,160,20);
        typeContents.setHorizontalAlignment(SwingConstants.CENTER);



        //amount it should type

        JFormattedTextField amountToType = new JFormattedTextField();
        amountToType.setForeground(Color.gray);
        amountToType.setText("Amount to type");
        amountToType.setHorizontalAlignment(SwingConstants.CENTER);
        amountToType.setBounds(25,70,160,20);


        //press enter

        JCheckBox enter = new JCheckBox("Press enter");
        enter.setBounds(25,90,160,20);

        //color change
        JButton changeColorMode = new JButton("Change to ");
        changeColorMode.setBounds(25,120,160,20);
        if (darkMode) {
                changeColorMode.setText("Change to light mode");
        } else {
            changeColorMode.setText("Change to dark mode");
        }

        Robot robot = new Robot();
        //start
        JButton start = new JButton("Start");
        start.setBounds(25,150,160,20);

        //add ui elements
        frame.add(mainLabel);
        frame.add(typeContents);
        frame.add(amountToType);
        frame.add(enter);
        frame.add(changeColorMode);
        frame.add(start);
        //action code

        changeColorMode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (!darkMode) {
                    try {
                        BufferedWriter writer = new BufferedWriter(new FileWriter("ColorMode.cfg"));
                        writer.write("ColorMode: dark");
                        writer.close();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    changeColorMode.setText("Change to light mode");
                    FlatLightLaf.setup();
                    darkMode = true;
                    if(TextHasAlreadyBeenTypedIn) {
                        typeContents.setForeground(Color.white);
                        amountToType.setForeground(Color.white);
                    }
                    FlatDarkLaf.setup();
                    FlatDarkLaf.updateUI();
                    System.out.println("Changed to dark mode, var set to " + darkMode);
                } else {
                    changeColorMode.setText("Change to dark mode");
                    darkMode = false;
                    try {
                        BufferedWriter writer = new BufferedWriter(new FileWriter("ColorMode.cfg"));
                        writer.write("ColorMode: light");
                        writer.close();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    if(TextHasAlreadyBeenTypedIn) {
                        typeContents.setForeground(Color.black);
                        amountToType.setForeground(Color.black);
                    }
                    FlatLightLaf.setup();
                    FlatLightLaf.updateUI();

                    System.out.println("Changed to light mode, var set to " + darkMode);
                }

            }
        });

        typeContents.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if(!TextHasAlreadyBeenTypedIn) {
                    typeContents.setText("");
                    if(darkMode) {
                        typeContents.setForeground(Color.white);
                    } else {
                        typeContents.setForeground(Color.black);
                    }
                    System.out.println("focused");
                } else {
                    System.out.println("already been typed in");
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                TextHasAlreadyBeenTypedIn = true;
            }
        });

        amountToType.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c)) {
                    e.consume();
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {
                char c = e.getKeyChar();
                if (Character.isDigit(c)) {
                    if (!NumberHasAlreadyBeenTypedIn) {
                        NumberHasAlreadyBeenTypedIn = true;
                        amountToType.setText("");
                        if (darkMode) {
                            amountToType.setForeground(Color.white);
                        } else {
                            amountToType.setForeground(Color.black);
                        }
                        System.out.println("focused");
                    } else {
                        System.out.println("already been typed in");
                    }
                }

            }
            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
                System.out.println("starting spamming");
                try {
                    Robot robot = new Robot();

                    String text = typeContents.getText();
                    for (char c : text.toCharArray()) {
                        int keyCode = KeyEvent.getExtendedKeyCodeForChar(c);
                        if (KeyEvent.CHAR_UNDEFINED == keyCode) {
                            throw new RuntimeException(
                                    "Key code not found for character '" + c + "'");
                        }
                        robot.keyPress(keyCode);
                        robot.keyRelease(keyCode);
                        robot.delay(100);  // Add delay between keystrokes, if needed
                    }
                } catch (Exception a) {
                    a.printStackTrace();
                }
            }
        });
        //place at bottom
        frame.setVisible(true);
    }
}
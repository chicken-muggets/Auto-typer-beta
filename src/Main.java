import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Objects;

public class Main {

    ImageIcon logo = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("icon.png")));
    public static Boolean TextHasAlreadyBeenTypedIn = false;
    public static Boolean darkMode = true;
    public static Boolean NumberHasAlreadyBeenTypedIn = false;
    public static void main(String[] args) throws AWTException, IOException, InterruptedException {
        Keyboard keyboard = new Keyboard();
        File settingsFolderSetupDector = new File("Settings");
        if (!settingsFolderSetupDector.exists()) {
            settingsFolderSetupDector.mkdir();
        }
        File colorModeSetupDector = new File("Settings\\ColorMode.cfg");
        if (!colorModeSetupDector.exists()) {
            BufferedWriter creater = new BufferedWriter(new FileWriter("Settings\\ColorMode.cfg"));
            creater.close();
        }
        try {
            BufferedReader reader = new BufferedReader(new FileReader("Settings\\ColorMode.cfg"));
            if (reader.readLine() == null) {
                try {
                    BufferedWriter writer = new BufferedWriter(new FileWriter("Settings\\ColorMode.cfg"));
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
        BufferedReader reader = new BufferedReader(new FileReader("Settings\\ColorMode.cfg"));
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
        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
                for (double i = Double.parseDouble(amountToType.getText()); i > 0; i--) {
                    keyboard.type(typeContents.getText());
                    if (enter.isSelected()) {
                        keyboard.type("\n");
                    }
                    Point p = MouseInfo.getPointerInfo().getLocation();
                    System.out.println(p.x + " " + p.y);
                    if (p.x == 0 && p.y == 0) {
                        System.out.println("stopping");
                        break;

                    }
                }
            }
        });
        changeColorMode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (!darkMode) {
                    try {
                        BufferedWriter writer = new BufferedWriter(new FileWriter("Settings\\ColorMode.cfg"));
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
                        BufferedWriter writer = new BufferedWriter(new FileWriter("Settings\\ColorMode.cfg"));
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
        //place at bottom
        frame.setVisible(true);
    }
}
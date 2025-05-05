import javax.swing.*;
import java.awt.event.*;
import java.io.*;

public class SimpleTextEditor extends JFrame implements ActionListener {

    JTextArea textArea;
    JFileChooser fileChooser;
    String currentFilePath = null;

    public SimpleTextEditor() {
        // Frame settings
        setTitle("Simple Text Editor");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        // Text area
        textArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane);

        // Menu bar
        JMenuBar menuBar = new JMenuBar();

        // File menu
        JMenu fileMenu = new JMenu("File");
        String[] fileOptions = {"New", "Open", "Save", "Exit"};
        for (String option : fileOptions) {
            JMenuItem item = new JMenuItem(option);
            item.addActionListener(this);
            fileMenu.add(item);
        }
        fileMenu.setMnemonic(KeyEvent.VK_F);
        fileMenu.getItem(0).setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        fileMenu.getItem(1).setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        fileMenu.getItem(2).setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        fileMenu.getItem(3).setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.CTRL_MASK));
        fileMenu.getItem(3).setMnemonic(KeyEvent.VK_Q);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);

        fileChooser = new JFileChooser();

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        
        switch (command) {
            case "New":
                textArea.setText("");
                currentFilePath = null;
                setTitle("Simple Text Editor - New File");
                break;

            case "Open":
                int openResult = fileChooser.showOpenDialog(this);
                if (openResult == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                        textArea.read(reader, null);
                        currentFilePath = file.getAbsolutePath();
                        setTitle("Simple Text Editor - " + file.getName());
                    } catch (IOException ex) {
                        showError("Could not open file.");
                    }
                }
                break;

            case "Save":
                if (currentFilePath == null) {
                    int saveResult = fileChooser.showSaveDialog(this);
                    if (saveResult == JFileChooser.APPROVE_OPTION) {
                        currentFilePath = fileChooser.getSelectedFile().getAbsolutePath();
                    } else {
                        return;
                    }
                }
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(currentFilePath))) {
                    textArea.write(writer);
                    setTitle("Simple Text Editor - " + new File(currentFilePath).getName());
                } catch (IOException ex) {
                    showError("Could not save file.");
                }
                break;

            case "Exit":
                System.exit(0);
                break;
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SimpleTextEditor::new);
    }
}

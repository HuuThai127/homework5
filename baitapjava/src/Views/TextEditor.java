package Views;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;

import Controllers.TextEditorController;

import java.awt.*;
import java.io.File;
import java.util.List;

public class TextEditor extends JFrame {
    private JTextArea textArea;
    private JTree fileTree;

    public TextEditor() {
        super("Text Editor");
        // Khởi tạo text area
        textArea = new JTextArea();
        JScrollPane textScrollPane = new JScrollPane(textArea);
      //Khởi tạo Jtree
        fileTree = new JTree();
        fileTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        JScrollPane fileScrollPane = new JScrollPane(fileTree);
      // Khởi tạo nút mở
        JButton openButton = new JButton("Mở");
        openButton.addActionListener(e -> openFile());
      // Khởi tạo nút lưu
        JButton saveButton = new JButton("Luu");
        saveButton.addActionListener(e -> saveFile());
      //Tạo Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(openButton);
        buttonPanel.add(saveButton);
     // Đặt layout
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(fileScrollPane, BorderLayout.WEST);
        contentPane.add(textScrollPane, BorderLayout.CENTER);
        contentPane.add(buttonPanel, BorderLayout.SOUTH);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 380);
        setLocationRelativeTo(null);
    }

    private void openFile() {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            List<String> content = TextEditorController.loadFromFile(selectedFile.getAbsolutePath());
            if (content != null) {
                StringBuilder sb = new StringBuilder();
                for (String line : content) {
                    sb.append(line).append("\n");
                }
                textArea.setText(sb.toString());
            }
        }
    }

    private void saveFile() {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showSaveDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String fileName = selectedFile.getAbsolutePath();
            String content = textArea.getText();
            try {
                TextEditorController.saveToFile(fileName, content);
                JOptionPane.showMessageDialog(this, "File saved successfully!");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error saving file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    private void populateTree(File directory, DefaultMutableTreeNode parentNode) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                DefaultMutableTreeNode node = new DefaultMutableTreeNode(file);
                parentNode.add(node);
                if (file.isDirectory()) {
                    populateTree(file, node);
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TextEditor textView = new TextEditor();
            textView.setVisible(true);
        });
    }
}
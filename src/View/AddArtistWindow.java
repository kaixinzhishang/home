package View;

import Model.Singer;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddArtistWindow extends JFrame {
    // 声明文本框变量
    JTextField nameTextField = null;
    JTextField regionTextField = null;
    JTextField initialsTextField = null;

    public AddArtistWindow() {
        setTitle("添加歌手");
        setSize(400, 300);
        setLocationRelativeTo(null); // 居中显示
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(5, 5, 5, 5); // 设置组件之间的间距
        c.gridx = 0;
        c.gridy = 0;

        String[] labels = { "歌手名", "地区", "歌手名缩写" }; // 移除了"备注"
        for (int i = 0; i < labels.length; i++) {
            JLabel label = new JLabel(labels[i] + "：");
            JTextField textField = new JTextField(20);

            // 根据迭代次数为文本框变量赋值
            switch (i) {
                case 0:
                    nameTextField = textField;
                    break;
                case 1:
                    regionTextField = textField;
                    break;
                case 2:
                    initialsTextField = textField;
                    break;
            }

            add(label, c);
            c.gridx = 1;
            add(textField, c);
            c.gridx = 0;
            c.gridy++;
        }

        // 添加浏览按钮和文本框
        c.gridy++;
        JLabel imageLabel = new JLabel("歌手图片：");
        add(imageLabel, c);

        c.gridx = 1;
        JTextField imagePathField = new JTextField(20);
        JButton browseButton = new JButton("浏览...");
        JPanel panel = new JPanel();
        panel.add(imagePathField);
        panel.add(browseButton);
        add(panel, c);
        // 为browseButton添加监听器，用于选择图片文件
        browseButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fileChooser.setAcceptAllFileFilterUsed(false);
            fileChooser.addChoosableFileFilter(
                    new FileNameExtensionFilter("Image files", "jpg", "png", "jpeg", "gif", "bmp"));
            int returnValue = fileChooser.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                imagePathField.setText(selectedFile.getAbsolutePath());
            }
        });

        // 添加操作按钮
        c.gridx = 0;
        c.gridy++;
        c.gridwidth = 2;
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("添加");
        JButton resetButton = new JButton("重置");
        buttonPanel.add(addButton);
        buttonPanel.add(resetButton);
        add(buttonPanel, c);

        // 为addButton添加监听器，用于添加歌手信息到数据库
        JTextField finalNameTextField = nameTextField;
        JTextField finalRegionTextField = regionTextField;
        JTextField finalInitialsTextField = initialsTextField;
        addButton.addActionListener(e -> {
            String name = finalNameTextField.getText();
            String region = finalRegionTextField.getText();
            String initials = finalInitialsTextField.getText();
            String imagePath = imagePathField.getText(); // 歌手图片地址，这个已经在前面定义过

            // 检测文本框是否全部不为空
            if (name.isEmpty() || region.isEmpty() || initials.isEmpty() || imagePath.isEmpty()) {
                JOptionPane.showMessageDialog(null, "所有字段都是必填的，请填写完整。", "警告", JOptionPane.WARNING_MESSAGE);
            } else {
                Singer singer = new Singer(name, region, initials, imagePath);

                try {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    String url = "jdbc:mysql://localhost:3306/ktvsjk?useUnicode=true&characterEncoding=utf8&useSSL=true";
                    String user = "root";
                    String pass = "root";

                    // 使用 try-with-resources 自动管理资源
                    try (Connection connection = DriverManager.getConnection(url, user, pass);
                            PreparedStatement preparedStatement = connection.prepareStatement(
                                    "INSERT INTO singer (name, region, initials, address) VALUES (?, ?, ?, ?)")) {

                        preparedStatement.setString(1, singer.getName());
                        preparedStatement.setString(2, singer.getRegion());
                        preparedStatement.setString(3, singer.getInitials());
                        preparedStatement.setString(4, singer.getImagePath());
                        int rowsInserted = preparedStatement.executeUpdate();

                        if (rowsInserted > 0) {
                            JOptionPane.showMessageDialog(null, "歌手信息添加成功！");
                        } else {
                            JOptionPane.showMessageDialog(null, "歌手信息添加失败，未影响任何行。");
                        }
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(null, "添加歌手信息时数据库出错：" + ex.getMessage());
                        ex.printStackTrace();
                    }
                } catch (ClassNotFoundException ex) {
                    JOptionPane.showMessageDialog(null, "数据库驱动未找到：" + ex.getMessage());
                    ex.printStackTrace();
                }
            }
        });

        // 为重置按钮添加监听事件，用于清空所有文本框的内容
        resetButton.addActionListener(e -> {
            // 清除所有文本框的内容
            nameTextField.setText("");
            regionTextField.setText("");
            initialsTextField.setText("");
            imagePathField.setText("");
        });

        setVisible(true);
    }
}

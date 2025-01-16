package View;

import Model.Song;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddSongWindow extends JFrame {
    // 添加专属名字的文本框引用
    private JTextField nameTextField;
    private JTextField singerTextField;
    private JTextField languageTextField;
    private JTextField styleTextField;
    private JTextField initialsTextField;
    private JTextField addressTextField;

    public AddSongWindow() {
        setTitle("添加歌曲");
        setSize(400, 350); // 调整窗口高度以适应新添加的控件
        setLocationRelativeTo(null); // 居中显示
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // 使用 GridBagLayout
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        // 设置通用的填充和锚点
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(5, 5, 5, 5); // 添加一些间距

        // 修改标签和文本框创建部分，为文本框分配引用
        String[] labels = { "歌曲名称", "演唱者", "语言", "风格", "歌曲名缩写", "地址" };
        JTextField[] textFields = new JTextField[6];
        for (int i = 0; i < labels.length; i++) {
            c.gridx = 0;
            c.gridy = i;
            JLabel label = new JLabel(labels[i] + "：");
            add(label, c);

            c.gridx = 1;
            textFields[i] = new JTextField(20);
            add(textFields[i], c);
        }
        // 分配文本框引用
        nameTextField = textFields[0];
        singerTextField = textFields[1];
        languageTextField = textFields[2];
        styleTextField = textFields[3];
        initialsTextField = textFields[4];
        addressTextField = textFields[5]; // 假设地址文本框是最后一个
        c.gridx = 2;
        JButton browseButton = new JButton("浏览...");
        add(browseButton, c);
        browseButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            // 设置文件过滤器为音频文件
            fileChooser.setFileFilter(new FileNameExtensionFilter("Audio files", "mp3", "wav", "aac"));
            int returnValue = fileChooser.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                addressTextField.setText(selectedFile.getAbsolutePath());
            }
        });

        // 创建并添加“添加”和“重置”按钮
        c.gridx = 0;
        c.gridy += 1; // 地址后面的一行
        c.gridwidth = 3; // 按钮跨越三列
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("添加");
        JButton resetButton = new JButton("重置");
        buttonPanel.add(addButton);
        buttonPanel.add(resetButton);
        add(buttonPanel, c);

        // 添加按钮监听事件
        addButton.addActionListener(e -> {
            // 收集数据
            String name = nameTextField.getText();
            String singer = singerTextField.getText();
            String language = languageTextField.getText();
            String style = styleTextField.getText();
            String initials = initialsTextField.getText();
            String address = addressTextField.getText();

            // 检查文本框是否全部不为空
            if (name.isEmpty() || singer.isEmpty() || language.isEmpty() || style.isEmpty() || initials.isEmpty()
                    || address.isEmpty()) {
                JOptionPane.showMessageDialog(null, "所有字段都是必填的，请填写完整。", "警告", JOptionPane.WARNING_MESSAGE);
            } else {
                Song song = new Song(name, singer, language, style, initials, address);

                try {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    String url = "jdbc:mysql://localhost:3306/ktvsjk?useUnicode=true&characterEncoding=utf8&useSSL=true";
                    String user = "root";
                    String pass = "root";
                    // 连接到数据库并插入数据

                    // 使用 try-with-resources 自动管理资源
                    try (Connection connection = DriverManager.getConnection(url, user, pass);
                            PreparedStatement pstmt = connection.prepareStatement(
                                    "INSERT INTO song (name, singer, language, style, initials, address) VALUES (?, ?, ?, ?, ?, ?)")) {
                        pstmt.setString(1, name);
                        pstmt.setString(2, singer);
                        pstmt.setString(3, language);
                        pstmt.setString(4, style);
                        pstmt.setString(5, initials);
                        pstmt.setString(6, address);
                        int rowsInserted = pstmt.executeUpdate();

                        if (rowsInserted > 0) {
                            JOptionPane.showMessageDialog(null, "歌曲信息添加成功！");
                        } else {
                            JOptionPane.showMessageDialog(null, "歌曲信息添加失败，未影响任何行。");
                        }
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(null, "添加歌曲信息时数据库出错：" + ex.getMessage());
                        ex.printStackTrace();
                    }
                } catch (ClassNotFoundException ex) {
                    JOptionPane.showMessageDialog(null, "数据库驱动未找到：" + ex.getMessage());
                    ex.printStackTrace();
                }
            }
        });

        // 添加重置按钮监听事件
        resetButton.addActionListener(e -> {
            // 清除所有文本框的内容
            nameTextField.setText("");
            singerTextField.setText("");
            languageTextField.setText("");
            styleTextField.setText("");
            initialsTextField.setText("");
            addressTextField.setText("");
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AddSongWindow());
    }
}
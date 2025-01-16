package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class LanguageSongWindow extends JFrame {
    private JTextField searchTextField;
    private JButton searchButton;
    private JList<String> songList;
    private DefaultListModel<String> songListModel;

    public LanguageSongWindow() {
        super("语种点歌");
        setSize(600, 400);
        setLocationRelativeTo(null);// 居中显示
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // 初始化组件
        searchTextField = new JTextField(20);
        searchButton = new JButton("搜索");
        songListModel = new DefaultListModel<>();
        songList = new JList<>(songListModel);
        songList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    int index = songList.getSelectedIndex();
                    String songName = songListModel.get(index);
                    // 获取歌曲的 address
                    String songAddress = getSongAddress(songName);
                    addToOrderedList(songName, songAddress);
                }
            }
        });
        JScrollPane scrollPane = new JScrollPane(songList);

        // 布局
        JPanel panel = new JPanel();
        panel.setLayout(null); // 将布局设置为null，使用绝对布局
        panel.add(searchTextField);
        searchTextField.setBounds(100, 10, 300, 30);
        panel.add(searchButton);
        searchButton.setBounds(440, 10, 60, 30);
        panel.add(scrollPane); // 滚动面板现在位于按钮下方，但您可以根据需要调整
        scrollPane.setBounds(100, 50, 400, 250);

        add(panel, BorderLayout.CENTER); // 将整个面板添加到窗口的中心

        // 设置搜索按钮的点击事件
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchSinger(searchTextField.getText());
            }
        });
        // 加载MySQL JDBC驱动（可选，但推荐用于兼容性）
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "MySQL JDBC驱动未找到！");
            System.exit(1);
        }

        // 初始搜索（可选）
        // searchDatabase("");
    }

    private void addToOrderedList(String name, String address) {
        String query = "INSERT INTO yidiangequ (name, address) VALUES (?, ?)";
        try (Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/ktvsjk?useUnicode=true&characterEncoding=utf8&useSSL=true", "root",
                "root");
                PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, name);
            pstmt.setString(2, address);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "已添加歌曲: " + name);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "添加歌曲失败: " + ex.getMessage());
        }
    }

    // 查询语言并在列表中展示歌曲信息
    private void searchSinger(String keyword) {
        String url = "jdbc:mysql://localhost:3306/ktvsjk?useUnicode=true&characterEncoding=utf8&useSSL=true";
        String user = "root";
        String pass = "root";

        String query = "SELECT name, language FROM song WHERE language LIKE ?";
        try (Connection connection = DriverManager.getConnection(url, user, pass);
                PreparedStatement pstmt = connection.prepareStatement(query)) {

            pstmt.setString(1, "%" + keyword + "%");
            ResultSet rs = pstmt.executeQuery();
            songListModel.clear();

            while (rs.next()) {
                String name = rs.getString("name");
                String language = rs.getString("language");
                songListModel.addElement(name + " - " + language); // 显示歌曲和歌手信息
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "查询数据库时出错：" + ex.getMessage());
            ex.printStackTrace();// 异常处理语句，作用是打印发生的异常堆栈跟踪
        }
    }

    private String getSongAddress(String songName) {
        String query = "SELECT address FROM song WHERE name = ?";
        String address = null;
        try (Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/ktvsjk?useUnicode=true&characterEncoding=utf8&useSSL=true", "root",
                "root");
                PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, songName);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                address = rs.getString("address");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "查询歌曲地址失败: " + ex.getMessage());
        }
        return address;
    }

    // 主方法（用于测试）
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PinyinSongWindow window = new PinyinSongWindow();
            window.setVisible(true);
            // 确保了 Swing 组件的创建和操作都是在事件分发线程上进行的。
            // 它有助于避免并发问题并确保 Swing组件的线程安全性。
        });
    }
}
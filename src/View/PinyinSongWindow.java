package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class PinyinSongWindow extends JFrame {
    private JTextField searchTextField; // 搜索框
    private JButton searchButton; // 搜索按钮
    private JList<String> songList; // 歌曲列表
    private DefaultListModel<String> songListModel; // 歌曲列表模型

    public PinyinSongWindow() {
        super("拼音点歌"); // 设置窗口标题
        setSize(600, 400); // 设置窗口大小
        setLocationRelativeTo(null); // 窗口居中显示
        setDefaultCloseOperation(DISPOSE_ON_CLOSE); // 设置关闭操作

        // 初始化组件
        searchTextField = new JTextField(20); // 初始化搜索框
        searchButton = new JButton("搜索"); // 初始化搜索按钮
        songListModel = new DefaultListModel<>(); // 初始化歌曲列表模型
        songList = new JList<>(songListModel); // 初始化歌曲列表
        songList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) { // 单击事件
                    int index = songList.getSelectedIndex(); // 获取选中的索引
                    String songName = songListModel.get(index); // 获取歌曲名称
                    // 获取歌曲的地址
                    String songAddress = getSongAddress(songName);
                    addToOrderedList(songName, songAddress); // 添加到已点列表
                }
            }
        });
        JScrollPane scrollPane = new JScrollPane(songList); // 初始化滚动面板

        // 布局
        JPanel panel = new JPanel(); // 初始化面板
        panel.setLayout(null); // 设置绝对布局
        panel.add(searchTextField); // 添加搜索框到面板
        searchTextField.setBounds(100, 10, 300, 30); // 设置搜索框位置和大小
        panel.add(searchButton); // 添加搜索按钮到面板
        searchButton.setBounds(440, 10, 60, 30); // 设置搜索按钮位置和大小
        panel.add(scrollPane); // 添加滚动面板到面板
        scrollPane.setBounds(100, 50, 400, 250); // 设置滚动面板位置和大小

        add(panel, BorderLayout.CENTER); // 将面板添加到窗口中心

        // 事件监听器
        searchTextField.addKeyListener(new KeyAdapter() {
            private Timer timer; // 定时器

            @Override
            public void keyReleased(KeyEvent e) {
                if (timer != null && timer.isRunning()) { // 如果定时器正在运行
                    timer.stop(); // 停止定时器
                }
                timer = new Timer(500, new ActionListener() { // 设置定时器延迟500毫秒
                    @Override
                    public void actionPerformed(ActionEvent ae) {
                        searchDatabase(searchTextField.getText()); // 执行搜索
                    }
                });
                timer.start(); // 启动定时器
            }
        });
        // 加载MySQL JDBC驱动
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "MySQL JDBC驱动未找到！"); // 显示错误信息
            System.exit(1); // 退出程序
        }

        // 初始搜索（可选）
        // searchDatabase("");
    }

    // 添加歌曲到已点列表
    private void addToOrderedList(String name, String address) {
        String query = "INSERT INTO yidiangequ (name, address) VALUES (?, ?)"; // SQL插入语句
        try (Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/ktvsjk?useUnicode=true&characterEncoding=utf8&useSSL=true", "root",
                "root"); // 获取数据库连接
                PreparedStatement pstmt = conn.prepareStatement(query)) { // 准备SQL语句
            pstmt.setString(1, name); // 设置歌曲名称
            pstmt.setString(2, address); // 设置歌曲地址
            pstmt.executeUpdate(); // 执行插入操作
            JOptionPane.showMessageDialog(null, "已添加歌曲: " + name); // 显示添加成功信息
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "添加歌曲失败: " + ex.getMessage()); // 显示错误信息
        }
    }

    // 搜索数据库
    private void searchDatabase(String initials) {
        String query = "SELECT name FROM song WHERE initials LIKE ?"; // SQL查询语句
        try (Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/ktvsjk?useUnicode=true&characterEncoding=utf8&useSSL=true", "root",
                "root"); // 获取数据库连接
                PreparedStatement pstmt = conn.prepareStatement(query)) { // 准备SQL语句
            pstmt.setString(1, "%" + initials + "%"); // 设置模糊查询条件
            ResultSet rs = pstmt.executeQuery(); // 执行查询
            songListModel.clear(); // 清空歌曲列表模型
            while (rs.next()) {
                songListModel.addElement(rs.getString("name")); // 添加查询结果到歌曲列表模型
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "数据库查询失败: " + ex.getMessage()); // 显示错误信息
        }
    }

    // 获取歌曲地址
    private String getSongAddress(String songName) {
        String query = "SELECT address FROM song WHERE name = ?"; // SQL查询语句
        String address = null; // 初始化地址变量
        try (Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/ktvsjk?useUnicode=true&characterEncoding=utf8&useSSL=true", "root",
                "root"); // 获取数据库连接
                PreparedStatement pstmt = conn.prepareStatement(query)) { // 准备SQL语句
            pstmt.setString(1, songName); // 设置歌曲名称
            ResultSet rs = pstmt.executeQuery(); // 执行查询
            if (rs.next()) {
                address = rs.getString("address"); // 获取歌曲地址
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "查询歌曲地址失败: " + ex.getMessage()); // 显示错误信息
        }
        return address; // 返回歌曲地址
    }

    // 主方法（用于测试）
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PinyinSongWindow window = new PinyinSongWindow(); // 创建窗口实例
            window.setVisible(true); // 设置窗口可见
            // 确保了 Swing 组件的创建和操作都是在事件分发线程上进行的。
            // 它有助于避免并发问题并确保 Swing组件的线程安全性。
        });
    }
}

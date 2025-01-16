package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

import Model.PlaylistManager;

public class SingerSongWindow extends JFrame {
    private JTextField textField; // 用于输入歌手名字的文本框
    private JButton searchButton; // 用于搜索歌曲的按钮
    private JList<String> songList; // 显示歌曲列表的组件
    private DefaultListModel<String> listModel; // 歌曲列表的数据模型

    public SingerSongWindow(PlaylistManager sharedPlaylist) {
        // 初始化窗口和组件
        setTitle("歌手选歌"); // 设置窗口标题
        setSize(600, 400); // 设置窗口大小
        setLocationRelativeTo(null); // 窗口居中显示
        setDefaultCloseOperation(DISPOSE_ON_CLOSE); // 设置关闭操作

        textField = new JTextField(20); // 初始化文本框
        searchButton = new JButton("搜索"); // 初始化搜索按钮
        listModel = new DefaultListModel<>(); // 初始化歌曲列表的数据模型
        songList = new JList<>(listModel); // 初始化歌曲列表
        songList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION); // 设置歌曲列表的选择模式

        // 使用 BorderLayout 进行布局
        JPanel searchPanel = new JPanel(new BorderLayout()); // 创建搜索面板
        JPanel inputPanel = new JPanel(); // 创建输入面板
        inputPanel.add(textField); // 将文本框添加到输入面板
        inputPanel.add(searchButton); // 将搜索按钮添加到输入面板
        searchPanel.add(inputPanel, BorderLayout.NORTH); // 将输入面板添加到搜索面板的北部
        searchPanel.add(new JScrollPane(songList), BorderLayout.CENTER); // 将歌曲列表添加到搜索面板的中心

        add(searchPanel); // 将搜索面板添加到窗口

        // 设置搜索按钮的点击事件
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchSinger(textField.getText()); // 调用搜索歌手的方法
            }
        });

        // 添加歌曲到播放列表
        songList.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 1) { // 单击事件
                    int index = songList.getSelectedIndex(); // 获取选中的歌曲索引
                    String songName = songList.getModel().getElementAt(index); // 获取选中的歌曲名称
                    // 获取歌曲的地址
                    String songAddress = getSongAddress(songName);
                    addToOrderedList(songName, songAddress); // 将歌曲添加到播放列表
                }
            }
        });

        // 加载MySQL JDBC驱动（可选，但推荐用于兼容性）
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "MySQL JDBC驱动未找到！");
            System.exit(1);
        }

        setVisible(true); // 设置窗口可见
    }

    private void addToOrderedList(String name, String address) {
        String query = "INSERT INTO yidiangequ (name, address) VALUES (?, ?)"; // SQL插入语句
        try (Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/ktvsjk?useUnicode=true&characterEncoding=utf8&useSSL=true", "root",
                "root");
                PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, name); // 设置歌曲名称
            pstmt.setString(2, address); // 设置歌曲地址
            pstmt.executeUpdate(); // 执行插入操作
            JOptionPane.showMessageDialog(null, "已添加歌曲: " + name); // 显示添加成功的消息
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "添加歌曲失败: " + ex.getMessage()); // 显示添加失败的消息
        }
    }

    // 查询歌手并在列表中展示歌曲信息
    private void searchSinger(String keyword) {
        String url = "jdbc:mysql://localhost:3306/ktvsjk?useUnicode=true&characterEncoding=utf8&useSSL=true";
        String user = "root";
        String pass = "root";

        String query = "SELECT name, singer FROM song WHERE singer LIKE ?"; // SQL查询语句
        try (Connection connection = DriverManager.getConnection(url, user, pass);
                PreparedStatement pstmt = connection.prepareStatement(query)) {

            pstmt.setString(1, "%" + keyword + "%"); // 设置查询关键字
            ResultSet rs = pstmt.executeQuery(); // 执行查询
            listModel.clear(); // 清空列表模型

            while (rs.next()) {
                String name = rs.getString("name"); // 获取歌曲名称
                String singer = rs.getString("singer"); // 获取歌手名称
                listModel.addElement(name + " - " + singer); // 将歌曲和歌手信息添加到列表模型
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "查询数据库时出错：" + ex.getMessage()); // 显示查询错误的消息
            ex.printStackTrace(); // 打印异常堆栈跟踪
        }
    }

    // 获取歌曲地址，用于播放
    private String getSongAddress(String songName) {
        String url = "jdbc:mysql://localhost:3306/ktvsjk?useUnicode=true&characterEncoding=utf8&useSSL=true";
        String user = "root";
        String pass = "root";
        String query = "SELECT address FROM song WHERE name = ?"; // SQL查询语句
        String address = null;

        try (Connection connection = DriverManager.getConnection(url, user, pass);
                PreparedStatement pstmt = connection.prepareStatement(query)) {

            pstmt.setString(1, songName); // 设置歌曲名称
            ResultSet rs = pstmt.executeQuery(); // 执行查询
            if (rs.next()) {
                address = rs.getString("address"); // 获取歌曲地址
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "查询歌曲地址时出错：" + ex.getMessage()); // 显示查询错误的消息
            ex.printStackTrace(); // 打印异常堆栈跟踪
        }

        return address; // 返回歌曲地址
    }

    public static void main(String[] args) {
        PlaylistManager sharedPlaylist = new PlaylistManager(); // 创建共享播放列表
        new SingerSongWindow(sharedPlaylist); // 创建并显示窗口
    }
}

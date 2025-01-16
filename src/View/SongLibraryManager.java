package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class SongLibraryManager extends JDialog {
    private DefaultListModel<String> songListModel; // 用于存储歌曲信息的列表模型
    private JList<String> songList; // 用于显示歌曲信息的列表
    private JTextField queryTextField; // 用于输入查询内容的文本框

    public SongLibraryManager() {
        setTitle("歌曲库管理"); // 设置窗口标题
        setSize(600, 400); // 设置窗口大小
        setLocationRelativeTo(null); // 窗口居中显示
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE); // 设置关闭操作

        // 使用 BorderLayout 作为主布局
        setLayout(new BorderLayout());

        // 北部面板：查询部分
        JPanel northPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        queryTextField = new JTextField(20); // 创建一个文本框用于输入查询内容
        JButton queryButton = new JButton("查询歌曲"); // 创建查询按钮
        northPanel.add(queryTextField); // 将文本框添加到北部面板
        northPanel.add(queryButton); // 将查询按钮添加到北部面板
        add(northPanel, BorderLayout.NORTH); // 将北部面板添加到主布局的北部

        // 中部面板：歌曲列表区域（列表）
        songListModel = new DefaultListModel<>(); // 创建歌曲列表模型
        songList = new JList<>(songListModel); // 创建歌曲列表
        JScrollPane scrollPane = new JScrollPane(songList); // 创建带滚动条的面板
        add(scrollPane, BorderLayout.CENTER); // 将中部面板添加到主布局的中心

        // 南部面板：操作按钮
        JPanel southPanel = new JPanel();
        JButton addSongButton = new JButton("添加歌曲"); // 创建添加歌曲按钮
        JButton addArtistButton = new JButton("添加歌手"); // 创建添加歌手按钮
        southPanel.add(addSongButton); // 将添加歌曲按钮添加到南部面板
        southPanel.add(addArtistButton); // 将添加歌手按钮添加到南部面板
        add(southPanel, BorderLayout.SOUTH); // 将南部面板添加到主布局的南部

        // 为添加歌曲按钮添加事件监听器
        addSongButton.addActionListener(e -> {
            new AddSongWindow().setVisible(true); // 显示添加歌曲窗口
        });

        // 为添加歌手按钮添加事件监听器
        addArtistButton.addActionListener(e -> {
            new AddArtistWindow().setVisible(true); // 显示添加歌手窗口
        });

        // 为查询按钮添加事件监听器
        queryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String query = queryTextField.getText(); // 获取查询内容
                searchSongs(query); // 执行查询操作
            }
        });

        // 初始加载所有歌曲
        loadAllSongs();

        setVisible(true); // 显示窗口
    }

    // 加载所有歌曲信息
    private void loadAllSongs() {
        songListModel.clear(); // 清空列表模型
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // 加载数据库驱动
            String url = "jdbc:mysql://localhost:3306/ktvsjk?useUnicode=true&characterEncoding=utf8&useSSL=true"; // 数据库连接URL
            String user = "root"; // 数据库用户名
            String pass = "root"; // 数据库密码
            try (Connection conn = DriverManager.getConnection(url, user, pass); // 连接数据库
                    Statement stmt = conn.createStatement(); // 创建语句对象
                    ResultSet rs = stmt.executeQuery("SELECT * FROM song")) { // 执行查询

                while (rs.next()) { // 遍历结果集
                    String songInfo = rs.getString("name") + " - \n" +
                            rs.getString("singer") + " - \n" +
                            rs.getString("language") + " - \n" +
                            rs.getString("initials") + " - \n" +
                            rs.getString("style") + " - \n" +
                            rs.getString("address") + " - \n\n";
                    songListModel.addElement(songInfo); // 将歌曲信息添加到列表模型
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error connecting to the database: " + ex.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE); // 显示数据库错误信息
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        }
    }

    // 根据查询内容搜索歌曲
    private void searchSongs(String query) {
        songListModel.clear(); // 清空列表模型
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // 加载数据库驱动
            String url = "jdbc:mysql://localhost:3306/ktvsjk?useUnicode=true&characterEncoding=utf8&useSSL=true"; // 数据库连接URL
            String user = "root"; // 数据库用户名
            String pass = "root"; // 数据库密码
            try (Connection conn = DriverManager.getConnection(url, user, pass); // 连接数据库
                    PreparedStatement pstmt = conn
                            .prepareStatement("SELECT * FROM song WHERE name LIKE ? OR singer LIKE ?")) { // 创建预编译语句对象

                pstmt.setString(1, "%" + query + "%"); // 设置查询参数
                pstmt.setString(2, "%" + query + "%"); // 设置查询参数
                ResultSet rs = pstmt.executeQuery(); // 执行查询

                while (rs.next()) { // 遍历结果集
                    String songInfo = rs.getString("name") + " - \n" +
                            rs.getString("singer") + " - \n" +
                            rs.getString("language") + " - \n" +
                            rs.getString("initials") + " - \n" +
                            rs.getString("style") + " - \n" +
                            rs.getString("address") + " - \n\n";
                    songListModel.addElement(songInfo); // 将歌曲信息添加到列表模型
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error connecting to the database: " + ex.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE); // 显示数据库错误信息
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        }
    }

    // 主方法，用于启动程序
    public static void main(String[] args) {
        new SongLibraryManager(); // 创建并显示歌曲库管理窗口
    }
}

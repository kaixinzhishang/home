package View;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * PaihangWindow 类用于创建一个显示歌曲排行的窗口。
 */
public class PaihangWindow extends JFrame {
    private JList<String> list; // 用于显示歌曲排行的列表
    private DefaultListModel<String> listModel; // 列表的数据模型

    /**
     * 构造函数，初始化界面组件并设置窗口属性。
     */
    public PaihangWindow() {
        super("排行窗口"); // 设置窗口标题
        setSize(600, 400); // 设置窗口尺寸
        setLocationRelativeTo(null); // 窗口居中
        setDefaultCloseOperation(DISPOSE_ON_CLOSE); // 关闭窗口时仅释放窗口资源

        setLayout(new BorderLayout()); // 设置窗口布局为边界布局

        listModel = new DefaultListModel<>(); // 初始化列表模型
        list = new JList<>(listModel); // 创建JList组件并设置模型
        JScrollPane scrollPane = new JScrollPane(list); // 将列表放入滚动窗格
        add(scrollPane, BorderLayout.CENTER); // 将滚动窗格添加到窗口中央

        JPanel buttonPanel = new JPanel(); // 创建按钮面板
        JButton refreshButton = new JButton("刷新排行"); // 创建刷新按钮
        JButton closeButton = new JButton("关闭窗口"); // 创建关闭按钮

        // 添加刷新按钮的点击事件监听器
        refreshButton.addActionListener(e -> refreshSongs());
        // 添加关闭按钮的点击事件监听器
        closeButton.addActionListener(e -> dispose());

        buttonPanel.add(refreshButton); // 将刷新按钮添加到按钮面板
        buttonPanel.add(closeButton); // 将关闭按钮添加到按钮面板

        add(buttonPanel, BorderLayout.SOUTH); // 将按钮面板添加到窗口底部

        // 初始化时加载歌曲排行
        refreshSongs();
    }

    /**
     * 刷新歌曲排行，获取数据库中的歌曲信息并在列表中显示。
     */
    private void refreshSongs() {
        listModel.clear(); // 清空列表模型
        try {
            // 加载数据库驱动
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/ktvsjk?useUnicode=true&characterEncoding=utf8&useSSL=true";
            String user = "root"; // 数据库用户名
            String pass = "root"; // 数据库密码

            String query = "SELECT name, singer FROM song"; // SQL 查询语句

            // 使用 try-with-resources 自动管理数据库资源
            try (Connection connection = DriverManager.getConnection(url, user, pass);
                    PreparedStatement preparedStatement = connection.prepareStatement(query)) {

                ResultSet rs = preparedStatement.executeQuery(); // 执行查询并获取结果集

                List<String> songList = new ArrayList<>();
                while (rs.next()) {
                    String name = rs.getString("name"); // 获取歌曲名称
                    String singer = rs.getString("singer"); // 获取歌手名称
                    // 格式化字符串，将歌曲信息添加到列表
                    songList.add(String.format("Name: %s, Singer: %s", name, singer));
                }

                // 将每一条歌曲信息添加到列表模型中
                for (String song : songList) {
                    listModel.addElement(song);
                }

            } catch (SQLException ex) {
                // 如果数据库连接或查询失败，显示错误信息
                listModel.addElement("数据库连接或查询失败: " + ex.getMessage());
                JOptionPane.showMessageDialog(null, "数据库连接或查询出错：" + ex.getMessage());
                ex.printStackTrace();
            }
        } catch (ClassNotFoundException ex) {
            // 如果未找到数据库驱动，显示错误信息
            listModel.addElement("数据库驱动未找到：" + ex.getMessage());
            JOptionPane.showMessageDialog(null, "数据库驱动未找到：" + ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * 主方法，启动应用程序。
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PaihangWindow window = new PaihangWindow(); // 创建窗口实例
            window.setVisible(true); // 显示窗口
        });
    }
}

package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Login extends JFrame {
    JTabbedPane sy;
    JPanel panel1;
    JPanel panel2;
    JComboBox<String> lb1;
    JButton jb1, jb2, jb3;
    JTextField usernameField;
    JPasswordField passwordField;
    JButton loginButton;
    String selectedRoomType;

    public Login() {
        super("KTV点歌系统"); // 使用super设置标题
        setSize(500, 500);
        setLocationRelativeTo(null); // 居中显示
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        sy = new JTabbedPane(JTabbedPane.TOP);
        add(sy); // 直接添加到Login JFrame中

        panel1 = new JPanel();
        panel2 = new JPanel();

        sy.add("消费者", panel1);
        sy.add("管理员", panel2);

        setupPanel1();
        setupPanel2();

        setVisible(true); // 设置Login JFrame为可见
    }

    public void setupPanel1() {
        panel1.setLayout(null); // 设置null布局
        // 顶部添加“包厢选择”标签
        JLabel roomSelectionLabel = new JLabel("包厢选择");
        panel1.add(roomSelectionLabel);
        roomSelectionLabel.setBounds(100, 0, 70, 40);

        // 中间添加包厢类型的下拉列表
        lb1 = new JComboBox<>();
        lb1.addItem("玲珑包");
        lb1.addItem("小包");
        lb1.addItem("中包");
        lb1.addItem("大包");
        lb1.addItem("商务包");
        lb1.addItem("总统包");
        panel1.add(lb1);
        lb1.setBounds(200, 5, 70, 30);

        // 为下拉列表添加监听器
        lb1.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    // 获取当前选中的项
                    selectedRoomType = (String) e.getItem();
                    // 在这里你可以使用这个变量来做一些操作，比如显示收费信息
                }
            }
        });

        jb1 = new JButton("收费标准");
        jb2 = new JButton("开始唱歌");
        panel1.add(jb1);
        jb1.setBounds(180, 120, 100, 30);
        panel1.add(jb2);
        jb2.setBounds(180, 220, 100, 30);
        jb3 = new JButton("退出");
        panel1.add(jb3);
        jb3.setBounds(300, 340, 100, 30);

        jb1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showRoomChargesDialog(); // 调用显示收费信息的方法
            }
        });
        jb2.addActionListener(e -> {
            // 创建并显示欢唱窗口
            SingingWindow singingWindow = new SingingWindow(selectedRoomType);
            // 关闭登录窗口
            dispose();
        });
        jb3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // 关闭登录窗口
            }
        });
    }

    public String getSelectedRoomType() {
        return selectedRoomType;
    }

    private void showRoomChargesDialog() {
        JDialog dialog = new JDialog(this, "包厢收费信息", true); // 模态对话框
        dialog.setLayout(new BorderLayout()); // 使用BorderLayout布局

        // 创建一个文本区域用于显示收费信息
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false); // 设置文本区域为不可编辑
        textArea.setText("玲珑包：\n" +
                "基础收费（不含酒水和小吃）：10/小时\n" +
                "适合人数：2-4人\n" +
                "特点：小巧精致，适合私密聚会或情侣约会。\n" +

                "小包：\n" +
                "基础收费：20/小时\n" +
                "适合人数：4-6人\n" +
                "特点：经济实用，适合小型聚会或朋友聚会。\n" +

                "中包：\n" +
                "基础收费：30/小时\n" +
                "适合人数：6-8人\n" +
                "特点：空间适中，设备齐全，适合中等规模的聚会。\n" +

                "大包: \n" +
                "基础收费：40/小时\n" +
                "适合人数：8-12人\n" +
                "特点：宽敞舒适，适合大型聚会或团队活动。\n" +

                "商务包：\n" +
                "基础收费：50/小时\n" +
                "适合人数：10-15人\n" +
                "特点：高端装修，配备专业音响设备，适合商务会议或高端聚会。\n" +

                "总统包：\n" +
                "基础收费：60/小时\n" +
                "适合人数：15人以上\n" +
                "特点：极尽奢华，设备顶级，服务一流，适合重要商务活动或豪华派对。");

        // 添加滚动面板以便查看全部文本
        JScrollPane scrollPane = new JScrollPane(textArea);
        dialog.add(scrollPane, BorderLayout.CENTER);

        // 添加关闭按钮
        JButton closeButton = new JButton("关闭");
        closeButton.addActionListener(e -> dialog.dispose());
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(closeButton);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        // 设置对话框大小和可见性
        dialog.setSize(500, 500);
        dialog.setLocationRelativeTo(null); // 居中显示
        dialog.setVisible(true);
    }

    public void setupPanel2() {
        panel2.setLayout(null);// 设置null布局

        JLabel usernameLabel = new JLabel("用户名:");
        usernameLabel.setBounds(100, 110, 100, 50);
        JLabel passwordLabel = new JLabel("密码:");
        passwordLabel.setBounds(100, 160, 100, 50);
        // 添加文本框和密码框
        usernameField = new JTextField(20);
        usernameField.setBounds(160, 125, 200, 20);
        passwordField = new JPasswordField(20);
        passwordField.setBounds(160, 175, 200, 20);
        panel2.add(usernameLabel);
        panel2.add(passwordLabel);
        panel2.add(usernameField);
        panel2.add(passwordField);

        loginButton = new JButton("登录");
        loginButton.setBounds(200, 250, 100, 30);
        panel2.add(loginButton);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performLogin();
            }
        });
    }

    private void performLogin() {
        String username = usernameField.getText();
        char[] passwordChars = passwordField.getPassword();
        String password = new String(passwordChars);

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/ktvsjk?useUnicode=true&characterEncoding=utf8&useSSL=true";
            String user = "root";
            String pass = "root";
            Connection conn = DriverManager.getConnection(url, user, pass);

            String sql = "SELECT * FROM account WHERE name=? AND cipher=?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "登录成功！");
                // 登录成功后打开歌曲库管理界面
                new SongLibraryManager().setVisible(true);

                // 可以选择关闭登录窗口或者隐藏它
                // this.dispose(); // 关闭登录窗口
                // 或者
                // this.setVisible(false); // 隐藏登录窗口
            } else {
                JOptionPane.showMessageDialog(this, "用户名或密码错误！");
            }

            rs.close();
            pstmt.close();
            conn.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "登录过程中发生错误！");
        }
    }
}

package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

public class JiushuifuwuWindow extends JFrame {
    private Map<String, Integer> foodPrices = new HashMap<>(); // 食物价格映射
    private int totalAmount = 0; // 总金额

    public JiushuifuwuWindow() {

        super("酒水服务窗口");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // 创建选项卡窗格
        JTabbedPane tabbedPane = new JTabbedPane();

        // 创建饮料页面
        JPanel drinkPanel = new JPanel();
        drinkPanel.setLayout(new BorderLayout());
        DefaultListModel<String> drinkListModel = new DefaultListModel<>();
        drinkListModel.addElement("啤酒：20元");
        drinkListModel.addElement("红酒：50元");
        drinkListModel.addElement("白酒：80元");
        JList<String> drinkList = new JList<>(drinkListModel);
        drinkList.setFont(new Font("Serif", Font.PLAIN, 16));
        JScrollPane drinkScrollPane = new JScrollPane(drinkList);
        drinkPanel.add(drinkScrollPane, BorderLayout.CENTER);
        tabbedPane.addTab("饮料", drinkPanel);

        // 创建食品页面
        JPanel foodPanel = new JPanel();
        foodPanel.setLayout(new BorderLayout());
        DefaultListModel<String> foodListModel = new DefaultListModel<>();
        foodListModel.addElement("水果拼盘：40元");
        foodListModel.addElement("可口可乐：5元");
        foodListModel.addElement("百事可乐：5元");
        JList<String> foodList = new JList<>(foodListModel);
        foodList.setFont(new Font("Serif", Font.PLAIN, 16));
        JScrollPane foodScrollPane = new JScrollPane(foodList);
        foodPanel.add(foodScrollPane, BorderLayout.CENTER);
        tabbedPane.addTab("食品", foodPanel);

        // 将选项卡窗格添加到窗口中
        add(tabbedPane, BorderLayout.CENTER);

        // 添加按钮面板
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        JButton requestServiceButton = new JButton("请要求服务");
        requestServiceButton.setFont(new Font("Serif", Font.BOLD, 14));
        buttonPanel.add(requestServiceButton);

        JButton returnToMainButton = new JButton("返回主界面");
        returnToMainButton.setFont(new Font("Serif", Font.BOLD, 14));
        buttonPanel.add(returnToMainButton);

        JButton showAmountButton = new JButton("显示消费金额");
        showAmountButton.setFont(new Font("Serif", Font.BOLD, 14));
        buttonPanel.add(showAmountButton);

        JButton confirmButton = new JButton("确认");
        confirmButton.setFont(new Font("Serif", Font.BOLD, 14));
        buttonPanel.add(confirmButton);

        // 为按钮添加事件监听器（示例）
        requestServiceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 处理“请要求服务”按钮的点击事件
                JOptionPane.showMessageDialog(null, "服务已请求！");
            }
        });
        returnToMainButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 这里简单地关闭窗口，实际中可能需要其他逻辑来返回主界面
                dispose(); // 关闭当前窗口
            }
        });
        showAmountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 显示总金额
                JOptionPane.showMessageDialog(null, "总消费金额：" + totalAmount + "元");
            }
        });
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 假设“确认”按钮用于确认订单
                JOptionPane.showMessageDialog(null, "订单已确认！");
            }
        });

        // 为食物列表添加鼠标监听器
        foodList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { // 双击添加食物
                    String selectedFood = foodList.getSelectedValue();
                    String[] parts = selectedFood.split("：");
                    String foodName = parts[0];
                    int price = Integer.parseInt(parts[1].replace("元", ""));
                    totalAmount += price;
                    foodPrices.put(foodName, price);
                    JOptionPane.showMessageDialog(null, "已添加：" + foodName + "，价格：" + price + "元");
                }
            }
        });

        // 为饮料列表添加鼠标监听器
        drinkList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { // 双击添加饮料
                    String selectedDrink = drinkList.getSelectedValue();
                    String[] parts = selectedDrink.split("：");
                    String drinkName = parts[0];
                    int price = Integer.parseInt(parts[1].replace("元", ""));
                    totalAmount += price;
                    foodPrices.put(drinkName, price);
                    JOptionPane.showMessageDialog(null, "已添加：" + drinkName + "，价格：" + price + "元");
                }
            }
        });

        // 将按钮面板添加到窗口中
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JiushuifuwuWindow window = new JiushuifuwuWindow();
            window.setVisible(true);
        });
    }
}

package View;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FenleiWindow extends JFrame {
    public FenleiWindow() {
        super("分类点歌");
        setSize(600, 400);
        setLocationRelativeTo(null);// 居中显示
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(null); // 将布局设置为null，使用绝对布局
        JButton pinyinButton = new JButton("拼音点歌");
        JButton singerButton = new JButton("歌手点歌");
        JButton languageButton = new JButton("语别点歌");

        pinyinButton.setBounds(220, 40, 100, 40);
        singerButton.setBounds(220, 140, 100, 40);
        languageButton.setBounds(220, 240, 100, 40);

        pinyinButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new PinyinSongWindow().setVisible(true);
            }
        });

        singerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new SingerSongWindow(null).setVisible(true);
            }
        });

        languageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new LanguageSongWindow().setVisible(true);
            }
        });

        panel.add(pinyinButton);
        panel.add(singerButton);
        panel.add(languageButton);

        add(panel);
    }
}
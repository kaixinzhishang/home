package View;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import Model.PlaylistManager;

public class SingingWindow extends JFrame {
    private ImagePanel imagePanel;
    private PlaylistManager playlistManager; // 添加 PlaylistManager 实例
    private JLabel consumptionTimeLabel; // 消费时长标签
    private long startTime; // 记录开始时间

    public SingingWindow(String selectedRoomType) {
        super("欢唱不停" + selectedRoomType);
        setSize(540, 540);
        setLocationRelativeTo(null); // 居中显示
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        playlistManager = new PlaylistManager(); // 创建 PlaylistManager 实例
        imagePanel = new ImagePanel(playlistManager); // 将 PlaylistManager 实例传递给 ImagePanel
        add(imagePanel, BorderLayout.CENTER);

        startTime = System.currentTimeMillis(); // 初始化开始时间

        // 添加消费时长标签
        consumptionTimeLabel = new JLabel("消费时长：0小时 0分钟 0秒");
        consumptionTimeLabel.setFont(new Font("Serif", Font.BOLD, 14));
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topPanel.add(consumptionTimeLabel);
        add(topPanel, BorderLayout.NORTH);

        // 启动消费时长更新线程
        startConsumptionTimeUpdater();

        setVisible(true);
    }

    private void startConsumptionTimeUpdater() {
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000); // 每秒更新一次
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                updateConsumptionTime();
            }
        }).start();
    }

    private void updateConsumptionTime() {
        long currentTime = System.currentTimeMillis();
        long consumptionDuration = currentTime - startTime;
        long seconds = consumptionDuration / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        String consumptionTime = String.format("消费时长：%d小时 %d分钟 %d秒", hours, minutes % 60, seconds % 60);
        SwingUtilities.invokeLater(() -> consumptionTimeLabel.setText(consumptionTime));
    }

    private static class ImagePanel extends JPanel {
        private Image beijing;
        private Image fenlei;
        private Image yidiangequ;
        private Image paihang;
        private Image jiushuifuwu;
        private Image yibo;
        private Image qiege;
        private Image chongchang;
        private Image zanting;
        private Image bofang;
        private Image liebiao;
        private List<String> playlist = new ArrayList<>();// 添加一个列表来存储歌曲地址
        private int currentIndex = 0; // 当前播放索引
        private SourceDataLine line; // 用于音频播放的控制
        private AudioInputStream audioStream;
        private Thread playThread;
        private boolean isPlaying = false;
        private long pausePosition = 0; // 记录暂停位置

        public ImagePanel(PlaylistManager playlistManager) {
            beijing = Toolkit.getDefaultToolkit().getImage("wenjian/tupian/beijing.png");
            fenlei = Toolkit.getDefaultToolkit().getImage("wenjian/tupian/fenlei.png");
            yidiangequ = Toolkit.getDefaultToolkit().getImage("wenjian/tupian/yidiangequ.png");
            paihang = Toolkit.getDefaultToolkit().getImage("wenjian/tupian/paihang.png");
            jiushuifuwu = Toolkit.getDefaultToolkit().getImage("wenjian/tupian/jiushuifuwu.png");
            yibo = Toolkit.getDefaultToolkit().getImage("wenjian/tupian/yibo.png");
            qiege = Toolkit.getDefaultToolkit().getImage("wenjian/tupian/qiege.png");
            chongchang = Toolkit.getDefaultToolkit().getImage("wenjian/tupian/chongchang.png");
            zanting = Toolkit.getDefaultToolkit().getImage("wenjian/tupian/zanting.png");
            bofang = Toolkit.getDefaultToolkit().getImage("wenjian/tupian/bofang.png");
            liebiao = Toolkit.getDefaultToolkit().getImage("wenjian/tupian/liebiao.png");

            // 为图片添加鼠标监听器
            MouseListener mouseListener = new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    // 根据点击的图片位置判断点击的是哪张图片
                    int x = e.getX();
                    int y = e.getY();

                    // 假设每张图片的大小是110x110，并且它们的位置是固定的
                    if (x >= 0 && x < 110 && y >= 270 && y < 380) {
                        // 点击了fenlei图片
                        new FenleiWindow().setVisible(true);
                    } else if (x >= 110 && x < 220 && y >= 270 && y < 380) {
                        // 点击了yidiangequ图片
                        new YidiangequWindow(playlistManager).setVisible(true);
                    } else if (x >= 220 && x < 330 && y >= 270 && y < 380) {
                        // 点击了paihang图片
                        new PaihangWindow().setVisible(true);
                    } else if (x >= 330 && x < 440 && y >= 270 && y < 380) {
                        // 点击了jiushuifuwu图片
                        new JiushuifuwuWindow().setVisible(true); // 传递开始时间
                    } else if (x >= 440 && x < 550 && y >= 270 && y < 380) {
                        // 点击了列表图片
                        new PaihangWindow().setVisible(true);
                    }else if (x >= 0 && x < 110 && y >= 380 && y < 490) {
                        // 点击了yibo图片
                        new YibogequWindow(playlistManager).setVisible(true);
                    } else if (x >= 330 && x < 440 && y >= 380 && y < 490) {
                        // 点击了zanting图片
                        pauseAudio();
                    } else if (x >= 440 && x < 550 && y >= 380 && y < 490) {
                        // 点击了bofang图片
                        playAudioFromDatabase();
                    } else if (x >= 110 && x < 220 && y >= 380 && y < 490) {
                        // 点击了qiege图片
                        skipSong();
                    }
                }
            };
            addMouseListener(mouseListener);
        }

        private void updatePlaylist() {
            playlist.clear(); // 清空现有列表
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                String url = "jdbc:mysql://localhost:3306/ktvsjk?useUnicode=true&characterEncoding=utf8&useSSL=true";
                String user = "root";
                String pass = "root";

                try (Connection conn = DriverManager.getConnection(url, user, pass);
                        Statement stmt = conn.createStatement();
                        ResultSet rs = stmt.executeQuery("SELECT address, name FROM yidiangequ")) {

                    while (rs.next()) {
                        String audioFilePath = rs.getString("address");
                        String songName = rs.getString("name");
                        playlist.add(audioFilePath + "|" + songName); // 将歌曲地址和名字一起存储
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        }

        private void playCurrentAudio() {
            if (currentIndex < playlist.size()) {
                String[] songInfo = playlist.get(currentIndex).split("\\|");
                String audioFilePath = songInfo[0];
                try {
                    audioStream = AudioSystem.getAudioInputStream(new File(audioFilePath));
                    AudioFormat format = audioStream.getFormat();
                    DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
                    line = (SourceDataLine) AudioSystem.getLine(info);
                    line.open(format);
                    line.start();

                    playThread = new Thread(() -> {
                        byte[] buffer = new byte[4096];
                        int bytesRead;
                        try {
                            if (pausePosition > 0) {
                                audioStream.skip(pausePosition); // 从暂停位置继续播放
                            }
                            while ((bytesRead = audioStream.read(buffer, 0, buffer.length)) != -1) {
                                if (!isPlaying)
                                    break;
                                line.write(buffer, 0, bytesRead);
                            }
                            line.drain();
                            line.stop();
                            line.close();
                            audioStream.close();
                            if (isPlaying) { // 只有在歌曲播放完毕后才自动播放下一首歌
                                nextSong(); // 播放下一首歌
                            }
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    });
                    playThread.start();
                    isPlaying = true;
                } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
                    ex.printStackTrace();
                }
            }
        }

        private void nextSong() {
            if (currentIndex < playlist.size()) {
                String[] songInfo = playlist.get(currentIndex).split("\\|");
                String audioFilePath = songInfo[0];
                String songName = songInfo[1];
                insertSongIntoYibogequ(songName, audioFilePath); // 插入歌曲名字和地址到yibogequ表
                deleteSongFromDatabase(audioFilePath); // 删除当前播放的歌曲
                playlist.remove(currentIndex); // 从播放列表中删除歌曲
            }
            currentIndex++;
            if (currentIndex >= playlist.size()) {
                currentIndex = 0; // 可以选择循环播放或停止
            }
            playCurrentAudio();
        }

        private void playAudioFromDatabase() {
            if (!isPlaying) {
                updatePlaylist(); // 首先更新播放列表
                playCurrentAudio(); // 然后播放当前歌曲
            }
        }

        private void pauseAudio() {
            if (isPlaying) {
                isPlaying = false;
                line.stop();
                pausePosition = line.getMicrosecondPosition(); // 记录暂停位置
            } else {
                isPlaying = true;
                line.start();
                playThread = new Thread(() -> {
                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    try {
                        while ((bytesRead = audioStream.read(buffer, 0, buffer.length)) != -1) {
                            if (!isPlaying)
                                break;
                            line.write(buffer, 0, bytesRead);
                        }
                        line.drain();
                        line.stop();
                        line.close();
                        audioStream.close();
                        if (isPlaying) { // 只有在歌曲播放完毕后才自动播放下一首歌
                            nextSong(); // 播放下一首歌
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                });
                playThread.start();
            }
        }

private void skipSong() {
    if (currentIndex < playlist.size()) {
        String[] songInfo = playlist.get(currentIndex).split("\\|");
        String audioFilePath = songInfo[0];
        String songName = songInfo[1];
        insertSongIntoYibogequ(songName, audioFilePath); // 插入歌曲名字和地址到yibogequ表
        deleteSongFromDatabase(audioFilePath); // 删除跳过的歌曲
        playlist.remove(currentIndex); // 从播放列表中删除歌曲

        // 释放当前歌曲的资源
        if (line != null && line.isOpen()) {
            line.stop();
            line.close();
        }
        if (audioStream != null) {
            try {
                audioStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    currentIndex++;
    if (currentIndex >= playlist.size()) {
        currentIndex = 0; // 可以选择循环播放或停止
    }
    playCurrentAudio();
}


        private void deleteSongFromDatabase(String audioFilePath) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                String url = "jdbc:mysql://localhost:3306/ktvsjk?useUnicode=true&characterEncoding=utf8&useSSL=true";
                String user = "root";
                String pass = "root";

                try (Connection conn = DriverManager.getConnection(url, user, pass);
                        PreparedStatement pstmt = conn.prepareStatement("DELETE FROM yidiangequ WHERE address = ?")) {

                    pstmt.setString(1, audioFilePath);
                    pstmt.executeUpdate();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        }

        private void insertSongIntoYibogequ(String songName, String audioFilePath) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                String url = "jdbc:mysql://localhost:3306/ktvsjk?useUnicode=true&characterEncoding=utf8&useSSL=true";
                String user = "root";
                String pass = "root";

                try (Connection conn = DriverManager.getConnection(url, user, pass);
                        PreparedStatement pstmt = conn
                                .prepareStatement("INSERT INTO yibogequ (name, address) VALUES (?, ?)")) {

                    pstmt.setString(1, songName);
                    pstmt.setString(2, audioFilePath);
                    pstmt.executeUpdate();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            // 假设每张图片都是110x110大小
            g.drawImage(beijing, 0, 0, this);
            int x = 0;
            int y = 270;
            g.drawImage(fenlei, x, y, this);
            x += 110;
            g.drawImage(yidiangequ, x, y, this);
            x += 110;
            g.drawImage(paihang, x, y, this);
            x += 110;
            g.drawImage(jiushuifuwu, x, y, this);
            x += 110;
            g.drawImage(liebiao, x, y, this);
            x = 0; // 换行
            y += 110;
            g.drawImage(yibo, x, y, this);
            x += 110;
            g.drawImage(qiege, x, y, this);
            x += 110;
            g.drawImage(chongchang, x, y, this);
            x += 110;
            g.drawImage(zanting, x, y, this);
            x += 110;
            g.drawImage(bofang, x, y, this);
        }

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                String selectedRoomType = null;
                new SingingWindow(selectedRoomType);
            }
        });
    }
}

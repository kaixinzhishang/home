package View;

import javax.swing.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.DefaultListModel;
import Model.PlaylistManager;

public class YidiangequWindow extends JFrame {
    private JList<String> songList;
    private DefaultListModel<String> listModel;
    private DefaultListModel<String> tempSongList; // 用于临时存储歌曲名称的列表
    private PlaylistManager playlistManager;  // 播放列表管理器的引用


    public YidiangequWindow(PlaylistManager playlistManager) {
        super("已点歌曲窗口");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.playlistManager = playlistManager; // 保存 PlaylistManager 实例的引用
        initializeUI();
        loadSongsFromDatabase(); // 加载歌曲列表到 tempSongList 和 UI
    }

    private void prioritizeSong(int index) {
        String songName = tempSongList.getElementAt(index); // 从临时列表中获取歌曲名称

        // 从数据库中查询歌曲的路径
        String songPath = getSongPathByName(songName);

        // 如果找到了路径，则添加到播放列表的开头
        if (songPath != null) {
            playlistManager.addSongToPlaylist(songPath);
        }

        // 注意：这里我们没有修改 tempSongList，因为它只是用于显示目的
        // 如果你也想在 tempSongList 中反映这个更改，你应该手动移动它
        // 但这通常不是必要的，因为 tempSongList 只是临时的
    }

    // 添加一个辅助方法来从数据库中获取歌曲路径
    private String getSongPathByName(String songName) {
        String query = "SELECT address FROM yidiangequ WHERE name = ?";
        try (Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/ktvsjk?useUnicode=true&characterEncoding=utf8&useSSL=true", "root", "root");
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, songName);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("address"); // 返回找到的文件路径
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace(); // 在实际应用中，你可能想以更友好的方式处理错误
        }
        return null; // 如果没有找到歌曲，返回 null
    }

    private void initializeUI() {
        listModel = new DefaultListModel<>();
        tempSongList = new DefaultListModel<>(); // 初始化临时歌曲列表
        songList = new JList<>(tempSongList); // 使用 tempSongList 显示歌曲
        add(new JScrollPane(songList));

        // 添加鼠标监听事件
        songList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) { // 右键点击
                    int index = songList.locationToIndex(e.getPoint());
                    if (index != -1) {
                        showPopupMenu(e, index);
                    }
                }
            }
        });
    }

    private void loadSongsFromDatabase() {
        // 注意：loadSongsFromDatabase 方法现在将歌曲名称添加到 tempSongList 和 UI 的 listModel 中
        // 但通常，您只需要将它们添加到 tempSongList 中，因为 listModel 是用于 UI 显示的
        // 如果您想要保持两个列表同步，那么您需要在添加或删除歌曲时同时更新它们
        String query = "SELECT name FROM yidiangequ";
        try (Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/ktvsjk?useUnicode=true&characterEncoding=utf8&useSSL=true", "root", "root");
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                String songName = rs.getString("name");
                tempSongList.addElement(songName); // 添加到临时列表
                listModel.addElement(songName); // 也添加到 UI 列表（如果需要）
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "加载歌曲失败: " + ex.getMessage());
        }
    }


    private void showPopupMenu(MouseEvent e, int index) {
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem prioritizeItem = new JMenuItem("优先");
        JMenuItem deleteItem = new JMenuItem("删除");

        prioritizeItem.addActionListener(e1 -> prioritizeSong(index));
        deleteItem.addActionListener(e1 -> deleteSong(index));

        popupMenu.add(prioritizeItem);
        popupMenu.add(deleteItem);
        popupMenu.show(songList, e.getX(), e.getY());
    }

    private void deleteSong(int index) {
        String songName = listModel.getElementAt(index);
        listModel.remove(index);
        deleteSongFromDatabase(songName);
    }

    private void deleteSongFromDatabase(String songName) {
        String query = "DELETE FROM yidiangequ WHERE name = ?";
        try (Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/ktvsjk?useUnicode=true&characterEncoding=utf8&useSSL=true", "root", "root");
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, songName);
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "删除歌曲失败: " + ex.getMessage());
        }
    }
}
package Model;

import java.util.ArrayList;
import java.util.List;

public class PlaylistManager {
    private List<String> playlist = new ArrayList<>();

    /**
     * 向播放列表中添加一首歌曲（不考虑位置）。
     *
     * @param song 歌曲的路径或名称。
     */
    public void addSong(String song) {
        playlist.add(song);
    }

    /**
     * 获取播放列表的副本。
     *
     * @return 播放列表的副本。
     */
    public List<String> getPlaylist() {
        return new ArrayList<>(playlist); // 返回副本以避免外部修改
    }

    /**
     * 将一首歌曲添加到播放列表的开头（如果它还不存在）。
     *
     * @param songPath 歌曲的路径。
     */
    public void addSongToPlaylist(String songPath) {
        if (!playlist.contains(songPath)) {
            playlist.add(0, songPath); // 将歌曲添加到播放列表的开头
        }
    }

    // 可以添加更多方法，如 removeSong, clear 等
}
package Model;

public class Song {
    private String name; // 歌曲名称
    private String singer; // 演唱者
    private String language; // 语言
    private String style; // 风格
    private String initials; // 歌曲名缩写
    private String address; // 歌曲地址（可能指的是文件路径）

    // 构造函数
    public Song(String name, String singer, String language, String style, String initials, String address) {
        this.name = name;
        this.singer = singer;
        this.language = language;
        this.style = style;
        this.initials = initials;
        this.address = address;
    }

    // Getter 和 Setter 方法
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getInitials() {
        return initials;
    }

    public void setInitials(String initials) {
        this.initials = initials;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


}
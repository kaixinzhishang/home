package Model;

public class Singer {
    private String name;
    private String region;
    private String initials;
    private String imagePath;

    public Singer(String name, String region, String initials, String imagePath) {
        this.name = name;
        this.region = region;
        this.initials = initials;
        this.imagePath = imagePath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getInitials() {
        return initials;
    }

    public void setInitials(String initials) {
        this.initials = initials;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
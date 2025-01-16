import View.Login;

public class Main {
    public static void main(String args[]) {
        try {
            new Login();
        } catch (Exception e) {
            System.err.println("登录界面初始化失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

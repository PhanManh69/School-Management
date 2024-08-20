package school_management.models.user_controller;

public class UserController {
    private static String userName;
    private static String password;

    public static void setUser(String userName, String password) {
        UserController.userName = userName;
        UserController.password = password;
    }

    public static String getUserName() {
        return userName;
    }

    public static String getPassword() {
        return password;
    }
}

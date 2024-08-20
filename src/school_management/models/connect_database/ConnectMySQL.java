package school_management.models.connect_database;

import java.sql.*;
import javax.swing.*;

public class ConnectMySQL {

    public static Connection conn() {
        try {
            String url = "jdbc:mysql://localhost:2402/training_management";
            var user = "root";
            var password = "mysql6903@24Emanh";

            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, user, password);

            return conn;
        } catch (SQLException | ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, e);
        }
        return null;
    }

    public static void main(String[] args) {
        Connection conn = ConnectMySQL.conn();
        if (conn != null) {
            JOptionPane.showMessageDialog(null, "Kết nối thành công đến cơ sở dữ liệu.");
        } else {
            JOptionPane.showMessageDialog(null, "Không thể kết nối đến cơ sở dữ liệu.");
        }
    }
}

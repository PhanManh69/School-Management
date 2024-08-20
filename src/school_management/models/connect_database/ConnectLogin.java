package school_management.models.connect_database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import school_management.models.user_controller.UserController;
import school_management.views.dialog.Message;

public class ConnectLogin {
    
    public static int login(String userName, String password) {
        Connection conn = ConnectMySQL.conn();

        if (conn != null) {
            try {
                String studentQuery = "SELECT * FROM students WHERE student_code = ? AND password = ?";
                String lecturerQuery = "SELECT * FROM lecturers WHERE lecturers_code = ? AND password = ?";
                String adminQuery = "SELECT * FROM admin WHERE user_name = ? AND password = ?";
                
                try (PreparedStatement studentPstmt = conn.prepareStatement(studentQuery);
                     PreparedStatement lecturerPstmt = conn.prepareStatement(lecturerQuery);
                     PreparedStatement adminPstmt = conn.prepareStatement(adminQuery)) {
                    
                    studentPstmt.setString(1, userName);
                    studentPstmt.setString(2, password);
                    ResultSet studentResult = studentPstmt.executeQuery();
                    if (studentResult.next()) {
                        return 1;
                    }
                    
                    lecturerPstmt.setString(1, userName);
                    lecturerPstmt.setString(2, password);
                    ResultSet lecturerResult = lecturerPstmt.executeQuery();
                    if (lecturerResult.next()) {
                        return 2; 
                    }
                    
                    adminPstmt.setString(1, userName);
                    adminPstmt.setString(2, password);
                    ResultSet adminResult = adminPstmt.executeQuery();
                    if (adminResult.next()) {
                        return 3; 
                    }
                }
                
                return 0;
                
            } catch (SQLException e) {
                handleSQLException(e);
                System.out.println(e);
                return 0;
            } finally {
                try {
                    conn.close();
                } catch (SQLException e) {
                   handleSQLException(e);
                    System.out.println(e);
                }
            }
        } else {
            System.out.println("Unable to connect to database.");
            return 0;
        }
    }
    
    public static String className() {
        Connection conn = ConnectMySQL.conn();
        String userName = UserController.getUserName();
        String className = null;

        if (conn != null) {
            try {
                String studentQuery = "SELECT * FROM class c "
                        + "JOIN students s ON s.class_id = c.id "
                        + "WHERE s.student_code = ?";
                
                try (PreparedStatement studentPstmt = conn.prepareStatement(studentQuery)) {
                    studentPstmt.setString(1, userName);
                    ResultSet studentResult = studentPstmt.executeQuery();
                    if (studentResult.next()) {
                        className = studentResult.getString("c.class_name");
                        return className;
                    }
                }
                
            } catch (SQLException e) {
                handleSQLException(e);
                System.out.println(e);
            } finally {
                try {
                    conn.close();
                } catch (SQLException e) {
                    handleSQLException(e);
                    System.out.println(e);
                }
            }
        } else {
            System.out.println("Unable to connect to database.");
        }
        return className;
    }
    
    public static String studentName() {
        Connection conn = ConnectMySQL.conn();
        String userName = UserController.getUserName();
        String className = null;

        if (conn != null) {
            try {
                String studentQuery = "SELECT * FROM students WHERE student_code = ?";
                
                try (PreparedStatement studentPstmt = conn.prepareStatement(studentQuery)) {
                    studentPstmt.setString(1, userName);
                    ResultSet studentResult = studentPstmt.executeQuery();
                    if (studentResult.next()) {
                        className = studentResult.getString("name");
                        return className;
                    }
                }
                
            } catch (SQLException e) {
                handleSQLException(e);
                System.out.println(e);
            } finally {
                try {
                    conn.close();
                } catch (SQLException e) {
                    handleSQLException(e);
                    System.out.println(e);
                }
            }
        } else {
            System.out.println("Unable to connect to database.");
        }
        return className;
    }
    
    public static String course() {
        Connection conn = ConnectMySQL.conn();
        String userName = UserController.getUserName();
        String className = null;

        if (conn != null) {
            try {
                String studentQuery = "SELECT * FROM courses c "
                        + "JOIN students s ON s.course_id = c.id "
                        + "WHERE s.student_code = ?";
                
                try (PreparedStatement studentPstmt = conn.prepareStatement(studentQuery)) {
                    studentPstmt.setString(1, userName);
                    ResultSet studentResult = studentPstmt.executeQuery();
                    if (studentResult.next()) {
                        className = studentResult.getString("name");
                        return className;
                    }
                }
                
            } catch (SQLException e) {
                handleSQLException(e);
                System.out.println(e);
            } finally {
                try {
                    conn.close();
                } catch (SQLException e) {
                    handleSQLException(e);
                    System.out.println(e);
                }
            }
        } else {
            System.out.println("Unable to connect to database.");
        }
        return className;
    }
    
    public static String majors() {
        Connection conn = ConnectMySQL.conn();
        String userName = UserController.getUserName();
        String className = null;

        if (conn != null) {
            try {
                String studentQuery = "SELECT * FROM majors m "
                        + "JOIN students s ON s.majors_id = m.id "
                        + "WHERE s.student_code = ?";
                
                try (PreparedStatement studentPstmt = conn.prepareStatement(studentQuery)) {
                    studentPstmt.setString(1, userName);
                    ResultSet studentResult = studentPstmt.executeQuery();
                    if (studentResult.next()) {
                        className = studentResult.getString("name");
                        return className;
                    }
                }
                
            } catch (SQLException e) {
                handleSQLException(e);
                System.out.println(e);
            } finally {
                try {
                    conn.close();
                } catch (SQLException e) {
                    handleSQLException(e);
                    System.out.println(e);
                }
            }
        } else {
            System.out.println("Unable to connect to database.");
        }
        return className;
    }
    
    private static void handleSQLException(SQLException e) {
        Message messageDialog = new Message(null, true);
        messageDialog.showMessage("Lỗi SQL: " + e.getMessage());
    }
}

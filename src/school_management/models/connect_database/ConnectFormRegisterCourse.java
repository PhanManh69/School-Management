package school_management.models.connect_database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import school_management.views.dialog.Message;

public class ConnectFormRegisterCourse {

    public static boolean addSchedule(String studentCode, String subjectCode, String className) {
        Connection conn = ConnectMySQL.conn();

        if (conn != null) {
            try {
                String sql = "INSERT INTO register_course (register, student_id, subject_id, class_id) "
                        + "SELECT \"ok\", s.id, sub.id, c.id "
                        + "FROM students s "
                        + "JOIN subject sub ON sub.subject_code = ? "
                        + "JOIN class c ON c.class_name = ? "
                        + "WHERE s.student_code = ? "
                        + "AND NOT EXISTS ("
                        +     "SELECT 1 "
                        +     "FROM register_course rc "
                        +     "WHERE rc.student_id = s.id AND rc.subject_id = sub.id AND rc.class_id = c.id"
                        + ")";
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, subjectCode);
                    pstmt.setString(2, className);
                    pstmt.setString(3, studentCode);
                    int rowsAffected = pstmt.executeUpdate();
                                        if (rowsAffected > 0) {
                        System.out.println("Data has been inserted successfully!");
                        Message messageDialog = new Message(null, true);
                        messageDialog.showMessage("Đăng ký tín chỉ thành công!");
                    } else {
                        System.out.println("Failed to insert data!");
                        Message messageDialog = new Message(null, true);
                        messageDialog.showMessage("Tín chỉ đã được đăng ký!");
                    }
                    return rowsAffected > 0;
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
        return false;
    }

    public static List<String[]> displayInformation(String studentCode, String semester) {
        Connection conn = ConnectMySQL.conn();
        List<String[]> scheduleInfo = new ArrayList<>();

        String className = ConnectLogin.className();

        if (conn != null) {
            try {
                String sql = "SELECT IFNULL(rc.register, '') AS register, "
                        + "CONCAT('Lớp ', sub.name, ' - ', cl.class_name) AS subject_class, sub.subject_code, "
                        + "CONCAT('Từ ', se.start_date, ' đến ', se.end_date) AS time_learn, "
                        + "l.name, sub.number_credits "
                        + "FROM open_courses oc "
                        + "JOIN subject sub ON oc.subject_id = sub.id "
                        + "JOIN lecturers l ON oc.lecturers_id = l.id "
                        + "JOIN class cl ON oc.class_id = cl.id "
                        + "JOIN courses cou ON oc.course_id = cou.id "
                        + "JOIN majors m ON oc.majors_id = m.id "
                        + "JOIN semester se ON oc.semester = se.name "
                        + "JOIN students s ON s.course_id = cou.id AND s.majors_id = m.id "
                        + "LEFT JOIN register_course rc ON rc.student_id = s.id AND rc.subject_id = sub.id AND rc.class_id = cl.id "
                        + "WHERE s.student_code = ? AND oc.semester = ? AND cl.class_name = ?";

                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, studentCode);
                    pstmt.setString(2, semester);
                    pstmt.setString(3, className);
                    ResultSet rs = pstmt.executeQuery();
                    while (rs.next()) {
                        String getSubjectInfo = rs.getString("register");
                        String subjectClass = rs.getString("subject_class");
                        String subjectCode = rs.getString("sub.subject_code");
                        String timeLearn = rs.getString("time_learn");
                        String lecturersName = rs.getString("l.name");
                        String numberCredits = rs.getString("sub.number_credits");
                        String[] info = {getSubjectInfo, subjectClass, subjectCode, timeLearn, lecturersName, numberCredits};
                        scheduleInfo.add(info);
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
        return scheduleInfo;
    }

    public static String searchRegisterDate(String semester) {
        Connection conn = ConnectMySQL.conn();
        String date = null;

        if (conn != null) {
            try {
                String sql = "SELECT register_time FROM open_courses WHERE semester = ? LIMIT 1";
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, semester);
                    ResultSet rs = pstmt.executeQuery();
                    if (rs.next()) {
                        Date registerDate = rs.getDate("register_time");
                        if (registerDate != null) {
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            date = dateFormat.format(registerDate);
                        }
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
        return date;
    }

    private static void handleSQLException(SQLException e) {
        Message messageDialog = new Message(null, true);
        messageDialog.showMessage("Lỗi SQL: " + e.getMessage());
    }
}

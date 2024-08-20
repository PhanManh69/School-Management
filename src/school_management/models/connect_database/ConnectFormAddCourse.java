package school_management.models.connect_database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import school_management.views.dialog.Message;

public class ConnectFormAddCourse {

    public static boolean addSchedule(String registerTime, String semester, String coursesName,
            String majorsName, String subjectName, String classRoom, String lecturersName) {
        Connection conn = ConnectMySQL.conn();

        if (conn != null) {
            try {
                String sql = "INSERT INTO open_courses (open_registration, register_time, semester, "
                        + "course_id, majors_id, subject_id, class_id, lecturers_id) "
                        + "SELECT ?, ?, ?, cou.id, m.id, s.id, c.id, l.id "
                        + "FROM subject s "
                        + "JOIN majors m ON s.majors_id = m.id "
                        + "JOIN lecturers_subject ls ON s.id = ls.subject_id "
                        + "JOIN lecturers l ON ls.lecturers_id = l.id "
                        + "JOIN class c ON s.majors_id = s.majors_id "
                        + "JOIN courses cou ON c.course_id = cou.id "
                        + "WHERE cou.name = ? AND m.majors_code = ? AND s.name = ? AND c.class_name = ? AND l.name = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, "Mở");
                    pstmt.setString(2, registerTime);
                    pstmt.setString(3, semester);
                    pstmt.setString(4, coursesName);
                    pstmt.setString(5, majorsName);
                    pstmt.setString(6, subjectName);
                    pstmt.setString(7, classRoom);
                    pstmt.setString(8, lecturersName);
                    int rowsAffected = pstmt.executeUpdate();
                    System.out.println("Data has been inserted successfully!");
                    Message messageDialog = new Message(null, true);
                    messageDialog.showMessage("Lưu thông tin thành công!");
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

    public static boolean deleteSchedule(String id) {
        Connection conn = ConnectMySQL.conn();

        if (conn != null) {
            try {
                String sql = "DELETE FROM open_courses WHERE id = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, id);
                    int rowsAffected = pstmt.executeUpdate();

                    if (rowsAffected > 0) {
                        System.out.println("Data has been deleted successfully!");
                        Message messageDialog = new Message(null, true);
                        messageDialog.showMessage("Xóa thông tin thành công!");
                    } else {
                        Message messageDialog = new Message(null, true);
                        messageDialog.showMessage("Không thể xóa dữ liệu khỏi bảng!");
                    }

                    return rowsAffected > 0;
                }
            } catch (SQLException e) {
                handleSQLException(e);
                System.out.println(e);
                return false;
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
            return false;
        }
    }

    public static List<String[]> displayInformation() {
        Connection conn = ConnectMySQL.conn();
        List<String[]> scheduleInfo = new ArrayList<>();

        if (conn != null) {
            try {
                String sql = "SELECT o.id, o.open_registration, s.subject_code, s.name, l.name, c.class_name, o.semester, o.register_time "
                        + "FROM open_courses o "
                        + "JOIN subject s ON o.subject_id = s.id "
                        + "JOIN lecturers l ON o.lecturers_id = l.id "
                        + "JOIN class c ON o.class_id = c.id ";

                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    ResultSet rs = pstmt.executeQuery();
                    while (rs.next()) {
                        String id = rs.getString("o.id");
                        String openRegistration = rs.getString("o.open_registration");
                        String subjectCode = rs.getString("s.subject_code");
                        String subjectName = rs.getString("s.name");
                        String lecturersName = rs.getString("l.name");
                        String className = rs.getString("c.class_name");
                        String semester = rs.getString("o.semester");
                        String registerTime = rs.getString("o.register_time");

                        String[] info = {id, openRegistration, subjectCode, subjectName, lecturersName, className, semester, registerTime};
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

    public static List<String[]> searchInformation(String courseName, String majorsCode, String className) {
        Connection conn = ConnectMySQL.conn();
        List<String[]> scheduleInfo = new ArrayList<>();

        if (conn != null) {
            try {
                String sql = "SELECT o.id, o.open_registration, s.subject_code, s.name, l.name, c.class_name, o.semester, o.register_time "
                        + "FROM open_courses o "
                        + "JOIN subject s ON o.subject_id = s.id "
                        + "JOIN lecturers l ON o.lecturers_id = l.id "
                        + "JOIN class c ON o.class_id = c.id "
                        + "JOIN courses cou ON o.course_id = cou.id "
                        + "JOIN majors m ON o.majors_id = m.id "
                        + "WHERE cou.name = ? AND m.majors_code = ? AND c.class_name = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, courseName);
                    pstmt.setString(2, majorsCode);
                    pstmt.setString(3, className);
                    ResultSet rs = pstmt.executeQuery();
                    while (rs.next()) {
                        String id = rs.getString("o.id");
                        String openRegistration = rs.getString("o.open_registration");
                        String subjectCode = rs.getString("s.subject_code");
                        String subjectName = rs.getString("s.name");
                        String lecturersName = rs.getString("l.name");
                        String classNameInfo = rs.getString("c.class_name");
                        String semester = rs.getString("o.semester");
                        String registerTime = rs.getString("o.register_time");

                        String[] info = {id, openRegistration, subjectCode, subjectName, lecturersName, classNameInfo, semester, registerTime};
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

    public static List<String> getLecturers(String subjectName, String majorsName) {
        Connection conn = ConnectMySQL.conn();

        if (conn != null) {
            try {
                String sql = "SELECT l.name FROM lecturers_subject ls "
                        + "INNER JOIN subject s ON ls.subject_id = s.id "
                        + "JOIN lecturers l ON ls.lecturers_id = l.id "
                        + "JOIN majors m ON m.id = s.majors_id "
                        + "WHERE s.name = ? AND m.majors_code = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, subjectName);
                    pstmt.setString(2, majorsName);
                    ResultSet rs = pstmt.executeQuery();
                    List<String> listClassName = new ArrayList<>();
                    while (rs.next()) {
                        String className = rs.getString("l.name");
                        listClassName.add(className);
                    }
                    return listClassName;
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
        return null;
    }

    public static String getDate(String semester) {
        Connection conn = ConnectMySQL.conn();
        String startDate = null;

        if (conn != null) {
            try {
                String sql = "SELECT start_date FROM semester "
                        + "WHERE name = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, semester);
                    ResultSet rs = pstmt.executeQuery();
                    if (rs.next()) {
                        String startDateStr = rs.getString("start_date");
                        LocalDate startLocalDate = LocalDate.parse(startDateStr);
                        LocalDate newStartDate = startLocalDate.minusWeeks(2);
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                        startDate = newStartDate.format(formatter);
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
        return startDate;
    }
    
    public static List<String> getSemester() {
        Connection conn = ConnectMySQL.conn();

        if (conn != null) {
            try {
                String sql = "SELECT name FROM semester ";
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    ResultSet rs = pstmt.executeQuery();
                    List<String> listClassName = new ArrayList<>();
                    while (rs.next()) {
                        String className = rs.getString("name");
                        listClassName.add(className);
                    }
                    return listClassName;
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
        return null;
    }

    private static void handleSQLException(SQLException e) {
        Message messageDialog = new Message(null, true);
        messageDialog.showMessage("Lỗi SQL: " + e.getMessage());
    }
}

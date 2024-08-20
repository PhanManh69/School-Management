package school_management.models.connect_database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import school_management.models.user_controller.UserController;
import school_management.views.dialog.Message;

public class ConnectFormEvaluate {

    public static void saveInformation(String id, String pointCC, String pointKT, String pointCK) {
        Connection conn = ConnectMySQL.conn();
        if (conn != null) {
            try {
                String updateSql = "UPDATE transcript SET "
                        + "point_cc = ?, "
                        + "point_kt = ?, "
                        + "point_ck = ? "
                        + "WHERE id = ?";
                try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                    updateStmt.setString(1, pointCC);
                    updateStmt.setString(2, pointKT);
                    updateStmt.setString(3, pointCK);
                    updateStmt.setString(4, id);
                    updateStmt.executeUpdate();
                }
            } catch (SQLException ex) {
                handleSQLException(ex);
                System.out.println(ex);
            } finally {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    handleSQLException(ex);
                    System.out.println(ex);
                }
            }
        } else {
            Message messageDialog = new Message(null, true);
            messageDialog.showMessage("Không thể kết nối với CSDL!");
        }
    }

    public static List<String[]> searchInformation(String idMajorsInfo, String classNameInfo, String nameSubjectInfo) {
        Connection conn = ConnectMySQL.conn();
        List<String[]> scheduleInfo = new ArrayList<>();

        if (conn != null) {
            try {
                String sql = "SELECT t.id, s.student_code, s.name, c.class_name, sub.id, sub.name, "
                        + "t.point_cc, t.point_kt, t.point_ck "
                        + "FROM transcript t "
                        + "INNER JOIN students s ON t.student_id = s.id "
                        + "JOIN subject sub ON t.subject_id = sub.id "
                        + "JOIN class c ON s.class_id = c.id "
                        + "JOIN courses cou ON s.course_id = cou.id "
                        + "JOIN majors m ON s.majors_id = m.id "
                        + "WHERE c.class_name = ? AND sub.name = ?";

                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, classNameInfo);
                    pstmt.setString(2, nameSubjectInfo);
                    ResultSet rs = pstmt.executeQuery();
                    while (rs.next()) {
                        String id = rs.getString("t.id");
                        String infoCodeStudent = rs.getString("s.student_code");
                        String nameStudent = rs.getString("s.name");
                        String className = rs.getString("c.class_name");
                        String idSubject = rs.getString("sub.id");
                        String nameSubject = rs.getString("sub.name");
                        String pointCC = rs.getString("t.point_cc");
                        String pointKT = rs.getString("t.point_kt");
                        String pointCK = rs.getString("t.point_ck");
                        String[] info = {id, infoCodeStudent, nameStudent, className, idSubject, nameSubject, pointCC, pointKT, pointCK};
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

    public static List<String> getCourse() {
        Connection conn = ConnectMySQL.conn();

        if (conn != null) {
            try {
                String sql = "SELECT name FROM courses";
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    ResultSet rs = pstmt.executeQuery();
                    List<String> listIdCourse = new ArrayList<>();
                    while (rs.next()) {
                        String idCourse = rs.getString("name");
                        listIdCourse.add(idCourse);
                    }
                    return listIdCourse;
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

    public static List<String> getMojors() {
        Connection conn = ConnectMySQL.conn();

        if (conn != null) {
            try {
                String sql = "SELECT majors_code FROM majors";
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    ResultSet rs = pstmt.executeQuery();
                    List<String> listIdCourse = new ArrayList<>();
                    while (rs.next()) {
                        String idCourse = rs.getString("majors_code");
                        listIdCourse.add(idCourse);
                    }
                    return listIdCourse;
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

    public static List<String> getClassRoom(String idMajors, String idCourse) {
        Connection conn = ConnectMySQL.conn();

        if (conn != null) {
            try {
                String sql = "SELECT c.class_name FROM class c "
                        + "INNER JOIN majors m ON c.majors_id = m.id "
                        + "INNER JOIN courses cou ON c.course_id = cou.id "
                        + "WHERE m.majors_code = ? AND cou.name = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, idMajors);
                    pstmt.setString(2, idCourse);
                    ResultSet rs = pstmt.executeQuery();
                    List<String> listClassName = new ArrayList<>();
                    while (rs.next()) {
                        String className = rs.getString("c.class_name");
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

    public static List<String> getSubject(String idMajors) {
        Connection conn = ConnectMySQL.conn();

        String userName = UserController.getUserName();
        String password = UserController.getPassword();

        int role = ConnectLogin.login(userName, password);

        if (conn != null) {
            try {
                String sql = "";
                if (role == 2) {
                    sql = "SELECT s.name FROM subject s "
                            + "INNER JOIN majors m ON s.majors_id = m.id "
                            + "INNER JOIN lecturers_subject ls ON s.id = ls.subject_id "
                            + "INNER JOIN lecturers l ON ls.lecturers_id = l.id "
                            + "WHERE m.majors_code = ? AND l.lecturers_code = ?";
                } else if (role == 3) {
                    sql = "SELECT s.name FROM subject s "
                            + "INNER JOIN majors m ON s.majors_id = m.id "
                            + "WHERE m.majors_code = ?";
                }
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    if (role == 2) {
                        pstmt.setString(1, idMajors);
                        pstmt.setString(2, userName);
                    } else if (role == 3) {
                        pstmt.setString(1, idMajors);
                    }
                    ResultSet rs = pstmt.executeQuery();
                    List<String> listSubjectName = new ArrayList<>();
                    while (rs.next()) {
                        String nameSubject = rs.getString("s.name");
                        listSubjectName.add(nameSubject);
                    }
                    return listSubjectName;
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

    public static List<String[]> displayInformation() {
        Connection conn = ConnectMySQL.conn();
        List<String[]> scheduleInfo = new ArrayList<>();

        if (conn != null) {
            try {
                String sql = "SELECT t.id, s.student_code, s.name, c.class_name, sub.id, sub.name, "
                        + "t.point_cc, t.point_kt, t.point_ck "
                        + "FROM transcript t "
                        + "INNER JOIN students s ON t.student_id = s.id "
                        + "JOIN subject sub ON t.subject_id = sub.id "
                        + "JOIN class c ON s.class_id = c.id "
                        + "JOIN courses cou ON s.course_id = cou.id "
                        + "JOIN majors m ON s.majors_id = m.id ";

                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    ResultSet rs = pstmt.executeQuery();
                    while (rs.next()) {
                        String id = rs.getString("t.id");
                        String infoCodeStudent = rs.getString("s.student_code");
                        String nameStudent = rs.getString("s.name");
                        String className = rs.getString("c.class_name");
                        String codeSubject = rs.getString("sub.id");
                        String nameSubject = rs.getString("sub.name");
                        String pointCC = rs.getString("t.point_cc");
                        String pointKT = rs.getString("t.point_kt");
                        String pointCK = rs.getString("t.point_ck");
                        String[] info = {id, infoCodeStudent, nameStudent, className, codeSubject,
                            nameSubject, pointCC, pointKT, pointCK};
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

    private static void handleSQLException(SQLException e) {
        Message messageDialog = new Message(null, true);
        messageDialog.showMessage("Lỗi SQL: " + e.getMessage());
    }
}

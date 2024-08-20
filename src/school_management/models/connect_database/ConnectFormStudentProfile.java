package school_management.models.connect_database;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import static school_management.models.connect_database.ConnectMySQL.conn;
import school_management.views.dialog.Message;

public class ConnectFormStudentProfile {

    public static boolean importToDatabase(File excelFile) {
        try {
            try (FileInputStream fis = new FileInputStream(excelFile); Workbook workbook = new XSSFWorkbook(fis)) {
                Sheet sheet = workbook.getSheetAt(0);

                Connection conn = ConnectMySQL.conn();

                if (conn != null) {
                    try {
                        String sqlStudent = "INSERT INTO students (student_code, name, birthday, address, "
                                + "phone_number, email, course_id, majors_id, class_id, total_money, password) "
                                + "SELECT ?, ?, ?, ?, ?, ?, cou.id, m.id, c.id, ?, ? "
                                + "FROM students s "
                                + "JOIN courses cou ON s.course_id = cou.id "
                                + "JOIN majors m ON s.majors_id = m.id "
                                + "JOIN class c ON s.class_id = c.id "
                                + "WHERE cou.name = ? AND m.name = ? AND c.class_name = ? "
                                + "LIMIT 1";
                        
                        String sqlEvaluate = "INSERT INTO transcript (student_id, subject_id) "
                                + "SELECT s.id, sub.id "
                                + "FROM students s "
                                + "JOIN majors m ON s.majors_id = m.id "
                                + "JOIN subject sub ON sub.majors_id = m.id "
                                + "WHERE s.student_code = ?";
                        try (PreparedStatement pstmtStudent = conn.prepareStatement(sqlStudent);
                                PreparedStatement pstmtEvaluate = conn.prepareStatement(sqlEvaluate)) {
                            for (int i = 0; i <= sheet.getLastRowNum(); i++) {
                                Row row = sheet.getRow(i);
                                
                                pstmtEvaluate.setString(1, row.getCell(0).getStringCellValue());
                                
                                pstmtStudent.setString(1, row.getCell(0).getStringCellValue());
                                pstmtStudent.setString(2, row.getCell(1).getStringCellValue());

                                Cell birthdayCell = row.getCell(2);
                                if (birthdayCell.getCellType() == CellType.NUMERIC) {
                                    Date date = birthdayCell.getDateCellValue();
                                    pstmtStudent.setDate(3, new java.sql.Date(date.getTime()));
                                } else {
                                    pstmtStudent.setString(3, "2003-09-06");
                                }

                                pstmtStudent.setString(4, row.getCell(3).getStringCellValue());
                                pstmtStudent.setString(5, row.getCell(4).getStringCellValue());
                                pstmtStudent.setString(6, row.getCell(5).getStringCellValue());
                                pstmtStudent.setString(9, row.getCell(6).getStringCellValue());
                                pstmtStudent.setString(10, row.getCell(7).getStringCellValue());
                                pstmtStudent.setString(11, row.getCell(8).getStringCellValue());
                                pstmtStudent.setInt(7, (int) row.getCell(9).getNumericCellValue());
                                pstmtStudent.setString(8, row.getCell(10).getStringCellValue());
                                
                                pstmtStudent.executeUpdate();
                                pstmtEvaluate.executeUpdate();
                            }
                        }
                        conn.close();

                        Message messageDialog = new Message(null, true);
                        messageDialog.showMessage("Dữ liệu đã được nhập vào cơ sở dữ liệu thành công!");
                        return true;
                    } catch (SQLException e) {
                        handleSQLException(e);
                        System.out.println(e);
                    }
                } else {
                    System.out.println("Unable to connect to the database.");
                }
            }
        } catch (IOException e) {
            Message messageDialog = new Message(null, true);
            messageDialog.showMessage("Lỗi không thể mở file Excel!");
            System.out.println(e);
        }
        return false;
    }

    public static boolean updateInfo(String codeStudent, String nameStudent, String birthdayStudent, String addressStudent, String phoneStudent, String emailStudent) {
        Connection conn = ConnectMySQL.conn();

        if (conn != null) {
            try {
                String sql = "UPDATE students "
                        + "SET name = ?, birthday = ?, address= ?, phone_number = ?, email = ? "
                        + "WHERE student_code = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, nameStudent);
                    pstmt.setString(2, birthdayStudent);
                    pstmt.setString(3, addressStudent);
                    pstmt.setString(4, phoneStudent);
                    pstmt.setString(5, emailStudent);
                    pstmt.setString(6, codeStudent);
                    int rowsAffected = pstmt.executeUpdate();

                    System.out.println("Data has been updated successfully!");
                    Message messageDialog = new Message(null, true);
                    messageDialog.showMessage("Cập nhật thông tin thành công!");

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

    public static boolean deleteInfo(String codeStudent) {
        Connection conn = ConnectMySQL.conn();

        if (conn != null) {
            try {
                String deleteTranscriptSql = "DELETE FROM transcript WHERE student_id IN (SELECT id FROM students WHERE student_code = ?)";
                String deleteStudentSql = "DELETE FROM students WHERE student_code = ?";
                try (PreparedStatement pstmtTranscript = conn.prepareStatement(deleteTranscriptSql); PreparedStatement pstmtStudent = conn.prepareStatement(deleteStudentSql)) {
                    pstmtTranscript.setString(1, codeStudent);
                    pstmtStudent.setString(1, codeStudent);
                    int transcriptRowsAffected = pstmtTranscript.executeUpdate();
                    int studentRowsAffected = pstmtStudent.executeUpdate();

                    if (transcriptRowsAffected > 0 || studentRowsAffected > 0) {
                        System.out.println("Data has been deleted successfully!");
                        Message messageDialog = new Message(null, true);
                        messageDialog.showMessage("Xóa thông tin thành công!");
                    } else {
                        Message messageDialog = new Message(null, true);
                        messageDialog.showMessage("Không thể xóa dữ liệu khỏi bảng!");
                    }

                    return (transcriptRowsAffected > 0 || studentRowsAffected > 0);
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

    public static List<String[]> searchByClassName(String className) {
        Connection conn = ConnectMySQL.conn();
        List<String[]> scheduleInfo = new ArrayList<>();

        if (conn != null) {
            try {
                String sql = "SELECT s.*, m.name, c.class_name "
                        + "FROM students s "
                        + "INNER JOIN majors m ON s.majors_id = m.id "
                        + "INNER JOIN class c ON s.class_id = c.id "
                        + "WHERE c.class_name LIKE ?";
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, "%" + className + "%");
                    ResultSet rs = pstmt.executeQuery();
                    while (rs.next()) {
                        String codeStudent = rs.getString("s.student_code");
                        String nameStudent = rs.getString("s.name");
                        String infoClassName = rs.getString("c.class_name");
                        String nameMajors = rs.getString("m.name");
                        String[] info = {codeStudent, nameStudent, infoClassName, nameMajors};
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

    public static String[] displayInformationDetail(String codeStudent) {
        Connection conn = conn();

        if (conn != null) {
            try {
                String sql = "SELECT s.*, m.name, m.training_time, c.start_date, c.end_date, c.name, cl.class_name "
                        + "FROM students s "
                        + "INNER JOIN majors m ON s.majors_id = m.id "
                        + "INNER JOIN courses c ON s.course_id = c.id "
                        + "INNER JOIN class cl ON s.class_id = cl.id "
                        + "WHERE s.student_code = ?";

                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, codeStudent);
                    ResultSet rs = pstmt.executeQuery();
                    if (rs.next()) {
                        String infoCodeStudent = rs.getString("s.student_code");
                        String nameStudent = rs.getString("s.name");
                        String className = rs.getString("cl.class_name");
                        String nameMajors = rs.getString("m.name");
                        String idCourse = rs.getString("c.name");
                        String birthdayStudent = rs.getString("s.birthday");
                        String addressStudent = rs.getString("s.address");
                        String phoneStudent = rs.getString("s.phone_number");
                        String emailStudent = rs.getString("s.email");
                        String trainingTime = rs.getString("m.training_time");
                        String startDate = rs.getString("c.start_date");
                        String endDate = rs.getString("c.end_date");
                        return new String[]{infoCodeStudent, nameStudent, className, nameMajors, idCourse,
                            birthdayStudent, addressStudent, phoneStudent, emailStudent, trainingTime, startDate, endDate};
                    } else {
                        Message messageDialog = new Message(null, true);
                        messageDialog.showMessage("Không tìm thấy thông tin sinh viên!");
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
        return null;
    }

    public static List<String[]> displayInformation() {
        Connection conn = ConnectMySQL.conn();
        List<String[]> scheduleInfo = new ArrayList<>();

        if (conn != null) {
            try {
                String sql = "SELECT s.*, m.name, c.class_name "
                        + "FROM students s "
                        + "INNER JOIN majors m ON s.majors_id = m.id "
                        + "INNER JOIN class c ON s.class_id = c.id ";
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    ResultSet rs = pstmt.executeQuery();
                    while (rs.next()) {
                        String codeStudent = rs.getString("s.student_code");
                        String nameStudent = rs.getString("s.name");
                        String className = rs.getString("c.class_name");
                        String nameMajors = rs.getString("m.name");
                        String[] info = {codeStudent, nameStudent, className, nameMajors};
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

    public static int getCountStudent() {
        Connection conn = ConnectMySQL.conn();
        int rowCount = 0;

        if (conn != null) {
            try {
                String sql = "SELECT COUNT(*) AS row_count FROM students";
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    ResultSet rs = pstmt.executeQuery();
                    if (rs.next()) {
                        rowCount = rs.getInt("row_count");
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
        return rowCount;
    }

    public static int getCountMajors() {
        Connection conn = ConnectMySQL.conn();
        int rowCount = 0;

        if (conn != null) {
            try {
                String sql = "SELECT COUNT(*) AS row_count FROM majors";
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    ResultSet rs = pstmt.executeQuery();
                    if (rs.next()) {
                        rowCount = rs.getInt("row_count");
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
        return rowCount;
    }

    public static int getCountClass() {
        Connection conn = ConnectMySQL.conn();
        int rowCount = 0;

        if (conn != null) {
            try {
                String sql = "SELECT COUNT(*) AS row_count FROM class";
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    ResultSet rs = pstmt.executeQuery();
                    if (rs.next()) {
                        rowCount = rs.getInt("row_count");
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
        return rowCount;
    }

    public static int getCountCourse() {
        Connection conn = ConnectMySQL.conn();
        int rowCount = 0;

        if (conn != null) {
            try {
                String sql = "SELECT COUNT(*) AS row_count FROM courses";
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    ResultSet rs = pstmt.executeQuery();
                    if (rs.next()) {
                        rowCount = rs.getInt("row_count");
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
        return rowCount;
    }

    private static void handleSQLException(SQLException e) {
        Message messageDialog = new Message(null, true);
        messageDialog.showMessage("Lỗi SQL: " + e.getMessage());
    }
}

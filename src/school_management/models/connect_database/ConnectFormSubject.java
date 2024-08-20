package school_management.models.connect_database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import school_management.views.dialog.Message;

public class ConnectFormSubject {
    
    public static List<String[]> searchInformation(String majorsCode) {
        Connection conn = ConnectMySQL.conn();
        List<String[]> scheduleInfo = new ArrayList<>();

        if (conn != null) {
            try {
                String sql = "SELECT s.block_knowledge, COUNT(s.block_knowledge) AS number_subject, "
                        + "SUM(s.number_credits) AS number_credits "
                        + "FROM subject s JOIN majors m ON s.majors_id = m.id "
                        + "WHERE m.majors_code = ? "
                        + "GROUP BY s.block_knowledge";

                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, majorsCode);
                    ResultSet rs = pstmt.executeQuery();
                    while (rs.next()) {
                        String blockKnowledge = rs.getString("s.block_knowledge");
                        String numberSubject = rs.getString("number_subject");
                        String numberCredits = rs.getString("number_credits");
                        String[] info = {blockKnowledge, numberSubject, numberCredits};
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

    public static List<String[]> displayInformation() {
        Connection conn = ConnectMySQL.conn();
        List<String[]> scheduleInfo = new ArrayList<>();

        if (conn != null) {
            try {
                String sql = "SELECT s.block_knowledge "
                        + "FROM subject s JOIN majors m ON s.majors_id = m.id "
                        + "GROUP BY s.block_knowledge";

                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    ResultSet rs = pstmt.executeQuery();
                    while (rs.next()) {
                        String blockKnowledg = rs.getString("s.block_knowledge");
                        String[] info = {blockKnowledg};
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
    
    public static List<String[]> displayInformationTrain(String majorsCode, String blockKnowledge) {
        Connection conn = ConnectMySQL.conn();
        List<String[]> scheduleInfo = new ArrayList<>();

        if (conn != null) {
            try {
                String sql = "SELECT s.subject_code, s.name, s.number_credits "
                        + "FROM subject s "
                        + "JOIN majors m ON s.majors_id = m.id "
                        + "WHERE m.majors_code = ? AND s.block_knowledge = ?";

                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, majorsCode);
                    pstmt.setString(2, blockKnowledge);
                    ResultSet rs = pstmt.executeQuery();
                    while (rs.next()) {
                        String subjectCode = rs.getString("s.subject_code");
                        String nameSubject = rs.getString("s.name");
                        String numberCredits = rs.getString("s.number_credits");
                        String[] info = {subjectCode, nameSubject, numberCredits};
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

package school_management.models.connect_database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import school_management.models.user_controller.UserController;
import school_management.views.dialog.Message;

public class ConnectFormLerningOut {
    public static List<String[]> displayInformation() {
        String userName = UserController.getUserName();
        Connection conn = ConnectMySQL.conn();
        List<String[]> scheduleInfo = new ArrayList<>();

        if (conn != null) {
            try {
                String sql = "SELECT sub.subject_code, sub.name, sub.number_credits, "
                        + "CASE "
                        + "     WHEN (t.point_cc * 0.1 + t.point_kt * 0.2 + t.point_ck * 0.7) >= 4 THEN 'Đạt' "
                        + "     WHEN (t.point_cc * 0.1 + t.point_kt * 0.2 + t.point_ck * 0.7) < 4 THEN 'Thi Lại' "
                        + "     ELSE 'Chưa Thi' "
                        + "END AS evaluate, "
                        + "s.student_code, t.point_cc, t.point_kt, t.point_ck, "
                        + "CASE  "
                        + "     WHEN (t.point_cc * 0.1 + t.point_kt * 0.2 + t.point_ck * 0.7) > 9 THEN 'A' "
                        + "     WHEN (t.point_cc * 0.1 + t.point_kt * 0.2 + t.point_ck * 0.7) > 7 THEN 'B' "
                        + "     WHEN (t.point_cc * 0.1 + t.point_kt * 0.2 + t.point_ck * 0.7) > 4 THEN 'C' "
                        + "     WHEN (t.point_cc * 0.1 + t.point_kt * 0.2 + t.point_ck * 0.7) <= 4 THEN 'D' "
                        + "     ELSE '' "
                        + "END AS point_text "
                        + "FROM transcript t "
                        + "JOIN subject sub ON t.subject_id = sub.id "
                        + "JOIN students s ON t.student_id = s.id "
                        + "WHERE s.student_code = ?";

                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, userName);
                    ResultSet rs = pstmt.executeQuery();
                    while (rs.next()) {
                        String subjectCode = rs.getString("sub.subject_code");
                        String nameSubject = rs.getString("sub.name");
                        String numberCredits = rs.getString("sub.number_credits");
                        String evaluate = rs.getString("evaluate");
                        String studentCode = rs.getString("s.student_code");
                        String pointCC = rs.getString("t.point_cc");
                        String pointKT = rs.getString("t.point_kt");
                        String pointCK = rs.getString("t.point_ck");
                        String pointText = rs.getString("point_text");
                        String[] info = {subjectCode, nameSubject, numberCredits, evaluate, studentCode,
                            pointCC, pointKT, pointCK, pointText};
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

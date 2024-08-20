package school_management.controllers.form;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import school_management.models.connect_database.ConnectFormAddCourse;
import school_management.models.connect_database.ConnectFormRegisterCourse;
import school_management.models.user_controller.UserController;
import school_management.utilities.swing.Button;
import school_management.utilities.swing.table.MultilineTableCellRenderer;
import school_management.views.dialog.Message;
import school_management.utilities.swing.table.Table;

public class RegisterTheCourse {

    public void addCourse(Button btnAdd, Table table1, JLabel txtStudentCode, JComboBox cbCourse) {
        btnAdd.addActionListener((ActionEvent e) -> {
            int selectedRow = table1.getSelectedRow();
            if (selectedRow != -1) {
                String studentCode = txtStudentCode.getText();
                String subjectCode = (String) table1.getValueAt(selectedRow, 2);
                String classRow = (String) table1.getValueAt(selectedRow, 1);
                int length = classRow.length();
                String className = length > 8 ? classRow.substring(length - 8) : classRow;
                String semester = (String) cbCourse.getSelectedItem();

                String searchRegisterDate = ConnectFormRegisterCourse.searchRegisterDate(semester);
                System.out.println(searchRegisterDate);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                LocalDate registerDate = LocalDate.parse(searchRegisterDate, formatter);
                LocalDate presentDate = LocalDate.now();

                if (registerDate.isAfter(presentDate)) {
                    boolean success = ConnectFormRegisterCourse.addSchedule(studentCode, subjectCode, className);
                    if (success) {
                        displayInformation(txtStudentCode, cbCourse, table1);
                    }
                } else {
                    Message messageDialog = new Message(null, true);
                    messageDialog.showMessage("Bạn đã hết thời hạn đăng ký học phần!");
                }
            } else {
                Message messageDialog = new Message(null, true);
                messageDialog.showMessage("Hãy chọn môn hoc muốn đăng ký!");
            }
        });
    }

    public void displayInformation(JLabel txtStudentCode, JComboBox cbCourse, Table table1) {
        String studentCode = UserController.getUserName();
        String semester = ((String) cbCourse.getSelectedItem() != null) ? (String) cbCourse.getSelectedItem() : "1_2021_2022";

        DefaultTableModel tableModel = (DefaultTableModel) table1.getModel();
        tableModel.setRowCount(0);

        List<String[]> listInfo = ConnectFormRegisterCourse.displayInformation(studentCode, semester);
        if (listInfo != null) {
            for (String[] row : listInfo) {
                tableModel.addRow(row);
            }
        }
        
        txtStudentCode.setText(studentCode);
    }

    public void searchSemester(JComboBox cbCourse, JLabel txtStudentCode, Table table1) {
        cbCourse.addActionListener((ActionEvent e) -> {
            displayInformation(txtStudentCode, cbCourse, table1);
        });
    }

    public void fillSemesterComboBox(JComboBox cbCourse) {
        List<String> semesters = ConnectFormAddCourse.getSemester();
        if (semesters != null) {
            Comparator<String> semesterComparator = (s1, s2) -> {
                int year1 = Integer.parseInt(s1.substring(2, 6));
                int year2 = Integer.parseInt(s2.substring(2, 6));
                int part1 = Integer.parseInt(s1.substring(0, 1));
                int part2 = Integer.parseInt(s2.substring(0, 1));

                if (year1 != year2) {
                    return year1 - year2;
                } else {
                    return part1 - part2;
                }
            };

            Collections.sort(semesters, semesterComparator);
            cbCourse.setModel(new DefaultComboBoxModel<>(semesters.toArray(String[]::new)));
        }
    }

    public void tableEdit(Table table1, JScrollPane jScrollPane1) {
        table1.setFont(new java.awt.Font("Times New Roman", Font.PLAIN, 24));
        table1.fixTable(jScrollPane1);
        TableColumn columnOK = table1.getColumnModel().getColumn(0);
        columnOK.setPreferredWidth(150);
        TableColumn columnClass = table1.getColumnModel().getColumn(1);
        columnClass.setPreferredWidth(450);
        columnClass.setCellRenderer(new MultilineTableCellRenderer());
        TableColumn columnCode = table1.getColumnModel().getColumn(2);
        columnCode.setPreferredWidth(300);
        TableColumn columnDate = table1.getColumnModel().getColumn(3);
        columnDate.setPreferredWidth(380);
        columnDate.setCellRenderer(new MultilineTableCellRenderer());
        TableColumn columnTeacher = table1.getColumnModel().getColumn(4);
        columnTeacher.setPreferredWidth(300);
        TableColumn columnNumber = table1.getColumnModel().getColumn(5);
        columnNumber.setPreferredWidth(150);
    }

    public void buttonEdit(Button btnAdd) {
        btnAdd.setFont(new java.awt.Font("sansserif", 1, 24));
    }
}

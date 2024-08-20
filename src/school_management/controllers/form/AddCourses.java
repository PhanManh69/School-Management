package school_management.controllers.form;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import school_management.models.connect_database.ConnectFormAddCourse;
import school_management.models.connect_database.ConnectFormEvaluate;
import school_management.models.connect_database.ConnectFormRegisterCourse;
import school_management.models.connect_database.ConnectLogin;
import school_management.models.user_controller.UserController;
import school_management.utilities.swing.Button;
import school_management.views.dialog.AddCourse;
import school_management.views.dialog.Message;
import school_management.utilities.swing.table.Table;

public class AddCourses {

    public void addContent(Button btnAdd, Table table) {
        btnAdd.addActionListener((ActionEvent e) -> {
            AddCourse addCourseDialog = new AddCourse(null, true);
            addCourseDialog.setVisible(true);
            displayInformation(table);
        });
    }

    public void deleteContent(Button btnDelete, Table table) {
        btnDelete.addActionListener((ActionEvent e) -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                String registerDateString = (String) table.getValueAt(selectedRow, 7);
                
                LocalDate registerDate = LocalDate.parse(registerDateString, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                LocalDate presentDate = LocalDate.now();

                if (registerDate.isAfter(presentDate)) {
                    String idSchedule = (String) table.getValueAt(selectedRow, 0);
                    boolean success = ConnectFormAddCourse.deleteSchedule(idSchedule);
                    if (success) {
                        displayInformation(table);
                    }
                } else {
                    Message messageDialog = new Message(null, true);
                    messageDialog.showMessage("Học phần này đã được học!");
                }
            } else {
                Message messageDialog = new Message(null, true);
                messageDialog.showMessage("Hãy chọn nội dung muốn xóa!");
            }
        });
    }

    public void displayInformation(Table table) {
        DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
        tableModel.setRowCount(0);

        List<String[]> listInfo = ConnectFormAddCourse.displayInformation();
        if (listInfo != null) {
            for (String[] row : listInfo) {
                tableModel.addRow(row);
            }
        }
    }

    private void performSearch(JComboBox cbCourse, JComboBox cbMajors, JComboBox cbClassName, Table table) {
        String courseName = (String) cbCourse.getSelectedItem();
        String majorsCode = (String) cbMajors.getSelectedItem();
        String className = (String) cbClassName.getSelectedItem();

        DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
        tableModel.setRowCount(0);

        List<String[]> listInfo = ConnectFormAddCourse.searchInformation(courseName, majorsCode, className);
        if (listInfo != null) {
            for (String[] row : listInfo) {
                tableModel.addRow(row);
            }
        }
    }

    public void searchCourse(JComboBox cbCourse, JComboBox cbMajors, JComboBox cbClass, Table table) {
        cbCourse.addActionListener((ActionEvent e) -> {
            fillMajorsComboBox(cbMajors);
            cbClass.removeAllItems();
            displayInformation(table);
        });
    }

    public void searchMajors(JComboBox cbCourse, JComboBox cbMajors, JComboBox cbClass, Table table) {
        cbMajors.addActionListener((ActionEvent e) -> {
            fillClassComboBox(cbClass, cbCourse, cbMajors);
            displayInformation(table);
        });
    }

    public void searchClass(JComboBox cbCourse, JComboBox cbMajors, JComboBox cbClass, Table table) {
        cbClass.addActionListener((ActionEvent e) -> {
            performSearch(cbCourse, cbMajors, cbClass, table);
        });
    }

    public void fillCourseComboBox(JComboBox cbCourse) {
        List<String> name = ConnectFormEvaluate.getCourse();
        if (name != null) {
            cbCourse.setModel(new DefaultComboBoxModel<>(name.toArray(String[]::new)));
        }
    }

    public void fillMajorsComboBox(JComboBox cbMajors) {
        List<String> name = ConnectFormEvaluate.getMojors();
        if (name != null) {
            cbMajors.setModel(new DefaultComboBoxModel<>(name.toArray(String[]::new)));
        }
    }

    public void fillClassComboBox(JComboBox cbClass, JComboBox cbCourse, JComboBox cbMajors) {
        String idMajors = (String) cbMajors.getSelectedItem();
        String idCourse = (String) cbCourse.getSelectedItem();

        List<String> name = ConnectFormEvaluate.getClassRoom(idMajors, idCourse);
        if (name != null) {
            cbClass.setModel(new DefaultComboBoxModel<>(name.toArray(String[]::new)));
        }
    }

    public void tableEdit(Table table, JScrollPane jScrollPane1) {
        table.setFont(new java.awt.Font("Times New Roman", Font.PLAIN, 24));
        table.fixTable(jScrollPane1);
        TableColumn column0 = table.getColumnModel().getColumn(0);
        column0.setPreferredWidth(10);
        TableColumn columnCredits = table.getColumnModel().getColumn(3);
        columnCredits.setPreferredWidth(300);
        TableColumn column3 = table.getColumnModel().getColumn(4);
        column3.setPreferredWidth(150);
    }

    public void buttonEdit(Button btnAdd, Button btnDelete) {
        btnAdd.setFont(new java.awt.Font("sansserif", 1, 24));
        btnDelete.setFont(new java.awt.Font("sansserif", 1, 24));

        String userName = UserController.getUserName();
        String password = UserController.getPassword();

        int role = ConnectLogin.login(userName, password);

        switch (role) {
            case 2 -> {
                btnAdd.setVisible(false);
                btnDelete.setVisible(false);
            }
            case 3 -> {
                btnAdd.setVisible(true);
                btnDelete.setVisible(true);
            }
            default -> {
            }
        }
    }
}

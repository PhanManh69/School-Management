package school_management.controllers.form;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import school_management.models.connect_database.ConnectFormLearn;
import school_management.models.connect_database.ConnectFormStudentProfile;
import school_management.models.object.ModelCard;
import school_management.models.object.ModelStudent;
import school_management.utilities.swing.icon.GoogleMaterialDesignIcons;
import school_management.utilities.swing.icon.IconFontSwing;
import school_management.utilities.swing.table.EventAction;
import school_management.views.dialog.Message;
import school_management.utilities.swing.table.Table;
import school_management.views.component.Card;
import com.toedter.calendar.JDateChooser;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JScrollPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.TableColumnModel;
import school_management.models.connect_database.ConnectLogin;
import school_management.models.user_controller.UserController;
import school_management.utilities.swing.Button;
import school_management.utilities.swing.exporter.ExcelExporter;
import school_management.views.dialog.AddCourse;

public class StudentProfile {

    private EventAction eventAction;
    boolean checkTickClass = false;

    public void searchInfo(JComboBox classRoom, Table table1, Card card1, Card card2, Card card3, Card card4, JTextField txtCodeStudent, JTextField txtNameStudent, JTextField txtClassRoom,
            JTextField txtMajors, JTextField txtCourse, JTextField txtAddress, JTextField txtPhone, JTextField txtEmail,
            JTextField txtTrainingTime, JTextField txtStartDate, JTextField txtEndDate, JDateChooser birthday) {

        classRoom.addActionListener((ActionEvent e) -> {
            performSearch(classRoom, table1, card1, card2, card3, card4, txtCodeStudent, txtNameStudent, txtClassRoom, txtMajors,
                    txtCourse, txtAddress, txtPhone, txtEmail, txtTrainingTime, txtStartDate, txtEndDate, birthday);
        });
    }

    private void performSearch(JComboBox classRoom, Table table1, Card card1, Card card2, Card card3, Card card4, JTextField txtCodeStudent, JTextField txtNameStudent, JTextField txtClassRoom,
            JTextField txtMajors, JTextField txtCourse, JTextField txtAddress, JTextField txtPhone, JTextField txtEmail,
            JTextField txtTrainingTime, JTextField txtStartDate, JTextField txtEndDate, JDateChooser birthday) {

        eventActions(table1, card1, card2, card3, card4, txtCodeStudent, txtNameStudent, txtClassRoom, txtMajors, txtCourse,
                txtAddress, txtPhone, txtEmail, txtTrainingTime, txtStartDate, txtEndDate, birthday);
        String selectedClass = (String) classRoom.getSelectedItem();

        DefaultTableModel tableModel = (DefaultTableModel) table1.getModel();
        tableModel.setRowCount(0);

        List<String[]> listInfo = ConnectFormStudentProfile.searchByClassName(selectedClass);
        if (listInfo != null) {
            for (String[] row : listInfo) {
                ModelStudent student = new ModelStudent(row[0], row[1], row[2], row[3]);
                tableModel.addRow(student.toRowTable(eventAction));
            }
        }
    }

    public void displayContent(Table table1, JTextField txtCodeStudent, JTextField txtNameStudent, JTextField txtClassRoom, JTextField txtMajors, JTextField txtCourse,
            JTextField txtAddress, JTextField txtPhone, JTextField txtEmail, JTextField txtTrainingTime, JTextField txtStartDate, JTextField txtEndDate, JDateChooser birthday) {
        table1.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = table1.getSelectedRow();
                if (selectedRow != -1) {
                    String codeStudent = (String) table1.getValueAt(selectedRow, 0);
                    String[] infoCodeStudent = ConnectFormStudentProfile.displayInformationDetail(codeStudent);
                    if (infoCodeStudent != null && infoCodeStudent.length == 12) {
                        SwingUtilities.invokeLater(() -> {
                            txtCodeStudent.setText(infoCodeStudent[0]);
                            txtNameStudent.setText(infoCodeStudent[1]);
                            txtClassRoom.setText(infoCodeStudent[2]);
                            txtMajors.setText(infoCodeStudent[3]);
                            txtCourse.setText(infoCodeStudent[4]);
                            txtAddress.setText(infoCodeStudent[6]);
                            txtPhone.setText(infoCodeStudent[7]);
                            txtEmail.setText(infoCodeStudent[8]);
                            txtTrainingTime.setText(infoCodeStudent[9]);
                            txtStartDate.setText(infoCodeStudent[10]);
                            txtEndDate.setText(infoCodeStudent[11]);

                            String birthdayDateStr = infoCodeStudent[5];

                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-yy");
                            Date birthdayDateValue;
                            try {
                                birthdayDateValue = dateFormat.parse(birthdayDateStr);
                                birthday.setDate(birthdayDateValue);
                            } catch (ParseException ex) {
                            }

                            edittable(txtNameStudent, txtAddress, txtPhone, txtEmail);
                        });
                    }
                }
            }
        });
    }

    public void initTableData(Table table1, Card card1, Card card2, Card card3, Card card4, JTextField txtCodeStudent, JTextField txtNameStudent, JTextField txtClassRoom,
            JTextField txtMajors, JTextField txtCourse, JTextField txtAddress, JTextField txtPhone, JTextField txtEmail,
            JTextField txtTrainingTime, JTextField txtStartDate, JTextField txtEndDate, JDateChooser birthday) {

        eventActions(table1, card1, card2, card3, card4, txtCodeStudent, txtNameStudent, txtClassRoom, txtMajors, txtCourse, txtAddress, txtPhone, txtEmail, txtTrainingTime, txtStartDate, txtEndDate, birthday);
        DefaultTableModel tableModel = (DefaultTableModel) table1.getModel();
        tableModel.setRowCount(0);

        List<String[]> listInfo = ConnectFormStudentProfile.displayInformation();
        if (listInfo != null) {
            for (String[] row : listInfo) {
                ModelStudent student = new ModelStudent(row[0], row[1], row[2], row[3]);
                tableModel.addRow(student.toRowTable(eventAction));
            }
        }
    }

    public void importExcel(Button buttonAdd, Table table1, Card card1, Card card2, Card card3, Card card4, JTextField txtCodeStudent, JTextField txtNameStudent, JTextField txtClassRoom,
            JTextField txtMajors, JTextField txtCourse, JTextField txtAddress, JTextField txtPhone, JTextField txtEmail,
            JTextField txtTrainingTime, JTextField txtStartDate, JTextField txtEndDate, JDateChooser birthday) {
        buttonAdd.addActionListener((ActionEvent e) -> {
//            JFileChooser fileChooser = new JFileChooser();
//            FileNameExtensionFilter filter = new FileNameExtensionFilter("Excel files", "xlsx", "xls");
//            fileChooser.setFileFilter(filter);
//            int returnValue = fileChooser.showOpenDialog(null);
//            if (returnValue == JFileChooser.APPROVE_OPTION) {
//                File selectedFile = fileChooser.getSelectedFile();
//                boolean success = ConnectFormStudentProfile.importToDatabase(selectedFile);
//                if (success) {
//                    initTableData(table1, card1, card2, card3, card4, txtCodeStudent, txtNameStudent, txtClassRoom, txtMajors,
//                            txtCourse, txtAddress, txtPhone, txtEmail, txtTrainingTime, txtStartDate, txtEndDate, birthday);
//                }
//            }
            AddCourse addCourseDialog = new AddCourse(null, true);
            addCourseDialog.setVisible(true);
        });
    }

    public void downloadExcel(Button btnDownload, Table table2, JComboBox classRoom) {
        String[] className = {""};

        classRoom.addActionListener((ActionEvent e) -> {
            className[0] = (String) classRoom.getSelectedItem();
            checkTickClass = true;
        });

        btnDownload.addActionListener((ActionEvent e) -> {
            if (checkTickClass) {
                ExcelExporter.exportToExcelStudent(table2, className[0]);
            } else {
                Message messageDialog = new Message(null, true);
                messageDialog.showMessage("Phải chọn lớp muốn xuất!");
            }
        });
    }

    public void eventActions(Table table1, Card card1, Card card2, Card card3, Card card4, JTextField txtCodeStudent, JTextField txtNameStudent, JTextField txtClassRoom,
            JTextField txtMajors, JTextField txtCourse, JTextField txtAddress, JTextField txtPhone, JTextField txtEmail,
            JTextField txtTrainingTime, JTextField txtStartDate, JTextField txtEndDate, JDateChooser birthday) {
        eventAction = new EventAction() {
            @Override
            public void delete(ModelStudent student) {
                deleteInfo(table1, card1, card2, card3, card4, txtCodeStudent, txtNameStudent, txtClassRoom, txtMajors,
                        txtCourse, txtAddress, txtPhone, txtEmail, txtTrainingTime, txtStartDate, txtEndDate, birthday);
            }

            @Override
            public void update(ModelStudent student) {
                updateInfo(table1, card1, card2, card3, card4, txtCodeStudent, txtNameStudent, txtClassRoom, txtMajors,
                        txtCourse, txtAddress, txtPhone, txtEmail, txtTrainingTime, txtStartDate, txtEndDate, birthday);
            }
        };
    }

    public void updateInfo(Table table1, Card card1, Card card2, Card card3, Card card4, JTextField txtCodeStudent, JTextField txtNameStudent, JTextField txtClassRoom,
            JTextField txtMajors, JTextField txtCourse, JTextField txtAddress, JTextField txtPhone, JTextField txtEmail,
            JTextField txtTrainingTime, JTextField txtStartDate, JTextField txtEndDate, JDateChooser birthday) {
        String codeStudent = txtCodeStudent.getText();
        String nameStudent = txtNameStudent.getText();
        String addressStudent = txtAddress.getText();
        String phoneStudent = txtPhone.getText();
        String emailStudent = txtEmail.getText();

        var birthdayDateValue = birthday.getDate();
        var dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd");
        if (birthdayDateValue != null) {
            String birthdayStudent = dateFormat.format(birthdayDateValue);

            if (codeStudent != null && !codeStudent.isEmpty() && nameStudent != null && !nameStudent.isEmpty()
                    && birthdayStudent != null && !birthdayStudent.isEmpty()
                    && addressStudent != null && !addressStudent.isEmpty()
                    && phoneStudent != null && !phoneStudent.isEmpty()
                    && emailStudent != null && !emailStudent.isEmpty()) {

                ConnectFormStudentProfile.updateInfo(codeStudent, nameStudent, birthdayStudent, addressStudent, phoneStudent, emailStudent);

                initTableData(table1, card1, card2, card3, card4, txtCodeStudent, txtNameStudent, txtClassRoom, txtMajors,
                        txtCourse, txtAddress, txtPhone, txtEmail, txtTrainingTime, txtStartDate, txtEndDate, birthday);
                resetUI(txtCodeStudent, txtNameStudent, txtClassRoom, txtMajors, txtCourse, txtAddress, txtPhone, txtEmail, txtTrainingTime, txtStartDate, txtEndDate, birthday);
            } else {
                Message messageDialog = new Message(null, true);
                messageDialog.showMessage("Vui lòng nhập đầy đủ thông tin!");
            }
        } else {
            Message messageDialog = new Message(null, true);
            messageDialog.showMessage("Vui lòng chọn sinh viên muốn sửa!");
        }
    }

    public void deleteInfo(Table table1, Card card1, Card card2, Card card3, Card card4, JTextField txtCodeStudent, JTextField txtNameStudent, JTextField txtClassRoom,
            JTextField txtMajors, JTextField txtCourse, JTextField txtAddress, JTextField txtPhone, JTextField txtEmail,
            JTextField txtTrainingTime, JTextField txtStartDate, JTextField txtEndDate, JDateChooser birthday) {
        int selectedRow = table1.getSelectedRow();
        if (selectedRow != -1) {
            String codeStudent = (String) table1.getValueAt(selectedRow, 0);
            boolean success = ConnectFormStudentProfile.deleteInfo(codeStudent);

            if (success) {

                initTableData(table1, card1, card2, card3, card4, txtCodeStudent, txtNameStudent, txtClassRoom, txtMajors, txtCourse,
                        txtAddress, txtPhone, txtEmail, txtTrainingTime, txtStartDate, txtEndDate, birthday);
                resetUI(txtCodeStudent, txtNameStudent, txtClassRoom, txtMajors, txtCourse, txtAddress, txtPhone, txtEmail, txtTrainingTime, txtStartDate, txtEndDate, birthday);
                initCardData(card1, card2, card3, card4);
            }
        } else {
            Message messageDialog = new Message(null, true);
            messageDialog.showMessage("Hãy chọn nội dung muốn xóa!");
        }
    }

    public void fillClassRoomComboBox(JComboBox classRoom, JDateChooser birthday) {
        List<String> classNames = ConnectFormLearn.getClassNames();
        if (classNames != null) {
            classRoom.setModel(new DefaultComboBoxModel<>(classNames.toArray(String[]::new)));
        }
        classRoom.addItem("");
        birthday.getDateEditor().setEnabled(false);
        birthday.setDateFormatString("yyyy-MM-dd");
    }

    public void tableEdit(Table table1, JScrollPane jScrollPane1) {
        table1.setFont(new java.awt.Font("Time New Roman", Font.PLAIN, 24));
        table1.fixTable(jScrollPane1);
        TableColumn columnAction = table1.getColumnModel().getColumn(4);
        columnAction.setPreferredWidth(50);
        TableColumn columnMajors = table1.getColumnModel().getColumn(3);
        columnMajors.setPreferredWidth(250);
        TableColumn columnClassRoom = table1.getColumnModel().getColumn(2);
        columnClassRoom.setPreferredWidth(100);
        TableColumn columnFullName = table1.getColumnModel().getColumn(1);
        columnFullName.setPreferredWidth(200);

        String userName = UserController.getUserName();
        String password = UserController.getPassword();

        int role = ConnectLogin.login(userName, password);

        if (role == 2) {
            hideColumn(table1, 4);
        }
    }

    public void buttonEdit(Button btnAdd, Button btnExport) {
        btnAdd.setFont(new java.awt.Font("sansserif", 1, 24));
        btnExport.setFont(new java.awt.Font("sansserif", 1, 24));

        String userName = UserController.getUserName();
        String password = UserController.getPassword();

        int role = ConnectLogin.login(userName, password);

        switch (role) {
            case 2 -> {
                btnAdd.setVisible(false);
            }
            case 3 -> {
                btnAdd.setVisible(true);
            }
            default -> {
            }
        }
    }

    public void textBoxEdit(JTextField txtNameStudent, JTextField txtAddress, JTextField txtPhone, JTextField txtEmail) {
        txtNameStudent.setEditable(false);
        txtAddress.setEditable(false);
        txtPhone.setEditable(false);
        txtEmail.setEditable(false);
    }

    public void edittable(JTextField txtNameStudent, JTextField txtAddress, JTextField txtPhone, JTextField txtEmail) {
        txtNameStudent.setEditable(true);
        txtAddress.setEditable(true);
        txtPhone.setEditable(true);
        txtEmail.setEditable(true);
    }

    public void initCardData(Card card1, Card card2, Card card3, Card card4) {
        Icon icon1 = IconFontSwing.buildIcon(GoogleMaterialDesignIcons.PEOPLE, 60, new Color(255, 255, 255, 100), new Color(255, 255, 255, 15));
        card1.setData(new ModelCard("Tổng Số Sinh Viên", ConnectFormStudentProfile.getCountStudent(), 80, icon1));
        Icon icon2 = IconFontSwing.buildIcon(GoogleMaterialDesignIcons.MONETIZATION_ON, 60, new Color(255, 255, 255, 100), new Color(255, 255, 255, 15));
        card2.setData(new ModelCard("Tổng Số Lớp", ConnectFormStudentProfile.getCountClass(), 60, icon2));
        Icon icon3 = IconFontSwing.buildIcon(GoogleMaterialDesignIcons.SHOPPING_BASKET, 60, new Color(255, 255, 255, 100), new Color(255, 255, 255, 15));
        card3.setData(new ModelCard("Tổng Số Ngành Học", ConnectFormStudentProfile.getCountMajors(), 40, icon3));
        Icon icon4 = IconFontSwing.buildIcon(GoogleMaterialDesignIcons.BUSINESS_CENTER, 60, new Color(255, 255, 255, 100), new Color(255, 255, 255, 15));
        card4.setData(new ModelCard("Tổng Số Khóa Học", ConnectFormStudentProfile.getCountCourse(), 20, icon4));
    }

    public void resetUI(JTextField txtCodeStudent, JTextField txtNameStudent, JTextField txtClassRoom, JTextField txtMajors, JTextField txtCourse,
            JTextField txtAddress, JTextField txtPhone, JTextField txtEmail, JTextField txtTrainingTime, JTextField txtStartDate, JTextField txtEndDate, JDateChooser birthday) {
        txtCodeStudent.setText("");
        txtNameStudent.setText("");
        txtClassRoom.setText("");
        txtMajors.setText("");
        txtCourse.setText("");
        txtAddress.setText("");
        txtPhone.setText("");
        txtEmail.setText("");
        txtTrainingTime.setText("");
        txtStartDate.setText("");
        txtEndDate.setText("");
        birthday.setDate(null);
    }

    public void hideColumn(Table table, int columnIndex) {
        TableColumnModel columnModel = table.getColumnModel();
        TableColumn column = columnModel.getColumn(columnIndex);
        column.setMaxWidth(0);
        column.setMinWidth(0);
        column.setPreferredWidth(0);
        column.setWidth(0);
        column.setResizable(false);
    }
}

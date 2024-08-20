package school_management.controllers.form;

import java.awt.Desktop;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import com.toedter.calendar.JDateChooser;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import school_management.models.connect_database.ConnectFormLearn;
import school_management.models.connect_database.ConnectLogin;
import school_management.models.user_controller.UserController;
import school_management.utilities.swing.Button;
import school_management.views.dialog.AddSchedule;
import school_management.views.dialog.EditSchedule;
import school_management.views.dialog.Message;
import school_management.utilities.swing.table.Table;

public class Learn {
    public void addContent(Button btnAdd, Table table1) {
        btnAdd.addActionListener((ActionEvent e) -> {
            AddSchedule addScheduleDialog = new AddSchedule(null, true);
            addScheduleDialog.setVisible(true);
            displayInformation(table1);
        });
        displayInformation(table1);
    }

    public void editContent(Button btnEdit, Table table1) {
        btnEdit.addActionListener((ActionEvent e) -> {
            int selectedRow = table1.getSelectedRow();
            if (selectedRow != -1) {
                String idSchedule = (String) table1.getValueAt(selectedRow, 0);
                String className = (String) table1.getValueAt(selectedRow, 1);
                String dateApply = (String) table1.getValueAt(selectedRow, 2);
                String content = (String) table1.getValueAt(selectedRow, 3);

                EditSchedule editScheduleDialog = new EditSchedule(null, true);
                editScheduleDialog.setScheduleInfo(idSchedule, className, dateApply, content);
                editScheduleDialog.setVisible(true);
                displayInformation(table1);
            } else {
                Message messageDialog = new Message(null, true);
                messageDialog.showMessage("Hãy chọn nội dung muốn sửa!");
            }
        });
    }

    public void deleteContent(Button btnDelete, Table table1) {
        btnDelete.addActionListener((ActionEvent e) -> {
            int selectedRow = table1.getSelectedRow();
            if (selectedRow != -1) {
                String idSchedule = (String) table1.getValueAt(selectedRow, 0);
                boolean success = ConnectFormLearn.deleteSchedule(idSchedule);
                if (success) {
                    displayInformation(table1);
                }
            } else {
                Message messageDialog = new Message(null, true);
                messageDialog.showMessage("Hãy chọn nội dung muốn xóa!");
            }
        });
    }

    private void performSearch(JDateChooser startDate, JDateChooser endDate, JComboBox classRoom, Table table1) {
        var startDateValue = startDate.getDate();
        var endDateValue = endDate.getDate();
        var dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd");

        String selectedClass = (String) classRoom.getSelectedItem();
        String startDateString = (startDateValue != null) ? dateFormat.format(startDateValue) : "1000-01-01";
        String endDateString = (endDateValue != null) ? dateFormat.format(endDateValue) : "9999-12-31";

        DefaultTableModel tableModel = (DefaultTableModel) table1.getModel();
        tableModel.setRowCount(0);
        
        List<String[]> listInfo = ConnectFormLearn.searchByClassNameAndDateRange(selectedClass, startDateString, endDateString);
        if (listInfo != null) {
            for (String[] row : listInfo) {
                tableModel.addRow(row);
            }
        }
    }

    public void searchInfo(JComboBox classRoom, JDateChooser startDate, JDateChooser endDate, Table table1) {
        classRoom.addActionListener((ActionEvent e) -> {
            performSearch(startDate, endDate, classRoom, table1);
        });

        startDate.getDateEditor().addPropertyChangeListener((PropertyChangeEvent evt) -> {
            if ("date".equals(evt.getPropertyName())) {
                performSearch(startDate, endDate, classRoom, table1);
            }
        });

        endDate.getDateEditor().addPropertyChangeListener((PropertyChangeEvent evt) -> {
            if ("date".equals(evt.getPropertyName())) {
                performSearch(startDate, endDate, classRoom, table1);
            }
        });
    }

    public void fillClassRoomComboBox(JComboBox classRoom) {
        List<String> classNames = ConnectFormLearn.getClassNames();
        if (classNames != null) {
            classRoom.setModel(new DefaultComboBoxModel<>(classNames.toArray(String[]::new)));
        }
    }

    public void displayInformation(Table table1) {
        DefaultTableModel tableModel = (DefaultTableModel) table1.getModel();
        tableModel.setRowCount(0);

        List<String[]> listInfo = ConnectFormLearn.displayInformation();
        if (listInfo != null) {
            for (String[] row : listInfo) {
                tableModel.addRow(row);
            }
        }
    }

    public void tableEdit(Table table1, JScrollPane jScrollPane1) {
        table1.setFont(new java.awt.Font("Times New Roman", Font.PLAIN, 24));
        table1.fixTable(jScrollPane1);
        TableColumn columnID = table1.getColumnModel().getColumn(0);
        columnID.setPreferredWidth(130);
        TableColumn columnClass = table1.getColumnModel().getColumn(1);
        columnClass.setPreferredWidth(80);
        TableColumn columnDate = table1.getColumnModel().getColumn(2);
        columnDate.setPreferredWidth(250);
        TableColumn columnContent = table1.getColumnModel().getColumn(3);
        columnContent.setPreferredWidth(700);

        table1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int column = table1.columnAtPoint(e.getPoint());
                int row = table1.rowAtPoint(e.getPoint());
                if (column == 3) {
                    String url = (String) table1.getValueAt(row, column);
                    if (url != null && !url.isEmpty()) {
                        openWebPage(url);
                    }
                }
            }
        });
    }

    private void openWebPage(String url) {
        try {
            Desktop.getDesktop().browse(new URI(url));
        } catch (IOException | URISyntaxException e) {
        }
    }

    public void buttonEdit(Button btnAdd, Button btnEdit, Button btnDelete, JComboBox classRoom) {
        btnAdd.setFont(new java.awt.Font("sansserif", 1, 24));
        btnEdit.setFont(new java.awt.Font("sansserif", 1, 24));
        btnDelete.setFont(new java.awt.Font("sansserif", 1, 24));
        classRoom.addItem("");
        
        String userName = UserController.getUserName();
        String password = UserController.getPassword();
        
        int role = ConnectLogin.login(userName, password);
        
        switch (role) {
            case 1 -> {
                btnAdd.setVisible(false);
                btnEdit.setVisible(false);
                btnDelete.setVisible(false);
            }
            case 2 -> {
                btnAdd.setVisible(false);
                btnEdit.setVisible(false);
                btnDelete.setVisible(false);
            }
            case 3 -> {
                btnAdd.setVisible(true);
                btnEdit.setVisible(true);
                btnDelete.setVisible(true);
            }
            default -> {
            }
        }
    }

    public void dateChooserEdit(JDateChooser startDate, JDateChooser endDate) {
        startDate.getDateEditor().setEnabled(false);
        endDate.getDateEditor().setEnabled(false);
        startDate.setDateFormatString("yyyy-MM-dd");
        endDate.setDateFormatString("yyyy-MM-dd");
    }
}

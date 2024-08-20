package school_management.controllers.form;

import java.awt.Font;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import school_management.models.user_controller.UserController;
import school_management.models.connect_database.ConnectFormHome;
import school_management.models.connect_database.ConnectLogin;
import school_management.utilities.swing.Button;
import school_management.views.dialog.AddNotification;
import school_management.views.dialog.EditNotification;
import school_management.views.dialog.Message;
import school_management.utilities.swing.table.Table;

public class Home {
    public void tableEdit(Table table1, JScrollPane jScrollPane1) {
        table1.setFont(new java.awt.Font("Times New Roman", Font.PLAIN, 24));
        table1.fixTable(jScrollPane1);
        TableColumn columnDate = table1.getColumnModel().getColumn(0);
        columnDate.setPreferredWidth(350);
        TableColumn columnTitle = table1.getColumnModel().getColumn(1);
        columnTitle.setPreferredWidth(750);
    }

    public void buttonEdit(Button btnAdd, Button btnEdit, Button btnDelete) {
        btnAdd.setFont(new java.awt.Font("sansserif", 1, 24));
        btnEdit.setFont(new java.awt.Font("sansserif", 1, 24));
        btnDelete.setFont(new java.awt.Font("sansserif", 1, 24));
        
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

    public void textFileEdit(JTextArea txtContent, JScrollPane jScrollPane2) {
        txtContent.setLineWrap(true);
        txtContent.setWrapStyleWord(true);
        txtContent.setEditable(false);
        jScrollPane2.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
    }

    public void addContent(Button btnAdd, Table table1, JLabel lbTitle, JTextArea txtContent) {
        btnAdd.addActionListener((ActionEvent e) -> {
            AddNotification addNotificationDialog = new AddNotification(null, true);
            addNotificationDialog.setVisible(true);
            displayInformation(table1);
        });
        displayInformation(table1);
        displayContent(table1, lbTitle, txtContent);
    }

    public void editContent(Button btnEdit, Table table1, JLabel lbTitle, JTextArea txtContent) {
        btnEdit.addActionListener((ActionEvent e) -> {
            int selectedRow = table1.getSelectedRow();
            if (selectedRow != -1) {
                String dateNotification = (String) table1.getValueAt(selectedRow, 0);
                String[] notificationInfo = ConnectFormHome.getInfoInNotification(dateNotification);
                if (notificationInfo != null && notificationInfo.length == 2) {
                    EditNotification editNotificationDialog = new EditNotification(null, true);
                    editNotificationDialog.setNotificationInfo(dateNotification, notificationInfo[0], notificationInfo[1]);
                    editNotificationDialog.setVisible(true);
                    displayInformation(table1);
                    resetUI(lbTitle, txtContent);
                }
            } else {
                Message messageDialog = new Message(null, true);
                messageDialog.showMessage("Hãy chọn nội dung muốn sửa!");
            }
        });
    }

    public void deleteContent(Button btnDelete, Table table1, JLabel lbTitle, JTextArea txtContent) {
        btnDelete.addActionListener((ActionEvent e) -> {
            int selectedRow = table1.getSelectedRow();
            if (selectedRow != -1) {
                String dateNotification = (String) table1.getValueAt(selectedRow, 0);
                boolean success = ConnectFormHome.deleteNotification(dateNotification);
                if (success) {
                    displayInformation(table1);
                    resetUI(lbTitle, txtContent);
                }
            } else {
                Message messageDialog = new Message(null, true);
                messageDialog.showMessage("Hãy chọn nội dung muốn xóa!");
            }
        });
    }

    public void displayInformation(Table table1) {
        DefaultTableModel tableModel = (DefaultTableModel) table1.getModel();
        tableModel.setRowCount(0);

        String[][] listInfo = ConnectFormHome.displayInformation();
        if (listInfo != null) {
            for (String[] row : listInfo) {
                tableModel.insertRow(0, row);
            }
        }
    }

    public void displayContent(Table table1, JLabel lbTitle, JTextArea txtContent) {
        table1.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = table1.getSelectedRow();
                if (selectedRow != -1) {
                    String dateNotification = (String) table1.getValueAt(selectedRow, 0);
                    String[] notificationInfo = ConnectFormHome.getInfoInNotification(dateNotification);
                    if (notificationInfo != null && notificationInfo.length == 2) {
                        lbTitle.setText(notificationInfo[0]);
                        txtContent.setText(notificationInfo[1]);
                    }
                }
            }
        });
    }

    public void resetUI(JLabel lbTitle, JTextArea txtContent) {
        lbTitle.setText("Nội Dung");
        txtContent.setText("");
    }
}

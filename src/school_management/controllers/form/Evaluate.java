package school_management.controllers.form;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import school_management.models.connect_database.ConnectFormEvaluate;
import school_management.utilities.swing.Button;
import school_management.utilities.swing.exporter.ExcelExporter;
import school_management.views.dialog.Message;
import school_management.utilities.swing.table.Table;

public class Evaluate {

    private boolean isSubjectSearchTriggered = false;

    public void saveInformation(Button btnSave, Table table2) {
        btnSave.addActionListener((ActionEvent e) -> {
            boolean success = true;
            DefaultTableModel model = (DefaultTableModel) table2.getModel();
            int rowCount = model.getRowCount();

            if (rowCount > 0 && isSubjectSearchTriggered) {
                for (int i = 0; i < rowCount; i++) {
                    String id = (String) model.getValueAt(i, 0);
                    String pointCC = (String) model.getValueAt(i, 6);
                    String pointKT = (String) model.getValueAt(i, 7);
                    String pointCK = (String) model.getValueAt(i, 8);

                    if ((pointCC == null || isValidPoint(pointCC))
                            && (pointKT == null || isValidPoint(pointKT))
                            && (pointCK == null || isValidPoint(pointCK))) {

                        try {
                            if (pointCC != null) {
                                double cc = Double.parseDouble(pointCC);
                                if (!(cc >= 0 && cc <= 10)) {
                                    showMessage("Điểm nằm trong khoảng từ 0 đến 10!");
                                    success = false;
                                    return;
                                }
                            }

                            if (pointKT != null) {
                                double kt = Double.parseDouble(pointKT);
                                if (!(kt >= 0 && kt <= 10)) {
                                    showMessage("Điểm nằm trong khoảng từ 0 đến 10!");
                                    success = false;
                                    return;
                                }
                            }

                            if (pointCK != null) {
                                double ck = Double.parseDouble(pointCK);
                                if (!(ck >= 0 && ck <= 10)) {
                                    showMessage("Điểm nằm trong khoảng từ 0 đến 10!");
                                    success = false;
                                    return;
                                }
                            }

                            ConnectFormEvaluate.saveInformation(id, pointCC, pointKT, pointCK);

                        } catch (NumberFormatException ex) {
                            showMessage("Điểm không hợp lệ!");
                            success = false;
                        }
                    } else {
                        showMessage("Điểm không hợp lệ!");
                        success = false;
                    }
                }
            } else {
                showMessage("Lỗi hệ thống, hãy thử lại!");
                success = false;
            }
            if (success) {
                showMessage("Lưu thông tin thành công!");
            } else {
                showMessage("Lưu thông tin không thành công!");
            }
        });
    }

    public void downloadExcel(Button btnDownload, Table table2, JComboBox cbClass, JComboBox cbSubject) {
        String[] className = {""};
        String[] subjectName = {""};

        cbClass.addActionListener((ActionEvent e) -> {
            className[0] = (String) cbClass.getSelectedItem();
        });

        cbSubject.addActionListener((ActionEvent e) -> {
            subjectName[0] = (String) cbSubject.getSelectedItem();
        });

        btnDownload.addActionListener((ActionEvent e) -> {
            ExcelExporter.exportToExcelPoint(table2, className[0], subjectName[0]);
        });
    }

    public void searchCourse(JComboBox cbCourse, JComboBox cbClass, JComboBox cbSubject, JComboBox cbMajors) {
        cbCourse.addActionListener((ActionEvent e) -> {
            fillMajorsComboBox(cbMajors);
            cbClass.removeAllItems();
            cbSubject.removeAllItems();
        });
    }

    public void searchMajors(JComboBox cbMajors, JComboBox cbSubject, JComboBox cbCourse, JComboBox cbClass) {
        cbMajors.addActionListener((ActionEvent e) -> {
            fillClassRoomComboBox(cbCourse, cbMajors, cbClass);
            cbSubject.removeAllItems();
        });
    }

    public void searchClass(JComboBox cbClass, JComboBox cbMajors, JComboBox cbSubject) {
        cbClass.addActionListener((ActionEvent e) -> {
            fillSubjectComboBox(cbMajors, cbSubject);
        });
    }

    public void searchSubject(JComboBox cbSubject, JComboBox cbMajors, JComboBox cbClass, Table table2) {
        cbSubject.addActionListener((ActionEvent e) -> {
            searchInformation(cbMajors, cbClass, cbSubject, table2);
            isSubjectSearchTriggered = true;
        });
    }

    public void searchInformation(JComboBox cbMajors, JComboBox cbClass, JComboBox cbSubject, Table table2) {
        String idMajorsInfo = (String) cbMajors.getSelectedItem();
        String classNameInfo = (String) cbClass.getSelectedItem();
        String nameSubjectInfo = (String) cbSubject.getSelectedItem();

        DefaultTableModel tableModel = (DefaultTableModel) table2.getModel();
        tableModel.setRowCount(0);

        List<String[]> listInfo = ConnectFormEvaluate.searchInformation(idMajorsInfo, classNameInfo, nameSubjectInfo);
        if (listInfo != null) {
            for (String[] row : listInfo) {
                tableModel.addRow(row);
            }
        }
    }

    public void displayInformation(Table table2) {
        DefaultTableModel tableModel = (DefaultTableModel) table2.getModel();

        List<String[]> listInfo = ConnectFormEvaluate.displayInformation();
        if (listInfo != null) {
            for (String[] row : listInfo) {
                tableModel.addRow(row);
            }
        }
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

    public void fillClassRoomComboBox(JComboBox cbCourse, JComboBox cbMajors, JComboBox cbClass) {
        String idCourse = (String) cbCourse.getSelectedItem();
        String idMajors = (String) cbMajors.getSelectedItem();

        List<String> name = ConnectFormEvaluate.getClassRoom(idMajors, idCourse);
        if (name != null) {
            cbClass.setModel(new DefaultComboBoxModel<>(name.toArray(String[]::new)));
        }
    }

    public void fillSubjectComboBox(JComboBox cbMajors, JComboBox cbSubject) {
        String idMajors = (String) cbMajors.getSelectedItem();

        List<String> name = ConnectFormEvaluate.getSubject(idMajors);
        if (name != null) {
            cbSubject.setModel(new DefaultComboBoxModel<>(name.toArray(String[]::new)));
        }
    }

    public void tableEdit(Table table2, JScrollPane jScrollPane2) {
        table2.setFont(new java.awt.Font("Times New Roman", Font.PLAIN, 18));
        table2.fixTable(jScrollPane2);
        TableColumn columnID = table2.getColumnModel().getColumn(0);
        columnID.setPreferredWidth(15);
        TableColumn columnCode = table2.getColumnModel().getColumn(1);
        columnCode.setPreferredWidth(100);
        TableColumn columnName = table2.getColumnModel().getColumn(2);
        columnName.setPreferredWidth(150);
        TableColumn columnClass = table2.getColumnModel().getColumn(3);
        columnClass.setPreferredWidth(100);
        TableColumn columnMMH = table2.getColumnModel().getColumn(4);
        columnMMH.setPreferredWidth(30);
        TableColumn columnNameMH = table2.getColumnModel().getColumn(5);
        columnNameMH.setPreferredWidth(250);
        TableColumn columnCC = table2.getColumnModel().getColumn(6);
        columnCC.setPreferredWidth(55);
        TableColumn columnKT = table2.getColumnModel().getColumn(7);
        columnKT.setPreferredWidth(55);
        TableColumn columnCK = table2.getColumnModel().getColumn(8);
        columnCK.setPreferredWidth(55);
    }

    public void buttonEdit(Button btnSave, Button btnDownload) {
        btnSave.setFont(new java.awt.Font("sansserif", 1, 24));
        btnDownload.setFont(new java.awt.Font("sansserif", 1, 24));
    }

    private boolean isValidPoint(String point) {
        if (point == null) {
            return true;
        }
        try {
            double value = Double.parseDouble(point);
            return value >= 0 && value <= 10;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public void fillDataJTable(Table table2) {
        DefaultTableModel model = (DefaultTableModel) table2.getModel();
        JTable jt = new JTable();
        jt.setModel(model);
    }

    private void showMessage(String message) {
        Message messageDialog = new Message(null, true);
        messageDialog.showMessage(message);
    }
}

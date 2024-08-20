package school_management.controllers.form;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import school_management.models.connect_database.ConnectFormEvaluate;
import school_management.models.connect_database.ConnectFormSubject;
import school_management.utilities.swing.table.Table;

public class Subject {
    private int lastSelectedRow = -1;
    
    public void displayInformation(Table table3) {
        DefaultTableModel tableModel = (DefaultTableModel) table3.getModel();

        List<String[]> listInfo = ConnectFormSubject.displayInformation();
        if (listInfo != null) {
            for (String[] row : listInfo) {
                tableModel.addRow(row);
            }
        }
    }

    public void displayInformationTrain(Table table3, Table table2, JComboBox cbMajors) {
        table3.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = table3.getSelectedRow();
                if (selectedRow != -1 && selectedRow != lastSelectedRow) {
                    lastSelectedRow = selectedRow;

                    String majorsCode = (String) cbMajors.getSelectedItem();
                    String blockKnowledge = (String) table3.getValueAt(selectedRow, 0);

                    DefaultTableModel tableModel = (DefaultTableModel) table2.getModel();
                    while (tableModel.getRowCount() > 0) {
                        tableModel.removeRow(0);
                    }

                    List<String[]> listInfo = ConnectFormSubject.displayInformationTrain(majorsCode, blockKnowledge);
                    if (listInfo != null) {
                        for (String[] row : listInfo) {
                            tableModel.addRow(row);
                        }
                    }
                } else if (selectedRow == lastSelectedRow) {
                    DefaultTableModel tableModel = (DefaultTableModel) table2.getModel();
                    tableModel.removeRow(selectedRow);
                    lastSelectedRow = -1;
                }
            }
        });
    }

    public void searchCourse(JComboBox cbCourses, JComboBox cbMajors, Table table3) {
        cbCourses.addActionListener((ActionEvent e) -> {
            fillMajorsComboBox(cbMajors);
            DefaultTableModel model = (DefaultTableModel) table3.getModel();
            while (model.getRowCount() > 0) {
                model.removeRow(0);
            }
            displayInformation(table3);
        });
    }

    public void searchMajors(JComboBox cbMajors, Table table3) {
        cbMajors.addActionListener((ActionEvent e) -> {
            searchInformation(cbMajors, table3);
        });
    }

    public void fillCourseComboBox(JComboBox cbCourses) {
        List<String> name = ConnectFormEvaluate.getCourse();
        if (name != null) {
            cbCourses.setModel(new DefaultComboBoxModel<>(name.toArray(String[]::new)));
        }
    }

    public void fillMajorsComboBox(JComboBox cbMajors) {
        List<String> name = ConnectFormEvaluate.getMojors();
        if (name != null) {
            cbMajors.setModel(new DefaultComboBoxModel<>(name.toArray(String[]::new)));
        }
    }

    public void searchInformation(JComboBox cbMajors, Table table3) {
        String majorsCode = (String) cbMajors.getSelectedItem();

        DefaultTableModel tableModel = (DefaultTableModel) table3.getModel();
        tableModel.setRowCount(0);

        List<String[]> listInfo = ConnectFormSubject.searchInformation(majorsCode);
        if (listInfo != null) {
            for (String[] row : listInfo) {
                tableModel.addRow(row);
            }
        }
    }

    public void tableEdit(Table table2, Table table3, JScrollPane jScrollPane2, JScrollPane jScrollPane3) {
        table2.setFont(new java.awt.Font("Times New Roman", Font.PLAIN, 24));
        table2.fixTable(jScrollPane2);
        table3.setFont(new java.awt.Font("Times New Roman", Font.PLAIN, 24));
        table3.fixTable(jScrollPane3);
        TableColumn columnCode = table2.getColumnModel().getColumn(0);
        columnCode.setPreferredWidth(100);
        TableColumn columnName = table2.getColumnModel().getColumn(1);
        columnName.setPreferredWidth(500);
        TableColumn columnCredits = table2.getColumnModel().getColumn(2);
        columnCredits.setPreferredWidth(100);
        TableColumn columnEvaluate = table2.getColumnModel().getColumn(3);
        columnEvaluate.setPreferredWidth(80);
        TableColumn columnCodeStudent = table3.getColumnModel().getColumn(0);
        columnCodeStudent.setPreferredWidth(250);
        TableColumn columnCC = table3.getColumnModel().getColumn(1);
        columnCC.setPreferredWidth(45);
        TableColumn columnKT = table3.getColumnModel().getColumn(2);
        columnKT.setPreferredWidth(45);
    }
}

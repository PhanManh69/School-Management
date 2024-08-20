package school_management.controllers.form;

import java.awt.Font;
import java.util.List;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import school_management.models.connect_database.ConnectFormLerningOut;
import school_management.utilities.swing.table.Table;

public class LearningOutcomes {
    public void displayInformation(Table table2) {
        DefaultTableModel tableModel = (DefaultTableModel) table2.getModel();

        List<String[]> listInfo = ConnectFormLerningOut.displayInformation();
        if (listInfo != null) {
            for (String[] row : listInfo) {
                tableModel.addRow(row);
            }
        }
    }

    public void tableEdit(Table table2, JScrollPane jScrollPane2) {
        table2.setFont(new java.awt.Font("Times New Roman", Font.PLAIN, 24));
        table2.fixTable(jScrollPane2);
        TableColumn columnCode = table2.getColumnModel().getColumn(0);
        columnCode.setPreferredWidth(100);
        TableColumn columnName = table2.getColumnModel().getColumn(1);
        columnName.setPreferredWidth(400);
        TableColumn columnCredits = table2.getColumnModel().getColumn(2);
        columnCredits.setPreferredWidth(50);
        TableColumn columnEvaluate = table2.getColumnModel().getColumn(3);
        columnEvaluate.setPreferredWidth(80);
        TableColumn columnCodeStudent = table2.getColumnModel().getColumn(4);
        columnCodeStudent.setPreferredWidth(100);
        TableColumn columnCC = table2.getColumnModel().getColumn(5);
        columnCC.setPreferredWidth(55);
        TableColumn columnKT = table2.getColumnModel().getColumn(6);
        columnKT.setPreferredWidth(55);
        TableColumn columnCK = table2.getColumnModel().getColumn(7);
        columnCK.setPreferredWidth(55);
        TableColumn columnDC = table2.getColumnModel().getColumn(8);
        columnDC.setPreferredWidth(55);
    }
}

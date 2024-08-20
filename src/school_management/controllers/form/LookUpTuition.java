package school_management.controllers.form;

import java.awt.Font;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import school_management.models.connect_database.ConnectFormLookUpTuition;
import school_management.utilities.swing.table.Table;

public class LookUpTuition {

    private int totalMoney = 0;

    public void displayInformation(Table table3) {
        DefaultTableModel tableModel = (DefaultTableModel) table3.getModel();

        List<String[]> listInfo = ConnectFormLookUpTuition.displayInformation();
        if (listInfo != null) {
            for (String[] row : listInfo) {
                tableModel.addRow(row);
                int value = Integer.parseInt(row[1]);
                totalMoney += value;
            }
        }
    }

    public void displayInformationPay(Table table7) {
        DefaultTableModel tableModel = (DefaultTableModel) table7.getModel();

        List<String[]> listInfo = ConnectFormLookUpTuition.displayInformationPay();
        if (listInfo != null) {
            for (String[] row : listInfo) {
                tableModel.addRow(row);
            }
        }
    }

    public void setLabel(JLabel lbTotalMoney, JLabel lbRemaining, JLabel lbText) {
        String totalMoneyStr = ConnectFormLookUpTuition.displayTotalMoney();
        int value = totalMoneyStr != null ? Integer.parseInt(totalMoneyStr) : 0;
        String setLbTotalMoney = String.valueOf(value);
        
        lbTotalMoney.setText(setLbTotalMoney);

        int remaining = value - totalMoney;

        if (remaining > 0) {
            String valueRemaining = String.valueOf(remaining);
            lbRemaining.setText(valueRemaining);
            lbText.setText("Tài Khoản Thừa: ");
        } else if (remaining < 0) {
            String valueRemaining = String.valueOf(remaining);
            String numberRemaining = valueRemaining.substring(1);
            lbRemaining.setText(numberRemaining);
            lbText.setText("Còn Phải Đóng: ");
        } else {
            String valueRemaining = String.valueOf(remaining);
            lbRemaining.setText(valueRemaining);
        }
    }

    public void tableEdit(Table table3, Table table7, JScrollPane jScrollPane3, JScrollPane jScrollPane7) {
        table3.setFont(new java.awt.Font("Times New Roman", Font.PLAIN, 24));
        table3.fixTable(jScrollPane3);
        table7.setFont(new java.awt.Font("Times New Roman", Font.PLAIN, 24));
        table7.fixTable(jScrollPane7);
        TableColumn column1 = table7.getColumnModel().getColumn(0);
        column1.setPreferredWidth(80);
        TableColumn column2 = table7.getColumnModel().getColumn(1);
        column2.setPreferredWidth(200);
        TableColumn column3 = table7.getColumnModel().getColumn(2);
        column3.setPreferredWidth(80);
    }
}

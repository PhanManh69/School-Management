package school_management.utilities.swing.table;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.TableCellRenderer;

public class MultilineTableCellRenderer implements TableCellRenderer {

    public MultilineTableCellRenderer() {
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JTextArea textArea = new JTextArea();
        textArea.setFont(new Font("Times New Roman", Font.PLAIN, 24));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setBorder(null);
        textArea.setForeground(new Color(70, 73, 75));
        if (!isSelected) {
            textArea.setBackground(Color.WHITE);
            textArea.setBorder(BorderFactory.createLineBorder(Color.WHITE, 3));
        } else {
            textArea.setBackground(new Color(239, 244, 255));
            textArea.setBorder(BorderFactory.createLineBorder(new Color(239, 244, 255), 3));
        }
        textArea.setText((String) value);
        adjustRowHeight(table, row, column, textArea);
        return textArea;
    }

    private void adjustRowHeight(JTable table, int row, int column, JTextArea textArea) {
        int cWidth = table.getTableHeader().getColumnModel().getColumn(column).getWidth();
        textArea.setSize(cWidth, Integer.MAX_VALUE);
        int prefH = textArea.getPreferredSize().height;
        if (table.getRowHeight(row) != prefH) {
            table.setRowHeight(row, prefH);
        }
    }
}

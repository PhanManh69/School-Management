package school_management.utilities.swing.table;

import java.awt.Component;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class HeaderEvaluate extends DefaultTableCellRenderer {

    private Font font;

    public HeaderEvaluate(Font font) {
        this.font = font;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (component instanceof JLabel) {
            JLabel label = (JLabel) component;
            label.setFont(font);
        }
        return component;
    }
}

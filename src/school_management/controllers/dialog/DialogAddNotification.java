package school_management.controllers.dialog;

import java.awt.Dimension;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import school_management.utilities.swing.scrollbar.ScrollBarCustom;

public class DialogAddNotification {
    public void init(JTextField txtDate, JTextField txtTitle, JTextArea txtContent, JScrollPane jScrollPane1) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date currentDate = new Date();
        String formattedDate = dateFormat.format(currentDate);
        txtDate.setText(formattedDate);
        txtDate.setEditable(false);
        txtDate.setPreferredSize(new Dimension(677, 90));
        txtTitle.setPreferredSize(new Dimension(677, 90));
        jScrollPane1.setVerticalScrollBar(new ScrollBarCustom());
        
        settingJTextField(txtTitle, "Nhập tiêu đề");
        settingJTextArea(txtContent, "Nhập nội dung");
    }
    
    public void settingJTextField(JTextField txt, String text) {
        txt.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (txt.getText().equals(text)) {
                    txt.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (txt.getText().isEmpty()) {
                    txt.setText(text);
                }
            }
        });
    }

    public void settingJTextArea(JTextArea txt, String text) {
        txt.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (txt.getText().equals(text)) {
                    txt.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (txt.getText().isEmpty()) {
                    txt.setText(text);
                }
            }
        });
        txt.setLineWrap(true);
        txt.setWrapStyleWord(true);
    }
}

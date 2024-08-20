package school_management.controllers.form;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import school_management.controllers.event.ChatEvent;
import school_management.models.object.ModelMessage;
import school_management.views.component.ChatBox;
import school_management.views.component.ChatArea;

public class Support {
    public void init(ChatArea chatArea) {
        chatArea.setTitle("Chức Năng / Hỗ Trợ Trực Tuyến");
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy, hh:mmaa");
        chatArea.addChatEvent(new ChatEvent() {
            @Override
            public void mousePressedSendButton(ActionEvent evt) {
                Icon icon = new ImageIcon(getClass().getResource("/school_management/utilities/icon/p1.png"));
                String name = "Admin";
                String date = df.format(new Date());
                String message = chatArea.getText().trim();
                chatArea.addChatBox(new ModelMessage(icon, name, date, message), ChatBox.BoxType.RIGHT);
                chatArea.clearTextAndGrabFocus();
            }

            @Override
            public void mousePressedFileButton(ActionEvent evt) {

            }

            @Override
            public void keyTyped(KeyEvent evt) {

            }
        });
    }
}

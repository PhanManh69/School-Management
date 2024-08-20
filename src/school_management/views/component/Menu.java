package school_management.views.component;

import school_management.controllers.event.EventMenu;
import school_management.controllers.event.EventMenuSelected;
import school_management.controllers.event.EventShowPopupMenu;
import school_management.models.object.ModelMenu;
import school_management.utilities.swing.MenuAnimation;
import school_management.utilities.swing.MenuItem;
import school_management.utilities.swing.scrollbar.ScrollBarCustom;
import java.awt.Color;
import java.awt.Component;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.ImageIcon;
import net.miginfocom.swing.MigLayout;
import school_management.models.user_controller.UserController;
import school_management.models.connect_database.ConnectLogin;

public class Menu extends javax.swing.JPanel {

    public boolean isShowMenu() {
        return showMenu;
    }

    public void addEvent(EventMenuSelected event) {
        this.event = event;
    }

    public void setEnableMenu(boolean enableMenu) {
        this.enableMenu = enableMenu;
    }

    public void setShowMenu(boolean showMenu) {
        this.showMenu = showMenu;
    }

    public void addEventShowPopup(EventShowPopupMenu eventShowPopup) {
        this.eventShowPopup = eventShowPopup;
    }

    private final MigLayout layout;
    private EventMenuSelected event;
    private EventShowPopupMenu eventShowPopup;
    private boolean enableMenu = true;
    private boolean showMenu = true;

    public Menu() {
        initComponents();
        setOpaque(false);
        sp.getViewport().setOpaque(false);
        sp.setVerticalScrollBar(new ScrollBarCustom());
        layout = new MigLayout("wrap, fillx, insets 0", "[fill]", "[]0[]");
        panel.setLayout(layout);
    }

    public void initMenuItem() {
        String userName = UserController.getUserName();
        String password = UserController.getPassword();
        
        int role = ConnectLogin.login(userName, password);
        
        switch (role) {
            case 1 -> {
                addMenu(new ModelMenu(new ImageIcon(getClass().getResource("/school_management/utilities/icon/1.png")), "Chức Năng", "Trang Chủ", "Hỗ Trợ Trực Tuyến"));
                addMenu(new ModelMenu(new ImageIcon(getClass().getResource("/school_management/utilities/icon/6.png")), "Quản Lý Sinh Viên", "Học Tập"));
                addMenu(new ModelMenu(new ImageIcon(getClass().getResource("/school_management/utilities/icon/5.png")), "Quản Lý Đào Tạo", "Chương Trình Học", "Kết Quả Học Tập", "Đăng Ký Học Phần"));
                addMenu(new ModelMenu(new ImageIcon(getClass().getResource("/school_management/utilities/icon/11.png")), "Học Phí", "Tra Cứu Học Phí", "Thanh Toán Học Phí"));
            }
            case 2 -> {
                addMenu(new ModelMenu(new ImageIcon(getClass().getResource("/school_management/utilities/icon/1.png")), "Chức Năng", "Trang Chủ", "Hỗ Trợ Trực Tuyến"));
                addMenu(new ModelMenu(new ImageIcon(getClass().getResource("/school_management/utilities/icon/6.png")), "Quản Lý Sinh Viên", "Hồ Sơ Sinh Viên", "Học Tập", "Đánh Giá", "Thêm Học Phần"));
                addMenu(new ModelMenu(new ImageIcon(getClass().getResource("/school_management/utilities/icon/5.png")), "Quản Lý Đào Tạo", "Chương Trình Học"));
            }
            case 3 -> {
                addMenu(new ModelMenu(new ImageIcon(getClass().getResource("/school_management/utilities/icon/1.png")), "Chức Năng", "Trang Chủ", "Hỗ Trợ Trực Tuyến"));
                addMenu(new ModelMenu(new ImageIcon(getClass().getResource("/school_management/utilities/icon/6.png")), "Quản Lý Sinh Viên", "Hồ Sơ Sinh Viên", "Học Tập", "Đánh Giá", "Thêm Học Phần"));
                addMenu(new ModelMenu(new ImageIcon(getClass().getResource("/school_management/utilities/icon/5.png")), "Quản Lý Đào Tạo", "Chương Trình Học"));
            }
            default -> {
            }
        }
    }

    private void addMenu(ModelMenu menu) {
        panel.add(new MenuItem(menu, getEventMenu(), event, panel.getComponentCount()), "h 40!");
    }

    private EventMenu getEventMenu() {
        return (Component com, boolean open) -> {
            if (enableMenu) {
                if (isShowMenu()) {
                    if (open) {
                        new MenuAnimation(layout, com).openMenu();
                    } else {
                        new MenuAnimation(layout, com).closeMenu();
                    }
                    return true;
                } else {
                    eventShowPopup.showPopup(com);
                }
            }
            return false;
        };
    }

    public void hideallMenu() {
        for (Component com : panel.getComponents()) {
            MenuItem item = (MenuItem) com;
            if (item.isOpen()) {
                new MenuAnimation(layout, com, 500).closeMenu();
                item.setOpen(false);
            }
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        sp = new javax.swing.JScrollPane();
        panel = new javax.swing.JPanel();
        profile1 = new school_management.views.component.Profile();

        sp.setBorder(null);
        sp.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        panel.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        panel.setOpaque(false);

        javax.swing.GroupLayout panelLayout = new javax.swing.GroupLayout(panel);
        panel.setLayout(panelLayout);
        panelLayout.setHorizontalGroup(
            panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 312, Short.MAX_VALUE)
        );
        panelLayout.setVerticalGroup(
            panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 523, Short.MAX_VALUE)
        );

        sp.setViewportView(panel);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(profile1, javax.swing.GroupLayout.PREFERRED_SIZE, 305, Short.MAX_VALUE)
            .addComponent(sp, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(profile1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sp))
        );
    }// </editor-fold>//GEN-END:initComponents

    @Override
    protected void paintComponent(Graphics grphcs) {
        Graphics2D g2 = (Graphics2D) grphcs;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        GradientPaint gra = new GradientPaint(0, 0, new Color(249, 98, 33), getWidth(), 0, new Color(249, 134, 33));
        g2.setPaint(gra);
        g2.fillRect(0, 0, getWidth(), getHeight());
        super.paintComponent(grphcs);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel panel;
    private school_management.views.component.Profile profile1;
    private javax.swing.JScrollPane sp;
    // End of variables declaration//GEN-END:variables
}

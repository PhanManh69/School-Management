package school_management.main;

import school_management.views.form.MainForm;
import school_management.views.form.FormSupport;
import school_management.views.form.FormEvaluate;
import school_management.views.form.FormRegisterTheCourse;
import school_management.views.form.FormLearn;
import school_management.views.form.FormStudentProfile;
import school_management.views.form.FormLearningOutcomes;
import school_management.views.form.FormPaymentSystem;
import school_management.views.form.FormAddCourses;
import school_management.views.form.FormLookUpTuition;
import school_management.views.form.FormSubject;
import school_management.views.component.Header;
import school_management.views.component.Menu;
import school_management.utilities.swing.MenuItem;
import school_management.utilities.swing.PopupMenu;
import school_management.utilities.swing.icon.GoogleMaterialDesignIcons;
import school_management.utilities.swing.icon.IconFontSwing;
import java.awt.Component;
import java.awt.event.ActionEvent;
import net.miginfocom.swing.MigLayout;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTarget;
import org.jdesktop.animation.timing.TimingTargetAdapter;
import school_management.models.user_controller.UserController;
import school_management.models.connect_database.ConnectLogin;
import school_management.views.form.FormHome;

public class Main extends javax.swing.JFrame {

    private MigLayout layout;
    private Menu menu;
    private Header header;
    private MainForm main;
    private Animator animator;
    private static Main instance;

    public Main() {
        initComponents();
        init();
        instance = this;
    }

    private void init() {
        layout = new MigLayout("fill", "0[]0[100%, fill]0", "0[fill, top]0");
        bg.setLayout(layout);
        menu = new Menu();
        header = new Header();
        main = new MainForm();

        String userName = UserController.getUserName();
        String password = UserController.getPassword();

        int role = ConnectLogin.login(userName, password);

        menu.addEvent((var menuIndex, var subMenuIndex) -> {
            switch (menuIndex) {
                case 0 -> {
                    switch (subMenuIndex) {
                        case 0 ->
                            main.showForm(new FormHome());
                        case 1 ->
                            main.showForm(new FormSupport());
                        default -> {
                        }
                    }
                }
                case 1 -> {
                    switch (subMenuIndex) {
                        case 0 -> {
                            switch (role) {
                                case 1 ->
                                    main.showForm(new FormLearn());
                                case 2 ->
                                    main.showForm(new FormStudentProfile());
                                case 3 ->
                                    main.showForm(new FormStudentProfile());
                                default -> {
                                }
                            }
                        }
                        case 1 -> {
                            switch (role) {
                                case 2 ->
                                    main.showForm(new FormLearn());
                                case 3 ->
                                    main.showForm(new FormLearn());
                                default -> {
                                }
                            }
                        }
                        case 2 -> {
                            switch (role) {
                                case 2 ->
                                    main.showForm(new FormEvaluate());
                                case 3 ->
                                    main.showForm(new FormEvaluate());
                                default -> {
                                }
                            }
                        }
                        case 3 -> {
                            switch (role) {
                                case 2 ->
                                    main.showForm(new FormAddCourses());
                                case 3 ->
                                    main.showForm(new FormAddCourses());
                                default -> {
                                }
                            }
                        }
                        default -> {
                        }
                    }
                }
                case 2 -> {
                    switch (subMenuIndex) {
                        case 0 -> {
                            switch (role) {
                                case 1 ->
                                    main.showForm(new FormSubject());
                                case 2 ->
                                    main.showForm(new FormSubject());
                                case 3 ->
                                    main.showForm(new FormSubject());
                                default -> {
                                }
                            }
                        }
                        case 1 -> {
                            switch (role) {
                                case 1 ->
                                    main.showForm(new FormLearningOutcomes());
                                default -> {
                                }
                            }
                        }
                        case 2 -> {
                            switch (role) {
                                case 1 ->
                                    main.showForm(new FormRegisterTheCourse());
                                default -> {
                                }
                            }
                        }
                        default -> {
                        }
                    }
                }
                case 3 -> {
                    switch (subMenuIndex) {
                        case 0 -> {
                            switch (role) {
                                case 1 ->
                                    main.showForm(new FormLookUpTuition());
                                default -> {
                                }
                            }
                        }
                        case 1 -> {
                            switch (role) {
                                case 1 ->
                                    main.showForm(new FormPaymentSystem());
                                default -> {
                                }
                            }
                        }
                        default -> {
                        }
                    }
                }
                default -> {
                }
            }
        });
        menu.addEventShowPopup((Component com) -> {
            MenuItem item = (MenuItem) com;
            PopupMenu popup = new PopupMenu(Main.this, item.getIndex(), item.getEventSelected(), item.getMenu().getSubMenu());
            int x1 = Main.this.getX() + 52;
            int y1 = Main.this.getY() + com.getY() + 86;
            popup.setLocation(x1, y1);
            popup.setVisible(true);
        });
        menu.initMenuItem();
        bg.add(menu, "w 390!, spany 2");
        bg.add(header, "h 75!, wrap");
        bg.add(main, "w 100%, h 100%");
        TimingTarget target = new TimingTargetAdapter() {
            @Override
            public void timingEvent(float fraction) {
                double width;
                if (menu.isShowMenu()) {
                    width = 60 + (330 * (1f - fraction));
                } else {
                    width = 60 + (330 * fraction);
                }
                layout.setComponentConstraints(menu, "w " + width + "!, spany2");
                menu.revalidate();
            }

            @Override
            public void end() {
                menu.setShowMenu(!menu.isShowMenu());
                menu.setEnableMenu(true);
            }

        };
        animator = new Animator(500, target);
        animator.setResolution(0);
        animator.setDeceleration(0.5f);
        animator.setAcceleration(0.5f);
        header.addMenuEvent((ActionEvent ae) -> {
            if (!animator.isRunning()) {
                animator.start();
            }
            menu.setEnableMenu(false);
            if (menu.isShowMenu()) {
                menu.hideallMenu();
            }
        });
        IconFontSwing.register(GoogleMaterialDesignIcons.getIconFont());
        main.showForm(new FormHome());
    }

    public static Main getInstance() {
        return instance;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        bg = new javax.swing.JLayeredPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        bg.setBackground(new java.awt.Color(245, 245, 245));
        bg.setOpaque(true);

        javax.swing.GroupLayout bgLayout = new javax.swing.GroupLayout(bg);
        bg.setLayout(bgLayout);
        bgLayout.setHorizontalGroup(
            bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 2100, Short.MAX_VALUE)
        );
        bgLayout.setVerticalGroup(
            bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1080, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(bg)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(bg)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new Main().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLayeredPane bg;
    // End of variables declaration//GEN-END:variables
}

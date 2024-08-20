package school_management.views.form;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.table.DefaultTableModel;
import school_management.models.connect_database.ConnectFormPayment;
import school_management.models.connect_database.ConnectLogin;
import school_management.models.user_controller.UserController;

public final class FormPaymentSystem extends javax.swing.JPanel {

    public FormPaymentSystem() {
        initComponents();
        init();
    }

    public void init() {
        qrCode();
        displayInformation();
        editTable();
        editLabel();
    }
    
    public void qrCode() {
        try {
            String qrCodeData = "72DCTT20201";
            String charset = "UTF-8";
            Map<EncodeHintType, ErrorCorrectionLevel> hintMap = new HashMap<>();
            hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
            BitMatrix matrix = new MultiFormatWriter().encode(
                    new String(qrCodeData.getBytes(charset), charset),
                    BarcodeFormat.QR_CODE, 400, 400, hintMap);

            Path path = FileSystems.getDefault().getPath("D:\\Learning\\Subject UTT\\Java_Nang_Cao\\BTL_JavaSwing\\qr-code\\QRCode.png");
            File file = path.toFile();
            MatrixToImageWriter.writeToFile(matrix, "PNG", file);

            ImageIcon qrCodeIcon = new ImageIcon("D:\\Learning\\Subject UTT\\Java_Nang_Cao\\BTL_JavaSwing\\qr-code\\QRCode.png");

            lbQrCode.setIcon(qrCodeIcon);
        } catch (WriterException | IOException e) {
            System.out.println(e);
        }
    }
    
    public void displayInformation() {
        DefaultTableModel tableModel = (DefaultTableModel) table7.getModel();

        List<String[]> listInfo = ConnectFormPayment.displayInformation();
        if (listInfo != null) {
            for (String[] row : listInfo) {
                tableModel.addRow(row);
            }
        }
    }
    
    public void editTable() {
        table7.setFont(new java.awt.Font("Times New Roman", Font.PLAIN, 24));
        table7.fixTable(jScrollPane7);
    }
    
    public void editLabel() {
        String userName = UserController.getUserName();
        String name = ConnectLogin.studentName();
        String nameClass = ConnectLogin.className();
        
        codeStudent.setText(userName);
        nameStudent.setText(name);
        classRoom.setText(nameClass);
        numberAccount.setText(userName);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lbQrCode = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        nameStudent = new javax.swing.JLabel();
        codeStudent = new javax.swing.JLabel();
        classRoom = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        numberAccount = new javax.swing.JLabel();
        numberAccount1 = new javax.swing.JLabel();
        jScrollPane7 = new javax.swing.JScrollPane();
        table7 = new school_management.utilities.swing.table.Table();

        setBackground(new java.awt.Color(255, 255, 255));

        lbQrCode.setBackground(new java.awt.Color(255, 255, 255));
        lbQrCode.setForeground(new java.awt.Color(255, 255, 255));
        lbQrCode.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbQrCode.setIcon(new javax.swing.ImageIcon("D:\\Learning\\Subject UTT\\Java_Nang_Cao\\BTL_JavaSwing\\qr-code\\QRCode.png")); // NOI18N
        lbQrCode.setToolTipText("QR Code");
        lbQrCode.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true), "Mã QR Code", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.ABOVE_TOP, new java.awt.Font("Times New Roman", 0, 18))); // NOI18N

        jLabel1.setFont(new java.awt.Font("Times New Roman", 0, 24)); // NOI18N
        jLabel1.setText("Họ và Tên:");

        jLabel2.setFont(new java.awt.Font("Times New Roman", 0, 24)); // NOI18N
        jLabel2.setText("Mã Sinh Viên:");

        jLabel3.setFont(new java.awt.Font("Times New Roman", 0, 24)); // NOI18N
        jLabel3.setText("Lớp:");

        nameStudent.setFont(new java.awt.Font("Times New Roman", 0, 24)); // NOI18N
        nameStudent.setText("ho ten");

        codeStudent.setFont(new java.awt.Font("Times New Roman", 0, 24)); // NOI18N
        codeStudent.setText("Họ và Tên:");

        classRoom.setFont(new java.awt.Font("Times New Roman", 0, 24)); // NOI18N
        classRoom.setText("Họ và Tên:");

        jLabel4.setFont(new java.awt.Font("Times New Roman", 0, 24)); // NOI18N
        jLabel4.setText("Số Tài Khoản:");

        numberAccount.setFont(new java.awt.Font("Times New Roman", 0, 24)); // NOI18N
        numberAccount.setText("Số Tài Khoản:");

        numberAccount1.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        numberAccount1.setText("Số Tiền Còn Phải Đóng");

        jScrollPane7.setPreferredSize(new java.awt.Dimension(454, 437));

        table7.setBackground(new java.awt.Color(255, 255, 255));
        table7.setForeground(new java.awt.Color(255, 255, 255));
        table7.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Học Kỳ", "Còn Phải Đóng"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        table7.setToolTipText("Chi Tiết");
        table7.setDoubleBuffered(true);
        jScrollPane7.setViewportView(table7);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(59, 59, 59)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addGap(18, 18, 18)
                                .addComponent(numberAccount))
                            .addComponent(lbQrCode, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                        .addGap(55, 55, 55)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3))
                        .addGap(45, 45, 45)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(classRoom)
                            .addComponent(codeStudent)
                            .addComponent(nameStudent)))
                    .addComponent(numberAccount1, javax.swing.GroupLayout.PREFERRED_SIZE, 1315, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 1393, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(46, 46, 46)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbQrCode, javax.swing.GroupLayout.PREFERRED_SIZE, 312, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(nameStudent))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(codeStudent))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(classRoom))))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(numberAccount))
                .addGap(18, 18, 18)
                .addComponent(numberAccount1)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 510, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel classRoom;
    private javax.swing.JLabel codeStudent;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JLabel lbQrCode;
    private javax.swing.JLabel nameStudent;
    private javax.swing.JLabel numberAccount;
    private javax.swing.JLabel numberAccount1;
    private school_management.utilities.swing.table.Table table7;
    // End of variables declaration//GEN-END:variables
}

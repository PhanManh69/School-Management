package school_management.utilities.swing.exporter;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.JTable;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import school_management.models.connect_database.ConnectFormStudentProfile;
import school_management.views.dialog.Message;

public class ExcelExporter {

    public static void exportToExcelPoint(JTable table, String cbClass, String cbSubject) {
        try {
            JFileChooser jFileChooser = new JFileChooser();
            jFileChooser.showSaveDialog(null);
            File saveFile = jFileChooser.getSelectedFile();

            if (saveFile != null) {
                Message messageDialog = new Message(null, true);
                messageDialog.showMessage("Xuất file thành công!");

                saveFile = new File(saveFile.toString() + ".xlsx");
                FileOutputStream out;
                try (Workbook wb = new XSSFWorkbook()) {
                    Sheet sheet = wb.createSheet("Danh sách điểm");
                    sheet.setColumnWidth(0, 20 * 100);
                    sheet.setColumnWidth(1, 20 * 250);
                    sheet.setColumnWidth(2, 20 * 350);
                    sheet.setColumnWidth(3, 20 * 250);
                    sheet.setColumnWidth(4, 20 * 200);
                    sheet.setColumnWidth(5, 20 * 350);
                    sheet.setColumnWidth(6, 20 * 200);
                    sheet.setColumnWidth(7, 20 * 200);
                    sheet.setColumnWidth(8, 20 * 200);

                    Font titleFont = wb.createFont();
                    titleFont.setFontName("Times New Roman");
                    titleFont.setFontHeightInPoints((short) 16);
                    titleFont.setBold(true);

                    CellStyle titleCellStyle = wb.createCellStyle();
                    titleCellStyle.setFont(titleFont);

                    Row rowTitle = sheet.createRow(1);
                    Cell titleCell = rowTitle.createCell(0);
                    titleCell.setCellValue("Danh sách điểm Môn Học: " + cbSubject);
                    titleCell.setCellStyle(titleCellStyle);

                    Font classFont = wb.createFont();
                    classFont.setFontName("Times New Roman");
                    classFont.setFontHeightInPoints((short) 13);
                    classFont.setBold(true);

                    CellStyle classCellStyle = wb.createCellStyle();
                    classCellStyle.setFont(classFont);

                    Row rowClass = sheet.createRow(2);
                    Cell classCell = rowClass.createCell(0);
                    classCell.setCellValue("Lớp: " + cbClass);
                    classCell.setCellStyle(classCellStyle);

                    Row rowCol = sheet.createRow(4);
                    for (int i = 0; i < table.getColumnCount(); i++) {
                        Cell cell = rowCol.createCell(i);
                        cell.setCellValue(table.getColumnName(i));
                    }
                    for (int j = 0; j < table.getRowCount(); j++) {
                        Row row = sheet.createRow(j + 5);
                        for (int k = 0; k < table.getColumnCount(); k++) {
                            Cell cell = row.createCell(k);
                            if (table.getValueAt(j, k) != null) {
                                cell.setCellValue(table.getValueAt(j, k).toString());
                            }
                        }
                    }
                    out = new FileOutputStream(new File(saveFile.toString()));
                    wb.write(out);
                }
                out.close();
                openFile(saveFile.toString());
            } else {
                Message messageDialog = new Message(null, true);
                messageDialog.showMessage("Lỗi không thể tải được file!");
            }
        } catch (IOException io) {
            Message messageDialog = new Message(null, true);
            messageDialog.showMessage("Lỗi không thể tải được file!");
            System.out.println(io);
        }
    }

    public static void exportToExcelStudent(JTable table, String cbClass) {
        try {
            JFileChooser jFileChooser = new JFileChooser();
            jFileChooser.showSaveDialog(null);
            File saveFile = jFileChooser.getSelectedFile();

            if (saveFile != null) {
                Message messageDialog = new Message(null, true);
                messageDialog.showMessage("Xuất file thành công!");

                saveFile = new File(saveFile.toString() + ".xlsx");
                FileOutputStream out;
                try (Workbook wb = new XSSFWorkbook()) {
                    Sheet sheet = wb.createSheet("Danh sách sinh viên");
                    sheet.setColumnWidth(0, 20 * 250);
                    sheet.setColumnWidth(1, 20 * 250);
                    sheet.setColumnWidth(2, 20 * 250);
                    sheet.setColumnWidth(3, 20 * 300);
                    sheet.setColumnWidth(4, 20 * 200);
                    sheet.setColumnWidth(5, 20 * 200);
                    sheet.setColumnWidth(6, 20 * 450);
                    sheet.setColumnWidth(7, 20 * 250);
                    sheet.setColumnWidth(8, 20 * 450);
                    sheet.setColumnWidth(9, 20 * 250);
                    sheet.setColumnWidth(10, 20 * 250);
                    sheet.setColumnWidth(11, 20 * 250);

                    Font titleFont = wb.createFont();
                    titleFont.setFontName("Times New Roman");
                    titleFont.setFontHeightInPoints((short) 16);
                    titleFont.setBold(true);

                    CellStyle titleCellStyle = wb.createCellStyle();
                    titleCellStyle.setFont(titleFont);

                    Row rowTitle = sheet.createRow(1);
                    Cell titleCell = rowTitle.createCell(0);
                    titleCell.setCellValue("Trường đại học Công Nghệ Giao Thông Vận Tải");
                    titleCell.setCellStyle(titleCellStyle);

                    Font classFont = wb.createFont();
                    classFont.setFontName("Times New Roman");
                    classFont.setFontHeightInPoints((short) 13);
                    classFont.setBold(true);

                    CellStyle classCellStyle = wb.createCellStyle();
                    classCellStyle.setFont(classFont);

                    Row rowClass = sheet.createRow(2);
                    Cell classCell = rowClass.createCell(0);
                    classCell.setCellValue("Lớp: " + cbClass);
                    classCell.setCellStyle(classCellStyle);

                    int rowNum = 4;
                    Row rowCol = sheet.createRow(rowNum++);
                    rowCol.createCell(0).setCellValue("Mã Sinh Viên");
                    rowCol.createCell(1).setCellValue("Tên Sinh Viên");
                    rowCol.createCell(2).setCellValue("Lớp");
                    rowCol.createCell(3).setCellValue("Ngành Học");
                    rowCol.createCell(4).setCellValue("Khóa Học");
                    rowCol.createCell(5).setCellValue("Ngày Sinh");
                    rowCol.createCell(6).setCellValue("Địa Chỉ");
                    rowCol.createCell(7).setCellValue("Số Điện Thoại");
                    rowCol.createCell(8).setCellValue("Email");
                    rowCol.createCell(9).setCellValue("Thời Gian Đào Tạo");
                    rowCol.createCell(10).setCellValue("Ngày Bắt Đầu");
                    rowCol.createCell(11).setCellValue("Ngày Kết Thúc");

                    for (int j = 0; j < table.getRowCount(); j++) {
                        Row row = sheet.createRow(j + 5);
                        Object value = table.getValueAt(j, 0);
                        if (value != null) {
                            Cell codeStudent = row.createCell(0);
                            codeStudent.setCellValue(value.toString());

                            String[] infoCodeStudent = ConnectFormStudentProfile.displayInformationDetail(value.toString());

                            if (infoCodeStudent != null && infoCodeStudent.length > 0) {
                                for (int k = 1; k < infoCodeStudent.length; k++) {
                                    Cell cell = row.createCell(k);
                                    cell.setCellValue(infoCodeStudent[k]);
                                }
                            }
                        }
                    }

                    out = new FileOutputStream(new File(saveFile.toString()));
                    wb.write(out);
                }
                out.close();
                openFile(saveFile.toString());
            } else {
                Message messageDialog = new Message(null, true);
                messageDialog.showMessage("Lỗi không thể tải được file!");
            }
        } catch (IOException io) {
            Message messageDialog = new Message(null, true);
            messageDialog.showMessage("Lỗi không thể tải được file!");
            System.out.println(io);
        }
    }

    public static void openFile(String file) {
        try {
            File path = new File(file);
            Desktop.getDesktop().open(path);
        } catch (IOException ioe) {
            Message messageDialog = new Message(null, true);
            messageDialog.showMessage("Lỗi không thể mở tệp!");
            System.out.println(ioe);
        }
    }
}

package school_management.controllers.dialog;

import java.awt.event.ActionEvent;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import school_management.models.connect_database.ConnectFormAddCourse;
import school_management.models.connect_database.ConnectFormEvaluate;
import school_management.utilities.swing.Button;

public class DialogAddCourse {

    public void searchCourse(JComboBox cbSemester, JComboBox cbCouurse, JComboBox cbMajors, JComboBox cbSubject, JComboBox cbClass, JComboBox cbLecturers) {
        cbCouurse.addActionListener((ActionEvent e) -> {
            fillMajorsComboBox(cbMajors);
            cbSubject.removeAllItems();
            cbClass.removeAllItems();
            cbLecturers.removeAllItems();
            cbSemester.removeAllItems();
        });
    }

    public void searchMajors(JComboBox cbSemester, JComboBox cbCouurse, JComboBox cbMajors, JComboBox cbSubject, JComboBox cbClass, JComboBox cbLecturers) {
        cbMajors.addActionListener((ActionEvent e) -> {
            fillSubjectComboBox(cbMajors, cbSubject);
            cbClass.removeAllItems();
            cbLecturers.removeAllItems();
            cbSemester.removeAllItems();
        });
    }

    public void searchSubject(JComboBox cbSemester, JComboBox cbCouurse, JComboBox cbMajors, JComboBox cbSubject, JComboBox cbClass, JComboBox cbLecturers) {
        cbSubject.addActionListener((ActionEvent e) -> {
            fillClassRoomComboBox(cbCouurse, cbMajors, cbClass);
            cbLecturers.removeAllItems();
            cbSemester.removeAllItems();
        });
    }

    public void searchClass(JComboBox cbSemester, JComboBox cbCouurse, JComboBox cbMajors, JComboBox cbSubject, JComboBox cbClass, JComboBox cbLecturers) {
        cbClass.addActionListener((ActionEvent e) -> {
            fillLecturersComboBox(cbSubject, cbMajors, cbLecturers);
            cbSemester.removeAllItems();
        });
    }
    
    public void searchLecturers(JComboBox cbSemester, JComboBox cbCouurse, JComboBox cbMajors, JComboBox cbSubject, JComboBox cbClass, JComboBox cbLecturers) {
        cbLecturers.addActionListener((ActionEvent e) -> {
            fillSemesterComboBox(cbSemester);
        });
    }
    
    public void searchSemester(JComboBox cbSemester, JTextField txtDate) {
        cbSemester.addActionListener((ActionEvent e) -> {
            fillDate(cbSemester, txtDate);
        });
    }

    public void fillCourseComboBox(JComboBox cbCouurse) {
        List<String> name = ConnectFormEvaluate.getCourse();
        if (name != null) {
            cbCouurse.setModel(new DefaultComboBoxModel<>(name.toArray(String[]::new)));
        }
    }

    public void fillMajorsComboBox(JComboBox cbMajors) {
        List<String> name = ConnectFormEvaluate.getMojors();
        if (name != null) {
            cbMajors.setModel(new DefaultComboBoxModel<>(name.toArray(String[]::new)));
        }
    }

    public void fillSubjectComboBox(JComboBox cbMajors, JComboBox cbSubject) {
        String idMajors = (String) cbMajors.getSelectedItem();

        List<String> name = ConnectFormEvaluate.getSubject(idMajors);
        if (name != null) {
            cbSubject.setModel(new DefaultComboBoxModel<>(name.toArray(String[]::new)));
        }
    }

    public void fillClassRoomComboBox(JComboBox cbCouurse, JComboBox cbMajors, JComboBox cbClass) {
        String idCourse = (String) cbCouurse.getSelectedItem();
        String idMajors = (String) cbMajors.getSelectedItem();

        List<String> name = ConnectFormEvaluate.getClassRoom(idMajors, idCourse);
        if (name != null) {
            cbClass.setModel(new DefaultComboBoxModel<>(name.toArray(String[]::new)));
        }
    }

    public void fillLecturersComboBox(JComboBox cbSubject, JComboBox cbMajors, JComboBox cbLecturers) {
        String subjectName = (String) cbSubject.getSelectedItem();
        String majorstName = (String) cbMajors.getSelectedItem();

        List<String> name = ConnectFormAddCourse.getLecturers(subjectName, majorstName);
        if (name != null) {
            cbLecturers.setModel(new DefaultComboBoxModel<>(name.toArray(String[]::new)));
        }
    }

    public void fillSemesterComboBox(JComboBox cbSemester) {
        List<String> semesters  = ConnectFormAddCourse.getSemester();
        if (semesters != null) {
            Comparator<String> semesterComparator = (s1, s2) -> {
                int year1 = Integer.parseInt(s1.substring(2, 6));
                int year2 = Integer.parseInt(s2.substring(2, 6));
                int part1 = Integer.parseInt(s1.substring(0, 1));
                int part2 = Integer.parseInt(s2.substring(0, 1));

                if (year1 != year2) {
                    return year1 - year2;
                } else {
                    return part1 - part2;
                }
            };

            Collections.sort(semesters, semesterComparator);
            cbSemester.setModel(new DefaultComboBoxModel<>(semesters.toArray(String[]::new)));
        }
    }
    
    public void fillDate(JComboBox cbSemester, JTextField txtDate) {
        String semesterName = (String) cbSemester.getSelectedItem();
        String startDate = ConnectFormAddCourse.getDate(semesterName);
        if (startDate != null) {
            txtDate.setText(startDate);
        }
    }

    public void buttonEdit(Button btnSave, Button btnClose, JTextField txtDate) {
        btnSave.setFont(new java.awt.Font("sansserif", 1, 24));
        btnClose.setFont(new java.awt.Font("sansserif", 1, 24));
        txtDate.setEditable(false);
    }
}

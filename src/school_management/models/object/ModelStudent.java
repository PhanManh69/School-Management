package school_management.models.object;

import school_management.utilities.swing.table.EventAction;
import school_management.utilities.swing.table.ModelAction;

public class ModelStudent {

    public String getName() {
        return fullName;
    }

    public void setName(String fullName) {
        this.fullName = fullName;
    }

    public String getClassroom() {
        return classRoom;
    }

    public void setClassroom(String classRoom) {
        this.classRoom = classRoom;
    }

    public String getCodeStudent() {
        return codeStudent;
    }

    public void setCodeStudent(String codeStudent) {
        this.codeStudent = codeStudent;
    }

    public String getMajors() {
        return majors;
    }

    public void setMajors(String majors) {
        this.majors = majors;
    }

    public ModelStudent(String fullName, String codeStudent, String classRoom, String majors) {
        this.fullName = fullName;
        this.codeStudent = codeStudent;
        this.classRoom = classRoom;
        this.majors = majors;
    }

    public ModelStudent() {
    }

    private String codeStudent;
    private String fullName;
    private String classRoom;
    private String majors;

    public Object[] toRowTable(EventAction event) {
        return new Object[]{fullName, codeStudent, classRoom, majors, new ModelAction(this, event)};
    }
}

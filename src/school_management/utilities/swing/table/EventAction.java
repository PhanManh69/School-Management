package school_management.utilities.swing.table;

import school_management.models.object.ModelStudent;

public interface EventAction {

    public void delete(ModelStudent student);

    public void update(ModelStudent student);
}

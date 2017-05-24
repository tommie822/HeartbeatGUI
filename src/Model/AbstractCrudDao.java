package Model;

import java.util.Collection;
import java.util.HashSet;

/**
 * Created by tom on 23-5-2017.
 */
public abstract class AbstractCrudDao<T> {
    private Collection<UpdateListener> updateListeners = new HashSet<>();
    private Collection<PatientListener> patientListeners = new HashSet<>();
    public void addUpdateListener(final UpdateListener listener){
        updateListeners.add(listener);
    }
    public void addNewPatientListener(final PatientListener listener){
        patientListeners.add(listener);
    }

    protected void newData(CrudAction crudAction){
        crudAction.doAction();
        for(UpdateListener listener : updateListeners){
            listener.updateLineChart();
        }
    }
    protected void newPatient(CrudAction crudAction){
        crudAction.doAction();
        for(PatientListener patientListener : patientListeners){
            patientListener.updateListView();
        }
    }

    public interface CrudAction {
        void doAction();
    }

    public interface UpdateListener {
        void updateLineChart();
    }

    public interface PatientListener{
        void updateListView();
    }
}

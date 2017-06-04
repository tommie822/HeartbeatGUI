package Model;

import java.util.Collection;
import java.util.HashSet;

/**
 * Created by tom on 23-5-2017.
 */
public abstract class AbstractCrudDao<T> {

  private Collection<NewDataListener> newDataListeners = new HashSet<>();
  private Collection<NewPatientListener> newPatientListeners = new HashSet<>();
  private Collection<DataClearedListener> dataClearedListeners = new HashSet<>();
  private Collection<CriticalStateListener> criticalStateListeners = new HashSet<>();

  public void addNewDataListener(final NewDataListener newListener) {
    newDataListeners.add(newListener);
  }

  public void addNewPatientListener(final NewPatientListener newListener) {
    newPatientListeners.add(newListener);
  }

  void newData(CrudAction crudAction) {

    crudAction.doAction();
    for (NewDataListener listener : newDataListeners) {
      listener.updateLineChart();
    }
  }

  public void addDataClearedListener(final DataClearedListener newListener) {
    dataClearedListeners.add(newListener);
  }

  public void addCriticalStateListener(final CriticalStateListener newListener){
    criticalStateListeners.add(newListener);
  }

  void newPatient(CrudAction crudAction) {
    crudAction.doAction();
    for (NewPatientListener newPatientListener : newPatientListeners) {
      newPatientListener.updatePatientListView();
    }
  }

  void updatePatientName(CrudAction crudAction) {
    crudAction.doAction();
    for (NewPatientListener newPatientListener : newPatientListeners) {
      newPatientListener.updatePatientListView();
    }
  }

  void dataCleared(CrudAction crudAction) {
    crudAction.doAction();
    for (DataClearedListener dataClearedListener : dataClearedListeners) {
      dataClearedListener.stopConnectionThread();
    }
  }

  void updateCritical(CrudAction2 crudAction){
    crudAction.doAction();
    for (CriticalStateListener criticalStateListener: criticalStateListeners){
      criticalStateListener.showWarning(crudAction.getIdWristband(), crudAction.getPatientName());
    }
  }

  public interface CrudAction {

    void doAction();
  }

  public interface CrudAction2 {

    void doAction();
    int getIdWristband();
    String getPatientName();
  }

  public interface NewDataListener {

    void updateLineChart();
  }

  public interface NewPatientListener {

    void updatePatientListView();
  }

  public interface DataClearedListener {

    void stopConnectionThread();
  }

  public interface CriticalStateListener {

    void showWarning(int idWristband, String patientName);
  }
}

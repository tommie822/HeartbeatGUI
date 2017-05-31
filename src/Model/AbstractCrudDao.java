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

  public void addDataClearedListener(DataClearedListener newListener) {
    dataClearedListeners.add(newListener);
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

  public interface CrudAction {

    void doAction();
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
}

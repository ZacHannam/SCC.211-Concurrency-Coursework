package uk.hannam.concurrency.workers;

import java.util.HashMap;

public enum Workers {

    ADD_WORKER(0, AddWorker.class),
    REMOVE_WORKER(1, RemoveWorker.class);

    private final int index;
    private final Class<? extends Worker> workerType;

    /**
     * Get the index of the argument passed in
     * @return index
     */
    public int getIndex() {
        return this.index;
    }

    /**
     * Get the worker class
     * @return worker class
     */
    public Class<? extends Worker> getWorkerType() {
        return this.workerType;
    }

    Workers(int paramIndex, Class<? extends Worker> paramWorkerType) {
        this.index = paramIndex;
        this.workerType = paramWorkerType;
    }

    private static final HashMap<Integer, Class<? extends Worker>> map = new HashMap<>();

    /**
     * Map where index is primary key
     * @return map of index and workers
     */
    public static HashMap<Integer, Class<? extends Worker>> getMap() {
        return Workers.map;
    }

    /**
     * Get the worker class by the argument index
     * @param paramID the index
     * @return the worker class
     */
    public static Class<? extends Worker> getByID(int paramID) {
        if(Workers.getMap().containsKey(paramID)) return Workers.getMap().get(paramID);
        return null;
    }

    static {
        for(Workers worker : Workers.values()) {
            if(Workers.getMap().containsKey(worker.getIndex())) throw new RuntimeException("No primary index for worker type");
            Workers.getMap().put(worker.getIndex(), worker.getWorkerType());
        }
    }
}

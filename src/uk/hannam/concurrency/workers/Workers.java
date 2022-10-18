package uk.hannam.concurrency.workers;

import java.util.HashMap;

public enum Workers {

    ADD_WORKER(0, AddWorker.class),
    REMOVE_WORKER(1, RemoveWorker.class);

    private int index;
    private Class<? extends Worker> workerType;

    public int getIndex() {
        return this.index;
    }

    public <T extends Worker> Class<? extends Worker> getWorkerType() {
        return this.workerType;
    }

    Workers(int paramIndex, Class<? extends Worker> paramWorkerType) {
        this.index = paramIndex;
        this.workerType = paramWorkerType;
    }

    private static HashMap<Integer, Class<? extends Worker>> map = new HashMap<>();

    public static <T extends Worker> Class<? extends Worker> getByID(int paramID) {
        if(Workers.map.containsKey(paramID)) return Workers.map.get(paramID);
        return null;
    }

    static {
        for(Workers worker : Workers.values()) {
            Workers.map.put(worker.getIndex(), worker.getWorkerType());
        }
    }
}

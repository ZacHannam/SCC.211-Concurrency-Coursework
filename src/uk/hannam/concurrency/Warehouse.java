package uk.hannam.concurrency;

import uk.hannam.concurrency.workers.AddWorker;
import uk.hannam.concurrency.workers.RemoveWorker;
import uk.hannam.concurrency.workers.Worker;
import uk.hannam.concurrency.workers.Workers;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class Warehouse {

    /**
     * Whether the statements should be printed
     */
    private boolean printStatus = true;

    /**
     * Set whether the program should print the results
     * @param paramPrintStatus value of if the program should print
     */
    protected void setPrintStatus(Boolean paramPrintStatus){
        this.printStatus = paramPrintStatus;
    }
    public boolean getPrintStatus(){
        return this.printStatus;
    }

    /**
     * Timeout is the time between checking the lock
     */
    private static final long timeOut = 1;
    public static long getTimeout() { return timeOut; }

    /**
     * Lock controls the integer and checking if it is in use
     */
    private boolean lock;
    public synchronized void lock() { this.lock = true; }
    public synchronized void unlock() { this.lock = false; }
    public synchronized boolean isLocked() { return this.lock; }

    /**
     * removeWorkers - workers that remove 1 from the count
     * addWorkers - workers that add 1 to the count
     * flag -
     */
    private Map<Integer, Integer> numberOfWorkers;
    private final int flag;
    public int getFlag() {
        return this.flag;
    }

    public Map<Integer, Integer> getNumberOfWorkers() {
        return this.numberOfWorkers;
    }

    /**
     * Synchronized as edited by multiple threads at once
     */
    private volatile int count;

    /**
     * Change the amount on the counter
     * @param paramAmount amount to change count by
     * @return the changed amount
     */
    public int changeAmount(int paramAmount) {
        if(flag != 0) {
            return this.__changeAmount(paramAmount);
        }
        synchronized(this) {
            return this.__changeAmount(paramAmount);
        }
    }
    private int __changeAmount(int paramAmount){
        //noinspection NonAtomicOperationOnVolatileField
        this.count += paramAmount;
        return this.count;
    }

    /**
     * Get the count on the counter
     * @return the count
     */
    public int getCount() {
        if (flag != 0) {
            return this.__getCount();
        }
        synchronized (this) {
            return this.__getCount();
        }
    }
    private int __getCount() {
        return this.count;
    }

    private void printFinalCount() { if(this.getPrintStatus()) System.out.println("Final Inventory size = " + this.getCount()); }

    /**
     * List of all threads
     */
    private final List<Worker> workers = new ArrayList<>();
    private List<Worker> getWorkers() { return this.workers; }
    private void addWorker(Worker paramWorker) { this.getWorkers().add(paramWorker); }

    private void instantiateWorkerThreads() {
        try {

            Class<?>[] constructorParam = {Warehouse.class};

            for(int id : this.getNumberOfWorkers().keySet()) {

                if(Workers.getByID(id) == null) {
                    throw new NullPointerException();
                }

                for(int n = 0; n < this.getNumberOfWorkers().get(id); n++) {
                    Worker worker = Objects.requireNonNull(Workers.getByID(id)).getConstructor(constructorParam).newInstance(this);
                    this.addWorker(worker);
                }
            }

        } catch(NullPointerException | InstantiationError | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Error instantiating workers");
        }
    }

    /**
     * Runs all the worker threads
     */
    private void runThreads() {

        for (Worker worker : this.getWorkers()) {
            worker.start();
        }
    }

    /**
     * Runs the warehouse simulation
     */
    public void run(){
        this.instantiateWorkerThreads();
        this.runThreads();
        this.printFinalCount();
    }

    /**
     * @param paramFlag The bug flag
     */
    public Warehouse(Map<Integer, Integer> paramNumberOfWorkers, int paramFlag) {
        this.numberOfWorkers = paramNumberOfWorkers;
        this.flag = paramFlag;
    }

}

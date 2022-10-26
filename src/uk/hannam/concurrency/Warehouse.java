package uk.hannam.concurrency;

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
    @SuppressWarnings("unused")
    public void setPrintStatus(Boolean paramPrintStatus){
        this.printStatus = paramPrintStatus;
    }

    /**
     * Get whether the results should be printed
     * @return if the method will give an output
     */
    public boolean getPrintStatus(){
        return this.printStatus;
    }

    private final Map<Integer, Integer> numberOfWorkers;
    private final int flag;

    /**
     * Returns the value of the flag
     * @return the value of the flag
     */
    public int getFlag() {
        return this.flag;
    }

    /**
     * Get the number of workers for each argument i.e {0: 10, 1: 50} where 0 and 1 are addWorker and removeWorker
     * @return the worker map
     */
    public Map<Integer, Integer> getNumberOfWorkers() {
        return this.numberOfWorkers;
    }

    /**
     * Synchronized as edited by multiple threads at once
     */
    private volatile int count;

    /**
     * Change the amount on the counter
     * @param paramCount amount to change count by
     * @return the changed amount
     */
    public int changeAmount(int paramCount, String paramDescription) {
        if(flag != 0) {
            return this.__changeAmount(paramCount, paramDescription);
        }
        synchronized(this) {
            return this.__changeAmount(paramCount, paramDescription);
        }
    }
    private int __changeAmount(int paramAmount, String paramDescription){
        //noinspection NonAtomicOperationOnVolatileField
        this.count += paramAmount;
        if (this.getPrintStatus()) System.out.println(paramDescription + ". Inventory size = " + this.count);
        return this.count;
    }

    /**
     * Set the amount of inventory
     * @param paramCount amount to set count to
     * @return the new amount
     */
    public int setAmount(int paramCount, String paramDescription) {
        if(flag != 0) {
            return this.__setAmount(paramCount, paramDescription);
        }
        synchronized(this) {
            return this.__setAmount(paramCount, paramDescription);
        }
    }

    private int __setAmount(int paramCount, String paramDescription) {
        this.count = paramCount;
        if (this.getPrintStatus()) System.out.println(paramDescription + ". Inventory size = " + this.count);
        return this.count;
    }

    /**
     * Get the count of the inventory
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

    /**
     * Prints the final count of inventory
     */
    private void printFinalCount() { if(this.getPrintStatus()) System.out.println("Final Inventory size = " + this.getCount()); }

    private final List<Worker> workers = new ArrayList<>();

    /**
     * Get the list of all the worker threads
     * @return worker threads
     */
    public List<Worker> getWorkers() { return this.workers; }

    private void addWorker(Worker paramWorker) { this.getWorkers().add(paramWorker); }

    /**
     * Creates all the worker threads
     */
    private void instantiateWorkerThreads() {
        try {

            final Class<?>[] constructorParam = {Warehouse.class};

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

        for (Worker worker : this.getWorkers()) {
            try {
                worker.join();
            } catch (InterruptedException e) {
                throw new RuntimeException("Exception in joining threads");
            }
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

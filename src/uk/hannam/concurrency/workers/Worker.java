package uk.hannam.concurrency.workers;

import uk.hannam.concurrency.Warehouse;

import java.util.Random;

public abstract class Worker extends Thread{

    private static final int MAX_WAIT_TIME = 10;

    /**
     * The description of the worker, i.e. Added, Removed
     * @return the description
     */
    public abstract String getDescription();

    /**
     * The amount that is added to or removed from inventory
     * @return the added amount
     */
    public abstract int getAddedAmount();

    public void run() {

        if (this.getWarehouse().getFlag() == 0) {
            this.runWithoutBugs();
        } else {
            this.runWithBugs();
        }
    }

    private synchronized void runWithoutBugs() {
        this.getWarehouse().changeAmount(this.getAddedAmount(), this.getDescription());
    }

    private synchronized void runWithBugs() {

        // This is bugged as it reads the count and waits before making a change to the count. This means other threads might change the count.

        int currentCount = this.getWarehouse().getCount();

        try {
            Random random = new Random();
            super.wait(random.nextInt(MAX_WAIT_TIME) + 1);
        } catch (InterruptedException e) {
            throw new RuntimeException("Error in waiting for threads.");
        }

        this.getWarehouse().setAmount(currentCount + this.getAddedAmount(), this.getDescription());
    }

    private final Warehouse warehouse;

    /**
     * Get the warehouse or parent of the thread
     * @return thread parent
     */
    public Warehouse getWarehouse(){
        return this.warehouse;
    }

    public Worker(Warehouse paramWarehouse){
        this.warehouse = paramWarehouse;
    }

}

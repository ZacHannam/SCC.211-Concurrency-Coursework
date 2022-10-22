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

        // Using lock.lock() will wait until the lock is unlocked, however it is added to a queue.

        // Wait for lock to be unlocked

        while(this.getWarehouse().getLock().isWriteLocked()) {
            Random random = new Random();
            try {
                super.wait(0, random.nextInt(MAX_WAIT_TIME) + 1);
            } catch (InterruptedException e) {
                throw new RuntimeException("Error in waiting for threads.");
            }
        }

        // ^^ not needed but randomises the thread that acquires the lock

        this.getWarehouse().getLock().writeLock().lock();
        try {
            int result = this.getWarehouse().changeAmount(this.getAddedAmount());

            if (this.getWarehouse().getPrintStatus()) System.out.println(this.getDescription() + ". Inventory size = " + result);

        } finally {
            this.getWarehouse().getLock().writeLock().unlock();
        }
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

        int result = this.getWarehouse().setAmount(currentCount + this.getAddedAmount());

        if(this.getWarehouse().getPrintStatus()) System.out.println(this.getDescription() + ". Inventory size = " + result);

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

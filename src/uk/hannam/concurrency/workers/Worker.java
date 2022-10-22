package uk.hannam.concurrency.workers;

import uk.hannam.concurrency.Warehouse;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public abstract class Worker extends Thread{

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

    public synchronized void run() {

        int result;

        if(this.getWarehouse().getFlag() == 0) {

            this.getWarehouse().getLock().lock();
            try {

                result = this.getWarehouse().changeAmount(this.getAddedAmount());

                if(this.getWarehouse().getPrintStatus()) System.out.println(this.getDescription() + ". Inventory size = " + result);

            } finally {
                this.getWarehouse().getLock().unlock();
            }



        } else {

            int currentCount = this.getWarehouse().getCount();

            try {
                Random random = new Random();
                wait(random.nextInt(10) + 1);
            } catch (InterruptedException e) {
                throw new RuntimeException("Error in waiting for threads.");
            }

            result = this.getWarehouse().setAmount(currentCount + this.getAddedAmount());

            if(this.getWarehouse().getPrintStatus()) System.out.println(this.getDescription() + ". Inventory size = " + result);
        }
    }

    private final Warehouse warehouse;
    public Warehouse getWarehouse(){
        return this.warehouse;
    }

    public Worker(Warehouse paramWarehouse){
        this.warehouse = paramWarehouse;
    }

}

package uk.hannam.concurrency.workers;

import uk.hannam.concurrency.Warehouse;

import java.util.Random;

public abstract class Worker extends Thread{

    public abstract String getDescription();
    public abstract int getAddedAmount();

    public synchronized void run() {

        while(this.getWarehouse().isLocked()) {
            try {
                wait(Warehouse.getTimeout());
            } catch (InterruptedException e) {
                throw new RuntimeException("Error in waiting for threads.");
            }
        }

        this.getWarehouse().lock();

        int result;

        if(this.getWarehouse().getFlag() == 0) {


            result = this.getWarehouse().changeAmount(this.getAddedAmount());

        } else {

            int currentCount = this.getWarehouse().getCount();

            try {
                Random random = new Random();
                wait(random.nextInt(10) + 1);
            } catch (InterruptedException e) {
                throw new RuntimeException("Error in waiting for threads.");
            }

            result = this.getWarehouse().setAmount(currentCount + this.getAddedAmount());
        }

        if(this.getWarehouse().getPrintStatus()) System.out.println(this.getDescription() + ". Inventory size = " + result);

        this.getWarehouse().unlock();

    }

    private final Warehouse warehouse;
    public Warehouse getWarehouse(){
        return this.warehouse;
    }

    public Worker(Warehouse paramWarehouse){
        this.warehouse = paramWarehouse;
    }

}

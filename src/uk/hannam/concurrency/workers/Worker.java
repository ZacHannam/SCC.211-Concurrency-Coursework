package uk.hannam.concurrency.workers;

import uk.hannam.concurrency.Warehouse;

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

        int result = this.getWarehouse().changeAmount(this.getAddedAmount());

        /*
        Inserted Code to further bug when the flag is set to 1
         */
        if(this.getWarehouse().getFlag() != 0) {
            for (int n = 0; n < 5; n++) {
                if(result == this.getWarehouse().getCount()) {
                    break;
                }
                this.getWarehouse().changeAmount(this.getAddedAmount());
            }
        }
        /*
                                    END
         */

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

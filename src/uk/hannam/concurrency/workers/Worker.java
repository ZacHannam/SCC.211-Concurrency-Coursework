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
            System.out.print(""); // call completely messes up synchronisation
            for (int n = 0; n < 5; n++) {
                System.out.print(""); // call completely messes up synchronisation
                if(result == this.getWarehouse().getCount()) {
                    break;
                }
                this.getWarehouse().changeAmount(this.getAddedAmount());
            }
            this.getWarehouse().changeAmount((int) Math.ceil(this.getWarehouse().getCount() / (double) this.getWarehouse().getWorkers().size()));

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

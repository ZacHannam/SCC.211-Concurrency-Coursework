package uk.hannam.concurrency.workers;

import uk.hannam.concurrency.Warehouse;

public class AddWorker extends Worker {

    public AddWorker(Warehouse paramWarehouse) {
        super(paramWarehouse);
    }

    @Override
    public String getDescription() {
        return "Added";
    }

    @Override
    public int getAddedAmount() {
        return 1;
    }
}

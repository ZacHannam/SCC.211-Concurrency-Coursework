package uk.hannam.concurrency.workers;

import uk.hannam.concurrency.Warehouse;

public class RemoveWorker extends Worker {

    public RemoveWorker(Warehouse paramWarehouse) {
        super(paramWarehouse);
    }

    @Override
    public String getDescription() {
        return "Removed";
    }

    @Override
    public int getAddedAmount() {
        return -1;
    }
}

package uk.hannam.concurrency;

public class Main {

    public static void main(String[] args) {

        try {

            int addWorkers = Integer.parseInt(args[0]);
            int removeWorkers = Integer.parseInt(args[1]);
            int flag = Integer.parseInt(args[2]);

            Warehouse warehouse = new Warehouse(addWorkers, removeWorkers, flag);
            warehouse.run();

        } catch(NumberFormatException | IndexOutOfBoundsException e) {
            throw new RuntimeException("Usage: <n. Add Workers> <n. Remove Workers> <flag [0-1]>");
        }


    }
}

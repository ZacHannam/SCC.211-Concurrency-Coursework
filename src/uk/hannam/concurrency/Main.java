package uk.hannam.concurrency;

import uk.hannam.concurrency.workers.Workers;

import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        try {

            Map<Integer, Integer> numberOfWorkers = new HashMap<>();

            for(int argIndex = 0; argIndex < args.length - 1; argIndex++) {
                numberOfWorkers.put(argIndex, Integer.parseInt(args[argIndex]));
            }
            int flag = Integer.parseInt(args[2]);

            Warehouse warehouse = new Warehouse(numberOfWorkers, flag);
            warehouse.run();

        } catch(NumberFormatException | IndexOutOfBoundsException e) {
            throw new RuntimeException("Usage: <n. Add Workers> <n. Remove Workers> <flag [0-1]>");
        }
    }
}

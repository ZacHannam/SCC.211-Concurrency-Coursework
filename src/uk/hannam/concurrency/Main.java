package uk.hannam.concurrency;

import uk.hannam.concurrency.workers.Workers;

import java.util.HashMap;
import java.util.Map;

public class Main {

    public static String getUsageMessage() {
        StringBuilder usage = new StringBuilder("Usage: ");
        for(Workers workerType : Workers.values()) {
            usage.append("<n. ").append(workerType.toString()).append("> ");
        }
        usage.append("<flag [0-1]>");
        return usage.toString();
    }

    public static void main(String[] args) {
        try {

            if(args.length != Workers.values().length + 1) {
                throw new IndexOutOfBoundsException();
            }

            Map<Integer, Integer> numberOfWorkers = new HashMap<>();

            for(int argIndex = 0; argIndex < args.length - 1; argIndex++) {
                numberOfWorkers.put(argIndex, Integer.parseInt(args[argIndex]));
            }
            int flag = Integer.parseInt(args[args.length-1]);

            Warehouse warehouse = new Warehouse(numberOfWorkers, flag);
            warehouse.run();

        } catch(NumberFormatException | IndexOutOfBoundsException e) {
            throw new RuntimeException(getUsageMessage());
        }
    }
}

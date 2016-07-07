import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.FileAppender;
import org.apache.log4j.spi.LoggingEvent;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;


/**
 * Created by kevinkim on 2016-07-07.
 */
public class ValidatorAppender extends FileAppender {
        ArrayList<String> eventsList = new ArrayList();

        //TODO get path from properties file
        Path logPath = Paths.get("C:\\Users\\kevinkim\\src\\ValidatorAppender\\log\\log.txt");
        Multimap<String, String> metricsMap = HashMultimap.create();


        // Whenever log append is used store event information
        @Override
        public void append(LoggingEvent event) {
            eventsList.add(String.valueOf(event.timeStamp));
            eventsList.add(event.getLoggerName());
            eventsList.add(String.valueOf(event.getMessage()));
        }

        public void close() {
        }

        public boolean requiresLayout() {
            return false;
        }

         // Parse log file, Check consistency for specific Key named "label"
        public void validateMetric(String label) throws IOException {
            System.out.println("=== Validating " + label + " logged Metrics ===");
            String key,value;
            for (String line : Files.readAllLines(logPath)) {
                key = line.split(" ")[0];
                value = line.split(" ")[1];
                metricsMap.put(key,value);
            }

            Collection results = metricsMap.get(label);
            if ( results.size() == 1) {
                System.out.println((char)27 + "[32m" + label +" \t"+ results + "\t[CONSISTENT]" + (char)27 + "[0m");
            } else {
                System.out.println((char)27 + "[31m" + label +" \t"+ results + "\t[INCONSISTENT]" + (char)27 + "[0m");
            }



        }

        // Parse log file, Check consistency for each Keys
        public void validateAll() throws IOException {
            System.out.println("=== Validate All logged Metrics ===");
            String key,value;
            for (String line : Files.readAllLines(logPath)) {
                key = line.split(" ")[0];
                value = line.split(" ")[1];
                metricsMap.put(key,value);
            }

            for (Object k : metricsMap.keySet()) {
                Collection results = metricsMap.get(k.toString());
                if ( results.size() == 1) {
                    System.out.println((char)27 + "[32m" + k +" \t"+ results + "\t[CONSISTENT]" + (char)27 + "[0m");
                } else {
                    System.out.println((char)27 + "[31m" + k+" \t"+ results + "\t[INCONSISTENT]" + (char)27 + "[0m");
                }
            }
        }


}


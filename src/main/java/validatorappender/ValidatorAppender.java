package validatorappender;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;


/**
 * Created by kevinkim on 2016-07-07.
 */
public class ValidatorAppender extends AppenderSkeleton {

        private ArrayList<String> eventsList = new ArrayList();
        private Logger logger = Logger.getLogger("validatorLog"); //Defining the Logger
        private FileAppender appender = (FileAppender)logger.getAppender("validator");
        private Path logPath = Paths.get(appender.getFile());
        private String logPathStr = appender.getFile();
        private Multimap<String, String> metricsMap = HashMultimap.create();
        private Multimap<String, String> timestampMap = HashMultimap.create();

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
             String key,value,timestamp;
             try {
                 File file = new File(logPathStr);
                 FileReader fileReader = new FileReader(file);
                 BufferedReader bufferedReader = new BufferedReader(fileReader);
                 String line;
                 while ((line = bufferedReader.readLine()) != null) {
                     key = line.split(" ")[0];
                     value = line.split(" ")[1];
                     timestamp = line.split(" ")[3];
                     metricsMap.put(key,value);
                     timestampMap.put(value,timestamp);
                 }
                 fileReader.close();
             } catch (IOException e) {
                 e.printStackTrace();
             }

            Collection results = metricsMap.get(label);
            Collection timestamps;
            if ( results.size() == 1) {
                System.out.println((char)27 + "[32m" + label + "\t[CONSISTENT]" + (char)27 + "[0m");
                for (Object result : results){
                    timestamps = timestampMap.get(result.toString());
                    System.out.println((char)27 + "[32m\t" + result+ "\t" + timestamps + (char)27 + "[0m");
                }
            } else {
                System.out.println((char)27 + "[31m" + label + "\t[INCONSISTENT]" + (char)27 + "[0m");
                for (Object result : results){
                    timestamps = timestampMap.get(result.toString());
                    System.out.println((char)27 + "[31m\t" + result+ "\t" + timestamps + (char)27 + "[0m");
                }
            }
        }

        // Parse log file, Check consistency for each Keys
        public void validateAll() throws IOException {
            System.out.println("=== Validate All logged Metrics ===");

            String key,value,timestamp;
            try {
                File file = new File(logPathStr);
                FileReader fileReader = new FileReader(file);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    key = line.split(" ")[0];
                    value = line.split(" ")[1];
                    timestamp = line.split(" ")[3];
                    metricsMap.put(key,value);
                    timestampMap.put(value,timestamp);
                }
                fileReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Collection timestamps;
            for (Object k : metricsMap.keySet()) {
                Collection results = metricsMap.get(k.toString());

                if ( results.size() == 1) {
                    System.out.println((char)27 + "[32m" + k.toString() + "\t[CONSISTENT]" + (char)27 + "[0m");
                    for (Object result : results){
                        timestamps = timestampMap.get(result.toString());
                        System.out.println((char)27 + "[32m\t" + result+ "\t" + timestamps + (char)27 + "[0m");
                    }
                } else {
                    System.out.println((char)27 + "[31m" + k.toString() + "\t[INCONSISTENT]" + (char)27 + "[0m");
                    for (Object result : results){
                        timestamps = timestampMap.get(result.toString());
                        System.out.println((char)27 + "[31m\t" + result+ "\t" + timestamps + (char)27 + "[0m");
                    }
                }
            }
        }


}


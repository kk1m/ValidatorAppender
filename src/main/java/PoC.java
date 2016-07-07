import org.apache.log4j.Logger;

import java.io.IOException;

/**
 * Created by kevinkim on 2016-07-07.
 */
public class PoC {


    public static void main(String []args) throws IOException {
        Logger l = Logger.getLogger("PoC");

        //Takes the log event as a ("key" + value) and store in logfile
        ValidatorAppender app = new ValidatorAppender();

        //Current usage method requires to add appender
        //TODO create a custom appender module
        l.addAppender(app);

        l.warn("RDD1.count " + 100);
        l.warn("RDD1.count " + 100);
        l.warn("RDD1.count " + 111);

        l.warn("RDD2.count " + 2000);
        l.warn("RDD2.count " + 2000);

        l.warn("RDD3.count " + 300);
        l.warn("RDD3.count " + 300);
        l.trace("RDD3.count " + 301);

        //Validate specific metric labeled by key "RDD1.count"
        app.validateMetric("RDD1.count");

        //Validate all metrics
        app.validateAll();
    }
}

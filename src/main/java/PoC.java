import validatorappender.*;

import org.apache.log4j.Logger;

import java.io.IOException;

/**
 * Created by kevinkim on 2016-07-07.
 */
public class PoC {

    //Example of using the SparkValidator
    public static void main(String []args) throws IOException {

        //=======To Use the "validator" logger==========
        Logger valLog = Logger.getLogger("validatorLog");
        ValidatorAppender app = new ValidatorAppender();
        valLog.addAppender(app);
        //===============================================

        valLog.debug("RDD1.count " + 100);
        valLog.debug("RDD1.count " + 100);
        valLog.debug("RDD1.count " + 100);

        valLog.debug("RDD2.count " + 2000);
        valLog.debug("RDD2.count " + 2001);

        valLog.debug("RDD3.count " + 300);
        valLog.debug("RDD3.count " + 300);
        valLog.debug("RDD3.count " + 301);

        //Validate specific metric labeled by key "RDD1.count"
        //app.validateMetric("RDD1.count");

        //Validate all metrics
        app.validateAll();
    }
}

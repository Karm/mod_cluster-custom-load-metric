package biz.karms.modcluster;

import static org.jboss.logging.Logger.Level.ERROR;
import static org.jboss.logging.Logger.Level.INFO;
import static org.jboss.logging.Logger.Level.WARN;

import org.jboss.logging.Logger;
import org.jboss.logging.annotations.Cause;
import org.jboss.logging.annotations.LogMessage;
import org.jboss.logging.annotations.Message;
import org.jboss.logging.annotations.MessageLogger;

/**
 * 
 * @author Michal Karm Babacek <mbabacek@redhat.com>
 * 
 */
@MessageLogger(projectCode = "MODCLUSTER")
public interface CustomLoadMetricLogger {
  CustomLoadMetricLogger LOGGER = Logger.getMessageLogger(CustomLoadMetricLogger.class, CustomLoadMetricLogger.class.getPackage().getName());

  @LogMessage(level = INFO)
  @Message(id = 603, value = "Found load %f in file %s.")
  void loadFound(double load, String loadfile);

  @LogMessage(level = ERROR)
  @Message(id = 604, value = "This is a totally wrong regexp: %s Try e.g. ^LOAD: ([0-9]*)$")
  void patternSyntaxWrong(@Cause Throwable cause, String pattern);

  @LogMessage(level = ERROR)
  @Message(id = 605, value = "I can't load this file: %s. The returned load will be 0, putting this worker in the standby mode.")
  void fileNotFound(@Cause Throwable cause, String loadfile);

  @LogMessage(level = ERROR)
  @Message(id = 606, value = "I can't parse Double from what your regular expression matcher group 1 returned: %s. The returned load will be 0, putting this worker in the standby mode.")
  void cantParseDobule(@Cause Throwable cause, String result);

  @LogMessage(level = WARN)
  @Message(id = 607, value = "No load has been found in the file %s, I treat is as an error and I set the worker to standby mode with load 0.")
  void noLoadFound(String loadfile);

}

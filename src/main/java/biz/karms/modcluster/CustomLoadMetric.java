package biz.karms.modcluster;

import java.io.FileInputStream;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jboss.modcluster.container.Engine;
import org.jboss.modcluster.load.metric.impl.AbstractLoadMetric;

/**
 * @author Michal Karm Babacek
 */
public class CustomLoadMetric extends AbstractLoadMetric {

  private String loadfile;
  private Pattern pattern;

  @Override
  public double getLoad(Engine engine) throws Exception {
    Scanner scanner = null;
    try {
      scanner = new Scanner(new FileInputStream(loadfile), "UTF-8");
      while (scanner.hasNextLine()) {
        Matcher matcher = pattern.matcher(scanner.nextLine());
        if (matcher.matches()) {
          // We ain't gonna read the rest of the file once the load has been found.
          return Double.parseDouble(matcher.group(1));
        }
      }
    } finally {
      if (scanner != null) {
        scanner.close();
      }
    }
    // No load has been found in the file, we treat is as an error and we set the worker to standby with code 0.
    return 0;
  }

  public void setLoadfile(String loadfile) {
    this.loadfile = loadfile;
  }

  public void setParseexpression(String parseexpression) {
    pattern = Pattern.compile(parseexpression);
  }
}

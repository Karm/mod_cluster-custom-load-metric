package biz.karms.modcluster;

import org.jboss.modcluster.container.Engine;
import org.jboss.modcluster.load.metric.impl.AbstractLoadMetric;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * @author Michal Karm Babacek
 */
public class CustomLoadMetric extends AbstractLoadMetric {

    private String loadfile;
    private Pattern pattern;

    @Override
    public double getLoad(Engine engine) throws Exception {
        Scanner scanner = null;
        String groupOneResult = null;
        try {
            scanner = new Scanner(new FileInputStream(loadfile), "UTF-8");
            while (scanner.hasNextLine()) {
                Matcher matcher = pattern.matcher(scanner.nextLine());
                if (matcher.matches() && matcher.group(1) != null) {
                    // We ain't gonna read the rest of the file once the load has been found.
                    groupOneResult = matcher.group(1);
                    double load = Double.parseDouble(groupOneResult);
                    CustomLoadMetricLogger.LOGGER.loadFound(load, loadfile);
                    return load;
                }
            }
        } catch (FileNotFoundException e) {
            CustomLoadMetricLogger.LOGGER.fileNotFound(e, loadfile);
        } catch (NumberFormatException e) {
            CustomLoadMetricLogger.LOGGER.cantParseDobule(e, groupOneResult);
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
        CustomLoadMetricLogger.LOGGER.noLoadFound(loadfile);
        return 0;
    }

    public void setLoadfile(String loadfile) {
        this.loadfile = loadfile;
    }

    public void setParseexpression(String parseexpression) {
        try {
            pattern = Pattern.compile(parseexpression);
        } catch (PatternSyntaxException e) {
            CustomLoadMetricLogger.LOGGER.patternSyntaxWrong(e, parseexpression);
        }
    }
}

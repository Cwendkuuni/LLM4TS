import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.parser.DefaultParser;
import org.junit.Assert;
import org.junit.Test;

public class ParserTest {

 private final static String[] TEST_ARGUMENTS = new String[]{"-a", "--optionB=valueB", "-o", "output.txt", "--", "arg1", "arg2"};

 @Test
 public void testParser() throws ParseException {
 CommandLineParser parser = new DefaultParser();
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", true, "Option B");
 options.addOption("o", true, "Output file");
 options.addRequiredOption("optionC", true, "Option C", true);

 CommandLine cmd = parser.parse(options, TEST_ARGUMENTS, false);

 Assert.assertEquals("Option A should be set", true, cmd.hasOption("a"));
 Assert.assertEquals("Option B should be set", "valueB", cmd.getOptionValue("b"));
 Assert.assertEquals("Output file should be set", "output.txt", cmd.getOptionValue("o"));
 Assert.assertEquals("Option C should be set", "requiredOptionC", cmd.getOptionValue("optionC"));
 }

 @Test(expected = ParseException.class)
 public void testMissingRequiredOption() throws ParseException {
 CommandLineParser parser = new DefaultParser();
 Options options = new Options();
 options.addRequiredOption("optionA", true, "Option A", true);

 String[] arguments = new String[0];
 CommandLine cmd = parser.parse(options, arguments, false);
 }

 @Test(expected = ParseException.class)
 public void testInvalidOption() throws ParseException {
 CommandLineParser parser = new DefaultParser();
 Options options = new Options();
 options.addOption("a", true, "Option A");

 String[] arguments = {"--invalidOption"};
 CommandLine cmd = parser.parse(options, arguments, false);
 }

 @Test(expected = ParseException.class)
 public void testMissingValue() throws ParseException {
 CommandLineParser parser = new DefaultParser();
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", true, "Option B", true);

 String[] arguments = {"-a"};
 CommandLine cmd = parser.parse(options, arguments, false);
 }

 @Test(expected = ParseException.class)
 public void testExtraArguments() throws ParseException {
 CommandLineParser parser = new DefaultParser();
 Options options = new Options();
 options.addOption("a", true, "Option A");

 String[] arguments = {"-a", "--optionB=valueB", "extraArg"};
 CommandLine cmd = parser.parse(options, arguments, false);
 }

 @Test
 public void testProperties() throws ParseException {
 CommandLineParser parser = new DefaultParser();
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", true, "Option B", true);
 options.addOption("p", true, "Property name", true);

 Properties properties = new Properties();
 properties.put("p", "property value");
 CommandLine cmd = parser.parse(options, null, properties, false);

 Assert.assertEquals("Property should be set", "property value", cmd.getOptionValue("p"));
 }
}
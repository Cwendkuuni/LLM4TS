import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ParserTest {

 private Parser parser;
 private Options options;

 @Before
 public void setUp() {
 options = new Options();
 options.addOption("i", "inputFile", true, "input file path");
 options.addOption("o", "outputFile", true, "output file path");
 options.addOption("h", "help", false, "display help message");
 }

 @Test
 public void testParseWithValidArguments() throws ParseException {
 parser = new DefaultParser();
 CommandLine cmd = parser.parse(options, new String[]{"-i", "input.txt", "-o", "output.txt"});
 Assert.assertNotNull(cmd);
 Assert.assertEquals("input.txt", cmd.getOptionValue("inputFile"));
 Assert.assertEquals("output.txt", cmd.getOptionValue("outputFile"));
 Assert.assertFalse(cmd.hasOption("help"));
 }

 @Test(expected = ParseException.class)
 public void testParseWithInvalidArguments() throws ParseException {
 parser = new DefaultParser();
 CommandLine cmd = parser.parse(options, new String[]{"--invalidOption"});
 }

 @Test(expected = MissingOptionException.class)
 public void testMissingRequiredOptions() throws ParseException {
 parser = new DefaultParser();
 CommandLine cmd = parser.parse(options, new String[]{"-o"});
 }

 @Test(expected = MissingArgumentException.class)
 public void testMissingArgument() throws ParseException {
 parser = new DefaultParser();
 CommandLine cmd = parser.parse(options, new String[]{"-i"});
 }

 @Test
 public void testParseWithProperties() throws ParseException {
 Properties properties = new Properties();
 properties.setProperty("inputFile", "input.txt");
 properties.setProperty("outputFile", "output.txt");
 CommandLine cmd = parser.parse(options, new String[0], properties);
 Assert.assertEquals("input.txt", cmd.getOptionValue("inputFile"));
 Assert.assertEquals("output.txt", cmd.getOptionValue("outputFile"));
 }
}
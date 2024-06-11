import org.apache.commons.cli.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

 private String[] testArguments = new String[]{"-a", "--optionB", "-o", "output.txt", "--optionC=valueC"};
 private String[] testArgumentsWithHelp = new String[]{"--help"};
 private String[] testArgumentsWithInvalidOption = new String[]{"--invalidOption"};
 private String[] testArgumentsWithMissingOption = new String[]{"-a", "--optionB"};

 @Test
 public void testParseWithValidArguments() throws ParseException {
 Options options = new Options();
 options.addOption("a", "optionA", false, "Option A");
 options.addOption("b", "optionB", true, "Option B");
 options.addOption("o", "output", true, "Output file");
 options.addOption("c", "optionC", true, "Option C with value");

 Parser parser = new DefaultParser();
 CommandLine cmd = parser.parse(options, testArguments, null);

 assertTrue(cmd.hasOption("a"));
 assertFalse(cmd.hasOption("a").isSet());
 assertFalse(cmd.hasOption("b"));
 assertTrue(cmd.hasOption("b", true));
 assertFalse(cmd.hasOption("o"));
 assertTrue(cmd.hasOption("o"));
 assertEquals("output.txt", cmd.getOptionValue("o"));
 assertTrue(cmd.hasOption("c"));
 assertEquals("valueC", cmd.getOptionValue("c"));
 }

 @Test(expected = ParseException.class)
 public void testParseWithMissingRequiredOption() throws ParseException {
 Options options = new Options();
 options.addOption("a", "optionA", false, "Option A");
 options.addOption("b", "optionB", true, "Option B");
 options.addOption("c", "optionC", true, "Option C");
 options.setRequiredOptions(Arrays.asList("b"));

 Parser parser = new DefaultParser();
 CommandLine cmd = parser.parse(options, testArgumentsWithMissingOption, null);
 fail("Expected ParseException when option 'b' is missing");
 }

 @Test(expected = UnrecognizedOptionException.class)
 public void testParseWithInvalidOption() throws ParseException {
 Options options = new Options();
 options.addOption("a", "optionA", false, "Option A");
 options.addOption("b", "optionB", true, "Option B");

 Parser parser = new DefaultParser();
 CommandLine cmd = parser.parse(options, testArgumentsWithInvalidOption, null);
 fail("Expected UnrecognizedOptionException when encountering invalid option 'invalidOption'");
 }

 @Test(expected = MissingArgumentException.class)
 public void testParseWithMissingArgument() throws ParseException {
 Options options = new Options();
 options.addOption("a", "optionA", false, "Option A");
 options.addOption("o", "output", true, "Output file");

 Parser parser = new DefaultParser();
 CommandLine cmd = parser.parse(options, testArgumentsWithMissingOption, null);
 fail("Expected MissingArgumentException when option 'o' has no argument value");
 }

 @Test
 public void testParseWithHelp() throws ParseException {
 Options options = new Options();
 options.addOption("h", "help", false, "Prints help message.");
 options.addOption("a", "optionA", false, "Option A");

 Parser parser = new DefaultParser();
 CommandLine cmd = parser.parse(options, testArgumentsWithHelp, null);
 assertTrue(cmd.hasOption("h"));
 assertFalse(cmd.hasOptions());
 }

 @Test
 public void testParseWithHelpAndSingleOption() throws ParseException {
 Options options = new Options();
 options.addOption("h", "help", false, "Prints help message.");
 options.addOption("a", "optionA", false, "Option A");

 Parser parser = new DefaultParser();
 CommandLine cmd = parser.parse(options, new String[0], null);
 assertTrue(cmd.hasOption("h"));
 assertFalse(cmd.hasOptions());
 }

 @Test
 public void testParseWithHelpAndMultipleOptions() throws ParseException {
 Options options = new Options();
 options.addOption("h", "help", false, "Prints help message.");
 options.addOption("a", "optionA", false, "Option A");
 options.addOption("b", "optionB", true, "Option B");

 Parser parser = new DefaultParser();
 CommandLine cmd = parser.parse(options, new String[0], null);
 assertTrue(cmd.hasOption("h"));
 assertFalse(cmd.hasOption("a"));
 assertFalse(cmd.hasOption("b"));
 }

 @Test
 public void testParseWithHelpAndValidArguments() throws ParseException {
 Options options = new Options();
 options.addOption("h", "help", false, "Prints help message.");
 options.addOption("a", "optionA", false, "Option A");
 options.addOption("b", "optionB", true, "Option B");
 options.addOption("o", "output", true, "Output file");
 options.addOption("c", "optionC", true, "Option C with value");

 Parser parser = new DefaultParser();
 CommandLine cmd = parser.parse(options, testArguments, null);
 assertTrue(cmd.hasOption("h"));
 assertFalse(cmd.hasOptions());
 }

 @Test
 public void testParseWithProperties() throws ParseException {
 Options options = new Options();
 options.addOption("h", "help", false, "Prints help message.");
 options.addOption("a", "optionA", false, "Option A");
 options.addOption("b", "optionB", true, "Option B");
 options.addOption("o", "output", true, "Output file");
 options.addOption("c", "optionC", true, "Option C with value");

 Properties properties = new Properties();
 properties.setProperty("optionC", "testValue");

 CommandLine cmd = new DefaultParser().parse(options, testArguments, properties);
 assertTrue(cmd.hasOption("a"));
 assertFalse(cmd.hasOption("a").isSet());
 assertFalse(cmd.hasOption("b"));
 assertTrue(cmd.hasOption("b", true));
 assertFalse(cmd.hasOption("o"));
 assertTrue(cmd.hasOption("o"));
 assertEquals("output.txt", cmd.getOptionValue("o"));
 assertTrue(cmd.hasOption("c"));
 assertEquals("testValue", cmd.getOptionValue("c"));
 }
}
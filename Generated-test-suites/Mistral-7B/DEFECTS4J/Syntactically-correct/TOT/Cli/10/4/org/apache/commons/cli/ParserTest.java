import org.apache.commons.cli.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

 private String[] args = new String[]{"--input", "input.txt", "-o", "output.txt", "--help"};
 private String[] argsWithInvalidOption = new String[]{"--invalid-option"};
 private String[] argsWithMissingRequiredOption = new String[]{"--option1"};
 private String[] argsWithMissingValueForOptionalOption = new String[]{"--option1"};

 @Test
 public void testParse() throws ParseException {
 // Test parsing with valid options and arguments
 Parser parser = new DefaultParser();
 Options options = new Options();
 options.addRequiredOption("input", true, "i")
 .longOpt("input")
 .desc("Input file");
 options.addRequiredOption("o", true, "o")
 .longOpt("output")
 .desc("Output file");
 options.addOption("help", false)
 .longOpt("help")
 .desc("Prints help");
 parser.setOptions(options);
 CommandLine commandLine = parser.parse(options, args);
 assertEquals("input.txt", commandLine.getOptionValue("input"));
 assertEquals("output.txt", commandLine.getOptionValue("o"));
 assertFalse(commandLine.hasOption("help"));

 // Test parsing with help option
 CommandLine helpCommandLine = parser.parse(options, args, null, true);
 assertTrue(helpCommandLine.hasOption("help"));
 }

 @Test(expected = ParseException.class)
 public void testParseWithInvalidOption() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 parser.setOptions(options);
 parser.parse(options, argsWithInvalidOption);
 }

 @Test(expected = MissingArgumentException.class)
 public void testParseWithMissingValueForOptionalOption() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 options.addOption("option1", true)
 .longOpt("option1")
 .desc("Option 1")
 .valueSeparator('=')
 .desc("Value for option 1");
 parser.setOptions(options);
 CommandLine commandLine = parser.parse(options, argsWithMissingValueForOptionalOption);
 assertNull(commandLine.getOptionValue("option1"));
 }

 @Test(expected = MissingOptionException.class)
 public void testParseWithMissingRequiredOption() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 options.addRequiredOption("option1", true)
 .longOpt("option1")
 .desc("Option 1");
 parser.setOptions(options);
 try {
 parser.parse(options, argsWithMissingRequiredOption);
 fail("MissingRequiredOptionException was not thrown");
 } catch (MissingOptionException e) {
 assertTrue(e.getMessage().contains("Option 'option1' is required."));
 }
 }
}
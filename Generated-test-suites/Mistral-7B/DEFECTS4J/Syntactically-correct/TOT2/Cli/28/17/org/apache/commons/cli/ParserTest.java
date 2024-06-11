import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest
{
 private Parser parser;
 private Options options;
 private String[] arguments;

 @Before
 public void setup()
 {
 options = new Options();
 options.addOption("i", "input", true, "Input file path");
 options.addOption("o", "output", true, "Output file path");
 parser = new DefaultParser();
 parser.setOptions(options);
 arguments = new String[]{"-i", "test.txt", "-o", "output.txt"};
 }

 @Test
 public void testParseWithValidArguments() throws ParseException
 {
 CommandLine commandLine = parser.parse(options, arguments);
 assertTrue(commandLine.hasOption("i"));
 assertTrue(commandLine.hasOption("o"));
 assertEquals("test.txt", commandLine.getOptionValue("i"));
 assertEquals("output.txt", commandLine.getOptionValue("o"));
 }

 @Test(expected = MissingOptionException.class)
 public void testParseMissingRequiredOption() throws ParseException
 {
 options.addRequiredOption("r", "required", false, "A required option");
 arguments = new String[]{"-i", "test.txt"};
 parser.setOptions(options);
 try
 {
 parser.parse(options, arguments);
 fail("MissingRequiredOptionException expected");
 }
 catch (MissingOptionException e)
 {
 assertEquals("The following required options do not have assigned values: required", e.getMessage());
 }
 }

 @Test(expected = MissingArgumentException.class)
 public void testParseMissingArgument() throws ParseException
 {
 options.getOption("i").setRequired(false);
 arguments = new String[]{"-i"};
 parser.setOptions(options);
 try
 {
 parser.parse(options, arguments);
 fail("MissingArgumentException expected");
 }
 catch (MissingArgumentException e)
 {
 assertEquals("Required option 'i' is missing", e.getMessage());
 }
 }

 @Test(expected = UnrecognizedOptionException.class)
 public void testParseUnrecognizedOption() throws ParseException
 {
 arguments = new String[]{"--unrecognized"};
 parser.setOptions(options);
 try
 {
 parser.parse(options, arguments);
 fail("UnrecognizedOptionException expected");
 }
 catch (UnrecognizedOptionException e)
 {
 assertEquals("Unrecognized option: unrecognized", e.getMessage());
 }
 }

 @Test(expected = ParseException.class)
 public void testParseWithInvalidArguments() throws ParseException
 {
 options.addOption("i", "input", true, "Input file path");
 options.addOption("o", "output", true, "Output file path");
 options.addOption("z", "invalid", false, "Invalid option");
 arguments = new String[]{"-i", "test.txt", "-o", "output.txt", "--z"};
 parser.setOptions(options);
 try
 {
 parser.parse(options, arguments);
 fail("ParseException expected");
 }
 catch (ParseException e)
 {
 assertEquals("Unrecognized option: z", e.getMessage());
 }
 }
}
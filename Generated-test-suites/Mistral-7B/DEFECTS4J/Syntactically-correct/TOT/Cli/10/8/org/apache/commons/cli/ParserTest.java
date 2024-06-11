import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.NoSuchOptionException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert.*;

public class ParserTest {

 private Options options;
 private String[] args;

 @Before
 public void setUp() {
 options = new Options();
 options.addRequiredOption("i", "input", true, "Input file path");
 options.addOption("o", "output", true, "Output file path");
 options.addOption("h", "help", false, "Print help message");
 }

 @Test
 public void testParseWithValidArguments() throws ParseException {
 args = new String[]{"-i", "/path/to/input.txt", "-o", "/path/to/output.txt"};
 CommandLine cmd = new Parser().parse(options, args);
 assertEquals("/path/to/input.txt", cmd.getOptionValue("input"));
 assertEquals("/path/to/output.txt", cmd.getOptionValue("output"));
 Assert.assertFalse(cmd.hasOption("help"));
 }

 @Test(expected = MissingArgumentException.class)
 public void testParseMissingArgument() throws ParseException {
 args = new String[]{"-i", "/path/to/input.txt"};
 new Parser().parse(options, args);
 fail("MissingArgumentException expected but not thrown");
 }

 @Test(expected = MissingOptionException.class)
 public void testParseMissingRequiredOption() throws ParseException {
 args = new String[]{"-o", "/path/to/output.txt"};
 try {
 new Parser().parse(options, args);
 fail("MissingOptionException expected but not thrown");
 } catch (MissingOptionException e) {
 assertEquals("Missing required option 'input'", e.getMessage());
 }
 }

 @Test(expected = NoSuchOptionException.class)
 public void testParseUnknownOption() throws ParseException {
 args = new String[]{"--unknown"};
 try {
 new Parser().parse(options, args);
 fail("NoSuchOptionException expected but not thrown");
 } catch (NoSuchOptionException e) {
 assertEquals("Unknown option: --unknown", e.getMessage());
 }
 }

 @Test
 public void testParseHelpOption() throws ParseException {
 args = new String[]{"-h"};
 CommandLine cmd = new Parser().parse(options, args);
 assertTrue(cmd.hasOption("help"));
 }

 @Test
 public void testParseWithMixedArguments() throws ParseException {
 args = new String[]{"-i", "/path/to/input.txt", "--help", "-o", "/path/to/output.txt"};
 CommandLine cmd = new Parser().parse(options, args);
 assertEquals("/path/to/input.txt", cmd.getOptionValue("input"));
 assertEquals("/path/to/output.txt", cmd.getOptionValue("output"));
 assertTrue(cmd.hasOption("help"));
 }

 @Test(expected = UnrecognizedOptionException.class)
 public void testParseUnrecognizedOption() throws ParseException {
 args = new String[]{"--unknown-option"};
 try {
 new Parser().parse(options, args);
 fail("UnrecognizedOptionException expected but not thrown");
 } catch (UnrecognizedOptionException e) {
 assertEquals("Unrecognized option: --unknown-option", e.getMessage());
 }
 }

 @Test
 public void testParseWithEmptyArguments() throws ParseException {
 args = new String[0];
 CommandLine cmd = new Parser().parse(options, args);
 Assert.assertEquals(0, cmd.getArgs().size());
 }
}
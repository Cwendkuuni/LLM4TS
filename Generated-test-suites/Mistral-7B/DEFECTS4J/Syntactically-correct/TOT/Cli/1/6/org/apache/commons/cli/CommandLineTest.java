import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.*;

public class CommandLineTest {

 @Test
 public void testHasOption() {
 CommandLine commandLine = new CommandLine();
 Options options = new Options();
 options.addBooleanOption("test", "A test option");
 commandLine.parse(options.parse("--test"));
 assertTrue(commandLine.hasOption("test"));
 }

 @Test
 public void testHasOptionFalse() {
 CommandLine commandLine = new CommandLine();
 Options options = new Options();
 commandLine.parse(options.parse("--invalid-option"));
 assertFalse(commandLine.hasOption("invalid-option"));
 }

 @Test
 public void testHasOptionWithArgument() {
 Options options = new Options();
 options.addOption("input", true, "Specify input file");
 CommandLine commandLine = new CommandLine();
 commandLine.parse(options.parse("--input input.txt"));
 assertTrue(commandLine.hasOption("input"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testHasOptionWithNoArgument() {
 CommandLine commandLine = new CommandLine();
 Options options = new Options();
 options.addOption("input");
 commandLine.parse(options.parse(new String[0]));
 }

 @Test
 public void testGetOptionValue() {
 Options options = new Options();
 options.addOption("input", true, "Specify input file");
 CommandLine commandLine = new CommandLine();
 commandLine.parse(options.parse("--input input.txt"));
 assertEquals("input.txt", commandLine.getOptionValue("input"));
 }

 @Test
 public void testGetOptionValueNull() {
 CommandLine commandLine = new CommandLine();
 commandLine.parse(new String[0][0]);
 assertEquals(null, commandLine.getOptionValue("invalid-option"));
 }

 @Test
 public void testGetOptionValues() {
 Options options = new Options();
 options.addOption("inputs", true, "Specify input files, multiple allowed");
 CommandLine commandLine = new CommandLine();
 commandLine.parse(options.parse(new String[]{"--inputs", "input1.txt", "input2.txt"}));
 String[] expected = new String[]{"input1.txt", "input2.txt"};
 assertArrayEquals(expected, commandLine.getOptionValues("inputs"));
 }

 @Test
 public void testGetOptionValuesEmpty() {
 CommandLine commandLine = new CommandLine();
 commandLine.parse(new String[0][0]);
 assertArrayEquals(new String[0], commandLine.getOptionValues("invalid-option"));
 }

 @Test
 public void testGetOptionValuesNoArgument() {
 Options options = new Options();
 options.addOption("input");
 CommandLine commandLine = new CommandLine();
 commandLine.parse(options.parse(new String[0]));
 assertNull(commandLine.getOptionValues("input"));
 }

 @Test
 public void testAddArg() {
 CommandLine commandLine = new CommandLine();
 commandLine.parse(new String[0][0]);
 commandLine.addArg("arg1");
 List argList = commandLine.getArgList();
 assertEquals(1, argList.size());
 assertEquals("arg1", argList.get(0));
 }

 @Test
 public void testParseException() {
 Options options = new Options();
 options.addOption("input", true, "Specify input file");
 CommandLine commandLine = null;
 try {
 commandLine = new CommandLine();
 commandLine.parse(options.parse("--invalid-option = invalid-value"));
 fail("Expected ParseException");
 } catch (ParseException e) {
 // expected
 }
 assertNotNull(commandLine.getArgList());
 assertEquals(1, commandLine.getArgList().size());
 assertEquals("invalid-value", commandLine.getArgList().get(0));
 }

 @Test
 public void testParseExceptionWithArgs() {
 Options options = new Options();
 options.addOption("input", true, "Specify input file");
 CommandLine commandLine = null;
 try {
 commandLine = new CommandLine();
 commandLine.parse(options.parse("--invalid-option = invalid-value --arg1 arg2"));
 fail("Expected ParseException");
 } catch (ParseException e) {
 // expected
 }
 assertNotNull(commandLine.getArgList());
 assertEquals(2, commandLine.getArgList().size());
 assertEquals("arg1", commandLine.getArgList().get(0));
 assertEquals("arg2", commandLine.getArgList().get(1));
 }
}
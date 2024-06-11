import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.ParseException;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.WriteableCommandLineImpl;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

 @Test
 public void testBasicUsage() {
 WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(
 new Option("a", "apple", true, "An apple"),
 new Option("b", "banana", false)
 );
 writeableCommandLine.addOption(new Option("c", "color", 1, "Set the color", new String[]{"hue"}));
 writeableCommandLine.addSwitch(new Option("d", "debug", false));
 writeableCommandLine.addValue(writeableCommandLine.getOption("a"), "red");
 writeableCommandLine.addValue(writeableCommandLine.getOption("c"), "blue");
 writeableCommandLine.addSwitch(writeableCommandLine.getOption("d"), true);

 CommandLine parsedCommandLine = null;
 try {
 parsedCommandLine = writeableCommandLine.parse();
} catch (ParseException e) {
 fail("Failed to parse command line: " + e.getMessage());
}

 assertTrue(writeableCommandLine.hasOption(writeableCommandLine.getOption("a")));
 assertEquals("red", writeableCommandLine.getValues(writeableCommandLine.getOption("a"), null).get(0));
 assertTrue(writeableCommandLine.hasOption(writeableCommandLine.getOption("c")));
 assertFalse(writeableCommandLine.getValues(writeableCommandLine.getOption("c"), null).isEmpty());
 assertTrue(writeableCommandLine.getSwitch(writeableCommandLine.getOption("d"), false));

 assertEquals("--apple=red --color hue=blue --debug", writeableCommandLine.toString());
 }

 @Test
 public void testMissingOption() {
 WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(
 new Option("a", "apple", true, "An apple")
 );

 try {
 writeableCommandLine.parse();
 fail("Expected ParseException for missing option");
 } catch (ParseException e) {
 // pass
 }

 assertTrue(writeableCommandLine.hasOption(writeableCommandLine.getOption("a")));
 assertNull(writeableCommandLine.getValues(writeableCommandLine.getOption("a"), null));
 }

 @Test
 public void testMissingValue() {
 WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(
 new Option("a", "apple", true, "An apple")
 );
 writeableCommandLine.addOption(writeableCommandLine.getOption("a"));

 try {
 writeableCommandLine.parse();
 fail("Expected ParseException for missing value");
 } catch (ParseException e) {
 // pass
 }

 assertTrue(writeableCommandLine.hasOption(writeableCommandLine.getOption("a")));
 assertNull(writeableCommandLine.getValues(writeableCommandLine.getOption("a"), null));
 }

 @Test
 public void testInvalidValueType() {
 WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(
 new Option("a", "apple", 1, "An apple")
 );
 writeableCommandLine.addOption(writeableCommandLine.getOption("a"));
 writeableCommandLine.addValue(writeableCommandLine.getOption("a"), 5);

 try {
 writeableCommandLine.parse();
 fail("Expected ParseException for invalid value type");
 } catch (ParseException e) {
 // pass
 }

 assertTrue(writeableCommandLine.hasOption(writeableCommandLine.getOption("a")));
 assertNull(writeableCommandLine.getValues(writeableCommandLine.getOption("a"), null));
 }

 @Test
 public void testInvalidSwitchValue() {
 WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(
 new Option("a", "apple", false)
 );
 writeableCommandLine.addOption(writeableCommandLine.getOption("a"));
 writeableCommandLine.addSwitch(writeableCommandLine.getOption("a"), true);

  try {
    writeableCommandLine.parse();
    fail("Expected ParseException for invalid switch value");
  } catch (ParseException e) {
    // pass
  }

  assertTrue(writeableCommandLine.hasOption(writeableCommandLine.getOption("a")));
  assertFalse(writeableCommandLine.getSwitch(writeableCommandLine.getOption("a"), false));
}

 @Test
 public void testMultipleValues() {
 WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(
 new Option("a", "apple", true, "An apple")
 );
 writeableCommandLine.addOption(writeableCommandLine.getOption("a"));
 writeableCommandLine.addValue(writeableCommandLine.getOption("a"), "red");
 writeableCommandLine.addValue(writeableCommandLine.getOption("a"), "green");

 CommandLine parsedCommandLine = null;
 try {
 parsedCommandLine = writeableCommandLine.parse();
} catch (ParseException e) {
 fail("Failed to parse command line: " + e.getMessage());
}

 assertTrue(writeableCommandLine.hasOption(writeableCommandLine.getOption("a")));
 assertFalse(writeableCommandLine.getValues(writeableCommandLine.getOption("a"), null).isEmpty());
 List values = writeableCommandLine.getValues(writeableCommandLine.getOption("a"), null);
 assertEquals(2, values.size());
 assertEquals("red", values.get(0));
 assertEquals("green", values.get(1));
 }

 @Test
 public void testParseExceptionWithMessage() {
 WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(
 new Option("a", "apple", true, "An apple")
 );
 writeableCommandLine.addOption(writeableCommandLine.getOption("a"));
 writeableCommandLine.addValue(writeableCommandLine.getOption("a"), "5");

 try {
 writeableCommandLine.parse();
 fail("Expected ParseException with message: Invalid value for option 'a'.");
 } catch (ParseException e) {
 String message = e.getMessage();
 assertTrue(message.startsWith("Invalid value for option 'a'."));
 }
 }
}
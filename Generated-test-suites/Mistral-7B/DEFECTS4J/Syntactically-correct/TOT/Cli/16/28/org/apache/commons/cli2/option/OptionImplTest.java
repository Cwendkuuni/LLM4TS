package org.apache.commons.cli2.option;

import org.apache.commons.cli2.CommandLineParser;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.ParseException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionImplTest {

 private Option option;
 private CommandLineParser parser;
 private HelpFormatter formatter;

 @Before
 public void setup() {
 option = new OptionBuilder()
 .withLongOpt("example")
 .withDescription("Example option")
 .withRequiredArg()
 .create();

 parser = new DefaultParser();
 formatter = new HelpFormatter();
 }

 @Test
 public void testCanProcess() {
 WriteableCommandLine commandLine = new DefaultCommandLine();
 ListIterator<String> arguments = commandLine.getArgList().listIterator();

 // Test valid argument
 arguments.add("exampleValue");
 assertTrue(option.canProcess(commandLine, arguments));

 // Test invalid argument
 arguments.add("invalidArgument");
 assertFalse(option.canProcess(commandLine, arguments));

 // Test empty argument list
 arguments.clear();
 assertFalse(option.canProcess(commandLine, arguments));
 }

 @Test
 public void testFindOption() {
 WriteableCommandLine commandLine = new DefaultCommandLine();
 commandLine.addOption(option);

 Option foundOption = option.findOption("example");
 assertEquals(option, foundOption);
 }

 @Test(expected = ParseException.class)
 public void testFindOptionInvalid() {
 WriteableCommandLine commandLine = new DefaultCommandLine();
 commandLine.addOption(option);

 Option invalidOption = option.findOption("invalid");
 fail("Expected ParseException to be thrown");
 }

 @Test
 public void testToString() {
 String expected = "example:exampleValue";
 assertEquals(expected, option.toString());
 }

 @Test
 public void testEquals() {
 Option identicalOption = new OptionBuilder()
 .withLongOpt("example")
 .withDescription("Example option")
 .withRequiredArg()
 .create();

 assertTrue(option.equals(identicalOption));

 Option differentOption = new OptionBuilder()
 .withLongOpt("different")
 .withDescription("Different option")
 .withRequiredArg()
 .create();

 assertFalse(option.equals(differentOption));
 }

 @Test
 public void testHashCode() {
 Option identicalOption = new OptionBuilder()
 .withLongOpt("example")
 .withDescription("Example option")
 .withRequiredArg()
 .create();

 int expected = option.hashCode();
 int identicalHashCode = identicalOption.hashCode();

 assertEquals(expected, identicalHashCode);

 Option differentOption = new OptionBuilder()
 .withLongOpt("different")
 .withDescription("Different option")
 .withRequiredArg()
 .create();

 int differentHashCode = differentOption.hashCode();

 assertNotEquals(expected, differentHashCode);
 }

 @Test
 public void testId() {
 int expectedId = option.getId();
 int identicalId = new OptionBuilder()
 .withLongOpt("example")
 .withDescription("Example option")
 .withRequiredArg()
 .create().getId();

 assertEquals(expectedId, identicalId);
 }

 @Test
 public void testDefaults() {
 WriteableCommandLine commandLine = new DefaultCommandLine();
 option.defaults(commandLine);
 assertNull(commandLine.getOptionValue("example"));
 }

 @Test
 public void testCheckPrefixes() {
 Set prefixes = java.util.EnumSet.allOf(Option.Prefix.class);
 option.checkPrefixes(prefixes);
 }
}
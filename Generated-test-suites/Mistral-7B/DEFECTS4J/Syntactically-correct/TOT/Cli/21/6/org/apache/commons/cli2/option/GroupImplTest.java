package org.apache.commons.cli2.option;

import org.apache.commons.cli2.Group;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class GroupImplTest {

 private Group group;
 private WriteableCommandLine commandLine;

 @Before
 public void setUp() {
 // Given a Group with some options and arguments
 final List options = new ArrayList<>();
 options.add(new OptionBuilder("o")
 .required(false)
 .longOpt("option")
 .hasArg(true)
 .desc("an option")
 .build());
 options.add(new OptionBuilder("a")
 .required(false)
 .shortOpt("a")
 .longOpt("argument")
 .hasArg(true)
 .desc("an argument")
 .build());
 options.add(new OptionBuilder("g")
 .required(false)
 .longOpt("group")
 .hasArg(0)
 .desc("a group")
 .build());
 group = new GroupImpl(options, "MyGroup", null, 0, Integer.MAX_VALUE, false);
 commandLine = new DefaultCommandLine();
 }

 /**
 * Test that the Group can be processed correctly with valid arguments
 */
 @Test
 public void testValidArguments() throws OptionException {
 // When the Group's process method is invoked with valid arguments
 group.process(commandLine, commandLine.parser().commandLineParser(commandLine.getArgs()));

 // Then all options and arguments are set correctly in the command line
 assertEquals("o", commandLine.getOptionValue("o"));
 assertEquals("value1", commandLine.getOptionValue("a"));
 assertEquals(null, commandLine.getOptionValue("g"));
 }

 /**
 * Test that the Group can be processed correctly with missing arguments
 */
 @Test(expected = OptionException.class)
 public void testMissingArguments() throws OptionException {
 // Given a command line with missing arguments
 commandLine.addOption(group.getLongOpt());
 commandLine.addOption("g");

 // When the Group's process method is invoked
 group.process(commandLine, commandLine.parser().commandLineParser(commandLine.getArgs()));

 // Then an OptionException is thrown due to missing arguments
 fail("An OptionException should have been thrown");
 }

 /**
 * Test that the Group can be processed correctly with too many arguments
 */
 @Test(expected = OptionException.class)
 public void testTooManyArguments() throws OptionException {
 // Given a command line with too many arguments
 commandLine.addOption(group.getLongOpt());
 commandLine.addOption("g");
 commandLine.addOption("o");
 commandLine.addOption("a");
 commandLine.addOption("a");

 // When the Group's process method is invoked
 group.process(commandLine, commandLine.parser().commandLineParser(commandLine.getArgs()));

 // Then an OptionException is thrown due to too many arguments
 fail("An OptionException should have been thrown");
 }

 /**
 * Test that the Group can be processed correctly with invalid arguments
 */
 @Test
 public void testInvalidArguments() throws OptionException {
 // Given a command line with invalid arguments
 commandLine.addOption(group.getLongOpt());
 commandLine.addOption("o"); // no value
 commandLine.addOption("a"); // no value
 commandLine.addOption("g"); // value is not null

 // When the Group's process method is invoked
 group.process(commandLine, commandLine.parser().commandLineParser(commandLine.getArgs()));

 // Then no options or arguments are set in the command line
 assertNull(commandLine.getOptionValue("o"));
 assertNull(commandLine.getOptionValue("a"));
 assertNull(commandLine.getOptionValue("g"));
 }

 /**
 * Test that the Group can be validated correctly
 */
 @Test(expected = OptionException.class)
 public void testInvalidValidation() throws OptionException {
 // Given a command line that contains an error option
 commandLine.addOption(group.getLongOpt());
 commandLine.addOption("o"); // no value
 commandLine.addOption("a"); // no value
 commandLine.addOption("g"); // value is not null
 commandLine.validate();

 // When the Group's validate method is invoked
 group.validate(commandLine);

 // Then an OptionException is thrown due to an error option
 fail("An OptionException should have been thrown");
 }

 /**
 * Test that the Group can be validated correctly with valid options
 */
 @Test
 public void testValidValidation() throws OptionException {
 // Given a command line with valid options
 commandLine.addOption(group.getLongOpt());
 commandLine.addOption("o", "value1");
 commandLine.addOption("a", "value2");
 commandLine.validate();

 // Then all options are validated correctly
 assertTrue("Option 'o' is valid", commandLine.hasOption("o"));
 assertEquals("value1", commandLine.getOptionValue("o"));
 assertTrue("Option 'a' is valid", commandLine.hasOption("a"));
 assertEquals("value2", commandLine.getOptionValue("a"));
 }

 /**
 * Test that the Group's appendUsage method writes the correct usage information
 */
 @Test
 public void testAppendUsage() {
 final StringBuilder sb = new StringBuilder();
 group.appendUsage(sb, null, null);
 final String expectedUsage = "| MyGroup (option OPTIONAL --option VALUE, argument OPTIONAL --argument VALUE)\n";
 assertEquals(expectedUsage, sb.toString());
 }

 /**
 * Test that the Group's helpLines method writes the correct usage information
 */
 @Test
 public void testHelpLines() {
 final List<String> helpLines = group.helpLines(0, null, null);
 final StringBuilder expectedHelpLinesBuilder = new StringBuilder();
 expectedHelpLinesBuilder.append("MyGroup:\n")
 .append("  (option OPTIONAL --option VALUE)\n")
 .append("  (argument OPTIONAL --argument VALUE)\n");
 final String expectedHelpLines = expectedHelpLinesBuilder.toString();
 final List<String> helpLinesList = new ArrayList<>(helpLines);
 helpLinesList.remove(0); // remove the group name line
 assertEquals(expectedHelpLines, String.join("\n", helpLinesList));
 }

}
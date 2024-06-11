package org.apache.commons.cli2.option;

import static org.junit.Assert.*;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.DisplaySetting;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.util.Arguments;
import org.junit.Before;
import org.junit.Test;

public class GroupImplTest {

 private List<Option> options;
 private GroupImpl group;

 @Before
 public void setUp() {
 options = new ArrayList<>();
 options.add(new Option("a", "arg1", true, "test argument 1")
 .setValueRequired(true)
 .setArgName("arg1"));
 options.add(new Option("b", "arg2", false, "test argument 2"));
 options.add(new Option("c", "arg3", false, "test argument 3"));
 group = new GroupImpl(options, "group", "group description", 1, 3);
 }

 @Test
 public void testCanProcess() {
 final WriteableCommandLine commandLine = new WriteableCommandLine("test");
 final List<String> arguments = commandLine.getArguments();

 // basic usage test
 arguments.add("-a");
 arguments.add("arg1Value");
 assertTrue(group.canProcess(commandLine, "a"));
 assertEquals("arg1Value", group.getValue("arg1"));

 // edge case: minimum number of options
 arguments.clear();
 arguments.add("-a");
 arguments.add("arg1Value");
 arguments.add("arg2Value");
 assertTrue(group.canProcess(commandLine, "a"));
 assertEquals("arg1Value", group.getValue("arg1"));
 assertEquals("arg2Value", group.getValue("arg2"));

 // edge case: maximum number of options
 arguments.clear();
 arguments.add("-a");
 arguments.add("arg1Value");
 arguments.add("arg2Value");
 arguments.add("arg3Value");
 assertTrue(group.canProcess(commandLine, "a"));
 assertEquals("arg1Value", group.getValue("arg1"));
 assertEquals("arg2Value", group.getValue("arg2"));
 assertEquals("arg3Value", group.getValue("arg3"));

 // error case: invalid option
 arguments.clear();
 arguments.add("-x");
 assertFalse(group.canProcess(commandLine, "x"));

 // error case: missing argument
 arguments.clear();
 arguments.add("-a");
 assertEquals(1, group.getMinimum());
 assertFalse(group.canProcess(commandLine, "a"));
 arguments.add("arg1Value");
 assertTrue(group.canProcess(commandLine, "a"));
 assertEquals("arg1Value", group.getValue("arg1"));
 }

 @Test(expected = OptionException.class)
 public void testValidate() {
 final WriteableCommandLine commandLine = new WriteableCommandLine("test");
 final List<String> arguments = commandLine.getArguments();

 // error case: too few options
 arguments.clear();
 arguments.add("-a");
 assertFalse(group.canProcess(commandLine, "a"));
 group.validate(commandLine);

 // error case: too many options
 arguments.clear();
 arguments.add("-a");
 arguments.add("arg1Value");
 arguments.add("arg2Value");
 arguments.add("arg3Value");
 group.validate(commandLine);
 }

 @Test
 public void testProcess() {
 final WriteableCommandLine commandLine = new WriteableCommandLine("test");
 final List<String> arguments = commandLine.getArguments();

 // basic usage test
 arguments.clear();
 arguments.add("-a");
 arguments.add("arg1Value");
 group.process(commandLine, arguments.listIterator());
 assertEquals("arg1Value", group.getValue("arg1"));

 // edge case: minimum number of options
 arguments.clear();
 arguments.add("-a");
 arguments.add("arg1Value");
 arguments.add("arg2Value");
 group.process(commandLine, arguments.listIterator());
 assertEquals("arg1Value", group.getValue("arg1"));
 assertEquals("arg2Value", group.getValue("arg2"));

 // edge case: maximum number of options
 arguments.clear();
 arguments.add("-a");
 arguments.add("arg1Value");
 arguments.add("arg2Value");
 arguments.add("arg3Value");
 group.process(commandLine, arguments.listIterator());
 assertEquals("arg1Value", group.getValue("arg1"));
 assertEquals("arg2Value", group.getValue("arg2"));
 assertEquals("arg3Value", group.getValue("arg3"));

 // error case: missing argument
 arguments.clear();
 arguments.add("-a");
 group.process(commandLine, arguments.listIterator());
 assertNull(group.getValue("arg1"));
 }

 @Test
 public void testAppendUsage() {
 final StringBuffer buffer = new StringBuffer();
 group.appendUsage(buffer, Collections.EMPTY_SET, null);
 String expected = "group description:\n" +
 "group (arg1)\n" +
 "  arg1: test argument 1 (required)\n";
 assertEquals(expected, buffer.toString());
 }

 @Test
 public void testHelpLines() {
 final Set helpSettings = new HashSet<>();
 helpSettings.add(DisplaySetting.DISPLAY_GROUP_NAME);
 helpSettings.add(DisplaySetting.DISPLAY_GROUP_EXPANDED);
 helpSettings.add(DisplaySetting.DISPLAY_GROUP_ARGUMENT);
 final List<HelpLine> helpLines = group.helpLines(0, helpSettings, null);
 assertEquals(3, helpLines.size());
 }

}
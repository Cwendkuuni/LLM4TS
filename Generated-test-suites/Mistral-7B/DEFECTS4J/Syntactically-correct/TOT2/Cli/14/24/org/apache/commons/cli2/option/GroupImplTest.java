package org.apache.commons.cli2.option;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.ParseException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GroupImplTest {

 private List<Option> options;
 private Group group;

 @Before
 public void setUp() {
 options = new ArrayList<Option>();
 options.add(OptionBuilder.withLongOpt("longOption1")
 .withDescription("description for longOption1")
 .withValueSeparators(new String[]{"="})
 .create());
 options.add(OptionBuilder.withLongOpt("longOption2")
 .withDescription("description for longOption2")
 .withValueSeparators(new String[]{"="})
 .create());

 group = new GroupImpl(options, "testGroup", "test description", 1, 2);
 }

 @Test
 public void testCanProcessWithValidOption() {
 CommandLine commandLine = new CommandLine();
 commandLine.parseArgument(new String[]{"--longOption1=value1"});
 assertTrue(group.canProcess(commandLine, "--longOption1"));
 }

 @Test(expected = OptionException.class)
 public void testCanProcessWithInvalidOption() {
 CommandLine commandLine = new CommandLine();
 commandLine.parseArgument(new String[]{"--invalidOption"});
 assertFalse(group.canProcess(commandLine, "--invalidOption"));
 }

 @Test
 public void testCanProcessWithBurstedOption() {
 CommandLine commandLine = new CommandLine();
 commandLine.parseArgument(new String[]{"--longOption1=value1", "--longOption1=value2"});
 assertTrue(group.canProcess(commandLine, "--longOption1"));
 }

 @Test(expected = OptionException.class)
 public void testCanProcessWithInvalidBurstedOption() {
 CommandLine commandLine = new CommandLine();
 commandLine.parseArgument(new String[]{"--invalidOption=value1", "--invalidOption=value2"});
 assertFalse(group.canProcess(commandLine, "--invalidOption"));
 }

 @Test
 public void testValidateWithValidOptions() throws OptionException {
 CommandLine commandLine = new CommandLine();
 commandLine.parseArgument(options.toArray(new Option[0]));
 group.validate(commandLine);
 }

 @Test(expected = OptionException.class)
 public void testValidateWithTooFewOptions() throws OptionException {
 CommandLine commandLine = new CommandLine();
 commandLine.parseArgument(new String[0]);
 group.validate(commandLine);
 }

 @Test(expected = OptionException.class)
 public void testValidateWithTooManyOptions() throws OptionException {
 CommandLine commandLine = new CommandLine();
 commandLine.parseArgument(new String[]{
 "option1", "option1", "option1", "option1", "option1"});
 group.validate(commandLine);
 }

 @Test
 public void testProcessWithValidArguments() throws ParseException {
 CommandLine commandLine = new CommandLine();
 commandLine.parseArgument(new String[]{"--longOption1=value1", "--longOption2=value2"});
 List<String> arguments = Arrays.asList(new String[]{"--test", "-t"});
 group.process(commandLine, arguments.iterator());
 assertEquals("value1", commandLine.getOptionValue("longOption1"));
 assertEquals("value2", commandLine.getOptionValue("longOption2"));
 }

 @Test
 public void testProcessWithInvalidArgument() throws ParseException {
 CommandLine commandLine = new CommandLine();
 commandLine.parseArgument(new String[]{"--longOption1=value1", "--longOption2=value2"});
 List<String> arguments = Arrays.asList(new String[]{"--invalidOption=value1"});
 group.process(commandLine, arguments.iterator());
 assertFalse(commandLine.hasOption("invalidOption"));
 }

 @Test
 public void testHelp() {
 HelpFormatter formatter = new HelpFormatter();
 String help = formatter.printHelp("test", "", group, false, false, false);
 assertTrue(help.contains("test description"));
 assertTrue(help.contains("longOption1"));
 assertTrue(help.contains("description for longOption1"));
 assertTrue(help.contains("longOption2"));
 assertTrue(help.contains("description for longOption2"));
 }
}
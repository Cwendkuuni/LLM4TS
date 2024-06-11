package org.apache.commons.cli2.option;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArgumentImplTest {

 private Argument arg;
 private WriteableCommandLine commandLine;

 @Before
 public void setUp() {
 arg = new ArgumentImpl("argName", "description", 1, 1, '.', ' ', null, "--remaining", null, 0);
 commandLine = new WriteableCommandLine();
 }

 @Test
 public void testProcessValues() {
 // Test processing valid arguments
 arg.process(commandLine, Arrays.asList("--argName arg1", "--argName arg2").iterator());
 assertEquals(2, commandLine.getValues(arg).size());
 assertEquals("arg1", commandLine.getValues(arg).get(0));
 assertEquals("arg2", commandLine.getValues(arg).get(1));

 // Test processing multiple values with separator
 arg = new ArgumentImpl("argName", "description", 1, 3, '.', ' ', null, "--remaining", null, 0);
 commandLine.clear();
 arg.process(commandLine, Arrays.asList("--argName arg1 arg2 arg3").iterator());
 assertEquals(3, commandLine.getValues(arg).size());
 assertEquals("arg1", commandLine.getValues(arg).get(0));
 assertEquals("arg2", commandLine.getValues(arg).get(1));
 assertEquals("arg3", commandLine.getValues(arg).get(2));

 // Test consuming remaining arguments
 arg = new ArgumentImpl("argName", "description", 1, 1, '.', ' ', null, "--remaining", null, 0);
 commandLine.clear();
 arg.process(commandLine, Arrays.asList("--argName arg1", "--argName arg2", "--remaining arg3 arg4 arg5").iterator());
 assertEquals(2, commandLine.getValues(arg).size());
 assertEquals("arg1", commandLine.getValues(arg).get(0));
 assertEquals("arg2", commandLine.getValues(arg).get(1));
 assertEquals(3, commandLine.getRemainingOptions().size());
 assertEquals("arg3", commandLine.getRemainingOptions().get(0));
 assertEquals("arg4", commandLine.getRemainingOptions().get(1));
 assertEquals("arg5", commandLine.getRemainingOptions().get(2));
 }

 @Test
 public void testCanProcess() {
 assertTrue(arg.canProcess(commandLine, "arg1"));
 assertTrue(arg.canProcess(commandLine, "arg1 arg2"));
 }

 @Test(expected = OptionException.class)
 public void testCannotProcess() {
 arg = new ArgumentImpl("argName", "description", 1, 1, '.', ' ', null, "--remaining", null, 0);
 commandLine.clear();
 arg.process(commandLine, Arrays.asList("--argName arg1 arg2 arg3 arg4").iterator());
 arg.process(commandLine, Arrays.asList("--argName arg1 arg2").iterator());
 assertEquals(2, commandLine.getValues(arg).size());
 assertEquals("arg1", commandLine.getValues(arg).get(0));
 assertEquals("arg2", commandLine.getValues(arg).get(1));
 argument(commandLine, "--argName", "arg3");
 }

 @Test(expected = OptionException.class)
 public void testCannotProcessSeparatedValues() {
 arg = new ArgumentImpl("argName", "description", 1, 3, '.', ' ', null, "--remaining", null, 0);
 commandLine.clear();
 arg.process(commandLine, Arrays.asList("--argName arg1 arg2 arg3 arg4").iterator());
 argument(commandLine, "--argName", "arg5 arg6");
 }

 @Test(expected = OptionException.class)
 public void testConsumeRemainingWithOption() {
 arg = new ArgumentImpl("argName", "description", 1, 1, '.', ' ', null, "--remaining", null, 0);
 commandLine.clear();
 arg.process(commandLine, Arrays.asList("--argName arg1", "--consume arg2 arg3 arg4 arg5 arg6 arg7").iterator());
 assertEquals(1, commandLine.getValues(arg).size());
 assertEquals("arg1", commandLine.getValues(arg).get(0));
 assertEquals(6, commandLine.getRemainingOptions().size());
 }

 @Test
 public void testValidate() {
 arg = new ArgumentImpl("argName", "description", 1, 1, '.', ' ', null, "--remaining", null, 0);
 commandLine.clear();
 arg.process(commandLine, Arrays.asList("--argName arg1").iterator());
 try {
 arg.validate(commandLine);
 } catch (OptionException e) {
 fail("Argument validation failed: " + e.getMessage());
 }

 arg = new ArgumentImpl("argName", "description", 1, 1, '.', ' ', null, "--remaining", null, 0);
 commandLine.clear();
 arg.process(commandLine, Arrays.asList("--argName arg1 arg2").iterator());
 try {
 arg.validate(commandLine);
 } catch (OptionException e) {
 fail("Argument validation failed: " + e.getMessage());
 }

 arg = new ArgumentImpl("argName", "description", 1, 1, '.', ' ', null, "--remaining", null, 0);
 commandLine.clear();
 arg.process(commandLine, Arrays.asList("--argName arg1 arg2 arg3").iterator());
 try {
 arg.validate(commandLine);
 } catch (OptionException e) {
 fail("Argument validation failed: " + e.getMessage());
 }

 arg = new ArgumentImpl("argName", "description", 1, 1, '.', ' ', null, "--remaining", null, 0);
 commandLine.clear();
 arg.process(commandLine, Arrays.asList("--argName arg1 arg2 arg3 arg4 arg5").iterator());
 try {
 arg.validate(commandLine);
 fail("Argument validation failed: expected OptionException");
 } catch (OptionException e) {
 // expected
 }
 }

 @Test(expected = OptionException.class)
 public void testInvalidValidator() {
 arg = new ArgumentImpl("argName", "description", 1, 1, '.', ' ', new InvalidValidator(), "--remaining", null, 0);
 commandLine.clear();
 arg.process(commandLine, Arrays.asList("--argName arg1").iterator());
 arg.validate(commandLine);
 }

 private void argument(WriteableCommandLine commandLine, String argName, String argValue) {
 commandLine.parseArgument(new Option(argName, argName, true, null), argValue);
 }

 @Test
 public void testToString() {
 assertEquals("Argument{name='argName', description='description', minimum=1, maximum=1, initialSeparator=., subsequentSeparator=, subsequentSplit=false, validator=null, consumeRemaining=--remaining, defaultValues=null}", arg.toString());
 }
}
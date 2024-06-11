import org.apache.commons.cli2.*;
import org.apache.commons.cli2.option.ArgumentImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArgumentImplTest {

 private WriteableCommandLine commandLine;

 @Before
 public void setUp() {
 commandLine = new DefaultWriterCommandLine();
 }

 @Test
 public void testArgumentImpl() {
 // Test with default values
 ArgumentImpl argument1 = new ArgumentImpl("arg1", "Description for arg1", 1, 1, ' ', ' ', null, "--", null, 0);
 ArgumentImpl argument2 = new ArgumentImpl("arg2", "Description for arg2", 1, 2, ' ', ' ', null, "--", null, 0);

 // Test getters
 assertEquals("arg1", argument1.getPreferredName());
 assertEquals("Description for arg1", argument1.getDescription());
 assertEquals(1, argument1.getMinimum());
 assertEquals(1, argument1.getMaximum());
 assertEquals(' ', argument1.getInitialSeparator());
 assertEquals(' ', argument1.getSubsequentSeparator());
 assertFalse(argument1.getSubsequentSplit());
 assertNull(argument1.getValidator());
 assertNull(argument1.getConsumeRemaining());
 assertNull(argument1.getDefaultValues());

 // Test with custom value and validator
 ArgumentImpl argument3 = new ArgumentImpl("arg3", "Description for arg3", 1, 1, ' ', ' ', new MyValidator(), "--", Arrays.asList("value1", "value2"), 0);
 assertEquals("arg3", argument3.getPreferredName());
 assertEquals("Description for arg3", argument3.getDescription());
 assertEquals(1, argument3.getMinimum());
 assertEquals(1, argument3.getMaximum());
 assertEquals(' ', argument3.getInitialSeparator());
 assertEquals(' ', argument3.getSubsequentSeparator());
 assertFalse(argument3.getSubsequentSplit());
 assertNotNull(argument3.getValidator());
 assertEquals("value1", argument3.getDefaultValues().get(0));
 assertEquals("value2", argument3.getDefaultValues().get(1));

 // Test processValues
 List<String> args = Arrays.asList("arg1", "value1", "value2", "--arg3", "value3", "value4");
 argument1.processValues(commandLine, args.listIterator(), argument1);
 assertEquals(2, commandLine.getValues(argument1).size());
 assertEquals("value1", commandLine.getValues(argument1).get(0));
 assertEquals("value2", commandLine.getValues(argument1).get(1));

 argument3.processValues(commandLine, args.listIterator(), argument3);
 assertEquals(4, commandLine.getValues(argument3).size());
 assertEquals("value3", commandLine.getValues(argument3).get(2));
 assertEquals("value4", commandLine.getValues(argument3).get(3));

 // Test canProcess
 assertTrue(argument1.canProcess(commandLine, "value1"));
 assertTrue(argument1.canProcess(commandLine, "arg1 value1"));

 assertTrue(argument3.canProcess(commandLine, "value3"));
 assertTrue(argument3.canProcess(commandLine, "arg3 value3 value4"));

 // Test validate
 try {
 argument3.validate(commandLine);
 } catch (OptionException e) {
 fail("OptionException should not be thrown");
 }

 ArgumentImpl argument4 = new ArgumentImpl("arg4", "Description for arg4", 1, 1, ' ', ' ', null, "--", null, 0);
 List<String> invalidArgs = Arrays.asList("arg4", "--arg4", "arg4 value1 value2 value3");

 for (String arg : invalidArgs) {
 try {
 argument4.validate(commandLine);
 fail("OptionException should be thrown");
 } catch (OptionException e) {
 // expected
 }
 }

 // Test process
 argument1.process(commandLine, args.listIterator());
 assertEquals(2, commandLine.getValues(argument1).size());
 assertEquals("value1", commandLine.getValues(argument1).get(0));
 assertEquals("value2", commandLine.getValues(argument1).get(1));

 argument3.process(commandLine, args.listIterator());
 assertEquals(4, commandLine.getValues(argument3).size());
 assertEquals("value3", commandLine.getValues(argument3).get(2));
 assertEquals("value4", commandLine.getValues(argument3).get(3));

 // Test getters for consumed remaining
 ArgumentImpl argument5 = new ArgumentImpl("arg5", "Description for arg5", 1, 1, ' ', ' ', null, "--", null, 0);
 ArgumentImpl argument6 = new ArgumentImpl("arg6", "Description for arg6", 1, 1, ' ', ' ', null, "--consumeRemaining--", null, 0);

 assertEquals(null, argument5.getConsumeRemaining());
 assertEquals("--consumeRemaining--", argument6.getConsumeRemaining());

 // Test appendUsage
 StringBuilder usage = new StringBuilder();
 argument1.appendUsage(usage, null, null);
 assertFalse(usage.toString().contains("["));
 argument2.appendUsage(usage, null, null);
 assertFalse(usage.toString().contains("["));
 argument3.appendUsage(usage, null, null);
 assertFalse(usage.toString().contains("["));
 argument5.appendUsage(usage, null, null);
 assertFalse(usage.toString().contains("["));
 argument6.appendUsage(usage, null, null);
 assertTrue(usage.toString().contains("--consumeRemaining--"));
 }

 @After
 public void tearDown() {
 commandLine = null;
 }

 private static class MyValidator implements Validator {
 @Override
 public void validate(List<String> values) throws InvalidArgumentException {
 // validation logic
 if (values.size() != 2) {
 throw new InvalidArgumentException("Invalid number of values");
 }
 }
 }
}
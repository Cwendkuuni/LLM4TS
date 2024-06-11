import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.OptionFactory;
import org.apache.commons.cli2.TestUtils;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

public class ArgumentImplTest {

 private CommandLine cmd;

 @Before
 public void setUp() {
 cmd = new CommandLine();
 }

 @Test
 public void testArgumentWithNoDefaults() {
 Argument arg = new ArgumentImpl("arg", "arg description", 1, 1, ArgumentImpl.DEFAULT_INITIAL_SEPARATOR,
 ArgumentImpl.DEFAULT_SUBSEQUENT_SEPARATOR, null, null);

 arg.process(cmd, Arrays.asList("--arg", "value").iterator());
 assertEquals("value", cmd.getValue(arg));
 }

 @Test
 public void testArgumentWithDefaults() {
 Argument arg = new ArgumentImpl("arg", "arg description", 1, 1, ArgumentImpl.DEFAULT_INITIAL_SEPARATOR,
 ArgumentImpl.DEFAULT_SUBSEQUENT_SEPARATOR, null, Arrays.asList("defaultValue"), 0);

 arg.defaults(cmd);
 assertEquals("defaultValue", cmd.getValue(arg));
 }

 @Test
 public void testArgumentWithMinimumAndMaximum() {
 Argument arg = new ArgumentImpl("arg", "arg description", 2, 3, ArgumentImpl.DEFAULT_INITIAL_SEPARATOR,
 ArgumentImpl.DEFAULT_SUBSEQUENT_SEPARATOR, null, null);

 try {
 arg.process(cmd, Arrays.asList("--arg", "value1").iterator());
 fail("Should have thrown an exception as minimum value is 2.");
 } catch (OptionException e) {
 // expected
 }

 try {
 arg.process(cmd, Arrays.asList("--arg", "value1", "value2").iterator());
 assertEquals("value1", cmd.getValue(arg, 0));
 assertEquals("value2", cmd.getValue(arg, 1));
 } catch (OptionException e) {
 fail("Should not have thrown an exception.");
 }

 try {
 arg.process(cmd, Arrays.asList("--arg", "value1", "value2", "value3").iterator());
 fail("Should have thrown an exception as maximum value is 3.");
 } catch (OptionException e) {
 // expected
 }
 }

 @Test
 public void testArgumentWithSubsequentSeparator() {
 Argument arg = new ArgumentImpl("arg", "arg description", 1, 1, ArgumentImpl.DEFAULT_INITIAL_SEPARATOR, ';',
 null, null);

 arg.process(cmd, Arrays.asList("--arg", "value1;value2").iterator());
 assertEquals(Arrays.asList("value1", "value2"), cmd.getValues(arg));
 }

 @Test
 public void testArgumentWithSubsequentSplit() {
 Argument arg = new ArgumentImpl("arg", "arg description", 1, 1, ArgumentImpl.DEFAULT_INITIAL_SEPARATOR, ',',
 ',', null);

 arg.process(cmd, Arrays.asList("--arg", "value1,value2").iterator());
 assertEquals(Arrays.asList("value1", "value2"), cmd.getValues(arg));
 }

 @Test
 public void testArgumentWithValidator() {
 Argument arg = new ArgumentImpl("arg", "arg description", 1, 1, ArgumentImpl.DEFAULT_INITIAL_SEPARATOR,
 ArgumentImpl.DEFAULT_SUBSEQUENT_SEPARATOR, new MyValidator(), null, null);

 try {
 arg.process(cmd, Arrays.asList("--arg", "foo").iterator());
 fail("Should have thrown an exception as validator failed.");
 } catch (OptionException e) {
 // expected
 }

 try {
 arg.process(cmd, Arrays.asList("--arg", "1").iterator());
 assertEquals("1", cmd.getValue(arg));
 } catch (OptionException e) {
 fail("Should not have thrown an exception.");
 }
 }

 @Test
 public void testArgumentWithConsumeRemaining() {
 Argument arg = new ArgumentImpl("arg", "arg description", 1, 1, ArgumentImpl.DEFAULT_INITIAL_SEPARATOR,
 ArgumentImpl.DEFAULT_SUBSEQUENT_SEPARATOR, null, null, null, OptionBuilder.withLongOpt(OptionFactory.LONG_OPTION_NO_ARG));

 arg.process(cmd, Arrays.asList("--arg", "--consume", "value1", "value2").iterator());
 assertEquals(Arrays.asList("value1", "value2"), cmd.getValues(arg));
 }

 @Test
 public void testArgumentWithHelp() {
 Argument arg = new ArgumentImpl("arg", "arg description", 1, 1, ArgumentImpl.DEFAULT_INITIAL_SEPARATOR,
 ArgumentImpl.DEFAULT_SUBSEQUENT_SEPARATOR, null, null, 0);
 Option option = OptionBuilder.withLongOpt("arg")
 .withDescription("arg description")
 .withArgument(arg)
 .create();

 HelpFormatter formatter = new HelpFormatter();
 formatter.printHelp("test", "Test program", arg.getHelpLines(0, Arrays.asList(option), null), true);
 }

 private static class MyValidator implements Validator {
 @Override
 public void validate(List values) throws InvalidArgumentException {
 if (!values.get(0).equals("1")) {
 throw new InvalidArgumentException("Invalid value: " + values.get(0));
 }
 }
 }
}
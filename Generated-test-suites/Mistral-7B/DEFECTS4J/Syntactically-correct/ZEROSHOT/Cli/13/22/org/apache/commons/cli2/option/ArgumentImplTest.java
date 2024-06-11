import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.ParseException;
import org.apache.commons.cli2.TestUtils;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class ArgumentImplTest {

 private List<String> args;
 private CommandLine commandLine;
 private ArgumentImpl argument;

 @Before
 public void setUp() {
 args = TestUtils.getArgs(new String[]{"--test", "--arg1", "arg2", "arg3"});
 commandLine = new CommandLine();
 argument = new ArgumentImpl("arg", "Test Argument", 1, Integer.MAX_VALUE, DEFAULT_INITIAL_SEPARATOR,
 DEFAULT_SUBSEQUENT_SEPARATOR, null, DEFAULT_CONSUME_REMAINING, null, 0);
 }

 @Test
 public void testMinimum() {
 assertThrows(OptionException.class, () -> argument.processValues(commandLine, args.listIterator(), argument));
 }

 @Test
 public void testValid() {
 argument.processValues(commandLine, args.listIterator(), argument);
 assertEquals(Arrays.asList("arg2", "arg3"), commandLine.getValues(argument));
 }

 @Test
 public void testMaximum() {
 args = TestUtils.getArgs(new String[]{"--test", "--arg1", "arg2", "arg3", "arg4"});
 argument.processValues(commandLine, args.listIterator(), argument);
 assertEquals(Arrays.asList("arg2", "arg3"), commandLine.getValues(argument));
 }

 @Test
 public void testConsumeRemaining() {
 args = TestUtils.getArgs(new String[]{"--test", "--arg1", "--", "arg2", "arg3"});
 argument = new ArgumentImpl("arg", "Test Argument", 1, Integer.MAX_VALUE, DEFAULT_INITIAL_SEPARATOR,
 DEFAULT_SUBSEQUENT_SEPARATOR, null, DEFAULT_CONSUME_REMAINING, null, 0);
 argument.processValues(commandLine, args.listIterator(), argument);
 assertEquals(Arrays.asList("arg2", "arg3"), commandLine.getValues(argument));
 }

 @Test
 public void testOptional() {
 argument = new ArgumentImpl("arg", "Test Argument", 0, Integer.MAX_VALUE, DEFAULT_INITIAL_SEPARATOR,
 DEFAULT_SUBSEQUENT_SEPARATOR, null, DEFAULT_CONSUME_REMAINING, null, 0);
 assertNull(commandLine.getValues(argument));
 }

 @Test
 public void testValidator() {
 argument = new ArgumentImpl("arg", "Test Argument", 1, 1, DEFAULT_INITIAL_SEPARATOR,
 DEFAULT_SUBSEQUENT_SEPARATOR, new MyValidator(), DEFAULT_CONSUME_REMAINING, null, 0);
 args = TestUtils.getArgs(new String[]{"--test", "--arg1", "arg"});
 assertThrows(InvalidArgumentException.class, () -> argument.processValues(commandLine, args.listIterator(), argument));
 }

 @Test
 public void testProcess() {
 argument.process(commandLine, args.listIterator());
 assertEquals(Arrays.asList("arg2", "arg3"), commandLine.getValues(argument));
 }

 @Test
 public void testInitialSeparator() {
 argument = new ArgumentImpl("arg", "Test Argument", 1, Integer.MAX_VALUE, '=',
 DEFAULT_SUBSEQUENT_SEPARATOR, null, DEFAULT_CONSUME_REMAINING, null, 0);
 args = TestUtils.getArgs(new String[]{"--test", "--arg=arg1", "arg2", "arg3"});
 argument.processValues(commandLine, args.listIterator(), argument);
 assertEquals(Arrays.asList("arg1", "arg2", "arg3"), commandLine.getValues(argument));
 }

 @Test
 public void testSubsequentSeparator() {
 argument = new ArgumentImpl("arg", "Test Argument", 1, Integer.MAX_VALUE,
 DEFAULT_INITIAL_SEPARATOR, ';', null, DEFAULT_CONSUME_REMAINING, null, 0);
 args = TestUtils.getArgs(new String[]{"--test", "--arg arg1;arg2", "arg3", "arg4"});
 argument.processValues(commandLine, args.listIterator(), argument);
 assertEquals(Arrays.asList("arg1", "arg2"), commandLine.getValues(argument));
 }

 @Test
 public void testSubsequentSplit() {
 argument = new ArgumentImpl("arg", "Test Argument", 1, Integer.MAX_VALUE,
 DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, null, DEFAULT_CONSUME_REMAINING, null, 0);
 args = TestUtils.getArgs(new String[]{"--test", "--arg arg1 arg2", "arg3", "arg4"});
 argument.processValues(commandLine, args.listIterator(), argument);
 assertEquals(Arrays.asList("arg1", "arg2"), commandLine.getValues(argument));
 }

 @Test
 public void testUsage() {
 final HelpFormatter formatter = new HelpFormatter();
 argument.appendUsage(new StringBuffer(), null, null);
 final String usage = formatter.printHelp("--test", argument.getHelpLines(0, null, null).get(0));
 assertEquals("  --arg [ARG]                        Test Argument\n" +
                 "                        (can be repeated)\n", usage);
 }

 @Test
 public void testDefaultValues() {
 argument = new ArgumentImpl("arg", "Test Argument", 0, Integer.MAX_VALUE,
 DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, null, DEFAULT_CONSUME_REMAINING,
 Arrays.asList("val1", "val2"), 0);
 argument.defaults(commandLine);
 assertEquals(Arrays.asList("val1", "val2"), commandLine.getValues(argument));
 }
}

 class MyValidator implements Validator {
 @Override
 public void validate(List<String> values) throws InvalidArgumentException {
 if (values.size() > 1) {
 throw new InvalidArgumentException("Exactly one value is required");
 }
 }
}
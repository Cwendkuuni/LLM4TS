import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.TestUtils;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArgumentImplTest {

 private CommandLine cmd;

 @Before
 public void setUp() {
 cmd = new CommandLine();
 }

 @Test
 public void testArgumentWithNoMinMax() {
 ArgumentImpl arg = new ArgumentImpl("arg", "test argument", 0, 0, DEFAULT_INITIAL_SEPARATOR,
 DEFAULT_SUBSEQUENT_SEPARATOR, null, DEFAULT_CONSUME_REMAINING, null, 0);
 arg.defaults(cmd);
 assertNull(cmd.getValues(arg));
 }

 @Test
 public void testArgumentWithMinMax() {
 ArgumentImpl arg = new ArgumentImpl("arg", "test argument", 2, 3, DEFAULT_INITIAL_SEPARATOR,
 DEFAULT_SUBSEQUENT_SEPARATOR, null, DEFAULT_CONSUME_REMAINING, Arrays.asList("a", "b"), 0);
 arg.defaults(cmd);
 assertEquals(Arrays.asList("a", "b"), cmd.getValues(arg));
 }

 @Test
 public void testArgumentValidation() {
 ArgumentImpl arg = new ArgumentImpl("arg", "test argument", 2, 3, DEFAULT_INITIAL_SEPARATOR,
 DEFAULT_SUBSEQUENT_SEPARATOR, null, DEFAULT_CONSUME_REMAINING, Arrays.asList("a", "b"), 0);
 arg.validate(cmd, arg);

 assertEquals(Arrays.asList("a", "b"), cmd.getValues(arg));

 try {
 arg.validate(cmd, arg);
 fail();
 } catch (OptionException e) {
 // expected
 }

 ArgumentImpl argWithMax = new ArgumentImpl("arg", "test argument", 2, 1, DEFAULT_INITIAL_SEPARATOR,
 DEFAULT_SUBSEQUENT_SEPARATOR, null, DEFAULT_CONSUME_REMAINING, Arrays.asList("a", "b"), 0);
 try {
 argWithMax.validate(cmd, argWithMax);
 fail();
 } catch (OptionException e) {
 // expected
 }

 ArgumentImpl argWithNoDefaults = new ArgumentImpl("arg", "test argument", 2, 3, DEFAULT_INITIAL_SEPARATOR,
 DEFAULT_SUBSEQUENT_SEPARATOR, null, DEFAULT_CONSUME_REMAINING, null, 0);
 try {
 argWithNoDefaults.validate(cmd, argWithNoDefaults);
 fail();
 } catch (OptionException e) {
 // expected
 }
}

 @Test
 public void testConsumingOption() {
 Option option = OptionBuilder.withLongOpt("option").withValueSeparator(DEFAULT_INITIAL_SEPARATOR)
 .withArgName("ARG")
 .create();
 Argument arg = new ArgumentImpl("arg", "test argument", 0, 1, DEFAULT_INITIAL_SEPARATOR,
 DEFAULT_SUBSEQUENT_SEPARATOR, null, DEFAULT_CONSUME_REMAINING, null, 0);
 arg.defaults(cmd);
 Option argOption = OptionBuilder.withLongOpt("argument")
 .withArg(arg)
 .create();
 cmd.parse(new String[]{"--option", "--argument", "arg1", "arg2"});
 assertEquals(Arrays.asList("arg1", "arg2"), cmd.getValues(arg));
 assertEquals(Arrays.asList("--option"), cmd.getValues(option));
}

 @Test
 public void testOptionWithSubsequentSplit() {
 Option option = OptionBuilder.withLongOpt("option")
 .withValueSeparator(DEFAULT_SUBSEQUENT_SEPARATOR)
 .withArgName("ARG")
 .create();
 Argument arg = new ArgumentImpl("arg", "test argument", 0, 1, DEFAULT_INITIAL_SEPARATOR,
 DEFAULT_SUBSEQUENT_SEPARATOR, null, DEFAULT_CONSUME_REMAINING, null, 0);
 arg.defaults(cmd);
 Option argOption = OptionBuilder.withLongOpt("argument")
 .withArg(arg)
 .create();
 cmd.parse(new String[]{"--option", "--argument", "arg1=val1,val2"});
 assertEquals(Arrays.asList("val1", "val2"), cmd.getValues(arg));
}

 @Test
 public void testOptionWithValidator() {
 Option option = OptionBuilder.withLongOpt("option")
 .create();
 Argument arg = new ArgumentImpl("arg", "test argument", 0, 1, DEFAULT_INITIAL_SEPARATOR,
 DEFAULT_SUBSEQUENT_SEPARATOR, new MyValidator(), DEFAULT_CONSUME_REMAINING, null, 0);
 arg.defaults(cmd);
 Option argOption = OptionBuilder.withLongOpt("argument")
 .withArg(arg)
 .create();
 try {
 cmd.parse(new String[]{"--option", "--argument", "arg1"});
 fail();
 } catch (OptionException e) {
 // expected
 }

 try {
 cmd.parse(new String[]{"--option", "--argument", "arg1=a,b"});
 fail();
 } catch (OptionException e) {
 // expected
 }

 try {
 cmd.parse(new String[]{"--option", "--argument", "arg1=a,c"});
 assertEquals(Arrays.asList("a", "c"), cmd.getValues(arg));
 } catch (OptionException e) {
 // expected
 }
}

 @Test
 public void testOptionUsage() {
 ArgumentImpl arg = new ArgumentImpl("arg", "test argument", 2, 3, DEFAULT_INITIAL_SEPARATOR,
 DEFAULT_SUBSEQUENT_SEPARATOR, null, DEFAULT_CONSUME_REMAINING, Arrays.asList("a", "b"), 0);
 Option option = OptionBuilder.withLongOpt("option")
 .withArg(arg)
 .create();
 HelpFormatter formatter = new HelpFormatter();
 cmd.parse(new String[0]);
 formatter.printHelp("Test", "", true, "Test program", cmd.getOptions(), 60, 60, true);
 String usage = TestUtils.getHelpMessage(System.out);
 assertTrue(usage.contains("ARG [ARG]..."));
}

 @Test
 public void testOptionName() {
 ArgumentImpl arg = new ArgumentImpl("arg", "test argument", 2, 3, DEFAULT_INITIAL_SEPARATOR,
 DEFAULT_SUBSEQUENT_SEPARATOR, null, DEFAULT_CONSUME_REMAINING, Arrays.asList("a", "b"), 0);
 Option option = OptionBuilder.withLongOpt("option")
 .withArg(arg)
 .create();
 assertEquals("argument", option.getLongOpt());
}
}

 class MyValidator implements Validator {

 @Override
 public void validate(List values) throws InvalidArgumentException {
 for (String value : values) {
 if (value.length() > 5) {
 throw new InvalidArgumentException("value is too long", "value", value);
 }
 }
 }
}
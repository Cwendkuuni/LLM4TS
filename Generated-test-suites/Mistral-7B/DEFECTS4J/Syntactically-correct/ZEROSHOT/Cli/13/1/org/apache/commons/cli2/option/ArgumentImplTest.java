import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.Validate;
import org.apache.commons.cli2.exceptions.OptionException;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArgumentImplTest {

 @Test
 public void testArgumentImpl() {
 ArgumentImpl argument = new ArgumentImpl(
 "arg",
 "A test argument",
 1,
 1,
 '.',
 ':',
 new ValidateIntRange(0, 10),
 "--extra",
 Collections.singletonList("defaultValue"),
 0);

 assertEquals("arg", argument.getName());
 assertEquals("A test argument", argument.getDescription());
 assertEquals(1, argument.getMinimum());
 assertEquals(1, argument.getMaximum());
 assertEquals('.', argument.getInitialSeparator());
 assertEquals(':', argument.getSubsequentSeparator());
 assertEquals(true, argument.getSubsequentSplit());
 assertEquals(ValidateIntRange.class, argument.getValidator().getClass());
 assertEquals("--extra", argument.getConsumeRemaining());
 assertEquals(Collections.singletonList("defaultValue"), argument.getDefaultValues());
 }

 @Test
 public void testProcessValues() throws OptionException {
 CommandLine cmd = new CommandLine();
 ArgumentImpl argument = new ArgumentImpl(
 "arg",
 "A test argument",
 1,
 1,
 '.',
 ':',
 new ValidateIntRange(0, 10),
 "--extra",
 Collections.singletonList("defaultValue"),
 0);

 List<String> args = Arrays.asList(
 "--arg", "1", "--arg", "2:11", "--arg", "3:15", "--extra", "--arg", "5:17");
 argument.process(cmd, args.listIterator(), option("arg"));

 List<String> expected = Arrays.asList("1", "2:11", "3:15");
 assertEquals(expected, cmd.getValues(option("arg")));
 }

 @Test(expected = OptionException.class)
 public void testProcessValuesWithInvalidValue() throws OptionException {
 CommandLine cmd = new CommandLine();
 ArgumentImpl argument = new ArgumentImpl(
 "arg",
 "A test argument",
 1,
 1,
 '.',
 ':',
 new ValidateIntRange(0, 10),
 "--extra",
 Collections.singletonList("defaultValue"),
 0);

 List<String> args = Arrays.asList(
 "--arg", "1", "--arg", "2:11", "--arg", "3:15", "--extra", "--arg", "12:18");
 argument.process(cmd, args.listIterator(), option("arg"));

 fail("OptionException should have been thrown");
 }

 @Test
 public void testCanProcess() {
 ArgumentImpl argument = new ArgumentImpl(
 "arg",
 "A test argument",
 1,
 1,
 '.',
 ':',
 new ValidateIntRange(0, 10),
 "--extra",
 Collections.singletonList("defaultValue"),
 0);

 assertTrue(argument.canProcess(new CommandLine(), "test"));
 }

 @Test
 public void testGetPrefixes() {
 ArgumentImpl argument = new ArgumentImpl(
 "arg",
 "A test argument",
 1,
 1,
 '.',
 ':',
 new ValidateIntRange(0, 10),
 "--extra",
 Collections.singletonList("defaultValue"),
 0);

 assertEquals(Collections.emptySet(), argument.getPrefixes());
 }

 @Test
 public void testProcess() throws OptionException {
 CommandLine cmd = new CommandLine();
 ArgumentImpl argument = new ArgumentImpl(
 "arg",
 "A test argument",
 1,
 1,
 '.',
 ':',
 new ValidateIntRange(0, 10),
 "--extra",
 Collections.singletonList("defaultValue"),
 0);

 List<String> args = Arrays.asList("--arg", "1", "--arg", "2:11", "--arg", "3:15");
 argument.process(cmd, args.listIterator(), option("arg"));

 List<String> expected = Arrays.asList("1", "2:11", "3:15");
 assertEquals(expected, cmd.getValues(option("arg")));
 }

 @Test
 public void testGetInitialSeparator() {
 ArgumentImpl argument = new ArgumentImpl(
 "arg",
 "A test argument",
 1,
 1,
 '.',
 ':',
 new ValidateIntRange(0, 10),
 "--extra",
 Collections.singletonList("defaultValue"),
 0);

 assertEquals('.', argument.getInitialSeparator());
 }

 @Test
 public void testGetSubsequentSeparator() {
 ArgumentImpl argument = new ArgumentImpl(
 "arg",
 "A test argument",
 1,
 1,
 '.',
 ':',
 new ValidateIntRange(0, 10),
 "--extra",
 Collections.singletonList("defaultValue"),
 0);

 assertEquals(':', argument.getSubsequentSeparator());
 }

 @Test
 public void testGetTriggers() {
 ArgumentImpl argument = new ArgumentImpl(
 "arg",
 "A test argument",
 1,
 1,
 '.',
 ':',
 new ValidateIntRange(0, 10),
 "--extra",
 Collections.singletonList("defaultValue"),
 0);

 assertEquals(Collections.emptySet(), argument.getTriggers());
 }

 @Test
 public void testGetConsumeRemaining() {
 ArgumentImpl argument = new ArgumentImpl(
 "arg",
 "A test argument",
 1,
 1,
 '.',
 ':',
 new ValidateIntRange(0, 10),
 "--extra",
 Collections.singletonList("defaultValue"),
 0);

 assertEquals("--extra", argument.getConsumeRemaining());
 }

 @Test
 public void testGetDefaultValues() {
 ArgumentImpl argument = new ArgumentImpl(
 "arg",
 "A test argument",
 1,
 1,
 '.',
 ':',
 new ValidateIntRange(0, 10),
 "--extra",
 Collections.singletonList("defaultValue"),
 0);

 assertEquals(Collections.singletonList("defaultValue"), argument.getDefaultValues());
 }

 @Test
 public void testGetValidator() {
 ArgumentImpl argument = new ArgumentImpl(
 "arg",
 "A test argument",
 1,
 1,
 '.',
 ':',
 new ValidateIntRange(0, 10),
 "--extra",
 Collections.singletonList("defaultValue"),
 0);

 assertEquals(new ValidateIntRange(0, 10), argument.getValidator());
 }

 @Test
 public void testValidate() throws OptionException {
 CommandLine cmd = new CommandLine();
 ArgumentImpl argument = new ArgumentImpl(
 "arg",
 "A test argument",
 1,
 1,
 '.',
 ':',
 new ValidateIntRange(0, 10),
 "--extra",
 Collections.singletonList("defaultValue"),
 0);

 List<String> args = Arrays.asList("--arg", "1:5");
 argument.validate(cmd);
 }

 @Test(expected = OptionException.class)
 public void testValidateWithInvalidValue() throws OptionException {
 CommandLine cmd = new CommandLine();
 ArgumentImpl argument = new ArgumentImpl(
 "arg",
 "A test argument",
 1,
 1,
 '.',
 ':',
 new ValidateIntRange(0, 10),
 "--extra",
 Collections.singletonList("defaultValue"),
 0);

 List<String> args = Arrays.asList("--arg", "1:5", "--arg", "11:18");
 argument.validate(cmd);

 fail("OptionException should have been thrown");
 }

 @Test
 public void testAppendUsage() {
 ArgumentImpl argument = new ArgumentImpl(
 "arg",
 "A test argument",
 1,
 1,
 '.',
 ':',
 new ValidateIntRange(0, 10),
 "--extra",
 Collections.singletonList("defaultValue"),
 0);

 StringBuilder buffer = new StringBuilder();
 argument.appendUsage(buffer, null, null);

 String expected = "  [--arg]            A test argument";
 assertEquals(expected, buffer.toString());
 }
}

class ValidateIntRange implements Validator {
 private int min;
 private int max;

 public ValidateIntRange(int min, int max) {
 this.min = min;
 this.max = max;
 }

 public void validate(List values) throws InvalidArgumentException {
 int count = values.size();
 for (int i = 0; i < count; i++) {
 String value = (String) values.get(i);
 try {
 int number = Integer.parseInt(value);
 } catch (NumberFormatException e) {
 throw new InvalidArgumentException(
 "Invalid integer value: " + value, this, value);
 }
 if (number < min) {
 throw new InvalidArgumentException(
 "Value " + value + " is less than minimum value " + min, this, value);
 }
 if (number > max) {
 throw new InvalidArgumentException(
 "Value " + value + " is greater than maximum value " + max, this, value);
 }
 }
 }
}
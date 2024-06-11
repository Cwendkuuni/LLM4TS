import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.ParseException;
import org.apache.commons.cli2.TestUtils;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class ArgumentImplTest {

 private CommandLine cmd;
 private Argument arg;

 @Before
 public void setUp() {
 cmd = new CommandLine();
 arg = new ArgumentImpl("argName", "Argument description", 1, 1, ArgumentImpl.DEFAULT_INITIAL_SEPARATOR,
 ArgumentImpl.DEFAULT_SUBSEQUENT_SEPARATOR, null, ArgumentImpl.DEFAULT_CONSUME_REMAINING,
 Collections.singletonList("defaultValue"), 0);
 }

 @Test
 public void testGetName() {
 assertEquals("argName", arg.getName());
 }

 @Test
 public void testGetDescription() {
 assertEquals("Argument description", arg.getDescription());
 }

 @Test
 public void testGetMinimum() {
 assertEquals(1, arg.getMinimum());
 }

 @Test
 public void testGetMaximum() {
 assertEquals(1, arg.getMaximum());
 }

 @Test
 public void testGetInitialSeparator() {
 assertEquals(ArgumentImpl.DEFAULT_INITIAL_SEPARATOR, arg.getInitialSeparator());
 }

 @Test
 public void testGetSubsequentSeparator() {
 assertEquals(ArgumentImpl.DEFAULT_SUBSEQUENT_SEPARATOR, arg.getSubsequentSeparator());
 }

 @Test
 public void testCanProcess() {
 assertTrue(arg.canProcess(cmd, "defaultValue"));
 }

 @Test(expected = OptionException.class)
 public void testCannotProcessInvalidValue() throws OptionException {
 arg.process(cmd, TestUtils.getList("invalidValue").listIterator());
 }

 @Test
 public void testProcessValues() throws OptionException {
 arg.process(cmd, TestUtils.getList("value1", "value2").listIterator());

 assertEquals("value1", cmd.getValue(arg.getOpt(), 0));
 assertEquals("value2", cmd.getValue(arg.getOpt(), 1));
 }

 @Test
 public void testProcessRemainingValues() throws OptionException {
 arg = new ArgumentImpl("argName", "Argument description", 1, 1, ArgumentImpl.DEFAULT_INITIAL_SEPARATOR,
 ArgumentImpl.DEFAULT_SUBSEQUENT_SEPARATOR, null, ArgumentImpl.DEFAULT_CONSUME_REMAINING,
 Collections.emptyList(), 0);

 arg.process(cmd, TestUtils.getList("--consume-remaining", "value1", "value2").listIterator());

 assertEquals("value1", cmd.getValue(arg.getOpt(), 0));
 assertEquals("value2", cmd.getValue(arg.getOpt(), 1));
 assertEquals(2, cmd.getValues(arg.getOpt()).size());
 }

 @Test
 public void testGetTriggers() {
 assertNull(arg.getTriggers());
 }

 @Test
 public void testProcess() throws OptionException {
 arg.process(cmd, TestUtils.getList("value1", "value2").listIterator());

 assertEquals("value1", cmd.getValue(arg.getOpt(), 0));
 assertEquals("value2", cmd.getValue(arg.getOpt(), 1));
 }

 @Test
 public void testGetPrefixes() {
 assertFalse(arg.getPrefixes().isEmpty());
 }

 @Test
 public void testGetValidator() {
 assertNull(arg.getValidator());
 }

 @Test
 public void testValidate() throws OptionException {
 arg = new ArgumentImpl("argName", "Argument description", 1, 1, ArgumentImpl.DEFAULT_INITIAL_SEPARATOR,
 ArgumentImpl.DEFAULT_SUBSEQUENT_SEPARATOR, null, ArgumentImpl.DEFAULT_CONSUME_REMAINING,
 Collections.singletonList("defaultValue"), 0) {
 private final String validValue = "validValue";
 private final String invalidValue = "invalidValue";
 private final Validator validator = new StubValidator();

 @Override
 public void validate(WriteableCommandLine commandLine, Option option)
 throws OptionException {
 try {
 validator.validate(commandLine.getValues(option));
 } catch (InvalidArgumentException ive) {
 throw new OptionException(option, ive.getMessage());
 }
 }
 };

 arg.validate(cmd, arg);

 assertTrue(cmd.getValues(arg.getOpt()).contains(validValue));
 assertFalse(cmd.getValues(arg.getOpt()).contains(invalidValue));
 }

 @Test(expected = OptionException.class)
 public void testValidateInvalid() throws OptionException {
 arg = new ArgumentImpl("argName", "Argument description", 1, 1, ArgumentImpl.DEFAULT_INITIAL_SEPARATOR,
 ArgumentImpl.DEFAULT_SUBSEQUENT_SEPARATOR, null, ArgumentImpl.DEFAULT_CONSUME_REMAINING,
 Collections.emptyList(), 0) {
 private final String value = "value";
 private final InvalidArgumentException expectedException = new InvalidArgumentException("Validation error");
 private final Validator validator = new StubValidator() {
 @Override
 public void validate(List values) throws InvalidArgumentException {
 throw expectedException;
 }
 };

 @Override
 public void validate(WriteableCommandLine commandLine, Option option)
 throws OptionException {
 validator.validate(commandLine.getValues(option));
 }
 };

 arg.validate(cmd, arg);
 }

 @Test
 public void testAppendUsage() {
 final StringBuffer buffer = new StringBuffer();
 arg.appendUsage(buffer, null, null);

 assertEquals(" argName (An Argument description)\n", buffer.toString());
 }

 @Test
 public void testHelpLines() {
 final Set<DisplaySetting> helpSettings = TestUtils.createHelpSettings();
 final HelpFormatter formatter = new HelpFormatter();

 final List<HelpLine> helpLines = arg.helpLines(0, helpSettings, null);

 final HelpLine helpLine = helpLines.get(0);

 assertEquals("argName:", helpLine.getKeys()[0]);
 assertEquals("An Argument description", helpLine.getShortDescription());
 assertEquals("", helpLine.getLongDescription());
 assertEquals(0, helpLine.getOptionSpec().getOption().getMinimum());
 assertEquals(1, helpLine.getOptionSpec().getOption().getMaximum());
 }

 @Test
 public void testDefaultValues() {
 arg = new ArgumentImpl("argName", "Argument description", 1, 1, ArgumentImpl.DEFAULT_INITIAL_SEPARATOR,
 ArgumentImpl.DEFAULT_SUBSEQUENT_SEPARATOR, null, ArgumentImpl.DEFAULT_CONSUME_REMAINING,
 Arrays.asList("defaultValue1", "defaultValue2"), 0);

 cmd.parse(TestUtils.getArgs("-argName", "value1", "value2"));

 assertEquals("value1", cmd.getValue(arg.getOpt(), 0));
 assertEquals("value2", cmd.getValue(arg.getOpt(), 1));
 assertEquals("defaultValue1", cmd.getValue(arg.getOpt(), 2));
 assertEquals("defaultValue2", cmd.getValue(arg.getOpt(), 3));
 }
}

class StubValidator implements Validator {

 @Override
 public void validate(List values) throws InvalidArgumentException {
 // empty
 }
}
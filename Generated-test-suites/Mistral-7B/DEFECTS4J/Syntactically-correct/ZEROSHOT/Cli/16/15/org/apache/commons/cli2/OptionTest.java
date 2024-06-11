import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.parser.DefaultParser;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class OptionTest {

 private Option option;
 private WriteableCommandLine commandLine;

 @Before
 public void setup() {
 option = new TestOption();
 commandLine = new TestCommandLine();
 }

 @Test
 public void testProcess() throws Exception {
 List<String> args = Arrays.asList("--option", "value").listIterator();
 option.process(commandLine, args);
 assertEquals("value", commandLine.getValue("option"));
 }

 @Test
 public void testProcessWithNoValue() throws Exception {
 List<String> args = Arrays.asList("--option").listIterator();
 option.process(commandLine, args);
 assertNull(commandLine.getValue("option"));
 }

 @Test
 public void testProcessWithInvalidArgument() throws Exception {
 List<String> args = Arrays.asList("--invalid-option", "value").listIterator();
 assertThrows(OptionException.class, () -> option.process(commandLine, args));
 }

 @Test
 public void testDefaults() {
 option.defaults(commandLine);
 assertEquals("defaultValue", commandLine.getValue("option"));
 }

 @Test
 public void testCanProcessWithValidArgument() throws Exception {
 List<String> args = Arrays.asList("--option", "value").listIterator();
 assertTrue(option.canProcess(commandLine, args));
 }

 @Test
 public void testCanProcessWithInvalidArgument() throws Exception {
 List<String> args = Arrays.asList("--invalid-option", "value").listIterator();
 assertFalse(option.canProcess(commandLine, args));
 }

 @Test
 public void testGetTriggers() {
 Set<String> triggers = option.getTriggers();
 Set<String> expectedTriggers = new HashSet<>(Arrays.asList("--option"));
 assertEquals(expectedTriggers, triggers);
 }

 @Test
 public void testGetPrefixes() {
 Set<String> prefixes = option.getPrefixes();
 Set<String> expectedPrefixes = new HashSet<>(Arrays.asList("--"));
 assertEquals(expectedPrefixes, prefixes);
 }

 @Test
 public void testValidate() throws Exception {
 option.validate(commandLine);
 }

 @Test(expected = OptionException.class)
 public void testValidateWithMissingValue() throws Exception {
 option.validate(commandLine);
 commandLine.setValue("option", null);
 option.validate(commandLine);
 }

 @Test(expected = OptionException.class)
 public void testValidateWithInvalidValue() throws Exception {
 option.validate(commandLine);
 commandLine.setValue("option", "invalidValue");
 option.validate(commandLine);
 }

 @Test
 public void testHelpLines() {
 List<String> helpLines = option.helpLines(1, new HashSet<>(), null);
 assertEquals(2, helpLines.size());
 assertTrue(helpLines.get(0).startsWith("- option:"));
 assertTrue(helpLines.get(1).startsWith("  value:"));
 }

 @Test
 public void testAppendUsage() {
 StringBuilder buffer = new StringBuilder();
 option.appendUsage(buffer, new HashSet<>(), null);
 assertEquals("option - option:\n  value:\n", buffer.toString());
 }

 @Test
 public void testGetPreferredName() {
 assertEquals("option", option.getPreferredName());
 }

 @Test
 public void testGetDescription() {
 assertEquals("This is a test option", option.getDescription());
 }

 @Test
 public void testgetId() {
 assertEquals(1, option.getId());
 }

 @Test
 public void testFindOption() {
 Option otherOption = new TestOption();
 commandLine.addOption(otherOption);
 Option foundOption = option.findOption("option");
 assertSame(option, foundOption);
 }

 @Test
 public void testIsRequired() {
 assertFalse(option.isRequired());
 }

 @Test
 public void testGetParent() {
 assertNull(option.getParent());
 }

 @Test
 public void testSetParent() {
 Option parentOption = new TestOption();
 option.setParent(parentOption);
 assertSame(parentOption, option.getParent());
 }
}

class TestOption implements Option {
 @Override
 public void process(WriteableCommandLine commandLine, ListIterator<String> args)
 throws OptionException {
 // Empty implementation
 }

 @Override
 public void defaults(WriteableCommandLine commandLine) {
 commandLine.setValue("option", "defaultValue");
 }

 @Override
 public boolean canProcess(WriteableCommandLine commandLine, String argument) {
 return true;
 }

 @Override
 public boolean canProcess(WriteableCommandLine commandLine, ListIterator<String> arguments) {
 return true;
 }

 @Override
 public Set getTriggers() {
 return new HashSet(Arrays.asList("--option"));
 }

 @Override
 public Set getPrefixes() {
 return new HashSet(Arrays.asList("--"));
 }

 @Override
 public void validate(WriteableCommandLine commandLine) throws OptionException {
 // Empty implementation
 }

 @Override
 public List helpLines(int depth, Set helpSettings, Comparator comp) {
 return new ArrayList<>();
 }

 @Override
 public void appendUsage(StringBuffer buffer, Set helpSettings, Comparator comp) {
 // Empty implementation
 }

 @Override
 public String getPreferredName() {
 return "option";
 }

 @Override
 public String getDescription() {
 return "This is a test option";
 }

 @Override
 public int getId() {
 return 1;
 }

 @Override
 public Option findOption(String trigger) {
 return null;
 }

 @Override
 public boolean isRequired() {
 return false;
 }

 @Override
 public Option getParent() {
 return null;
 }

 @Override
 public void setParent(Option parent) {
 // Empty implementation
 }
}

class TestCommandLine implements WriteableCommandLine {
 private final List<String> values = new ArrayList<>();

 @Override
 public void setValue(String key, String value) {
 values.put(key, value);
 }

 @Override
 public String getValue(String key) {
 return values.get(values.indexOf(key));
 }

 @Override
 public List<String> getValues(String key) {
 return Arrays.asList(values.get(values.indexOf(key)));
 }
}
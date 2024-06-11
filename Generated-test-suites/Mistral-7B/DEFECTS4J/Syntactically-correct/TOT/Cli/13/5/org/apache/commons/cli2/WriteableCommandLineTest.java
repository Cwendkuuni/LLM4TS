import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineTest {

 @Test
 public void testAddOption() {
 WriteableCommandLine commandLine = new MockWriteableCommandLine();
 Option option = new Option("o", "output", true, "Output file path");
 commandLine.addOption(option);
 assertTrue(commandLine.getOptions().contains(option));
 }

 @Test
 public void testAddValue() {
 WriteableCommandLine commandLine = new MockWriteableCommandLine();
 Option option = new Option("o", "output", true, "Output file path");
 commandLine.addOption(option);
 commandLine.addValue(option, "output.txt");
 assertEquals("output.txt", commandLine.getOptionValue(option));
 }

 @Test
 public void testGetValues() {
 WriteableCommandLine commandLine = new MockWriteableCommandLine();
 Option option = new Option("a", "arg", true, "Argument value");
 commandLine.addOption(option);
 commandLine.addValue(option, "arg1");
 commandLine.addValue(option, "arg2");
 assertEquals(Arrays.asList("arg1", "arg2"), commandLine.getValues(option));
 }

 @Test
 public void testSetDefaultValues() {
 WriteableCommandLine commandLine = new MockWriteableCommandLine();
 Option option = new Option("o", "output", true, "Output file path");
 List<String> defaultValues = Arrays.asList("default1.txt", "default2.txt");
 commandLine.setDefaultValues(option, defaultValues);
 assertEquals(defaultValues, commandLine.getValues(option));
 }

 @Test(expected = IllegalStateException.class)
 public void testAddSwitchTwice() {
 WriteableCommandLine commandLine = new MockWriteableCommandLine();
 Option option = new Option("s", "switch", false, "Switch value");
 commandLine.addSwitch(option, true);
 commandLine.addSwitch(option, false);
 // No exception is thrown in real implementation, but we're testing for an exception here
 fail("IllegalStateException expected when adding switch value twice");
 }

 @Test
 public void testSetDefaultSwitch() {
 WriteableCommandLine commandLine = new MockWriteableCommandLine();
 Option option = new Option("s", "switch", false, "Switch value");
 commandLine.setDefaultSwitch(option, true);
 assertTrue(commandLine.hasOption(option) && commandLine.isSet(option));
 }

 @Test
 public void testAddProperty() {
 WriteableCommandLine commandLine = new MockWriteableCommandLine();
 commandLine.addProperty("property", "value");
 assertEquals("value", commandLine.getProperty("property"));
 }

 @Test
 public void testLooksLikeOption() {
 WriteableCommandLine commandLine = new MockWriteableCommandLine();
 assertTrue(commandLine.looksLikeOption("--option"));
 assertTrue(commandLine.looksLikeOption("-o"));
 assertFalse(commandLine.looksLikeOption("arg"));
 }
}

class MockWriteableCommandLine implements WriteableCommandLine {
 private List<Option> options = new ArrayList<>();
 private Map<String, String> properties = new HashMap<>();

 @Override
 public void addOption(Option option) {
 options.add(option);
 }

 @Override
 public void addValue(Option option, Object value) {
 // Empty implementation
 }

 @Override
 public List<String> getValues(Option option) {
 return Collections.emptyList();
 }

 @Override
 public void setDefaultValues(Option option, List values) {
 // Empty implementation
 }

 @Override
 public void addSwitch(Option option, boolean value) throws IllegalStateException {
 // Empty implementation
 }

 @Override
 public void setDefaultSwitch(Option option, Boolean value) {
 // Empty implementation
 }

 @Override
 public void addProperty(String property, String value) {
 properties.put(property, value);
 }

 @Override
 public String getProperty(String property) {
 return properties.get(property);
 }

 @Override
 public boolean looksLikeOption(String argument) {
 return argument.startsWith("--") || argument.startsWith("-");
 }

 @Override
 public boolean hasOption(Option option) {
 return options.contains(option);
 }

 @Override
 public boolean isSet(Option option) {
 return option.getValues() != null && !option.getValues().isEmpty();
 }
}
import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class WriteableCommandLineTest {

 @Test
 public void testAddOption() {
 WriteableCommandLine cmdLine = new CliCommandLineParser().parse("-t,--test");
 Option option = Option.builder("o").longOpt("option").build();
 cmdLine.addOption(option);
 assertEquals("--option", option.getOpt());
 assertEquals("o", option.getShortOpt());
 assertFalse(cmdLine.getOptions().isEmpty());
 }

 @Test
 public void testAddValue() {
 Option option = Option.builder("o").longOpt("option").valueSeparator(' ').build();
 WriteableCommandLine cmdLine = new CliCommandLineParser().parse("-t,--test value");
 cmdLine.addOption(option);
 cmdLine.addValue(option, "value1 value2");
 assertEquals("value1 value2", option.getValue());
 }

 @Test
 public void testGetValues() {
 Option option = Option.builder("o").longOpt("option").build();
 WriteableCommandLine cmdLine = new CliCommandLineParser().parse("-t,--test value1 value2");
 cmdLine.addOption(option);
 cmdLine.addValue(option, "value1 value2");
 List<String> values = cmdLine.getValuesForOption("o");
 assertArrayEquals(new String[]{"value1", "value2"}, values.toArray(new String[0]));
 }

 @Test
 public void testSetDefaultValues() {
 Option option = Option.builder("o").longOpt("option").build();
 WriteableCommandLine cmdLine = new CliCommandLineParser().parse("");
 cmdLine.setDefaultValues(option, Arrays.asList("value1", "value2"));
 List<String> values = cmdLine.getValuesForOption("o");
 assertArrayEquals(new String[]{"value1", "value2"}, values.toArray(new String[0]));
 }

 @Test(expected = IllegalStateException.class)
 public void testAddSwitchTwice() {
 Option option = Option.builder("o").longOpt("option").build();
 WriteableCommandLine cmdLine = new CliCommandLineParser().parse("");
 cmdLine.addSwitch(option, true);
 cmdLine.addSwitch(option, true);
 }

 @Test
 public void testSetDefaultSwitch() {
 Option option = Option.builder("o").longOpt("option").build();
 WriteableCommandLine cmdLine = new CliCommandLineParser().parse("");
 cmdLine.setDefaultSwitch(option, true);
 assertTrue(option.isSet());
 }

 @Test
 public void testAddProperty() {
 WriteableCommandLine cmdLine = new CliCommandLineParser().parse("");
 cmdLine.addProperty("myProperty", "value");
 assertEquals("value", cmdLine.getPropertyValue("myProperty"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLooksLikeOptionWithEmptyArgument() {
 WriteableCommandLine cmdLine = new CliCommandLineParser().parse("");
 assertFalse(cmdLine.looksLikeOption(""));
 }

 @Test
 public void testLooksLikeOptionWithValidOption() {
 WriteableCommandLine cmdLine = new CliCommandLineParser().parse("-t,--test");
 assertTrue(cmdLine.looksLikeOption("test"));
 }

 @Test
 public void testLooksLikeOptionWithInvalidArgument() {
 WriteableCommandLine cmdLine = new CliCommandLineParser().parse("-t,--test value");
 assertFalse(cmdLine.looksLikeOption("value"));
 }
}
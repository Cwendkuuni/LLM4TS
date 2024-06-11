import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.ParseException;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class WriteableCommandLineTest {

 private CommandLine commandLine;

 @Test
 public void testAddOption() throws Exception {
 Option option = Option.builder("o").longOpt("option").hasArg(true).build();
 commandLine = new Cmd(); // assuming Cmd is a class implementing WriteableCommandLine
 commandLine.addOption(option);
 Option addedOption = commandLine.getOption("option");
 assertEquals("o", addedOption.getOpt());
 assertEquals("option", addedOption.getLongOpt());
 assertFalse(addedOption.hasArg());
 }

 @Test
 public void testAddValue() throws Exception {
 Option option = Option.builder("o").longOpt("option").valueSeparator('=').build();
 commandLine = new Cmd();
 commandLine.addOption(option);
 commandLine.addValue(option, "value1=1");
 commandLine.addValue(option, "value2=2");
 List<String> values = commandLine.getValuesForOption("option");
 assertArrayEquals(new String[]{"value1=1", "value2=2"}, values.toArray());
 }

 @Test(expected = IllegalStateException.class)
 public void testAddSwitchTwice() throws Exception {
 Option option = Option.builder("s").longOpt("switch").build();
 commandLine = new Cmd();
 commandLine.addSwitch(option, true);
 commandLine.addSwitch(option, true);
 }

 @Test
 public void testAddSwitch() throws Exception {
 Option option = Option.builder("s").longOpt("switch").build();
 commandLine = new Cmd();
 commandLine.addSwitch(option, true);
 assertTrue(commandLine.hasOption("switch"));
 assertTrue(commandLine.getOption("switch").getValue().booleanValue());
 }

 @Test
 public void testSetDefaultValues() throws Exception {
 Option option = Option.builder("o").longOpt("option").build();
 commandLine = new Cmd();
 commandLine.setDefaultValues(option, new String[]{"default1", "default2"});
 List<String> values = commandLine.getValuesForOption("option");
 assertArrayEquals(new String[]{"default1", "default2"}, values.toArray());
 }

 @Test
 public void testLooksLikeOption() throws Exception {
 commandLine = new Cmd();
 commandLine.addOption(Option.builder("t").longOpt("test").build());
 assertTrue(commandLine.looksLikeOption("--test"));
 assertTrue(commandLine.looksLikeOption("--test="));
 assertTrue(commandLine.looksLikeOption("-t"));
 }

 @Test
 public void testLooksLikeOption_invalid() throws Exception {
 commandLine = new Cmd();
 assertFalse(commandLine.looksLikeOption("invalid"));
 }

 @Test(expected = ParseException.class)
 public void testAddInvalidValue() throws Exception {
 Option option = Option.builder("o").longOpt("option").hasArg(true).build();
 commandLine = new Cmd();
 commandLine.addOption(option);
 commandLine.addValue(option, "value");
 commandLine.addValue(option, "invalid");
 }

 @Test
 public void testAddProperty() throws Exception {
 commandLine = new Cmd();
 commandLine.addProperty("property", "defaultValue");
 String propertyValue = commandLine.getProperty("property");
 assertEquals("defaultValue", propertyValue);
 }
}
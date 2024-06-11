import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert.*;
import java.util.ArrayList;

public class WriteableCommandLineTest {

 private WriteableCommandLine commandLine;

 @Before
 public void setup() {
 commandLine = new CliCommandLine(); // Assuming CliCommandLine is a concrete implementation of WriteableCommandLine
 }

 @Test
 public void testAddOption() {
 Option option = Option.builder("o").longOpt("option").build();
 commandLine.addOption(option);
 assertTrue(commandLine.getOptions().contains(option));
 }

 @Test
 public void testAddValue() {
 Option option = Option.builder("v").longOpt("value").valueSeparator(' ').build();
 commandLine.addOption(option);
 commandLine.addValue(option, "test1 test2");
 List<String> values = commandLine.getUndefaultedValues(option);
 assertEquals(2, values.size());
 assertEquals("test1 test2", values.toString());
 }

 @Test
 public void testGetUndefaultedValues() {
 Option option = Option.builder("u").longOpt("values").build();
 commandLine.addOption(option);
 commandLine.addValue(option, "test1");
 List<String> values = commandLine.getUndefaultedValues(option);
 assertEquals(1, values.size());
 assertEquals("test1", values.get(0));
 }

 @Test
 public void testSetDefaultValues() {
 Option option = Option.builder("d").longOpt("defaults").build();
 List<String> defaults = new ArrayList<>();
 defaults.add("default1");
 defaults.add("default2");
 commandLine.setDefaultValues(option, defaults);
 List<String> values = commandLine.getUndefaultedValues(option);
 assertTrue(values.isEmpty());
 List<String> defaultsValues = commandLine.getOptionValues(option);
 assertEquals(2, defaultsValues.size());
 assertEquals("default1 default2", defaultsValues.toString());
 }

 @Test
 public void testAddSwitch() {
 Option option = Option.builder("s").longOpt("switch").build();
 commandLine.addSwitch(option, true);
 assertTrue(commandLine.getValue(option.getOpt())[0].equals("true"));
 }

 @Test(expected = IllegalStateException.class)
 public void testAddSwitchWithExistingValue() {
 Option option = Option.builder("s").longOpt("switch").build();
 commandLine.addSwitch(option, true);
 commandLine.addSwitch(option, true);
 }

 @Test
 public void testSetDefaultSwitch() {
 Option option = Option.builder("sw").longOpt("defaultSwitch").build();
 commandLine.setDefaultSwitch(option, true);
 assertFalse(commandLine.getValue(option.getOpt())[0].equals("false"));
 }

 @Test
 public void testAddProperty() {
 Option option = Option.builder("p").longOpt("property").build();
 commandLine.addProperty(option, "name", "value");
 assertEquals("value", commandLine.getProperty(option.getOpt()));
 }

 @Test
 public void testAddPropertyWithDefault() {
 commandLine.addProperty("name", "defaultValue");
 assertEquals("defaultValue", commandLine.getProperty("name"));
 }

 @Test
 public void testLooksLikeOption() {
 assertTrue(commandLine.looksLikeOption("--option"));
 assertTrue(commandLine.looksLikeOption("-o"));
 assertFalse(commandLine.looksLikeOption("--value"));
 assertFalse(commandLine.looksLikeOption("test"));
 }
}
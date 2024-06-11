import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.WriteableCommandLineImpl;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;

public class WriteableCommandLineImplTest {

 private WriteableCommandLineImpl commandLine;

 @Before
 public void setUp() {
 Option option = new Option("o", "option", true, "Option value");
 Option subOption = new Option("s", "suboption", true, "Suboption value");
 Argument arg = new Argument("a", "argument");
 option.addArgument(arg);
 option.addOption(subOption);
 commandLine = new WriteableCommandLineImpl(option, Arrays.asList("a", "o", "s", "suboption", "value"));
 }

 @Test
 public void testAddOption() {
 Option option = new Option("o2", "option2");
 commandLine.addOption(option);
 List options = commandLine.getOptions();
 assertTrue(options.contains(option));
 }

 @Test(expected = IllegalStateException.class)
 public void testAddOptionTwice() {
 Option option = new Option("o", "option");
 commandLine.addOption(option);
 commandLine.addOption(option);
 }

 @Test
 public void testAddValue() {
 commandLine.addValue(commandLine.getOption("o"), "value1");
 List values = commandLine.getValues(commandLine.getOption("o"), null);
 assertEquals(Arrays.asList("value1"), values);
 commandLine.addValue(commandLine.getOption("o"), "value2");
 values = commandLine.getValues(commandLine.getOption("o"), null);
 assertEquals(Arrays.asList("value1", "value2"), values);
 }

 @Test
 public void testAddSwitch() {
 commandLine.addSwitch(commandLine.getOption("o"), true);
 boolean switchValue = commandLine.getSwitch(commandLine.getOption("o"), false);
 assertTrue(switchValue);
 commandLine.addSwitch(commandLine.getOption("o"), false);
 switchValue = commandLine.getSwitch(commandLine.getOption("o"), true);
 assertFalse(switchValue);
 }

 @Test
 public void testHasOption() {
 assertTrue(commandLine.hasOption(commandLine.getOption("o")));
 assertFalse(commandLine.hasOption(new Option("o2", "option2")));
 }

 @Test
 public void testGetOption() {
 Option option = commandLine.getOption("o");
 Option subOption = commandLine.getOption("s");
 assertEquals(option, commandLine.getOption("o"));
 assertEquals(subOption, commandLine.getOption("s"));
 }

 @Test
 public void testGetValues() {
 List values = commandLine.getValues(commandLine.getOption("o"), null);
 assertEquals(Arrays.asList("value1", "value2"), values);
 }

 @Test
 public void testGetUndefaultedValues() {
 List values = commandLine.getUndefaultedValues(commandLine.getOption("o"));
 assertNull(values);
 }

 @Test
 public void testGetSwitch() {
 assertSame(commandLine.getOption("o"), commandLine.getOption("o"));
 Boolean switchValue = commandLine.getSwitch(commandLine.getOption("o"), false);
 assertTrue(switchValue);
 }

 @Test
 public void testAddProperty() {
 Option option = commandLine.getOption("o");
 commandLine.addProperty(option, "property", "value");
 Map properties = commandLine.getProperty(option);
 assertEquals(1, properties.size());
 assertEquals("value", properties.get("property"));
 }

 @Test
 public void testGetProperty() {
 Option option = commandLine.getOption("o");
 commandLine.addProperty(option, "property", "value1");
 commandLine.addProperty(option, "property", "value2");
 assertEquals("value2", commandLine.getProperty(option, "property"));
 }

 @Test
 public void testGetProperties() {
 Option option = commandLine.getOption("o");
 commandLine.addProperty(option, "property1", "value1");
 commandLine.addProperty(option, "property2", "value2");
 Map properties = commandLine.getProperties(option);
 assertEquals(2, properties.size());
 assertTrue(properties.containsKey("property1"));
 assertTrue(properties.containsKey("property2"));
 }

 @Test
 public void testToString() {
 assertEquals("a option option suboption value1 value2", commandLine.toString());
 }

 @Test
 public void testGetListOptions() {
 List options = commandLine.getOptions();
 List expectedOptions = Arrays.asList(commandLine.getOption("o"), commandLine.getOption("s"));
 assertEquals(expectedOptions, options);
 }

 @Test
 public void testGetOptionTriggers() {
 Set optionTriggers = commandLine.getOptionTriggers();
 Set expectedOptionTriggers = Arrays.asList("o", "s").iterator().asList();
 assertEquals(expectedOptionTriggers, optionTriggers);
 }

 @Test
 public void testSetDefaultValues() {
 Option option = commandLine.getOption("o");
 List defaults = new ArrayList(Arrays.asList("default1", "default2"));
 commandLine.setDefaultValues(option, defaults);
 List values = commandLine.getValues(option, null);
 assertEquals(defaults, values);
 }

 @Test
 public void testSetDefaultSwitch() {
 Option option = commandLine.getOption("o");
 commandLine.setDefaultSwitch(option, true);
 Boolean switchValue = commandLine.getSwitch(option, false);
 assertTrue(switchValue);
 commandLine.setDefaultSwitch(option, false);
 switchValue = commandLine.getSwitch(option, true);
 assertFalse(switchValue);
 }

 @Test
 public void testGetNormalised() {
 List normalised = commandLine.getNormalised();
 List expectedNormalised = Arrays.asList("a", "o", "s", "suboption", "value1", "value2");
 assertEquals(expectedNormalised, normalised);
 }
}
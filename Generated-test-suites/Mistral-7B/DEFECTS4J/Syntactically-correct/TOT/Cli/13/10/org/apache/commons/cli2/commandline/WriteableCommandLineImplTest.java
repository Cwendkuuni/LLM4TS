import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.parser.DefaultParser;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class WriteableCommandLineImplTest {

 private WriteableCommandLineImpl commandLine;
 private DefaultParser parser;

 @Before
 public void setUp() {
 parser = new DefaultParser();
 commandLine = new WriteableCommandLineImpl(null, Collections.emptyList());
 }

 @Test
 public void testAddOption() {
 Option option = new Option("o", "option", false, "An option");
 commandLine.addOption(option);
 assertTrue(commandLine.hasOption(option));
 }

 @Test
 public void testAddValue() {
 Option option = new Option("o", "option", false, "An option");
 commandLine.addOption(option);
 commandLine.addValue(option, "value1");
 commandLine.addValue(option, "value2");
 assertEquals(Arrays.asList("value1", "value2"), commandLine.getValues(option, null));
 }

 @Test
 public void testAddSwitch() {
 Option option = new Option("s", "switch", false, "A switch");
 commandLine.addSwitch(option, true);
 assertTrue(commandLine.getSwitch(option, false));
 }

 @Test
 public void testHasOption() {
 Option option = new Option("o", "option", false, "An option");
 commandLine.addOption(option);
 assertTrue(commandLine.hasOption(option));
 }

 @Test
 public void testGetOption() {
 Option option = new Option("o", "option", false, "An option");
 commandLine.addOption(option);
 assertSame(option, commandLine.getOption(option.getPreferredName()));
 }

 @Test
 public void testGetValues() {
 Option option = new Option("o", "option", false, "An option");
 commandLine.addOption(option);
 commandLine.addValue(option, "value1");
 commandLine.addValue(option, "value2");
 assertEquals(Arrays.asList("value1", "value2"), commandLine.getValues(option, null));
 }

 @Test
 public void testGetSwitch() {
 Option option = new Option("s", "switch", false, "A switch");
 commandLine.addSwitch(option, true);
 assertTrue(commandLine.getSwitch(option, false));
 }

 @Test
 public void testAddProperty() {
 commandLine.addProperty("property", "value");
 assertEquals("value", commandLine.getProperty("property", null));
 }

 @Test
 public void testGetProperty() {
 commandLine.addProperty("property", "value");
 assertEquals("value", commandLine.getProperty("property", null));
 assertEquals(Collections.emptySet(), commandLine.getProperties());
 }

 @Test
 public void testToString() {
 Option option = new Option("o", "option", false, "An option");
 commandLine.addOption(option);
 commandLine.addValue(option, "value1");
 commandLine.addValue(option, "value2");
 String actual = commandLine.toString();
 String expected = "option value1 value2";
 assertEquals(expected, actual);
 }

 @Test
 public void testGetListOptions() {
 Option option1 = new Option("o1", "option1", false, "Option 1");
 Option option2 = new Option("o2", "option2", false, "Option 2");
 commandLine.addOption(option1);
 commandLine.addOption(option2);
 List<Option> options = commandLine.getOptions();
 assertEquals(2, options.size());
 assertTrue(options.contains(option1));
 assertTrue(options.contains(option2));
 }

 @Test
 public void testGetOptionTriggers() {
 Option option = new Option("o", "option", false, "An option");
 commandLine.addOption(option);
 Set<String> optionTriggers = commandLine.getOptionTriggers();
 assertTrue(optionTriggers.contains(option.getPreferredName()));
 }

 @Test(expected = IllegalStateException.class)
 public void testAddSwitchWithExistingSwitch() {
 Option option = new Option("s", "switch", false, "A switch");
 commandLine.addSwitch(option, true);
 commandLine.addSwitch(option, true);
 }

 @Test
 public void testParseCommandLine() {
 Option option = new Option("o", "option", false, "An option");
 commandLine.addOption(option);
 String[] args = {"-o", "value1", "-o", "value2"};
 CommandLine parsed = parser.parse(commandLine, args);
 assertEquals(Arrays.asList("value1", "value2"), parsed.getValues(option, null));
 }

 @Test
 public void testParseCommandLineWithEmptyValues() {
 Option option = new Option("o", "option", false, "An option");
 commandLine.addOption(option);
 String[] args = {"-o"};
 CommandLine parsed = parser.parse(commandLine, args);
 assertNull(parsed.getValues(option, null));
 }

 @Test
 public void testParseCommandLineWithProperties() {
 Option option = new Option("o", "option", false, "An option");
 commandLine.addOption(option);
 commandLine.addProperty("property", "value");
 String[] args = {"-o", "value1", "--property=value2"};
 CommandLine parsed = parser.parse(commandLine, args);
 assertEquals("value2", parsed.getProperty("property", null));
 }
}
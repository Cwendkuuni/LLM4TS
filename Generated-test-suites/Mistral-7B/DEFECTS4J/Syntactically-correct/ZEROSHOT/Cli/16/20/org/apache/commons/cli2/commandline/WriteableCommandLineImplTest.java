import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

 private WriteableCommandLineImpl commandLine;
 private Option option;

 @Before
 public void setUp() {
 option = new Option("o", "option", false, "a test option");
 commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
 }

 @Test
 public void testAddOption() {
 commandLine.addOption(option);
 assertTrue(commandLine.hasOption(option));
 }

 @Test(expected = IllegalStateException.class)
 public void testAddOptionTwice() {
 commandLine.addOption(option);
 commandLine.addOption(option);
 fail("Option should not be able to be added twice");
 }

 @Test
 public void testGetOption() {
 Option returnedOption = commandLine.getOption("o");
 assertEquals(option, returnedOption);
 }

 @Test
 public void testGetOptionFalseTrigger() {
 Option returnedOption = commandLine.getOption("x");
 assertNull(returnedOption);
 }

 @Test
 public void testGetValues() {
 List values = commandLine.getValues(option, null);
 assertEquals(Collections.EMPTY_LIST, values);
 }

 @Test
 public void testAddValue() {
 commandLine.addValue(option, "value1");
 List values = commandLine.getValues(option, null);
 assertEquals(Collections.singletonList("value1"), values);
 }

 @Test
 public void testAddValues() {
 commandLine.addValue(option, "value1");
 commandLine.addValue(option, "value2");
 List values = commandLine.getValues(option, null);
 assertEquals(Arrays.asList("value1", "value2"), values);
 }

 @Test
 public void testAddSwitch() {
 commandLine.addSwitch(option, true);
 assertTrue(commandLine.getSwitch(option, null));
 }

 @Test
 public void testAddSwitchWithDefault() {
 commandLine.addSwitch(option, false);
 commandLine.setDefaultSwitch(option, true);
 assertTrue(commandLine.getSwitch(option, null));
 }

 @Test
 public void testGetSwitch() {
 commandLine.addSwitch(option, false);
 assertFalse(commandLine.getSwitch(option, null));
 }

 @Test
 public void testGetProperty() {
 commandLine.addProperty(option, "property", "value");
 assertEquals("value", commandLine.getProperty(option, "property"));
 }

 @Test
 public void testGetPropertyDefault() {
 commandLine.addProperty(option, "property", "defaultValue");
 assertEquals("defaultValue", commandLine.getProperty(option, "property"));
 }

 @Test
 public void testGetProperties() {
 commandLine.addProperty(option, "property1", "value1");
 commandLine.addProperty(option, "property2", "value2");
 Set properties = commandLine.getProperties(option);
 assertEquals(2, properties.size());
 }

 @Test
 public void testToString() {
 CommandLine cl = new CommandLine();
 cl.addOption(option);
 cl.parse(new String[]{"o"});
 String result = commandLine.toString();
 String expected = String.format("%s %s", option.getLongOpt(), option.getValueDescription());
 assertEquals(expected, result);
 }

 @Test
 public void testGetOptions() {
 assertEquals(Collections.singletonList(option), commandLine.getOptions());
 }

 @Test
 public void testGetOptionTriggers() {
 Set triggers = commandLine.getOptionTriggers();
 assertEquals(1, triggers.size());
 assertTrue(triggers.contains("o"));
 }

 @Test
 public void testSetDefaultValues() {
 List defaultValues = Arrays.asList("value1", "value2");
 commandLine.setDefaultValues(option, defaultValues);
 List values = commandLine.getValues(option, null);
 assertEquals(defaultValues, values);
 }

 @Test
 public void testSetDefaultSwitch() {
 commandLine.setDefaultSwitch(option, true);
 assertTrue(commandLine.getSwitch(option, null));
 }

 @Test
 public void testGetNormalised() {
 CommandLine cl = new CommandLine();
 cl.addOption(option);
 cl.parse(new String[]{"o"});
 List normalised = commandLine.getNormalised();
 assertEquals(Arrays.asList("o"), normalised);
 }
}
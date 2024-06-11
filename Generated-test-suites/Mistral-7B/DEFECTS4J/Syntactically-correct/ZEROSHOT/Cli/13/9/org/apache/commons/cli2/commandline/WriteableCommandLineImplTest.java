import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

 private WriteableCommandLineImpl commandLine;
 private Option option;

 @Before
 public void setUp() {
 commandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
 option = new Option("a", "append", true, "Append to a file");
 commandLine.addOption(option);
 }

 @Test
 public void testAddOption() {
 assertTrue(commandLine.getOptions().contains(option));
 }

 @Test
 public void testAddValue() {
 List values = commandLine.getValues(option, null);
 assertEquals(0, values.size());
 commandLine.addValue(option, "testValue");
 values = commandLine.getValues(option, null);
 assertEquals(1, values.size());
 assertEquals("testValue", values.get(0));
 }

 @Test
 public void testAddSwitch() {
 assertFalse(commandLine.getSwitch(option, false));
 commandLine.addSwitch(option, true);
 assertTrue(commandLine.getSwitch(option, false));
 assertFalse(commandLine.getSwitch(option, true));
 }

 @Test
 public void testHasOption() {
 assertTrue(commandLine.hasOption(option));
 }

 @Test
 public void testGetOption() {
 Option result = commandLine.getOption("a");
 assertEquals(option, result);
 }

 @Test
 public void testGetValues() {
 List values = commandLine.getValues(option, null);
 assertEquals(0, values.size());
 commandLine.addValue(option, "testValue1");
 commandLine.addValue(option, "testValue2");
 values = commandLine.getValues(option, null);
 assertEquals(2, values.size());
 assertEquals("testValue1", values.get(0));
 assertEquals("testValue2", values.get(1));
 }

 @Test
 public void testGetSwitch() {
 assertFalse(commandLine.getSwitch(option, false));
 commandLine.addSwitch(option, true);
 assertTrue(commandLine.getSwitch(option, false));
 assertFalse(commandLine.getSwitch(option, true));
 }

 @Test
 public void testAddProperty() {
 commandLine.addProperty("testProperty", "testValue");
 assertEquals("testValue", commandLine.getProperty("testProperty", ""));
 }

 @Test
 public void testGetProperty() {
 commandLine.addProperty("testProperty", "testValue");
 assertEquals("testValue", commandLine.getProperty("testProperty", ""));
 assertEquals("", commandLine.getProperty("nonExistentProperty", ""));
 }

 @Test
 public void testGetProperties() {
 Map properties = commandLine.getProperties();
 assertEquals(1, properties.size());
 assertTrue(properties.containsKey("testProperty"));
 }

 @Test
 public void testLooksLikeOption() {
 assertTrue(commandLine.looksLikeOption("a"));
 assertFalse(commandLine.looksLikeOption("notOption"));
 }

 @Test
 public void testToString() {
 List arguments = new ArrayList<>();
 arguments.add("testArgument1");
 arguments.add("testArgument2");
 commandLine = new WriteableCommandLineImpl(null, arguments);
 commandLine.addOption(option);
 commandLine.addValue(option, "testValue");
 String result = commandLine.toString();
 String expected = "testArgument1 testArgument2 -a testValue";
 assertEquals(expected, result);
 }

 @Test
 public void testGetOptionsList() {
 List options = commandLine.getOptions();
 assertEquals(1, options.size());
 assertTrue(options.contains(option));
 }

 @Test
 public void testGetOptionTriggersSet() {
 Set optionTriggers = commandLine.getOptionTriggers();
 assertEquals(1, optionTriggers.size());
 assertTrue(optionTriggers.contains("a"));
 }

 @Test
 public void testSetDefaultValues() {
 List defaults = new ArrayList<>();
 defaults.add("defaultValue1");
 defaults.add("defaultValue2");
 commandLine.setDefaultValues(option, defaults);
 List result = commandLine.getValues(option, null);
 assertEquals(2, result.size());
 assertEquals("defaultValue1", result.get(0));
 assertEquals("defaultValue2", result.get(1));
 }

 @Test
 public void testSetDefaultSwitch() {
 commandLine.setDefaultSwitch(option, true);
 assertTrue(commandLine.getSwitch(option, false));
 assertFalse(commandLine.getSwitch(option, true));
 }

 @Test
 public void testGetNormalised() {
 List normalised = commandLine.getNormalised();
 assertEquals(3, normalised.size());
 }
}
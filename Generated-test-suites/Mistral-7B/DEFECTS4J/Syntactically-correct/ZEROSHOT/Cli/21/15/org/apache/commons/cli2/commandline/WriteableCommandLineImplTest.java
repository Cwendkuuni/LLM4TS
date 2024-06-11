import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

 private WriteableCommandLineImpl commandLine;
 private Option option;

 @Before
 public void setUp() {
 option = new Option("o", "option", true, "an option");
 commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
 }

 @After
 public void tearDown() {
 commandLine = null;
 option = null;
 }

 @Test
 public void testAddOption() {
 commandLine.addOption(option);
 assertTrue(commandLine.getOptions().contains(option));
 }

 @Test(expected = IllegalStateException.class)
 public void testAddOptionTwice() {
 commandLine.addOption(option);
 commandLine.addOption(option);
 fail("Should throw an IllegalStateException");
 }

 @Test
 public void testAddValue() {
 commandLine.addValue(option, "value");
 List values = commandLine.getValues(option, null);
 assertEquals(1, values.size());
 assertEquals("value", values.get(0));
 }

 @Test
 public void testAddSwitch() {
 commandLine.addSwitch(option, true);
 assertTrue(commandLine.getSwitch(option, false));
 }

 @Test
 public void testHasOption() {
 assertTrue(commandLine.hasOption(option));
 }

 @Test
 public void testGetOption() {
 Option result = commandLine.getOption("o");
 assertEquals(option, result);
 }

 @Test
 public void testGetValues() {
 commandLine.addValue(option, "value1");
 commandLine.addValue(option, "value2");
 List values = commandLine.getValues(option, null);
 assertEquals(2, values.size());
 assertEquals("value1", values.get(0));
 assertEquals("value2", values.get(1));
 }

 @Test
 public void testGetUndefaultedValues() {
 commandLine.addValue(option, "value1");
 List values = commandLine.getUndefaultedValues(option);
 assertEquals(1, values.size());
 assertEquals("value1", values.get(0));
 }

 @Test
 public void testGetSwitch() {
 commandLine.addSwitch(option, true);
 assertTrue(commandLine.getSwitch(option, false));
 }

 @Test
 public void testGetProperty() {
 commandLine.addProperty(option, "property", "value");
 String result = commandLine.getProperty(option, "property");
 assertEquals("value", result);
 }

 @Test
 public void testAddProperty() {
 commandLine.addProperty(option, "property1", "value1");
 commandLine.addProperty(option, "property2", "value2");
 Map result = commandLine.getProperties(option);
 assertEquals(2, result.size());
 assertEquals("value1", result.get("property1"));
 assertEquals("value2", result.get("property2"));
 }

 @Test
 public void testGetProperties() {
 commandLine.addProperty(option, "property1", "value1");
 commandLine.addProperty(option, "property2", "value2");
 Set result = commandLine.getProperties();
 assertEquals(2, result.size());
 assertTrue(result.contains("property1"));
 assertTrue(result.contains("property2"));
 }

 @Test
 public void testLooksLikeOption() {
 assertTrue(commandLine.looksLikeOption("o"));
 assertTrue(commandLine.looksLikeOption("o--option"));
 assertFalse(commandLine.looksLikeOption("argument"));
 }

 @Test
 public void testToString() {
 commandLine.addValue(option, "value1");
 commandLine.addValue(option, "value2");
 String result = commandLine.toString();
 assertEquals("option value1 value2", result);
 }

 @Test
 public void testGetOptions() {
 List options = commandLine.getOptions();
 assertEquals(1, options.size());
 assertEquals(option, options.get(0));
 }

 @Test
 public void testGetOptionTriggers() {
 Set optionTriggers = commandLine.getOptionTriggers();
 assertEquals(1, optionTriggers.size());
 assertTrue(optionTriggers.contains("o"));
 }

 @Test
 public void testSetDefaultValues() {
 List defaultValues = new ArrayList();defaultValues.add("default1");defaultValues.add("default2");
 commandLine.setDefaultValues(option, defaultValues);
 List result = commandLine.getValues(option, null);
 assertEquals(2, result.size());
 assertEquals("default1", result.get(0));
 assertEquals("default2", result.get(1));
 }

 @Test
 public void testSetDefaultSwitch() {
 commandLine.setDefaultSwitch(option, true);
 assertTrue(commandLine.getSwitch(option, false));
 }

 @Test
 public void testGetNormalised() {
 List normalised = commandLine.getNormalised();
 assertEquals(0, normalised.size());
 }
}
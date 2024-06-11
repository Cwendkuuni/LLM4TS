import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class WriteableCommandLineImplTest {

 @Test
 public void testWriteableCommandLineImplCreation() {
 WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(new Option("testOption"), new ArrayList<>());
 assertNotNull(writeableCommandLine);
 }

 @Test
 public void testAddOption() {
 WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(new Option("testOption"), new ArrayList<>());
 Option option = new Option(null, "testOption", false, "Test Option");
 writeableCommandLine.addOption(option);
 assertTrue(writeableCommandLine.hasOption(option));
 }

 @Test(expected = IllegalStateException.class)
 public void testAddOptionWithExistingOption() {
 WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(new Option("testOption"), new ArrayList<>());
 Option option = new Option(null, "testOption", false, "Test Option");
 writeableCommandLine.addOption(option);
 writeableCommandLine.addOption(option);
 }

 @Test
 public void testAddValue() {
 WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(new Option("testOption"), new ArrayList<>());
 writeableCommandLine.addOption(new Option(null, "testOption", false, "Test Option"));
 writeableCommandLine.addValue(new Option("testOption"), "value1");
 List values = writeableCommandLine.getValues(new Option("testOption"), null);
 assertEquals("value1", values.get(0));
 }

 @Test
 public void testAddSwitch() {
 WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(new Option("testOption"), new ArrayList<>());
 writeableCommandLine.addOption(new Option(null, "testOption", false, "Test Option"));
 writeableCommandLine.addSwitch(new Option("testOption"), true);
 assertTrue(writeableCommandLine.getSwitch(new Option("testOption"), false));
 }

 @Test
 public void testHasOption() {
 WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(new Option("testOption"), new ArrayList<>());
 Option option = new Option(null, "testOption", false, "Test Option");
 writeableCommandLine.addOption(option);
 assertTrue(writeableCommandLine.hasOption(option));
 }

 @Test
 public void testGetOption() {
 WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(new Option("testOption"), new ArrayList<>());
 Option option = new Option(null, "testOption", false, "Test Option");
 writeableCommandLine.addOption(option);
 assertEquals(option, writeableCommandLine.getOption("testOption"));
 }

 @Test
 public void testGetValues() {
 WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(new Option("testOption"), new ArrayList<>());
 Option option = new Option(null, "testOption", false, "Test Option");
 writeableCommandLine.addOption(option);
 writeableCommandLine.addValue(option, "value1");
 writeableCommandLine.addValue(option, "value2");
 List values = writeableCommandLine.getValues(option, null);
 assertEquals(2, values.size());
 assertEquals("value1", values.get(0));
 assertEquals("value2", values.get(1));
 }

 @Test
 public void testGetUndefaultedValues() {
 WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(new Option("testOption"), new ArrayList<>());
 Option option = new Option(null, "testOption", false, "Test Option");
 writeableCommandLine.addOption(option);
 List values = writeableCommandLine.getUndefaultedValues(option);
 assertNull(values);
 }

 @Test
 public void testGetSwitch() {
 WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(new Option("testOption"), new ArrayList<>());
 Option option = new Option(null, "testOption", false, "Test Option");
 writeableCommandLine.addOption(option);
 writeableCommandLine.addSwitch(option, true);
 assertTrue(writeableCommandLine.getSwitch(option, false));
 }

 @Test
 public void testGetProperty() {
 WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(new Option("testOption"), new ArrayList<>());
 Option option = new Option(null, "testOption", false, "Test Option");
 Properties properties = new Properties();
 properties.setProperty("testProperty", "testValue");
 writeableCommandLine.addOption(option);
 writeableCommandLine.addProperty(option, "testProperty", "testValue");
 assertEquals("testValue", writeableCommandLine.getProperty(option, "testProperty", null));
 }

 @Test
 public void testAddProperty() {
 WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(new Option("testOption"), new ArrayList<>());
 Option option = new Option(null, "testOption", false, "Test Option");
 Properties properties = new Properties();
 properties.setProperty("testProperty1", "testValue1");
 properties.setProperty("testProperty2", "testValue2");
 writeableCommandLine.addOption(option);
 writeableCommandLine.addProperty(option, "testProperty1", "testValue1");
 writeableCommandLine.addProperty(option, "testProperty2", "testValue2");
 assertEquals(2, writeableCommandLine.getProperties(option).size());
 }

 @Test
 public void testGetProperties() {
 WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(new Option("testOption"), new ArrayList<>());
 Option option = new Option(null, "testOption", false, "Test Option");
 Properties properties = new Properties();
 properties.setProperty("testProperty1", "testValue1");
 properties.setProperty("testProperty2", "testValue2");
 writeableCommandLine.addOption(option);
 writeableCommandLine.addProperty(option, "testProperty1", "testValue1");
 writeableCommandLine.addProperty(option, "testProperty2", "testValue2");
 Set propertiesSet = writeableCommandLine.getProperties(option);
 assertEquals(2, propertiesSet.size());
 }

 @Test
 public void testGetPropertiesEmpty() {
 WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(new Option("testOption"), new ArrayList<>());
 Option option = new Option(null, "testOption", false, "Test Option");
 writeableCommandLine.addOption(option);
 Set propertiesSet = writeableCommandLine.getProperties(option);
 assertEquals(0, propertiesSet.size());
 }

 @Test
 public void testToString() {
 WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(new Option("testOption"), new ArrayList<>());
 Option option1 = new Option(null, "testOption1", false, "Test Option 1");
 Option option2 = new Option(null, "testOption2", false, "Test Option 2");
 Option option3 = new Option(null, "testOption3", false, "Test Option 3");
 writeableCommandLine.addOption(option1);
 writeableCommandLine.addOption(option2);
 writeableCommandLine.addOption(option3);
 String expected = "testOption1 testOption2 testOption3";
 assertEquals(expected, writeableCommandLine.toString());
 }

 @Test
 public void testGetOptions() {
 WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(new Option("testOption"), new ArrayList<>());
 Option option1 = new Option(null, "testOption1", false, "Test Option 1");
 Option option2 = new Option(null, "testOption2", false, "Test Option 2");
 Option option3 = new Option(null, "testOption3", false, "Test Option 3");
 writeableCommandLine.addOption(option1);
 writeableCommandLine.addOption(option2);
 writeableCommandLine.addOption(option3);
 List options = writeableCommandLine.getOptions();
 assertEquals(3, options.size());
 }

 @Test
 public void testGetOptionTriggers() {
 WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(new Option("testOption"), new ArrayList<>());
 Option option1 = new Option(null, "testOption1", false, "Test Option 1");
 Option option2 = new Option(null, "testOption2", false, "Test Option 2");
 Option option3 = new Option(null, "testOption3", false, "Test Option 3");
 writeableCommandLine.addOption(option1);
 writeableCommandLine.addOption(option2);
 writeableCommandLine.addOption(option3);
 Set optionTriggers = writeableCommandLine.getOptionTriggers();
 assertEquals(3, optionTriggers.size());
 }

 @Test
 public void testLooksLikeOption() {
 WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(new Option("testOption"), new ArrayList<>());
 String trigger1 = "--testOption";
 String trigger2 = "-testOption";
 assertTrue(writeableCommandLine.looksLikeOption(trigger1));
 assertTrue(writeableCommandLine.looksLikeOption(trigger2));
 }

 @Test
 public void testSetDefaultValues() {
 WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(new Option("testOption"), new ArrayList<>());
 Option option = new Option(null, "testOption", false, "Test Option");
 List defaults = new ArrayList();
 defaults.add("value1");
 defaults.add("value2");
 writeableCommandLine.setDefaultValues(option, defaults);
 List values = writeableCommandLine.getValues(option, null);
 assertEquals(2, values.size());
 assertEquals("value1", values.get(0));
 assertEquals("value2", values.get(1));
 }

 @Test
 public void testSetDefaultSwitch() {
 WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(new Option("testOption"), new ArrayList<>());
 Option option = new Option(null, "testOption", false, "Test Option");
 writeableCommandLine.setDefaultSwitch(option, true);
 assertTrue(writeableCommandLine.getSwitch(option, false));
 }

 @Test
 public void testGetNormalised() {
 WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(new Option("testOption"), new ArrayList<>());
 Option option1 = new Option(null, "testOption1", false, "Test Option 1");
 Option option2 = new Option(null, "testOption2", false, "Test Option 2");
 Option option3 = new Option(null, "testOption3", false, "Test Option 3");
 writeableCommandLine.addOption(option1);
 writeableCommandLine.addOption(option2);
 writeableCommandLine.addOption(option3);
 List normalised = writeableCommandLine.getNormalised();
 assertEquals(3, normalised.size());
 }
}
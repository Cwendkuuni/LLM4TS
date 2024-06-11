import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class WriteableCommandLineImplTest {

 private WriteableCommandLine writeableCommandLine;
 private Option option;

 @Before
 public void setUp() {
 writeableCommandLine = new WriteableCommandLineImpl(new Option("a", "append", true, "append file"), new ArrayList());
 option = new Option("b", "boolean", false, "a boolean option");
 writeableCommandLine.addOption(option);
 }

 @Test
 public void testHasOption() {
 assertTrue(writeableCommandLine.hasOption(option));
 }

 @Test
 public void testGetOption() {
 Option testOption = writeableCommandLine.getOption("b");
 assertEquals(option, testOption);
 }

 @Test
 public void testGetValues_withValues() {
 writeableCommandLine.addValue(option, "testValue1");
 writeableCommandLine.addValue(option, "testValue2");
 List values = writeableCommandLine.getValues(option, null);
 assertEquals(2, values.size());
 assertEquals("testValue1", values.get(0));
 assertEquals("testValue2", values.get(1));
 }

 @Test
 public void testGetValues_withoutValues() {
 List values = writeableCommandLine.getValues(option, null);
 assertTrue(values.isEmpty());
 }

 @Test
 public void testGetSwitch() {
 assertFalse(writeableCommandLine.getSwitch(option, false));
 }

 @Test
 public void testGetSwitch_withValue() {
 writeableCommandLine.addSwitch(option, true);
 assertTrue(writeableCommandLine.getSwitch(option, false) == false);
 }

 @Test
 public void testGetProperty() {
 writeableCommandLine.addProperty(option, "testProperty", "testValue");
 assertEquals("testValue", writeableCommandLine.getProperty(option, "testProperty", "defaultValue"));
 assertEquals("defaultValue", writeableCommandLine.getProperty(option, "nonexistentProperty", "defaultValue"));
 assertNull(writeableCommandLine.getProperty(option, null));
 }

 @Test
 public void testGetProperties() {
 writeableCommandLine.addProperty(option, "testProperty1", "testValue1");
 writeableCommandLine.addProperty(option, "testProperty2", "testValue2");
 Set properties = writeableCommandLine.getProperties(option);
 assertEquals(2, properties.size());
 assertTrue(properties.contains("testProperty1"));
 assertTrue(properties.contains("testProperty2"));
 }

 @Test
 public void testToString() {
 assertEquals("testValue1 testValue2", writeableCommandLine.toString());
 }
}
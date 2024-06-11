import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class WriteableCommandLineImplTest {

 private WriteableCommandLine writeableCommandLine;
 private Option option;

 @Before
 public void setUp() {
 option = new Option("test", "Test option");
 writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
 }

 @Test
 public void testAddOption() {
 writeableCommandLine.addOption(option);
 assertTrue(writeableCommandLine.getOptions().contains(option));
 }

 @Test(expected = IllegalStateException.class)
 public void testAddOptionTwice() {
 writeableCommandLine.addOption(option);
 writeableCommandLine.addOption(option);
 }

 @Test
 public void testAddValue() {
 writeableCommandLine.addValue(option, "value1");
 List values = writeableCommandLine.getValues(option, null);
 assertEquals(1, values.size());
 assertEquals("value1", values.get(0));
 }

 @Test
 public void testAddSwitch() {
 writeableCommandLine.addSwitch(option, true);
 assertTrue(writeableCommandLine.getSwitch(option, false));
 }

 @Test(expected = IllegalStateException.class)
 public void testAddSwitchTwice() {
 writeableCommandLine.addSwitch(option, true);
 writeableCommandLine.addSwitch(option, true);
 }

 @Test
 public void testHasOption() {
 assertTrue(writeableCommandLine.hasOption(option));
 }

 @Test
 public void testGetOption() {
 Option opt = writeableCommandLine.getOption("test");
 assertSame(option, opt);
 }

 @Test
 public void testGetValues() {
 writeableCommandLine.addValue(option, "value1");
 writeableCommandLine.addValue(option, "value2");
 List values = writeableCommandLine.getValues(option, null);
 assertEquals(2, values.size());
 assertEquals("value1", values.get(0));
 assertEquals("value2", values.get(1));
 }

 @Test
 public void testGetUndefaultedValues() {
 List values = writeableCommandLine.getUndefaultedValues(option);
 assertNotNull(values);
 assertFalse(values.isEmpty());
 }

 @Test
 public void testGetSwitch() {
 writeableCommandLine.addSwitch(option, true);
 assertTrue(writeableCommandLine.getSwitch(option, false));
 writeableCommandLine.addSwitch(option, false);
 assertFalse(writeableCommandLine.getSwitch(option, false));
 }

 @Test
 public void testGetProperty() {
 writeableCommandLine.addProperty(option, "property", "value");
 assertEquals("value", writeableCommandLine.getProperty(option, "property"));
 }

 @Test
 public void testGetProperties() {
 writeableCommandLine.addProperty(option, "property1", "value1");
 writeableCommandLine.addProperty(option, "property2", "value2");
 Set properties = writeableCommandLine.getProperties(option);
 assertEquals(2, properties.size());
 }

 @Test
 public void testLooksLikeOption() {
 assertTrue(writeableCommandLine.looksLikeOption("test"));
 }

 @Test
 public void testToString() {
 CommandLine commandLine = new DefaultCommandLine();
 commandLine.addOption(option);
 commandLine.addValue(option, "value");
 writeableCommandLine = new WriteableCommandLineImpl(commandLine);
 writeableCommandLine.addValue(option, "value2");
 String expected = "test value value2";
 assertEquals(expected, writeableCommandLine.toString());
 }
}
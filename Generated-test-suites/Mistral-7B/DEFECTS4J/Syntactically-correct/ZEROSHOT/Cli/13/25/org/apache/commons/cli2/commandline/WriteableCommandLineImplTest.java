import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class WriteableCommandLineImplTest {

 @Test
 public void testCreateWriteableCommandLineImpl() {
 WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(new Option("option"), Collections.emptyList());
 assertEquals("option", writeableCommandLine.getOptions().get(0).getLongOpt());
 assertNull(writeableCommandLine.getValues("option", null));
 assertFalse(writeableCommandLine.hasOption(new Option("nonexistentoption")));
 }

 @Test
 public void testAddOption() {
 WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(new Option("option"), Collections.emptyList());
 Option option = new Option("option", "o", false, "Description");
 writeableCommandLine.addOption(option);
 assertEquals(1, writeableCommandLine.getOptions().size());
 assertEquals(option, writeableCommandLine.getOption("o"));
 }

 @Test
 public void testAddValue() {
 WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(new Option("option"), Collections.emptyList());
 Option option = new Option("option", "o", false, "Description");
 writeableCommandLine.addOption(option);
 writeableCommandLine.addValue(option, "value1");
 List values = writeableCommandLine.getValues(option, null);
 assertEquals(1, values.size());
 assertEquals("value1", values.get(0));
 }

 @Test
 public void testAddSwitch() {
 WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(new Option("option"), Collections.emptyList());
 Option option = new Option("option", "o", false, "Description");
 writeableCommandLine.addOption(option);
 writeableCommandLine.addSwitch(option, true);
 assertTrue(writeableCommandLine.getSwitch(option, false));
 }

 @Test
 public void testHasOption() {
 WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(new Option("option"), Collections.emptyList());
 Option option = new Option("option", "o", false, "Description");
 writeableCommandLine.addOption(option);
 assertTrue(writeableCommandLine.hasOption(option));
 }

 @Test
 public void testGetOption() {
 WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(new Option("option"), Collections.emptyList());
 Option option = new Option("option", "o", false, "Description");
 writeableCommandLine.addOption(option);
 Option result = writeableCommandLine.getOption("o");
 assertSame(option, result);
 }

 @Test
 public void testGetValues() {
 WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(new Option("option"), Collections.emptyList());
 Option option = new Option("option", "o", false, "Description");
 writeableCommandLine.addOption(option);
 writeableCommandLine.addValue(option, "value1");
 writeableCommandLine.addValue(option, "value2");
 List values = writeableCommandLine.getValues(option, null);
 assertEquals(2, values.size());
 assertEquals("value1", values.get(0));
 assertEquals("value2", values.get(1));
 }

 @Test
 public void testGetSwitch() {
 WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(new Option("option"), Collections.emptyList());
 Option option = new Option("option", "o", false, "Description");
 writeableCommandLine.addOption(option);
 writeableCommandLine.addSwitch(option, true);
 assertTrue(writeableCommandLine.getSwitch(option, false));
 assertTrue(writeableCommandLine.getSwitch(option, true));
 }

 @Test
 public void testAddProperty() {
 WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(new Option("option"), Collections.emptyList());
 writeableCommandLine.addProperty("property", "propertyValue");
 assertEquals("propertyValue", writeableCommandLine.getProperty("property", null));
 }

 @Test
 public void testGetProperty() {
 WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(new Option("option"), Collections.emptyList());
 writeableCommandLine.addProperty("property", "propertyValue");
 assertEquals("propertyValue", writeableCommandLine.getProperty("property", null));
 assertNull(writeableCommandLine.getProperty("nonexistentproperty", null));
 }
}
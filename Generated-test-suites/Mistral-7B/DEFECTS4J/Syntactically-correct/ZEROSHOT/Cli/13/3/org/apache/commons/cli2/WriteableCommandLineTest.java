import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.ParseContext;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineTest {

 private WriteableCommandLine commandLine;
 private ParseContext parseContext;

 @Before
 public void setup() {
 commandLine = new DefaultWriteableCommandLine();
 parseContext = new ParseContext();
 commandLine.parse(parseContext, new String[0]);
 }

 @Test
 public void testAddOption() {
 Option option = new Option("a", "argument", true, "Optional argument");
 commandLine.addOption(option);
 assertTrue(commandLine.getOptions().contains(option));
 }

 @Test
 public void testAddValue() {
 Option option = new Option("a", "argument", true, "Optional argument");
 commandLine.addOption(option);
 commandLine.addValue(option, "value1");
 assertEquals(1, commandLine.getValues(option).size());
 assertEquals("value1", commandLine.getValues(option).get(0));
 }

 @Test
 public void testGetValues() {
 Option option = new Option("a", "argument", true, "Optional argument");
 commandLine.addOption(option);
 commandLine.addValue(option, "value1");
 commandLine.addValue(option, "value2");
 List values = commandLine.getValues(option);
 assertEquals(2, values.size());
 assertEquals("value1", values.get(0));
 assertEquals("value2", values.get(1));
 }

 @Test
 public void testSetDefaultValues() {
 Option option = new Option("a", "argument", true, "Optional argument");
 commandLine.addOption(option);
 commandLine.setDefaultValues(option, Arrays.asList("default1", "default2"));
 assertEquals(2, commandLine.getValues(option).size());
 assertEquals("default1", commandLine.getValues(option).get(0));
 assertEquals("default2", commandLine.getValues(option).get(1));
 }

 @Test(expected = IllegalStateException.class)
 public void testAddSwitchTwice() {
 Option option = new Option("a", "argument", false, "Optional argument");
 commandLine.addOption(option);
 commandLine.addSwitch(option, true);
 commandLine.addSwitch(option, false);
 fail("IllegalStateException expected but not thrown");
 }

 @Test
 public void testAddSwitch() {
 Option option = new Option("a", "argument", false, "Optional argument");
 commandLine.addOption(option);
 commandLine.addSwitch(option, true);
 assertTrue(option.getValue().isPresent());
 assertTrue(option.getValue().get());
 }

 @Test
 public void testSetDefaultSwitch() {
 Option option = new Option("a", "argument", false, "Optional argument");
 commandLine.addOption(option);
 commandLine.setDefaultSwitch(option, true);
 assertFalse(option.getValue().isPresent());
 assertTrue(option.isSet());
 }

 @Test
 public void testAddProperty() {
 commandLine.addProperty("property", "value");
 assertEquals("value", commandLine.getProperty("property"));
 }

 @Test
 public void testLooksLikeOption() {
 assertTrue(commandLine.looksLikeOption("--option"));
 assertTrue(commandLine.looksLikeOption("-o"));
 }

 @Test
 public void testLooksLikeOptionFalse() {
 assertFalse(commandLine.looksLikeOption("arg"));
 assertFalse(commandLine.looksLikeOption("--"));
 }
}
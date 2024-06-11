import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.ParseContext;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class WriteableCommandLineTest {

 private WriteableCommandLine commandLine;
 private Option option;

 @Before
 public void setUp() {
 commandLine = new DefaultWriteableCommandLine();
 option = new Option("a", "argument", true, "default");
 }

 @Test
 public void testAddOption() {
 commandLine.addOption(option);
 assertEquals(1, commandLine.getOptions().size());
 assertSame(option, commandLine.getOptions().get(0));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddOptionTwice() {
 commandLine.addOption(option);
 commandLine.addOption(option);
 }

 @Test
 public void testAddValue() {
 commandLine.addOption(option);
 commandLine.addValue(option, "value1");
 commandLine.addValue(option, "value2");
 List<String> values = commandLine.getOptionValues("a");
 assertEquals(2, values.size());
 assertTrue(values.contains("value1"));
 assertTrue(values.contains("value2"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddValueToNonExistOption() {
 commandLine.addValue(new Option("b", "doesntExist"), "value1");
 }

 @Test
 public void testSetDefaultValues() {
 List<String> defaultValues = java.util.Arrays.asList("value1", "value2");
 commandLine.addOption(option);
 commandLine.setDefaultValues(option, defaultValues);
 List<String> values = commandLine.getOptionValues("a");
 assertEquals(2, values.size());
 assertTrue(values.contains("value1"));
 assertTrue(values.contains("value2"));
 }

 @Test
 public void testAddSwitch() {
 commandLine.addOption(option);
 commandLine.addSwitch(option, true);
 assertTrue(option.getValue().isSet());
 }

 @Test(expected = IllegalStateException.class)
 public void testAddSwitchTwice() {
 commandLine.addOption(option);
 commandLine.addSwitch(option, true);
 commandLine.addSwitch(option, false);
 }

 @Test
 public void testSetDefaultSwitch() {
 commandLine.addOption(option);
 commandLine.setDefaultSwitch(option, true);
 assertFalse(option.getValue().isSet());
 }

 @Test
 public void testAddProperty() {
 commandLine.addProperty("property1", "value1");
 assertEquals("value1", commandLine.getOptionValue("property1"));
 }

 @Test
 public void testLooksLikeOption() {
 assertTrue(commandLine.looksLikeOption("--argument"));
 assertTrue(commandLine.looksLikeOption("-a"));
 assertFalse(commandLine.looksLikeOption("value1"));
 }

 @Test
 public void testParseContext() {
 ParseContext parseContext = new DefaultParseContext();
 commandLine.setParseContext(parseContext);
 Option newOption = new Option("c", "newOption");
 commandLine.addOption(newOption);
 commandLine.addValue(newOption, "value1");
 commandLine.parse();
 List<String> values = parseContext.getParsedOptions().getOptionValues("c");
 assertEquals(1, values.size());
 assertEquals("value1", values.get(0));
 }
}
import org.apache.commons.cli2.BaseOption;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.parse.DefaultParser;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class WriteableCommandLineTest {

 private WriteableCommandLine commandLine;
 private Options options;

 @Before
 public void setUp() {
 options = new Options();
 commandLine = new CliOptionParserCommandLineParser().parse(options, new String[0]);
 }

 @Test
 public void testAddOption() {
 Option option = new Option("a", "apple", true, "Apple is nice");
 commandLine.addOption(option);
 assertEquals("Apple is nice", commandLine.getOption("a").getLongOpt().getDescription());
 }

 @Test
 public void testAddValue() {
 Option option = new Option("a", "apple");
 commandLine.addOption(option);
 commandLine.addValue(option, "red");
 List<String> values = commandLine.getUndefaultedValues(option);
 assertEquals(1, values.size());
 assertEquals("red", values.get(0));
 }

 @Test
 public void testGetUndefaultedValues() {
 Option option = new Option("a", "apple");
 commandLine.addOption(option);
 commandLine.addValue(option, "red");
 List<String> values = commandLine.getUndefaultedValues(option);
 assertEquals(1, values.size());
 assertEquals("red", values.get(0));

 Option option2 = new Option("b", "banana");
 commandLine.addOption(option2);
 List<String> values2 = commandLine.getUndefaultedValues(option2);
 assertNull(values2);
 }

 @Test
 public void testSetDefaultValues() {
 Option option = new Option("a", "apple");
 List<String> defaultValues = new ArrayList<>();
 defaultValues.add("default1");
 defaultValues.add("default2");
 commandLine.setDefaultValues(option, defaultValues);
 List<String> values = commandLine.getUndefaultedValues(option);
 assertEquals(0, values.size());
 List<String> defaults = commandLine.getOption("a").getValues();
 assertEquals(2, defaults.size());
 assertEquals("default1", defaults.get(0));
 assertEquals("default2", defaults.get(1));
 }

 @Test
 public void testAddSwitch() {
 Option option = new Option("a", "apple", false, "Apple is nice");
 commandLine.addOption(option);
 assertFalse(option.getValue().isSet());
 commandLine.addSwitch(option, true);
 assertTrue(option.getValue().isSet());
 }

 @Test(expected = IllegalStateException.class)
 public void testAddSwitchTwice() {
 Option option = new Option("a", "apple", false, "Apple is nice");
 commandLine.addOption(option);
 commandLine.addSwitch(option, true);
 commandLine.addSwitch(option, true);
 }

 @Test
 public void testSetDefaultSwitch() {
 Option option = new Option("a", "apple", false, "Apple is nice");
 commandLine.addOption(option);
 commandLine.setDefaultSwitch(option, true);
 assertTrue(option.getValue().isSet());
 }

 @Test
 public void testAddProperty() {
 Option option = new Option("a", "apple");
 commandLine.addOption(option);
 commandLine.addProperty(option, "color", "red");
 List<String> values = commandLine.getUndefaultedValues(option);
 assertNull(values);
 String value = commandLine.getOption("a").getProperty("color");
 assertEquals("red", value);
 }

 @Test
 public void testAddPropertyDefault() {
 commandLine.addProperty("color", "red");
 String value = commandLine.getDefaultProperty("color");
 assertEquals("red", value);
 }

 @Test
 public void testLooksLikeOption() {
 assertTrue(commandLine.looksLikeOption("--apple"));
 assertTrue(commandLine.looksLikeOption("--apple="));
 assertFalse(commandLine.looksLikeOption("--apple-"));
 assertFalse(commandLine.looksLikeOption("--apple-value"));
 }
}
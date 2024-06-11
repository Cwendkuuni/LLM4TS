import org.apache.commons.cli2.BaseParser;
import org.apache.commons.cli2.Option;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import static org.junit.Assert.*;

public class WriteableCommandLineTest {

 private WriteableCommandLine commandLine;

 @Before
 public void setup() {
 commandLine = new BaseParser().parse("test", new String[]{"-a", "-b", "--optionA=valueA", "--optionB=valueB"});
 }

 /**
 * Test method for {@link WriteableCommandLine#addOption(Option)} .
 */
 @Test
 public void testAddOption() {
 Option option = Option.builder("test").longOpt("testOption").build();
 commandLine.addOption(option);
 assertEquals(1, commandLine.getOptions().size());
 }

 /**
 * Test method for {@link WriteableCommandLine#addValue(Option, Object)} .
 */
 @Test
 public void testAddValue() {
 Option option = Option.builder("valueOption").valueRequired(true).build();
 commandLine.addOption(option);
 commandLine.addValue(option, "testValue");
 assertEquals("testValue", commandLine.getOptionValue("valueOption"));
 }

 /**
 * Test method for {@link WriteableCommandLine#getValues(Option)} .
 */
 @Test
 public void testGetValues() {
 Option option = Option.builder("valueOption").valueRequired(true).build();
 commandLine.addOption(option);
 commandLine.addValue(option, "testValue1");
 commandLine.addValue(option, "testValue2");
 List<String> values = commandLine.getValues(option);
 assertEquals(2, values.size());
 assertEquals("testValue1", values.get(0));
 assertEquals("testValue2", values.get(1));
 }

 /**
 * Test method for {@link WriteableCommandLine#setDefaultValues(Option, List)} .
 */
 @Test
 public void testSetDefaultValues() {
 Option option = Option.builder("defaultOption").build();
 commandLine.setDefaultValues(option, new ArrayList<String>() {{
 add("defaultValue1");
 add("defaultValue2");
 }});
 assertEquals(2, commandLine.getValues(option).size());
 assertEquals("defaultValue1", commandLine.getValues(option).get(0));
 assertEquals("defaultValue2", commandLine.getValues(option).get(1));
 }

 /**
 * Test method for {@link WriteableCommandLine#addSwitch(Option, boolean)} .
 */
 @Test
 public void testAddSwitch() {
 Option option = Option.builder("testSwitch").build();
 commandLine.addOption(option);
 try {
 commandLine.addSwitch(option, true);
 } catch (IllegalStateException ignored) {
 // expected since switch has already been added
 }
 commandLine.addSwitch(option, false);
 assertTrue(commandLine.hasOption("testSwitch") && commandLine.getOptionValue("testSwitch").equals("false"));
 }

 /**
 * Test method for {@link WriteableCommandLine#setDefaultSwitch(Option, Boolean)} .
 */
 @Test
 public void testSetDefaultSwitch() {
 Option option = Option.builder("testSwitch").build();
 commandLine.addOption(option);
 commandLine.setDefaultSwitch(option, true);
 assertTrue(commandLine.hasOption("testSwitch") && commandLine.getOptionValue("testSwitch").equals("true"));
 }

 /**
 * Test method for {@link WriteableCommandLine#addProperty(String, String)} .
 */
 @Test
 public void testAddProperty() {
 commandLine.addProperty("testProperty", "testValue");
 assertEquals("testValue", commandLine.getProperty("testProperty"));
 }

 /**
 * Test method for {@link WriteableCommandLine#looksLikeOption(String)} .
 */
 @Test
 public void testLooksLikeOption() {
 assertTrue(commandLine.looksLikeOption("-a"));
 assertTrue(commandLine.looksLikeOption("--optionA"));
 assertFalse(commandLine.looksLikeOption("testValue"));
 }
}
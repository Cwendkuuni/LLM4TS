import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.CommandLine;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;

public class WriteableCommandLineTest {

 private CommandLine cmd;

 @Test
 public void testAddOption() {
 Option option = Option.builder("option").build();
 cmd.addOption(option);
 assertEquals(option, cmd.getOption("option"));
 }

 @Test
 public void testAddValue() {
 Option option = Option.builder("option").build();
 cmd.addOption(option);
 cmd.addValue(option, "value1");
 assertEquals(1, cmd.getValues("option").size());
 assertEquals("value1", cmd.getValues("option").get(0));
 }

 @Test
 public void testGetValues() {
 Option option = Option.builder("option").build();
 cmd.addOption(option);
 cmd.addValue(option, "value1");
 cmd.addValue(option, "value2");
 List<Object> values = cmd.getValues("option");
 assertEquals(2, values.size());
 assertEquals("value1", values.get(0));
 assertEquals("value2", values.get(1));
 }

 @Test
 public void testSetDefaultValues() {
 Option option = Option.builder("option").build();
 List<Object> values = Arrays.asList("value1", "value2");
 cmd.setDefaultValues(option, values);
 assertEquals(values, cmd.getValues("option"));
 }

 @Test(expected = IllegalStateException.class)
 public void testAddSwitchTwice() {
 Option option = Option.builder("option").build();
 cmd.addSwitch(option, true);
 assertThrows(IllegalStateException.class, () -> cmd.addSwitch(option, false));
 }

 @Test
 public void testAddSwitch() {
 Option option = Option.builder("option").build();
 cmd.addSwitch(option, true);
 assertTrue(option.isSet());
 }

 @Test
 public void testSetDefaultSwitch() {
 Option option = Option.builder("option").build();
 cmd.setDefaultSwitch(option, true);
 assertTrue(option.isSet());
 }

 @Test
 public void testAddProperty() {
 cmd.addProperty("property", "value");
 assertEquals("value", cmd.getProperty("property"));
 }

 @Test
 public void testLooksLikeOption() {
 assertTrue(cmd.looksLikeOption("-o"));
 assertTrue(cmd.looksLikeOption("--option"));
 assertFalse(cmd.looksLikeOption("value"));
 }
}
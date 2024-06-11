import org.apache.commons.cli.Option;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class OptionTest {

 @Test
 public void testOptionWithNoArgs() {
 Option option = new Option("a", "Option A", false);
 assertEquals("Option A", option.getDescription());
 assertFalse(option.hasArg());
 assertNull(option.getValue());
 }

 @Test
 public void testOptionWithOneArg() {
 Option option = new Option("b", "Option B", true);
 assertEquals("Option B", option.getDescription());
 assertTrue(option.hasArg());
 assertNull(option.getValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidOption() {
 Option option = new Option("", "Invalid option", false);
 }

 @Test
 public void testOptionWithLongName() {
 Option option = new Option("c", "Option C with long name", false, "longName");
 assertEquals("Option C with long name", option.getLongOpt());
 assertEquals("Option C", option.getOpt());
 }

 @Test
 public void testOptionWithOptionalArg() {
 Option option = new Option("d", "Option D", true, "Optional Arg");
 assertEquals("Optional Arg", option.getDescription());
 assertTrue(option.hasOptionalArg());
 assertNull(option.getValue());
 }

 @Test
 public void testOptionWithValue() {
 Option option = new Option("e", "Option E", false);
 option.setValue("test");
 assertEquals("test", option.getValue());
 }

 @Test(expected = RuntimeException.class)
 public void testInvalidNumberOfValues() {
 Option option = new Option("f", "Option F", -3, "");
 }

 @Test
 public void testOptionWithMultipleValues() {
 Option option = new Option("g", "Option G", false, "G1,G2");
 option.setValueSeparator(',');
 option.addValueForProcessing("G1");
 option.addValueForProcessing("G2");
 assertEquals(2, option.getValuesList().size());
 assertEquals("G1", option.getValuesList().get(0));
 assertEquals("G2", option.getValuesList().get(1));
 }

 @Test
 public void testOptionWithValueAndDescription() {
 Option option = new Option("h", "Option H", false, "Value H1,H2", "Option H with values H1 and H2");
 option.setValueSeparator(',');
 option.addValueForProcessing("H1");
 option.addValueForProcessing("H2");
 assertEquals(2, option.getValuesList().size());
 assertEquals("Value H1", option.getValue(0));
 assertEquals("Value H2", option.getValue(1));
 assertEquals("Option H with values H1 and H2", option.getDescription());
 }

 @Test
 public void testOptionWithType() {
 Option option = new Option("i", "Option I", false);
 option.setType(Integer.class);
 assertEquals(Integer.class, option.getType());
 }

 @Test
 public void testOptionCloning() {
 Option originalOption = new Option("j", "Option J", false);
 Option clonedOption = (Option)originalOption.clone();
 assertEquals(originalOption, clonedOption);
 }
}
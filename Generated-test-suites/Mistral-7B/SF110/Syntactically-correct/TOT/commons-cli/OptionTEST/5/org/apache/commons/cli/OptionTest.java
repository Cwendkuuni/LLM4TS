import org.apache.commons.cli.*;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class OptionTest {

 @Test
 public void testOptionCreationWithNoArguments() {
 Option option = new Option("a", "Option A");
 assertEquals("Option A", option.getDescription());
 assertFalse(option.hasArgs());
 assertFalse(option.hasOptionalArg());
 assertFalse(option.isRequired());
 assertNull(option.getValue());
 }

 @Test
 public void testOptionCreationWithOneArgument() {
 Option option = new Option("b", "Option B", true, "Argument for Option B");
 assertEquals("Argument for Option B", option.getArgName());
 assertEquals(1, option.getArgs());
 assertTrue(option.hasArg());
 assertTrue(option.hasOptionalArg());
 assertFalse(option.isRequired());
 assertNull(option.getValue());
 }

 @Test
 public void testOptionCreationWithLongOption() {
 Option option = new Option("c", "longOptionC", false, "Description for long option C");
 assertEquals("longOptionC", option.getLongOpt());
 assertFalse(option.hasArgs());
 assertFalse(option.hasOptionalArg());
 assertFalse(option.isRequired());
 assertNull(option.getValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testOptionCreationWithInvalidOption() {
 Option option = new Option(" ", "Invalid option");
 }

 @Test
 public void testOptionEquals() {
 Option option1 = new Option("a", "Option A");
 Option option2 = new Option("a", "Option A");
 Option option3 = new Option("b", "Option B");
 assertTrue(option1.equals(option1));
 assertTrue(option1.equals(option2));
 assertFalse(option1.equals(option3));
 }

 @Test
 public void testOptionHashCode() {
 Option option1 = new Option("a", "Option A");
 Option option2 = new Option("a", "Option A");
 Option option3 = new Option("b", "Option B");
 assertEquals(option1.hashCode(), option1.hashCode());
 assertEquals(option1.hashCode(), option2.hashCode());
 assertNotEquals(option1.hashCode(), option3.hashCode());
 }

 @Test
 public void testOptionToString() {
 Option option = new Option("d", "Option D", false, "Description for option D");
 assertEquals("[ option: d :: Option D ]", option.toString());
 }

 @Test
 public void testOptionClone() {
 Option option = new Option("e", "Option E", false, "Description for option E");
 Option clonedOption = option.clone();
 assertEquals(option.getOpt(), clonedOption.getOpt());
 assertEquals(option.getLongOpt(), clonedOption.getLongOpt());
 assertEquals(option.getArgName(), clonedOption.getArgName());
 assertEquals(option.getDescription(), clonedOption.getDescription());
 assertEquals(option.isRequired(), clonedOption.isRequired());
 assertEquals(option.hasOptionalArg(), clonedOption.hasOptionalArg());
 assertEquals(option.hasArg(), clonedOption.hasArg());
 assertEquals(option.getArgs(), clonedOption.getArgs());
 assertSame(option.getType(), clonedOption.getType());
 assertSame(option.values, clonedOption.values);
 }

 @Test
 public void testOptionValueProcessing() {
 Option option = new Option("f", null, false, null, 3, String.class);
 option.setValueSeparator(';');
 option.addValueForProcessing("value1");
 option.addValueForProcessing("value2");
 option.addValueForProcessing("value3");
 assertEquals(3, option.getArgs());
 assertEquals("[value1, value2, value3]", option.toString());
 assertSame(option.getValuesList(), option.getValuesList());
 }

 @Test(expected = RuntimeException.class)
 public void testOptionValueProcessingWithInvalidNumberOfArguments() {
 Option option = new Option("g", null, false, null, 2, String.class);
 option.setValueSeparator(';');
 option.addValueForProcessing("value1");
 option.addValueForProcessing("value2");
 option.addValueForProcessing("value3");
 option.addValueForProcessing("value4");
 }
}
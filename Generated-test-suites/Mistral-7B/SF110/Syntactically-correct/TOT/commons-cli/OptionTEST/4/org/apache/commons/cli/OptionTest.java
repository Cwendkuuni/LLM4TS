import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionValidator;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionTest {

 @Test
 public void testOptionCreationWithNoArguments() {
 Option option = new Option("a", "option-a", false, "Description for option A");
 assertEquals("option-a", option.getOpt());
 assertEquals("option-a", option.getKey());
 assertFalse(option.hasArg());
 assertFalse(option.hasOptionalArg());
 assertFalse(option.hasLongOpt());
 assertEquals("Description for option A", option.getDescription());
 assertFalse(option.isRequired());
 assertNull(option.getValue());
 assertFalse(option.hasArgs());
 assertFalse(option.hasValueSeparator());
 assertSame(OptionValidator.validateOption("a"), option);
 }

 @Test
 public void testOptionCreationWithSingleArgument() {
 Option option = new Option("b", "option-b", true, "Description for option B");
 assertEquals("b", option.getOpt());
 assertEquals("option-b", option.getKey());
 assertTrue(option.hasArg());
 assertFalse(option.hasOptionalArg());
 assertFalse(option.hasLongOpt());
 assertEquals("Description for option B", option.getDescription());
 assertFalse(option.isRequired());
 assertNull(option.getValue());
 assertFalse(option.hasArgs());
 assertFalse(option.hasValueSeparator());
 }

 @Test
 public void testOptionCreationWithSingleArgumentAndDescription() {
 Option option = new Option("c", "option-c", true, "Description for option C with an argument");
 assertEquals("c", option.getOpt());
 assertEquals("option-c", option.getKey());
 assertTrue(option.hasArg());
 assertFalse(option.hasOptionalArg());
 assertFalse(option.hasLongOpt());
 assertEquals("Description for option C with an argument", option.getDescription());
 assertFalse(option.isRequired());
 assertNull(option.getValue());
 assertFalse(option.hasArgs());
 assertFalse(option.hasValueSeparator());
 }

 @Test
 public void testOptionCreationWithLongOptionName() {
 Option option = new Option("d", null, false, "Description for option D with a long option name");
 assertEquals("d", option.getOpt());
 assertNull(option.getLongOpt());
 assertFalse(option.hasArg());
 assertFalse(option.hasOptionalArg());
 assertFalse(option.hasLongOpt());
 assertEquals("Description for option D with a long option name", option.getDescription());
 assertFalse(option.isRequired());
 assertNull(option.getValue());
 assertFalse(option.hasArgs());
 assertFalse(option.hasValueSeparator());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidOptionCreation() {
 Option option = new Option("e", null, true, null);
 }

 @Test
 public void testOptionId() {
 Option optionA = new Option("a", "option-a", false, "Description for option A");
 Option optionB = new Option("b", "option-b", true, "Description for option B");
 Option optionC = new Option("c", "option-c", true, "Description for option C");
 assertEquals('a', optionA.getId());
 assertEquals('b', optionB.getId());
 assertEquals('c', optionC.getId());
 }

 @Test
 public void testOptionEquals() {
 Option optionA = new Option("a", "option-a", false, "Description for option A");
 Option optionB = new Option("a", "option-a", false, "Description for option A");
 Option optionC = new Option("b", "option-b", false, "Description for option B");
 assertTrue(optionA.equals(optionB));
 assertFalse(optionA.equals(optionC));
 }

 @Test
 public void testOptionToString() {
 Option option = new Option("t", "option-t", false, "Description for option T");
 assertEquals("[ option: t :: Description for option T ]", option.toString());
 }

 @Test
 public void testOptionClone() {
 Option option = new Option("u", "option-u", true, "Description for option U");
 Option clonedOption = option.clone();
 assertEquals(option, clonedOption);
 }
}
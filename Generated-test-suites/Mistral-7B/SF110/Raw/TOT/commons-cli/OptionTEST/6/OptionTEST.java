import org.apache.commons.cli.Option;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionTest {

 @Test
 public void testOptionCreation() throws IllegalArgumentException {
 Option option = new Option("a", "option-a", false, "This is option A.");
 assertEquals("a", option.getOpt());
 assertEquals("option-a", option.getLongOpt());
 assertFalse(option.hasArg());
 assertEquals("This is option A.", option.getDescription());
 assertFalse(option.isRequired());
 assertNull(option.getValue());
 assertFalse(option.hasValueSeparator());
 }

 @Test
 public void testOptionCreationWithArg() throws IllegalArgumentException {
 Option option = new Option("b", "option-b", true, "This is option B.");
 assertEquals("b", option.getOpt());
 assertEquals("option-b", option.getLongOpt());
 assertTrue(option.hasArg());
 assertEquals(1, option.getArgs());
 assertEquals("This is option B.", option.getDescription());
 assertFalse(option.isRequired());
 assertNull(option.getValue());
 assertFalse(option.hasValueSeparator());
 }

 @Test
 public void testOptionCreationWithLongOptAndArg() throws IllegalArgumentException {
 Option option = new Option("c", null, true, "This is long option C with arg.", "argname");
 assertEquals("c", option.getOpt());
 assertEquals("C", option.getLongOpt());
 assertTrue(option.hasArg());
 assertEquals("argname", option.getArgName());
 assertEquals("This is long option C with arg.", option.getDescription());
 assertFalse(option.isRequired());
 assertNull(option.getValue());
 assertFalse(option.hasValueSeparator());
 }

 @Test
 public void testOptionCreationWithValueSeparator() throws IllegalArgumentException {
 Option option = new Option("d", null, false, "Option D with value separator.", "value1,value2");
 assertEquals("d", option.getOpt());
 assertEquals(null, option.getLongOpt());
 assertFalse(option.hasArg());
 assertEquals("Option D with value separator.", option.getDescription());
 assertFalse(option.isRequired());
 assertNull(option.getValue());
 assertTrue(option.hasValueSeparator());
 assertEquals(',', option.getValueSeparator());
 }

 @Test
 public void testOptionParsingWithValidValues() throws ParseException {
 Option option = new Option("e", null, false, "Option E.");
 option.setType(String.class);
 List expectedValues = new ArrayList();
 expectedValues.add("value1");
 expectedValues.add("value2");
 option.parseArgument("--e=value1,value2");
 assertEquals(expectedValues, option.getValuesList());
 }

 @Test(expected = ParseException.class)
 public void testOptionParsingWithInvalidNumberOfArguments() throws ParseException {
 Option option = new Option("f", null, true, "Option F.");
 option.setType(String.class);
 option.parseArgument("--f");
 }

 @Test(expected = ParseException.class)
 public void testOptionParsingWithInvalidValueSeparator() throws ParseException {
 Option option = new Option("g", null, false, "Option G.");
 option.setType(String.class);
 option.setValueSeparator('%');
 option.parseArgument("--g=value1%value2");
 }

 @Test
 public void testOptionToString() {
 Option option = new Option("h", null, false, "Option H.");
 option.setArgName("ARG");
 option.setArgs(2);
 option.setValueSeparator(':');
 assertEquals("[ option: h [ARG1]:[ARG2] :: Option H. ]", option.toString());
 }

}
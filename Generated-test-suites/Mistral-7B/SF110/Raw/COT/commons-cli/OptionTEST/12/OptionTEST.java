import org.apache.commons.cli.Option;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionTest {

 private Option createOption(String opt) {
 return new Option(opt, null, false, "Test Option");
 }

 @Test
 public void testOptionConstructor() {
 Option option = createOption("t");
 assertEquals("Test Option", option.getDescription());
 assertNull(option.getLongOpt());
 assertFalse(option.hasOptionalArg());
 assertFalse(option.hasLongOpt());
 assertFalse(option.hasArg());
 }

 @Test
 public void testOptionConstructorWithLongOpt() {
 Option option = createOption("--test", "Test Option with Long Opt");
 assertEquals("Test Option with Long Opt", option.getDescription());
 assertEquals("--test", option.getLongOpt());
 assertFalse(option.hasOptionalArg());
 assertTrue(option.hasLongOpt());
 assertFalse(option.hasArg());
 }

 @Test
 public void testOptionConstructorWithArg() {
 Option option = createOption("t", true, "Test Option with Arg");
 assertEquals("Test Option with Arg", option.getDescription());
 assertEquals(1, option.getArgs());
 assertTrue(option.hasOptionalArg());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidOptionName() {
 try {
 new Option("", "Test Option");
 fail("Should throw IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidOptionNameWithLongOpt() {
 try {
 new Option("--", "Test Option");
 fail("Should throw IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testGetId() {
 Option option = new Option("t", "Test Option");
 assertEquals('t', option.getId());
 }

 @Test
 public void testGetKey() {
 Option option = new Option("t", null, false, "Test Option");
 assertEquals("t", option.getKey());
 Option optionWithLongOpt = new Option("--test", "Test Option", false, "Test Option with Long Opt");
 assertEquals("--test", optionWithLongOpt.getKey());
 }

 @Test
 public void testGetOpt() {
 Option option = new Option("t", "Test Option");
 assertEquals("t", option.getOpt());
 }

 @Test
 public void testGetType() {
 Option option = new Option("t", "Test Option");
 assertNull(option.getType());
 }

 @Test
 public void testSetType() {
 Option option = new Option("t", "Test Option");
 option.setType(Integer.class);
 assertEquals(Integer.class, option.getType());
 }

 @Test
 public void testGetLongOpt() {
 Option option = new Option("t", null, false, "Test Option");
 assertNull(option.getLongOpt());
 Option optionWithLongOpt = new Option("--test", "Test Option with Long Opt", false, "Test Option with Long Opt");
 assertEquals("--test", optionWithLongOpt.getLongOpt());
 }

 @Test
 public void testSetLongOpt() {
 Option option = new Option("t", null, false, "Test Option");
 option.setLongOpt("--test-long");
 assertEquals("--test-long", option.getLongOpt());
 }

 @Test
 public void testSetOptionalArg() {
 Option option = new Option("t", null, false, "Test Option");
 option.setOptionalArg(true);
 assertTrue(option.hasOptionalArg());
 }

 @Test
 public void testHasOptionalArg() {
 Option option = new Option("t", null, false, "Test Option");
 assertFalse(option.hasOptionalArg());
 Option optionWithArg = new Option("t", null, true, "Test Option with Arg");
 assertTrue(optionWithArg.hasOptionalArg());
 }

 @Test
 public void testHasLongOpt() {
 Option option = new Option("t", null, false, "Test Option");
 assertFalse(option.hasLongOpt());
 Option optionWithLongOpt = new Option("--test", "Test Option with Long Opt", false, "Test Option with Long Opt");
 assertTrue(optionWithLongOpt.hasLongOpt());
 }

 @Test
 public void testHasArg() {
 Option option = new Option("t", null, false, "Test Option");
 assertFalse(option.hasArg());
 Option optionWithArg = new Option("t", null, true, "Test Option with Arg");
 assertTrue(optionWithArg.hasArgs());
 Option optionWithValueSeparator = new Option("t", null, false, "Test Option with Value Separator", ';');
 assertFalse(optionWithValueSeparator.hasArg());
 }

 @Test
 public void testGetDescription() {
 Option option = new Option("t", null, false, "Test Option");
 assertEquals("Test Option", option.getDescription());
 }

 @Test
 public void testSetDescription() {
 Option option = new Option("t", null, false, "Test Option");
 option.setDescription("Modified Test Option");
 assertEquals("Modified Test Option", option.getDescription());
 }

 @Test
 public void testIsRequired() {
 Option option = new Option("t", null, false, "Test Option");
 assertFalse(option.isRequired());
 Option optionRequired = new Option("t", null, false, "Test Option", true);
 assertTrue(optionRequired.isRequired());
 }

 @Test
 public void testSetRequired() {
 Option option = new Option("t", null, false, "Test Option");
 option.setRequired(true);
 assertTrue(option.isRequired());
 }

 @Test
 public void testSetArgName() {
 Option option = new Option("t", null, false, "Test Option");
 option.setArgName("argName");
 assertEquals("argName", option.getArgName());
 }

 @Test
 public void testGetArgName() {
 Option option = new Option("t", null, false, "Test Option");
 assertNull(option.getArgName());
 Option optionWithArgName = new Option("t", null, false, "Test Option", null, null, "argName");
 assertEquals("argName", optionWithArgName.getArgName());
 }

 @Test
 public void testHasArgName() {
 Option option = new Option("t", null, false, "Test Option");
 assertFalse(option.hasArgName());
 Option optionWithArgName = new Option("t", null, false, "Test Option", null, null, "argName");
 assertTrue(optionWithArgName.hasArgName());
 }

 @Test
 public void testHasArgs() {
 Option option = new Option("t", null, false, "Test Option");
 assertFalse(option.hasArgs());
 Option optionWithArgs = new Option("t", null, true, "Test Option with Args");
 assertTrue(optionWithArgs.hasArgs());
 }

 @Test
 public void testSetArgs() {
 Option option = new Option("t", null, false, "Test Option");
 option.setArgs(2);
 assertEquals(2, option.getArgs());
 }

 @Test
 public void testSetValueSeparator() {
 Option option = new Option("t", null, false, "Test Option");
 option.setValueSeparator(';');
 assertEquals(';', option.getValueSeparator());
 }

 @Test
 public void testGetValueSeparator() {
 Option option = new Option("t", null, false, "Test Option");
 assertFalse(option.hasValueSeparator());
 Option optionWithValueSeparator = new Option("t", null, false, "Test Option with Value Separator", ',');
 assertTrue(optionWithValueSeparator.hasValueSeparator());
 assertEquals(',', optionWithValueSeparator.getValueSeparator());
 }

 @Test
 public void testAddValueForProcessing() throws Exception {
 Option option = new Option("t", null, false, "Test Option");
 option.setValueSeparator(',');
 option.addValueForProcessing("value1");
 option.addValueForProcessing("value2");
 List<String> expectedValues = new ArrayList<>();
 expectedValues.add("value1");
 expectedValues.add("value2");
 assertEquals(expectedValues, option.getValuesList());
 }

 @Test(expected = RuntimeException.class)
 public void testAddValueForProcessingWithNoArgsAllowed() throws Exception {
 Option option = new Option("t", null, false, "Test Option");
 option.setValueSeparator(',');
 option.addValueForProcessing("value");
 fail("Should throw RuntimeException: NO_ARGS_ALLOWED");
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetValueWithInvalidIndex() {
 Option option = new Option("t", null, false, "Test Option");
 option.setValueSeparator(',');
 option.addValueForProcessing("value1");
 option.addValueForProcessing("value2");
 try {
 option.getValue(2);
 fail("Should throw IndexOutOfBoundsException");
 } catch (IndexOutOfBoundsException e) {
 // expected
 }
 }

 // Test toString() method
 @Test
 public void testToString() {
 Option option = new Option("t", null, false, "Test Option");
 assertEquals("[ option: t :: Test Option ]", option.toString());
 Option optionWithLongOpt = new Option("--test", "Test Option with Long Opt", false, "Test Option with Long Opt");
 assertEquals("[ option: --test :: Test Option with Long Opt ]", optionWithLongOpt.toString());
 Option optionWithArg = new Option("t", null, true, "Test Option with Arg");
 assertEquals("[ option: t [ARG] :: Test Option with Arg ]", optionWithArg.toString());
 Option optionWithValueSeparator = new Option("t", null, false, "Test Option with Value Separator", ',');
 assertEquals("[ option: t [ARG1, ARG2, ...] :: Test Option with Value Separator ]", optionWithValueSeparator.toString());
 }

 // Test equals() method
 @Test
 public void testEquals() {
 Option option1 = new Option("t", null, false, "Test Option 1");
 Option option2 = new Option("t", null, false, "Test Option 1");
 assertTrue(option1.equals(option2));
 Option option3 = new Option("--test", null, false, "Test Option 3");
 assertFalse(option1.equals(option3));
 }

 // Test hashCode() method
 @Test
 public void testHashCode() {
 Option option1 = new Option("t", null, false, "Test Option 1");
 Option option2 = new Option("t", null, false, "Test Option 1");
 assertEquals(option1.hashCode(), option2.hashCode());
 Option option3 = new Option("--test", null, false, "Test Option 3");
 assertNotEquals(option1.hashCode(), option3.hashCode());
 }

 // Test clone() method
 @Test
 public void testClone() throws CloneNotSupportedException {
 Option option = new Option("t", null, false, "Test Option");
 Option clonedOption = (Option)option.clone();
 assertEquals(option, clonedOption);
 }
}
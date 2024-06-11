import org.apache.commons.cli.Option;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionTest {

 private Option createOptionWithArgs(String opt, String longOpt, boolean hasArg, String description) {
 return new Option(opt, longOpt, hasArg, description);
 }

 @Test
 public void testOptionConstructorWithOptAndDescription() {
 Option option = createOptionWithArgs("a", null, false, "Test Option A");
 assertEquals("Test Option A", option.getDescription());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testOptionConstructorWithInvalidOpt() {
 Option option = new Option(null, null, false, "Test Option");
 fail("Should throw IllegalArgumentException with null opt");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testOptionConstructorWithInvalidLongOpt() {
 Option option = new Option("a", null, false, "Test Option");
 fail("Should throw IllegalArgumentException with null longOpt");
 }

 @Test
 public void testOptionGetterMethods() throws IllegalArgumentException {
 Option option = createOptionWithArgs("a", "long-a", true, "Test Option A [ARG1]");
 assertEquals("a", option.getOpt());
 assertEquals("long-a", option.getLongOpt());
 assertEquals(true, option.hasArg());
 assertEquals("Test Option A [ARG1]", option.getDescription());
 }

 @Test
 public void testOptionIdGetter() {
 Option option = new Option("a", null, false, "Test Option A");
 assertEquals('a', option.getId());
 }

 @Test
 public void testOptionKeyGetter() {
 Option option = new Option("a", null, false, "Test Option A");
 assertEquals("a", option.getKey());
 }

 @Test
 public void testOptionHasLongOptGetter() {
 Option option = new Option(null, "long-a", false, "Test Option A");
 assertEquals(true, option.hasLongOpt());
 }

 @Test
 public void testOptionHasArgGetter() {
 Option option = new Option("a", null, true, "Test Option A [ARG1]");
 assertEquals(true, option.hasArg());
 }

 @Test
 public void testOptionHasArgNameGetter() {
 Option option = new Option("a", null, false, "Test Option A [ARG1]", "arg1");
 assertEquals("arg1", option.getArgName());
 }

 @Test
 public void testOptionHasArgsGetter() {
 Option option = new Option("a", null, true, "Test Option A [ARG1]");
 assertEquals(true, option.hasArgs());
 }

 @Test
 public void testOptionIsRequiredGetter() {
 Option option = new Option("a", null, false, "Test Option A [ARG1]");
 assertEquals(false, option.isRequired());
 }

 @Test
 public void testOptionSetRequired() {
 Option option = new Option("a", null, false, "Test Option A [ARG1]");
 option.setRequired(true);
 assertEquals(true, option.isRequired());
 }

 @Test
 public void testOptionSetDescription() {
 Option option = new Option("a", null, false, "Test Option A [ARG1]");
 option.setDescription("Updated Test Option A [ARG1]");
 assertEquals("Updated Test Option A [ARG1]", option.getDescription());
 }

 @Test
 public void testOptionSetArgName() {
 Option option = new Option("a", null, false, "Test Option A [ARG1]", "arg1");
 option.setArgName("arg2");
 assertEquals("arg2", option.getArgName());
 }

 @Test
 public void testOptionSetArgs() {
 Option option = new Option("a", null, false, "Test Option A [ARG1]", "arg1");
 option.setArgs(2);
 assertEquals(2, option.getArgs());
 }

 @Test
 public void testOptionSetValueSeparator() {
 Option option = new Option("a", null, false, "Test Option A [ARG1]");
 option.setValueSeparator(';');
 assertEquals(';', option.getValueSeparator());
 }

 @Test
 public void testOptionToString() {
 Option option = new Option("a", null, false, "Test Option A [ARG1]");
 assertEquals("[ option: a :: Test Option A [ARG1] ]", option.toString());
 }

 @Test
 public void testOptionClearValues() {
 Option option = new Option("a", null, false, "Test Option A [ARG1]");
 option.addValue("value1");
 option.addValue("value2");
 option.clearValues();
 assertFalse(option.hasValues());
 }

 @Test(expected = RuntimeException.class)
 public void testAddValueWithNoArgsAllowed() {
 Option option = new Option("a", null, false, "Test Option A");
 option.addValue("value");
 fail("Should throw RuntimeException: NO_ARGS_ALLOWED");
 }

 @Test
 public void testAddValue() {
 Option option = new Option("a", null, true, "Test Option A [ARG1]");
 option.addValue("value1");
 option.addValue("value2");
 assertEquals(2, option.getValues().size());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetValueWithInvalidIndex() {
 Option option = new Option("a", null, true, "Test Option A [ARG1]");
 option.addValue("value1");
 option.addValue("value2");
 option.addValue("value3");
 assertEquals("value2", option.getValue(1));
 try {
 option.getValue(3);
 fail();
 } catch (IndexOutOfBoundsException e) {
 // Expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetTypeWithInvalidType() {
 Option option = new Option("a", null, false, "Test Option A [ARG1]");
 option.setType(new Integer(1));
 fail("Should throw IllegalArgumentException: Invalid type class for Option");
 }

 @Test
 public void testSetType() {
 Option option = new Option("a", null, false, "Test Option A [ARG1]");
 option.setType("String");
 assertEquals("String", option.getType().getClass().getName());
 }

 @Test
 public void testOptionClone() throws CloneNotSupportedException {
 Option originalOption = new Option("a", null, false, "Test Option A [ARG1]");
 Option clonedOption = originalOption.clone();
 assertEquals(originalOption, clonedOption);
 }
}
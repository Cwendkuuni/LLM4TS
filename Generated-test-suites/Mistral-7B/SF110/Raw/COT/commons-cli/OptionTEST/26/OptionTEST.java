import org.apache.commons.cli.Option;
import org.junit.Assert;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

public class OptionTest {

 @Test
 public void testOptionConstructionWithRequiredOption() {
 Option option = new Option("-o", "Option with required argument", true);
 Assert.assertEquals("-o", option.getOpt());
 Assert.assertNull(option.getLongOpt());
 Assert.assertEquals("Option with required argument", option.getDescription());
 Assert.assertTrue(option.isRequired());
 Assert.assertFalse(option.hasOptionalArg());
 Assert.assertFalse(option.hasLongOpt());
 Assert.assertFalse(option.hasArg());
 Assert.assertNull(option.getArgName());
 Assert.assertFalse(option.hasArgs());
 Assert.assertEquals(-1, option.getArgs());
 Assert.assertFalse(option.hasValueSeparator());
 }

 @Test
 public void testOptionConstructionWithOptionalOption() {
 Option option = new Option("-o", "Option with optional argument", false);
 Assert.assertEquals("-o", option.getOpt());
 Assert.assertNull(option.getLongOpt());
 Assert.assertEquals("Option with optional argument", option.getDescription());
 Assert.assertFalse(option.isRequired());
 Assert.assertTrue(option.hasOptionalArg());
 Assert.assertFalse(option.hasLongOpt());
 Assert.assertFalse(option.hasArg());
 Assert.assertNull(option.getArgName());
 Assert.assertFalse(option.hasArgs());
 Assert.assertEquals(-1, option.getArgs());
 Assert.assertFalse(option.hasValueSeparator());
 }

 @Test
 public void testOptionConstructionWithLongOption() {
 Option option = new Option("--longOption", "Long option with optional argument", false);
 Assert.assertNull(option.getOpt());
 Assert.assertEquals("--longOption", option.getLongOpt());
 Assert.assertEquals("Long option with optional argument", option.getDescription());
 Assert.assertFalse(option.isRequired());
 Assert.assertTrue(option.hasOptionalArg());
 Assert.assertTrue(option.hasLongOpt());
 Assert.assertFalse(option.hasArg());
 Assert.assertNull(option.getArgName());
 Assert.assertFalse(option.hasArgs());
 Assert.assertEquals(-1, option.getArgs());
 Assert.assertFalse(option.hasValueSeparator());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidOptionConstruction() {
 Option option = new Option("-", "Invalid option", false);
 }

 @Test
 public void testOptionSettersAndGetters() {
 Option option = new Option("-o", "Option with optional argument", false);
 option.setLongOpt("--longOption");
 option.setArgName("arg");
 option.setDescription("New option description");
 option.setRequired(true);
 option.setOptionalArg(false);
 option.setArgs(3);
 option.setType(Integer.class);
 option.setHasValueSeparator(true);
 Assert.assertEquals("-o", option.getOpt());
 Assert.assertEquals("--longOption", option.getLongOpt());
 Assert.assertEquals("arg", option.getArgName());
 Assert.assertEquals("New option description", option.getDescription());
 Assert.assertTrue(option.isRequired());
 Assert.assertFalse(option.hasOptionalArg());
 Assert.assertTrue(option.hasLongOpt());
 Assert.assertFalse(option.hasArg());
 Assert.assertNull(option.getValue());
 Assert.assertTrue(option.hasArgs());
 Assert.assertEquals(3, option.getArgs());
 Assert.assertEquals(Integer.class, option.getType());
 Assert.assertTrue(option.hasValueSeparator());
 }

 @Test
 public void testOptionEqualsAndHashCode() {
 Option option1 = new Option("-o", "Option 1", false);
 Option option2 = new Option("-o", "Option 1", false);
 Assert.assertEquals(option1, option2);
 Assert.assertEquals(option1.hashCode(), option2.hashCode());
 Option option3 = new Option("-p", "Option 2", false);
 Assert.assertNotEquals(option1, option3);
 Assert.assertNotEquals(option1.hashCode(), option3.hashCode());
 }

 @Test
 public void testOptionClone() {
 Option option = new Option("-o", "Option with optional argument", false);
 Option clonedOption = option.clone();
 Assert.assertEquals(option, clonedOption);
 }

 @Test
 public void testOptionToString() {
 Option option = new Option("-o", "Option with optional argument", false);
 Assert.assertEquals("[ option: -o  [ARG] :: Option with optional argument ]", option.toString());
 }

 @Test
 public void testAddValueForProcessing() {
 Option option = new Option("-o", "Option with optional argument", false);
 option.setValueSeparator('=');
 option.addValueForProcessing("value1=value2");
 List<String> expectedValues = new ArrayList<>();
 expectedValues.add("value1");
 expectedValues.add("value2");
 Assert.assertEquals(expectedValues, option.getValuesList());
 }

 @Test(expected = RuntimeException.class)
 public void testAddValueForProcessingWithNoArgsAllowed() {
 Option option = new Option("-o", "Option with no arguments allowed", -1);
 option.setValueSeparator('=');
 option.addValueForProcessing("value1=value2");
 }

 @Test
 public void testAddValueForProcessingWithMultipleValues() {
 Option option = new Option("-o", "Option with optional multiple values", -2);
 option.setValueSeparator('=');
 option.addValueForProcessing("value1=value2=value3");
 List<String> expectedValues = new ArrayList<>();
 expectedValues.add("value1");
 expectedValues.add("value2");
 expectedValues.add("value3");
 Assert.assertEquals(expectedValues, option.getValuesList());
 }

 @Test
 public void testAddValueForProcessingWithEmptyValue() {
 Option option = new Option("-o", "Option with optional argument", false);
 option.setValueSeparator('=');
 option.addValueForProcessing("=value");
 List<String> expectedValues = new ArrayList<>();
 expectedValues.add("value");
 Assert.assertEquals(expectedValues, option.getValuesList());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetValueOutOfBounds() {
 Option option = new Option("-o", "Option with optional argument", false);
 option.setValue("value");
 option.addValue("anotherValue");
 Assert.assertEquals("value", option.getValue());
 Assert.assertEquals("anotherValue", option.getValue(1));
 Assert.assertThrows(IndexOutOfBoundsException.class, () -> option.getValue(2));
 }

 @Test
 public void testGetValueWithDefaultValue() {
 Option option = new Option("-o", "Option with optional argument", false);
 Assert.assertNull(option.getValue());
 Assert.assertEquals("defaultValue", option.getValue("defaultValue"));
 }

 @Test
 public void testGetValues() {
 Option option = new Option("-o", "Option with optional multiple values", -2);
 option.setValueSeparator('=');
 option.addValueForProcessing("value1=value2=value3");
 Assert.assertArrayEquals(new String[]{"value1", "value2", "value3"}, option.getValues());
 }
}
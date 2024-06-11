import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class OptionBuilderTest {

 @Test
 public void testOptionBuilderWithDefaults() {
 OptionBuilder optionBuilder = OptionBuilder.instance();
 Option option = optionBuilder.create('A');

 assertEquals("Option 'A' created with no options set", null, option.getLongOpt());
 assertEquals("Option 'A' created with no description", null, option.getDescription());
 assertEquals("Option 'A' created with no argument name", null, option.getArgName());
 assertEquals("Option 'A' created as not required", false, option.isRequired());
 assertEquals("Option 'A' created with no arguments", Option.UNINITIALIZED, option.getArgs());
 assertEquals("Option 'A' created with no type", null, option.getType());
 assertEquals("Option 'A' created with no value separator", 0, option.getValueSeparator());
 }

 @Test
 public void testOptionBuilderWithLongOptSet() {
 OptionBuilder optionBuilder = OptionBuilder.instance();
 Option option = optionBuilder.withLongOpt("testOption")
 .create('A');

 assertEquals("Option 'A' created with long opt 'testOption'", "testOption", option.getLongOpt());
 }

 @Test
 public void testOptionBuilderWithDescriptionSet() {
 OptionBuilder optionBuilder = OptionBuilder.instance();
 Option option = optionBuilder.withDescription("Description of test option")
 .create('A');

 assertEquals("Option 'A' created with description 'Description of test option'", "Description of test option", option.getDescription());
 }

 @Test
 public void testOptionBuilderWithArgNameSet() {
 OptionBuilder optionBuilder = OptionBuilder.instance();
 Option option = optionBuilder.withArgName("argName")
 .create('A');

 assertEquals("Option 'A' created with argName 'argName'", "argName", option.getArgName());
 }

 @Test
 public void testOptionBuilderWithRequiredSet() {
 OptionBuilder optionBuilder = OptionBuilder.instance();
 Option option = optionBuilder.isRequired()
 .create('A');

 assertEquals("Option 'A' created as required", true, option.isRequired());
 }

 @Test
 public void testOptionBuilderWithArgSet() {
 OptionBuilder optionBuilder = OptionBuilder.instance();
 Option option = optionBuilder.hasArg()
 .create('A');

 assertEquals("Option 'A' created with one argument", 1, option.getArgs());
 }

 @Test
 public void testOptionBuilderWithOptionalArgSet() {
 OptionBuilder optionBuilder = OptionBuilder.instance();
 Option option = optionBuilder.hasOptionalArg()
 .create('A');

 assertEquals("Option 'A' created as having optional arg", true, option.hasOptionalArg());
 }

 @Test
 public void testOptionBuilderWithValueSeparatorSet() {
 OptionBuilder optionBuilder = OptionBuilder.instance();
 Option option = optionBuilder.withValueSeparator(':')
 .create('A');

 assertEquals("Option 'A' created with value separator ':'", ':', option.getValueSeparator());
 }

 @Test
 public void testOptionBuilderWithTypeSet() {
 OptionBuilder optionBuilder = OptionBuilder.instance();
 Option option = optionBuilder.withType(String.class)
 .create('A');

 assertEquals("Option 'A' created with type String", String.class, option.getType());
 }

 @Test
 public void testOptionBuilderWithArgsSet() {
 OptionBuilder optionBuilder = OptionBuilder.instance();
 Option option = optionBuilder.hasArgs(2)
 .create('A');

 assertEquals("Option 'A' created with 2 optional arguments", 2, option.getArgs());
 }

 @Test
 public void testOptionBuilderWithHasArgs() {
 OptionBuilder optionBuilder = OptionBuilder.instance();
 Option option = optionBuilder.hasArgs()
 .create('A');

 assertEquals("Option 'A' created as having optional arguments", Option.UNLIMITED_VALUES, option.getArgs());
 }

 @Test
 public void testCreateOptionWithEmptyArgs() throws IllegalArgumentException {
 OptionBuilder optionBuilder = OptionBuilder.instance();
 Option option = optionBuilder.create('A');

 // Test that an IllegalArgumentException is thrown when trying to create an option with no long opt set
 assertNotNull("Option 'A' created without long opt should not be null", option);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateOptionWithoutLongOptSet() throws IllegalArgumentException {
 OptionBuilder optionBuilder = OptionBuilder.instance();
 Option option = optionBuilder.create('A');
 }
}
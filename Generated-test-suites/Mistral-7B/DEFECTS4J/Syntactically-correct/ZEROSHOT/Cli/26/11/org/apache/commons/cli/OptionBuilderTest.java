import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.junit.Assert;
import org.junit.Test;

public class OptionBuilderTest {

 @Test
 public void testOptionBuilderWithDefaultValues() {
 Options options = new Options();
 Option option = OptionBuilder.create('o');
 options.addOption(option);

 Assert.assertEquals("Option created with default values", option, options.getOption('o'));
 Assert.assertNull(option.getLongOpt());
 Assert.assertNull(option.getDescription());
 Assert.assertNull(option.getArgName());
 Assert.assertFalse(option.isRequired());
 Assert.assertEquals(Option.UNINITIALIZED, option.getArgCount());
 Assert.assertNull(option.getType());
 Assert.assertFalse(option.hasOptionalArg());
 Assert.assertEquals('\0', option.getValueSeparator());
 }

 @Test
 public void testOptionBuilderWithLongOpt() {
 Options options = new Options();
 Option option = OptionBuilder.withLongOpt("optionA")
 .create('o');
 options.addOption(option);

 Assert.assertEquals("Option created with specified long opt", option.getLongOpt(), "optionA");
 Assert.assertEquals("Option description not set", option.getDescription(), "");
 // other assertions as in the previous test
 }

 @Test
 public void testOptionBuilderWithDescription() {
 Options options = new Options();
 Option option = OptionBuilder.withDescription("Description for option A")
 .create('o');
 options.addOption(option);

 Assert.assertEquals("Option created with specified description", option.getDescription(), "Description for option A");
 // other assertions as in the previous tests
 }

 @Test
 public void testOptionBuilderWithArgName() {
 Options options = new Options();
 Option option = OptionBuilder.withArgName("argName")
 .create('o');
 options.addOption(option);

 Assert.assertEquals("Option created with specified argName", option.getArgName(), "argName");
 // other assertions as in the previous tests
 }

 @Test
 public void testOptionBuilderIsRequired() {
 Options options = new Options();
 Option option = OptionBuilder.isRequired()
 .create('o');
 options.addOption(option);

 Assert.assertTrue("Option created as required", option.isRequired());
 // other assertions as in the previous tests
 }

 @Test
 public void testOptionBuilderHasArg() {
 Options options = new Options();
 Option option = OptionBuilder.hasArg()
 .create('o');
 options.addOption(option);

 Assert.assertEquals(Option.UNINITIALIZED, option.getArgCount());
 // other assertions as in the previous tests
 }

 @Test
 public void testOptionBuilderHasOptionalArg() {
 Options options = new Options();
 Option option = OptionBuilder.hasOptionalArg()
 .create('o');
 options.addOption(option);

 Assert.assertTrue("Option created with optional arg", option.hasOptionalArg());
 // other assertions as in the previous tests
 }

 @Test
 public void testOptionBuilderWithValueSeparator() {
 Options options = new Options();
 Option option = OptionBuilder.withValueSeparator(':')
 .create('o');
 options.addOption(option);

 Assert.assertEquals(':', option.getValueSeparator());
 // other assertions as in the previous tests
 }

 @Test
 public void testOptionBuilderHasArgs() {
 Options options = new Options();
 Option option = OptionBuilder.hasArgs()
 .create('o');
 options.addOption(option);

 Assert.assertEquals(Option.UNLIMITED_VALUES, option.getArgCount());
 // other assertions as in the previous tests
 }

 @Test
 public void testOptionBuilderHasArgsWithLimit() {
 Options options = new Options();
 Option option = OptionBuilder.hasArgs(3)
 .create('o');
 options.addOption(option);

 Assert.assertEquals(3, option.getArgCount());
 // other assertions as in the previous tests
 }

 @Test
 public void testOptionBuilderWithType() {
 Options options = new Options();
 String typeString = "String";
 Option option = OptionBuilder.withType(typeString.getClass())
 .create('o');
 options.addOption(option);

 Assert.assertEquals(typeString.getClass(), option.getType());
 // other assertions as in the previous tests
 }
}
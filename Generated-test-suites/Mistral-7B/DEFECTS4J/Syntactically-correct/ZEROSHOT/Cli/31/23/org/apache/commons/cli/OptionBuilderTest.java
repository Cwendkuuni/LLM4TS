import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class OptionBuilderTest {

 @Test
 void testOptionBuilderWithDefaults() {
 Options options = new Options();
 Option option = OptionBuilder.create();
 options.addOption(option);

 Assertions.assertEquals(Option.UNINITIALIZED, option.getArgs());
 Assertions.assertNull(option.getArgName());
 Assertions.assertNull(option.getDescription());
 Assertions.assertNull(option.getLongOpt());
 Assertions.assertFalse(option.isRequired());
 Assertions.assertFalse(option.hasOptionalArg());
 assertNull(option.getType());
 }

 @Test
 void testOptionBuilderWithLongOpt() {
 Options options = new Options();
 Option option = OptionBuilder.withLongOpt("longOption")
 .create();
 options.addOption(option);

 Assertions.assertEquals(Option.UNINITIALIZED, option.getArgs());
 Assertions.assertNull(option.getArgName());
 Assertions.assertEquals("longOption", option.getLongOpt());
 Assertions.assertFalse(option.isRequired());
 Assertions.assertFalse(option.hasOptionalArg());
 assertNull(option.getType());
 }

 @Test
 void testOptionBuilderWithArg() {
 Options options = new Options();
 Option option = OptionBuilder.hasArg()
 .create();
 options.addOption(option);

 Assertions.assertEquals(1, option.getArgs());
 Assertions.assertEquals("arg", option.getArgName());
 Assertions.assertNull(option.getDescription());
 Assertions.assertNull(option.getLongOpt());
 Assertions.assertFalse(option.isRequired());
 Assertions.assertFalse(option.hasOptionalArg());
 assertNull(option.getType());
 }

 @Test
 void testOptionBuilderWithValueSeparator() {
 Options options = new Options();
 Option option = OptionBuilder.withValueSeparator(':')
 .create();
 options.addOption(option);

 Assertions.assertEquals(Option.UNINITIALIZED, option.getArgs());
 Assertions.assertEquals("arg", option.getArgName());
 Assertions.assertNull(option.getDescription());
 Assertions.assertNull(option.getLongOpt());
 Assertions.assertFalse(option.isRequired());
 Assertions.assertFalse(option.hasOptionalArg());
 Assertions.assertEquals(':', option.getValueSeparator());
 }

 @Test
 void testOptionBuilderWithDescription() {
 Options options = new Options();
 Option option = OptionBuilder.withDescription("description")
 .create();
 options.addOption(option);

 Assertions.assertEquals(Option.UNINITIALIZED, option.getArgs());
 Assertions.assertEquals("arg", option.getArgName());
 Assertions.assertEquals("description", option.getDescription());
 Assertions.assertNull(option.getLongOpt());
 Assertions.assertFalse(option.isRequired());
 Assertions.assertFalse(option.hasOptionalArg());
 assertNull(option.getType());
 }

 @Test
 void testOptionBuilderWithLongOptAndArg() {
 Options options = new Options();
 Option option = OptionBuilder.withLongOpt("longOption")
 .hasArg()
 .create();
 options.addOption(option);

 Assertions.assertEquals(1, option.getArgs());
 Assertions.assertEquals("arg", option.getArgName());
 Assertions.assertEquals("longOption", option.getLongOpt());
 Assertions.assertFalse(option.isRequired());
 Assertions.assertFalse(option.hasOptionalArg());
 assertNull(option.getType());
 }

 @Test
 void testOptionBuilderWithLongOptArgAndValueSeparator() {
 Options options = new Options();
 Option option = OptionBuilder.withLongOpt("longOption")
 .hasArg()
 .withValueSeparator('=')
 .create();
 options.addOption(option);

 Assertions.assertEquals(1, option.getArgs());
 Assertions.assertEquals("arg", option.getArgName());
 Assertions.assertEquals("longOption", option.getLongOpt());
 Assertions.assertFalse(option.isRequired());
 Assertions.assertFalse(option.hasOptionalArg());
 Assertions.assertEquals('=', option.getValueSeparator());
 }

 @Test
 void testOptionBuilderWithLongOptArgValueSeparatorRequired() {
 Options options = new Options();
 Option option = OptionBuilder.withLongOpt("longOption")
 .hasArg(true)
 .withValueSeparator('=')
 .isRequired()
 .create();
 options.addOption(option);

 Assertions.assertEquals(1, option.getArgs());
 Assertions.assertEquals("arg", option.getArgName());
 Assertions.assertEquals("longOption", option.getLongOpt());
 Assertions.assertTrue(option.isRequired());
 Assertions.assertFalse(option.hasOptionalArg());
 Assertions.assertEquals('=', option.getValueSeparator());
 }

 @Test
 void testOptionBuilderWithLongOptArgValueSeparatorOptional() {
 Options options = new Options();
 Option option = OptionBuilder.withLongOpt("longOption")
 .hasArg()
 .withValueSeparator('=')
 .hasOptionalArg()
 .create();
 options.addOption(option);

 Assertions.assertEquals(1, option.getArgs());
 Assertions.assertEquals("arg", option.getArgName());
 Assertions.assertEquals("longOption", option.getLongOpt());
 Assertions.assertFalse(option.isRequired());
 Assertions.assertTrue(option.hasOptionalArg());
 Assertions.assertEquals('=', option.getValueSeparator());
 }

 @Test
 void testOptionBuilderWithLongOptArgValueSeparatorOptionalAndType() {
 Options options = new Options();
 Option option = OptionBuilder.withLongOpt("longOption")
 .hasArg()
 .withValueSeparator('=')
 .hasOptionalArg()
 .withType(String.class)
 .create();
 options.addOption(option);

 Assertions.assertEquals(1, option.getArgs());
 Assertions.assertEquals("arg", option.getArgName());
 Assertions.assertEquals("longOption", option.getLongOpt());
 Assertions.assertFalse(option.isRequired());
 Assertions.assertTrue(option.hasOptionalArg());
 Assertions.assertEquals('=', option.getValueSeparator());
 Assertions.assertSame(String.class, option.getType());
 }
}
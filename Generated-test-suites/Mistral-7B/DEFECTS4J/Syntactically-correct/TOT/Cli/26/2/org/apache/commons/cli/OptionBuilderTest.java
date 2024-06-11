import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Assert;
import org.junit.Test;

public class OptionBuilderTest {

 @Test
 public void testOptionBuilderWithLongOptAndHasArg() {
 OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longOpt")
 .hasArg();
 Option option = optionBuilder.create('L');

 Assert.assertEquals("longOpt", option.getLongOpt());
 Assert.assertEquals(1, option.getArgs().length);
 Assert.assertNull(option.getValue());
 }

 @Test
 public void testOptionBuilderWithLongOptAndHasOptionalArg() {
 OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longOpt")
 .hasOptionalArg();
 Option option = optionBuilder.create('L');

 Assert.assertEquals("longOpt", option.getLongOpt());
 Assert.assertNull(option.getValue());
 Assert.assertTrue(option.isOptional());
 }

 @Test
 public void testOptionBuilderWithLongOptAndValueSeparator() {
 OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longOpt")
 .withValueSeparator('=');
 Option option = optionBuilder.create('L');

 Assert.assertEquals("longOpt", option.getLongOpt());
 Assert.assertEquals("", option.getDescription());
 Assert.assertEquals("", option.getArgName());
 Assert.assertEquals(1, option.getArgs().length);
 Assert.assertNull(option.getValue());
 }

 @Test
 public void testOptionBuilderWithLongOptAndValue() {
 OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longOpt")
 .hasArg("value");
 Option option = optionBuilder.create('L');

 Assert.assertEquals("longOpt", option.getLongOpt());
 Assert.assertEquals("value", option.getValue());
 Assert.assertNull(option.getDescription());
 Assert.assertEquals("value", option.getArgName());
 Assert.assertArrayEquals(new String[]{"value"}, option.getArgs());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testOptionBuilderWithMissingLongOpt() {
 OptionBuilder optionBuilder = OptionBuilder.hasArg();
 Option option = optionBuilder.create('L');
 }
}
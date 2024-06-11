import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {

 @Test
 public void testOptionBuilderWithLongOptAndHasArg() {
 OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longOpt")
 .hasArg();
 Option option = optionBuilder.create('l');

 assertEquals("longOpt", option.getLongOpt());
 assertTrue(option.hasArg());
 }

 @Test
 public void testOptionBuilderWithLongOptAndNoArg() {
 OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longOpt");
 Option option = optionBuilder.create('l');

 assertEquals("longOpt", option.getLongOpt());
 assertFalse(option.hasArg());
 }

 @Test
 public void testOptionBuilderWithLongOptAndHasOptionalArg() {
 OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longOpt")
 .hasOptionalArg();
 Option option = optionBuilder.create('l');

 assertEquals("longOpt", option.getLongOpt());
 assertTrue(option.hasOptionalArg());
 }

 @Test
 public void testOptionBuilderWithLongOptAndValueSeparator() {
 OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longOpt")
 .withValueSeparator(':');
 Option option = optionBuilder.create('l');

 assertEquals("longOpt", option.getLongOpt());
 assertEquals(':', option.getValueSeparator());
 }

 @Test
 public void testOptionBuilderWithLongOptAndDescription() {
 OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longOpt")
 .withDescription("This is a test option");
 Option option = optionBuilder.create('l');

 assertEquals("This is a test option", option.getDescription());
 assertEquals("longOpt", option.getLongOpt());
 }

 @Test
 public void testOptionBuilderWithLongOptAndHasArgs() {
 OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longOpt")
 .hasArgs();
 Option option = optionBuilder.create('l');

 assertEquals("longOpt", option.getLongOpt());
 assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
 }

 @Test
 public void testOptionBuilderWithLongOptAndHasArgsAndValue() {
 OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longOpt")
 .hasArgs(2)
 .withValue("value");
 Option option = optionBuilder.create('l');

 assertEquals("longOpt", option.getLongOpt());
 assertEquals("value", option.getValue());
 assertEquals(Option.VALUE_REQUIRED, option.getArgPosition());
 assertEquals(2, option.getArgs().length);
 }

 @Test
 public void testOptionBuilderWithLongOptAndType() {
 OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longOpt")
 .withType(Integer.class);
 Option option = optionBuilder.create('l');

 assertEquals("longOpt", option.getLongOpt());
 assertEquals(Integer.class, option.getType());
 }

 @Test
 public void testCreateOptionWithNoSettings() {
 OptionBuilder optionBuilder = OptionBuilder.instance();
 Option option = optionBuilder.create('l');

 assertNull(option.getLongOpt());
 assertFalse(option.isRequired());
 assertFalse(option.hasOptionalArg());
 assertNull(option.getDescription());
 assertEquals(Option.UNINITIALIZED, option.getArgs().length);
 assertNull(option.getType());
 }

 @Test
 public void testCreateOptionWithDefaultSettings() {
 OptionBuilder optionBuilder = OptionBuilder.instance();
 Option option = optionBuilder.create('l');

 assertNull(option.getLongOpt());
 assertFalse(option.isRequired());
 assertFalse(option.hasOptionalArg());
 assertNull(option.getDescription());
 assertEquals(Option.UNINITIALIZED, option.getArgs().length);
 assertNull(option.getType());
 }

 @Test
 public void testCreateOptionWithLongOptAndHasArg() {
 OptionBuilder optionBuilder = OptionBuilder.instance();
 Option option = optionBuilder.hasArg().create('l');

 assertEquals("arg", option.getArgName());
 assertFalse(option.isRequired());
 assertFalse(option.hasOptionalArg());
 assertEquals(1, option.getArgs().length);
 assertNull(option.getType());
 }

 @Test
 public void testCreateOptionWithLongOptAndHasArgAndDescription() {
 OptionBuilder optionBuilder = OptionBuilder.instance();
 Option option = optionBuilder.withDescription("Test option")
 .hasArg()
 .create('l');

 assertEquals("Test option", option.getDescription());
 assertEquals("arg", option.getArgName());
 assertFalse(option.isRequired());
 assertFalse(option.hasOptionalArg());
 assertEquals(1, option.getArgs().length);
 assertNull(option.getType());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateOptionWithNoLongOpt() {
 OptionBuilder optionBuilder = OptionBuilder.instance();
 Option option = optionBuilder.create('l');

 fail("Expected IllegalArgumentException");
 }
}
import org.apache.commons.cli.Option;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class OptionBuilderTest {

 @Test
 public void testOptionBuilderWithDefaults() {
 OptionBuilder optionBuilder = OptionBuilder.getInstance();
 Option option = optionBuilder.create('D');

 assertEquals("No description provided", null, option.getDescription());
 assertEquals("arg", option.getArgName());
 assertEquals("d", option.getOpt());
 assertNull(option.getLongOpt());
 assertFalse(option.isRequired());
 assertFalse(option.hasOptionalArg());
 assertFalse(option.hasArgs());
 assertNull(option.getType());
 assertNull(option.getValueSeparator());
 }

 @Test
 public void testOptionBuilderWithLongOpt() {
 OptionBuilder optionBuilder = OptionBuilder.getInstance();
 Option option = optionBuilder.withLongOpt("database-file")
 .create('D');

 assertEquals("database-file", option.getLongOpt());
 }

 @Test
 public void testOptionBuilderWithDescription() {
 OptionBuilder optionBuilder = OptionBuilder.getInstance();
 Option option = optionBuilder.withDescription("Database file to use")
 .create('D');

 assertEquals("Database file to use", option.getDescription());
 }

 @Test
 public void testOptionBuilderWithArgName() {
 OptionBuilder optionBuilder = OptionBuilder.getInstance();
 Option option = optionBuilder.withArgName("data")
 .create('D');

 assertEquals("data", option.getArgName());
 }

 @Test
 public void testOptionBuilderWithArgRequired() {
 OptionBuilder optionBuilder = OptionBuilder.getInstance();
 Option option = optionBuilder.isRequired()
 .create('D');

 assertTrue(option.isRequired());
 }

 @Test
 public void testOptionBuilderWithOptionalArg() {
 OptionBuilder optionBuilder = OptionBuilder.getInstance();
 Option option = optionBuilder.hasOptionalArg()
 .create('D');

 assertTrue(option.hasOptionalArg());
 }

 @Test
 public void testOptionBuilderWithValueSeparator() {
 OptionBuilder optionBuilder = OptionBuilder.getInstance();
 Option option = optionBuilder.withValueSeparator(':')
 .create('D');

 assertEquals(':', option.getValueSeparator());
 }

 @Test
 public void testOptionBuilderWithValueSeparatorEmpty() {
 OptionBuilder optionBuilder = OptionBuilder.getInstance();
 Option option = optionBuilder.withValueSeparator()
 .create('D');

 assertEquals('=', option.getValueSeparator());
 }

 @Test
 public void testOptionBuilderWithHasArgs() {
 OptionBuilder optionBuilder = OptionBuilder.getInstance();
 Option option = optionBuilder.hasArgs(2)
 .create('D');

 assertEquals(2, option.getArgs().length());
 }

 @Test
 public void testOptionBuilderWithHasArgsUnlimited() {
 OptionBuilder optionBuilder = OptionBuilder.getInstance();
 Option option = optionBuilder.hasArgs()
 .create('D');

 assertEquals(Option.UNLIMITED_VALUES, option.getArgs().length());
 }

 @Test
 public void testOptionBuilderWithType() {
 OptionBuilder optionBuilder = OptionBuilder.getInstance();
 Option option = optionBuilder.withType(String.class)
 .create('D');

 assertEquals(String.class, option.getType());
 }
}
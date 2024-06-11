import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {

 @Test
 public void testOptionBuilderWithLongOpt() {
 OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longOpt");
 Option option = optionBuilder.create('l');
 assertEquals("longOpt", option.getLongOpt());
 }

 @Test
 public void testOptionBuilderHasArg() {
 OptionBuilder optionBuilder = OptionBuilder.hasArg();
 Option option = optionBuilder.create('h');
 assertEquals(1, option.getArgs().length);
 }

 @Test
 public void testOptionBuilderHasOptionalArg() {
 OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
 Option option = optionBuilder.create('o');
 assertTrue(option.isOptional());
 }

 @Test
 public void testOptionBuilderWithArgName() {
 OptionBuilder optionBuilder = OptionBuilder.withArgName("argName");
 Option option = optionBuilder.create('a');
 assertEquals("argName", option.getArgName());
 }

 @Test
 public void testOptionBuilderIsRequired() {
 OptionBuilder optionBuilder = OptionBuilder.isRequired();
 Option option = optionBuilder.create('r');
 assertTrue(option.isRequired());
 }

 @Test
 public void testOptionBuilderWithValueSeparator() {
 OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(':');
 Option option = optionBuilder.create('v');
 assertEquals(':', option.getValueSeparator());
 }

 @Test
 public void testOptionBuilderHasArgs() {
 OptionBuilder optionBuilder = OptionBuilder.hasArgs();
 Option option = optionBuilder.create('g');
 assertEquals(Option.UNLIMITED_VALUES, option.getArgs().length);
 }

 @Test
 public void testOptionBuilderHasArgsWithSpecifiedNumber() {
 OptionBuilder optionBuilder = OptionBuilder.hasArgs(2);
 Option option = optionBuilder.create('x');
 assertEquals(2, option.getArgs().length);
 }

 @Test
 public void testOptionBuilderWithType() {
 OptionBuilder optionBuilder = OptionBuilder.withType(Integer.class);
 Option option = optionBuilder.create('i');
 assertEquals(Integer.class, option.getType());
 }

 @Test
 public void testOptionBuilderWithDescription() {
 OptionBuilder optionBuilder = OptionBuilder.withDescription("description");
 Option option = optionBuilder.create('d');
 assertEquals("description", option.getDescription());
 }

 @Test
 public void testOptionBuilderCreateWithoutLongOpt() {
 OptionBuilder optionBuilder = OptionBuilder.instance();
 try {
 Option option = optionBuilder.create('e');
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testOptionBuilderCreateWithoutDescription() {
 OptionBuilder optionBuilder = OptionBuilder.instance();
 optionBuilder.reset();
 Option option = optionBuilder.create('f');
 assertNull(option.getDescription());
 }
}
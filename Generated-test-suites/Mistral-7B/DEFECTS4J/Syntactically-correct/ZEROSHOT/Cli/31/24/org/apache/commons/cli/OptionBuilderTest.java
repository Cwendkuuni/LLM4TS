import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class OptionBuilderTest {

 @Test
 public void testOptionBuilderHasArg() {
 OptionBuilder optionBuilder = OptionBuilder.hasArg();
 Option option = optionBuilder.create('A');
 assertEquals(1, option.getArgPosition().numberOfArgs);
 }

 @Test
 public void testOptionBuilderHasOptionalArg() {
 OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
 Option option = optionBuilder.create('B');
 assertEquals(1, option.getArgPosition().numberOfArgs);
 assertTrue(option.isOptionalArg());
 }

 @Test
 public void testOptionBuilderHasArgs() {
 OptionBuilder optionBuilder = OptionBuilder.hasArgs(3);
 Option option = optionBuilder.create('C');
 assertEquals(3, option.getArgPosition().numberOfArgs);
 }

 @Test
 public void testOptionBuilderHasArgsUnlimited() {
 OptionBuilder optionBuilder = OptionBuilder.hasArgs();
 Option option = optionBuilder.create('D');
 assertEquals(Option.UNLIMITED_VALUES, option.getArgPosition().numberOfArgs);
 }

 @Test
 public void testOptionBuilderWithArgName() {
 OptionBuilder optionBuilder = OptionBuilder.withArgName("argName");
 Option option = optionBuilder.create('E');
 assertEquals("argName", option.getArgName());
 }

 @Test
 public void testOptionBuilderIsRequired() {
 OptionBuilder optionBuilder = OptionBuilder.isRequired();
 Option option = optionBuilder.create('F');
 assertTrue(option.isRequired());
 }

 @Test
 public void testOptionBuilderWithDescription() {
 OptionBuilder optionBuilder = OptionBuilder.withDescription("description");
 Option option = optionBuilder.create('G');
 assertEquals("description", option.getDescription());
 }

 @Test
 public void testOptionBuilderWithLongOpt() {
 OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longOpt");
 Option option = optionBuilder.create('H');
 assertEquals("longOpt", option.getLongOpt());
 }

 @Test
 public void testOptionBuilderWithType() {
 OptionBuilder optionBuilder = OptionBuilder.withType(String.class);
 Option option = optionBuilder.create('I');
 assertEquals(String.class, option.getType());
 }

 @Test
 public void testOptionBuilderWithValueSeparator() {
 OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(':');
 Option option = optionBuilder.create('J');
 assertEquals(':', option.getValueSeparator());
 }

 @Test
 public void testOptionBuilderReset() {
 OptionBuilder optionBuilder = OptionBuilder.hasArg();
 Option option = optionBuilder.create('K');
 OptionBuilder.reset();
 Option option2 = OptionBuilder.create('L');
 assertNull(option2.getDescription());
 assertFalse(option2.isRequired());
 assertEquals(Option.UNINITIALIZED, option2.getArgPosition().numberOfArgs);
 assertFalse(option2.isOptionalArg());
 assertNull(option2.getType());
 assertNull(option2.getValueSeparator());
 assertNull(option2.getArgName());
 }

 @Test
 public void testOptionBuilderCreateWithoutSettings() {
 OptionBuilder optionBuilder = OptionBuilder.instance();
 Option option = optionBuilder.create('M');
 assertNotNull(option);
 }
}
import org.apache.commons.cli.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {

 @Test
 public void testOptionBuilderWithLongOpt() {
 OptionBuilder optionBuilder = OptionBuilder.withLongOpt("optionName");
 Option expectedOption = OptionBuilder.create('o');
 assertEquals(expectedOption.getLongOpt(), optionBuilder.create('o').getLongOpt());
 }

 @Test
 public void testOptionBuilderHasArg() {
 OptionBuilder optionBuilder = OptionBuilder.hasArg();
 Option expectedOption = Option.builder().hasArg(true).create('a');
 assertEquals(expectedOption.getArgs().numberOfArgs(), optionBuilder.create('a').getArgs().numberOfArgs());
 }

 @Test
 public void testOptionBuilderHasArgs() {
 OptionBuilder optionBuilder = OptionBuilder.hasArgs();
 Option expectedOption = Option.builder().hasArgs(Option.UNLIMITED_VALUES).create('a');
 assertEquals(expectedOption.getArgs().numberOfArgs(), optionBuilder.create('a').getArgs().numberOfArgs());
 }

 @Test
 public void testOptionBuilderIsRequired() {
 OptionBuilder optionBuilder = OptionBuilder.isRequired();
 Option expectedOption = Option.builder().isRequired(true).create('r');
 assertTrue(expectedOption.isRequired());
 }

 @Test
 public void testOptionBuilderWithValueSeparator() {
 OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(':');
 Option expectedOption = Option.builder().withValueSeparator(':').create('D');
 assertEquals(expectedOption.getValueSeparator(), optionBuilder.create('D').getValueSeparator());
 }

 @Test
 public void testOptionBuilderHasOptionalArg() {
 OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
 Option expectedOption = Option.builder().hasOptionalArg(true).create('o');
 assertEquals(expectedOption.hasOptionalArg(), optionBuilder.create('o').hasOptionalArg());
 }

 @Test
 public void testOptionBuilderHasType() {
 OptionBuilder optionBuilder = OptionBuilder.withType(Integer.class);
 Option expectedOption = Option.builder().withType(Integer.class).create('i');
 assertEquals(expectedOption.getTypeClass(), optionBuilder.create('i').getTypeClass());
 }

 @Test
 public void testOptionBuilderWithDescription() {
 OptionBuilder optionBuilder = OptionBuilder.withDescription("Option description");
 Option expectedOption = Option.builder().withDescription("Option description").create('d');
 assertEquals(expectedOption.getDescription(), optionBuilder.create('d').getDescription());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testOptionBuilderWithoutLongOpt() {
 OptionBuilder optionBuilder = new OptionBuilder();
 optionBuilder.create('x');
 }
}
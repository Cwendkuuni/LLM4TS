import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class OptionBuilderTest {

 @Test
 public void testOptionBuilderWithDefaults() {
 OptionBuilder optionBuilder = new OptionBuilder();
 Option option = optionBuilder.create('D');

 assertEquals("No description provided", null, option.getDescription());
 assertEquals("arg", "arg", option.getArgName());
 assertEquals(0, option.getArgNumber());
 assertEquals(false, option.isRequired());
 assertEquals(Option.NO_ARGUMENTS, option.getArgs().getValue());
 assertEquals(null, option.getType());
 assertEquals(false, option.hasOptionalArg());
 }

 @Test
 public void testOptionBuilderWithLongOpt() {
 OptionBuilder optionBuilder = new OptionBuilder();
 Option option = optionBuilder.withLongOpt("dataDir")
 .create('D');

 assertEquals("dataDir", option.getLongOpt(), "Incorrect long opt value");
 }

 @Test
 public void testOptionBuilderHasArg() {
 OptionBuilder optionBuilder = new OptionBuilder();
 Option option = optionBuilder.hasArg()
 .create('D');

 assertEquals(Option.REQUIRED_ARGUMENT, option.getArgs().getValue());
 }

 @Test
 public void testOptionBuilderHasOptionalArg() {
 OptionBuilder optionBuilder = new OptionBuilder();
 Option option = optionBuilder.hasOptionalArg()
 .create('D');

 assertEquals(Option.OPTIONAL_ARGUMENT, option.getArgs().getValue());
 }

 @Test
 public void testOptionBuilderWithArgName() {
 OptionBuilder optionBuilder = new OptionBuilder();
 Option option = optionBuilder.withArgName("dir")
 .create('D');

 assertEquals("dir", option.getArgName(), "Incorrect arg name");
 }

 @Test
 public void testOptionBuilderIsRequired() {
 OptionBuilder optionBuilder = new OptionBuilder();
 Option option = optionBuilder.isRequired()
 .create('D');

 assertEquals(true, option.isRequired(), "Option should be required");
 }

 @Test
 public void testOptionBuilderWithValueSeparator() {
 OptionBuilder optionBuilder = new OptionBuilder();
 Option option = optionBuilder.withValueSeparator(':')
 .create('D');

 assertEquals(':', option.getValueSeparator(), "Incorrect value separator");
 }

 @Test
 public void testOptionBuilderHasArgs() {
 OptionBuilder optionBuilder = new OptionBuilder();
 Option option = optionBuilder.hasArgs()
 .create('D');

 assertEquals(Option.UNLIMITED_VALUES, option.getArgs().getValue());
 }

 @Test
 public void testOptionBuilderHasArgsWithLimit() {
 OptionBuilder optionBuilder = new OptionBuilder();
 Option option = optionBuilder.hasArgs(5)
 .create('D');

 assertEquals(5, option.getArgs().getValue());
 }

 @Test
 public void testOptionBuilderWithType() {
 OptionBuilder optionBuilder = new OptionBuilder();
 Option option = optionBuilder.withType(String.class)
 .create('D');

 assertEquals(String.class, option.getType());
 }

 @Test
 public void testOptionBuilderWithDescription() {
 OptionBuilder optionBuilder = new OptionBuilder();
 Option option = optionBuilder.withDescription("Sets the data directory")
 .create('D');

 assertEquals("Sets the data directory", option.getDescription());
 }
}
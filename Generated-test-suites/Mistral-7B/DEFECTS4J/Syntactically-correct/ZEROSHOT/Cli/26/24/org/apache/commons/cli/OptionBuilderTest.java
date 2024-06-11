import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class OptionBuilderTest {

 @Test
 public void testWithLongOpt() {
 OptionBuilder optionBuilder = OptionBuilder.getInstance();
 optionBuilder.withLongOpt("longOption");
 Option expectedOption = new Option("longOption", null);
 Option actualOption = optionBuilder.create();
 assertEquals(expectedOption, actualOption);
 }

 @Test
 public void testHasArg() {
 OptionBuilder optionBuilder = OptionBuilder.getInstance();
 optionBuilder.hasArg();
 Option expectedOption = Option.builder("option").required(false).build().singleValue(true);
 Option actualOption = optionBuilder.create();
 assertEquals(expectedOption.getClass(), actualOption.getClass());
 assertEquals(expectedOption.getName(), actualOption.getName());
 assertEquals(expectedOption.getLongOpt(), actualOption.getLongOpt());
 assertEquals(expectedOption.isRequired(), actualOption.isRequired());
 assertEquals(expectedOption.getArgs(), actualOption.getArgs());
 assertEquals(expectedOption.getValue(), actualOption.getValue());
 }

 @Test
 public void testHasArgWithName() {
 OptionBuilder optionBuilder = OptionBuilder.getInstance();
 optionBuilder.hasArg().withArgName("argName");
 Option expectedOption = Option.builder("option").required(false).argName("argName").build().singleValue(true);
 Option actualOption = optionBuilder.create();
 assertEquals(expectedOption.getClass(), actualOption.getClass());
 assertEquals(expectedOption.getName(), actualOption.getName());
 assertEquals(expectedOption.getLongOpt(), actualOption.getLongOpt());
 assertEquals(expectedOption.isRequired(), actualOption.isRequired());
 assertEquals(expectedOption.getArgs(), actualOption.getArgs());
 assertNull(expectedOption.getValue());
 }

 @Test
 public void testIsRequired() {
 OptionBuilder optionBuilder = OptionBuilder.getInstance();
 optionBuilder.isRequired();
 Option expectedOption = Option.builder("option").required(true).build().noArg();
 Option actualOption = optionBuilder.create();
 assertEquals(expectedOption.getClass(), actualOption.getClass());
 assertEquals(expectedOption.getName(), actualOption.getName());
 assertEquals(expectedOption.getLongOpt(), actualOption.getLongOpt());
 assertEquals(expectedOption.isRequired(), actualOption.isRequired());
 assertNull(expectedOption.getValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateWithoutLongOpt() {
 OptionBuilder optionBuilder = OptionBuilder.getInstance();
 optionBuilder.create();
 }

 @Test
 public void testWithValueSeparator() {
 OptionBuilder optionBuilder = OptionBuilder.getInstance();
 optionBuilder.withValueSeparator(':');
 Option expectedOption = Option.builder("option").required(false).valueSeparator(':').build().vararg(true);
 Option actualOption = optionBuilder.create();
 assertEquals(expectedOption.getClass(), actualOption.getClass());
 assertEquals(expectedOption.getName(), actualOption.getName());
 assertEquals(expectedOption.getLongOpt(), actualOption.getLongOpt());
 assertEquals(expectedOption.isRequired(), actualOption.isRequired());
 assertEquals(expectedOption.getArgs(), actualOption.getArgs());
 assertNull(expectedOption.getValue());
 }

 @Test
 public void testHasArgs() {
 OptionBuilder optionBuilder = OptionBuilder.getInstance();
 optionBuilder.hasArgs();
 Option expectedOption = Option.builder("option").required(false).args(Option.UNLIMITED_VALUES).build().vararg(true);
 Option actualOption = optionBuilder.create();
 assertEquals(expectedOption.getClass(), actualOption.getClass());
 assertEquals(expectedOption.getName(), actualOption.getName());
 assertEquals(expectedOption.getLongOpt(), actualOption.getLongOpt());
 assertEquals(expectedOption.isRequired(), actualOption.isRequired());
 assertEquals(expectedOption.getArgs(), actualOption.getArgs());
 assertNull(expectedOption.getValue());
 }

 @Test
 public void testHasArgsWithNumber() {
 OptionBuilder optionBuilder = OptionBuilder.getInstance();
 optionBuilder.hasArgs(3);
 Option expectedOption = Option.builder("option").required(false).args(3).build().vararg(true);
 Option actualOption = optionBuilder.create();
 assertEquals(expectedOption.getClass(), actualOption.getClass());
 assertEquals(expectedOption.getName(), actualOption.getName());
 assertEquals(expectedOption.getLongOpt(), actualOption.getLongOpt());
 assertEquals(expectedOption.isRequired(), actualOption.isRequired());
 assertEquals(expectedOption.getArgs(), actualOption.getArgs());
 assertNull(expectedOption.getValue());
 }

 @Test
 public void testHasOptionalArg() {
 OptionBuilder optionBuilder = OptionBuilder.getInstance();
 optionBuilder.hasOptionalArg();
 Option expectedOption = Option.builder("option").required(false).args(1).optionalArg(true).build().singleValue(true);
 Option actualOption = optionBuilder.create();
 assertEquals(expectedOption.getClass(), actualOption.getClass());
 assertEquals(expectedOption.getName(), actualOption.getName());
 assertEquals(expectedOption.getLongOpt(), actualOption.getLongOpt());
 assertEquals(expectedOption.isRequired(), actualOption.isRequired());
 assertEquals(expectedOption.getArgs(), actualOption.getArgs());
 assertNull(expectedOption.getValue());
 }

 @Test
 public void testHasOptionalArgs() {
 OptionBuilder optionBuilder = OptionBuilder.getInstance();
 optionBuilder.hasOptionalArgs();
 Option expectedOption = Option.builder("option").required(false).args(Option.UNLIMITED_VALUES).optionalArg(true).build().vararg(true);
 Option actualOption = optionBuilder.create();
 assertEquals(expectedOption.getClass(), actualOption.getClass());
 assertEquals(expectedOption.getName(), actualOption.getName());
 assertEquals(expectedOption.getLongOpt(), actualOption.getLongOpt());
 assertEquals(expectedOption.isRequired(), actualOption.isRequired());
 assertEquals(expectedOption.getArgs(), actualOption.getArgs());
 assertNull(expectedOption.getValue());
 }

 @Test
 public void testHasOptionalArgsWithNumber() {
 OptionBuilder optionBuilder = OptionBuilder.getInstance();
 optionBuilder.hasOptionalArgs(3);
 Option expectedOption = Option.builder("option").required(false).args(3).optionalArg(true).build().vararg(true);
 Option actualOption = optionBuilder.create();
 assertEquals(expectedOption.getClass(), actualOption.getClass());
 assertEquals(expectedOption.getName(), actualOption.getName());
 assertEquals(expectedOption.getLongOpt(), actualOption.getLongOpt());
 assertEquals(expectedOption.isRequired(), actualOption.isRequired());
 assertEquals(expectedOption.getArgs(), actualOption.getArgs());
 assertNull(expectedOption.getValue());
 }

 @Test
 public void testWithType() {
 OptionBuilder optionBuilder = OptionBuilder.getInstance();
 optionBuilder.withType(String.class);
 Option expectedOption = Option.builder("option").required(false).args(1).type(String.class).build().singleValue(true);
 Option actualOption = optionBuilder.create();
 assertEquals(expectedOption.getClass(), actualOption.getClass());
 assertEquals(expectedOption.getName(), actualOption.getName());
 assertEquals(expectedOption.getLongOpt(), actualOption.getLongOpt());
 assertEquals(expectedOption.isRequired(), actualOption.isRequired());
 assertEquals(expectedOption.getArgs(), actualOption.getArgs());
 assertEquals(expectedOption.getTypeClass(), actualOption.getTypeClass());
 }

 @Test
 public void testWithDescription() {
 OptionBuilder optionBuilder = OptionBuilder.getInstance();
 optionBuilder.withDescription("description");
 Option expectedOption = Option.builder("option").required(false).args(1).build().singleValue(true)
 .longOpt("longOption").description("description");
 Option actualOption = optionBuilder.create();
 assertEquals(expectedOption.getClass(), actualOption.getClass());
 assertEquals(expectedOption.getName(), actualOption.getName());
 assertEquals(expectedOption.getLongOpt(), actualOption.getLongOpt());
 assertEquals(expectedOption.isRequired(), actualOption.isRequired());
 assertEquals(expectedOption.getArgs(), actualOption.getArgs());
 assertEquals(expectedOption.getDescription(), actualOption.getDescription());
 }

 @Test
 public void testReset() {
 OptionBuilder optionBuilder = OptionBuilder.getInstance();
 optionBuilder.withLongOpt("longOption");
 optionBuilder.hasArg();
 optionBuilder.isRequired();
 optionBuilder.withValueSeparator(':');
 Option actualOption = optionBuilder.create();
 Option expectedOption = Option.builder("option").required(true).args(1).valueSeparator(':').build().singleValue(true);
 assertEquals(expectedOption.getClass(), actualOption.getClass());
 assertEquals(expectedOption.getName(), actualOption.getName());
 assertEquals(expectedOption.getLongOpt(), actualOption.getLongOpt());
 assertEquals(expectedOption.isRequired(), actualOption.isRequired());
 assertEquals(expectedOption.getArgs(), actualOption.getArgs());
 assertNull(expectedOption.getValue());
 OptionBuilder.reset();
 Option resetOption = OptionBuilder.getInstance().create();
 assertNull(resetOption.getLongOpt());
 }
}
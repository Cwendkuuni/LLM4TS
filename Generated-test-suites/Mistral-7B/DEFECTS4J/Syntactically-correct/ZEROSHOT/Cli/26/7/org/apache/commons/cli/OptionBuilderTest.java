import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {

 @Test
 public void testOptionBuilderWithLongOpt() {
 OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longOpt");
 Option expectedOption = new Option(null, "longOpt", false, null);
 Option actualOption = optionBuilder.create();
 assertEquals(expectedOption, actualOption);
 }

 @Test
 public void testOptionBuilderHasArg() {
 OptionBuilder optionBuilder = OptionBuilder.hasArg();
 Option expectedOption = new Option(null, null, true, null);
 Option actualOption = optionBuilder.create();
 assertEquals(expectedOption, actualOption);
 }

 @Test
 public void testOptionBuilderHasArgWithName() {
 OptionBuilder optionBuilder = OptionBuilder.withArgName("argName");
 Option expectedOption = new Option(null, null, true, "argName");
 Option actualOption = optionBuilder.create();
 assertEquals(expectedOption, actualOption);
 }

 @Test
 public void testOptionBuilderIsRequired() {
 OptionBuilder optionBuilder = OptionBuilder.isRequired();
 Option expectedOption = new Option(null, null, true, null);
 Option actualOption = optionBuilder.create();
 assertEquals(expectedOption, actualOption);
 }

 @Test
 public void testOptionBuilderHasOptionalArg() {
 OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
 Option expectedOption = new Option(null, null, true, null);
 Option actualOption = optionBuilder.create();
 assertEquals(expectedOption, actualOption);
 }

 @Test
 public void testOptionBuilderHasValueSeparator() {
 OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(':');
 Option expectedOption = new Option(null, null, true, null, ':');
 Option actualOption = optionBuilder.create();
 assertEquals(expectedOption, actualOption);
 }

 @Test
 public void testOptionBuilderHasArgs() {
 OptionBuilder optionBuilder = OptionBuilder.hasArgs();
 Option expectedOption = new Option(null, null, Option.UNLIMITED_VALUES, null);
 Option actualOption = optionBuilder.create();
 assertEquals(expectedOption, actualOption);
 }

 @Test
 public void testOptionBuilderHasArgsWithNum() {
 OptionBuilder optionBuilder = OptionBuilder.hasArgs(2);
 Option expectedOption = new Option(null, null, 2, null);
 Option actualOption = optionBuilder.create();
 assertEquals(expectedOption, actualOption);
 }

 @Test
 public void testOptionBuilderWithType() {
 OptionBuilder optionBuilder = OptionBuilder.withType(String.class);
 Option expectedOption = new Option(null, null, false, null, (Type) String.class);
 Option actualOption = optionBuilder.create();
 assertEquals(expectedOption, actualOption);
 }

 @Test
 public void testOptionBuilderWithDescription() {
 OptionBuilder optionBuilder = OptionBuilder.withDescription("Description");
 Option expectedOption = new Option(null, null, false, null, null, "Description");
 Option actualOption = optionBuilder.create();
 assertEquals(expectedOption, actualOption);
 }
}
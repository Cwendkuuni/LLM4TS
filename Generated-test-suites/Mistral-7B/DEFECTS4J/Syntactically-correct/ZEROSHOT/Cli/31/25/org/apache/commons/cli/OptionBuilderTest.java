import org.apache.commons.cli.Option;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class OptionBuilderTest {

 @Test
 public void testOptionBuilderDefaultValues() {
 OptionOptionBuilder = OptionBuilder.create();

 assertNull(OptionOptionBuilder.getLongOpt());
 assertNull(OptionOptionBuilder.getDescription());
 assertNull(OptionOptionBuilder.getArgName());
 assertFalse(OptionOptionBuilder.isRequired());
 assertEquals(Option.UNINITIALIZED, OptionOptionBuilder.getNumberOfArgs());
 assertNull(OptionOptionBuilder.getType());
 assertFalse(OptionOptionBuilder.isOptionalArg());
 assertNull(OptionOptionBuilder.getValueSeparator());
 }

 @Test
 public void testOptionBuilderWithLongOpt() {
 OptionBuilder.withLongOpt("myLongOpt");
 OptionOptionBuilder = OptionBuilder.create();

 assertEquals("myLongOpt", OptionOptionBuilder.getLongOpt());
 assertNull(OptionOptionBuilder.getDescription());
 assertNull(OptionOptionBuilder.getArgName());
 assertFalse(OptionOptionBuilder.isRequired());
 assertEquals(Option.UNINITIALIZED, OptionOptionBuilder.getNumberOfArgs());
 assertNull(OptionOptionBuilder.getType());
 assertFalse(OptionOptionBuilder.isOptionalArg());
 assertNull(OptionOptionBuilder.getValueSeparator());
 }

 @Test
 public void testOptionBuilderHasArg() {
 OptionBuilder.hasArg();
 OptionOptionBuilder = OptionBuilder.create();

 assertEquals(1, OptionOptionBuilder.getNumberOfArgs());
 assertNull(OptionOptionBuilder.getArgName());
 assertFalse(OptionOptionBuilder.isRequired());
 assertNull(OptionOptionBuilder.getType());
 assertFalse(OptionOptionBuilder.isOptionalArg());
 assertNull(OptionOptionBuilder.getValueSeparator());
 }

 @Test
 public void testOptionBuilderHasArgWithArgName() {
 OptionBuilder.hasArg();
 OptionBuilder.withArgName("myArgName");
 OptionOptionBuilder = OptionBuilder.create();

 assertEquals(1, OptionOptionBuilder.getNumberOfArgs());
 assertEquals("myArgName", OptionOptionBuilder.getArgName());
 assertFalse(OptionOptionBuilder.isRequired());
 assertNull(OptionOptionBuilder.getType());
 assertFalse(OptionOptionBuilder.isOptionalArg());
 assertNull(OptionOptionBuilder.getValueSeparator());
 }

 @Test
 public void testOptionBuilderIsRequired() {
 OptionBuilder.isRequired();
 OptionOptionBuilder = OptionBuilder.create();

 assertTrue(OptionOptionBuilder.isRequired());
 assertNull(OptionOptionBuilder.getArgName());
 assertNull(OptionOptionBuilder.getType());
 assertEquals(Option.UNINITIALIZED, OptionOptionBuilder.getNumberOfArgs());
 assertFalse(OptionOptionBuilder.isOptionalArg());
 assertNull(OptionOptionBuilder.getValueSeparator());
 }

 @Test
 public void testOptionBuilderWithValueSeparator() {
 OptionBuilder.withValueSeparator(':');
 OptionOptionBuilder = OptionBuilder.create();

 assertEquals(':', OptionOptionBuilder.getValueSeparator());
 assertNull(OptionOptionBuilder.getArgName());
 assertNull(OptionOptionBuilder.getType());
 assertEquals(Option.UNINITIALIZED, OptionOptionBuilder.getNumberOfArgs());
 assertFalse(OptionOptionBuilder.isOptionalArg());
 }

 @Test
 public void testOptionBuilderHasArgs() {
 OptionBuilder.hasArgs();
 OptionOptionBuilder = OptionBuilder.create();

 assertEquals(Option.UNLIMITED_VALUES, OptionOptionBuilder.getNumberOfArgs());
 assertNull(OptionOptionBuilder.getArgName());
 assertNull(OptionOptionBuilder.getType());
 assertFalse(OptionOptionBuilder.isOptionalArg());
 assertNull(OptionOptionBuilder.getValueSeparator());
 }

 @Test
 public void testOptionBuilderHasArgsWithArgName() {
 OptionBuilder.hasArgs();
 OptionBuilder.withArgName("myArgName");
 OptionOptionBuilder = OptionBuilder.create();

 assertEquals(Option.UNLIMITED_VALUES, OptionOptionBuilder.getNumberOfArgs());
 assertEquals("myArgName", OptionOptionBuilder.getArgName());
 assertNull(OptionOptionBuilder.getType());
 assertFalse(OptionOptionBuilder.isOptionalArg());
 assertNull(OptionOptionBuilder.getValueSeparator());
 }

 @Test
 public void testOptionBuilderHasOptionalArg() {
 OptionBuilder.hasOptionalArg();
 OptionOptionBuilder = OptionBuilder.create();

 assertTrue(OptionOptionBuilder.isOptionalArg());
 assertEquals(1, OptionOptionBuilder.getNumberOfArgs());
 assertNull(OptionOptionBuilder.getArgName());
 assertNull(OptionOptionBuilder.getType());
 assertNull(OptionOptionBuilder.getValueSeparator());
 }

 @Test
 public void testOptionBuilderHasOptionalArgs() {
 OptionBuilder.hasOptionalArgs();
 OptionOptionBuilder = OptionBuilder.create();

 assertEquals(Option.UNLIMITED_VALUES, OptionOptionBuilder.getNumberOfArgs());
 assertTrue(OptionOptionBuilder.isOptionalArg());
 assertNull(OptionOptionBuilder.getArgName());
 assertNull(OptionOptionBuilder.getType());
 assertNull(OptionOptionBuilder.getValueSeparator());
 }

 @Test
 public void testOptionBuilderHasOptionalArgsWithArgName() {
 OptionBuilder.hasOptionalArgs();
 OptionBuilder.withArgName("myArgName");
 OptionOptionBuilder = OptionBuilder.create();

 assertEquals(Option.UNLIMITED_VALUES, OptionOptionBuilder.getNumberOfArgs());
 assertTrue(OptionOptionBuilder.isOptionalArg());
 assertEquals("myArgName", OptionOptionBuilder.getArgName());
 assertNull(OptionOptionBuilder.getType());
 assertNull(OptionOptionBuilder.getValueSeparator());
 }

 @Test
 public void testOptionBuilderWithType() {
 OptionBuilder.withType(String.class);
 OptionOptionBuilder = OptionBuilder.create();

 assertEquals(String.class, OptionOptionBuilder.getType());
 assertNull(OptionOptionBuilder.getArgName());
 assertNull(OptionOptionBuilder.getType());
 assertEquals(Option.UNINITIALIZED, OptionOptionBuilder.getNumberOfArgs());
 assertFalse(OptionOptionBuilder.isOptionalArg());
 assertNull(OptionOptionBuilder.getValueSeparator());
 }

 @Test
 public void testOptionBuilderWithDescription() {
 OptionBuilder.withDescription("My Option Description");
 OptionOptionBuilder = OptionBuilder.create();

 assertEquals("My Option Description", OptionOptionBuilder.getDescription());
 assertNull(OptionOptionBuilder.getArgName());
 assertNull(OptionOptionBuilder.getType());
 assertEquals(Option.UNINITIALIZED, OptionOptionBuilder.getNumberOfArgs());
 assertFalse(OptionOptionBuilder.isOptionalArg());
 assertNull(OptionOptionBuilder.getValueSeparator());
 }
}
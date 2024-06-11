import org.apache.commons.cli.Option;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class OptionBuilderTest {

 @Test
 public void testWithLongOpt() {
 OptionBuilder optionBuilder = OptionBuilder.withLongOpt("optionLongName");
 Option expectedOption = new Option("optionShortName", "optionLongName", false, null);
 expectedOption.setLongOpt("optionLongName");

 Option actualOption = OptionBuilder.create();
 OptionBuilder.withLongOpt("optionLongName").hasArg().create();

 assertEquals(expectedOption, actualOption);
 }

 @Test
 public void testWithArgName() {
 OptionBuilder optionBuilder = OptionBuilder.withArgName("argumentName");
 Option expectedOption = new Option("optionShortName", "optionDescription", false, "argumentName");

 Option actualOption = OptionBuilder.create();
 OptionBuilder.withArgName("argumentName").create();

 assertEquals(expectedOption, actualOption);
 }

 @Test
 public void testHasArg() {
 OptionBuilder optionBuilder = OptionBuilder.instance();
 Option expectedOption = new Option("optionShortName", "optionDescription", true, null);

 Option actualOption = OptionBuilder.create();
 OptionBuilder.instance().reset();
 OptionBuilder.instance().hasArg().create();

 assertEquals(expectedOption, actualOption);
 }

 @Test
 public void testIsRequired() {
 OptionBuilder optionBuilder = OptionBuilder.instance();
 Option expectedOption = new Option("optionShortName", "optionDescription", false, null);
 Option actualOption = OptionBuilder.create();

 OptionBuilder.instance().isRequired().create();

 assertEquals(expectedOption, actualOption);

 expectedOption.setRequired(true);
 OptionBuilder.instance().isRequired(true).create();

 assertEquals(expectedOption, actualOption);
 }

 @Test
 public void testHasOptionalArg() {
 OptionBuilder optionBuilder = OptionBuilder.instance();
 Option expectedOption = new Option("optionShortName", "optionDescription", false, null);
 Option actualOption = OptionBuilder.create();

 OptionBuilder.instance().hasOptionalArg().create();

 assertEquals(expectedOption, actualOption);

 expectedOption.setOptionalArg(true);
 OptionBuilder.instance().hasOptionalArg(true).create();

 assertEquals(expectedOption, actualOption);
 }

 @Test
 public void testHasArgs() {
 OptionBuilder optionBuilder = OptionBuilder.instance();
 Option expectedOption = new Option("optionShortName", "optionDescription", false, null);
 Option actualOption = OptionBuilder.create();

 OptionBuilder.instance().hasArgs().create();

 assertEquals(expectedOption, actualOption);

 expectedOption.setArgArguments(new String[]{"arg1", "arg2"});
 OptionBuilder.instance().hasArgs(2).create();

 assertEquals(expectedOption, actualOption);
 }

 @Test
 public void testCreateWithNoSettings() {
 OptionBuilder optionBuilder = OptionBuilder.instance();
 Option actualOption = OptionBuilder.create();

 Option expectedOption = new Option("", "", false, null);

 assertEquals(expectedOption, actualOption);
 }

 @Test
 public void testCreateWithAllSettings() {
 OptionBuilder optionBuilder = OptionBuilder.instance();
 Option expectedOption = new Option("optionShortName", "optionDescription", true, "argumentName", Option.VALUE_REQUIRED, "valueType");
 Option actualOption = OptionBuilder.create();
 OptionBuilder.instance().withLongOpt("optionLongName")
 .withDescription("optionDescription")
 .hasArg()
 .withArgName("argumentName")
 .isRequired()
 .withType("valueType")
 .create();

 assertEquals(expectedOption, actualOption);
 }

 @Test
 public void testReset() {
 OptionBuilder optionBuilder = OptionBuilder.instance();
 Option expectedOption = new Option("optionShortName", "optionDescription", false, null);
 Option actualOption = OptionBuilder.create();

 OptionBuilder.instance().hasArg().create();
 OptionBuilder.instance().reset();
 OptionBuilder.instance().hasArg().create();

 assertEquals(expectedOption, actualOption);
 }

 @Test
 public void testUndoHasArg() {
 OptionBuilder optionBuilder = OptionBuilder.instance();
 Option expectedOption = new Option("optionShortName", "optionDescription", false, null);
 Option actualOption = OptionBuilder.create();
 OptionBuilder.instance().hasArg().create();
 OptionBuilder.instance().reset();

 assertEquals(expectedOption, actualOption);
 }

 @Test
 public void testUndoIsRequired() {
 OptionBuilder optionBuilder = OptionBuilder.instance();
 Option expectedOption = new Option("optionShortName", "optionDescription", false, null);
 Option actualOption = OptionBuilder.create();
 OptionBuilder.instance().isRequired().create();
 OptionBuilder.instance().reset();

 assertEquals(expectedOption, actualOption);
 }

 @Test
 public void testUndoOptionalArg() {
 OptionBuilder optionBuilder = OptionBuilder.instance();
 Option expectedOption = new Option("optionShortName", "optionDescription", false, null);
 Option actualOption = OptionBuilder.create();
 OptionBuilder.instance().hasOptionalArg().create();
 OptionBuilder.instance().reset();

 assertEquals(expectedOption, actualOption);
 }

 @Test
 public void testUndoValueSeparator() {
 OptionBuilder optionBuilder = OptionBuilder.instance();
 Option expectedOption = new Option("optionShortName", "optionDescription", false, null);
 Option actualOption = OptionBuilder.create();
 OptionBuilder.instance().withValueSeparator(':').create();
 OptionBuilder.instance().reset();

 assertEquals(expectedOption, actualOption);
 }

 @Test
 public void testCreateWithDefaultSettings() {
 Option expectedOption = Option.builder().create();
 Option actualOption = OptionBuilder.create();

 assertEquals(expectedOption, actualOption);
 }
}
import org.apache.commons.cli.*;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class ParserTest {

 @Test
 public void testParseWithEmptyArgs() {
 Parser parser = new DefaultParser();
 Options options = new Options();
 CommandLine result;
 try {
 result = parser.parse(options, new String[0]);
 } catch (ParseException e) {
 throw new RuntimeException("Parsing should have succeeded.", e);
 }
 assertEquals(0, result.getArgs().size());
 assertEquals(0, result.getOptions().size());
 }

 @Test
 public void testParseWithArgs() {
 Parser parser = new DefaultParser();
 Options options = new Options();
 options.addOption("a", "arg1", true, "An argument for option a.");
 CommandLine result;
 try {
 result = parser.parse(options, new String[]{"-a"});
 } catch (ParseException e) {
 throw new RuntimeException("Parsing should have succeeded.", e);
 }
 assertEquals(1, result.getArgs().size());
 assertEquals("", result.getArgs().get(0));
 assertEquals(1, result.getOptions().size());
 Option option = result.getOptions().getOption("a");
 assertTrue(option.getValue().isPresent());
 assertEquals("", option.getValue().get());
 }

 @Test
 public void testParseWithMultipleArgs() {
 Parser parser = new DefaultParser();
 Options options = new Options();
 options.addOption("a", "arg1", true, "An argument for option a.");
 CommandLine result;
 try {
 result = parser.parse(options, new String[]{"-a", "arg2", "arg3"});
 } catch (ParseException e) {
 throw new RuntimeException("Parsing should have succeeded.", e);
 }
 assertEquals(3, result.getArgs().size());
 assertEquals("arg2", result.getArgs().get(1));
 assertEquals("arg3", result.getArgs().get(2));
 assertEquals(1, result.getOptions().size());
 Option option = result.getOptions().getOption("a");
 assertTrue(option.getValue().isPresent());
 assertEquals("arg3", option.getValue().get());
 }

 @Test
 public void testParseWithOptionalArg() {
 Parser parser = new DefaultParser();
 Options options = new Options();
 options.addOption("a", "arg1", true, "An argument for option a.");
 CommandLine result;
 try {
 result = parser.parse(options, new String[]{"-a", "arg1value"});
 } catch (ParseException e) {
 throw new RuntimeException("Parsing should have succeeded.", e);
 }
 assertEquals(1, result.getArgs().size());
 assertEquals("arg1value", result.getArgs().get(0));
 assertEquals(1, result.getOptions().size());
 Option option = result.getOptions().getOption("a");
 assertTrue(option.getValue().isPresent());
 assertEquals("arg1value", option.getValue().get());

 try {
 result = parser.parse(options, new String[]{"-a"});
 } catch (ParseException e) {
 // expected, no error message needed
 }
 assertEquals(0, result.getArgs().size());
 assertEquals(1, result.getOptions().size());
 option = result.getOptions().getOption("a");
 assertNull(option.getValue());
 }

 @Test
 public void testParseWithMultipleOptions() {
 Parser parser = new DefaultParser();
 Options options = new Options();
 options.addOption("a", "arg1", true, "An argument for option a.");
 options.addOption("b", "arg2", true, "An argument for option b.");
 CommandLine result;
 try {
 result = parser.parse(options, new String[]{"-a", "arg1value", "-b", "arg2value"});
 } catch (ParseException e) {
 throw new RuntimeException("Parsing should have succeeded.", e);
 }
 assertEquals(0, result.getArgs().size());
 assertEquals(2, result.getOptions().size());
 Option optionA = result.getOptions().getOption("a");
 assertTrue(optionA.getValue().isPresent());
 assertEquals("arg1value", optionA.getValue().get());
 Option optionB = result.getOptions().getOption("b");
 assertTrue(optionB.getValue().isPresent());
 assertEquals("arg2value", optionB.getValue().get());
 }

 @Test
 public void testParseWithRequiredOptions() {
 Parser parser = new DefaultParser();
 Options options = new Options();
 options.addOption("a", true, "An required option.");
 options.addRequiredOption("b", false, "An required option.");
 CommandLine result;
 assertThrows(MissingOptionException.class, () -> {
 result = parser.parse(options, new String[0]);
 });
 try {
 result = parser.parse(options, new String[]{"-a"});
 } catch (ParseException e) {
 throw new RuntimeException("Parsing should have succeeded.", e);
 }
 assertEquals(1, result.getArgs().size());
 assertEquals(1, result.getOptions().size());
 Option optionA = result.getOptions().getOption("a");
 assertTrue(optionA.getValue().isPresent());
 assertFalse(optionA.isRequired());
 assertFalse(result.getOptions().getOption("b").isPresent());
 try {
 result = parser.parse(options, new String[]{"-a", "-b"});
 } catch (ParseException e) {
 throw new RuntimeException("Parsing should have succeeded.", e);
 }
 assertEquals(1, result.getArgs().size());
 assertEquals(2, result.getOptions().size());
 Option optionA = result.getOptions().getOption("a");
 assertTrue(optionA.getValue().isPresent());
 assertFalse(optionA.isRequired());
 Option optionB = result.getOptions().getOption("b");
 assertTrue(optionB.getValue().isPresent());
 assertTrue(optionB.isRequired());
 }

 @Test
 public void testParseWithInvalidOption() {
 Parser parser = new DefaultParser();
 Options options = new Options();
 CommandLine result;
 assertThrows(UnrecognizedOptionException.class, () -> {
 result = parser.parse(options, new String[]{"-invalid"});
 });
 }

 @Test
 public void testParseWithDuplicateOption() {
 Parser parser = new DefaultParser();
 Options options = new Options();
 options.addOption("a", true, "An option.");
 CommandLine result;
 try {
 result = parser.parse(options, new String[]{"-a", "-a"});
 } catch (ParseException e) {
 throw new RuntimeException("Parsing should have succeeded.", e);
 }
 assertEquals(0, result.getArgs().size());
 assertEquals(1, result.getOptions().size());
 Option option = result.getOptions().getOption("a");
 assertTrue(option.getValue().isPresent());
 assertEquals(2, option.getValue().get().size());
 }

 @Test
 public void testParseWithLongOptions() {
 Parser parser = new DefaultParser();
 Options options = new Options();
 options.addOption("longOption", true, "A long option.");
 CommandLine result;
 try {
 result = parser.parse(options, new String[]{"--longOption"});
 } catch (ParseException e) {
 throw new RuntimeException("Parsing should have succeeded.", e);
 }
 assertEquals(0, result.getArgs().size());
 assertEquals(1, result.getOptions().size());
 Option option = result.getOptions().getOption("longOption");
 assertTrue(option.getValue().isPresent());
 assertEquals("", option.getValue().get());
 }

 @Test
 public void testParseWithLongOptionValue() {
 Parser parser = new DefaultParser();
 Options options = new Options();
 options.addOption("a", true, "An option.");
 CommandLine result;
 try {
 result = parser.parse(options, new String[]{"--a=longOptionValue"});
 } catch (ParseException e) {
 throw new RuntimeException("Parsing should have succeeded.", e);
 }
 assertEquals(0, result.getArgs().size());
 assertEquals(1, result.getOptions().size());
 Option option = result.getOptions().getOption("a");
 assertTrue(option.getValue().isPresent());
 assertEquals("longOptionValue", option.getValue().get());
 }

 @Test
 public void testParseWithComplexOptions() {
 Parser parser = new DefaultParser();
 Options options = new Options();
 options.addOption("a", true, "An option.");
 options.addOptionGroup(new OptionGroup("group1", "Group 1"));
 options.addOption("group1:group1:arg1", true, "An option in group 1.");
 options.addOptionGroup(new OptionGroup("group2", "Group 2"));
 options.addOption("group2:group2:arg2", true, "An option in group 2.");
 CommandLine result;
 try {
 result = parser.parse(options, new String[]{"--group1:group1:arg1=value1", "--group2:group2:arg2=value2"});
 } catch (ParseException e) {
 throw new RuntimeException("Parsing should have succeeded.", e);
 }
 assertEquals(0, result.getArgs().size());
 assertEquals(4, result.getOptions().size());
 Option aOption = result.getOptions().getOption("a");
 assertTrue(aOption.getValue().isPresent());
 assertEquals("", aOption.getValue().get());
 Option group1Option = result.getOptions().getOption("group1:group1:arg1");
 assertTrue(group1Option.getValue().isPresent());
 assertEquals("value1", group1Option.getValue().get());
 Option group2Option = result.getOptions().getOption("group2:group2:arg2");
 assertTrue(group2Option.getValue().isPresent());
 assertEquals("value2", group2Option.getValue().get());
 Option group1 = result.getOptions().getOptionGroup("group1");
 assertTrue(group1.isSelected());
 Option group2 = result.getOptions().getOptionGroup("group2");
 assertTrue(group2.isSelected());
 }
}
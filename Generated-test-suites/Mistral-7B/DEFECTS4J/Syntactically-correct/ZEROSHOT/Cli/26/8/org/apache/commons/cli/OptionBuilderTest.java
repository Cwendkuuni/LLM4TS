import org.apache.commons.cli.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {

 @Test
 public void testHasArg() {
 OptionBuilder optionBuilder = OptionBuilder.hasArg();
 Option option = optionBuilder.create('D');
 assertEquals("Option 'D' is expected to have an argument", 1, option.getArgs().length);
 }

 @Test
 public void testHasArgs() {
 OptionBuilder optionBuilder = OptionBuilder.hasArgs();
 Option option = optionBuilder.create('D');
 OptionParser parser = new DefaultParser();
 CommandLine commandLine = null;
 try {
 commandLine = parser.parse(new String[]{"-D", "test"});
 } catch (ParseException e) {
 fail("Failed to parse command line: " + e.getMessage());
 }
 assertEquals("Option 'D' is expected to have multiple arguments", 1, commandLine.getOptionValues("D").length);
 }

 @Test
 public void testHasOptionalArg() {
 OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
 Option option = optionBuilder.create('D');
 OptionParser parser = new DefaultParser();
 CommandLine commandLine = null;
 try {
 commandLine = parser.parse(new String[]{"-D"});
 } catch (ParseException e) {
 fail("Failed to parse command line: " + e.getMessage());
 }
 assertNull("Option 'D' is expected to have an optional argument, but has a required one", commandLine.getOptionValue("D"));

 commandLine = null;
 try {
 commandLine = parser.parse(new String[]{"-D", "test"});
 } catch (ParseException e) {
 fail("Failed to parse command line: " + e.getMessage());
 }
 assertEquals("Option 'D' is expected to have an optional argument", "test", commandLine.getOptionValue("D"));
 }

 @Test
 public void testHasOptionalArgs() {
 OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs();
 Option option = optionBuilder.create('D');
 OptionParser parser = new DefaultParser();
 CommandLine commandLine = null;
 try {
 commandLine = parser.parse(new String[]{"-D", "test1", "test2", "test3"});
 } catch (ParseException e) {
 fail("Failed to parse command line: " + e.getMessage());
 }
 assertEquals("Option 'D' is expected to have multiple optional arguments", new String[]{"test1", "test2", "test3"}, commandLine.getOptionValues("D"));
 }

 @Test
 public void testHasArgsWithArgName() {
 OptionBuilder optionBuilder = OptionBuilder.hasArgs("argName");
 Option option = optionBuilder.create('D');
 OptionParser parser = new DefaultParser();
 CommandLine commandLine = null;
 try {
 commandLine = parser.parse(new String[]{"-D", "test"});
 } catch (ParseException e) {
 fail("Failed to parse command line: " + e.getMessage());
 }
 assertEquals("Option 'D' is expected to have an argument named 'argName'", "test", commandLine.getOptionValue("D"));
 }

 @Test
 public void testHasOptionalArgsWithArgName() {
 OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs("argName");
 Option option = optionBuilder.create('D');
 OptionParser parser = new DefaultParser();
 CommandLine commandLine = null;
 try {
 commandLine = parser.parse(new String[]{"-D"});
 } catch (ParseException e) {
 fail("Failed to parse command line: " + e.getMessage());
 }
 assertNull("Option 'D' is expected to have an optional argument with name 'argName'", commandLine.getOptionValue("D"));

 commandLine = null;
 try {
 commandLine = parser.parse(new String[]{"-D", "test"});
 } catch (ParseException e) {
 fail("Failed to parse command line: " + e.getMessage());
 }
 assertEquals("Option 'D' is expected to have an optional argument with name 'argName'", "test", commandLine.getOptionValue("D"));
 }

 @Test
 public void testWithValueSeparator() {
 OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(':');
 Option option = optionBuilder.create('D');
 OptionParser parser = new DefaultParser();
 CommandLine commandLine = null;
 try {
 commandLine = parser.parse(new String[]{"-D", "key:value"});
 } catch (ParseException e) {
 fail("Failed to parse command line: " + e.getMessage());
 }
 assertEquals("Option 'D' is expected to have a value separator ':'", "key:value", option.getValue(commandLine, "D"));
 }

 @Test
 public void testWithDescription() {
 OptionBuilder optionBuilder = OptionBuilder.withDescription("Description for Option D");
 Option option = optionBuilder.create('D');
 assertEquals("Option 'D' is expected to have a description", "Description for Option D", option.getDescription());
 }

 @Test
 public void testCreateWithRequiredArg() {
 OptionBuilder optionBuilder = OptionBuilder.isRequired();
 Option option = optionBuilder.create('D');
 OptionParser parser = new DefaultParser();
 CommandLine commandLine = null;
 try {
 commandLine = parser.parse(new String[]{"-D"});
 } catch (ParseException e) {
 fail("Failed to parse command line: " + e.getMessage());
 }
 assertNotNull("Option 'D' is expected to be a required argument", commandLine.getOption("D"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateWithoutLongOpt() {
 OptionBuilder optionBuilder = OptionBuilder.instance();
 Option option = optionBuilder.create('D');
 fail("OptionBuilder should throw an IllegalArgumentException if longopt has not been set");
 }
}
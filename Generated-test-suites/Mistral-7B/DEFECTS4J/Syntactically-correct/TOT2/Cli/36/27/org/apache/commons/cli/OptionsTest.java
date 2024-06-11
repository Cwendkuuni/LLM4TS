import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.HelpFormatter;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

 @Test
 public void testOptionsWithNoArguments() {
 Options options = new Options();
 options.addOption("a", "optionA", false, "Description for option A");
 options.addOption("b", "optionB", false, "Description for option B");

 CommandLineParser parser = new DefaultParser();

 try {
 CommandLine commandLine = parser.parse(options.toCommandLineParser(), new String[]{"-a"});
 assertTrue(commandLine.hasOption("a"));
 assertFalse(commandLine.hasOption("b"));
 } catch (ParseException e) {
 e.printStackTrace();
 fail("ParseException was thrown");
 }
 }

 @Test
 public void testOptionsWithRequiredArgument() {
 Options options = new Options();
 options.addOption("a", "optionA", true, "Description for option A");

 CommandLineParser parser = new DefaultParser();

 try {
 CommandLine commandLine = parser.parse(options.toCommandLineParser(), new String[]{"-a", "arg"});
 assertTrue(commandLine.hasOption("a"));
 assertEquals("arg", commandLine.getOptionValue("a"));
 } catch (ParseException e) {
 e.printStackTrace();
 fail("ParseException was thrown");
 }
 }

 @Test
 public void testOptionsWithOptionalArgument() {
 Options options = new Options();
 options.addOption("a", "optionA", true, "Description for option A");
 options.addOption("b", "optionB", false, "Description for option B");

 CommandLineParser parser = new DefaultParser();

 try {
 CommandLine commandLine = parser.parse(options.toCommandLineParser(), new String[]{"-a", "arg1", "-b"});
 assertTrue(commandLine.hasOption("a"));
 assertEquals("arg1", commandLine.getOptionValue("a"));
 assertTrue(commandLine.hasOption("b"));
 } catch (ParseException e) {
 e.printStackTrace();
 fail("ParseException was thrown");
 }
 }

 @Test
 public void testOptionsWithLongArguments() {
 Options options = new Options();
 options.addOption("a", "optionALong", false, "Description for option A with long name");
 options.addOption("b", "optionBLong", false, "Description for option B with long name");

 CommandLineParser parser = new DefaultParser();

 try {
 CommandLine commandLine = parser.parse(options.toCommandLineParser(), new String[]{"--optionALong"});
 assertTrue(commandLine.hasOption("a"));
 } catch (ParseException e) {
 e.printStackTrace();
 fail("ParseException was thrown");
 }
 }

 @Test
 public void testOptionsWithLongAndShortArguments() {
 Options options = new Options();
 options.addOption("a", "optionA", false, "Description for option A");
 options.addOption("b", "optionB", false, "Description for option B");
 options.addOption("c", "optionCLong", false, "Description for option C with long name");

 CommandLineParser parser = new DefaultParser();

 try {
 CommandLine commandLine = parser.parse(options.toCommandLineParser(), new String[]{"-c"});
 assertTrue(commandLine.hasOption("c"));
 } catch (ParseException e) {
 e.printStackTrace();
 fail("ParseException was thrown");
 }
 }

 @Test(expected = ParseException.class)
 public void testOptionsWithInvalidArgument() {
 Options options = new Options();
 options.addOption("a", "optionA", false, "Description for option A");

 CommandLineParser parser = new DefaultParser();

 try {
 parser.parse(options.toCommandLineParser(), new String[]{"-a", "invalid"});
 fail("ParseException was not thrown");
 } catch (ParseException e) {
 // expected
 }
 }

 @Test(expected = ParseException.class)
 public void testOptionsWithMissingArgument() {
 Options options = new Options();
 options.addOption("a", "optionA", true, "Description for option A");

 CommandLineParser parser = new DefaultParser();

 try {
 parser.parse(options.toCommandLineParser(), new String[]{"-a"});
 fail("ParseException was not thrown");
 } catch (ParseException e) {
 // expected
 }
 }

 @Test
 public void testOptionsWithNoArgumentsPassed() {
 Options options = new Options();
 CommandLineParser parser = new DefaultParser();
 try {
 CommandLine commandLine = parser.parse(options.toCommandLineParser(), new String[]{});
 assertEquals(0, commandLine.getArgList().size());
 } catch (ParseException e) {
 e.printStackTrace();
 fail("ParseException was thrown");
 }
 }

 @Test
 public void testOptionsWithArgumentsPassed() {
 Options options = new Options();
 options.addOption("a", "optionA", false, "Description for option A");
 CommandLineParser parser = new DefaultParser();
 try {
 CommandLine commandLine = parser.parse(options.toCommandLineParser(), new String[]{"arg1", "arg2"});
 assertEquals(2, commandLine.getArgList().size());
 assertEquals("arg1", commandLine.getArgList().get(0));
 assertEquals("arg2", commandLine.getArgList().get(1));
 } catch (ParseException e) {
 e.printStackTrace();
 fail("ParseException was thrown");
 }
 }

 @Test
 public void testOptionsWithEmptyArgumentsPassed() {
 Options options = new Options();
 options.addOption("a", "optionA", false, "Description for option A");
 CommandLineParser parser = new DefaultParser();
 try {
 CommandLine commandLine = parser.parse(options.toCommandLineParser(), new String[]{""});
 fail("ParseException was not thrown");
 } catch (ParseException e) {
 // expected
 }
 }

 @Test
 public void testOptionsWithInvalidOptionsPassed() {
 Options options = new Options();
 options.addOption("a", "optionA", false, "Description for option A");
 CommandLineParser parser = new DefaultParser();
 try {
 parser.parse(options.toCommandLineParser(), new String[]{"-z"});
 fail("ParseException was not thrown");
 } catch (ParseException e) {
 // expected
 }
 }

 @Test
 public void testOptionsWithMixedArgumentsAndOptionsPassed() {
 Options options = new Options();
 options.addOption("a", "optionA", false, "Description for option A");
 CommandLineParser parser = new DefaultParser();
 try {
 CommandLine commandLine = parser.parse(options.toCommandLineParser(), new String[]{"arg1", "-a", "arg2"});
 assertEquals(3, commandLine.getArgList().size());
 assertEquals("arg1", commandLine.getArgList().get(0));
 assertTrue(commandLine.hasOption("a"));
 assertEquals("arg2", commandLine.getArgList().get(2));
 } catch (ParseException e) {
 e.printStackTrace();
 fail("ParseException was thrown");
 }
 }

 @Test
 public void testOptionsWithOptionsGroups() {
 Options options = new Options();
 OptionGroup optionGroup = new OptionGroup("group", "Group description");
 options.addOptionGroup(optionGroup);
 optionGroup.addOption("a", "optionA", false, "Description for option A in group");
 optionGroup.addOption("b", "optionB", false, "Description for option B in group");
 options.addOption("c", "optionC", false, "Description for option C outside group");
 CommandLineParser parser = new DefaultParser();
 try {
 CommandLine commandLine = parser.parse(options.toCommandLineParser(), new String[]{"--group", "-a"});
 assertFalse(commandLine.hasOption("c"));
 assertTrue(commandLine.hasOption("a"));
 } catch (ParseException e) {
 e.printStackTrace();
 fail("ParseException was thrown");
 }
 }

 @Test(expected = ParseException.class)
 public void testOptionsWithInvalidLongOptionName() {
 Options options = new Options();
 options.addOption("a", "optionA", false, "Description for option A");
 try {
 options.addOption("123", "option123", false, "Description for option 123");
 fail("ParseException was not thrown");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = ParseException.class)
 public void testOptionsWithDuplicateShortOptionName() {
 Options options = new Options();
 options.addOption("a", "optionA", false, "Description for option A");
 options.addOption("a", "optionB", false, "Description for option B");
 try {
 options.parse("", new String[0]);
 fail("ParseException was not thrown");
 } catch (ParseException e) {
 // expected
 }
 }

 @Test(expected = ParseException.class)
 public void testOptionsWithDuplicateLongOptionName() {
 Options options = new Options();
 options.addOption("a", "optionA", false, "Description for option A");
 options.addOption("--optionA", "optionB", false, "Description for option B");
 try {
 options.parse("", new String[0]);
 fail("ParseException was not thrown");
 } catch (ParseException e) {
 // expected
 }
 }

 @Test(expected = ParseException.class)
 public void testOptionsWithInvalidShortOptionName() {
 Options options = new Options();
 try {
 options.addOption("0", "option0", false, "Description for option 0");
 fail("ParseException was not thrown");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = ParseException.class)
 public void testOptionsWithInvalidLongOptionName() {
 Options options = new Options();
 try {
 options.addOption("--0", "option0", false, "Description for option 0");
 fail("ParseException was not thrown");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }
}
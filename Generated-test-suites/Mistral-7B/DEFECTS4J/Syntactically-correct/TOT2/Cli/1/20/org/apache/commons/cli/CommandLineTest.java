import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

public class CommandLineTest {

 private static final String[] TEST_ARGS = {"--test", "arg1", "--option", "value", "--another-option"};

 @Test
 public void testParseBasic() {
 CommandLine commandLine = new CommandLine();
 CommandLineParser parser = new DefaultParser();

 try {
 parser.parse(new DefaultOptions(), TEST_ARGS, false);
 CommandLine parsedCommandLine = parser.getParsedCommandLine();
 commandLine.parse(TEST_ARGS);

 Assert.assertEquals(commandLine, parsedCommandLine);
 } catch (ParseException e) {
 // This should not occur as we are testing valid input
 Assert.fail(e.getMessage());
 }
 }

 @Test(expected = ParseException.class)
 public void testParseMissingRequiredOption() {
 CommandLine commandLine = new CommandLine();
 CommandLineParser parser = new DefaultParser();

 try {
 parser.parse(new Options().addOption(Option.builder("o").required(true).build()), TEST_ARGS, false);
 } catch (ParseException e) {
 // Expected behavior, missing required option
 Assert.assertEquals("Option 'o' is missing", e.getMessage());
 }
 }

 @Test
 public void testParseOptionalOption() {
 CommandLine commandLine = new CommandLine();
 CommandLineParser parser = new DefaultParser();

 try {
 parser.parse(new Options().addOption(Option.builder("o").build()), TEST_ARGS, false);
 CommandLine parsedCommandLine = parser.getParsedCommandLine();

 Assert.assertFalse(parsedCommandLine.hasOption("o"));
 commandLine = parsedCommandLine;

 // Test setting optional option
 commandLine.parse(TEST_ARGS.clone(), new String[]{"--o", "value"});
 Assert.assertTrue(commandLine.hasOption("o"));
 Assert.assertEquals("value", commandLine.getOptionValue("o"));
 } catch (ParseException e) {
 // This should not occur as we are testing valid input
 Assert.fail(e.getMessage());
 }
 }

 @Test
 public void testParseOptionWithArg() {
 CommandLine commandLine = new CommandLine();
 CommandLineParser parser = new DefaultParser();

 try {
 parser.parse(new Options().addOption(Option.builder("a").hasArg(true).build()), TEST_ARGS, false);
 CommandLine parsedCommandLine = parser.getParsedCommandLine();

 Assert.assertEquals("value", parsedCommandLine.getOptionValue("a"));
 commandLine = parsedCommandLine;

 // Test setting option with arg
 commandLine.parse(TEST_ARGS.clone(), new String[]{"--a", "value2"});
 Assert.assertEquals("value2", commandLine.getOptionValue("a"));
 } catch (ParseException e) {
 // This should not occur as we are testing valid input
 Assert.fail(e.getMessage());
 }
 }

 @Test
 public void testParseMultipleOptionsWithArg() {
 CommandLine commandLine = new CommandLine();
 CommandLineParser parser = new DefaultParser();

 try {
 parser.parse(new Options().addOption(Option.builder("a").hasArg(true).build())
 .addOption(Option.builder("b").hasArg(true).build()), TEST_ARGS, false);
 CommandLine parsedCommandLine = parser.getParsedCommandLine();

 Assert.assertEquals(2, parsedCommandLine.args.size());
 Assert.assertEquals("arg1", parsedCommandLine.getOptionValue("a"));
 Assert.assertEquals("value", parsedCommandLine.getOptionValue("b"));
 commandLine = parsedCommandLine;

 // Test setting multiple options with arg
 commandLine.parse(TEST_ARGS.clone(), new String[]{"--a", "arg2", "--b", "value2"});
 Assert.assertEquals("arg2", parsedCommandLine.getOptionValue("a"));
 Assert.assertEquals("value2", parsedCommandLine.getOptionValue("b"));
 } catch (ParseException e) {
 // This should not occur as we are testing valid input
 Assert.fail(e.getMessage());
 }
 }

 @Test
 public void testParseLongOptions() {
 CommandLine commandLine = new CommandLine();
 CommandLineParser parser = new DefaultParser();

 try {
 parser.parse(new Options().addOption(Option.builder("--long-option").build()), TEST_ARGS, false);
 CommandLine parsedCommandLine = parser.getParsedCommandLine();

 Assert.assertTrue(parsedCommandLine.hasOption("--long-option"));
 commandLine = parsedCommandLine;

 // Test setting long option
 commandLine.parse(TEST_ARGS.clone(), new String[]{"--long-option", "value"});
 Assert.assertTrue(commandLine.hasOption("--long-option"));
 Assert.assertEquals("value", commandLine.getOptionValue("--long-option"));
 } catch (ParseException e) {
 // This should not occur as we are testing valid input
 Assert.fail(e.getMessage());
 }
 }

 @Test
 public void testParseMixedShortAndLongOptions() {
 CommandLine commandLine = new CommandLine();
 CommandLineParser parser = new DefaultParser();

 try {
 parser.parse(new Options().addOption(Option.builder("o").build())
 .addOption(Option.builder("--long-option").build()), TEST_ARGS, false);
 CommandLine parsedCommandLine = parser.getParsedCommandLine();

 Assert.assertTrue(parsedCommandLine.hasOption("o"));
 commandLine = parsedCommandLine;

 // Test setting short option
 commandLine.parse(TEST_ARGS.clone(), new String[]{"--o"});
 Assert.assertTrue(commandLine.hasOption("o"));

 // Test setting long option
 commandLine.parse(TEST_ARGS.clone(), new String[]{"--long-option", "value"});
 Assert.assertTrue(commandLine.hasOption("--long-option"));
 Assert.assertEquals("value", commandLine.getOptionValue("--long-option"));
 } catch (ParseException e) {
 // This should not occur as we are testing valid input
 Assert.fail(e.getMessage());
 }
 }

 @Test
 public void testParseMissingValueOption() {
 CommandLine commandLine = new CommandLine();
 CommandLineParser parser = new DefaultParser();

 try {
 parser.parse(new Options().addOption(Option.builder("o").hasArg(true).build()), TEST_ARGS, false);
 CommandLine parsedCommandLine = parser.getParsedCommandLine();

 Assert.assertEquals(Collections.emptyList(), parsedCommandLine.args);
 commandLine = parsedCommandLine;

 // Test setting missing value option
 commandLine.parse(TEST_ARGS.clone(), new String[]{"--o"});
 Assert.assertEquals(Collections.emptyList(), commandLine.args);
 } catch (ParseException e) {
 // This should not occur as we are testing valid input
 Assert.fail(e.getMessage());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidOption() {
 CommandLine commandLine = new CommandLine();
 CommandLineParser parser = new DefaultParser();

 try {
 parser.parse(new Options().addOption(Option.builder("o").build()), TEST_ARGS.clone().append("--invalid"), false);
 } catch (ParseException e) {
 // This should not occur as we are testing valid input
 Assert.fail(e.getMessage());
 }

 CommandLine parsedCommandLine = parser.getParsedCommandLine();
 commandLine = parsedCommandLine;

 // Test setting invalid option
 commandLine.parse(TEST_ARGS.clone().append("--invalid"), new String[0]);
 }
}
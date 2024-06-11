import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class OptionsTest {

 private Options createTestOptions() {
 return new Options()
 .addOption("o", "output", false, "Output flag")
 .addOption("i", "input", true, "Input file")
 .addOptionGroup(new OptionGroup("g", "group")
 .addOption("g1", true, "Group option 1")
 .addOption("g2", false, "Group option 2")
 .addOption("g3", true, "Group option 3")
 );
 }

 @Test
 public void testAddOption() {
 Options options = createTestOptions();
 Option opt = options.getOption("o");
 Assert.assertEquals("Output flag", opt.getLongOpt(), opt.getDescription());
 }

 @Test
 public void testAddShortOption() {
 Options options = createTestOptions();
 Option opt = options.getOption("i");
 Assert.assertEquals("Input file", opt.getLongOpt(), opt.isRequired() ? null : opt.getArgs().length > 0 ? opt.getArgs()[0] : null);
 }

 @Test
 public void testAddLongOption() {
 Options options = createTestOptions();
 Option opt = options.getOption("g1");
 Assert.assertTrue(opt.isSelectableByDefault());
 }

 @Test
 public void testAddOptionGroup() {
 Options options = createTestOptions();
 OptionGroup group = options.getOptionGroup("g");
 Assert.assertEquals("group", group.getKey());
 }

 @Test(expected = ParseException.class)
 public void testParseWithInvalidOption() {
 Options options = createTestOptions();
 CommandLineParser parser = new DefaultParser();
 String[] args = {"--invalid"};
 try {
 parser.parse(options, args);
 } catch (ParseException e) {
 Assert.assertEquals("Unrecognized option: -invalid", e.getMessage());
 }
 }

 @Test(expected = ParseException.class)
 public void testParseMissingRequiredOption() {
 Options options = createTestOptions();
 CommandLineParser parser = new DefaultParser();
 String[] args = {"--o"};
 try {
 parser.parse(options, args);
 fail("Missing required option: -i");
 } catch (ParseException e) {
 // expected
 }
 }

 @Test
 public void testParseWithShortOption() {
 Options options = createTestOptions();
 CommandLineParser parser = new DefaultParser();
 String[] args = {"-o"};
 try {
 CommandLine commandLine = parser.parse(options, args);
 Option opt = options.getOption("o");
 Assert.assertTrue(commandLine.hasOption(opt.getKey()));
 } catch (ParseException e) {
 fail("Unexpected error parsing options: " + e.getMessage());
 }
 }

 @Test
 public void testParseWithLongOption() {
 Options options = createTestOptions();
 CommandLineParser parser = new DefaultParser();
 String[] args = {"--output"};
 try {
 CommandLine commandLine = parser.parse(options, args);
 Option opt = options.getOption("o");
 Assert.assertTrue(commandLine.hasOption(opt.getKey()));
 } catch (ParseException e) {
 fail("Unexpected error parsing options: " + e.getMessage());
 }
 }

 @Test
 public void testParseWithLongOptionValue() {
 Options options = createTestOptions();
 CommandLineParser parser = new DefaultParser();
 String[] args = {"--input", "test.txt"};
 try {
 CommandLine commandLine = parser.parse(options, args);
 Option opt = options.getOption("i");
 Assert.assertEquals("test.txt", opt.getValue());
 } catch (ParseException e) {
 fail("Unexpected error parsing options: " + e.getMessage());
 }
 }

 @Test
 public void testParseWithMultipleOptions() {
 Options options = createTestOptions();
 CommandLineParser parser = new DefaultParser();
 String[] args = {"-o", "--input", "test.txt", "--g1", "--g2"};
 try {
 CommandLine commandLine = parser.parse(options, args);
 Option optOutput = options.getOption("o");
 Option optInput = options.getOption("i");
 Option optGroup1 = options.getOption("g1");
 Option optGroup2 = options.getOption("g2");
 Assert.assertTrue(commandLine.hasOption(optOutput.getKey()));
 Assert.assertTrue(commandLine.hasOption(optInput.getKey()));
 Assert.assertTrue(commandLine.hasOption(optGroup1.getKey()));
 Assert.assertTrue(commandLine.hasOption(optGroup2.getKey()));
 } catch (ParseException e) {
 fail("Unexpected error parsing options: " + e.getMessage());
 }
 }

 @Test
 public void testParseWithOptionGroup() {
 Options options = createTestOptions();
 CommandLineParser parser = new DefaultParser();
 String[] args = {"--group"};
 try {
 CommandLine commandLine = parser.parse(options, args);
 OptionGroup group = options.getOptionGroup("g");
 Assert.assertTrue(commandLine.hasOption(group.getKey()));
 } catch (ParseException e) {
 fail("Unexpected error parsing options: " + e.getMessage());
 }
 }

 @Test
 public void testParseWithOptionGroupOptions() {
 Options options = createTestOptions();
 CommandLineParser parser = new DefaultParser();
 String[] args = {"--group", "--g1"};
 try {
 CommandLine commandLine = parser.parse(options, args);
 OptionGroup group = options.getOptionGroup("g");
 Option optGroup1 = group.getOption("g1");
 Assert.assertTrue(commandLine.hasOption(optGroup1.getKey()));
 } catch (ParseException e) {
 fail("Unexpected error parsing options: " + e.getMessage());
 }
 }

 @Test
 public void testParseWithMissingOptionGroup() {
 Options options = createTestOptions();
 CommandLineParser parser = new DefaultParser();
 String[] args = {"--g"};
 try {
 CommandLine commandLine = parser.parse(options, args);
 fail("Missing option group: --g");
 } catch (ParseException e) {
 // expected
 }
 }

 @Test
 public void testParseWithMixedOptionsAndOptionGroup() {
 Options options = createTestOptions();
 CommandLineParser parser = new DefaultParser();
 String[] args = {"--o", "--input", "--g", "--g1"};
 try {
 CommandLine commandLine = parser.parse(options, args);
 Option optOutput = options.getOption("o");
 Option optInput = options.getOption("i");
 OptionGroup group = options.getOptionGroup("g");
 Option optGroup1 = group.getOption("g1");
 Assert.assertTrue(commandLine.hasOption(optOutput.getKey()));
 Assert.assertTrue(commandLine.hasOption(optInput.getKey()));
 Assert.assertTrue(commandLine.hasOption(group.getKey()));
 Assert.assertTrue(commandLine.hasOption(optGroup1.getKey()));
 } catch (ParseException e) {
 fail("Unexpected error parsing options: " + e.getMessage());
 }
 }

 @Test
 public void testHelp() {
 Options options = createTestOptions();
 CommandLineParser parser = new DefaultParser();
 HelpFormatter formatter = new HelpFormatter();
 String[] args = {"--help"};
 try {
 CommandLine commandLine = parser.parse(options, args);
 formatter.printHelp("OptionsTest", options);
 } catch (ParseException e) {
 fail("Unexpected error parsing options: " + e.getMessage());
 }
 }

 @Test
 public void testHelpWithLongOptions() {
 Options options = createTestOptions();
 CommandLineParser parser = new DefaultParser();
 HelpFormatter formatter = new HelpFormatter();
 String[] args = {"--help", "--all"};
 try {
 CommandLine commandLine = parser.parse(options, args);
 formatter.printHelp("OptionsTest", options, true);
 } catch (ParseException e) {
 fail("Unexpected error parsing options: " + e.getMessage());
 }
 }
}
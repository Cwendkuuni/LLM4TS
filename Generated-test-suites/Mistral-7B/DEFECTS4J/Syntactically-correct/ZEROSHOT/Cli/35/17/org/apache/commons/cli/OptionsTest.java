import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

 private Options options;

 @Before
 public void setUp() {
 options = new Options();

 options.addOption("i", true, "inputFile")
 .longOpt("input")
 .desc("Input file");

 options.addOption("o", true, "outputFile")
 .longOpt("output")
 .desc("Output file");

 options.addOption("h", false, "help")
 .desc("Print this help message.");

 options.addOptionGroup(new OptionGroup("bg", "Background")
 .addOption(new Option("b", "background", true, "Background color"))
 .addOption(new Option("g", "geometry", true, "Geometry"))
 );
 }

 @Test
 public void testAddOption() {
 Option inputOption = options.getOption("i");
 Option inputLongOption = options.getOption("input");
 Option outputOption = options.getOption("o");
 Option outputLongOption = options.getOption("output");
 Option helpOption = options.getOption("h");

 assertEquals("inputFile", inputOption.getLongOpt());
 assertEquals("input", inputLongOption.getKey());
 assertEquals("Print this help message.", helpOption.getDesc());
 }

 @Test
 public void testHasOption() {
 assertTrue(options.hasOption("i"));
 assertTrue(options.hasOption("input"));
 assertTrue(options.hasOption("h"));
 assertTrue(options.hasOption("help"));
 assertFalse(options.hasOption("j"));
 }

 @Test
 public void testGetMatchingOptions() {
 List<String> matchingOptions = options.getMatchingOptions("in");
 assertEquals(2, matchingOptions.size());
 assertEquals("input", matchingOptions.get(0));
 assertEquals("inputFile", matchingOptions.get(1));
 }

 @Test
 public void testGetOptionGroup() {
 OptionGroup bgGroup = options.getOptionGroup("bg");
 Option backgroundOption = bgGroup.getOption("b");
 Option geometryOption = bgGroup.getOption("g");

 assertEquals("Background", bgGroup.getLongOpt());
 assertEquals("Background", backgroundOption.getLongOpt());
 assertEquals("Geometry", geometryOption.getLongOpt());
 }

 @Test
 public void testParse() {
 CommandLineParser parser = new DefaultParser();

 try {
 CommandLine commandLine = parser.parse(options, new String[]{"--input", "input.txt", "--output", "output.txt", "--help"});

 assertEquals(3, commandLine.getArgs().length);
 assertEquals("input.txt", commandLine.getOptionValue("input"));
 assertEquals("output.txt", commandLine.getOptionValue("output"));
 assertNull(commandLine.getOptionValue("j"));
 assertTrue(commandLine.hasOption("help"));
 } catch (ParseException e) {
 e.printStackTrace();
 fail();
 }
 }

 @Test
 public void testParseWithInvalidOption() {
 CommandLineParser parser = new DefaultParser();

 try {
 CommandLine commandLine = parser.parse(options, new String[]{"--invalid"});
 fail();
 } catch (ParseException e) {
 // expected
 }
 }

 @Test
 public void testParseWithMissingRequiredOption() {
 CommandLineParser parser = new DefaultParser();

 try {
 CommandLine commandLine = parser.parse(options, new String[]{"--output"});
 fail();
 } catch (ParseException e) {
 String message = e.getMessage();
 assertTrue(message.contains("The following required options do not have values:"));
 }
 }

 @Test
 public void testParseWithMissingShortOption() {
 CommandLineParser parser = new DefaultParser();

 try {
 CommandLine commandLine = parser.parse(options, new String[]{"--i", "input.txt", "--o", "output.txt"});
 fail();
 } catch (ParseException e) {
 String message = e.getMessage();
 assertTrue(message.contains("The following options don't have a long option:"));
 }
 }

 @Test
 public void testParseWithUnrecognizedOption() {
 CommandLineParser parser = new DefaultParser();

 try {
 CommandLine commandLine = parser.parse(options, new String[]{"--j", "input.txt", "--o", "output.txt"});
 fail();
 } catch (ParseException e) {
 String message = e.getMessage();
 assertTrue(message.contains("Unrecognized option:"));
 }
 }

 @Test
 public void testToString() {
 assertEquals("[ Options: [ short {i= inputFile, o= outputFile}, {} ], [ long {input= inputFile, output= outputFile}, input= inputFile, help=}, {} ]]", options.toString());
 }
}
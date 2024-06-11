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

 // Add options using short name only
 options.addOption("i", "inputFile", true, "Input file path");
 options.addOption("o", "outputFile", true, "Output file path");

 // Add options using long name only
 options.addOption("inputfile", "ifp", true, "Input file path");
 options.addOption("outputfile", "ofp", true, "Output file path");

 // Add options using both short and long names
 options.addOption("h", "help", false, "Print this help message.");
 options.addOption("help", true, false, "Print this help message.");
 }

 @Test
 public void testHasShortOption() {
 assertTrue(options.hasShortOption("i"));
 assertTrue(options.hasShortOption("h"));
 }

 @Test
 public void testHasLongOption() {
 assertTrue(options.hasLongOption("inputfile"));
 assertTrue(options.hasLongOption("help"));
 }

 @Test
 public void testHasOption() {
 assertTrue(options.hasOption("i"));
 assertTrue(options.hasOption("inputfile"));
 assertTrue(options.hasOption("h"));
 assertTrue(options.hasOption("help"));
 }

 @Test
 public void testGetOption() {
 Option inputOption = options.getOption("i");
 Option helpOption = options.getOption("h");

 assertEquals("Input file path", inputOption.getLongOpt());
 assertEquals("h", helpOption.getShortOpt());
 }

 @Test
 public void testGetMatchingOptions() {
 List<String> options = this.options.getMatchingOptions("io");
 assertEquals(2, options.size());
 assertEquals("inputfile", options.get(0));
 assertEquals("outputfile", options.get(1));
 }

 @Test
 public void testParse() {
 CommandLineParser parser = new DefaultParser();

 try {
 CommandLine cmd = parser.parse(options.parse(new String[]{"-i", "/tmp/input.txt", "-o", "/tmp/output.txt", "--help"}), false);

 assertEquals("/tmp/input.txt", cmd.getOptionValue("inputFile"));
 assertEquals("/tmp/output.txt", cmd.getOptionValue("outputFile"));
 assertNull(cmd.getOptionValue("help"));
 } catch (ParseException e) {
 fail("Parsing failed: " + e.getMessage());
 }
 }

 @Test
 public void testPrintHelp() {
 HelpFormatter formatter = new HelpFormatter();

 try {
 formatter.printHelp("MyApp", options, true);
 } catch (IllegalArgumentException e) {
 fail("Error printing help: " + e.getMessage());
 }
 }

}
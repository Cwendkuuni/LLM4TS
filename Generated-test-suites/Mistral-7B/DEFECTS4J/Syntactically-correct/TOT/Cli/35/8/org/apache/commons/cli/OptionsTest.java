import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

 @Test
 public void testSimpleOption() {
 Options options = new Options();
 options.addOption("i", "inputFile", true, "input file path");

 CommandLineParser parser = new DefaultParser();
 try {
 CommandLine cmd = parser.parse(options.toArray(), new String[]{"-i", "/path/to/input.txt"});
 assertTrue(cmd.hasOption("i"));
 assertEquals("/path/to/input.txt", cmd.getOptionValue("inputFile"));
 } catch (ParseException e) {
 e.printStackTrace();
 fail("Parsing failed.");
 }
 }

 @Test
 public void testMissingOption() {
 Options options = new Options();
 options.addOption("i", "inputFile", true, "input file path");

 CommandLineParser parser = new DefaultParser();
 try {
 CommandLine cmd = parser.parse(options.toArray(), new String[]{});
 fail("Parsing should have failed.");
 } catch (ParseException e) {
 // Expected
 }
 }

 @Test
 public void testLongOption() {
 Options options = new Options();
 options.addOption("io", "inputOutput", false, "Input and output flag");

 CommandLineParser parser = new DefaultParser();
 try {
 CommandLine cmd = parser.parse(options.toArray(), new String[]{"--inputOutput"});
 assertTrue(cmd.hasOption("io"));
 } catch (ParseException e) {
 e.printStackTrace();
 fail("Parsing failed.");
 }
 }

 @Test
 public void testOptionGroup() {
 Options options = new Options();
 OptionGroup group = new OptionGroup("g", "group");
 group.setRequired(true);
 group.addOption(new Option("a", "argument", true, "Argument for group"));
 options.addOptionGroup(group);

 CommandLineParser parser = new DefaultParser();
 try {
 CommandLine cmd = parser.parse(options.toArray(), new String[]{"-g", "-a", "argumentValue"});
 assertTrue(cmd.hasOption("a"));
 assertEquals("argumentValue", cmd.getOptionValue("argument"));
 assertTrue(cmd.hasOptionGroup("g"));
 } catch (ParseException e) {
 e.printStackTrace();
 fail("Parsing failed.");
 }
 }

 @Test(expected = ParseException.class)
 public void testMissingRequiredOption() {
 Options options = new Options();
 OptionGroup group = new OptionGroup("g", "group");
 group.setRequired(true);
 group.addOption(new Option("a", "argument", true, "Argument for group"));
 options.addOptionGroup(group);

 CommandLineParser parser = new DefaultParser();
 try {
 CommandLine cmd = parser.parse(options.toArray(), new String[]{});
 fail("Parsing should have failed.");
 } catch (ParseException e) {
 // Expected
 }
 }
}
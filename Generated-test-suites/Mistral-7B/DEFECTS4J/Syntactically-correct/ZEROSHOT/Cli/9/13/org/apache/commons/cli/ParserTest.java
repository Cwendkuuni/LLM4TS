import org.apache.commons.cli.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

 @Test
 public void testParse() throws ParseException {
 Options options = new Options();
 Option option = new Option("o", "output", true, "Output file");
 options.addOption(option);

 String[] args = {"-o", "output.txt", "--help"};
 Parser parser = new DefaultParser();
 CommandLine cmd = parser.parse(options, args, false);

 assertEquals("output.txt", cmd.getOptionValue("output"));
 assertNull(cmd.getOptionValue("help"));
 }

 @Test(expected = MissingArgumentException.class)
 public void testMissingArgument() throws ParseException {
 Options options = new Options();
 Option option = new Option("o", "output", true, "Output file");
 options.addOption(option);

 String[] args = {"-o"};
 Parser parser = new DefaultParser();
 parser.parse(options, args, false);
 }

 @Test(expected = MissingOptionException.class)
 public void testMissingRequiredOption() throws ParseException {
 Options options = new Options();
 Option option = new Option("o", "output", true, "Output file")
 .required(true);
 options.addOption(option);

 String[] args = {};
 Parser parser = new DefaultParser();
 try {
 parser.parse(options, args, false);
 } catch (ParseException e) {
 String expectedError = "Missing required option: output";
 assertEquals(expectedError, e.getMessage());
 }
 }

 @Test(expected = UnrecognizedOptionException.class)
 public void testUnrecognizedOption() throws ParseException {
 Options options = new Options();
 Option option = new Option("o", "output", true, "Output file");
 options.addOption(option);

 String[] args = {"--unknownOption"};
 Parser parser = new DefaultParser();
 try {
 parser.parse(options, args, false);
 } catch (ParseException e) {
 String expectedError = "Unrecognized option: unknownOption";
 assertEquals(expectedError, e.getMessage());
 }
 }

 @Test
 public void testParseWithProperties() throws ParseException {
 Options options = new Options();
 Option option = new Option("o", "output", true, "Output file");
 options.addOption(option);

 Properties properties = new Properties();
 properties.setProperty("output", "output.txt");

 String[] args = {};
 Parser parser = new DefaultParser();
 CommandLine cmd = parser.parse(options, args, properties, false);

 assertEquals("output.txt", cmd.getOptionValue("output"));
 }

 @Test
 public void testParseWithStopAtNonOption() throws ParseException {
 Options options = new Options();
 Option option = new Option("o", "output", true, "Output file");
 options.addOption(option);

 String[] args = {"-o", "output.txt", "-x", "arg"};
 Parser parser = new DefaultParser();
 CommandLine cmd = parser.parse(options, args, false, true);

 assertEquals(2, cmd.getArgs().size());
 assertEquals("arg", cmd.getArgs().get(1));
 }
}
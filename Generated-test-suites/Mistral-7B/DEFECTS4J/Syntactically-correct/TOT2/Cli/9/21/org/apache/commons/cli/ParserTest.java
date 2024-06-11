import org.apache.commons.cli.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

 private Parser parser;

 @Before
 public void setup() {
 Options options = new Options();
 parser = new DefaultParser();
 options.addRequiredOption("i", "input", true, "Input file path");
 options.addOption("o", "output", true, "Output file path");
 parser.setOptions(options);
 }

 @Test
 public void testParse() throws ParseException {
 String[] args = new String[]{"-i", "/path/to/input.txt", "-o", "/path/to/output.txt"};
 CommandLine cmd = parser.parse(args);
 assertEquals("-i", cmd.getOptions().getOptionValues("i").get(0));
 assertEquals("/path/to/input.txt", cmd.getOptions().getOptionValues("i").get(0));
 assertEquals("-o", cmd.getOptions().getOptionValues("o").get(0));
 assertEquals("/path/to/output.txt", cmd.getOptions().getOptionValues("o").get(0));
 assertEquals(0, cmd.getArgs().size());
 }

 @Test(expected = MissingOptionException.class)
 public void testMissingRequiredOption() throws ParseException {
 String[] args = new String[]{"-o", "/path/to/output.txt"};
 try {
 parser.parse(args);
 } catch (ParseException e) {
 List<String> requiredOptions = parser.getOptions().getRequiredOptions();
 String missingOption = requiredOptions.get(0);
 assertEquals(missingOption, e.getMessage().split(": ")[1]);
 }
 }

 @Test(expected = UnrecognizedOptionException.class)
 public void testUnrecognizedOption() throws ParseException {
 String[] args = new String[]{"--unknown-option"};
 try {
 parser.parse(args);
 } catch (ParseException e) {
 assertEquals("Unrecognized option: --unknown-option", e.getMessage());
 }
 }

 @Test(expected = MissingArgumentException.class)
 public void testMissingArgument() throws ParseException {
 String[] args = new String[]{"-i", "input.txt"};
 try {
 parser.parse(args);
 } catch (ParseException e) {
 assertEquals("Missing argument for option: i", e.getMessage());
 }
 }

 @Test
 public void testParseWithProperties() throws ParseException {
 Properties properties = new Properties();
 properties.setProperty("input", "/path/to/input.txt");
 properties.setProperty("output", "/path/to/output.txt");
 CommandLine cmd = parser.parse(new String[0], properties);
 assertEquals("-i", cmd.getOptions().getOptionValues("i").get(0));
 assertEquals("/path/to/input.txt", cmd.getOptions().getOptionValues("i").get(0));
 assertEquals("-o", cmd.getOptions().getOptionValues("o").get(0));
 assertEquals("/path/to/output.txt", cmd.getOptions().getOptionValues("o").get(0));
 assertEquals(0, cmd.getArgs().size());
 }
}
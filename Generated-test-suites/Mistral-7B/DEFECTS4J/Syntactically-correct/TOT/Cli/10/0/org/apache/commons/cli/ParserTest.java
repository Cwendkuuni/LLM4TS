import org.apache.commons.cli.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

 private static final String[] ARGS_WITH_REQUIRED_OPTIONS = {"-o", "output.txt", "--input", "input.txt"};
 private static final String[] ARGS_WITH_INVALID_OPTION = {"--invalid"};
 private static final String[] ARGS_WITH_MISSING_REQUIRED_OPTIONS = {"--input", "input.txt"};
 private static final String[] ARGS_WITH_NO_OPTIONS = {};

 @Test
 public void testParsingWithRequiredOptions() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 options.addRequiredOption("o", "output", true, "Output file name");
 options.addRequiredOption("i", "input", true, "Input file name");
 parser.setOptions(options);
 CommandLine cmd = parser.parse(options, ARGS_WITH_REQUIRED_OPTIONS);
 assertEquals("output.txt", cmd.getOptionValue("output"));
 assertEquals("input.txt", cmd.getOptionValue("input"));
 }

 @Test(expected = MissingOptionException.class)
 public void testParsingMissingRequiredOptions() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 options.addRequiredOption("o", "output", true, "Output file name");
 options.addRequiredOption("i", "input", true, "Input file name");
 parser.setOptions(options);
 CommandLine cmd = parser.parse(options, ARGS_WITH_MISSING_REQUIRED_OPTIONS);
 fail("Missing required options exception should have been thrown.");
 }

 @Test(expected = UnrecognizedOptionException.class)
 public void testParsingInvalidOption() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 parser.setOptions(options);
 CommandLine cmd = parser.parse(options, ARGS_WITH_INVALID_OPTION);
 fail("Unrecognized option exception should have been thrown.");
 }

 @Test
 public void testParsingNoOptions() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 parser.setOptions(options);
 CommandLine cmd = parser.parse(options, ARGS_WITH_NO_OPTIONS);
 assertTrue(cmd.getArgs().length == 0);
 }

}
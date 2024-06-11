import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.NoSuchOptionException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class ParserTest {

 private static final String[] TEST_ARGUMENTS = {"--input", "test.txt", "-o", "output.txt", "--help"};

 @Test
 public void testParse() throws ParseException {
 // Given
 Options options = new Options();
 options.addRequiredOption("input", true, "i", "input file");
 options.addOption("o", true, "o", "output file");
 options.addOption("help", false, null, "print this message");

 CommandLineParser parser = new DefaultParser();

 // When
 CommandLine cmd = parser.parse(options, TEST_ARGUMENTS, false);

 // Then
 Assert.assertEquals("input file", "test.txt", cmd.getOptionValue("input"));
 Assert.assertEquals("output file", "output.txt", cmd.getOptionValue("o"));
 Assert.assertFalse("help should not be set", cmd.hasOption("help"));
 }

 @Test(expected = MissingArgumentException.class)
 public void testMissingArgument() throws ParseException {
 // Given
 Options options = new Options();
 options.addOption("input", true, "i", "input file");
 options.addOption("o", true, "o", "output file");

 CommandLineParser parser = new DefaultParser();

 // When
 CommandLine cmd = parser.parse(options, TEST_ARGUMENTS, false);

 // Then an exception should be thrown
 }

 @Test(expected = MissingOptionException.class)
 public void testMissingOption() throws ParseException {
 // Given
 Options options = new Options();
 options.addOption("input", true, "i", "input file");

 CommandLineParser parser = new DefaultParser();

 // When
 CommandLine cmd = parser.parse(options, TEST_ARGUMENTS, false);

 // Then an exception should be thrown
 }

 @Test(expected = NoSuchOptionException.class)
 public void testNoSuchOption() throws ParseException {
 // Given
 Options options = new Options();
 options.addOption("input", true, "i", "input file");
 options.addOption("o", true, "o", "output file");
 options.addOption("unknown", true, "u", "unknown option");

 CommandLineParser parser = new DefaultParser();

 // When
 CommandLine cmd = parser.parse(options, TEST_ARGUMENTS, false);

 // Then an exception should be thrown
 }

 @Test
 public void testHelp() throws ParseException {
 // Given
 Options options = new Options();
 options.addRequiredOption("input", true, "i", "input file");
 options.addOption("o", true, "o", "output file");
 options.addOption("help", false, null, "print this message");

 CommandLineParser parser = new DefaultParser();

 // When
 CommandLine cmd = parser.parse(options, TEST_ARGUMENTS, true);

 // Then the help message should be printed
 }

 @Test
 public void testEmptyArguments() throws ParseException {
 // Given
 Options options = new Options();
 options.addRequiredOption("input", true, "i", "input file");
 options.addOption("o", true, "o", "output file");

 CommandLineParser parser = new DefaultParser();

 // When
 CommandLine cmd = parser.parse(options, new String[0], false);

 // Then an exception should be thrown
 }
}
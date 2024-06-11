import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class ParserTest {

 private static final String[] ARGUMENTS = {"--input", "inputFile.txt", "--output", "outputFile.txt", "-o", "outputDirectory", "--help"};

 @Test
 public void testParseWithValidArguments() throws ParseException {
 // Given
 Options options = new Options();
 options.addRequiredOption("input", true, "i", "Input file");
 options.addRequiredOption("output", true, "o", "Output file or directory");
 options.addOption("help", false, "h", "Print help message");
 Parser parser = new DefaultParser();

 // When
 CommandLine cmd = parser.parse(options, ARGUMENTS);

 // Then
 assertEquals("inputFile.txt", cmd.getOptionValue("input"));
 assertEquals("outputFile.txt", cmd.getOptionValue("output"));
 assertNull(cmd.getOptionValue("outputDirectory"));
 assertFalse(cmd.hasOption("help"));
 }

 @Test
 public void testParseWithMissingRequiredOptions() {
 // Given
 Options options = new Options();
 options.addRequiredOption("input", true, "i", "Input file");
 options.addRequiredOption("output", true, "o", "Output file or directory");
 Parser parser = new DefaultParser();

 // When and Then
 assertThrows(MissingOptionException.class, () -> {
 parser.parse(options, new String[0]);
 });
 }

 @Test
 public void testParseWithInvalidOption() {
 // Given
 Options options = new Options();
 Parser parser = new DefaultParser();

 // When and Then
 assertThrows(UnrecognizedOptionException.class, () -> {
 parser.parse(options, new String[]{"--invalid"});
 });
 }

 @Test
 public void testParseWithPartialArguments() {
 // Given
 Options options = new Options();
 options.addRequiredOption("input", true, "i", "Input file");
 options.addRequiredOption("output", true, "o", "Output file or directory");
 Parser parser = new DefaultParser();

 // When
 CommandLine cmd = parser.parse(options, new String[]{"--input"});

 // Then
 assertEquals(1, cmd.getArgs().size());
 assertEquals("", cmd.getArgs().get(0));
 }

 @Test
 public void testParseWithStopAtNonOption() {
 // Given
 Options options = new Options();
 options.addRequiredOption("input", true, "i", "Input file");
 options.addRequiredOption("output", true, "o", "Output file or directory");
 Parser parser = new DefaultParser();

 // When
 CommandLine cmd = parser.parse(options, ARGUMENTS, true);

 // Then
 assertEquals(4, cmd.getArgs().size());
 assertEquals("inputFile.txt", cmd.getArgs().get(0));
 assertEquals("outputDirectory", cmd.getArgs().get(1));
 assertNull(cmd.getArgs().get(2));
 assertNull(cmd.getArgs().get(3));
 }

 @Test
 public void testParseWithProperties() {
 // Given
 Options options = new Options();
 options.addRequiredOption("input", true, "i", "Input file");
 options.addRequiredOption("output", true, "o", "Output file or directory");
 options.addOption("help", false, "h", "Print help message");
 Parser parser = new DefaultParser();

 // When
 Properties properties = new Properties();
 properties.setProperty("input", "inputFile.txt");
 properties.setProperty("output", "outputDirectory");
 CommandLine cmd = parser.parse(options, null, properties, false);

 // Then
 assertEquals("inputFile.txt", cmd.getOptionValue("input"));
 assertEquals("outputDirectory", cmd.getOptionValue("output"));
 assertNull(cmd.getOptionValue("help"));
 }

}
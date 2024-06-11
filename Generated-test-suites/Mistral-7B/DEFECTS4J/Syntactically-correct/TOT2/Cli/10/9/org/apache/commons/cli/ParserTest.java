import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class ParserTest {

 private static final String[] ARGUMENTS = {"--option1", "--option2=value", "-o", "value", "--", "-a", "arg1", "--requiredOption"};
 private static final Properties PROPERTIES = new Properties();
 static {
 PROPERTIES.setProperty("option1", "value1");
 PROPERTIES.setProperty("option2", "value2");
 }

 @Test
 public void testParse() throws ParseException {
 // Given
 CommandLineParser parser = new DefaultParser();
 Options options = new Options();

 // When
 CommandLine cmd = parser.parse(options, ARGUMENTS, PROPERTIES);

 // Then
 assertEquals(6, cmd.getArgs().size());
 assertEquals("value1", cmd.getOptionValue("option1"));
 assertEquals("value2", cmd.getOptionValue("option2"));
 assertNull(cmd.getOptionValue("nonexistentOption"));
 assertEquals(5, options.getOptions().size());
 assertEquals("value", cmd.getOptionValue("o"));
 }

 @Test
 public void testParseWithStopAtNonOption() throws ParseException {
 // Given
 CommandLineParser parser = new DefaultParser();
 Options options = new Options();

 // When
 CommandLine cmd = parser.parse(options, ARGUMENTS, PROPERTIES, true);

 // Then
 assertEquals(7, cmd.getArgs().size());
 assertEquals(null, cmd.getOptionValue("nonexistentOption"));
 }

 @Test
 public void testMissingOption() {
 // Given
 CommandLineParser parser = new DefaultParser();
 Options options = new Options();
 options.addRequiredOption("requiredOption", false, "r", "ro");

 // When & Then
 assertThrows(MissingOptionException.class, () -> parser.parse(options, new String[0]));
 }

 @Test
 public void testUnknownOption() {
 // Given
 CommandLineParser parser = new DefaultParser();
 Options options = new Options();

 // When & Then
 assertThrows(UnrecognizedOptionException.class, () -> parser.parse(options, new String[]{"--unknownOption"}));
 }
}
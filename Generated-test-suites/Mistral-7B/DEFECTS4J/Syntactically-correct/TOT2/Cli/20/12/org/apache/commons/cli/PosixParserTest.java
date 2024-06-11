import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for PosixParser.
 */
public class PosixParserTest {

 private static final String[] TEST_ARGUMENTS = new String[]{"--foo", "-a", "-b", "-c=123", "--option-with-args=value1 value2", "--option-without-value", "-d", "--"};

 @Test
 public void testFlatten() throws ParseException {
 // Given
 PosixParser parser = new PosixParser();
 Options options = new Options();

 // When
 String[] result = parser.flatten(options, TEST_ARGUMENTS, true);

 // Then
 Assert.assertArrayEquals(new String[]{"--foo", "-a", "-b", "--option-with-args", "value1", "value2", "--option-without-value", "-c", "123", "--d"}, result);
 }

 @Test(expected = ParseException.class)
 public void testFlattenWithStopAtNonOptionFalseAndInvalidToken() throws ParseException {
 // Given
 PosixParser parser = new PosixParser();
 Options options = new Options();

 // When
 parser.flatten(options, new String[]{"--foo", "--bar=value", "invalid"}, false);

 // Then
 // ParseException should be thrown
 }

 @Test(expected = ParseException.class)
 public void testFlattenWithStopAtNonOptionTrueAndInvalidToken() throws ParseException {
 // Given
 PosixParser parser = new PosixParser();
 Options options = new Options();

 // When
 parser.flatten(options, new String[]{"--foo", "--bar=value", "invalid"}, true);

 // Then
 // ParseException should be thrown
 }

 @Test(expected = ParseException.class)
 public void testFlattenWithInvalidOption() throws ParseException {
 // Given
 PosixParser parser = new PosixParser();
 Options options = new Options();
 options.addOption("invalidOption", false, false, "Invalid option");

 // When
 parser.flatten(options, TEST_ARGUMENTS, false);

 // Then
 // ParseException should be thrown
 }

 @Test
 public void testFlattenWithEmptyArgumentList() throws ParseException {
 // Given
 PosixParser parser = new PosixParser();
 Options options = new Options();

 // When
 String[] result = parser.flatten(options, new String[0], true);

 // Then
 Assert.assertArrayEquals(new String[]{}, result);
 }
}
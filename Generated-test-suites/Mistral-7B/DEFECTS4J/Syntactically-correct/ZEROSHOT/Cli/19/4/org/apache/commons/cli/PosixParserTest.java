import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class PosixParserTest {

 private Options options;
 private String[] args;

 @Before
 public void setUp() {
 options = new Options();
 options.addOption("o", "option", true, "option description");
 options.addOption("a", "another-option", false, "another option description");
 }

 @After
 public void tearDown() {
 options = null;
 args = null;
 }

 @Test
 public void testFlattenSimple() throws ParseException {
 String[] expected = new String[]{"o", "option", "value"};
 args = new String[]{"--option", "value"};
 String[] actual = new PosixParser().flatten(options, args, false);
 assertArrayEquals(expected, actual);
 }

 @Test
 public void testFlattenWithNoOption() throws ParseException {
 args = new String[]{"--", "value"};
 String[] actual = new PosixParser().flatten(options, args, false);
 String[] expected = new String[]{"--", "value"};
 assertArrayEquals(expected, actual);
 }

 @Test
 public void testFlattenWithSingleOption() throws ParseException {
 String[] expected = new String[]{"o"};
 args = new String[]{"-o"};
 String[] actual = new PosixParser().flatten(options, args, false);
 assertArrayEquals(expected, actual);
 }

 @Test
 public void testFlattenWithSingleOptionValue() throws ParseException {
 String[] expected = new String[]{"o", "value"};
 args = new String[]{"-o", "value"};
 String[] actual = new PosixParser().flatten(options, args, false);
 assertArrayEquals(expected, actual);
 }

 @Test
 public void testFlattenWithTwoOptions() throws ParseException {
 String[] expected = new String[]{"o", "option", "a"};
 args = new String[]{"-o", "option", "-a"};
 String[] actual = new PosixParser().flatten(options, args, false);
 assertArrayEquals(expected, actual);
 }

 @Test
 public void testFlattenWithTwoOptionsValues() throws ParseException {
 String[] expected = new String[]{"o", "option", "value1", "a", "value2"};
 args = new String[]{"-o", "option", "value1", "-a", "value2"};
 String[] actual = new PosixParser().flatten(options, args, false);
 assertArrayEquals(expected, actual);
 }

 @Test
 public void testFlattenWithLongOption() throws ParseException {
 String[] expected = new String[]{"longOption", "value"};
 args = new String[]{"--longOption", "value"};
 String[] actual = new PosixParser().flatten(options, args, false);
 assertArrayEquals(expected, actual);
 }

 @Test
 public void testFlattenWithLongOptionValue() throws ParseException {
 String[] expected = new String[]{"longOption", "value1", "value2"};
 args = new String[]{"--longOption", "value1=value2"};
 String[] actual = new PosixParser().flatten(options, args, false);
 assertArrayEquals(expected, actual);
 }

 @Test
 public void testFlattenWithStopAtNonOption() throws ParseException {
 String[] expected = new String[]{"a", "nonOption", "--", "value"};
 args = new String[]{"-a", "--", "--", "nonOption", "--", "value"};
 String[] actual = new PosixParser().flatten(options, args, true);
 assertArrayEquals(expected, actual);
 }

 @Test
 public void testFlattenWithNoStopAtNonOption() throws ParseException {
 String[] expected = new String[]{"a", "--", "nonOption"};
 args = new String[]{"-a", "--", "--", "nonOption"};
 String[] actual = new PosixParser().flatten(options, args, false);
 assertArrayEquals(expected, actual);
 }

 @Test(expected = ParseException.class)
 public void testFlattenWithInvalidOption() throws ParseException {
 args = new String[]{"-z"};
 new PosixParser().flatten(options, args, false);
 }

 @Test
 public void testProcessOptionToken() {
 options = new Options();
 options.addOption("o", "option", true, "option description");
 String[] args = new String[]{"-o", "value1"};
 new PosixParser().processOptionToken("-o", false);
 assertEquals("o", currentOption.getOpt());
 assertEquals("value1", currentOption.getValue());
 assertNull(currentOption.getValues());
 }

 @Test
 public void testProcessOptionTokenWithMultipleValues() {
 options = new Options();
 options.addOption("o", "option", true, "option description");
 String[] args = new String[]{"-o", "value1", "value2"};
 new PosixParser().processOptionToken("-o", false);
 assertEquals("o", currentOption.getOpt());
 assertEquals(Arrays.asList(new String[]{"value1", "value2"}), currentOption.getValues());
 }
}
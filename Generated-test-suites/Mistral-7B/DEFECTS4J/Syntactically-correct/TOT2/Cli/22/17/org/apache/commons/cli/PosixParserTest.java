import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class PosixParserTest {

 private static final String[] ARGUMENTS_NO_OPTIONS = new String[]{"java", "MyProgram"};
 private static final String[] ARGUMENTS_LONG_OPTION = new String[]{"-D", "--debug"};
 private static final String[] ARGUMENTS_LONG_OPTION_VALUE = new String[]{"-D", "--debug", "true"};
 private static final String[] ARGUMENTS_SHORT_OPTION = new String[]{"-d", "--debug"};
 private static final String[] ARGUMENTS_SHORT_OPTION_VALUE = new String[]{"-d", "--debug"};
 private static final String[] ARGUMENTS_SHORT_OPTION_VALUE_LONG = new String[]{"-d", "--debug", "true"};
 private static final String[] ARGUMENTS_LONG_OPTION_NO_VALUE = new String[]{"--debug"};
 private static final String[] ARGUMENTS_LONG_OPTION_NO_VALUE_MULTIPLE = new String[]{"--debug", "--debug"};
 private static final String[] ARGUMENTS_UNRECOGNIZED = new String[]{"--unknown"};
 private static final String[] ARGUMENTS_MIXED = new String[]{"-d", "--debug"};
 private static final String[] ARGUMENTS_MIXED_UNRECOGNIZED = new String[]{"-d", "--debug", "--unknown"};
 private static final String[] ARGUMENTS_MIXED_UNRECOGNIZED_LONG = new String[]{"-d", "--debug", "--unknown"};
 private static final String[] ARGUMENTS_EMPTY = new String[0];
 private static final String[] ARGUMENTS_STOP_AT_NON_OPTION = new String[]{"-d", "--debug", "MyProgram"};

 @Test
 public void testFlattenNoOptions() throws ParseException {
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(new Options(), ARGUMENTS_NO_OPTIONS, false);
 Assert.assertArrayEquals(ARGUMENTS_NO_OPTIONS, result);
 }

 @Test
 public void testFlattenLongOption() throws ParseException {
 Options options = new Options();
 options.addOption("debug", false, true, "Print debug information");
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, ARGUMENTS_LONG_OPTION, false);
 Assert.assertArrayEquals(new String[]{"--debug"}, result);
 }

 @Test
 public void testFlattenLongOptionValue() throws ParseException {
 Options options = new Options();
 options.addOption("debug", false, true, "Print debug information");
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, ARGUMENTS_LONG_OPTION_VALUE, false);
 Assert.assertArrayEquals(new String[]{"--debug", "true"}, result);
 }

 @Test(expected = ParseException.class)
 public void testFlattenLongOptionValueError() throws ParseException {
 Options options = new Options();
 options.addOption("debug", false, true, "Print debug information");
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, ARGUMENTS_MIXED_UNRECOGNIZED_LONG, false);
 }

 @Test
 public void testFlattenShortOption() throws ParseException {
 Options options = new Options();
 options.addOption("d", false, true, "Print debug information");
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, ARGUMENTS_SHORT_OPTION, false);
 Assert.assertArrayEquals(new String[]{"-d"}, result);
 }

 @Test
 public void testFlattenShortOptionValue() throws ParseException {
 Options options = new Options();
 options.addOption("d", false, true, "Print debug information");
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, ARGUMENTS_SHORT_OPTION_VALUE, false);
 Assert.assertArrayEquals(new String[]{"-d"}, result);
 }

 @Test
 public void testFlattenShortOptionValueLong() throws ParseException {
 Options options = new Options();
 options.addOption("d", false, true, "Print debug information");
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, ARGUMENTS_SHORT_OPTION_VALUE_LONG, false);
 Assert.assertArrayEquals(new String[]{"-d", "true"}, result);
 }

 @Test
 public void testFlattenLongOptionNoValue() throws ParseException {
 Options options = new Options();
 options.addOption("debug", true, false, "Print debug information");
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, ARGUMENTS_LONG_OPTION_NO_VALUE, false);
 Assert.assertArrayEquals(new String[]{"--debug"}, result);
 }

 @Test
 public void testFlattenLongOptionNoValueMultiple() throws ParseException {
 Options options = new Options();
 options.addOption("debug", true, false, "Print debug information");
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, ARGUMENTS_LONG_OPTION_NO_VALUE_MULTIPLE, false);
 Assert.assertArrayEquals(new String[]{"--debug", "--debug"}, result);
 }

 @Test(expected = ParseException.class)
 public void testFlattenUnrecognizedOption() throws ParseException {
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(new Options(), ARGUMENTS_UNRECOGNIZED, false);
 }

 @Test
 public void testFlattenMixedOptions() throws ParseException {
 Options options = new Options();
 options.addOption("d", false, true, "Print debug information");
 options.addOption("debug", false, true, "Print debug information");
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, ARGUMENTS_MIXED, false);
 Assert.assertArrayEquals(new String[]{"-d", "--debug"}, result);
 }

 @Test(expected = ParseException.class)
 public void testFlattenMixedOptionsUnrecognized() throws ParseException {
 Options options = new Options();
 options.addOption("d", false, true, "Print debug information");
 options.addOption("debug", false, true, "Print debug information");
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, ARGUMENTS_MIXED_UNRECOGNIZED, false);
 }

 @Test
 public void testFlattenStopAtNonOption() throws ParseException {
 Options options = new Options();
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, ARGUMENTS_STOP_AT_NON_OPTION, true);
 List<String> expected = Arrays.asList(ARGUMENTS_NO_OPTIONS);
 Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), result);
 }
}
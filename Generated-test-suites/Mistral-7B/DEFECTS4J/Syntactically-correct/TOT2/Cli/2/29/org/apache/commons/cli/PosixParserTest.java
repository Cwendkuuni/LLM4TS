import org.apache.commons.cli.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

 @Test
 public void testFlattenEmptyArgs() {
 Options options = new Options();
 String[] arguments = {};
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, arguments, false);
 assertArrayEquals(new String[]{}, result);
 }

 @Test
 public void testFlattenSimpleArgs() {
 Options options = new Options();
 String[] arguments = {"--option1", "-o", "value1", "--option2=value2"};
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, arguments, false);
 assertArrayEquals(new String[]{"--option1", "-o", "--option2=value2"}, result);
 }

 @Test
 public void testFlattenSpecialTokens() {
 Options options = new Options();
 String[] arguments = {"--", "--option1", "--", "-o", "--option2=value2"};
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, arguments, false);
 assertArrayEquals(new String[]{"--", "--option1", "--option2=value2"}, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFlattenInvalidOption() {
 Options options = new Options();
 String[] arguments = {"--invalidOption"};
 PosixParser parser = new PosixParser();
 parser.flatten(options, arguments, false);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFlattenInvalidOptionWithValue() {
 Options options = new Options();
 String[] arguments = {"--invalidOption=value"};
 PosixParser parser = new PosixParser();
 parser.flatten(options, arguments, false);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFlattenInvalidTwoCharOption() {
 Options options = new Options();
 String[] arguments = {"--.."};
 PosixParser parser = new PosixParser();
 parser.flatten(options, arguments, false);
 }

 @Test
 public void testFlattenLongOptionWithValue() {
 Options options = new Options();
 options.addOption("longOption1", false, "l1");
 options.addOption("longOption2", true, "l2");
 String[] arguments = {"--longOption1", "--longOption2"};
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, arguments, false);
 assertArrayEquals(new String[]{"--longOption1", "--longOption2"}, result);
 }

 @Test
 public void testFlattenLongOptionWithValueAndValue() {
 Options options = new Options();
 options.addOption("longOption1", false, "l1");
 options.addOption("longOption2", true, "l2");
 String[] arguments = {"--longOption1", "--longOption2=value"};
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, arguments, false);
 assertArrayEquals(new String[]{"--longOption1", "--longOption2=value"}, result);
 }

 @Test
 public void testFlattenLongOptionWithMultipleValues() {
 Options options = new Options();
 options.addOption("longOption1", true, "l1");
 String[] arguments = {"--longOption1", "value1", "--longOption1", "value2"};
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, arguments, false);
 assertArrayEquals(new String[]{"--longOption1", "value1", "--longOption1", "value2"}, result);
 }

 @Test
 public void testFlattenMixedShortAndLongOptions() {
 Options options = new Options();
 options.addOption("o", true, "o");
 options.addOption("longOption1", false, "l1");
 options.addOption("longOption2", true, "l2");
 String[] arguments = {"-o", "value", "--longOption1", "--longOption2"};
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, arguments, false);
 assertArrayEquals(new String[]{"-o", "value", "--longOption1", "--longOption2"}, result);
 }

 @Test
 public void testFlattenMixedShortAndLongOptionsWithValues() {
 Options options = new Options();
 options.addOption("o", true, "o");
 options.addOption("longOption1", false, "l1");
 options.addOption("longOption2", true, "l2");
 String[] arguments = {"-o", "value", "--longOption1=value1", "--longOption2=value2"};
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, arguments, false);
 assertArrayEquals(new String[]{"-o", "value", "--longOption1=value1", "--longOption2=value2"}, result);
 }

 @Test
 public void testFlattenWithStopAtNonOption() {
 Options options = new Options();
 String[] arguments = {"--option1", "-o", "--nonOption"};
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, arguments, true);
 assertArrayEquals(new String[]{"--option1", "-o"}, result);
 }

 @Test
 public void testFlattenWithBursting() {
 Options options = new Options();
 options.addOption("l", true, "l");
 String[] arguments = {"-l--longOption", "-l--longOption2"};
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, arguments, false);
 assertArrayEquals(new String[]{"-l--longOption", "-l--longOption2"}, result);
 }

 @Test
 public void testFlattenWithBurstingAndStopAtNonOption() {
 Options options = new Options();
 options.addOption("l", true, "l");
 String[] arguments = {"-l--longOption", "-l--longOption2", "--nonOption"};
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, arguments, true);
 assertArrayEquals(new String[]{"-l--longOption"}, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFlattenWithInvalidBurstedOption() {
 Options options = new Options();
 options.addOption("l", true, "l");
 String[] arguments = {"-l--invalid", "-l--longOption"};
 PosixParser parser = new PosixParser();
 parser.flatten(options, arguments, false);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFlattenWithInvalidBurstedOptionAndStopAtNonOption() {
 Options options = new Options();
 options.addOption("l", true, "l");
 String[] arguments = {"-l--invalid", "-l--longOption", "--nonOption"};
 PosixParser parser = new PosixParser();
 parser.flatten(options, arguments, true);
 }
}
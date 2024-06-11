import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class PosixParserTest {

 private String[] getTestArgs() {
 return new String[]{"--foo=bar", "--foo", "-a", "-b", "--", "-c", "--help", "--version"};
 }

 @Test
 public void testFlattenNormalUseCase() {
 Options options = new Options();
 PosixParser parser = new PosixParser();
 String[] args = getTestArgs();
 String[] expected = {"--foo", "bar", "--", "-a", "-b", "--", "--help", "--version"};
 String[] result = parser.flatten(options, args, false);
 assertArrayEquals(expected, result);
 }

 @Test(expected = ParseException.class)
 public void testFlattenInvalidOption() {
 Options options = new Options();
 PosixParser parser = new PosixParser();
 String[] args = new String[]{"--invalidOption"};
 parser.flatten(options, args, false);
 }

 @Test
 public void testFlattenLongOptionWithValue() {
 Options options = new Options();
 PosixParser parser = new PosixParser();
 String[] args = new String[]{"--longOption=value"};
 String[] expected = {"--longOption", "value"};
 String[] result = parser.flatten(options, args, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenLongOptionWithoutValue() {
 Options options = new Options();
 PosixParser parser = new PosixParser();
 String[] args = new String[]{"--longOption"};
 String[] expected = {"--longOption"};
 String[] result = parser.flatten(options, args, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenWithMultipleLongOptions() {
 Options options = new Options();
 PosixParser parser = new PosixParser();
 String[] args = new String[]{"--longOption1", "--longOption2", "--longOption3"};
 String[] expected = {"--longOption1", "--longOption2", "--longOption3"};
 String[] result = parser.flatten(options, args, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenTwoShortOptions() {
 Options options = new Options();
 PosixParser parser = new PosixParser();
 String[] args = new String[]{"-a", "-b"};
 String[] expected = {"-a", "-b"};
 String[] result = parser.flatten(options, args, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenShortOptionWithValue() {
 Options options = new Options();
 PosixParser parser = new PosixParser();
 String[] args = new String[]{"-a=value"};
 String[] expected = {"-a", "value"};
 String[] result = parser.flatten(options, args, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenShortOptionWithoutValue() {
 Options options = new Options();
 PosixParser parser = new PosixParser();
 String[] args = new String[]{"-a"};
 String[] expected = {"-a"};
 String[] result = parser.flatten(options, args, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenShortOptionWithArg() {
 Options options = new Options();
 options.addOption("a", true, "arg");
 PosixParser parser = new PosixParser();
 String[] args = new String[]{"-a"};
 String[] expected = {"-a"};
 String[] result = parser.flatten(options, args, false);
 assertArrayEquals(expected, result);
 options.clearOptions();
 String[] args2 = new String[]{"--", "-a", "arg"};
 String[] expected2 = {"--", "-a", "arg"};
 String[] result2 = parser.flatten(options, args2, false);
 assertArrayEquals(expected2, result2);
 }

 @Test
 public void testFlattenStopAtNonOption() {
 Options options = new Options();
 PosixParser parser = new PosixParser();
 String[] args = new String[]{"--foo", "-a", "-b"};
 String[] expected = {"--foo", "-a"};
 String[] result = parser.flatten(options, args, true);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenStopAtNonOption_LastArgIsOption() {
 Options options = new Options();PosixParser parser = new PosixParser();
 String[] args = new String[]{"--foo", "-a", "--someOption"};
 String[] expected = {"--foo", "-a"};
 String[] result = parser.flatten(options, args, true);
 assertArrayEquals(expected, result);
 }

 @Test(expected = ParseException.class)
 public void testFlattenStopAtNonOption_LastArgIsNotOption() {
 Options options = new Options();
 PosixParser parser = new PosixParser();
 String[] args = new String[]{"--foo", "-a", "arg"};
 parser.flatten(options, args, true);
 }

 @Test
 public void testFlattenStopAtNonOption_FirstArgIsOption() {
 Options options = new Options();
 PosixParser parser = new PosixParser();
 String[] args = new String[]{"--foo", "--", "-a", "-b"};
 String[] expected = {"--foo"};
 String[] result = parser.flatten(options, args, true);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenWithLongArgument() {
 Options options = new Options();
 PosixParser parser = new PosixParser();
 String[] args = new String[]{"--longOption=longArgument"};
 String[] expected = {"--longOption", "longArgument"};
 String[] result = parser.flatten(options, args, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenWithMultipleLongArguments() {
 Options options = new Options();
 PosixParser parser = new PosixParser();
 String[] args = new String[]{"--longOption1=longArgument1", "--longOption2=longArgument2"};
 String[] expected = {"--longOption1", "longArgument1", "--longOption2", "longArgument2"};
 String[] result = parser.flatten(options, args, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenWithShortOptionAndLongArgument() {
 Options options = new Options();
 PosixParser parser = new PosixParser();
 String[] args = new String[]{"-a", "--longOption=longArgument"};
 String[] expected = {"-a", "--longOption", "longArgument"};
 String[] result = parser.flatten(options, args, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenWithMultipleShortOptionsAndLongArguments() {
 Options options = new Options();
 PosixParser parser = new PosixParser();
 String[] args = new String[]{"-a", "-b", "--longOption1=longArgument1", "--longOption2=longArgument2"};
 String[] expected = {"-a", "-b", "--longOption1", "longArgument1", "--longOption2", "longArgument2"};
 String[] result = parser.flatten(options, args, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenWithMultipleShortOptionsAndLongArguments_FirstArgIsLongOption() {
 Options options = new Options();
 PosixParser parser = new PosixParser();
 String[] args = new String[]{"--longOption1=longArgument1", "-a", "-b"};
 String[] expected = {"--longOption1", "longArgument1", "-a", "-b"};
 String[] result = parser.flatten(options, args, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenWithInvalidArguments() {
 Options options = new Options();
 PosixParser parser = new PosixParser();
 String[] args = new String[]{"--invalidOption", "--longOption", "value", "--longOption=value"};
 try {
 parser.flatten(options, args, false);
 } catch (ParseException e) {
 assertEquals("Unrecognized option: invalidOption", e.getMessage());
 }
 }

 @Test
 public void testFlattenWithEmptyArguments() {
 Options options = new Options();
 PosixParser parser = new PosixParser();
 String[] args = new String[]{};
 String[] expected = new String[]{};
 String[] result = parser.flatten(options, args, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testProcess() {
 PosixParser parser = new PosixParser();
 String[] args = new String[]{"--foo=bar", "--foo", "-a", "-b", "--", "-c", "--help", "--version"};
 String[] expected = {"--foo", "bar"};
 parser.init();
 String[] result = new String[parser.tokens.size()];
 for (int i = 0; i < parser.tokens.size(); i++) {
 result[i] = (String) parser.tokens.get(i);
 }
 parser.process(expected[0]);
 assertArrayEquals(new String[]{"--foo", "--"}, result);
 parser.init();
 result = new String[parser.tokens.size()];
 for (int i = 0; i < parser.tokens.size(); i++) {
 result[i] = (String) parser.tokens.get(i);
 }
 parser.process("--");
 assertArrayEquals(new String[]{"--"}, result);
 parser.init();
 result = new String[parser.tokens.size()];
 for (int i = 0; i < parser.tokens.size(); i++) {
 result[i] = (String) parser.tokens.get(i);
 }
 parser.process("--foo=bar");
 parser.process("bar");
 assertArrayEquals(new String[]{"--foo", "bar"}, result);
 parser.init();
 result = new String[parser.tokens.size()];
 for (int i = 0; i < parser.tokens.size(); i++) {
 result[i] = (String) parser.tokens.get(i);
 }
 parser.process("--foo");
 parser.process("--");
 assertArrayEquals(new String[]{"--foo"}, result);
 parser.init();
 result = new String[parser.tokens.size()];
 for (int i = 0; i < parser.tokens.size(); i++) {
 result[i] = (String) parser.tokens.get(i);
 }
 parser.process("--help");
 assertNull(parser.currentOption);
 assertArrayEquals(new String[]{"--help"}, result);
 parser.init();
 result = new String[parser.tokens.size()];
 for (int i = 0; i < parser.tokens.size(); i++) {
 result[i] = (String) parser.tokens.get(i);
 }
 parser.process("-a");
 parser.process("-b");
 assertNull(parser.currentOption);
 assertArrayEquals(new String[]{"-a", "-b"}, result);
 parser.init();
 result = new String[parser.tokens.size()];
 for (int i = 0; i < parser.tokens.size(); i++) {
 result[i] = (String) parser.tokens.get(i);
 }
 parser.process("-a");
 parser.process("--");
 parser.process("-c");
 assertNull(parser.currentOption);
 assertArrayEquals(new String[]{"-a", "--", "-c"}, result);
 }

}
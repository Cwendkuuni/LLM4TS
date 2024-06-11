import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.*;

public class GnuParserTest {

 @Test
 public void testFlattenWithSingleOption() {
 Options options = new Options();
 String[] arguments = new String[]{"-DmyOption"};
 GnuParser parser = new GnuParser();
 String[] flattenedArguments = parser.flatten(options, arguments, false);
 assertArrayEquals(new String[]{"-D", "myOption"}, flattenedArguments);
 }

 @Test
 public void testFlattenWithMultipleOptions() {
 Options options = new Options();
 String[] arguments = new String[]{"-DmyOption1", "-DmyOption2"};
 GnuParser parser = new GnuParser();
 String[] flattenedArguments = parser.flatten(options, arguments, false);
 assertArrayEquals(new String[]{"-D", "myOption1", "-D", "myOption2"}, flattenedArguments);
 }

 @Test
 public void testFlattenWithLongOption() {
 Options options = new Options();
 String[] arguments = new String[]{"--longOptionValue"};
 GnuParser parser = new GnuParser();
 String[] flattenedArguments = parser.flatten(options, arguments, false);
 assertArrayEquals(new String[]{"--", "longOptionValue"}, flattenedArguments);
 }

 @Test
 public void testFlattenWithLongOptionAndValue() {
 Options options = new Options();
 String[] arguments = new String[]{"--longOptionValue1", "--longOptionValue2=value2"};
 GnuParser parser = new GnuParser();
 String[] flattenedArguments = parser.flatten(options, arguments, false);
 assertArrayEquals(new String[]{"--", "longOptionValue1", "--longOptionValue2", "value2"}, flattenedArguments);
 }

 @Test(expected = ParseException.class)
 public void testFlattenWithNonOptionBeforeSingleOption() {
 Options options = new Options();
 String[] arguments = new String[]{"a", "-DmyOption"};
 GnuParser parser = new GnuParser();
 try {
 parser.flatten(options, arguments, false);
 fail("ParseException expected");
 } catch (ParseException e) {
 // expected
 }
 }

 @Test(expected = ParseException.class)
 public void testFlattenWithNonOptionBeforeLongOption() {
 Options options = new Options();
 String[] arguments = new String[]{"a", "--longOptionValue"};
 GnuParser parser = new GnuParser();
 try {
 parser.flatten(options, arguments, false);
 fail("ParseException expected");
 } catch (ParseException e) {
 // expected
 }
 }

 @Test(expected = ParseException.class)
 public void testFlattenWithInvalidOption() {
 Options options = new Options();
 String[] arguments = new String[]{"-InvalidOption"};
 GnuParser parser = new GnuParser();
 try {
 parser.flatten(options, arguments, false);
 fail("ParseException expected");
 } catch (ParseException e) {
 // expected
 }
 }

 @Test(expected = ParseException.class)
 public void testFlattenWithInvalidLongOption() {
 Options options = new Options();
 String[] arguments = new String[]{"--InvalidLongOption"};
 GnuParser parser = new GnuParser();
 try {
 parser.flatten(options, arguments, false);
 fail("ParseException expected");
 } catch (ParseException e) {
 // expected
 }
 }

 @Test(expected = ParseException.class)
 public void testFlattenWithMultipleInvalidOptions() {
 Options options = new Options();
 String[] arguments = new String[]{"-InvalidOption1", "-InvalidOption2"};
 GnuParser parser = new GnuParser();
 try {
 parser.flatten(options, arguments, false);
 fail("ParseException expected");
 } catch (ParseException e) {
 // expected
 }
 }

 @Test(expected = ParseException.class)
 public void testFlattenWithMultipleInvalidLongOptions() {
 Options options = new Options();
 String[] arguments = new String[]{"--InvalidLongOption1", "--InvalidLongOption2"};
 GnuParser parser = new GnuParser();
 try {
 parser.flatten(options, arguments, false);
 fail("ParseException expected");
 } catch (ParseException e) {
 // expected
 }
 }

 @Test
 public void testFlattenWithStopAtNonOption() {
 Options options = new Options();
 String[] arguments = new String[]{"-DmyOption", "nonOption"};
 GnuParser parser = new GnuParser();
 String[] flattenedArguments = parser.flatten(options, arguments, true);
 assertArrayEquals(new String[]{"-D", "myOption"}, flattenedArguments);
 }

 @Test
 public void testFlattenWithDoubleDash() {
 Options options = new Options();
 String[] arguments = new String[]{"--"};
 GnuParser parser = new GnuParser();
 String[] flattenedArguments = parser.flatten(options, arguments, false);
 assertArrayEquals(new String[]{"--"}, flattenedArguments);
 }

 @Test
 public void testFlattenWithDoubleDashAndOption() {
 Options options = new Options();
 String[] arguments = new String[]{"--", "-DmyOption"};
 GnuParser parser = new GnuParser();
 String[] flattenedArguments = parser.flatten(options, arguments, false);
 assertArrayEquals(new String[]{"--", "-D", "myOption"}, flattenedArguments);
 }

 @Test
 public void testFlattenWithDoubleDashAndValue() {
 Options options = new Options();
 String[] arguments = new String[]{"--", "--longOptionValue"};
 GnuParser parser = new GnuParser();
 String[] flattenedArguments = parser.flatten(options, arguments, false);
 assertArrayEquals(new String[]{"--", "--longOptionValue"}, flattenedArguments);
 }
}
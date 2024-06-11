import org.apache.commons.cli.*;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class GnuParserTest {

 @Test
 public void testFlatten() {
 Options options = new Options();
 String[] arguments = new String[]{"-Dproperty=value", "--foo", "-bar=value", "arg1", "--", "arg2", "arg3"};
 GnuParser parser = new GnuParser();

 String[] result = parser.flatten(options, arguments, false);
 String[] expected = new String[]{"-D", "property", "value", "--", "foo", "arg1", "--", "arg2", "arg3"};
 assertArrayEquals(expected, result);

 result = parser.flatten(options, arguments, true);
 expected = new String[]{"-D", "property", "value"};
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenWithStopAtNonOptionFalse() {
 options = new Options();
 arguments = new String[]{"-Dproperty=value", "--foo", "-bar=value", "arg1", "--", "arg2", "arg3"};
 parser = new GnuParser();

 result = parser.flatten(options, arguments, false);
 expected = new String[]{"-D", "property", "value", "--", "foo", "arg1", "--", "arg2", "arg3"};
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenWithStopAtNonOptionTrue() {
 options = new Options();
 arguments = new String[]{"-Dproperty=value", "--foo", "-bar=value", "arg1", "--", "arg2", "arg3"};
 parser = new GnuParser();

 result = parser.flatten(options, arguments, true);
 expected = new String[]{"-D", "property", "value"};
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenWithDashAndDoubleDash() {
 options = new Options();
 arguments = new String[]{"-Dproperty=value", "--", "-", "-D property=value"};
 parser = new GnuParser();

 result = parser.flatten(options, arguments, false);
 expected = new String[]{"-D", "property", "value", "--", "-", "-D", "property", "value"};
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenWithSingleDashOption() {
 options = new Options();
 arguments = new String[]{"-Dproperty=value", "--", "-", "-D property", "value"};
 parser = new GnuParser();

 result = parser.flatten(options, arguments, false);
 expected = new String[]{"-D", "property", "value", "--", "-", "-D", "property", "value"};
 assertArrayEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFlattenWithUnknownOption() {
 options = new Options();
 arguments = new String[]{"-Dproperty=value", "--", "unknownOption", "--", "arg2", "arg3"};
 parser = new GnuParser();

 parser.flatten(options, arguments, false);
 }
}
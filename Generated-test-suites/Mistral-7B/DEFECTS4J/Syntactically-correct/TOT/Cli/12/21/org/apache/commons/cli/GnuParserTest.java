import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class GnuParserTest {

 @Test
 public void testFlattenSimpleArgs() {
 Options options = new Options();
 String[] arguments = new String[]{"arg1", "arg2", "arg3"};
 GnuParser parser = new GnuParser();

 String[] flattenedArgs = parser.flatten(options, arguments, false);
 String[] expectedFlattenedArgs = new String[]{"arg1", "-", "arg2", "-", "arg3"};
 Assert.assertArrayEquals(expectedFlattenedArgs, flattenedArgs);
 }

 @Test
 public void testFlattenLongOption() {
 Options options = new Options();
 options.addLongOption("longopt", false, true, "A long option");
 String[] arguments = new String[]{"--longopt", "value"};
 GnuParser parser = new GnuParser();

 String[] flattenedArgs = parser.flatten(options, arguments, false);
 String[] expectedFlattenedArgs = new String[]{"--longopt", "value"};
 Assert.assertArrayEquals(expectedFlattenedArgs, flattenedArgs);
 }

 @Test
 public void testFlattenShortOption() {
 Options options = new Options();
 options.addOption("o", false, true, "An short option");
 String[] arguments = new String[]{"-o", "value"};
 GnuParser parser = new GnuParser();

 String[] flattenedArgs = parser.flatten(options, arguments, false);
 String[] expectedFlattenedArgs = new String[]{"-o", "value"};
 Assert.assertArrayEquals(expectedFlattenedArgs, flattenedArgs);
 }

 @Test
 public void testFlattenSingleShortOption() {
 Options options = new Options();
 String[] arguments = new String[]{"-o"};
 GnuParser parser = new GnuParser();

 String[] flattenedArgs = parser.flatten(options, arguments, false);
 String[] expectedFlattenedArgs = new String[]{"-o"};
 Assert.assertArrayEquals(expectedFlattenedArgs, flattenedArgs);
 }

 @Test
 public void testFlattenShortOptionWithValue() {
 Options options = new Options();
 options.addOption("o", true, true, "An short option with value");
 String[] arguments = new String[]{"-o", "value"};
 GnuParser parser = new GnuParser();

 String[] flattenedArgs = parser.flatten(options, arguments, false);
 String[] expectedFlattenedArgs = new String[]{"-o", "value"};
 Assert.assertArrayEquals(expectedFlattenedArgs, flattenedArgs);
 }

 @Test
 public void testFlattenMixedArgs() {
 Options options = new Options();
 options.addOption("o", false, true, "An short option");
 options.addLongOption("longopt", false, true, "A long option");
 String[] arguments = new String[]{"arg1", "--longopt", "-o", "arg4", "--", "arg5", "arg6"};
 GnuParser parser = new GnuParser();

 String[] flattenedArgs = parser.flatten(options, arguments, false);
 String[] expectedFlattenedArgs = new String[]{"arg1", "-", "arg1", "--longopt", "-o", "arg4", "--", "arg5", "arg6"};
 Assert.assertArrayEquals(expectedFlattenedArgs, flattenedArgs);
 }

 @Test(expected = ParseException.class)
 public void testFlattenInvalidOption() {
 Options options = new Options();
 String[] arguments = new String[]{"--invalid"};
 GnuParser parser = new GnuParser();

 String[] flattenedArgs = parser.flatten(options, arguments, false);
 }

 @Test
 public void testFlattenStopAtNonOption() {
 Options options = new Options();
 String[] arguments = new String[]{"arg1", "--", "arg3", "arg4"};
 GnuParser parser = new GnuParser();

 String[] flattenedArgs = parser.flatten(options, arguments, true);
 String[] expectedFlattenedArgs = new String[]{"arg1", "-", "arg3"};
 Assert.assertArrayEquals(expectedFlattenedArgs, flattenedArgs);
 }
}
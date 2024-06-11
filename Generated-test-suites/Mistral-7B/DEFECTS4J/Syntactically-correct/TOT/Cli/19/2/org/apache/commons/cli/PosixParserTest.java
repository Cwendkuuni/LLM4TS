import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;

public class PosixParserTest {

 private List tokens;
 private String[] arguments;
 private boolean stopAtNonOption;
 private Options options;

 @Before
 public void setup() {
 options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", 1, "Option B with an integer value");
 options.addOption("c", 2, "Option C with an integer value and mandatory argument");
 }

 @Test
 public void testFlattenBasic() {
 String[] args = new String[]{"-a", "-b", "--optionC=valueC", "-c argC", "--"};
 tokens = new PosixParser().flatten(options, args, false);
 Assert.assertArrayEquals(new String[]{"-a", "-b", "--optionC", "valueC", "-c", "argC"}, tokens.toArray(new String[0]));
 }

 @Test(expected = ParseException.class)
 public void testFlattenInvalidOption() {
 String[] args = new String[]{"-x", "--", "-a", "-b"};
 try {
 new PosixParser().flatten(options, args, false);
 } catch (ParseException e) {
 // expected
 }
 }

 @Test
 public void testFlattenStopAtNonOption() {
 String[] args = new String[]{"-a", "--nonOption", "-b"};
 tokens = new PosixParser().flatten(options, args, true);
 Assert.assertArrayEquals(new String[]{"-a", "--nonOption", "-b"}, tokens.toArray(new String[0]));
 }

 @Test
 public void testFlattenLongOptionWithValue() {
 String[] args = new String[]{"--optionB=valueB", "-a", "-b"};
 tokens = new PosixParser().flatten(options, args, false);
 Assert.assertArrayEquals(new String[]{"--optionB", "valueB", "-a", "-b"}, tokens.toArray(new String[0]));
 }

 @Test
 public void testFlattenLongOptionWithoutValue() {
 String[] args = new String[]{"--optionB", "-a", "-b"};
 tokens = new PosixParser().flatten(options, args, false);
 Assert.assertArrayEquals(new String[]{"--optionB", "-a", "-b"}, tokens.toArray(new String[0]));
 }

 @Test
 public void testFlattenOptionWithArgument() {
 String[] args = new String[]{"-c", "argC", "-a", "-b"};
 tokens = new PosixParser().flatten(options, args, false);
 Assert.assertArrayEquals(new String[]{"-c", "argC", "-a", "-b"}, tokens.toArray(new String[0]));
 }

 @Test(expected = ParseException.class)
 public void testFlattenInvalidLongOption() {
 String[] args = new String[]{"--invalidOption", "-a", "-b"};
 try {
 new PosixParser().flatten(options, args, false);
 } catch (ParseException e) {
 // expected
 }
 }

 @Test
 public void testFlattenMultipleOptionsWithValues() {
 String[] args = new String[]{"--optionA=valueA", "--optionB=valueB"};
 tokens = new PosixParser().flatten(options, args, false);
 Assert.assertArrayEquals(new String[]{"--optionA", "valueA", "--optionB", "valueB"}, tokens.toArray(new String[0]));
 }
}
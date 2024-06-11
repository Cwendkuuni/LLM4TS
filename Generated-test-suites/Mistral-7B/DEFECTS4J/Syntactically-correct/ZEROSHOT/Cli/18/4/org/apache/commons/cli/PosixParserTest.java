import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

public class PosixParserTest {

 @Test
 public void testFlattenSimple() {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", 1, "Option B with an argument");

 String[] args = {"-a", "--b", "arg", "-c"};
 try {
 String[] parsedArgs = new PosixParser().parse(options, args);
 Assert.assertArrayEquals(new String[]{"-a", "--b", "arg"}, parsedArgs);
 } catch (ParseException e) {
 // Should not reach here
 }
 }

 @Test
 public void testFlattenLongOption() {
 Options options = new Options();
 options.addOption("longOption", true, "Long option");

 String[] args = {"--longOption"};
 try {
 String[] parsedArgs = new PosixParser().parse(options, args);
 Assert.assertArrayEquals(new String[]{"--longOption"}, parsedArgs);
 } catch (ParseException e) {
 // Should not reach here
 }
 }

 @Test
 public void testFlattenDoubleDash() {
 Options options = new Options();
 options.addOption("a", true, "Option A");

 String[] args = {"--", "a"};
 try {
 String[] parsedArgs = new PosixParser().parse(options, args);
 Assert.assertArrayEquals(new String[]{"--", "a"}, parsedArgs);
 } catch (ParseException e) {
 // Should not reach here
 }
 }

 @Test
 public void testFlattenStopAtNonOption() {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", 1, "Option B with an argument");
 options.addOption("c", 1, "Option C with an argument");

 String[] args = {"-a", "--c", "arg", "-d"};
 try {
 String[] parsedArgs = new PosixParser().parse(options, args, true);
 Assert.assertArrayEquals(new String[]{"-a", "--c", "arg", "-d"}, parsedArgs);
 } catch (ParseException e) {
 // Should not reach here
 }
 }

 @Test
 public void testFlattenBursting() {
 Options options = new Options();
 options.addOption("ab", true, "Option AB");
 options.addOption("cd", 1, "Option CD with an argument");

 String[] args = {"-abcd"};
 try {
 String[] parsedArgs = new PosixParser().parse(options, args);
 Assert.assertArrayEquals(new String[]{"-ab", "-cd"}, parsedArgs);
 } catch (ParseException e) {
 // Should not reach here
 }
 }

 @Test(expected = ParseException.class)
 public void testFlattenInvalidOption() {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", 1, "Option B with an argument");

 String[] args = {"-invalidOption"};
 try {
 new PosixParser().parse(options, args);
 } catch (ParseException e) {
 // Expected
 }
 }
}
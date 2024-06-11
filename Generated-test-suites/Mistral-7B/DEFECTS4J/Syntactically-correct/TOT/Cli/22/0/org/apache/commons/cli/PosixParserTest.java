import org.apache.commons.cli.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

 @Test
 public void testFlattenSimpleOptions() {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", 1, "Option B");
 options.addOption("c", 3, "Option C with an argument");

 String[] args = {"-a", "--b", "-c", "arg"};
 String[] expected = {"-a", "--b", "-c", "arg"};
 String[] result = new PosixParser().flatten(options, args, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenLongOptions() {
 Options options = new Options();
 options.addOption("foo", true, "Option Foo");
 options.addOption("bar", 1, "Option Bar");
 options.addOption("baz", 3, "Option Baz with an argument");

 String[] args = {"--foo", "--bar=value", "--baz", "arg"};
 String[] expected = {"--foo", "--bar=value", "--baz", "arg"};
 String[] result = new PosixParser().flatten(options, args, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenMixedOptions() {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", 1, "Option B");
 options.addOption("c", 3, "Option C with an argument");

 String[] args = {"--foo", "-a", "--bar=value", "-c", "arg"};
 String[] expected = {"--foo", "-a", "--bar=value", "-c", "arg"};
 String[] result = new PosixParser().flatten(options, args, false);
 assertArrayEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFlattenInvalidOption() {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", 1, "Option B");

 String[] args = {"--invalid"};
 try {
 new PosixParser().flatten(options, args, false);
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testFlattenInvalidOptionWithStopAtNonOption() {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", 1, "Option B");

 String[] args = {"--invalid", "--b", "value"};
 String[] expected = {"--b", "value"};
 String[] result = new PosixParser().flatten(options, args, true);
 assertArrayEquals(expected, result);
 }
}
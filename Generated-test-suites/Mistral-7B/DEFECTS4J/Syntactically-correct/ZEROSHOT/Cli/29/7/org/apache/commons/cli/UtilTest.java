import org.apache.commons.cli.Util;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class UtilTest {

 @Test
 public void testStripLeadingHyphens() {
 String input = "--test";
 String expectedOutput = "test";
 String output = Util.stripLeadingHyphens(input);
 assertEquals(expectedOutput, output);

 input = "test";
 output = Util.stripLeadingHyphens(input);
 assertEquals(input, output);

 input = "--";
 output = Util.stripLeadingHyphens(input);
 assertEquals(input, output);

 input = null;
 output = Util.stripLeadingHyphens(input);
 assertEquals(null, output);
 }

 @Test
 public void testStripLeadingAndTrailingQuotes() {
 String input = "\"test\"";
 String expectedOutput = "test";
 String output = Util.stripLeadingAndTrailingQuotes(input);
 assertEquals(expectedOutput, output);

 input = "test";
 output = Util.stripLeadingAndTrailingQuotes(input);
 assertEquals(input, output);

 input = "\"test\"";
 output = Util.stripLeadingAndTrailingQuotes(input);
 assertEquals(input.substring(1, input.length() - 1), output);

 input = null;
 output = Util.stripLeadingAndTrailingQuotes(input);
 assertEquals(null, output);
 }
}
import org.apache.commons.cli.Util;
import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest {

 @Test
 public void testStripLeadingHyphens() {
 String inputStr = "--some-string";
 String expectedOutput = "some-string";
 String outputStr = Util.stripLeadingHyphens(inputStr);
 assertEquals(expectedOutput, outputStr);

 inputStr = "some-string";
 expectedOutput = "some-string";
 outputStr = Util.stripLeadingHyphens(inputStr);
 assertEquals(expectedOutput, outputStr);

 inputStr = "string";
 expectedOutput = "string";
 outputStr = Util.stripLeadingHyphens(inputStr);
 assertEquals(expectedOutput, outputStr);
 }

 @Test
 public void testStripLeadingAndTrailingQuotes() {
 String inputStr = "\"one two\"";
 String expectedOutput = "one two";
 String outputStr = Util.stripLeadingAndTrailingQuotes(inputStr);
 assertEquals(expectedOutput, outputStr);

 inputStr = "one two";
 expectedOutput = "one two";
 outputStr = Util.stripLeadingAndTrailingQuotes(inputStr);
 assertEquals(expectedOutput, outputStr);

 inputStr = "\"\"";
 expectedOutput = "";
 outputStr = Util.stripLeadingAndTrailingQuotes(inputStr);
 assertEquals(expectedOutput, outputStr);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testStripLeadingHyphensThrowsExceptionForNullInput() {
 String inputStr = null;
 Util.stripLeadingHyphens(inputStr);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testStripLeadingAndTrailingQuotesThrowsExceptionForNullInput() {
 String inputStr = null;
 Util.stripLeadingAndTrailingQuotes(inputStr);
 }
}
import org.apache.commons.cli.Util;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UtilTest {

 @Test
 public void testStripLeadingHyphens() {
 String str = "--Test-String";
 String expected = "Test-String";
 String actual = Util.stripLeadingHyphens(str);
 Assertions.assertEquals(expected, actual);

 str = "Test-String";
 expected = "Test-String";
 actual = Util.stripLeadingHyphens(str);
 Assertions.assertEquals(expected, actual);

 str = null;
 actual = Util.stripLeadingHyphens(str);
 Assertions.assertNull(actual);
 }

 @Test
 public void testStripLeadingAndTrailingQuotes() {
 String str = "\"Test-String\"";
 String expected = "Test-String";
 String actual = Util.stripLeadingAndTrailingQuotes(str);
 Assertions.assertEquals(expected, actual);

 str = "Test-String";
 expected = "Test-String";
 actual = Util.stripLeadingAndTrailingQuotes(str);
 Assertions.assertEquals(expected, actual);

 str = "";
 actual = Util.stripLeadingAndTrailingQuotes(str);
 Assertions.assertEquals(expected, actual);
 }

 @Test
 public void testStripLeadingAndTrailingQuotesWithSingleQuote() {
 String str = "'Test-String'";
 String expected = "Test-String";
 String actual = Util.stripLeadingAndTrailingQuotes(str);
 Assertions.assertEquals(expected, actual);
 }

 @Test
 public void testStripLeadingAndTrailingQuotesWithNoQuotes() {
 String str = "Test-String";
 String expected = "Test-String";
 String actual = Util.stripLeadingAndTrailingQuotes(str);
 Assertions.assertEquals(expected, actual);
 }

 @Test
 public void testStripLeadingAndTrailingQuotesWithEmptyString() {
 String str = "";
 String expected = "";
 String actual = Util.stripLeadingAndTrailingQuotes(str);
 Assertions.assertEquals(expected, actual);
 }
}
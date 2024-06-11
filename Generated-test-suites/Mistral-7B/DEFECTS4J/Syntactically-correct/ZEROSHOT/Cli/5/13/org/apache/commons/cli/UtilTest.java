import org.apache.commons.cli.Util;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class UtilTest {

 @Test
 public void testStripLeadingHyphens() {
 String strWithLeadingHyphens = "--option";
 String expected = "option";
 String result = Util.stripLeadingHyphens(strWithLeadingHyphens);
 assertEquals(expected, result);

 String strWithoutLeadingHyphens = "option";
 String strWithSingleLeadingHyphen = "-option";
 String strWithNoLeadingHyphens = "";
 String resultWithoutLeadingHyphens = Util.stripLeadingHyphens(strWithoutLeadingHyphens);
 String resultWithSingleLeadingHyphen = Util.stripLeadingHyphens(strWithSingleLeadingHyphen);
 String resultWithNoLeadingHyphens = Util.stripLeadingHyphens(strWithNoLeadingHyphens);
 assertEquals(strWithoutLeadingHyphens, resultWithoutLeadingHyphens);
 assertEquals(str, resultWithSingleLeadingHyphen);
 assertEquals("", resultWithNoLeadingHyphens);
 }

 @Test
 public void testStripLeadingAndTrailingQuotes() {
 String strWithLeadingAndTrailingQuotes = "\"one two\"";
 String expected = "one two";
 String result = Util.stripLeadingAndTrailingQuotes(strWithLeadingAndTrailingQuotes);
 assertEquals(expected, result);

 String strWithLeadingQuotesOnly = "\"one two\"";
 String strWithTrailingQuotesOnly = "\"one two\"";
 String strWithoutQuotes = "one two";
 String resultWithLeadingQuotesOnly = Util.stripLeadingAndTrailingQuotes(strWithLeadingQuotesOnly);
 String resultWithTrailingQuotesOnly = Util.stripLeadingAndTrailingQuotes(strWithTrailingQuotesOnly);
 String resultWithoutQuotes = Util.stripLeadingAndTrailingQuotes(strWithoutQuotes);
 assertEquals(expected, resultWithLeadingQuotesOnly);
 assertEquals(expected, resultWithTrailingQuotesOnly);
 assertEquals(strWithoutQuotes, resultWithoutQuotes);
 }
}
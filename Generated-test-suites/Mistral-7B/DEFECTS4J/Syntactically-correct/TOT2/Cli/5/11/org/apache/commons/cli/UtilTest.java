import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        String input = "--str";
        String expected = "str";
        String actual = Util.stripLeadingHyphens(input);
        assertEquals(expected, actual);
    }

    @Test
    public void testStripLeadingHyphensNoLeadingHyphens() {
        String input = "str";
        String expected = "str";
        String actual = Util.stripLeadingHyphens(input);
        assertEquals(expected, actual);
    }

    @Test
    public void testStripLeadingHyphensEmptyString() {
        String input = "";
        String expected = "";
        String actual = Util.stripLeadingHyphens(input);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStripLeadingHyphensNullString() {
        String input = null;
        Util.stripLeadingHyphens(input);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        String input = "'one two'";
        String expected = "one two";
        String actual = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expected, actual);
    }

    @Test
    public void testStripLeadingAndTrailingQuotesNoQuotes() {
        String input = "one two";
        String expected = "one two";
        String actual = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expected, actual);
    }

    @Test
    public void testStripLeadingAndTrailingQuotesEmptyString() {
        String input = "";
        String expected = "";
        String actual = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expected, actual);
    }

    @Test
    public void testStripLeadingAndTrailingQuotesLeadingQuotes() {
        String input = "'''one two'''";
        String expected = "one two";
        String actual = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expected, actual);
    }

    @Test
    public void testStripLeadingAndTrailingQuotesTrailingQuotes() {
        String input = "one two''";
        String expected = "one two";
        String actual = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expected, actual);
    }

    @Test
    public void testStripLeadingAndTrailingQuotesBothLeadingAndTrailingQuotes() {
        String input = "'''one two'''";
        String expected = "one two";
        String actual = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStripLeadingAndTrailingQuotesNullString() {
        String input = null;
        Util.stripLeadingAndTrailingQuotes(input);
    }

}
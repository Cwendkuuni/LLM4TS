import org.apache.commons.cli.Util;
import org.junit.Assert;
import org.junit.Test;

public class UtilTest
{
    @Test
    public void testStripLeadingHyphens()
    {
        Assert.assertEquals("", Util.stripLeadingHyphens(""));
        Assert.assertEquals("", Util.stripLeadingHyphens(null));
        Assert.assertEquals("two", Util.stripLeadingHyphens("--two"));
        Assert.assertEquals("one two", Util.stripLeadingHyphens("--one--two"));
        Assert.assertEquals("one two", Util.stripLeadingHyphens("--one two--"));
        Assert.assertEquals("one", Util.stripLeadingHyphens("--one"));
        Assert.assertEquals("one two", Util.stripLeadingHyphens("--one--two"));
        Assert.assertEquals("one two", Util.stripLeadingHyphens("--one two--"));
        Assert.assertEquals("one", Util.stripLeadingHyphens("--one"));
        Assert.assertEquals("abc", Util.stripLeadingHyphens("abc"));
        Assert.assertEquals("ABC", Util.stripLeadingHyphens("ABC"));
        Assert.assertEquals(" --one--two", Util.stripLeadingHyphens("--one--two"));
        Assert.assertEquals("--one--two", Util.stripLeadingHyphens("---one--two"));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes()
    {
        Assert.assertEquals("", Util.stripLeadingAndTrailingQuotes(""));
        Assert.assertEquals("one two", Util.stripLeadingAndTrailingQuotes("\"one two\""));
        Assert.assertEquals("one two", Util.stripLeadingAndTrailingQuotes("\"one two\""));
        Assert.assertEquals("one two three", Util.stripLeadingAndTrailingQuotes("\"one two three\""));
        Assert.assertEquals("one two three", Util.stripLeadingAndTrailingQuotes("\"one two three\""));
        Assert.assertEquals("--one--two", Util.stripLeadingAndTrailingQuotes("\"--one--two\""));
        Assert.assertEquals("--one--two", Util.stripLeadingAndTrailingQuotes("\"--one--two\""));
        Assert.assertEquals("--one--two", Util.stripLeadingAndTrailingQuotes("\"--one--two\""));
        Assert.assertEquals("--one--two", Util.stripLeadingAndTrailingQuotes("\"---one--two\""));
        Assert.assertEquals("--one--two", Util.stripLeadingAndTrailingQuotes("\"---one--two\""));
        Assert.assertEquals("\"--one--two\"", Util.stripLeadingAndTrailingQuotes("\"\"--one--two\""));
        Assert.assertEquals("\"--one--two\"", Util.stripLeadingAndTrailingQuotes("\"\"---one--two\""));
    }
}
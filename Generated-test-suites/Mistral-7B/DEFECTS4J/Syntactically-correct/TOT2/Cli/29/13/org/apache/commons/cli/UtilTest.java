import org.apache.commons.cli.Util;
import org.junit.Assert;
import org.junit.Test;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        String str = "--foo";
        String expected = "foo";
        String actual = Util.stripLeadingHyphens(str);
        Assert.assertEquals(expected, actual);

        str = "foo";
        expected = "foo";
        actual = Util.stripLeadingHyphens(str);
        Assert.assertEquals(expected, actual);

        str = "---foo";
        expected = "foo";
        actual = Util.stripLeadingHyphens(str);
        Assert.assertEquals(expected, actual);

        str = "";
        actual = Util.stripLeadingHyphens(str);
        Assert.assertNull(actual);
    }
}
package org.apache.commons.cli.test;

import org.apache.commons.cli.Util;
import org.junit.Assert;
import org.junit.Test;

public class UtilTest
{
    @Test
    public void testStripLeadingHyphens()
    {
        String str1 = "--test";
        String expected1 = "test";
        String result1 = Util.stripLeadingHyphens(str1);
        Assert.assertEquals(expected1, result1);

        String str2 = "test";
        String expected2 = "test";
        String result2 = Util.stripLeadingHyphens(str2);
        Assert.assertEquals(expected2, result2);

        String str3 = "--test--";
        String expected3 = "test";
        String result3 = Util.stripLeadingHyphens(str3);
        Assert.assertEquals(expected3, result3);

        String str4 = "---test---";
        String expected4 = "test";
        String result4 = Util.stripLeadingHyphens(str4);
        Assert.assertEquals(expected4, result4);

        String str5 = null;
        String expected5 = null;
        String result5 = Util.stripLeadingHyphens(str5);
        Assert.assertNull(result5);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes()
    {
        String str1 = "\"test\"";
        String expected1 = "test";
        String result1 = Util.stripLeadingAndTrailingQuotes(str1);
        Assert.assertEquals(expected1, result1);

        String str2 = "\"test1 test2\"";
        String expected2 = "test1 test2";
        String result2 = Util.stripLeadingAndTrailingQuotes(str2);
        Assert.assertEquals(expected2, result2);

        String str3 = "\"test1 \"test2\"";
        String expected3 = "test1 test2";
        String result3 = Util.stripLeadingAndTrailingQuotes(str3);
        Assert.assertEquals(expected3, result3);

        String str4 = "\"test1 \"test2\" test3\"";
        String expected4 = "test1 test2 test3";
        String result4 = Util.stripLeadingAndTrailingQuotes(str4);
        Assert.assertEquals(expected4, result4);

        String str5 = "";
        String expected5 = "";
        String result5 = Util.stripLeadingAndTrailingQuotes(str5);
        Assert.assertEquals(expected5, result5);

        String str6 = null;
        String expected6 = null;
        String result6 = Util.stripLeadingAndTrailingQuotes(str6);
        Assert.assertNull(result6);
    }
}
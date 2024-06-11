package org.jsoup.helper.test;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.helper.StringUtil;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class StringUtilTest {

    @Test
    public void testJoin() {
        List<String> strings = Arrays.asList("abc", "def", "ghi");
        String expected = "abcdefghi";
        String result = StringUtil.join(strings.iterator(), "");
        Assert.assertEquals(expected, result);

        strings = Collections.singletonList("hello");
        expected = "hello";
        result = StringUtil.join(strings.iterator(), "");
        Assert.assertEquals(expected, result);

        strings = Collections.emptyList();
        expected = "";
        result = StringUtil.join(strings.iterator(), "");
        Assert.assertEquals(expected, result);

        strings = Arrays.asList("", "ab", "cd", "ef");
        expected = "";
        result = StringUtil.join(strings.iterator(), "");
        Assert.assertEquals(expected, result);

        strings = Arrays.asList("ab", "", "cd", "ef");
        expected = "abcd";
        result = StringUtil.join(strings.iterator(), "");
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testJoinArray() {
        String[] strings = {"abc", "def", "ghi"};
        String expected = "abcdefghi";
        String result = StringUtil.join(strings, "");
        Assert.assertEquals(expected, result);

        strings = new String[0];
        expected = "";
        result = StringUtil.join(strings, "");
        Assert.assertEquals(expected, result);

        strings = new String[]{null};
        expected = "";
        result = StringUtil.join(strings, "");
        Assert.assertEquals(expected, result);

        strings = new String[]{"", "ab", "cd", "ef"};
        expected = "";
        result = StringUtil.join(strings, "");
        Assert.assertEquals(expected, result);

        strings = new String[]{"ab", "", "cd", "ef"};
        expected = "abcd";
        result = StringUtil.join(strings, "");
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testJoinCollectionNull() {
        String result = StringUtil.join(null, "");
        Assert.assertNull(result);
    }

    @Test
    public void testJoinCollectionEmpty() {
        String result = StringUtil.join(Collections.emptyList(), "");
        Assert.assertEquals("", result);
    }

    @Test
    public void testJoinCollectionWhitespace() {
        List<String> strings = Arrays.asList("abc", "def", "ghi");
        String expected = "abc def ghi";
        String result = StringUtil.join(strings, " ");
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testJoinCollectionEmptyString() {
        List<String> strings = Arrays.asList("abc", "", "def");
        String expected = "abc def";
        String result = StringUtil.join(strings, "");
        Assert.assertEquals(expected, result);

        strings = Arrays.asList("", "", "");
        expected = "";
        result = StringUtil.join(strings, "");
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testIsBlankNull() {
        boolean result = StringUtil.isBlank(null);
        Assert.assertTrue(result);
    }

    @Test
    public void testIsBlankEmpty() {
        boolean result = StringUtil.isBlank("");
        Assert.assertTrue(result);
    }

    @Test
    public void testIsBlankWhitespace() {
        boolean result = StringUtil.isBlank(" ");
        Assert.assertTrue(result);

        result = StringUtil.isBlank("\t");
        Assert.assertTrue(result);

        result = StringUtil.isBlank("\n");
        Assert.assertTrue(result);

        result = StringUtil.isBlank("\r");
        Assert.assertTrue(result);

        result = StringUtil.isBlank("\f");
        Assert.assertTrue(result);

        result = StringUtil.isBlank("\n\r");
        Assert.assertTrue(result);

        result = StringUtil.isBlank("\t\n");
        Assert.assertTrue(result);

        result = StringUtil.isBlank("\t\r");
        Assert.assertTrue(result);

        result = StringUtil.isBlank("\t\f");
        Assert.assertTrue(result);
    }

    @Test
    public void testIsBlankNonWhitespace() {
        boolean result = StringUtil.isBlank("ab");
        Assert.assertFalse(result);
    }

    @Test
    public void testIsNumericEmpty() {
        boolean result = StringUtil.isNumeric("");
        Assert.assertFalse(result);
    }

    @Test
    public void testIsNumericNumbers() {
        for (int i = 0; i < 10; i++) {
            String number = Integer.toString(i);
            boolean result = StringUtil.isNumeric(number);
            Assert.assertTrue(result);
        }
    }

    @Test
    public void testIsNumericAlphaNumeric() {
        String result = StringUtil.isNumeric("1a");
        Assert.assertFalse(result);
    }

    @Test
    public void testIsNumericWhitespace() {
        String result = StringUtil.isNumeric(" 123 ");
        Assert.assertTrue(result);
    }

    @Test
    public void testIsWhitespaceSpace() {
        boolean result = StringUtil.isWhitespace(' ');
        Assert.assertTrue(result);
    }

    @Test
    public void testIsWhitespaceTab() {
        boolean result = StringUtil.isWhitespace('\t');
        Assert.assertTrue(result);
    }

    @Test
    public void testIsWhitespaceNewline() {
        boolean result = StringUtil.isWhitespace('\n');
        Assert.assertTrue(result);
    }

    @Test
    public void testIsWhitespaceReturn() {
        boolean result = StringUtil.isWhitespace('\r');
        Assert.assertTrue(result);
    }

    @Test
    public void testIsWhitespaceFormFeed() {
        boolean result = StringUtil.isWhitespace('\f');
        Assert.assertTrue(result);
    }

    @Test
    public void testIsWhitespaceNonWhitespace() {
        boolean result = StringUtil.isWhitespace('a');
        Assert.assertFalse(result);
    }

    @Test
    public void testIsActuallyWhitespaceSpace() {
        boolean result = StringUtil.isActuallyWhitespace(' ');
        Assert.assertTrue(result);
    }

    @Test
    public void testIsActuallyWhitespaceTab() {
        boolean result = StringUtil.isActuallyWhitespace('\t');
        Assert.assertTrue(result);
    }

    @Test
    public void testIsActuallyWhitespaceNewline() {
        boolean result = StringUtil.isActuallyWhitespace('\n');
        Assert.assertTrue(result);
    }

    @Test
    public void testIsActuallyWhitespaceReturn() {
        boolean result = StringUtil.isActuallyWhitespace('\r');
        Assert.assertTrue(result);
    }

    @Test
    public void testIsActuallyWhitespaceFormFeed() {
        boolean result = StringUtil.isActuallyWhitespace('\f');
        Assert.assertTrue(result);
    }

    @Test
    public void testIsActuallyWhitespaceNbsp() {
        boolean result = StringUtil.isActuallyWhitespace((int) 160);
        Assert.assertTrue(result);
    }

    @Test
    public void testNormaliseWhitespaceEmpty() {
        String result = StringUtil.normaliseWhitespace("");
        Assert.assertEquals("", result);
    }

    @Test
    public void testNormaliseWhitespaceSingleWhiteSpace() {
        String result = StringUtil.normaliseWhitespace(" abc ");
        Assert.assertEquals(" abc ", result);
    }

    @Test
    public void testNormaliseWhitespaceMultipleWhiteSpace() {
        String result = StringUtil.normaliseWhitespace("   abc    def   ");
        Assert.assertEquals(" abc def ", result);
    }

    @Test
    public void testNormaliseWhitespaceMixedWhiteSpace() {
        String result = StringUtil.normaliseWhitespace("  abc \t def \r\n  ghi ");
        Assert.assertEquals("abc def ghi", result);
    }

    @Test
    public void testNormaliseWhitespaceLeadingTrailing() {
        String result = StringUtil.normaliseWhitespace("   abc   ");
        Assert.assertEquals("abc", result);
    }

    @Test
    public void testNormaliseWhitespaceTabLeading() {
        String result = StringUtil.normaliseWhitespace("\t abc");
        Assert.assertEquals("abc", result);
    }

    @Test
    public void testNormaliseWhitespaceTabTrailing() {
        String result = StringUtil.normaliseWhitespace(" abc \t");
        Assert.assertEquals("abc", result);
    }

    @Test
    public void testNormaliseWhitespaceMultipleTabs() {
        String result = StringUtil.normaliseWhitespace("\t\t abc \t\t");
        Assert.assertEquals("abc", result);
    }

    @Test
    public void testNormaliseWhitespaceLeadingAndTrailing() {
        String result = StringUtil.normaliseWhitespace("   abc   ");
        Assert.assertEquals("abc", result);
    }

    @Test
    public void testNormaliseWhitespaceMixedLeadingAndTrailing() {
        String result = StringUtil.normaliseWhitespace("  abc \t def \t   ghi ");
        Assert.assertEquals("abc def ghi", result);
    }

    @Test
    public void testIn() {
        String[] haystack = {"one", "two", "three"};
        String result = StringUtil.in(null, haystack);
        Assert.assertFalse(result);

        result = StringUtil.in("two", haystack);
        Assert.assertTrue(result);

        result = StringUtil.in("five", haystack);
        Assert.assertFalse(result);
    }

    @Test
    public void testInSorted() {
        String[] haystack = {"apple", "banana", "cherry"};
        String result = StringUtil.inSorted("cherry", haystack);
        Assert.assertTrue(result);

        result = StringUtil.inSorted("date", haystack);
        Assert.assertFalse(result);

        haystack = new String[0];
        result = StringUtil.inSorted("cherry", haystack);
        Assert.assertFalse(result);
    }

    @Test
    public void testJoinWithIteratorNullCollection() {
        String result = StringUtil.join(null, "");
        Assert.assertNull(result);
    }

    @Test
    public void testJoinWithIteratorEmptyCollection() {
        String result = StringUtil.join(Collections.emptyList().iterator(), "");
        Assert.assertEquals("", result);
    }

    @Test
    public void testJoinWithIteratorNullIterator() {
        String result = StringUtil.join(null, "");
        Assert.assertNull(result);
    }

    @Test
    public void testJoinWithIteratorEmptyIterator() {
        String result = StringUtil.join(Collections.emptyList().listIterator(), "");
        Assert.assertEquals("", result);
    }

    @Test
    public void testJoinWithIteratorSingleItem() {
        List<String> list = Arrays.asList("abc");
        String result = StringUtil.join(list.iterator(), "");
        Assert.assertEquals("abc", result);
    }

    @Test
    public void testJoinWithIteratorMultipleItems() {
        List<String> list = Arrays.asList("abc", "def");
        String result = StringUtil.join(list.listIterator(), "");
        Assert.assertEquals("abcdef", result);
    }

    @Test
    public void testJoinWithIteratorMultipleItemsReversed() {
        List<String> list = Arrays.asList("abc", "def");
        String result = StringUtil.join(list.listIterator(list.size()), "");
        Assert.assertEquals("defabc", result);
    }
}
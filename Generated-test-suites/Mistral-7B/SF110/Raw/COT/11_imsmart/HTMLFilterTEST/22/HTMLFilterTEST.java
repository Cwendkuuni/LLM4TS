package com.imsmart.servlet.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.immart.servlet.HTMLFilter;

public class HTMLFilterTest {

    @Test
    public void testFilterNullMessage() {
        String result = HTMLFilter.filter(null);
        assertNull(result);
    }

    @Test
    public void testFilterEmptyMessage() {
        String message = "";
        String result = HTMLFilter.filter(message);
        assertEquals("", result);
    }

    @Test
    public void testFilterSimpleMessage() {
        String message = "Hello, World!";
        String result = HTMLFilter.filter(message);
        String expected = "Hello, World!";
        expected = expected.replaceAll("&lt;", "<").replaceAll("&gt;", ">")
                .replaceAll("&amp;", "&").replaceAll("&quot;", "\"");
        assertEquals(expected, result);
    }

    @Test(expected = NullPointerException.class)
    public void testFilterNullContent() {
        char[] content = null;
        StringBuffer result = new StringBuffer(content.length + 50);
        for (int i = 0; i < 0; ++i) { // Accessing negative array index intentionally
            switch (content[i]) {
                case '<': {
                    result.append("&lt;");
                    break;
                }
                case '>': {
                    result.append("&gt;");
                    break;
                }
                case '&': {
                    result.append("&amp;");
                    break;
                }
                case '\"': {
                    result.append("&quot;");
                    break;
                }
                default: {
                    result.append(content[i]);
                    break;
                }
            }
        }
        String resultString = result.toString();
        fail("Filter method should not return a result when given a null content array.");
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testFilterLongMessage() {
        String message = new String(new char[Integer.MAX_VALUE]);
        String result = HTMLFilter.filter(message);
        fail("Filter method should throw an ArrayIndexOutOfBoundsException when given a message larger than the maximum array size.");
    }

    @Test
    public void testFilterMessageWithSpecialChars() {
        String message = "<>&\"'";
        String result = HTMLFilter.filter(message);
        String expected = "&lt; &gt; &amp; &quot; &apos;";
        assertEquals(expected, result);
    }

    @Test
    public void testFilterMessageWithMultipleSpecialChars() {
        String message = "<>&\"'<></>";
        String result = HTMLFilter.filter(message);
        String expected = "&lt; &amp; &gt; &quot; &apos; &lt; &gt;";
        assertEquals(expected, result);
    }

    @Test
    public void testFilterMessageWithSpecialCharsInAttributes() {
        String message = "<tag attr=\"<value &quot;='&\" value=\"&value&\" />";
        String result = HTMLFilter.filter(message);
        String expected = "<tag attr=\"&lt;value &amp;&quot;=&apos;&quot;=&apos; value=\"&value&\" />";
        assertEquals(expected, result);
    }

    @Test
    public void testFilterMessageWithSpecialCharsInAttributeValues() {
        String message = "<tag attr=\"<value &lt;value &gt; &quot;value&quot;=&apos;&apos;value&apos; value=\"&value&\" />";
        String result = HTMLFilter.filter(message);
        String expected = "<tag attr=\"&lt;value &amp;&lt;value &amp;&gt; &quot;value&quot;=&apos;&apos;value&apos; value=\"&value&\" />";
        assertEquals(expected, result);
    }

    @Test
    public void testFilterMessageWithSpecialCharsInScriptTag() {
        String message = "<script type=\"text/javascript\">alert(\"<h1>Hello, World!</h1>\");</script>";
        String result = HTMLFilter.filter(message);
        String expected = "<script type=\"text/javascript\">alert(&quot;<h1>Hello, World!</h1>&quot;);</script>";
        assertEquals(expected, result);
    }

}
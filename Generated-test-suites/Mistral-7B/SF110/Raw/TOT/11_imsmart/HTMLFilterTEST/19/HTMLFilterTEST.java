package com.imsmart.servlet;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class HTMLFilterTest {

    @Test
    public void testFilterEmptyMessage() {
        String actual = HTMLFilter.filter(null);
        assertNull(actual);
    }

    @Test
    public void testFilterNormalMessage() {
        String message = "This is a <strong>test</strong> message.";
        String actual = HTMLFilter.filter(message);
        String expected = "This is a &lt;strong&gt;test&lt;/strong&gt; message.";
        assertEquals(expected, actual);
    }

    @Test
    public void testFilterSpecialCharacters() {
        String message = "This message contains & <\" '> & special characters.";
        String actual = HTMLFilter.filter(message);
        String expected = "This message contains &amp; &lt; & &quot; &apos; &gt; &amp; special characters.";
        assertEquals(expected, actual);
    }

    @Test
    public void testFilterLongMessage() {
        String message = "This is a very long message that needs to be filtered. It should not cause any issues.";
        String actual = HTMLFilter.filter(message);
        String expected = new String(new char[message.length() + 50]);// assume this is the expected size of the result
        for (int i = 0; i < message.length(); i++) {
            switch (message.charAt(i)) {
                case '<':
                case '>':
                case '&':
                case '\"':
                    expected.replace(expected.length() - 5, 5, "&" + entity(message.charAt(i)) + ";");
                    break;
                default:
                    expected.setCharAt(expected.length() - 1, message.charAt(i));
                    break;
            }
        }
        assertEquals(expected, actual);
    }

    @Test
    public void testFilterWhitespaceMessage() {
        String message = "   ";
        String actual = HTMLFilter.filter(message);
        String expected = " ";
        assertEquals(expected, actual);
    }

    @Test
    public void testFilterSpecialCharactersOnlyMessage() {
        String message = "&&<>&\"";
        String actual = HTMLFilter.filter(message);
        String expected = "&amp;&amp; &lt; &gt; &amp; &amp; &quot;";
        assertEquals(expected, actual);
    }

    @Test
    public void testFilterLettersOnlyMessage() {
        String message = "helloWorld";
        String actual = HTMLFilter.filter(message);
        String expected = "helloWorld";
        assertEquals(expected, actual);
    }

    @Test
    public void testFilterScriptTag() {
        String message = "<script type=\"text/javascript\">alert('Hello World!');</script>";
        String actual = HTMLFilter.filter(message);
        String expected = "&lt;script type=&quot;text/javascript&quot;&gt;&amp;alert(&#x27;Hello World!&#x27;);&lt;/script&gt;";
        assertEquals(expected, actual);
    }

    private String entity(char c) {
        switch (c) {
            case '<':
                return "lt";
            case '>':
                return "gt";
            case '&':
                return "amp";
            case '\"':
                return "quot";
            default:
                return Character.toString(c);
        }
    }
}
package com.imsmart.servlet;
import org.junit.Test;
import static org.junit.Assert.*;

public class HTMLFilterTest {

    @Test
    public void testFilterEmptyString() {
        String input = "";
        String expectedOutput = "";
        String output = HTMLFilter.filter(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testFilterWhitespace() {
        String input = "   ";
        String expectedOutput = "   ";
        String output = HTMLFilter.filter(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testFilterSpecialCharacter() {
        String input = "HelloWorld!@#$%^&*()_+={}[]|\\:;\"'<>,.?/~`";
        String expectedOutput = "HelloWorld!&amp;&#036;&#038;&#036;&#032;&#038;&#036;&#032;&#036;&#032;&#038;&#036;&#032;&#038;&#036;&#032;&#038;&#036;&#032;&#038;&#036;&#032;&#038;&#036;&#032;&#038;&#036;&#032;&#038;&#036;&#032;&#038;&#036;&#032;&#038;&#036;&#032;";
        String output = HTMLFilter.filter(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testFilterFilteredCharacters() {
        String input = "&lt;&gt;&amp;&quot;";
        String expectedOutput = "&amp;&lt; &amp;&gt; &amp;&amp; &amp;&quot;";
        String output = HTMLFilter.filter(input);
        assertEquals(expectedOutput, output);
    }

    @Test(expected = NullPointerException.class)
    public void testFilterNullInput() {
        String input = null;
        String output = HTMLFilter.filter(input);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFilterNegativeLengthInput() {
        String input = new String(new char[0]);
        String output = HTMLFilter.filter(input);
    }

    @Test
    public void testFilter() {
        String input = "<html><head><title>Hello World</title></head><body>Hello World</body></html>";
        String expectedOutput = "&lt;html&gt;&lt;head&gt;&lt;title&gt;Hello World&lt;/title&gt;&lt;/head&gt;&lt;body&gt;Hello World&lt;/body&gt;&lt;/html&gt;";
        String output = HTMLFilter.filter(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testFilterWithInvalidCharacters() {
        String input = "<script>alert('Hello World')</script>";
        String expectedOutput = "&lt;script&gt;&amp;amp;lt;script&gt;&amp;amp;gt;alert(&amp;amp;#039;Hello World&amp;amp;#039;)&lt;/script&gt;";
        String output = HTMLFilter.filter(input);
        assertEquals(expectedOutput, output);
    }
}
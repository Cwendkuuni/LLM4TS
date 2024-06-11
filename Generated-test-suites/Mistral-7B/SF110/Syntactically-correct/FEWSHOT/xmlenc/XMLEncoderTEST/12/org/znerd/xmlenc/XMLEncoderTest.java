package org.znerd.xmlenc;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;

import java.io.StringWriter;
import java.io.Writer;

import org.junit.Test;

public class XMLEncoderTest {

    @Test
    public void testGetEncoder() {
        try {
            XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
            assertEquals("UTF-8", encoder.getEncoding());
        } catch (IllegalArgumentException | UnsupportedEncodingException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        try {
            XMLEncoder encoder = XMLEncoder.getEncoder("ISO-8859-1");
            assertEquals("ISO-8859-1", encoder.getEncoding());
        } catch (IllegalArgumentException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        try {
            XMLEncoder encoder = XMLEncoder.getEncoder(null);
            fail("Expected IllegalArgumentException but got null.");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            XMLEncoder encoder = XMLEncoder.getEncoder("");
            fail("Expected IllegalArgumentException but got empty string.");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testDeclaration() throws IOException {
        Writer out = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        encoder.declaration(out);
        String result = out.toString();
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", result);
    }

    @Test
    public void testText() throws IOException, InvalidXMLException {
        Writer out = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");

        encoder.text(out, "Hello, World!");
        String result = out.toString();
        assertEquals("<Hello, World!/>", result);

        encoder.text(out, "Hello, & World!");
        result = out.toString();
        assertEquals("<Hello, &amp; World!/>", result);

        encoder.text(out, "Hello, < World!");
        result = out.toString();
        assertEquals("<Hello, <! [CDATA[ World! ]]>>", result);

        encoder.text(out, "Hello, > World!");
        result = out.toString();
        assertEquals("<Hello, >World!</Hello>", result);

        encoder.text(out, "Hello, ® World!");
        result = out.toString();
        assertEquals("< Hello, &#174; World!>", result);

        encoder.text(out, "Hello, \n World!");
        result = out.toString();
        assertEquals("< Hello, World!\n>", result);

        encoder.text(out, "Hello, \r World!");
        result = out.toString();
        assertEquals("< Hello, World!\r>", result);

        encoder.text(out, "Hello, =\r\n World!");
        result = out.toString();
        assertEquals("< Hello, =&#13;#10; World!>", result);

        encoder.text(out, "Hello, <script> alert('XSS'); </script> World!");
        result = out.toString();
        assertEquals("< Hello, <![CDATA[ <script> alert('XSS'); </script> ]]> World!>", result);

        encoder.text(out, "");
        result = out.toString();
        assertEquals("", result);
    }

    @Test
    public void testTextWithEscapeAmpersands() throws IOException, InvalidXMLException {
        Writer out = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");

        encoder.text(out, "Hello, & World!", false);
        String result = out.toString();
        assertEquals("<Hello, & World!/>", result);

        encoder.text(out, "Hello, &amp; World!", true);
        result = out.toString();
        assertEquals("<Hello, &amp; World!/>", result);

        encoder.text(out, "Hello, &lt; World!", false);
        result = out.toString();
        assertEquals("<Hello, < World!>", result);

        encoder.text(out, "Hello, &lt; World!", true);
        result = out.toString();
        assertEquals("<Hello, < World!>", result);

        encoder.text(out, "Hello, &gt; World!", false);
        result = out.toString();
        assertEquals("<Hello, >World!/>", result);

        encoder.text(out, "Hello, &gt; World!", true);
        result = out.toString();
        assertEquals("<Hello, >World!/>", result);

        encoder.text(out, "Hello, &apos; World!", false);
        result = out.toString();
        assertEquals("<Hello, ' World!>", result);

        encoder.text(out, "Hello, &apos; World!", true);
        result = out.toString();
        assertEquals("<Hello, ' World!>", result);

        encoder.text(out, "Hello, &quot; World!", false);
        result = out.toString();
        assertEquals("<Hello, \" World!>", result);

        encoder.text(out, "Hello, &quot; World!", true);
        result = out.toString();
        assertEquals("<Hello, \" World!>", result);
    }

    @Test
    public void testTextWithEscapeQuotes() throws IOException, InvalidXMLException {
        Writer out = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");

        encoder.text(out, "Hello, \" World!", false);
        String result = out.toString();
        assertEquals("<Hello, \" World!>", result);

        encoder.text(out, "Hello, \" World!", true);
        result = out.toString();
        assertEquals("<Hello, &quot; World!>", result);

        encoder.text(out, "Hello, ' World!", false);
        result = out.toString();
        assertEquals("<Hello, ' World!>", result);

        encoder.text(out, "Hello, ' World!", true);
        result = out.toString();
        assertEquals("<Hello, &apos; World!>", result);
    }

    @Test
    public void testWhitespace() throws IOException, InvalidXMLException {
        Writer out = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");

        encoder.whitespace(out, "");
        String result = out.toString();
        assertEquals("", result);

        encoder.whitespace(out, " ");
        result = out.toString();
        assertEquals(" ", result);

        encoder.whitespace(out, "\t");
        result = out.toString();
        assertEquals("\t", result);

        encoder.whitespace(out, "\n");
        result = out.toString();
        assertEquals("\n", result);

        encoder.whitespace(out, "\r");
        result = out.toString();
        assertEquals("\r", result);
    }

    @Test
    public void testAttribute() throws IOException, InvalidXMLException {
        Writer out = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");

        encoder.attribute(out, "name", "value", '\'');
        String result = out.toString();
        assertEquals("<name value='value'/>", result);

        encoder.attribute(out, "name", "value", '"');
        result = out.toString();
        assertEquals("<name value='value'/>", result);

        encoder.attribute(out, "name", "value with & World!", '"');
        result = out.toString();
        assertEquals("<name value='value with &amp; World!'/>", result);

        encoder.attribute(out, "name", "value with < World!", '\'');
        result = out.toString();
        assertEquals("<name value='value with <![CDATA[ World!]]'/>", result);

        encoder.attribute(out, "name", "value with < World!", '"');
        result = out.toString();
        assertEquals("<name value='value with <World!>'>", result);

        encoder.attribute(out, "name", "value with < World!", '"', false);
        result = out.toString();
        assertEquals("<name value='value with < World!'>", result);

        encoder.attribute(out, "name", "value with < World!", '"', true);
        result = out.toString();
        assertEquals("<name value='value with <World!>'>", result);

        encoder.attribute(out, "name", "value with ' World!", '"');
        result = out.toString();
        assertEquals("<name value='value with ' World!'/>", result);

        encoder.attribute(out, "name", "value with ' World!", '\'');
        result = out.toString();
        assertEquals("<name value='value with 'World!'/>", result);
    }

    @Test
    public void testAttributeWithEscapeQuotes() throws IOException, InvalidXMLException {
        Writer out = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");

        encoder.attribute(out, "name", "value with \" World!", '"');
        String result = out.toString();
        assertEquals("<name value='value with \" World!'/>", result);

        encoder.attribute(out, "name", "value with ' World!", '"');
        result = out.toString();
        assertEquals("<name value='value with ' World!'/>", result);
    }

    @Test
    public void testAttributeWithEscapeAmpersands() throws IOException, InvalidXMLException {
        Writer out = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");

        encoder.attribute(out, "name", "value with & World!", '"');
        String result = out.toString();
        assertEquals("<name value='value with &amp; World!'/>", result);
    }
}
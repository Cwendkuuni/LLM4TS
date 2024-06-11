
package org.znerd.xmlenc;

import org.junit.Test;
import org.junit.Assert;
import java.io.*;
import java.util.Locale;

public class XMLEncoderTest {
    @Test
    public void testGetEncoder() {
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        Assert.assertEquals("UTF-8", encoder.getEncoding());

        XMLEncoder encoderUTF16 = XMLEncoder.getEncoder("UTF-16");
        Assert.assertEquals("UTF-16", encoderUTF16.getEncoding());

        XMLEncoder encoderUSASCII = XMLEncoder.getEncoder("US-ASCII");
        Assert.assertEquals("US-ASCII", encoderUSASCII.getEncoding());

        XMLEncoder encoderInvalid = XMLEncoder.getEncoder("ISO-8859-12"); // Or any other unsupported encoding
        try {
            XMLEncoder.getEncoder("ISO-8859-12");
            Assert.fail("Should have thrown UnsupportedEncodingException");
        } catch (UnsupportedEncodingException e) {
            // Expected
        }
    }

    @Test
    public void testText() throws IOException, InvalidXMLException {
        Writer writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");

        encoder.text(writer, "Hello, World!", true);
        Assert.assertEquals("Hello, World!", writer.toString());

        encoder.text(writer, "&", true);
        Assert.assertEquals("&amp;", writer.toString());

        encoder.text(writer, "Hello, <World!>", true);
        Assert.assertEquals("Hello, &lt;World!&gt;", writer.toString());

        encoder.text(writer, "Hello, World!<", true); // Edge case: text with unclosed tag in the middle
        Assert.assertEquals("Hello, World!&lt;", writer.toString());

        encoder.text(writer, "Hello, World!&", true); // Edge case: text with unescaped ampersand in the middle
        Assert.assertEquals("Hello, World!&amp;&", writer.toString());

        try {
            encoder.text(writer, "Hello, World!<script>alert('XSS')</script>", true); // Exception scenario: XSS injection attack
            Assert.fail("Should have thrown InvalidXMLException");
        } catch (InvalidXMLException e) {
            // Expected
        }
    }

    @Test
    public void testAttribute() throws IOException, InvalidXMLException {
        Writer writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");

        encoder.attribute(writer, "name", "value", '\"');
        Assert.assertEquals("<name value=\"value\"/>", writer.toString());

        encoder.attribute(writer, "name", "value\"", '\"'); // Edge case: attribute value with unescaped double quote in the middle
        Assert.assertEquals("<name value=\"value\"\" />", writer.toString());

        try {
            encoder.attribute(writer, "name", "value<script>alert('XSS')</script>", '\"'); // Exception scenario: XSS injection attack
            Assert.fail("Should have thrown InvalidXMLException");
        } catch (InvalidXMLException e) {
            // Expected
        }
    }
}
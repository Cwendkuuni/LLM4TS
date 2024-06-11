package org.znerd.xmlenc.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

import org.junit.Test;
import org.znerd.xmlenc.XMLEncoder;

public class XMLEncoderTest {

    @Test
    public void testGetEncoder() {
        final String encoding = "UTF-8";
        final XMLEncoder encoder = XMLEncoder.getEncoder(encoding);
        assertEquals(encoding, encoder.getEncoding());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEncoderWithNullEncoding() {
        XMLEncoder.getEncoder(null);
    }

    @Test(expected = UnsupportedEncodingException.class)
    public void testGetEncoderWithUnsupportedEncoding() {
        final String encoding = "xyz";
        XMLEncoder.getEncoder(encoding);
    }

    @Test
    public void testTextWithAscii() throws IOException {
        final StringWriter writer = new StringWriter();
        final XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        encoder.declaration(writer);
        encoder.text(writer, "Hello, World!", false);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\nHello, World!", writer.toString());
    }

    @Test(expected = InvalidXMLException.class)
    public void testTextWithInvalidCharacter1() throws IOException {
        final StringWriter writer = new StringWriter();
        final XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        encoder.declaration(writer);
        encoder.text(writer, "Hello, ¥World!", false);
    }

    @Test(expected = InvalidXMLException.class)
    public void testTextWithInvalidCharacter2() throws IOException {
        final StringWriter writer = new StringWriter();
        final XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        encoder.declaration(writer);
        encoder.text(writer, "Hello, <World!", false);
    }

    @Test(expected = InvalidXMLException.class)
    public void testTextWithInvalidCharacter3() throws IOException {
        final StringWriter writer = new StringWriter();
        final XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        encoder.declaration(writer);
        encoder.text(writer, "Hello, >", false);
    }

    @Test(expected = InvalidXMLException.class)
    public void testTextWithInvalidCharacter4() throws IOException {
        final StringWriter writer = new StringWriter();
        final XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        encoder.declaration(writer);
        encoder.text(writer, "Hello, &World!", false);
    }

    @Test
    public void testTextWithEscapedCharacter1() throws IOException {
        final StringWriter writer = new StringWriter();
        final XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        encoder.declaration(writer);
        encoder.text(writer, "Hello, &#163;World!", false);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\nHello, &#163;World!", writer.toString());
    }

    @Test
    public void testTextWithEscapedCharacter2() throws IOException {
        final StringWriter writer = new StringWriter();
        final XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        encoder.declaration(writer);
        encoder.text(writer, "Hello, &lt;World!", false);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\nHello, &lt;World!", writer.toString());
    }

    @Test
    public void testTextWithEscapedCharacter3() throws IOException {
        final StringWriter writer = new StringWriter();
        final XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        encoder.declaration(writer);
        encoder.text(writer, "Hello, &gt;World!", false);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\nHello, &gt;World!", writer.toString());
    }

    @Test
    public void testTextWithEscapedCharacter4() throws IOException {
        final StringWriter writer = new StringWriter();
        final XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        encoder.declaration(writer);
        encoder.text(writer, "Hello, &amp;World!", false);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\nHello, &amp;World!", writer.toString());
    }

    @Test
    public void testTextWithEscapedCharacter5() throws IOException {
        final StringWriter writer = new StringWriter();
        final XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        encoder.declaration(writer);
        encoder.text(writer, "Hello, &#x20AC;World!", false);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\nHello, &#x20AC;World!", writer.toString());
    }

    @Test
    public void testTextWithEscapedCharacter6() throws IOException {
        final StringWriter writer = new StringWriter();
        final XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        encoder.declaration(writer);
        encoder.text(writer, "Hello, &#xA3;World!", false);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\nHello, &#xA3;World!", writer.toString());
    }

    @Test
    public void testTextWithEscapedCharacter7() throws IOException {
        final StringWriter writer = new StringWriter();
        final XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        encoder.declaration(writer);
        encoder.text(writer, "Hello, &apos;World!", false);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\nHello, &apos;World!", writer.toString());
    }

    @Test
    public void testTextWithEscapedCharacter8() throws IOException {
        final StringWriter writer = new StringWriter();
        final XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        encoder.declaration(writer);
        encoder.text(writer, "Hello, \"World!", false);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\nHello, \"World!", writer.toString());
    }

    @Test
    public void testTextWithEscapedCharacter9() throws IOException {
        final StringWriter writer = new StringWriter();
        final XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        encoder.declaration(writer);
        encoder.text(writer, "Hello, 'World!", false);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\nHello, 'World!", writer.toString());
    }

    @Test
    public void testTextWithEscapedCharacter10() throws IOException {
        final StringWriter writer = new StringWriter();
        final XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        encoder.declaration(writer);
        encoder.text(writer, "Hello, <World!", false);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\nHello, &lt;World!", writer.toString());
    }

    @Test
    public void testTextWithEscapedCharacter11() throws IOException {
        final StringWriter writer = new StringWriter();
        final XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        encoder.declaration(writer);
        encoder.text(writer, "Hello, >World!", false);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\nHello, &gt;World!", writer.toString());
    }

    @Test
    public void testTextWithEscapedCharacter12() throws IOException {
        final StringWriter writer = new StringWriter();
        final XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        encoder.declaration(writer);
        encoder.text(writer, "Hello, &World!", false);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\nHello, &amp;World!", writer.toString());
    }

    @Test
    public void testTextWithQuotedAttributeValue1() throws IOException {
        final StringWriter writer = new StringWriter();
        final XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        encoder.declaration(writer);
        encoder.attribute(writer, "name", "value=\"Hello, World!\"", '\"', false);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<name value=\"Hello, World!\" />", writer.toString());
    }

    @Test(expected = NullPointerException.class)
    public void testTextWithNullWriter() throws IOException {
        final XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        encoder.text(null, "Hello, World!", false);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testTextWithNegativeOffset() throws IOException {
        final StringWriter writer = new StringWriter();
        final char[] test = "Hello, World!".toCharArray();
        final XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        encoder.text(writer, test, -1, test.length);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testTextWithNegativeLength() throws IOException {
        final StringWriter writer = new StringWriter();
        final char[] test = "Hello, World!".toCharArray();
        final XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        encoder.text(writer, test, 0, -1);
    }

    @Test(expected = NullPointerException.class)
    public void testTextWithNullCharacterArray() throws IOException {
        final StringWriter writer = new StringWriter();
        final XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        encoder.text(writer, null, 0, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTextWithNegativeCharacterIndex() throws IOException {
        final StringWriter writer = new StringWriter();
        final char[] test = "Hello, World!".toCharArray();
        final XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        encoder.text(writer, test, 0, test.length + 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testTextWithCharacterIndexOutOfBounds() throws IOException {
        final StringWriter writer = new StringWriter();
        final char[] test = "Hello, World!".toCharArray();
        final XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        encoder.text(writer, test, 5, 3);
    }

    @Test(expected = InvalidXMLException.class)
    public void testTextWithInvalidCharacter13() throws IOException {
        final StringWriter writer = new StringWriter();
        final XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        encoder.declaration(writer);
        encoder.text(writer, "Hello, ¥World!", false);
    }

    @Test(expected = InvalidXMLException.class)
    public void testTextWithInvalidCharacter14() throws IOException {
        final StringWriter writer = new StringWriter();
        final XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        encoder.declaration(writer);
        encoder.text(writer, "Hello, <World!", false);
    }

    @Test(expected = InvalidXMLException.class)
    public void testTextWithInvalidCharacter15() throws IOException {
        final StringWriter writer = new StringWriter();
        final XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        encoder.declaration(writer);
        encoder.text(writer, "Hello, >World!", false);
    }

    @Test(expected = InvalidXMLException.class)
    public void testTextWithInvalidCharacter16() throws IOException {
        final StringWriter writer = new StringWriter();
        final XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        encoder.declaration(writer);
        encoder.text(writer, "Hello, &World!", false);
    }

    @Test(expected = InvalidXMLException.class)
    public void testTextWithInvalidCharacter17() throws IOException {
        final StringWriter writer = new StringWriter();
        final XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        encoder.declaration(writer);
        encoder.text(writer, "Hello, &#x0000;World!", false);
    }

    @Test(expected = InvalidXMLException.class)
    public void testTextWithInvalidCharacter18() throws IOException {
        final StringWriter writer = new StringWriter();
        final XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        encoder.declaration(writer);
        encoder.text(writer, "Hello, &#xFFFF;World!", false);
    }

    @Test(expected = InvalidXMLException.class)
    public void testTextWithInvalidCharacter19() throws IOException {
        final StringWriter writer = new StringWriter();
        final XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        encoder.declaration(writer);
        encoder.text(writer, "Hello, &apos;&apos;World!", false);
    }

    @Test(expected = InvalidXMLException.class)
    public void testTextWithInvalidCharacter20() throws IOException {
        final StringWriter writer = new StringWriter();
        final XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        encoder.declaration(writer);
        encoder.text(writer, "Hello, \"\"World!", false);
    }

    @Test(expected = InvalidXMLException.class)
    public void testTextWithInvalidCharacter21() throws IOException {
        final StringWriter writer = new StringWriter();
        final XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        encoder.declaration(writer);
        encoder.text(writer, "Hello, ''\'World!", false);
    }
}
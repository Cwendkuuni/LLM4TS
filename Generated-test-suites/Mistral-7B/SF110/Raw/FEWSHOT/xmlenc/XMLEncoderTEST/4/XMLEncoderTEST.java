package org.znerd.xmlenc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.junit.Test;

public class XMLEncoderTest {

    @Test
    public void testGetEncoder() throws IllegalArgumentException, UnsupportedEncodingException {
        String encoding = "UTF-8";
        XMLEncoder encoder = XMLEncoder.getEncoder(encoding);
        assertEquals(encoding, encoder.getEncoding());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEncoderNullEncoding() {
        XMLEncoder.getEncoder(null);
    }

    @Test(expected = UnsupportedEncodingException.class)
    public void testGetEncoderUnsupportedEncoding() throws IllegalArgumentException {
        String encoding = "XYZ";
        XMLEncoder.getEncoder(encoding);
    }

    @Test
    public void testTextSingleCharacter() throws IOException, InvalidXMLException {
        String encoding = "UTF-8";
        XMLEncoder encoder = XMLEncoder.getEncoder(encoding);
        Writer out = new OutputStreamWriter(new ByteArrayOutputStream());
        encoder.text(out, 'A');
        assertEquals("&amp;A;", out.getBuffer().toString());
    }

    @Test
    public void testTextMultipleCharacters() throws IOException, InvalidXMLException {
        String encoding = "UTF-8";
        XMLEncoder encoder = XMLEncoder.getEncoder(encoding);
        Writer out = new OutputStreamWriter(new ByteArrayOutputStream());
        encoder.text(out, "ABC");
        assertEquals("&amp;A;&amp;B;&amp;C;", out.getBuffer().toString());
    }

    @Test
    public void testTextWithEscapeAmpersands() throws IOException, InvalidXMLException {
        String encoding = "UTF-8";
        XMLEncoder encoder = XMLEncoder.getEncoder(encoding);
        Writer out = new OutputStreamWriter(new ByteArrayOutputStream());
        encoder.text(out, "ABC&DEF", true);
        assertEquals("&amp;A;&amp;B;&amp;C;&amp;amp;D;&amp;F;", out.getBuffer().toString());
    }

    @Test
    public void testTextWithSevenBitEncoding() throws IOException, InvalidXMLException {
        String encoding = "US-ASCII";
        XMLEncoder encoder = XMLEncoder.getEncoder(encoding);
        Writer out = new OutputStreamWriter(new ByteArrayOutputStream());
        encoder.text(out, "ABC&DEF");
        assertEquals("ABC&amp;DEF;", out.getBuffer().toString());
    }

    @Test
    public void testTextWithNonASCIICharacter() throws IOException, InvalidXMLException {
        String encoding = "UTF-8";
        XMLEncoder encoder = XMLEncoder.getEncoder(encoding);
        Writer out = new OutputStreamWriter(new ByteArrayOutputStream());
        encoder.text(out, "ABC éDEF");
        assertEquals("&amp;A;&amp;B;&amp;C; &Atilde;D;&amp;F;", out.getBuffer().toString());
    }

    @Test
    public void testTextWithInvalidCharacter() throws IOException, InvalidXMLException {
        String encoding = "UTF-8";
        XMLEncoder encoder = XMLEncoder.getEncoder(encoding);
        Writer out = new OutputStreamWriter(new ByteArrayOutputStream());
        assertThrows(InvalidXMLException.class, () -> encoder.text(out, '\u0001'));
    }

    @Test
    public void testTextWithWhitespace() throws IOException, InvalidXMLException {
        String encoding = "UTF-8";
        XMLEncoder encoder = XMLEncoder.getEncoder(encoding);
        Writer out = new OutputStreamWriter(new ByteArrayOutputStream());
        encoder.text(out, "ABC\tDEF\nGHI");
        assertEquals("ABC &Tab;DEF &nbspp;GHI;", out.getBuffer().toString());
    }

    @Test
    public void testDeclaration() throws IOException, NullPointerException {
        String encoding = "UTF-8";
        XMLEncoder encoder = XMLEncoder.getEncoder(encoding);
        Writer out = new OutputStreamWriter(new ByteArrayOutputStream());
        encoder.declaration(out);
        assertEquals("<?xml version=\"1.0\" encoding=\"" + encoding + "\";?>", out.getBuffer().toString());
    }

    @Test
    public void testAttribute() throws IOException, NullPointerException, InvalidXMLException {
        String encoding = "UTF-8";
        XMLEncoder encoder = XMLEncoder.getEncoder(encoding);
        Writer out = new OutputStreamWriter(new ByteArrayOutputStream());
        encoder.attribute(out, "name", "value", '\"', true);
        assertEquals("<name value=\"&amp;quot;value&amp;quot;\"/>", out.getBuffer().toString());
    }

    @Test
    public void testAttributeWithQuotesAsCharacter() throws IOException, NullPointerException, InvalidXMLException {
        String encoding = "UTF-8";
        XMLEncoder encoder = XMLEncoder.getEncoder(encoding);
        Writer out = new OutputStreamWriter(new ByteArrayOutputStream());
        encoder.attribute(out, "name", "value'", '\'', true);
        assertEquals("<name value='&apos;value&apos;'/>", out.getBuffer().toString());
    }

    @Test
    public void testAttributeWithEscapeAmpersands() throws IOException, NullPointerException, InvalidXMLException {
        String encoding = "UTF-8";
        XMLEncoder encoder = XMLEncoder.getEncoder(encoding);
        Writer out = new OutputStreamWriter(new ByteArrayOutputStream());
        encoder.attribute(out, "name", "value&amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;value", '\"', true);
        assertEquals("<name value=\"&amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;quot;value&quot;\"/>", out.getBuffer().toString());
    }

    @Test
    public void testAttributeWithNonASCIICharacter() throws IOException, NullPointerException, InvalidXMLException {
        String encoding = "UTF-8";
        XMLEncoder encoder = XMLEncoder.getEncoder(encoding);
        Writer out = new OutputStreamWriter(new ByteArrayOutputStream());
        encoder.attribute(out, "name", "value \u00E9DEF", '\"', true);
        assertEquals("<name value=\"&amp;#233;DEF\"/>", out.getBuffer().toString());
    }

    @Test
    public void testAttributeWithInvalidCharacter() throws IOException, NullPointerException, InvalidXMLException {
        String encoding = "UTF-8";
        XMLEncoder encoder = XMLEncoder.getEncoder(encoding);
        Writer out = new OutputStreamWriter(new ByteArrayOutputStream());
        assertThrows(InvalidXMLException.class, () -> encoder.attribute(out, "name", "value\u0001", '\"', true));
    }
}
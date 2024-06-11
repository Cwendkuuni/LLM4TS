import org.junit.Test;
import org.junit.Assert.*;
import org.znerd.xmlenc.XMLEncoder;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class XMLEncoderTest {

    @Test
    public void testGetEncoder() {
        // Test valid encoding
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        Assert.assertNotNull(encoder);

        // Test null encoding
        try {
            XMLEncoder.getEncoder(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test unsupported encoding
        try {
            XMLEncoder.getEncoder("ISA-8859-12");
            fail("Expected UnsupportedEncodingException");
        } catch (UnsupportedEncodingException e) {
            // Expected
        }
    }

    // Test valid character
    @Test
    public void testText_char_false() {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        encoder.text(writer, 'A');
        String output = writer.toString();
        assertEquals("A", output);

        // Test invalid character
        try {
            XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
            StringWriter writer = new StringWriter();
            encoder.text(writer, 0x0000);
            fail("Expected InvalidXMLException");
        } catch (InvalidXMLException e) {
            // Expected
        }

        // Test null Writer
        try {
            XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
            encoder.text(null, 'A');
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }

        // Test invalid encoding
        try {
            XMLEncoder encoder = XMLEncoder.getEncoder(null);
            StringWriter writer = new StringWriter();
            encoder.text(writer, 'A');
            fail("Expected NullPointerException or UnsupportedEncodingException");
        } catch (NullPointerException | UnsupportedEncodingException e) {
            // Expected
        }
    }

    // Test valid character array with escapeAmpersands=false
    @Test
    public void testText_charArray_boolean() {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        char[] ch = {'A', 'B', 'C'};
        encoder.text(writer, ch, 0, 3, false);
        String output = writer.toString();
        assertEquals("ABC", output);

        // Test invalid character array with escapeAmpersands=false
        try {
            StringWriter writer = new StringWriter();
            XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
            char[] ch = {'\u0000'};
            encoder.text(writer, ch, 0, 1, false);
            fail("Expected InvalidXMLException");
        } catch (InvalidXMLException e) {
            // Expected
        }

        // Test null Writer
        try {
            XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
            char[] ch = {'A', 'B', 'C'};
            encoder.text(null, ch, 0, 3, false);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }

        // Test null character array
        try {
            XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
            encoder.text(new StringWriter(), null, 0, 3, false);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }

        // Test invalid encoding
        try {
            XMLEncoder encoder = XMLEncoder.getEncoder(null);
            StringWriter writer = new StringWriter();
            char[] ch = {'A', 'B', 'C'};
            encoder.text(writer, ch, 0, 3, false);
            fail("Expected NullPointerException or UnsupportedEncodingException");
        } catch (NullPointerException | UnsupportedEncodingException e) {
            // Expected
        }

        // Test out-of-bounds index
        try {
            StringWriter writer = new StringWriter();
            XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
            char[] ch = {'A', 'B', 'C'};
            encoder.text(writer, ch, 4, 3, false);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }
    }

    // Test valid character array with escapeAmpersands=true
    @Test
    public void testText_charArray_boolean_true() {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        char[] ch = {'A', 'B', 'C'};
        encoder.text(writer, ch, 0, 3, true);
        String output = writer.toString();
        assertEquals("A&Amp;B&Amp;C", output);

        // Test invalid character array with escapeAmpersands=true
        try {
            StringWriter writer = new StringWriter();
            XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
            char[] ch = {'\u0000'};
            encoder.text(writer, ch, 0, 1, true);
            fail("Expected InvalidXMLException");
        } catch (InvalidXMLException e) {
            // Expected
        }

        // Test null Writer
        try {
            XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
            char[] ch = {'A', 'B', 'C'};
            encoder.text(null, ch, 0, 3, true);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }

        // Test null character array
        try {
            XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
            encoder.text(new StringWriter(), null, 0, 3, true);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }

        // Test invalid encoding
        try {
            XMLEncoder encoder = XMLEncoder.getEncoder(null);
            StringWriter writer = new StringWriter();
            char[] ch = {'A', 'B', 'C'};
            encoder.text(writer, ch, 0, 3, true);
            fail("Expected NullPointerException or UnsupportedEncodingException");
        } catch (NullPointerException | UnsupportedEncodingException e) {
            // Expected
        }

        // Test out-of-bounds index
        try {
            StringWriter writer = new StringWriter();
            XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
            char[] ch = {'A', 'B', 'C'};
            encoder.text(writer, ch, 4, 3, true);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }
    }

    // Test whitespace method
    @Test
    public void testWhitespace() {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        String whitespace = "   Hello World   ";
        encoder.whitespace(writer, whitespace);
        String output = writer.toString();
        assertEquals("Hello World", output);
    }

    // Test attribute method
    @Test
    public void testAttribute() {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        String name = "name";
        String value = "value";
        char quotationMark = '"';
        encoder.attribute(writer, name, value, quotationMark, false);
        String output = writer.toString();
        assertEquals("<name>value</name>", output);
    }
}
package org.znerd.xmlenc;

import org.junit.*;
import static org.junit.Assert.*;
import java.io.*;

public class XMLEncoderTest {

    @BeforeClass
    public static void setUpClass() {
        // Set up class level variables or resources if needed
    }

    @AfterClass
    public static void tearDownClass() {
        // Clean up class level variables or resources if needed
    }

    @Before
    public void setUp() {
        // Set up instance variables or resources for each test
    }

    @After
    public void tearDown() {
        // Clean up instance variables or resources after each test
    }

    @Test
    public void testGetEncoder() {
        String encoding = "UTF-8";
        XMLEncoder encoder = XMLEncoder.getEncoder(encoding);
        assertEquals(encoding, encoder.getEncoding());
    }

    @Test
    public void testDeclaration() throws IOException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        encoder.declaration(writer);
        String xmlDeclaration = "<xml version=\"1.0\" encoding=\"UTF-8\"?>";
        assertEquals(xmlDeclaration, writer.toString());
    }

    @Test
    public void testText() throws IOException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        String text = "Hello, World!";
        encoder.text(writer, text, true);
        String expected = "<Hello,%20World%21>";
        assertEquals(expected, writer.toString());
    }

    @Test
    public void testTextWithSevenBitEncoding() throws IOException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("US-ASCII");
        String text = "Hello, World!";
        encoder.text(writer, text, true);
        String expected = "<Hello, World!>";
        assertEquals(expected, writer.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEncoderWithNullEncoding() {
        String encoding = null;
        try {
            XMLEncoder encoder = XMLEncoder.getEncoder(encoding);
            fail("IllegalArgumentException expected but not thrown");
        } catch (IllegalArgumentException e) {
            // Pass
        }
    }

    @Test(expected = UnsupportedEncodingException.class)
    public void testGetEncoderWithUnsupportedEncoding() {
        String encoding = "MyCustomEncoding";
        try {
            XMLEncoder encoder = XMLEncoder.getEncoder(encoding);
            fail("UnsupportedEncodingException expected but not thrown");
        } catch (UnsupportedEncodingException e) {
            // Pass
        }
    }

    @Test
    public void testWhitespace() throws IOException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        String whitespace = " Hello World ";
        encoder.whitespace(writer, whitespace);
        String expected = "<Hello%20World%20>";
        assertEquals(expected, writer.toString());
    }

    @Test
    public void testAttribute() throws IOException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        String name = "name";
        String value = "value";
        encoder.attribute(writer, name, value, '\"', true);
        String expected = "<name=\"value\" />";
        assertEquals(expected, writer.toString());
    }

    @Test
    public void testAttributeWithSingleQuote() throws IOException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        String name = "name";
        String value = "value'";
        encoder.attribute(writer, name, value, '\'', true);
        String expected = "<name=\"value'\"/>";
        assertEquals(expected, writer.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAttributeWithInvalidQuotationMark() throws IOException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        String name = "name";
        String value = "value±";
        encoder.attribute(writer, name, value, '±', true);
        fail("IllegalArgumentException expected but not thrown");
    }

    @Test
    public void testAttributeWithoutEscapeAmpersands() throws IOException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        String name = "name";
        String value = "value&";
        encoder.attribute(writer, name, value, '\"', false);
        String expected = "<name=\"value&\" />";
        assertEquals(expected, writer.toString());
    }

    @Test
    public void testAttributeWithEscapeAmpersands() throws IOException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        String name = "name";
        String value = "value&amp;";
        encoder.attribute(writer, name, value, '\"', true);
        String expected = "<name=\"value&amp;\"/>";
        assertEquals(expected, writer.toString());
    }

    @Test
    public void testAttributeWithLongText() throws IOException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        String name = "name";
        String value = "value";
        for (int i = 0; i < 10000; ++i) {
            value += "X";
        }
        encoder.attribute(writer, name, value, '\"', true);
        String expected = "<name=\"" + value.substring(0, 256) + "%22%20xml:space=\"preserve\"%3E%20%23x...";
        assertEquals(expected, writer.toString().substring(0, 1024));
    }

    // Additional test cases for other methods can be added here

    @AfterClass
    public static void tearDownClass() {
        // Clean up class level variables or resources if needed
    }
}
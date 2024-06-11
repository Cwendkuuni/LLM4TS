
package org.jdom2.output;
import org.jdom2.*;
import org.junit.*;
import static org.junit.Assert.*;

public class XMLOutputterTest {

    @BeforeClass
    public static void setup() {
        // Setup any static variables or resources that are required for testing
    }

    @AfterClass
    public static void teardown() {
        // Clean up any static variables or resources that were set up for testing
    }

    @Before
    public void setUp() {
        // Set up any instance variables or resources that are required for each test
    }

    @After
    public void tearDown() {
        // Clean up any instance variables or resources that were set up for each test
    }

    @Test
    public void testOutputDocument() throws IOException {
        Document doc = new Document();
        Element root = new Element("root");
        doc.setRootElement(root);
        root.addContent(new Element("child1").setText("child1"));
        root.addContent(new Element("child2").setText("child2"));

        XMLOutputter xmlOutputter = new XMLOutputter();
        String expectedXml = "<root><child1>child1</child1><child2>child2</child2></root>";
        String actualXml = xmlOutputter.outputString(doc);

        assertEquals(expectedXml, actualXml);
    }

    @Test
    public void testOutputEmptyDocument() throws IOException {
        XMLOutputter xmlOutputter = new XMLOutputter();
        String expectedXml = "<root/>";
        String actualXml = xmlOutputter.outputString(new Document());

        assertEquals(expectedXml, actualXml);
    }

    @Test
    public void testOutputMalformedDocument() throws IOException {
        Document doc = new Document();
        Element root = new Element("root");
        doc.setRootElement(root);
        root.addContent(new Element("child"));

        XMLOutputter xmlOutputter = new XMLOutputter();
        String actualXml = xmlOutputter.outputString(doc);

        assertNotNull(actualXml); // The test should not fail with a null result, but it should not be a valid XML
        // Add assertions to check if the output contains expected errors
    }

    @Test
    public void testOutputDocumentWithSpecialChars() throws IOException {
        Document doc = new Document();
        Element root = new Element("root");
        doc.setRootElement(root);
        root.addContent(new Element("child").setText("child text with <special> characters"));

        XMLOutputter xmlOutputter = new XMLOutputter();
        String expectedXml = "<root><child>child text with &lt;special&gt; characters</child></root>";
        String actualXml = xmlOutputter.outputString(doc);

        assertEquals(expectedXml, actualXml);
    }

    @Test
    public void testOutputElementContent() throws IOException {
        Document doc = new Document();
        Element root = new Element("root");
        doc.setRootElement(root);
        root.addContent(new Element("child").setText("child text"));

        XMLOutputter xmlOutputter = new XMLOutputter();
        String expectedXml = "<child>child text</child>";
        String actualXml = xmlOutputter.outputElementContentString(root.getContent().get(0));

        assertEquals(expectedXml, actualXml);
    }

    @Test
    public void testOutputEmptyElementContent() throws IOException {
        XMLOutputter xmlOutputter = new XMLOutputter();
        String expectedXml = "<elementName/>";
        String actualXml = xmlOutputter.outputElementContentString(new Element("elementName"));

        assertEquals(expectedXml, actualXml);
    }

    @Test(expected = IOException.class)
    public void testOutputNullDocument() throws IOException {
        XMLOutputter xmlOutputter = new XMLOutputter();
        xmlOutputter.outputString(null);
    }

    @Test(expected = IOException.class)
    public void testOutputClosedOutputStream() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        XMLOutputter xmlOutputter = new XMLOutputter();
        xmlOutputter.output(new Document(), outputStream);
        outputStream.close();

        try {
            xmlOutputter.output(new Document(), outputStream);
        } finally {
            assertThrows(IOException.class, () -> xmlOutputter.output(new Document(), outputStream));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOutputInvalidFormat() throws IOException {
        Format invalidFormat = new Format();
        XMLOutputter xmlOutputter = new XMLOutputter();

        try {
            xmlOutputter.output(new Document(), null);
        } finally {
            assertThrows(IllegalArgumentException.class, () -> xmlOutputter.output(new Document(), invalidFormat));
        }
    }

    @AfterClass
    public static void cleanup() {
        // Clean up any static resources or variables that were set up for testing
    }
}

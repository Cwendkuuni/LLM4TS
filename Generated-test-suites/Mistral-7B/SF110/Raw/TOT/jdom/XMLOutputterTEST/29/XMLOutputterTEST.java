import org.jdom2.*;
import org.jdom2.output.XMLOutputter;
import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.StringWriter;
import static org.junit.Assert.*;

public class XMLOutputterTest {

    @Test
    public void testOutputDocument() {
        // Create an XML document
        Document doc = new Document();
        Element root = new Element("root");
        root.addContent(new Element("child1").setText("Child 1"));
        root.addContent(new Element("child2").setText("Child 2"));
        doc.setRootElement(root);

        // Create an outputter with default format
        XMLOutputter outputter = new XMLOutputter();

        // Test output to a StringWriter
        StringWriter writer = new StringWriter();
        try {
            outputter.output(doc, writer);
            String expectedXml = "<root><child1>Child 1</child1><child2>Child 2</child2></root>";
            assertEquals(expectedXml, writer.toString());
        } catch (IOException e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }

        // Test output to a ByteArrayOutputStream
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            outputter.output(doc, byteArrayOutputStream);
            String expectedXml = "<root><child1>Child 1</child1><child2>Child 2</child2></root>";
            assertArrayEquals(expectedXml.getBytes(), byteArrayOutputStream.toByteArray());
        } catch (IOException e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }
    }

    @Test
    public void testOutputText() {
        // Create an outputter with default format
        XMLOutputter outputter = new XMLOutputter();

        // Test output of a Text element
        Text text = new Text("Hello World");
        StringWriter writer = new StringWriter();
        try {
            outputter.output(text, writer);
            String expectedXml = "Hello World";
            assertEquals(expectedXml, writer.toString());
        } catch (IOException e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }
    }

    @Test
    public void testOutputComment() {
        // Create an outputter with default format
        XMLOutputter outputter = new XMLOutputter();

        // Test output of a Comment element
        Comment comment = new Comment("This is a comment");
        StringWriter writer = new StringWriter();
        try {
            outputter.output(comment, writer);
            String expectedXml = "<!--This is a comment-->";
            assertEquals(expectedXml, writer.toString());
        } catch (IOException e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }
    }

    @Test
    public void testOutputEntityRef() {
        // Create an outputter with default format
        XMLOutputter outputter = new XMLOutputter();

        // Test output of an EntityRef element
        EntityRef entityRef = new EntityRef("entity1");
        StringWriter writer = new StringWriter();
        try {
            outputter.output(entityRef, writer);
            String expectedXml = "&entity1;";
            assertEquals(expectedXml, writer.toString());
        } catch (IOException e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }
    }

    @Test
    public void testOutputElementContent() {
        // Create an XML document with an element containing child elements
        Document doc = new Document();
        Element root = new Element("root");
        root.addContent(new Element("child1").setText("Child 1"));
        root.addContent(new Element("child2").setText("Child 2"));
        doc.setRootElement(root);

        // Create an outputter with default format
        XMLOutputter outputter = new XMLOutputter();

        // Test output of element content to a StringWriter
        StringWriter writer = new StringWriter();
        try {
            outputter.outputElementContent(root, writer);
            String expectedXml = "<child1>Child 1</child1><child2>Child 2</child2>";
            assertEquals(expectedXml, writer.toString());
        } catch (IOException e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }
    }

    @Test
    public void testOutputInvalidFormat() {
        // Create an outputter with null format
        XMLOutputter outputter = new XMLOutputter(null);

        // Test output to a StringWriter, expecting an exception
        StringWriter writer = new StringWriter();
        try {
            outputter.output(new Document(), writer);
            fail("Exception should be thrown");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testOutputWriterNull() {
        // Create an outputter with default format
        XMLOutputter outputter = new XMLOutputter();

        // Test output to a null Writer, expecting an exception
        try {
            outputter.output(new Document(), null);
            fail("Exception should be thrown");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test
    public void testOutputDocumentWithProcessor() {
        // Create an XML document
        Document doc = new Document();
        Element root = new Element("root");
        root.addContent(new Element("child1").setText("Child 1"));
        root.addContent(new Element("child2").setText("Child 2"));
        doc.setRootElement(root);

        // Create a custom XMLOutputProcessor
        XMLOutputProcessor customProcessor = new XMLOutputProcessor() {
            @Override
            public void process(Writer out, Format format, Content content) throws IOException {
                out.write("CUSTOM: ");
                super.process(out, format, content);
            }
        };

        // Create an outputter with the custom processor
        XMLOutputter outputter = new XMLOutputter(null, customProcessor);

        // Test output to a StringWriter
        StringWriter writer = new StringWriter();
        try {
            outputter.output(doc, writer);
            String expectedXml = "<root><child1>Child 1</child1><child2>Child 2</child2></root>";
            String actualXml = writer.toString();
            assertTrue(actualXml.startsWith("CUSTOM: " + expectedXml));
        } catch (IOException e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }
    }
}
import org.jdom2.*;
import org.jdom2.output.SAXOutputter;
import org.junit.*;
import org.junit.rules.ExpectedException;
import org.xml.sax.SAXException;

import java.io.StringWriter;
import java.util.Collections;

public class SAXOutputterTest {

    private SAXOutputter outputter;
    private StringWriter stringWriter;

    @Before
    public void setUp() {
        outputter = new SAXOutputter(new SimpleContentHandler(), new SimpleErrorHandler(), new SimpleDTDHandler(), new SimpleEntityResolver());
        stringWriter = new StringWriter();
        outputter.setOutputFormat(new PrettyFormat());
        outputter.setContentHandler(new PrettyPrintingContentHandler(stringWriter));
    }

    @Test
    public void testOutputDocument() throws JDOMException, SAXException {
        Document document = new JDOMFactory().newDocument();
        document.setDocType(new DocumentType("root", null, Collections.emptyList(), new Element("html", null)));
        document.getRootElement().setText("Hello World");

        outputter.output(document);

        Assert.assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n<![CDATA[Hello World]]>\n</root>\n", stringWriter.toString());
    }

    @Test
    public void testOutputEmptyDocument() throws JDOMException, SAXException {
        Document document = new JDOMFactory().newDocument();

        outputter.output(document);

        Assert.assertEquals("", stringWriter.toString());
    }

    @Test
    public void testOutputListOfContents() throws JDOMException, SAXException {
        Document document = new JDOMFactory().newDocument();
        document.setDocType(new DocumentType("root", null, Collections.emptyList(), new Element("html", null)));
        document.getRootElement().addContent(new Element("p", null).setText("Paragraph 1"));
        document.getRootElement().addContent(new Element("p", null).setText("Paragraph 2"));

        outputter.output(document);

        Assert.assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n<p>Paragraph 1</p>\n<p>Paragraph 2</p>\n</root>\n", stringWriter.toString());
    }

    @Test
    public void testOutputElement() throws JDOMException, SAXException {
        Document document = new JDOMFactory().newDocument();
        document.setDocType(new DocumentType("root", null, Collections.emptyList(), new Element("html", null)));
        Element root = document.getRootElement();
        root.addContent(new Element("p", null).setText("Paragraph 1"));
        Element child = new Element("child", null);
        child.setText("Child text");
        root.addContent(child);

        outputter.output(document);

        Assert.assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n<p>Paragraph 1</p>\n<child>Child text</child>\n</root>\n", stringWriter.toString());
    }

    @Test
    public void testOutputFragment() throws JDOMException, SAXException {
        Document document = new JDOMFactory().newDocument();
        document.setDocType(new DocumentType("root", null, Collections.emptyList(), new Element("html", null)));
        Element root = document.getRootElement();
        root.addContent(new Element("p", null).setText("Paragraph 1"));

        SAXOutputter outputterForFragment = new SAXOutputter(null, null, null, null, null);
        outputterForFragment.setOutputFormat(new PrettyFormat());
        outputterForFragment.setContentHandler(new PrettyPrintingContentHandler(stringWriter));

        outputterForFragment.outputFragment(root.getContent());

        Assert.assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<p>Paragraph 1</p>\n", stringWriter.toString());
    }

    @Test
    public void testErrorHandling() throws JDOMException, SAXException {
        Document document = new JDOMFactory().newDocument();
        document.setDocType(new DocumentType("root", null, Collections.emptyList(), new Element("html", null)));
        document.getRootElement().setText("Invalid XML: <script>alert('Hello')</script>");

        ExpectedException.thrownBy(() -> outputter.output(document));

        Assert.assertEquals("Error parsing XML: org.jdom2.JDOMException: Content is not allowed in prolog.", exception.getMessage());
    }

    @After
    public void tearDown() {
        stringWriter = null;
        outputter = null;
    }
}
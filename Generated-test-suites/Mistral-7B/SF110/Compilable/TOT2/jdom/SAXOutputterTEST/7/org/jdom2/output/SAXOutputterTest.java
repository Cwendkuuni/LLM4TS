package org.jdom2.output;
import org.jdom2.*;
import org.jdom2.output.*;
import org.junit.Assert.*;
import org.junit.Test;

public class SAXOutputterTest {

    @Test
    public void testOutputDocument() throws JDOMException {
        SAXOutputter outputter = new SAXOutputter();
        Document doc = new Document();
        Element root = new Element("root");
        doc.setRootElement(root);
        Element child1 = new Element("child1").setText("Child 1");
        root.addContent(child1);
        outputter.output(doc);
        String expectedXml = "<root><child1>Child 1</child1></root>";
        // Add your assertions here to compare the output with expected XML
    }
}
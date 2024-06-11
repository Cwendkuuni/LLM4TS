import org.jdom2.*;
import org.jdom2.output.XMLOutputter;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class XMLOutputterTest {

    @Test
    public void testOutputDocument() throws Exception {
        // Given
        Document document = new JDOMFactory().newDocument();
        Element root = new Element("root");
        document.setRootElement(root);
        root.addContent(new Element("child1").setText("child1"));
        root.addContent(new Element("child2").setText("child2"));

        XMLOutputter xmlOutputter = new XMLOutputter();

        // When
        String xmlString = xmlOutputter.outputString(document);

        // Then
        String expectedXml = "<root><child1>child1</child1><child2>child2</child2></root>";
        assertThat(xmlString, is(expectedXml));
    }
}
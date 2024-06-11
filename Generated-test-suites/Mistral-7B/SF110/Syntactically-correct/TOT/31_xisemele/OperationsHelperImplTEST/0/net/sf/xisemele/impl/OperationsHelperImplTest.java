import org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.w3c.dom.*;
import java.util.ArrayList;
import java.util.List;

class OperationsHelperImplTest {
    @Test
    void testChildren() {
        Document document = createSampleDocument();
        Node root = document.getDocumentElement();
        List<Node> expectedChildren = Arrays.asList(
                createSampleElement("element1"),
                createSampleElement("element2"),
                createSampleElement("element3")
        );

        List<Node> actualChildren = new OperationsHelperImpl().children(root);

        assertEquals(expectedChildren, actualChildren);
    }

    private Document createSampleDocument() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
            return builder.newDocument();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Element createSampleElement(String tagName) {
        Document document = createSampleDocument();
        Element element = document.createElement(tagName);
        document.appendChild(element);
        return element;
    }
}
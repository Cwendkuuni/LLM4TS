import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class OperationsHelperImplTest {
    @Test
    public void testChildren() {
        Document document = createDocument();
        Node root = document.getDocumentElement();
        List<Node> expectedChildren = Arrays.asList(new Node[] { new Element("child1"), new Element("child2"), new Element("child3") });
        List<Node> actualChildren = new OperationsHelperImpl().children(root);
        assertEquals(expectedChildren, actualChildren);
    }

    private Document createDocument() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Document document;
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Element root = builder.newElement("root");
            root.appendChild(builder.newElement("child1"));
            root.appendChild(builder.newElement("child2"));
            root.appendChild(builder.newElement("child3"));
            document = builder.newDocument();
            document.appendChild(root);
        } catch (ParserConfigurationException | SAXException e) {
            throw new RuntimeException(e);
        }
        return document;
    }
}
import static org.junit.Assert.*;
import static org.w3c.dom.node.NodeType.*;

import net.sf.xisemele.impl.OperationsHelperImpl;
import org.junit.Test;
import org.w3c.dom.*;

public class OperationsHelperImplTest {

    @Test
    public void testChildren() {
        Document document = createSampleDocument();
        Node root = document.getDocumentElement();
        List<Node> expected = Arrays.asList(
                new ElementImpl("element1"),
                new TextNode("text1"),
                new CommentNode("comment1"),
                new ElementImpl("element2"),
                new TextNode("text2")
        );

        List<Node> actual = new OperationsHelperImpl().children(root);

        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i).getNodeName(), actual.get(i).getNodeName());
            assertEquals(expected.get(i).getNodeType(), actual.get(i).getNodeType());
        }
    }

    private Document createSampleDocument() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        Document document = null;
        try {
            document = builder.newDocument();
        } catch (DocumentInitializationException e) {
            e.printStackTrace();
        }

        Element root = document.createElement("root");
        root.setNodeName("root");

        Element element1 = document.createElement("element1");
        element1.setNodeName("element1");

        Text text1 = document.createTextNode("text1");

        Comment comment1 = document.createComment("comment1");

        Element element2 = document.createElement("element2");
        element2.setNodeName("element2");

        Text text2 = document.createTextNode("text2");

        root.appendChild(element1);
        root.appendChild(text1);
        root.appendChild(comment1);
        root.appendChild(element2);
        root.appendChild(text2);

        document.appendChild(root);

        return document;
    }
}
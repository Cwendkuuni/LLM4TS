import static org.junit.Assert.*;
import static org.w3c.dom.node.NodeConstraint.*;
import org.junit.Test;
import org.w3c.dom.*;
import java.util.*;

public class OperationsHelperImplTest {

    @Test
    public void testChildren() throws Exception {
        final Document document = createTestDocument();
        final Node root = document.getDocumentElement();
        final List<Node> expectedChildren = Arrays.asList(
            createElement("element1"),
            createElement("element2"),
            createElement("element3")
        );

        final List<Node> actualChildren = new OperationsHelperImpl().children(root);

        assertEquals("Number of children", 3, actualChildren.size());
        assertEquals("Children list", expectedChildren, actualChildren);
    }

    // Helper method to create a test document
    private Document createTestDocument() throws Exception {
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        final Document document = factory.newDocument();
        document.appendChild(createRootElement(document, "rootElement"));
        document.getDocumentElement().appendChild(createElement(document, "element1"));
        document.getDocumentElement().appendChild(createElement(document, "element2"));
        document.getDocumentElement().appendChild(createElement(document, "element3"));
        return document;
    }
}
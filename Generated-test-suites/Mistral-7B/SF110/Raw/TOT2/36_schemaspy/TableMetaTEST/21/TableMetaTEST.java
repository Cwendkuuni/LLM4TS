import static org.junit.Assert.*;
import net.sourceforge.schemaspy.model.xml.TableColumnMeta;
import net.sourceforge.schemaspy.model.xml.TableMeta;
import org.w3c.dom.*;

public class TableMetaTest {

    @Test
    public void testTableMetaConstruction() {
        Node tableNode = createTestNode();
        TableMeta tableMeta = new TableMeta(tableNode);
        // Existing test cases
    }

    @Test
    public void testTableMetaConstructionNullNode() {
        TableMeta tableMeta = new TableMeta(null);
        assertNull(tableMeta);
    }

    @Test
    public void testTableMetaConstructionNoAttributes() {
        Node tableNode = createTestNodeNoAttributes();
        TableMeta tableMeta = new TableMeta(tableNode);
        assertNull(tableMeta.getName());
        assertNull(tableMeta.getComments());
        assertNull(tableMeta.getRemoteCatalog());
        assertNull(tableMeta.getRemoteSchema());
    }

    @Test
    public void testTableMetaConstructionDuplicateAttributes() {
        Node tableNode = createTestNodeDuplicateAttributes();
        TableMeta tableMeta = new TableMeta(tableNode);
        // Test the existing properties
    }

    @Test
    public void testTableMetaConstructionWithHtmlComments() {
        Node tableNode = createTestNodeWithHtmlComments();
        TableMeta tableMeta = new TableMeta(tableNode);
        assertEquals("expectedName", tableMeta.getName());
        // Test the other properties
    }

    // Add more test cases and methods as needed

    private Node createTestNode() {
        // Existing createTestNode method
    }

    private Node createTestNodeNoAttributes() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Node root = builder.newDocument().createElement("root");
        return root;
    }

    private Node createTestNodeDuplicateAttributes() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Node root = builder.newDocument().createElement("root");
        root.setAttribute("name", "name1");
        root.setAttribute("name", "name2");
        return root;
    }

    private Node createTestNodeWithHtmlComments() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Node root = builder.newDocument().createElement("root");
        Node commentsNode = builder.newDocument().createElement("comments");
        commentsNode.setTextContent("<html><p>This is a test</p></html>");
        root.appendChild(commentsNode);
        return root;
    }
}
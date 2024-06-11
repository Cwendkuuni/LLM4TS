
import static org.junit.Assert.*;
import org.junit.Test;
import org.w3c.dom.*;
import java.util.Arrays;
import java.util.List;

public class TableMetaTest {
    @Test
    public void testgetName() {
        String expectedName = "testTable";
        Node tableNode = createMockTableNode("name", expectedName, null, null, null, null, null);
        TableMeta tableMeta = new TableMeta(tableNode);
        String actualName = tableMeta.getName();

        assertEquals(expectedName, actualName);
    }

    @Test
    public void testGetComments() {
        String expectedComments = "This is a test table.";
        Node tableNode = createMockTableNode(null, null, null, null, null, null, createMockNode("comments", expectedComments));
        TableMeta tableMeta = new TableMeta(tableNode);
        String actualComments = tableMeta.getComments();

        assertEquals(expectedComments, actualComments);
    }

    @Test
    public void testGetColumns() {
        String columnName1 = "col1";
        String columnName2 = "col2";
        Node columnNode1 = createMockNode("name", columnName1);
        Node columnNode2 = createMockNode("name", columnName2);

        Node tableNode = createMockTableNode("name", null, null, null, null, null, null, columnNode1, columnNode2);
        TableMeta tableMeta = new TableMeta(tableNode);

        List<TableColumnMeta> expectedColumns = Arrays.asList(new TableColumnMeta(columnNode1), new TableColumnMeta(columnNode2));
        List<TableColumnMeta> actualColumns = tableMeta.getColumns();

        assertEquals(expectedColumns, actualColumns);
    }

    @Test
    public void testGetRemoteCatalog() {
        String remoteCatalog = "testCatalog";
        Node tableNode = createMockTableNode(null, null, null, null, null, null, null, null, null, createMockNode("remoteCatalog", remoteCatalog));
        TableMeta tableMeta = new TableMeta(tableNode);

        String actualRemoteCatalog = tableMeta.getRemoteCatalog();

        assertEquals(remoteCatalog, actualRemoteCatalog);
    }

    @Test
    public void testGetRemoteSchema() {
        String remoteSchema = "testSchema";
        Node tableNode = createMockTableNode(null, null, null, null, null, null, null, null, null, null, createMockNode("remoteSchema", remoteSchema));
        TableMeta tableMeta = new TableMeta(tableNode);

        String actualRemoteSchema = tableMeta.getRemoteSchema();

        assertEquals(remoteSchema, actualRemoteSchema);
    }

    private Node createMockNode(String tagName, String nodeValue) {
        Element element = document.createElement(tagName);
        element.setTextContent(nodeValue);
        return element;
    }

    private Node createMockTableNode(String... nodeValues) {
        Element tableElement = document.createElement("table");
        for (String nodeName : nodeValues) {
            Node node;
            if (nodeName.startsWith("column")) {
                node = createMockNode("name", nodeName.substring(7));
                tableElement.appendChild(node);
            } else {
                node = createMockNode(nodeName, nodeName);
                tableElement.setAttribute(nodeName, node.getNodeValue());
            }
            tableElement.appendChild(node);
        }
        return tableElement;
    }
}

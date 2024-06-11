import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.*;
import org.w3c.dom.bootstrap.DOMImplementationRegistration;
import org.w3c.dom.html.HTMLDocument;

public class TableMetaTest {

    private static final String XML = "<table name='testTable'><column/></table>";
    private Node tableNode;
    private TableMeta tableMeta;

    @Before
    public void setUp() throws Exception {
        HTMLDocument document = (HTMLDocument) new DOMParser().parseFromString(XML, "text/html");
        tableNode = document.getElementsByTagName("table")
                .item(0);
        tableMeta = new TableMeta(tableNode);
    }

    @Test
    public void testTableMetaWithInvalidXML() {
        assertThrows(RuntimeException.class, () -> new TableMeta(mock(Node.class)));
    }

    @Test
    public void testTableMetaWithNullTableNode() {
        assertThrows(NullPointerException.class, () -> new TableMeta(null));
    }

    @Test
    public void testTableMetaWithEmptyColumnsList() {
        Node emptyTableNode = mock(Node.class);
        when(emptyTableNode.getElementsByTagName("column")).thenReturn(NodeList.EMPTY_NODE);
        assertThrows(RuntimeException.class, () -> new TableMeta(emptyTableNode));
    }

    @Test
    public void testTableMetaWithEmptyComments() {
        Node tableNodeWithEmptyComments = mock(Node.class);
        when(tableNodeWithEmptyComments.getAttributes()).thenReturn(createNamedNodeMap());
        TableMeta tableMetaWithEmptyComments = new TableMeta(tableNodeWithEmptyComments);
        assertNull(tableMetaWithEmptyComments.getComments());
    }

    @Test
    public void testLoggerLogMessage() {
        Logger logger = mock(Logger.class);
        when(logger.isLoggable(Level.FINE)).thenReturn(true);
        when(logger.log(Level.FINE, anyString())).thenCallRealMethod();
        TableMeta.logger = logger;
        TableMeta tableMeta = new TableMeta(mock(Node.class));
        verify(logger).log(Level.FINE, "Found XML table metadata for testTable remoteCatalog: null remoteSchema: null comments: null");
    }

    @Test
    public void testGetName() {
        String expectedName = "expectedName";
        when(tableNode.getAttributes().getNamedItem("name")).thenReturn(createNamedNode("name", expectedName));
        String actualName = tableMeta.getName();
        assertEquals(expectedName, actualName);
    }

    @Test
    public void testGetComments() {
        String expectedComments = "expectedComments";
        when(tableNode.getAttributes().getNamedItem("comments")).thenReturn(createNamedNode("comments", expectedComments));
        String actualComments = tableMeta.getComments();
        assertEquals(expectedComments, actualComments);

        when(tableNode.getAttributes().getNamedItem("comments")).thenReturn(null);
        String actualNullComments = tableMeta.getComments();
        assertNull(actualNullComments);
    }

    @Test
    public void testGetColumns() {
        List<TableColumnMeta> expectedColumns = new ArrayList<>();
        Node columnNode = mock(Node.class);
        TableColumnMeta columnMeta = new TableColumnMeta(columnNode);
        expectedColumns.add(columnMeta);
        when(tableNode.getElementsByTagName("column")).thenReturn(createNodeList(columnNode));

        List<TableColumnMeta> actualColumns = tableMeta.getColumns();
        assertEquals(expectedColumns, actualColumns);
    }

    @Test
    public void testGetRemoteCatalog() {
        String expectedRemoteCatalog = "expectedRemoteCatalog";
        when(tableNode.getAttributes().getNamedItem("remoteCatalog")).thenReturn(createNamedNode("remoteCatalog", expectedRemoteCatalog));
        String actualRemoteCatalog = tableMeta.getRemoteCatalog();
        assertEquals(expectedRemoteCatalog, actualRemoteCatalog);

        when(tableNode.getAttributes().getNamedItem("remoteCatalog")).thenReturn(null);
        String actualNullRemoteCatalog = tableMeta.getRemoteCatalog();
        assertNull(actualNullRemoteCatalog);
    }

    @Test
    public void testGetRemoteSchema() {
        String expectedRemoteSchema = "expectedRemoteSchema";
        when(tableNode.getAttributes().getNamedItem("remoteSchema")).thenReturn(createNamedNode("remoteSchema", expectedRemoteSchema));
        String actualRemoteSchema = tableMeta.getRemoteSchema();
        assertEquals(expectedRemoteSchema, actualRemoteSchema);

        when(tableNode.getAttributes().getNamedItem("remoteSchema")).thenReturn(null);
        String actualNullRemoteSchema = tableMeta.getRemoteSchema();
        assertNull(actualNullRemoteSchema);
    }

    private NamedNodeMap createNamedNodeMap() {
        NamedNodeMap nodeMap = mock(NamedNodeMap.class);
        when(nodeMap.getNamedItem("name")).thenReturn(createNamedNode("name", "testTable"));
        when(nodeMap.getNamedItem("comments"))
                .thenReturn(createNamedNode("comments", ""));
        when(nodeMap.getNamedItem("remoteSchema")).thenReturn(createNamedNode("remoteSchema", ""));
        when(nodeMap.getNamedItem("remoteCatalog")).thenReturn(createNamedNode("remoteCatalog", ""));
        return nodeMap;
    }

    private Node createNamedNode(String nodeName, String nodeValue) {
        Element element = mock(Element.class);
        when(element.getNodeName()).thenReturn(nodeName);
        when(element.getNodeValue()).thenReturn(nodeValue);
        Node namedNode = mock(Node.class);
        when(namedNode.getFirstChild()).thenReturn(element);
        return namedNode;
    }

    private NodeList createNodeList(Node node) {
        NodeList nodeList = mock(NodeList.class);
        when(nodeList.getItem(0)).thenReturn(node);
        return nodeList;
    }
}
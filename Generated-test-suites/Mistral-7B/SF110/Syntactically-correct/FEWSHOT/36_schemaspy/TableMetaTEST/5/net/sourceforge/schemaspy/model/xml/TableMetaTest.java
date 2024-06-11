package net.sourceforge.schemaspy.model.xml;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class TableMetaTest {

    private static final String TABLE_NAME = "tableName";
    private static final String TABLE_COMMENTS = "table comments";
    private static final String TABLE_REMOTE_CATALOG = "remoteCatalog";
    private static final String TABLE_REMOTE_SCHEMA = "remoteSchema";

    private TableMeta tableMeta;
    private Element tableNode;

    @Before
    public void setUp() {
        tableNode = mock(Element.class);
        when(tableNode.getAttributes()).thenReturn(mock(NamedNodeMap.class));
        when(tableNode.getChildNodes()).thenReturn(mock(NodeList.class));

        Node nameNode = mock(Node.class);
        Node commentsNode = mock(Node.class);
        Node remoteSchemaNode = mock(Node.class);
        Node remoteCatalogNode = mock(Node.class);

        when(tableNode.getChildNodes().item(0)).thenReturn(nameNode);
        when(tableNode.getChildNodes().item(1)).thenReturn(commentsNode);
        when(tableNode.getAttributes().getNamedItem("name")).thenReturn(nameNode);
        when(tableNode.getAttributes().getNamedItem("comments")).thenReturn(commentsNode);
        when(tableNode.getAttributes().getNamedItem("remoteSchema")).thenReturn(remoteSchemaNode);
        when(tableNode.getAttributes().getNamedItem("remoteCatalog")).thenReturn(remoteCatalogNode);

        NodeList columnNodes = new ArrayList<Node>() {{
            add(mock(Node.class));
        }}.toArray(new Node[0]);
        when(tableNode.getElementsByTagName("column")).thenReturn(columnNodes);

        tableMeta = new TableMeta(tableNode);
    }

    @Test
    public void testTableMetaConstruction() {
        assertEquals(TABLE_NAME, tableMeta.getName());
        assertEquals(TABLE_COMMENTS, tableMeta.getComments());
        assertEquals(0, tableMeta.getColumns().size());
        assertEquals(null, tableMeta.getRemoteCatalog());
        assertEquals(null, tableMeta.getRemoteSchema());
    }

    @Test
    public void testTableMetaGetName() {
        assertEquals(TABLE_NAME, tableMeta.getName());
    }

    @Test
    public void testTableMetaGetComments() {
        assertEquals(TABLE_COMMENTS, tableMeta.getComments());
    }

    @Test
    public void testTableMetaGetColumns() {
        assertEquals(1, tableMeta.getColumns().size());
    }

    @Test
    public void testTableMetaGetRemoteCatalog() {
        when(remoteCatalogNode.getNodeValue()).thenReturn("remoteCatalogValue");
        assertEquals("remoteCatalogValue", tableMeta.getRemoteCatalog());
    }

    @Test
    public void testTableMetaGetRemoteSchema() {
        when(remoteSchemaNode.getNodeValue()).thenReturn("remoteSchemaValue");
        assertEquals("remoteSchemaValue", tableMeta.getRemoteSchema());
    }

    @Test
    public void testTableMetaLogger() {
        TableMeta.logger.info("Test log message");
        verify(TableMeta.logger, times(1)).info("Found XML table metadata for tableName remoteCatalog: null remoteSchema: null comments: null");
    }
}
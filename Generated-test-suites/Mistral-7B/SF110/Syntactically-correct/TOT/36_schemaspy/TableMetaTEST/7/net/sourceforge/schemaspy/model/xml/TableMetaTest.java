package net.sourceforge.schemaspy.model.xml;

import org.junit.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.w3c.dom.*;

public class TableMetaTest {

    @BeforeClass
    public static void beforeClass() {
        Logger.getLogger(TableMeta.class.getName()).setLevel(Level.FINE);
    }

    @Test
    public void testTableMetaConstruction() {
        Node tableNode = mock(Node.class);
        when(tableNode.getAttributes()).thenReturn(createNodeAttributes("name", "comments", "remoteSchema", "remoteCatalog"));
        when(tableNode.getChildNodes()).thenReturn(createNodeList("columnNode1", "columnNode2"));

        TableMeta tableMeta = new TableMeta(tableNode);

        Assert.assertEquals("name", tableMeta.getName());
        Assert.assertEquals(2, tableMeta.getColumns().size());
        Assert.assertEquals("remoteCatalog", tableMeta.getRemoteCatalog());
        Assert.assertEquals("remoteSchema", tableMeta.getRemoteSchema());
        Assert.assertNull(tableMeta.getComments());

        TableMeta.logger.fine("Found XML table metadata for name remoteCatalog: null remoteSchema: null comments: null");
    }

    @Test
    public void testTableMetaComments() {
        Node tableNode = mock(Node.class);
        when(tableNode.getAttributes()).thenReturn(createNodeAttributes("name", "comments=Test Comments", null, null));

        TableMeta tableMeta = new TableMeta(tableNode);

        Assert.assertEquals("Test Comments", tableMeta.getComments());

        Node tableNodeWithRemarks = mock(Node.class);
        when(tableNodeWithRemarks.getAttributes()).thenReturn(createNodeAttributes("name", null, null, "remarks=Test Remarks"));

        TableMeta tableMetaWithRemarks = new TableMeta(tableNodeWithRemarks);

        Assert.assertEquals("Test Remarks", tableMetaWithRemarks.getComments());

        Node tableNodeWithoutComments = mock(Node.class);
        when(tableNodeWithoutComments.getAttributes()).thenReturn(createNodeAttributes("name"));

        TableMeta tableMetaWithoutComments = new TableMeta(tableNodeWithoutComments);

        Assert.assertNull(tableMetaWithoutComments.getComments());
    }

    @Test
    public void testTableMetaInvalidConstruction() {
        Node tableNode = mock(Node.class);
        when(tableNode.getAttributes()).thenReturn(createNodeAttributes());

        try {
            new TableMeta(tableNode);
            fail("Exception expected");
        } catch (IllegalArgumentException e) {
            // expected
        }

        Node tableNodeWithMissingName = mock(Node.class);
        when(tableNodeWithMissingName.getAttributes()).thenReturn(createNodeAttributes("", "comments", null, null));

        try {
            new TableMeta(tableNodeWithMissingName);
            fail("Exception expected");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    private NodeList createNodeList(String... nodeNames) {
        NodeList nodeList = mock(NodeList.class);
        for (int i = 0; i < nodeNames.length; i++) {
            Node node = mock(Node.class);
            when(node.getNodeName()).thenReturn(nodeNames[i]);
            when(nodeList.item(i)).thenReturn(node);
        }
        return nodeList;
    }

    private NodeAttributes createNodeAttributes(String... attributeNamesAndValues) {
        NodeAttributes attributes = mock(NodeAttributes.class);
        for (String attributeNameAndValue : attributeNamesAndValues) {
            String[] nameValue = attributeNameAndValue.split("=");
            Node attributeNode = mock(Node.class);
            when(attributeNode.getNodeValue()).thenReturn(nameValue[1]);
            when(attributeNode.getNodeName()).thenReturn(nameValue[0]);
            when(attributes.getNamedItem(nameValue[0])).thenReturn(attributeNode);
        }
        return attributes;
    }
}
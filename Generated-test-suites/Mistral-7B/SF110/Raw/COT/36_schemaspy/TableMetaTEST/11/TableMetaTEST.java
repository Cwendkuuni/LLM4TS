import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

public class TableMetaTest {

    @Test
    public void testTableMetaConstruction() {
        Node tableNode = createMockTableNode();
        TableMeta tableMeta = new TableMeta(tableNode);

        Assert.assertEquals("expected table name", "actual_table_name", tableMeta.getName());
        Assert.assertEquals("expected comments", "actual_comments", tableMeta.getComments());
        Assert.assertNotNull("expected not null columns", tableMeta.getColumns());
        Assert.assertEquals("expected remoteCatalog", "actual_remoteCatalog", tableMeta.getRemoteCatalog());
        Assert.assertEquals("expected remoteSchema", "actual_remoteSchema", tableMeta.getRemoteSchema());
    }

    @Test(expected = NullPointerException.class)
    public void testTableMetaConstructionWithNullTableNode() {
        TableMeta tableMeta = new TableMeta(null);
    }

    @Test
    public void testGetTableColumnsEmptyList() {
        Node tableNode = createMockTableNodeWithEmptyColumns();
        TableMeta tableMeta = new TableMeta(tableNode);

        List<TableColumnMeta> columns = tableMeta.getColumns();
        Assert.assertEquals("size of columns list", 0, columns.size());
    }

    @Test
    public void testGetTableColumnsNonEmptyList() {
        Node tableNode = createMockTableNodeWithColumns();
        TableMeta tableMeta = new TableMeta(tableNode);

        List<TableColumnMeta> columns = tableMeta.getColumns();
        Assert.assertFalse("columns list is not empty", columns.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTableMetaWithNullNameAttribute() {
        Node tableNode = createMockTableNodeWithNullNameAttribute();
        TableMeta tableMeta = new TableMeta(tableNode);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTableMetaWithEmptyNameAttribute() {
        Node tableNode = createMockTableNodeWithEmptyNameAttribute();
        TableMeta tableMeta = new TableMeta(tableNode);
    }

    private Node createMockTableNode() {
        Element tableElement = createMockElement("table");
        return tableElement;
    }

    private Node createMockTableNodeWithEmptyColumns() {
        Element tableElement = createMockElement("table");
        return tableElement;
    }

    private Node createMockTableNodeWithColumns() {
        Element tableElement = createMockElement("table");
        NodeList columnNodes = createMockNodeListWithLength(2);
        tableElement.appendChild(columnNodes.item(0));
        tableElement.appendChild(columnNodes.item(1));
        return tableElement;
    }

    private Node createMockTableNodeWithNullNameAttribute() {
        Element tableElement = createMockElement("table");
        tableElement.removeAttribute("name");
        return tableElement;
    }

    private Node createMockTableNodeWithEmptyNameAttribute() {
        Element tableElement = createMockElement("table");
        tableElement.setAttribute("name", "");
        return tableElement;
    }

    private Element createMockElement(String tagName) {
        Element element = mock(Element.class);
        when(element.getTagName()).thenReturn(tagName);
        return element;
    }

    private NodeList createMockNodeListWithLength(int length) {
        NodeList nodeList = mock(NodeList.class);
        when(nodeList.getLength()).thenReturn(length);
        return nodeList;
    }
}
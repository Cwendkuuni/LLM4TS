package net.sourceforge.schemaspy.model.xml;

import org.junit.Test;
import static org.junit.Assert.*;

public class TableMetaTest {

    @Test
    public void testGetName() {
        String expectedName = "expectedTableName";
        TableMeta tableMeta = new TableMeta(createTestNode("name", expectedName));
        String actualName = tableMeta.getName();
        assertEquals(expectedName, actualName);
    }

    @Test
    public void testTableMetaWithNullTableNode() {
        TableMeta tableMeta = new TableMeta(null);
        assertEquals(null, tableMeta.getName());
        // More test cases for edge cases and exceptions
    }

    @Test
    public void testTableMetaWithEmptyColumnNodes() {
        Node tableNode = createTestNode("table", null);
        TableMeta tableMeta = new TableMeta(tableNode);
        assertTrue(tableMeta.getColumns().isEmpty());
    }

    // Test cases for other edge cases and exceptions

    private Node createTestNode(String nodeName, String nodeValue) {
        Element element = new Element("testElement");
        element.setAttribute("name", nodeName);
        if (nodeValue != null) {
            Text textNode = new Text(nodeValue);
            element.appendChild(textNode);
        }
        return element;
    }
}
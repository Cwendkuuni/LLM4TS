import org.junit.Test;
import static org.junit.Assert.*;

public class TableMetaTest {
    @Test
    public void testGetName() {
        String expectedName = "testTable";
        Node tableNode = createTestNode("name", "testTable");
        TableMeta tableMeta = new TableMeta(tableNode);
        String actualName = tableMeta.getName();

        assertEquals(expectedName, actualName);
    }

    private Node createTestNode(String nodeName, String nodeValue) {
        Document document = new DocumentImpl();
        Element tableElement = document.createElement("table");
        tableElement.setAttribute("name", nodeValue);
        document.appendChild(tableElement);
        Node nameNode = document.createAttributes().getNamedItem(nodeName);
        tableElement. setAttributeNode(nameNode);
        return tableElement;
    }
}
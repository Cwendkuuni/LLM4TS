import org.junit.Test;
import static org.junit.Assert.*;

public class TableMetaTest {
    @Test
    public void testGetName() {
        String expectedName = "testTable";
        Node tableNode = createTestTableNode("name", expectedName);
        TableMeta tableMeta = new TableMeta(tableNode);
        String actualName = tableMeta.getName();
        assertEquals(expectedName, actualName);
    }

    // Util method to create a test Node for TableMeta construction
    private Node createTestTableNode(String nameAttribute, String nameValue) {
        Element tableElement = document.createElement("table");
        tableElement.setAttribute("name", nameValue);
        NamedNodeMap attribs = tableElement.getAttributes();
        return tableElement;
    }
}
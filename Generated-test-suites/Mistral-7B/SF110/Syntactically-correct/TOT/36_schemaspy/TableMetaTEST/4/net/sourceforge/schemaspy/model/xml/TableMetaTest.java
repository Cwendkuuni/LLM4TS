import static org.junit.Assert.*;
import net.sourceforge.schemaspy.model.xml.TableColumnMeta;
import net.sourceforge.schemaspy.model.xml.TableMeta;

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
        String expectedComments = "This table stores customer information.";
        Node tableNode = createMockTableNode(null, null, null, null, null, null, "comments", expectedComments);
        TableMeta tableMeta = new TableMeta(tableNode);
        String actualComments = tableMeta.getComments();
        assertEquals(expectedComments, actualComments);
    }

    @Test
    public void testGetColumns() {
        String expectedColumnName1 = "column1";
        String expectedColumnName2 = "column2";
        Node tableNode = createMockTableNode(null, null, null, null, null, null, null, null, "column1", "column2");
        TableMeta tableMeta = new TableMeta(tableNode);
        List<TableColumnMeta> actualColumns = tableMeta.getColumns();
        assertEquals(2, actualColumns.size());
        assertTrue(actualColumns.contains(new TableColumnMeta(createMockColumnNode("name", expectedColumnName1))));
        assertTrue(actualColumns.contains(new TableColumnMeta(createMockColumnNode("name", expectedColumnName2))));
    }

    private Node createMockTableNode(String... attributes) {
        // Implement logic to create a mock Node with the given attributes
    }

    private Node createMockColumnNode(String... attributes) {
        // Implement logic to create a mock Node with the given attributes
    }
}
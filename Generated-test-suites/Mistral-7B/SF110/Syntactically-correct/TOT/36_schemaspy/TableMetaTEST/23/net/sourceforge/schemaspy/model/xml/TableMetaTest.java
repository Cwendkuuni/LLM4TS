import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TableMetaTest {

    @Test
    public void testGetName() {
        Node tableNode = createMockTableNode("name", "comments", null, null, null, null, null);
        TableMeta tableMeta = new TableMeta(tableNode);
        String expectedName = "tableName";
        String actualName = tableMeta.getName();

        assertEquals(expectedName, actualName);
    }
}
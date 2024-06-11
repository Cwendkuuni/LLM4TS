import static org.junit.Assert.*;
import net.sourceforge.schemaspy.model.xml.TableMeta;

public class TableMetaTest {
    @Test
    public void testGetName() {
        Node tableNode = createMockNode("name", "value"); // Create a mock node for testing
        TableMeta tableMeta = new TableMeta(tableNode);
        String name = tableMeta.getName();
        assertEquals("value", name);
    }
}
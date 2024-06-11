import org.junit.Assert;
import org.junit.Test;
import net.sourceforge.schemaspy.model.xml.TableMeta;

public class TableMetaTest {
    @Test
    public void testGetName() {
        Node tableNode = createMockTableNode("name", "comments", "remoteSchema", "remoteCatalog");
        TableMeta tableMeta = new TableMeta(tableNode);
        String expectedName = "name";
        String actualName = tableMeta.getName();
        Assert.assertEquals(expectedName, actualName);
    }
    
    private Node createMockTableNode(String name, String comments, String remoteSchema, String remoteCatalog) {
        // Implementation of creating a mock Node object with the given attributes
    }
}
import static org.junit.Assert.*;
import net.sourceforge.schemaspy.model.xml.TableMeta;

public class TableMetaTest {
  @Test
  public void testGetName() {
    Node tableNode = new Node() { /* dummy implementation */ };
    String expectedName = "testTable";
    TableMeta tableMeta = new TableMeta(tableNode);
    String actualName = tableMeta.getName();
    assertEquals(expectedName, actualName);
  }
}
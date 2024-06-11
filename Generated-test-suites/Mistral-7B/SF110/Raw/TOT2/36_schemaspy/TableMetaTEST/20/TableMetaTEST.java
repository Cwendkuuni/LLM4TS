import static org.junit.Assert.*;
import java.util.*;
import org.junit.Test;
import static org.mockito.Mockito.*;
import net.sourceforge.schemaspy.model.xml.TableColumnMeta;
import net.sourceforge.schemaspy.model.xml.TableMeta;

public class TableMetaTest {
  @Test
  public void testTableMetaConstructor() {
    Node tableNode = mock(Node.class);
    when(tableNode.getNodeName()).thenReturn("table");
    when(tableNode.getAttributes()).thenReturn(mock(NamedNodeMap.class));
    when(tableNode.getChildNodes()).thenReturn(mock(NodeList.class));
    TableMeta tableMeta = new TableMeta(tableNode);
    assertEquals("table", tableMeta.getName());
    assertNull(tableMeta.getComments());
    assertTrue(tableMeta.getColumns().isEmpty());
    assertNull(tableMeta.getRemoteCatalog());
    assertNull(tableMeta.getRemoteSchema());
  }

  @Test
  public void testGetName() {
    String name = "testTable";
    TableMeta tableMeta = new TableMeta(mock(Node.class));
    when(tableMeta.getName()).thenReturn(name);
    assertEquals(name, tableMeta.getName());
  }

  @Test
  public void testGetComments() {
    String comments = "testComments";
    TableMeta tableMeta = new TableMeta(mock(Node.class));
    when(tableMeta.getComments()).thenReturn(comments);
    assertEquals(comments, tableMeta.getComments());
  }

  @Test
  public void testGetColumns() {
    List<TableColumnMeta> columns = Arrays.asList(new TableColumnMeta(mock(Node.class)), new TableColumnMeta(mock(Node.class)));
    TableMeta tableMeta = new TableMeta(mock(Node.class));
    when(tableMeta.getColumns()).thenReturn(columns);
    assertEquals(columns, tableMeta.getColumns());
  }

  @Test
  public void testGetRemoteCatalog() {
    String remoteCatalog = "testRemoteCatalog";
    TableMeta tableMeta = new TableMeta(mock(Node.class));
    when(tableMeta.getRemoteCatalog()).thenReturn(remoteCatalog);
    assertEquals(remoteCatalog, tableMeta.getRemoteCatalog());
  }

  @Test
  public void testGetRemoteSchema() {
    String remoteSchema = "testRemoteSchema";
    TableMeta tableMeta = new TableMeta(mock(Node.class));
    when(tableMeta.getRemoteSchema()).thenReturn(remoteSchema);
    assertEquals(remoteSchema, tableMeta.getRemoteSchema());
  }

  // Add test cases for edge cases and exceptions here

  @Test(expected = NullPointerException.class)
  public void testTableMetaConstructorWithNullTableNode() {
    TableMeta tableMeta = new TableMeta(null);
  }

  @Test
  public void testGetEmptyName() {
    TableMeta tableMeta = new TableMeta(mock(Node.class));
    Node nameNode = mock(Node.class);
    when(nameNode.getNodeValue()).thenReturn("");
    when(tableMeta.getName()).thenReturn(nameNode);
    assertEquals("", tableMeta.getName());
  }

  @Test
  public void testEmptyColumnsList() {
    TableMeta tableMeta = new TableMeta(mock(Node.class));
    NodeList columnNodes = mock(NodeList.class);
    when(tableMeta.getColumns()).thenReturn(Collections.emptyList());
    assertEquals(Collections.emptyList(), tableMeta.getColumns());
  }

  @Test
  public void testNullRemoteCatalog() {
    TableMeta tableMeta = new TableMeta(mock(Node.class));
    Node remoteCatalogNode = mock(Node.class);
    when(tableMeta.getRemoteCatalog()).thenReturn(remoteCatalogNode);
    assertNull(tableMeta.getRemoteCatalog());
  }

  @Test
  public void testNullRemoteSchema() {
    TableMeta tableMeta = new TableMeta(mock(Node.class));
    Node remoteSchemaNode = mock(Node.class);
    when(tableMeta.getRemoteSchema()).thenReturn(remoteSchemaNode);
    assertNull(tableMeta.getRemoteSchema());
  }
}
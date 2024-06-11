import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import static org.junit.Assert.*;
import static net.sourceforge.schemaspy.model.xml.TableMeta.logger;

public class TableMetaTest {

 private TableMeta tableMeta;
 private Document document;

 @Before
 public void setUp() {
 // Setup for creating TableMeta object using the given Node
 document = createDocument();
 Node tableNode = document.createElement("Table");
 tableNode.setAttribute("name", "testTable");
 tableNode.setAttribute("comments", "test Comments");
 tableNode.setAttribute("remoteSchema", "testRemoteSchema");
 tableNode.setAttribute("remoteCatalog", "testRemoteCatalog");

 NodeList columnNodes = document.createProcessingInstruction("xml-local-declaration", "<xml-model><xsd:schema xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"><xsd:element name=\"column\"><xsd:complexType><xsd:sequence><xsd:element name=\"name\"></xsd:element></xsd:sequence></xsd:complexType></xsd:element></xsd:schema></xml-model>");
 Element root = document.createElement("Root");
 root.appendChild(tableNode);
 NodeList children = document.createProcessingInstruction("xml-declaration", "version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"").getParentNodes();
 children.item(0).getParentNode().insertBefore(root, children.item(0));

 tableMeta = new TableMeta(tableNode);
 }

 @Test
 public void testTableMetaConstructor() {
 // Basic test case for constructor
 assertEquals("testTable", tableMeta.getName());
 assertNull(tableMeta.getComments());
 assertNull(tableMeta.getRemoteCatalog());
 assertNull(tableMeta.getRemoteSchema());
 assertNotNull(tableMeta.getColumns());
 logger.fine("Expected output: Found XML table metadata for testTable remoteCatalog: null remoteSchema: null comments: null");
 }

 @Test(expected = NullPointerException.class)
 public void testTableMetaConstructorNullTableNode() {
 // Test case for constructor with null tableNode
 TableMeta nullTableMeta = new TableMeta(null);
 fail("Exception should be thrown: NullPointerException");
 }

 @Test(expected = NullPointerException.class)
 public void testTableMetaConstructorNullNameNode() {
 // Test case for constructor with null name Node
 NamedNodeMap attribs = document.createAttributeNode("name");
 Node tableNode = document.createElement("Table");
 tableNode.setAttributeNode(attribs);
 TableMeta nullTableMeta = new TableMeta(tableNode);
 fail("Exception should be thrown: NullPointerException");
 }

 @Test
 public void testGetName() {
 // Basic test case for getName()
 assertEquals("testTable", tableMeta.getName());
 }

 @Test
 public void testGetComments() {
 // Basic test case for getComments()
 assertEquals("test Comments", tableMeta.getComments());
 }

 @Test
 public void testGetColumns() {
 // Basic test case for getColumns()
 assertFalse(tableMeta.getColumns().isEmpty());
 }

 @Test
 public void testGetRemoteCatalog() {
 // Basic test case for getRemoteCatalog()
 assertEquals("testRemoteCatalog", tableMeta.getRemoteCatalog());
 }

 @Test
 public void testGetRemoteSchema() {
 // Basic test case for getRemoteSchema()
 assertEquals("testRemoteSchema", tableMeta.getRemoteSchema());
 }

 @Test(expected = NoSuchElementException.class)
 public void testGetColumnsWhenEmpty() {
 // Test case for getColumns() when empty
 TableMeta emptyTableMeta = new TableMeta(document.createElement("Table"));
 List<TableColumnMeta> columns = emptyTableMeta.getColumns();
 TableColumnMeta column = new TableColumnMeta(document.createElement("column"));
 columns.add(column);
 columns.remove(0); // remove first element
 fail("Exception should be thrown: NoSuchElementException");
 }
}
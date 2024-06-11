import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;

public class TableMetaTest {

 private static final Logger logger = Logger.getLogger(TableMeta.class.getName());
 private TableMeta tableMeta;

 @Before
 public void setup() {
 Node tableNode = createTestNode();
 table Meta = new TableMeta(tableNode);
 this.tableMeta = tableMeta;
 }

 @Test
 public void testConstructor() {
 assertEquals("expected name", "expectedName", tableMeta.getName());
 assertEquals("expected comments", "expectedComments", tableMeta.getComments());
 assertNotNull("columns should not be null", tableMeta.getColumns());
 assertNull("remoteCatalog should be null", tableMeta.getRemoteCatalog());
 assertNull("remoteSchema should be null", tableMeta.getRemoteSchema());
 }

 @Test
 public void testGetName() {
 String name = "expectedName";
 TableMeta tableMeta = new TableMeta(createTestNode(name));
 assertEquals(name, tableMeta.getName());
 }

 @Test
 public void testGetComments() {
 String comments = "expectedComments";
 Node commentsNode = createTestNode("comments", comments);
 TableMeta tableMeta = new TableMeta(createTestNode(null, commentsNode));
 assertEquals(comments, tableMeta.getComments());
 }

 @Test
 public void testGetColumns() {
 List<TableColumnMeta> columns = new ArrayList<>();
 columns.add(new TableColumnMeta(createTestNode()));
 TableMeta tableMeta = new TableMeta(createTestNode(null, null, columns.toArray(new TableColumnMeta[0])));
 assertEquals(columns, tableMeta.getColumns());
 }

 @Test
 public void testGetRemoteCatalog() {
 String remoteCatalog = "expectedRemoteCatalog";
 Node remoteCatalogNode = createTestNode("remoteCatalog", remoteCatalog);
 TableMeta tableMeta = new TableMeta(createTestNode(null, null, null, remoteCatalogNode));
 assertEquals(remoteCatalog, tableMeta.getRemoteCatalog());
 }

 @Test
 public void testGetRemoteSchema() {
 String remoteSchema = "expectedRemoteSchema";
 Node remoteSchemaNode = createTestNode("remoteSchema", remoteSchema);
 TableMeta tableMeta = new TableMeta(createTestNode(null, null, null, null, remoteSchemaNode));
 assertEquals(remoteSchema, tableMeta.getRemoteSchema());
 }

 @Test(expected = NullPointerException.class)
 public void testNoNameAttribute() {
 Node tableNode = createTestNode();
 TableMeta tableMeta = new TableMeta(tableNode);
 assertNull(tableMeta);
 }

 @Test(expected = NullPointerException.class)
 public void testNoColumnsNode() {
 Node tableNode = createTestNode(null, null);
 TableMeta tableMeta = new TableMeta(tableNode);
 assertNull(tableMeta);
 }

 @Test(expected = NullPointerException.class)
 public void testNullColumns() {
 Node tableNode = createTestNode(null, null, null);
 TableMeta tableMeta = new TableMeta(tableNode);
 assertNull(tableMeta);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyColumnsList() {
 Node tableNode = createTestNode(null, null, new ArrayList<>());
 TableMeta tableMeta = new TableMeta(tableNode);
 assertNull(tableMeta);
 }

 private Node createTestNode(String... attributesAndValues) {
 Element element = new Element("table");
 for (int i = 0; i < attributesAndValues.length; i += 2) {
 if (attributesAndValues[i] != null) {
 element.setAttribute(attributesAndValues[i], attributesAndValues[i + 1]);
 }
 }
 return element;
 }

 private Node createTestNode(String name, String comments) {
 Element element = new Element("table");
 element.setAttribute("name", name);
 Node commentsNode = new TextNode("" + comments);
 element.appendChild(commentsNode);
 return element;
 }

 private Node createTestNode(String name, String comments, TableColumnMeta... columns) {
 Element element = new Element("table");
 element.setAttribute("name", name);
 if (comments != null) {
 Node commentsNode = new TextNode("" + comments);
 element.appendChild(commentsNode);
 }
 Element columnsNode = new Element("columns");
 for (TableColumnMeta column : columns) {
 columnsNode.appendChild(column.getNode());
 }
 element.appendChild(columnsNode);
 return element;
 }

 private Node createTestNode(String name, String comments, String remoteCatalog, String remoteSchema) {
 Element element = new Element("table");
 element.setAttribute("name", name);
 if (comments != null) {
 Node commentsNode = new TextNode("" + comments);
 element.appendChild(commentsNode);
 }
 if (remoteCatalog != null) {
 element.setAttribute("remoteCatalog", remoteCatalog);
 }
 if (remoteSchema != null) {
 element.setAttribute("remoteSchema", remoteSchema);
 }
 return element;
 }

 private Node createTestNode(String name, String comments, String remoteCatalog, String remoteSchema, TableColumnMeta... columns) {
 Element element = new Element("table");
 element.setAttribute("name", name);
 if (comments != null) {
 Node commentsNode = new TextNode("" + comments);
 element.appendChild(commentsNode);
 }
 if (remoteCatalog != null) {
 element.setAttribute("remoteCatalog", remoteCatalog);
 }
 if (remoteSchema != null) {
 element.setAttribute("remoteSchema", remoteSchema);
 }
 Element columnsNode = new Element("columns");
 for (TableColumnMeta column : columns) {
 columnsNode.appendChild(column.getNode());
 }
 element.appendChild(columnsNode);
 return element;
 }
}
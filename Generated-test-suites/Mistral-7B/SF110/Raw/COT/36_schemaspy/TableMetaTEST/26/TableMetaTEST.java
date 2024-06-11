package net.sourceforge.schemaspy.model.xml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertFalse;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.sourceforge.schemaspy.model.TableColumnMeta;

@RunWith(MockitoJUnitRunner.class)
public class TableMetaTest {

 private Node tableNode;
 private TableMeta tableMeta;

 @Before
 public void setUp() {
 tableNode = Mockito.mock(Node.class);
 tableMeta = new TableMeta(tableNode);
 }

 @Test
 public void testTableMetaConstructor() {
 // Basic functionality test
 assertEquals("name", tableNode.getAttributes().getNamedItem("name").getNodeValue(), tableMeta.getName());
 Node commentsNode = Mockito.mock(Node.class);
 Mockito.when(tableNode.getAttributes().getNamedItem("comments")).thenReturn(commentsNode);
 Mockito.when(tableNode.getAttributes().getNamedItem("remarks")).thenReturn(commentsNode);
 assertEquals("comments", commentsNode.getNodeValue().trim(), tableMeta.getComments());
 Node remoteCatalogNode = Mockito.mock(Node.class);
 Mockito.when(tableNode.getAttributes().getNamedItem("remoteCatalog")).thenReturn(remoteCatalogNode);
 Mockito.when(tableNode.getAttributes().getNamedItem("remoteSchema")).thenReturn(remoteCatalogNode);
 assertNull(tableMeta.getRemoteCatalog());
 assertNull(tableMeta.getRemoteSchema());
 TableMeta loggerMock = Mockito.mock(TableMeta.class);
 Mockito.when(loggerMock.logger).thenReturn(Mockito.mock(Logger.class));
 TableMeta.logger = loggerMock;
 TableColumnMeta columnMeta = Mockito.mock(TableColumnMeta.class);
 List<TableColumnMeta> columnList = new ArrayList<>();
 columnList.add(columnMeta);
 Mockito.when(tableNode.getChildNodes()).thenReturn(createMockNodeList(columnList.toArray(new TableColumnMeta[columnList.size()])));
 assertEquals("columns", columnList, tableMeta.getColumns());
 }

 @Test(expected = NullPointerException.class)
 public void testTableMetaConstructorWithNullNode() {
 new TableMeta(null);
 }

 @Test(expected = NullPointerException.class)
 public void testTableMetaConstructorWithNullAttributes() {
 Node node = Mockito.mock(Node.class);
 tableMeta = new TableMeta(node);
 Mockito.when(node.getAttributes()).thenReturn(null);
 }

 @Test
 public void testTableMetaGetters() {
 assertSame(tableNode, tableMeta.getNode());
 }

 @Test
 public void testTableMetaGettersWithEmptyComments() {
 Node emptyCommentsNode = Mockito.mock(Node.class);
 Mockito.when(tableNode.getAttributes().getNamedItem("comments")).thenReturn(emptyCommentsNode);
 Mockito.when(tableNode.getAttributes().getNamedItem("remarks")).thenReturn(emptyCommentsNode);
 assertNull(tableMeta.getComments());
 }

 @Test
 public void testTableMetaGettersWithComments() {
 String comments = "test comments";
 Node commentsNode = Mockito.mock(Node.class);
 Mockito.when(tableNode.getAttributes().getNamedItem("comments")).thenReturn(commentsNode);
 Mockito.when(commentsNode.getNodeValue()).thenReturn(comments);
 assertEquals(comments, tableMeta.getComments());
 }

 @Test
 public void testTableMetaGettersWithRemoteCatalogAndSchema() {
 String remoteCatalog = "test remote catalog";
 String remoteSchema = "test remote schema";
 Node remoteCatalogNode = Mockito.mock(Node.class);
 Node remoteSchemaNode = Mockito.mock(Node.class);
 Mockito.when(tableNode.getAttributes().getNamedItem("remoteCatalog")).thenReturn(remoteCatalogNode);
 Mockito.when(tableNode.getAttributes().getNamedItem("remoteSchema")).thenReturn(remoteSchemaNode);
 Mockito.when(remoteCatalogNode.getNodeValue()).thenReturn(remoteCatalog);
 Mockito.when(remoteSchemaNode.getNodeValue()).thenReturn(remoteSchema);
 assertEquals(remoteCatalog, tableMeta.getRemoteCatalog());
 assertEquals(remoteSchema, tableMeta.getRemoteSchema());
 }

 @Test
 public void testTableMetaGettersWithEmptyRemoteCatalogAndSchema() {
 Node remoteCatalogNode = Mockito.mock(Node.class);
 Node remoteSchemaNode = Mockito.mock(Node.class);
 Mockito.when(tableNode.getAttributes().getNamedItem("remoteCatalog")).thenReturn(remoteCatalogNode);
 Mockito.when(tableNode.getAttributes().getNamedItem("remoteSchema")).thenReturn(remoteSchemaNode);
 assertNull(tableMeta.getRemoteCatalog());
 assertNull(tableMeta.getRemoteSchema());
 }

 @Test
 public void testTableMetaGetColumns() {
 List<TableColumnMeta> columns = Arrays.asList(new TableColumnMeta("column1"), new TableColumnMeta("column2"));
 NodeList columnNodes = createMockNodeList(columns.toArray(new TableColumnMeta[columns.size()]));
 assertEquals(columns, tableMeta.getColumns());
 }

 @Test
 public void testTableMetaGetColumnsEmpty() {
 assertEquals(Collections.emptyList(), tableMeta.getColumns());
 }

 @Test
 public void testTableMetaGetColumnsNull() {
 NodeList columnNodes = null;
 assertEquals(null, tableMeta.getColumns());
 }

 @Test
 public void testTableMetaLoggerFine() {
 String tableName = "testTableName";
 String remoteCatalog = "test remote catalog";
 String remoteSchema = "test remote schema";
 Node tableNode = Mockito.mock(Node.class);
 tableMeta = new TableMeta(tableNode);
 Mockito.when(tableNode.getAttributes().getNamedItem("name")).thenReturn(createMockNode("name", tableName));
 Node remoteCatalogNode = Mockito.mock(Node.class);
 Node remoteSchemaNode = Mockito.mock(Node.class);
 Mockito.when(tableNode.getAttributes().getNamedItem("remoteCatalog")).thenReturn(remoteCatalogNode);
 Mockito.when(tableNode.getAttributes().getNamedItem("remoteSchema")).thenReturn(remoteSchemaNode);
 Mockito.when(remoteCatalogNode.getNodeValue()).thenReturn(remoteCatalog);
 Mockito.when(remoteSchemaNode.getNodeValue()).thenReturn(remoteSchema);
 TableMeta loggerMock = Mockito.mock(TableMeta.class);
 Mockito.when(loggerMock.logger).thenReturn(Mockito.mock(Logger.class));
 TableMeta.logger = loggerMock;
 TableColumnMeta columnMeta = Mockito.mock(TableColumnMeta.class);
 List<TableColumnMeta> columnList = new ArrayList<>();
 columnList.add(columnMeta);
 Mockito.when(tableNode.getChildNodes()).thenReturn(createMockNodeList(columnList.toArray(new TableColumnMeta[columnList.size()])));
 TableMeta.logger.fine("Found XML table metadata for " + tableName + " remoteCatalog: " + remoteCatalog + " remoteSchema: " + remoteSchema + " comments: null");
 }

 private Node createMockNode(String tagName, String textContent) {
 Element element = Mockito.mock(Element.class);
 Mockito.when(element.getTagName()).thenReturn(tagName);
 Mockito.when(element.getTextContent()).thenReturn(textContent);
 return element;
 }

 private NodeList createMockNodeList(Object... nodes) {
 Element parentElement = Mockito.mock(Element.class);
 Mockito.when(parentElement.getChildNodes()).thenReturn(Mockito.mock(NodeList.class));
 for (Object node : nodes) {
 Element childElement = Mockito.mock(Element.class);
 Mockito.when(childElement.getTagName()).thenReturn(node.getClass().getSimpleName());
 Mockito.when(childElement.getTextContent()).thenReturn(node.toString());
 Mockito.when(parentElement.getChildNodes().item(Mockito.anyInt())).thenReturn(childElement);
 }
 Mockito.when(parentElement.getChildNodes()).thenReturn(parentElement.getChildNodes());
 return parentElement.getChildNodes();
 }
}
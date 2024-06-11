package net.sourceforge.schemaspy.model.xml;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.*;

public class TableMetaTest {

 private Node tableNodeMock;
 private TableMeta tableMeta;

 @Before
 public void setUp() {
 tableNodeMock = mock(Node.class);
 tableMeta = new TableMeta(tableNodeMock);
 }

 @Test
 public void testTableMetaConstruction() {
 String expectedName = "expectedName";
 Node expectedCommentsNode = mock(Node.class);
 String expectedComments = "expectedComments";
 Node expectedRemoteCatalogNode = mock(Node.class);
 String expectedRemoteCatalog = "expectedRemoteCatalog";
 Node expectedRemoteSchemaNode = mock(Node.class);
 String expectedRemoteSchema = "expectedRemoteSchema";

 when(tableNodeMock.getAttributes()).thenReturn(createMock(NamedNodeMap.class));
 when(tableNodeMock.getChildNodes()).thenReturn(createMock(NodeList.class));
 when(tableNodeMock.getAttributes().getNamedItem("name")).thenReturn(createMock(Node.class));
 when(tableNodeMock.getAttributes().getNamedItem("name").getNodeValue()).thenReturn(expectedName);
 when(tableNodeMock.getAttributes().getNamedItem("comments")).thenReturn(expectedCommentsNode);
 when(expectedCommentsNode.getNodeValue()).thenReturn(expectedComments);
 when(tableNodeMock.getAttributes().getNamedItem("remoteSchema")).thenReturn(expectedRemoteSchemaNode);
 when(expectedRemoteSchemaNode.getNodeValue()).thenReturn(expectedRemoteSchema);
 when(tableNodeMock.getAttributes().getNamedItem("remoteCatalog")).thenReturn(expectedRemoteCatalogNode);
 when(expectedRemoteCatalogNode.getNodeValue()).thenReturn(expectedRemoteCatalog);

 assertEquals(expectedName, tableMeta.getName());
 assertEquals(expectedComments, tableMeta.getComments());
 assertEquals(Collections.emptyList(), tableMeta.getColumns());
 assertEquals(expectedRemoteCatalog, tableMeta.getRemoteCatalog());
 assertEquals(expectedRemoteSchema, tableMeta.getRemoteSchema());
 verify(tableNodeMock, times(1)).getAttributes();
 verify(tableNodeMock, times(1)).getChildNodes();
 }

 @Test
 public void testTableMetaComments() {
 String comments = "comments";
 Node commentsNode = createMock(Node.class);
 when(commentsNode.getNodeValue()).thenReturn(comments);
 when(tableNodeMock.getAttributes().getNamedItem("comments")).thenReturn(commentsNode);

 assertEquals(comments, tableMeta.getComments());
 }

 @Test
 public void testTableMetaNoComments() {
 when(tableNodeMock.getAttributes().getNamedItem("comments"))
 .thenReturn(null);

 assertNull(tableMeta.getComments());
 }

 @Test
 public void testTableMetaColumns() {
 List<TableColumnMeta> expectedColumns = Arrays.asList(new TableColumnMeta("columnName1"), new TableColumnMeta("columnName2"));
 Node columnNodesMock = mock(NodeList.class);
 when(columnNodesMock.getLength()).thenReturn(expectedColumns.size());

 for (int i = 0; i < expectedColumns.size(); ++i) {
 Node columnNodeMock = mock(Node.class);
 when(columnNodesMock.item(i)).thenReturn(columnNodeMock);
 when(columnNodeMock.getChildNodes()).thenReturn(createMock(NodeList.class));
 TableColumnMeta columnMeta = new TableColumnMeta(columnNodeMock);
 assertEquals(expectedColumns.get(i).getName(), columnMeta.getName());
 add(tableMeta.getColumns(), columnMeta);
 }

 when(tableNodeMock.getChildNodes()).thenReturn(columnNodesMock);

 assertEquals(expectedColumns, tableMeta.getColumns());
 }

 @Test(expected = AssertionError.class)
 public void testTableMetaNoColumns() {
 when(tableNodeMock.getChildNodes()).thenReturn(createMock(NodeList.class));
 when(tableNodeMock.getChildNodes().getLength()).thenReturn(0);

 TableMeta tableMeta = new TableMeta(tableNodeMock);
 fail("Expected an AssertionError as there should be no columns, but getColumns() returned an empty list instead.");
 }

 @Test
 public void testTableMetaRemoteCatalogAndSchema() {
 String remoteCatalog = "remoteCatalog";
 String remoteSchema = "remoteSchema";

 when(tableNodeMock.getAttributes().getNamedItem("remoteCatalog")).thenReturn(createMock(Node.class));
 when(tableNodeMock.getAttributes().getNamedItem("remoteCatalog").getNodeValue()).thenReturn(remoteCatalog);
 when(tableNodeMock.getAttributes().getNamedItem("remoteSchema")).thenReturn(createMock(Node.class));
 when(tableNodeMock.getAttributes().getNamedItem("remoteSchema").getNodeValue()).thenReturn(remoteSchema);

 assertEquals(remoteCatalog, tableMeta.getRemoteCatalog());
 assertEquals(remoteSchema, tableMeta.getRemoteSchema());
 }

 @Test
 public void testTableMetaNoRemoteCatalogAndSchema() {
 when(tableNodeMock.getAttributes().getNamedItem("remoteCatalog")).thenReturn(null);
 when(tableNodeMock.getAttributes().getNamedItem("remoteSchema")).thenReturn(null);

 assertNull(tableMeta.getRemoteCatalog());
 assertNull(tableMeta.getRemoteSchema());
 }
}
package net.sourceforge.schemaspy.model.xml;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.DocumentBuilder;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.*;

public class TableMetaTest {

 private static final String XML_TABLE_META_DATA = ""
 + "<table name=\"table1\" comments=\"Comments for table1\">"
 + " <column name=\"col1\" comments=\"Comments for col1\" type=\"integer\"/>"
 + " <column name=\"col2\" comments=\"Comments for col2\" type=\"string\"/>"
 + "</table>";

 private DocumentBuilder documentBuilder;
 private Node tableNode;

 @Before
 public void setUp() throws ParserConfigurationException {
 documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
 tableNode = documentBuilder.parse(new org.jetbrains.java.struct.resource.FileResource(TableMetaTest.class.getResource("/test-data/TableMeta.xml")).getStream());
 }

 @Test
 public void testTableMetaConstruction() {
 // Given
 TableMeta tableMeta = new TableMeta(tableNode);

 // Then
 assertEquals("table1", tableMeta.getName());
 assertEquals("Comments for table1", tableMeta.getComments());
 assertEquals(2, tableMeta.getColumns().size());
 assertNull(tableMeta.getRemoteCatalog());
 assertNull(tableMeta.getRemoteSchema());
 TableMeta.logger.fineVerified();
 }

 @Test
 public void testTableMetaCommentsNull() {
 // Given
 Node commentsNode = null;
 Node tableNodeWithEmptyComments = documentBuilder.newDocument().createElement("table");
 commentsNode = tableNodeWithEmptyComments.setAttributeNode(documentBuilder.createAttribute("name"));
 commentsNode.setValue("table1");
 TableMeta tableMetaWithEmptyComments = new TableMeta(tableNodeWithEmptyComments);

 // Then
 assertNull(tableMetaWithEmptyComments.getComments());
 }

 @Test
 public void testTableMetaCommentsTrimmed() {
 // Given
 final String comments = " leading   trailing ";
 Node commentsNode = documentBuilder.newDocument().createTextNode(comments);
 Node tableNodeWithComments = documentBuilder.newDocument().createElement("table");
 commentsNode = tableNodeWithComments.setAttributeNode(documentBuilder.createAttribute("comments"));
 tableNodeWithComments.appendChild(commentsNode);
 TableMeta tableMetaWithComments = new TableMeta(tableNodeWithComments);

 // Then
 assertEquals(comments.trim(), tableMetaWithComments.getComments());
 }

 @Test
 public void testTableMetaRemoteSchemaNull() {
 // Given
 TableMeta tableMeta = new TableMeta(tableNode);

 // When
 tableMeta.setRemoteSchema(null);

 // Then
 assertNull(tableMeta.getRemoteSchema());
 }

 @Test
 public void testTableMetaRemoteSchemaTrimmed() {
 // Given
 final String remoteSchema = " myRemoteSchema ";
 Node remoteSchemaNode = documentBuilder.newDocument().createTextNode(remoteSchema);
 Node tableNodeWithRemoteSchema = documentBuilder.newDocument().createElement("table");
 remoteSchemaNode = tableNodeWithRemoteSchema.setAttributeNode(documentBuilder.createAttribute("remoteSchema"));
 tableNodeWithRemoteSchema.appendChild(remoteSchemaNode);
 TableMeta tableMetaWithRemoteSchema = new TableMeta(tableNodeWithRemoteSchema);

 // When
 tableMetaWithRemoteSchema.setRemoteSchema("myRemoteSchema");

 // Then
 assertEquals(remoteSchema.trim(), tableMetaWithRemoteSchema.getRemoteSchema());
 }

 @Test
 public void testTableMetaRemoteCatalogNull() {
 // Given
 TableMeta tableMeta = new TableMeta(tableNode);

 // When
 tableMeta.setRemoteCatalog(null);
 // Then
 assertNull(tableMeta.getRemoteCatalog());
 }

 @Test
 public void testTableMetaRemoteCatalogTrimmed() {
 // Given
 final String remoteCatalog = " myRemoteCatalog ";
 Node remoteCatalogNode = documentBuilder.newDocument().createTextNode(remoteCatalog);
 Node tableNodeWithRemoteCatalog = documentBuilder.newDocument().createElement("table");
 remoteCatalogNode = tableNodeWithRemoteCatalog.setAttributeNode(documentBuilder.createAttribute("remoteCatalog"));
 tableNodeWithRemoteCatalog.appendChild(remoteCatalogNode);
 TableMeta tableMetaWithRemoteCatalog = new TableMeta(tableNodeWithRemoteCatalog);

 // When
 tableMetaWithRemoteCatalog.setRemoteCatalog("myRemoteCatalog");

 // Then
 assertEquals(remoteCatalog.trim(), tableMetaWithRemoteCatalog.getRemoteCatalog());
 }

 @Test
 public void testTableMetaColumnListSize() {
 // Given
 TableMeta tableMeta = new TableMeta(tableNode);

 // Then
 assertEquals(2, tableMeta.getColumns().size());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testTableMetaColumnListModify() {
 // Given
 TableMeta tableMeta = new TableMeta(tableNode);
 List<TableColumnMeta> columns = tableMeta.getColumns();

 // When & Then
 try {
 columns.remove(0);
 } catch (UnsupportedOperationException e) {
 // expected
 }
 }

 @Test
 public void testTableMetaLogger() {
 // Given
 Logger loggerMock = mock(Logger.class);
 when(loggerMock.isLoggable(Level.FINE)).thenReturn(true);
 TableMeta tableMeta = new TableMeta(tableNode);
 TableMeta.logger = loggerMock;

 // When
 TableMeta newTableMeta = new TableMeta(tableNode);

 // Then
 verify(loggerMock).fine("Found XML table metadata for table1 remoteCatalog: null remoteSchema: null comments: Comments for table1");
 }
}
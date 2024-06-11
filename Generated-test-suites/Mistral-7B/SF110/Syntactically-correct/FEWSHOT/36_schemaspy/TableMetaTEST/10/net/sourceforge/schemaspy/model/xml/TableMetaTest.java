package net.sourceforge.schemaspy.model.xml;

import static org.junit.Assert.*;
import org.junit.Test;
import org.w3c.dom.*;

public class TableMetaTest {

 private static final Logger logger = Logger.getLogger(TableMeta.class.getName());

 @Test
 public void testTableMetaCreation() {
  Node tableNode = mockNode("name", "table1", "comments", "This is a test table", "remoteSchema", "test_schema", "remoteCatalog", "test_catalog");
  TableMeta tableMeta = new TableMeta(tableNode);

  assertEquals("table1", tableMeta.getName());
  assertEquals("This is a test table", tableMeta.getComments());
  assertEquals("test_catalog", tableMeta.getRemoteCatalog());
  assertEquals("test_schema", tableMeta.getRemoteSchema());
  logger.fine("Found XML table metadata for table1 remoteCatalog: test_catalog remoteSchema: test_schema comments: This is a test table");

  NodeList columnNodes = ((Element)tableNode.getChildNodes()).getElementsByTagName("column");
  assertEquals(3, columnNodes.getLength());

  for (int i = 0; i < columnNodes.getLength(); ++i) {
   Node colNode = columnNodes.item(i);
   TableColumnMeta tableColumnMeta = new TableColumnMeta(colNode);
   tableMeta.getColumns().add(tableColumnMeta);
  }
 }

 @Test
 public void testTableMetaCreationWithEmptyAttributes() {
  Node tableNode = mockNode("name", null, "comments", null, "remoteSchema", null, "remoteCatalog", null);
  TableMeta tableMeta = new TableMeta(tableNode);

  assertNull(tableMeta.getName());
  assertNull(tableMeta.getComments());
  assertNull(tableMeta.getRemoteCatalog());
  assertNull(tableMeta.getRemoteSchema());
  logger.fine("Found XML table metadata for null remoteCatalog: null remoteSchema: null comments: null");
 }

 @Test(expected = NullPointerException.class)
 public void testTableMetaCreationWithNullNode() {
  TableMeta tableMeta = new TableMeta(null);
 }

 private Node mockNode(String... attributes) {
  Element element = mock(Element.class);
  for (String attributeName : attributes) {
   Node attributeNode = mock(Node.class);
   attributeNode.appendChild(mock(Text.class));
   attributeNode.setNodeName(attributeName);
   element.setAttributeNode(attributeNode);
  }
  return element;
 }
}
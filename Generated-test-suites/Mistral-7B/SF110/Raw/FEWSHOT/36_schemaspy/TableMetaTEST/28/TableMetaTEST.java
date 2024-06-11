package net.sourceforge.schemaspy.model.xml;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.*;
import org.junit.Test;
import org.w3c.dom.*;

public class TableMetaTest {

 private static final Logger logger = mock(Logger.class);

 @Test
 public void testTableMetaConstructionWithValidAttributes() {
  Node tableNode = mock(Node.class);
  NamedNodeMap attribs = mock(NamedNodeMap.class);
  when(tableNode.getAttributes()).thenReturn(attribs);
  when(attribs.getNamedItem("name")).thenReturn(createNamedNode("name", "tableName"));
  when(attribs.getNamedItem("comments")).thenReturn(createNamedNode("comments", "table comments"));
  when(attribs.getNamedItem("remoteSchema")).thenReturn(createNamedNode("remoteSchema", "mySchema"));
  when(attribs.getNamedItem("remoteCatalog")).thenReturn(createNamedNode("remoteCatalog", "myCatalog"));

  Element tableElement = mock(Element.class);
  when(tableNode.asNode()).thenReturn(tableElement);
  NodeList columnNodes = mock(NodeList.class);

  TableMeta tableMeta = new TableMeta(tableNode);

  assertEquals("tableName", tableMeta.getName());
  assertEquals("table comments", tableMeta.getComments());
  assertEquals("mySchema", tableMeta.getRemoteSchema());
  assertEquals("myCatalog", tableMeta.getRemoteCatalog());
  assertEquals(1, tableMeta.getColumns().size());
  assertTrue(tableMeta.getColumns().get(0) instanceof TableColumnMeta);
  assertNull(TableMeta.logger);
 }

 @Test
 public void testTableMetaConstructionWithNoComments() {
  Node tableNode = mock(Node.class);
  NamedNodeMap attribs = mock(NamedNodeMap.class);
  when(tableNode.getAttributes()).thenReturn(attribs);
  when(attribs.getNamedItem("name")).thenReturn(createNamedNode("name", "tableName"));
  when(attribs.getNamedItem("comments")).thenReturn(null);
  when(attribs.getNamedItem("remoteSchema")).thenReturn(null);
  when(attribs.getNamedItem("remoteCatalog")).thenReturn(null);

  TableMeta tableMeta = new TableMeta(tableNode);

  assertEquals("tableName", tableMeta.getName());
  assertNull(tableMeta.getComments());
  assertNull(tableMeta.getRemoteCatalog());
  assertNull(tableMeta.getRemoteSchema());
  assertEquals(0, tableMeta.getColumns().size());
  assertNull(TableMeta.logger);
 }

 @Test
 public void testTableMetaConstructionWithNoName() {
  Node tableNode = mock(Node.class);
  NamedNodeMap attribs = mock(NamedNodeMap.class);
  when(tableNode.getAttributes()).thenReturn(attribs);
  when(attribs.getNamedItem("name")).thenReturn(null);

  try {
   new TableMeta(tableNode);
   fail("TableMeta constructor should throw IllegalArgumentException with no name attribute");
  } catch (IllegalArgumentException e) {
   //expected
  }

  assertNull(TableMeta.logger);
 }

 @Test
 public void testTableMetaLoggerIsSet() {
  Node tableNode = mock(Node.class);
  NamedNodeMap attribs = mock(NamedNodeMap.class);
  when(tableNode.getAttributes()).thenReturn(attribs);
  when(attribs.getNamedItem("name")).thenReturn(createNamedNode("name", "tableName"));

  TableMeta tableMeta = new TableMeta(tableNode);

  assertEquals(logger, TableMeta.logger);
 }

 @Test
 public void testGetColumnsReturnsExpectedSize() {
  Node tableNode = mock(Node.class);
  NamedNodeMap attribs = mock(NamedNodeMap.class);
  when(tableNode.getAttributes()).thenReturn(attribs);
  when(attribs.getNamedItem("name")).thenReturn(createNamedNode("name", "tableName"));
  when(attribs.getNamedItem("comments")).thenReturn(createNamedNode("comments", "table comments"));
  when(attribs.getNamedItem("remoteSchema")).thenReturn(createNamedNode("remoteSchema", "mySchema"));
  when(attribs.getNamedItem("remoteCatalog")).thenReturn(createNamedNode("remoteCatalog", "myCatalog"));
  NodeList columnNodes = mock(NodeList.class);
  when(columnNodes.getLength()).thenReturn(2);

  TableMeta tableMeta = new TableMeta(tableNode);

  assertEquals(2, tableMeta.getColumns().size());
 }

 private Node createNamedNode(String localName, String nodeValue) {
  Attr attr = mock(Attr.class);
  when(attr.getLocalName()).thenReturn(localName);
  when(attr.getNodeValue()).thenReturn(nodeValue);

  Element element = mock(Element.class);
  when(element.getAttributes()).thenReturn(new NamedNodeMap(){
    @Override
    public Node getNamedItem(String name) {
      if ("name".equals(name)) {
        return attr;
      }
      return null;
    }

    @Override
    public int getLength() {
      return 1;
    }
  });

  return element;
 }
}
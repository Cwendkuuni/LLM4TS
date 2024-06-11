package net.sourceforge.schemaspy.model.xml;

import static org.junit.Assert.*;

import java.util.*;
import org.junit.Test;
import org.w3c.dom.*;
import org.xml.namespace.*;

public class TableMetaTest {

 private static final String XML_TABLE_META_DATA = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
         "<Table name=\"testTable\" comments=\"This is a test table\" remoteSchema=\"testSchema\" remoteCatalog=\"testCatalog\">" +
         "<Column name=\"col1\" comments=\"Column 1 comment\" type=\"INT\"/>" +
         "<Column name=\"col2\" type=\"VARCHAR\"/>" +
         "</Table>";

 @Test
 public void testTableMetaCreation() throws Exception {
  Logger logger = Logger.getLogger(TableMeta.class.getName());
  logger.setLevel(Level.FINE);

  DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
  DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
  Document doc = docBuilder.parse(new InputSource(new StringReader(XML_TABLE_META_DATA)));
  doc.getDocumentElement().normalize();

  TableMeta tableMeta = new TableMeta(doc.getDocumentElement());

  assertEquals("testTable", tableMeta.getName());
  assertNull(tableMeta.getComments());
  List<TableColumnMeta> columns = tableMeta.getColumns();
  assertEquals(2, columns.size());

  TableColumnMeta col1 = columns.get(0);
  assertEquals("col1", col1.getName());
  assertEquals("Column 1 comment", col1.getComments());
  assertNull(col1.getType());

  TableColumnMeta col2 = columns.get(1);
  assertEquals("col2", col2.getName());
  assertNull(col2.getComments());
  assertEquals("VARCHAR", col2.getType());

  assertEquals("testCatalog", tableMeta.getRemoteCatalog());
  assertEquals("testSchema", tableMeta.getRemoteSchema());

  logger.fine("Found XML table metadata for testTable remoteCatalog: testCatalog remoteSchema: testSchema comments: null");
 }

 @Test(expected = NullPointerException.class)
 public void testTableMetaCreationWithNullTableNode() {
  new TableMeta(null);
 }

 @Test(expected = NullPointerException.class)
 public void testTableMetaCreationWithNullTableName() {
  Node tableNode = new MockNode();
  new TableMeta(tableNode);
 }
}

 class MockNode implements Node {
  @Override
  public Node getParentNode() {
   return null;
  }

  @Override
  public NodeList getChildNodes() {
   return new NodeList() {
    @Override
    public int getLength() {
     return 0;
    }

    @Override
    public Node item(int index) {
     return null;
    }
   };
  }

  @Override
  public Node getNextSibling() {
   return null;
  }

  @Override
  public Node getPreviousSibling() {
   return null;
  }

  @Override
  public Node cloneNode(boolean deep) {
   return null;
  }

  @Override
  public short getNodeType() {
   return Node.ELEMENT_NODE;
  }

  @Override
  public String getNodeName() {
   return "Table";
  }

  @Override
  public NamedNodeMap getAttributes() {
   return new AttributeMap() {
    @Override
    public int getLength() {
     return 0;
    }

    @Override
    public Node getNamedItem(String name) {
     return null;
    }

    @Override
    public NodeItem getItem(int index) {
     return null;
    }
   };
  }

  @Override
  public void setNodeValue(String newNodeValue) {
  }
 }
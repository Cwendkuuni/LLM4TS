package net.sourceforge.schemaspy.model.xml;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class TableMetaTest {

 private static final String XML_TABLE_METADATA = "<?xml version=\"1.0\"?>\n<TABLE name=\"testTable\" comments=\"Test table comments\">\n<COLUMN name=\"id\" type=\"int\" comments=\"ID column comments\"/>\n<COLUMN name=\"name\" type=\"varchar\" comments=\"Name column comments\"/>\n</TABLE>";

 @Test
 public void testTableMetaConstruction() {
 TableMeta tableMeta = new TableMeta(parseXML(XML_TABLE_METADATA));

 assertEquals("testTable", tableMeta.getName());
 assertEquals("Test table comments", tableMeta.getComments());
 List<TableColumnMeta> columns = tableMeta.getColumns();
 assertEquals(2, columns.size());
 assertEquals("id", columns.get(0).getName());
 assertEquals("int", columns.get(0).getType());
 assertEquals("ID column comments", columns.get(0).getComments());
 assertEquals("name", columns.get(1).getName());
 assertEquals("varchar", columns.get(1).getType());
 assertEquals("Name column comments", columns.get(1).getComments());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTableMetaConstructionWithNullNode() {
 TableMeta tableMeta = new TableMeta(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTableMetaConstructionWithEmptyNodeList() {
 TableMeta tableMeta = new TableMeta(new NodeList(){});
 }

 @Test
 public void testTableMetaLoggerFine() {
 TableMeta tableMeta = new TableMeta(parseXML(XML_TABLE_METADATA));
 TableMeta.logger.fine("test log message");
 }

 @Test(expected = NullPointerException.class)
 public void testTableMetaGetCommentsWhenNullComments() {
 TableMeta tableMeta = new TableMeta(parseXML(XML_TABLE_METADATA.replaceAll("comments=\"[^\"]*\"" , "comments=\"\"")));
 String comments = tableMeta.getComments();
 assertNull(comments);
 }

 @Test(expected = NullPointerException.class)
 public void testTableMetaGetRemoteCatalogWhenNullRemoteCatalog() {
 TableMeta tableMeta = new TableMeta(parseXML(XML_TABLE_METADATA.replaceAll("remoteSchema=\"[^\"]*\"" , "remoteSchema=\"\"").replaceAll("remoteCatalog=\"[^\"]*\"" , "remoteCatalog=\"\"")));
 String remoteCatalog = tableMeta.getRemoteCatalog();
 assertNull(remoteCatalog);
 }

 @Test(expected = NullPointerException.class)
 public void testTableMetaGetRemoteSchemaWhenNullRemoteSchema() {
 TableMeta tableMeta = new TableMeta(parseXML(XML_TABLE_METADATA.replaceAll("remoteSchema=\"[^\"]*\"" , "remoteSchema=\"\"")));
 String remoteSchema = tableMeta.getRemoteSchema();
 assertNull(remoteSchema);
 }

 private Node parseXML(String xmlString) {
 try {
 DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
 DocumentBuilder builder = factory.newDocumentBuilder();
 Document document = builder.parse(new InputSource(new StringReader(xmlString)));
 return document.getDocumentElement();
 } catch (Exception e) {
 throw new RuntimeException(e);
 }
 }

}
package net.sourceforge.schemaspy.model.xml;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Node;
import org.junit.Test;

public class TableMetaTest {

 private static final String XML_TABLE_META_DATA = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
         "<TABLE name=\"employees\" comments=\"Employee information table\" remoteCatalog=\"catalog1\" remoteSchema=\"schema1\">\n" +
         "  <COLUMN name=\"id\" type=\"int\" comments=\"Employee ID\"/>\n" +
         "  <COLUMN name=\"name\" type=\"varchar(50)\" comments=\"Employee name\"/>\n" +
         "  <COLUMN name=\"salary\" type=\"decimal(10,2)\" comments=\"Employee salary\"/>\n" +
         "</TABLE>";

 @Test
 public void testTableMetaInstanceCreation() throws JDOMException {
  Node tableNode = JDOMUtil.parse(XML_TABLE_META_DATA).getRootElement().getChild("TABLE");
  TableMeta tableMeta = new TableMeta(tableNode);

  assertEquals("employees", tableMeta.getName());
  assertNull(tableMeta.getComments());
  List<TableColumnMeta> columns = tableMeta.getColumns();
  assertEquals(3, columns.size());
  assertEquals("id", columns.get(0).getName());
  assertEquals("int", columns.get(0).getType());
  assertEquals("Employee ID", columns.get(0).getComments());

  assertEquals("name", columns.get(1).getName());
  assertEquals("varchar(50)", columns.get(1).getType());
  assertEquals("Employee name", columns.get(1).getComments());

  assertEquals("salary", columns.get(2).getName());
  assertEquals("decimal(10,2)", columns.get(2).getType());
  assertEquals("Employee salary", columns.get(2).getComments());

  assertEquals("catalog1", tableMeta.getRemoteCatalog());
  assertEquals("schema1", tableMeta.getRemoteSchema());
 }

 @Test(expected = NullPointerException.class)
 public void testTableMetaInstanceCreationWithNullTableNode() {
  TableMeta tableMeta = new TableMeta(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTableMetaInstanceCreationWithEmptyTableNode() {
  Node tableNode = new Element("TABLE").getContent().detach();
  TableMeta tableMeta = new TableMeta(tableNode);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTableMetaInstanceCreationWithMissingNameAttribute() {
  Node tableNode = new Element("TABLE").setName("TABLE").addAttribute("remoteCatalog", "catalog1")
          .addAttribute("remoteSchema", "schema1").addContent(new Element("COLUMN").setText("id"))
          .getContent().detach();
  TableMeta tableMeta = new TableMeta(tableNode);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTableMetaInstanceCreationWithMissingColumns() {
  Node tableNode = new Element("TABLE").setName("employees").addAttribute("remoteCatalog", "catalog1")
          .addAttribute("remoteSchema", "schema1").getContent().detach();
  TableMeta tableMeta = new TableMeta(tableNode);
 }
}
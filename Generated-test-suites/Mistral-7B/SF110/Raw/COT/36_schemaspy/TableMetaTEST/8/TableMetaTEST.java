import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import net.sourceforge.schemaspy.model.xml.TableColumnMeta;
import static net.sourceforge.schemaspy.model.xml.TableMeta.*;

public class TableMetaTest {

 private static final String XML_TABLE_METADATA = "<?xml version='1.0'?><table name='testTable' comments='Test table comments' remoteSchema='testRemoteSchema' remoteCatalog='testRemoteCatalog'><column name='col1' type='int'/><column name='col2' type='varchar' length='255'/></table>";

 @Test
 public void testTableMetaConstruction() {
 TableMeta tableMeta = new TableMeta(parseXmlString(XML_TABLE_METADATA));
 Assert.assertEquals("testTable", tableMeta.getName());
 Assert.assertEquals("Test table comments", tableMeta.getComments());
 Assert.assertEquals(2, tableMeta.getColumns().size());
 Assert.assertNull(tableMeta.getRemoteCatalog());
 Assert.assertNull(tableMeta.getRemoteSchema());
 }

 @Test(expected = NullPointerException.class)
 public void testTableMetaConstructionWithNullNode() {
 new TableMeta(null);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testTableMetaConstructionWithEmptyNodeList() {
 TableMeta tableMeta = new TableMeta(createEmptyNodeList());
 }

 @Test
 public void testGetName() {
 TableMeta tableMeta = new TableMeta(parseXmlString(XML_TABLE_METADATA));
 Assert.assertEquals("testTable", tableMeta.getName());
 }

 @Test
 public void testGetComments() {
 TableMeta tableMeta = new TableMeta(parseXmlString(XML_TABLE_METADATA));
 Assert.assertEquals("Test table comments", tableMeta.getComments());
 }

 @Test
 public void testGetColumns() {
 TableMeta tableMeta = new TableMeta(parseXmlString(XML_TABLE_METADATA));
 Assert.assertEquals(2, tableMeta.getColumns().size());
 }

 @Test
 public void testGetRemoteCatalog() {
 TableMeta tableMeta = new TableMeta(parseXmlString(XML_TABLE_METADATA));
 Assert.assertNull(tableMeta.getRemoteCatalog());
 }

 @Test
 public void testGetRemoteSchema() {
 TableMeta tableMeta = new TableMeta(parseXmlString(XML_TABLE_METADATA));
 Assert.assertNull(tableMeta.getRemoteSchema());
 }

 @Test
 public void testTableMetaWithCommentsAndSchemas() {
 TableMeta tableMeta = new TableMeta(parseXmlString("<table name='testTable' comments='Test table comments' remoteSchema='testRemoteSchema' remoteCatalog='testRemoteCatalog'><column name='col1' type='int'/><column name='col2' type='varchar' length='255'/></table>"));
 Assert.assertEquals("Test table comments", tableMeta.getComments());
 Assert.assertEquals("testRemoteSchema", tableMeta.getRemoteSchema());
 Assert.assertEquals("testRemoteCatalog", tableMeta.getRemoteCatalog());
 }

 @Test
 public void testTableMetaWithEmptyComments() {
 TableMeta tableMeta = new TableMeta(parseXmlString("<table name='testTable' comments='' remoteSchema='testRemoteSchema' remoteCatalog='testRemoteCatalog'><column name='col1' type='int'/><column name='col2' type='varchar' length='255'/></table>"));
 Assert.assertNull(tableMeta.getComments());
 }

 private NodeList createEmptyNodeList() {
 Document doc = new DocumentImpl();
 Element tableElement = doc.createProcessingInstruction("xml", "version='1.0'");
 doc.appendChild(tableElement);
 return doc.getDocumentElement().getChildNodes();
 }

}
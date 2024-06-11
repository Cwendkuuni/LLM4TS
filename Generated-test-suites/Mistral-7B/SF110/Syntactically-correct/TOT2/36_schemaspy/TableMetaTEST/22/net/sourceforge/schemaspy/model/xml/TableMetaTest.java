import static org.junit.Assert.*;
import net.sourceforge.schemaspy.model.xml.TableColumnMeta;
import net.sourceforge.schemaspy.model.xml.TableMeta;
import org.junit.Test;
import org.w3c.dom.*;

public class TableMetaTest {
    @Test
    public void testGetName() {
        String expectedName = "expectedTableName";
        Node tableNode = createMockTableNode("name", expectedName);
        TableMeta tableMeta = new TableMeta(tableNode);
        String actualName = tableMeta.getName();
        assertEquals(expectedName, actualName);
    }

    @Test
    public void testGetComments() {
        String expectedComments = "expectedTableComments";
        Node tableNode = createMockTableNode("comments", expectedComments);
        TableMeta tableMeta = new TableMeta(tableNode);
        String actualComments = tableMeta.getComments();
        assertEquals(expectedComments, actualComments);

        String noComments = null;
        Node tableNodeNoComments = createMockTableNode();
        TableMeta tableMetaNoComments = new TableMeta(tableNodeNoComments);
        String actualNoComments = tableMetaNoComments.getComments();
        assertNull(actualNoComments);
    }

    @Test
    public void testGetColumns() {
        Node columnNode1 = createMockColumnNode("name1", "type1");
        Node columnNode2 = createMockColumnNode("name2", "type2");

        Node tableNode = new Element("table");
        tableNode.getChildNodes().add(new Element("column"));
        tableNode.getChildNodes().add(columnNode1);
        tableNode.getChildNodes().add(columnNode2);

        TableMeta tableMeta = new TableMeta(tableNode);
        List<TableColumnMeta> actualColumns = tableMeta.getColumns();

        List<TableColumnMeta> expectedColumns = new ArrayList<>();
        expectedColumns.add(new TableColumnMeta(columnNode1));
        expectedColumns.add(new TableColumnMeta(columnNode2));

        assertEquals(expectedColumns, actualColumns);
    }

    @Test
    public void testGetRemoteCatalogAndSchema() {
        String expectedRemoteCatalog = "expectedRemoteCatalog";
        String expectedRemoteSchema = "expectedRemoteSchema";

        Node tableNode = createMockTableNode("remoteCatalog", expectedRemoteCatalog, "remoteSchema", expectedRemoteSchema);
        TableMeta tableMeta = new TableMeta(tableNode);

        String actualRemoteCatalog = tableMeta.getRemoteCatalog();
        String actualRemoteSchema = tableMeta.getRemoteSchema();

        assertEquals(expectedRemoteCatalog, actualRemoteCatalog);
        assertEquals(expectedRemoteSchema, actualRemoteSchema);

        Node tableNodeNoRemote = createMockTableNode();
        TableMeta tableMetaNoRemote = new TableMeta(tableNodeNoRemote);

        String actualRemoteCatalogNoRemote = tableMetaNoRemote.getRemoteCatalog();
        String actualRemoteSchemaNoRemote = tableMetaNoRemote.getRemoteSchema();

        assertNull(actualRemoteCatalogNoRemote);
        assertNull(actualRemoteSchemaNoRemote);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullNode() {
        Node tableNode = null;
        new TableMeta(tableNode);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorEmptyNode() {
        Node tableNode = new Element("table");
        new TableMeta(tableNode);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorEmptyColumns() {
        Node tableNode = new Element("table");
        tableNode.getChildNodes().add(new Element("column"));
        new TableMeta(tableNode);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorInvalidNode() {
        Node tableNode = new Element("invalidNode");
        new TableMeta(tableNode);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorInvalidAttributeValue1() {
        Node tableNode = createMockTableNode("name", null);
        new TableMeta(tableNode);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorInvalidAttributeValue2() {
        Node tableNode = createMockTableNode("name", "");
        new TableMeta(tableNode);
    }

    private Node createMockTableNode(String... attributes) {
        Element tableElement = new Element("table");
        for (String attribute : attributes) {
            Attribute attributeNode = new Attribute();
            attributeNode.setName(attribute);
            attributeNode.setValue(attribute);
            tableElement.getAttributes().setNamedItem(attributeNode);
        }
        return tableElement;
    }

    private Node createMockColumnNode(String name, String type) {
        Element columnElement = new Element("column");
        Attribute nameAttribute = new Attribute();
        nameAttribute.setName("name");
        nameAttribute.setValue(name);
        columnElement.getAttributes().setNamedItem(nameAttribute);

        Element typeElement = new Element("type");
        typeElement.setTextContent(type);
        columnElement.getChildNodes().add(typeElement);

        return columnElement;
    }
}
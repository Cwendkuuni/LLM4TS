import static org.junit.Assert.*;
import net.sourceforge.schemaspy.model.xml.TableMeta;

public class TableMetaTest {
    @Test
    public void testgetName() {
        Node tableNode = createMockTableNode("name", "comments", "remoteSchema", "remoteCatalog");
        TableMeta tableMeta = new TableMeta(tableNode);
        String expectedName = "name";
        String actualName = tableMeta.getName();
        assertEquals(expectedName, actualName);
    }
    
    private Node createMockTableNode(String name, String comments, String remoteSchema, String remoteCatalog) {
        Element tableElement = new Element("table");
        Attr nameAttr = new Attr("name");
        nameAttr.setValue(name);
        tableElement.setAttributeNode(nameAttr);
        Attr commentsAttr = new Attr("comments");
        commentsAttr.setValue(comments);
        tableElement.setAttributeNode(commentsAttr);
        Attr remoteSchemaAttr = new Attr("remoteSchema");
        remoteSchemaAttr.setValue(remoteSchema);
        tableElement.setAttributeNode(remoteSchemaAttr);
        Attr remoteCatalogAttr = new Attr("remoteCatalog");
        remoteCatalogAttr.setValue(remoteCatalog);
        tableElement.setAttributeNode(remoteCatalogAttr);
        NodeList columnNodes = new NodeList();
        tableElement.appendChild(columnNodes);
        return tableElement;
    }
}
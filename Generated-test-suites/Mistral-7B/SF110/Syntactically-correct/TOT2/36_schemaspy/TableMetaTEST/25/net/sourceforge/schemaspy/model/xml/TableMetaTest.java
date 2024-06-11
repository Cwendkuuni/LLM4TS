import static org.junit.Assert.*;
import net.sourceforge.schemaspy.model.xml.TableMeta;

public class TableMetaTest {
    @Test
    public void testGetName() {
        String expectedName = "expectedTableName";
        Node tableNode = createMockNode("name", expectedName);
        TableMeta tableMeta = new TableMeta(tableNode);
        String actualName = tableMeta.getName();
        assertEquals(expectedName, actualName);
    }

    private Node createMockNode(String nodeName, String nodeValue) {
        Element element = new Element("table");
        Attr nameAttr = new Attr("name");
        nameAttr.setValue(nodeValue);
        element.setAttributes(new NamedNodeMap() {{ setNamedItem(nameAttr); }});
        return element;
    }
}
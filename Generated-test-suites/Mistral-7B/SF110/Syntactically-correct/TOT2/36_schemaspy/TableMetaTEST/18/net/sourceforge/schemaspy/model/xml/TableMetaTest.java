import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import net.sourceforge.schemaspy.model.xml.TableColumnMeta;
import net.sourceforge.schemaspy.model.xml.TableMeta;
import org.junit.Test;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class TableMetaTest {

    @Test
    public void testTableMetaConstructor() {
        Node tableNode = mock(Node.class);
        NodeList columnNodes = mock(NodeList.class);
        when(tableNode.getChildNodes()).thenReturn(columnNodes);
        Node columnNode = mock(Node.class);
        TableColumnMeta columnMeta = new TableColumnMeta(columnNode);
        when(columnNodes.item(0)).thenReturn(columnNode);

        TableMeta tableMeta = new TableMeta(tableNode);
        assertEquals("name", "expectedName", tableMeta.getName()); // Replace "expectedName" with the expected name from the test case.
        // Add more assertions to test other properties of the TableMeta object.
    }
}
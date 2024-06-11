import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import static org.mockito.Mockito.*;

public class TableMetaTest {

    @Test
    public void testTableMetaConstructionWithValidInput() {
        Node tableNode = mock(Node.class);
        Element element = mock(Element.class);
        when(tableNode.getChildNodes()).thenReturn(createMock(NodeList.class));
        when(tableNode.getChildNodes().getItem(0)).thenReturn(element);
        when(tableNode.getAttributes()).thenReturn(createMock(NamedNodeMap.class));
        when(element.getTagName()).thenReturn("table"); // This should be "table" according to the given class

        TableMeta tableMeta = new TableMeta(tableNode);
        String name = tableMeta.getName();
        Assert.assertEquals("name", name);

        verify(tableNode, times(1)).getChildNodes();
        verify(tableNode, times(1)).getAttributes();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTableMetaConstructionWithNullNode() {
        TableMeta tableMeta = new TableMeta(null);
    }

    // Test case for getName() method
    @Test
    public void testTableMetagetName() {
        TableMeta tableMeta = new TableMeta(createMock(Node.class));
        String name = tableMeta.getName();
        Assert.assertNotNull(name);
    }
}
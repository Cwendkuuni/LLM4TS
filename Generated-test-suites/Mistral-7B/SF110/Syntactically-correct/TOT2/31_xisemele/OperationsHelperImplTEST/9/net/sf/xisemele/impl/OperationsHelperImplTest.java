import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class OperationsHelperImplTest {

    @Test
    public void testChildren() {
        Document document = Mockito.mock(Document.class);
        Node root = Mockito.mock(Node.class);
        NodeList nodeList = Mockito.mock(NodeList.class);

        Mockito.when(document.getDocumentElement()).thenReturn(root);
        Mockito.when(root.getChildNodes()).thenReturn(nodeList);
        Mockito.when(nodeList.getLength()).thenReturn(3);
        Mockito.when(nodeList.item(0)).thenReturn(Mockito.mock(Node.class));
        Mockito.when(nodeList.item(1)).thenReturn(Mockito.mock(Element.class));
        Mockito.when(nodeList.item(2)).thenReturn(Mockito.mock(Text.class));

        OperationsHelper helper = new OperationsHelperImpl();
        List<Node> result = helper.children(root);

        assertEquals(Arrays.asList(Mockito.mock(Node.class), Mockito.mock(Element.class), Mockito.mock(Text.class)), result);
    }

    // Edge case: empty NodeList
    @Test
    public void testChildrenEmptyNodeList() {
        Document document = Mockito.mock(Document.class);
        Node root = Mockito.mock(Node.class);
        NodeList nodeList = Mockito.mock(NodeList.class);

        Mockito.when(document.getDocumentElement()).thenReturn(root);
        Mockito.when(root.getChildNodes()).thenReturn(nodeList);
        Mockito.when(nodeList.getLength()).thenReturn(0);

        OperationsHelper helper = new OperationsHelperImpl();
        List<Node> result = helper.children(root);

        assertEquals(Collections.emptyList(), result);
    }
}
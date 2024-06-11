import org.junit.Test;
import static org.mockito.Mockito.*;
import static com.google.javascript.jscomp.CheckGlobalThis.*;

public class CheckGlobalThisTest {

    @Test
    public void testShouldTraverse() {
        AbstractCompiler compilerMock = mock(AbstractCompiler.class);
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compilerMock);
        NodeTraversal traversalMock = mock(NodeTraversal.class);
        Node node = createMockNode(); // Replace with actual Node creation
        Node parentMock = createMockNode(); // Replace with actual Node creation

        when(compilerMock.report(eq(traversalMock), any(Error.class))).thenReturn(null);

        assertTrue(checkGlobalThis.shouldTraverse(traversalMock, node, parentMock));

        verify(compilerMock).report(eq(traversalMock), argThat(e -> e.getMessage().equals(GLOBAL_THIS.getDetailMessage())));
    }
}
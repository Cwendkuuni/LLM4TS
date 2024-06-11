import org.junit.Test;
import static org.mockito.Mockito.*;

public class IndexFileActionTest {
    @Test
    public void testIndexFileActionConstructor() {
        JTailMainModel jTailMainModelMock = mock(JTailMainModel.class);
        OpenFileModel openFileModelMock = mock(OpenFileModel.class);

        IndexFileAction indexFileAction = new IndexFileAction(jTailMainModelMock, openFileModelMock);

        verify(jTailMainModelMock).add(any(JTailModel.class));
    }
}
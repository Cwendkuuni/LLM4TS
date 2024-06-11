import org.junit.Test;
import static org.mockito.Mockito.*;;

public class IndexFileActionTest {
    @Test
    public void testActionPerformed() {
        JTailMainModel jTailMainModelMock = mock(JTailMainModel.class);
        OpenFileModel openFileModelMock = mock(OpenFileModel.class);
        IndexFileAction indexFileAction = new IndexFileAction(jTailMainModelMock, openFileModelMock);
        ActionEvent actionEventMock = mock(ActionEvent.class);

        indexFileAction.actionPerformed(actionEventMock);

        verify(jTailMainModelMock).add(any(JTailModel.class));
    }
}
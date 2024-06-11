import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class IndexFileActionTest {
    @Test
    public void testActionPerformed() {
        JTailMainModel jTailMainModel = mock(JTailMainModel.class);
        OpenFileModel openFileModel = new OpenFileModel("testFile.txt", FileIndexType.FILE_BASED);
        IndexFileAction indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);

        ActionEvent actionEvent = mock(ActionEvent.class);
        Component dialog = mock(Component.class);

        when(actionEvent.getSource()).thenReturn(dialog);

        indexFileAction.actionPerformed(actionEvent);

        verify(jTailMainModel).add(any(JTailModel.class));
        verify(dialog).setVisible(false);
    }
}
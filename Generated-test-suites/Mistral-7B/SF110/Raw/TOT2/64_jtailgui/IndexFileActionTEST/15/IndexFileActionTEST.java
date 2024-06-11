import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class IndexFileActionTest {

    @Test
    public void testActionPerformed() {
        JTailMainModel jTailMainModelMock = mock(JTailMainModel.class);
        OpenFileModel openFileModelMock = mock(OpenFileModel.class);
        JTailLogger loggerMock = mock(JTailLogger.class);
        InformationHandler infoHandlerMock = mock(InformationHandler.class);
        ExceptionHandler exceptionHandlerMock = mock(ExceptionHandler.class);
        UIMessage uiMessageMock = mock(UIMessage.class);

        when(openFileModelMock.getFile()).thenReturn(new File("test.txt"));
        when(openFileModelMock.getIndexType()).thenReturn(IndexType.FILE_BASED);
        when(openFileModelMock.getFileNotFoundException()).thenThrow(new FileNotFoundException("File not found"));

        IndexFileAction indexFileAction = new IndexFileAction(jTailMainModelMock, openFileModelMock);
        indexFileAction.setJTailLogger(loggerMock);
        indexFileAction.setInformationHandler(infoHandlerMock);
        indexFileAction.setExceptionHandler(exceptionHandlerMock);

        ActionEvent actionEvent = mock(ActionEvent.class);
        Component dialogMock = mock(Component.class);
        when(actionEvent.getSource()).thenReturn(dialogMock);

        indexFileAction.actionPerformed(actionEvent);

        verify(loggerMock).debug("File : test.txt");
        verify(loggerMock).debug("Index type : FILE_BASED");
        verify(dialogMock).setVisible(false);
        verify(jTailMainModelMock).add(any(JTailModel.class));
    }
}
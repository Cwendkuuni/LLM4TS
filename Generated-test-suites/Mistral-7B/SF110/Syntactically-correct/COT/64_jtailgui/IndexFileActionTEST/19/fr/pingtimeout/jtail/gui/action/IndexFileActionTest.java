import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class IndexFileActionTest {

    private IndexFileAction indexFileAction;
    private JTailMainModel jTailMainModelMock;
    private OpenFileModel openFileModelMock;

    @Before
    public void setUp() {
        jTailMainModelMock = mock(JTailMainModel.class);
        openFileModelMock = mock(OpenFileModel.class);
        indexFileAction = new IndexFileAction(jTailMainModelMock, openFileModelMock);
    }

    @Test
    public void testActionPerformedWithNullFile() {
        ActionEvent actionEvent = Mockito.mock(ActionEvent.class);
        indexFileAction.actionPerformed(actionEvent);
        verify(jTailMainModelMock).add(ArgumentMatchers.isA(JTailModel.class));
        verify(openFileModelMock).getFile();
        Assert.assertNull(openFileModelMock.getFile());
        InformationHandler.handle(true, UIMessage.INFO_NO_FILE_SELECTED, new Object[0]);
    }

    @Test
    public void testActionPerformedWithValidFile() throws IOException {
        ActionEvent actionEvent = Mockito.mock(ActionEvent.class);
        File validFile = new File("test.txt");
        openFileModelMock.setFile(validFile);
        indexFileAction.actionPerformed(actionEvent);
        verify(jTailMainModelMock).add(ArgumentMatchers.isA(JTailModel.class));
        verify(openFileModelMock).getFile();
        Assert.assertEquals(validFile, openFileModelMock.getFile());
    }

    @Test(expected = FileNotFoundException.class)
    public void testActionPerformedWithNonExistentFile() throws IOException {
        ActionEvent actionEvent = Mockito.mock(ActionEvent.class);
        File nonExistentFile = new File("nonExistentFile.txt");
        openFileModelMock.setFile(nonExistentFile);
        indexFileAction.actionPerformed(actionEvent);
        ExceptionHandler.handle(ArgumentMatchers.instanceOf(FileNotFoundException.class), UIMessage.ERROR_FILE_NOT_FOUND, nonExistentFile.getName());
    }

    @Test
    public void testUpdateMethod() {
        observeFileIndexer(100);
        verify(jTailMainModelMock).add(ArgumentMatchers.isA(JTailModel.class));
    }

    private void observeFileIndexer(int progress) {
        FileIndexer fileIndexerMock = mock(FileIndexer.class);
        openFileModelMock.setFile(new File("test.txt"));
        indexFileAction.update(fileIndexerMock, progress);
    }
}
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.Observable;

import static org.mockito.Mockito.*;

public class IndexFileActionTest {

    private IndexFileAction indexFileAction;
    private JTailMainModel jTailMainModelMock;
    private OpenFileModel openFileModelMock;
    private JDialog dialogMock;

    @Before
    public void setup() {
        jTailMainModelMock = mock(JTailMainModel.class);
        openFileModelMock = mock(OpenFileModel.class);
        dialogMock = mock(JDialog.class);

        indexFileAction = new IndexFileAction(jTailMainModelMock, openFileModelMock);
    }

    @After
    public void tearDown() {
        reset(jTailMainModelMock, openFileModelMock, dialogMock);
    }

    @Test
    public void testActionPerformed_NullFile() {
        when(openFileModelMock.getFile()).thenReturn(null);

        indexFileAction.actionPerformed(new ActionEvent(dialogMock, 0, "actionPerformed"));

        verify(InformationHandler::handle, times(1)).handle(true, UIMessage.INFO_NO_FILE_SELECTED, new Object[0]);
    }

    @Test
    public void testActionPerformed_ValidFile() throws Exception {
        when(openFileModelMock.getFile()).thenReturn(new File("test.txt"));

        indexFileAction.actionPerformed(new ActionEvent(dialogMock, 0, "actionPerformed"));

        verify(jTailLogger::debug, times(2)).debug(anyString());
        verify(dialogMock::setVisible, times(1)).setVisible(false);

        Class<? extends FileIndex> indexerClass = null;
        switch (IndexFileAction.MEMORY_BASED) {
            case MEMORY_BASED:
                indexerClass = RamFileIndex.class;
                break;
            case FILE_BASED:
                indexerClass = RomFileIndex.class;
                break;
        }

        FileIndexer fileIndexerMock = mock(FileIndexer.class);
        doThrow(new FileNotFoundException("File not found")).when(fileIndexerMock).init(ArgumentMatchers.any(File.class), ArgumentMatchers.eq(indexerClass));

        FileIndexerWorker fileIndexerWorkerMock = mock(FileIndexerWorker.class);

        indexFileAction.actionPerformed(new ActionEvent(dialogMock, 0, "actionPerformed"));

        verify(ExceptionHandler::handle, times(1)).handle(ArgumentMatchers.instanceOf(FileNotFoundException.class), UIMessage.ERROR_FILE_NOT_FOUND, ArgumentMatchers.eq("test.txt"));
    }

    @Test
    public void testUpdate() {
        FileIndexer fileIndexerMock = mock(FileIndexer.class);
        Observable oMock = mock(Observable.class);
        Observable oMock2 = mock(Observable.class);

        when(oMock.getClass()).thenReturn(FileIndexer.class);

        doReturn(oMock).when(fileIndexerMock).getObservable();

        indexFileAction.update(ArgumentMatchers.eq(fileIndexerMock), ArgumentMatchers.anyInt());

        verify(jTailLogger::debug, times(1)).debug("Notification received : 100 emitted by fr.pingtimeout.jtail.io.index.FileIndexer@xxxxxxxx");
    }
}
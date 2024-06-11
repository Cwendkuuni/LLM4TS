import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

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
    public void testActionPerformedNullFile() {
        indexFileAction.actionPerformed(new ActionEvent(new JButton(), 0, "test"));
        verify(jTailMainModelMock).add(ArgumentMatchers.isNull());
    }

    @Test
    public void testActionPerformedValidFile() {
        File file = new File("test.txt");
        openFileModelMock.setFile(file);
        openFileModelMock.setIndexType(IndexType.FILE_BASED);

        indexFileAction.actionPerformed(new ActionEvent(new JButton(), 0, "test"));

        Assert.assertEquals(file, openFileModelMock.getFile());
        verify(jTailMainModelMock).add(ArgumentMatchers.instanceOf(JTailModel.class));
    }

    @Test(expected = NullPointerException.class)
    public void testActionPerformedNullIndexerClass() {
        openFileModelMock.setFile(new File("test.txt"));
        openFileModelMock.setIndexType(null);

        indexFileAction.actionPerformed(new ActionEvent(new JButton(), 0, "test"));
    }

    @Test(expected = FileNotFoundException.class)
    public void testActionPerformedNonExistingFile() {
        File file = new File("nonExistingFile.txt");
        openFileModelMock.setFile(file);
        openFileModelMock.setIndexType(IndexType.FILE_BASED);

        indexFileAction.actionPerformed(new ActionEvent(new JButton(), 0, "test"));
    }

    @Test
    public void testUpdateNullFileIndexer() {
        File file = new File("test.txt");
        openFileModelMock.setFile(file);
        openFileModelMock.setIndexType(IndexType.FILE_BASED);

        indexFileAction.update(ArgumentMatchers.any(Observable.class), ArgumentMatchers.anyInt());
    }

    @Test
    public void testUpdateValidFileIndexer() throws IOException {
        File file = new File("test.txt");
        openFileModelMock.setFile(file);
        openFileModelMock.setIndexType(IndexType.FILE_BASED);

        FileIndexer fileIndexerMock = mock(FileIndexer.class);
        indexFileAction.update(Mockito.mock(Observable.class), 100);

        verify(fileIndexerMock).getFile();
        verify(jTailMainModelMock).add(ArgumentMatchers.instanceOf(JTailModel.class));
    }

}
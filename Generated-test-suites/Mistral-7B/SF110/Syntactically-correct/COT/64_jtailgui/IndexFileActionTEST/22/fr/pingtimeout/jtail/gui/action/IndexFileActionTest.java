import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class IndexFileActionTest {

    private IndexFileAction indexFileAction;
    private JTailMainModel jTailMainModel;
    private OpenFileModel openFileModel;

    @Before
    public void setUp() {
        jTailMainModel = mock(JTailMainModel.class);
        openFileModel = mock(OpenFileModel.class);
        indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
    }

    @Test
    public void testActionPerformedNullFile() {
        when(openFileModel.getFile()).thenReturn(null);
        indexFileAction.actionPerformed(new ActionEvent(new Object(), 0, ""));
        Mockito.verify(jTailMainModel, Mockito.never()).add(Mockito.any(JTailModel.class));
        Assert.assertTrue(InformationHandler.getNumberOfMessages() > 0);
    }

    @Test
    public void testActionPerformedValidFile() throws FileNotFoundException, IOException {
        File file = new File("testFile.txt");
        when(openFileModel.getFile()).thenReturn(file);
        when(openFileModel.getIndexType()).thenReturn(IndexType.FILE_BASED);

        indexFileAction.actionPerformed(new ActionEvent(new Object(), 0, ""));
        Mockito.verify(jTailMainModel, Mockito.times(1)).add(Mockito.any(JTailModel.class));
    }

    @Test(expected = FileNotFoundException.class)
    public void testActionPerformedInvalidFile() throws FileNotFoundException {
        File file = new File("testFile.txt");
        when(openFileModel.getFile()).thenReturn(file);
        when(openFileModel.getIndexType()).thenThrow(new FileNotFoundException("Error message"));

        indexFileAction.actionPerformed(new ActionEvent(new Object(), 0, ""));
    }

    @Test
    public void testUpdate() {
        FileIndexer fileIndexer = mock(FileIndexer.class);
        when(fileIndexer.getFile()).thenReturn(new File("testFile.txt"));
        indexFileAction.update(fileIndexer, 100);
        Mockito.verify(jTailMainModel, Mockito.times(1)).add(Mockito.any(JTailModel.class));
    }
}
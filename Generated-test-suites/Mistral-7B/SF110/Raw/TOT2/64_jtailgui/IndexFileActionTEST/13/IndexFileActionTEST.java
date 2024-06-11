import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class IndexFileActionTest {

    @Test
    public void testConstructor() {
        JTailMainModel jTailMainModel = mock(JTailMainModel.class);
        OpenFileModel openFileModel = mock(OpenFileModel.class);

        IndexFileAction indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);

        assertEquals("action.open.label", indexFileAction.getName());
        assertEquals("action.open.tooltip", indexFileAction.getToolTipText());
        assertEquals('O', indexFileAction.getMnemonic());
        assertEquals("F2", indexFileAction.getAcceleratorKeyString());
        assertEquals(IndexFileAction.OPEN_ICON, indexFileAction.getIcon());
        verify(jTailMainModel).add(Mockito.any(JTailModel.class));
        verify(openFileModel).addObserver(indexFileAction);
    }

    @Test
    public void testActionPerformedWithValidEvent() {
        JTailMainModel jTailMainModel = mock(JTailMainModel.class);
        OpenFileModel openFileModel = mock(OpenFileModel.class);
        ActionEvent actionEvent = mock(ActionEvent.class);
        IndexFileAction indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);

        when(openFileModel.getFile()).thenReturn(new File("testFile.txt"));
        when(openFileModel.getIndexType()).thenReturn(IndexType.FILE_BASED);

        indexFileAction.actionPerformed(actionEvent);

        FileIndexerWorker fileIndexerWorker = Mockito.spy(new FileIndexerWorker(Mockito.mock(FileIndexer.class)));
        indexFileAction.fileIndexerWorker = fileIndexerWorker;

        fileIndexerWorker.execute();

        verify(jTailMainModel).add(Mockito.any(JTailModel.class));
    }

    @Test(expected = NullPointerException.class)
    public void testActionPerformedWithNullEvent() {
        JTailMainModel jTailMainModel = mock(JTailMainModel.class);
        OpenFileModel openFileModel = mock(OpenFileModel.class);
        IndexFileAction indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);

        indexFileAction.actionPerformed(null);
    }

    @Test(expected = FileNotFoundException.class)
    public void testActionPerformedWithFileNotFoundException() {
        JTailMainModel jTailMainModel = mock(JTailMainModel.class);
        OpenFileModel openFileModel = mock(OpenFileModel.class);
        ActionEvent actionEvent = mock(ActionEvent.class);
        IndexFileAction indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);

        when(openFileModel.getFile()).thenThrow(new FileNotFoundException("testFile.txt"));

        indexFileAction.actionPerformed(actionEvent);
    }

    @Test
    public void testUpdateWithValidArguments() {
        JTailMainModel jTailMainModel = mock(JTailMainModel.class);
        Observable observable = mock(Observable.class);
        Object arg = 100;
        IndexFileAction indexFileAction = new IndexFileAction(jTailMainModel, null);

        indexFileAction.update(observable, arg);

        verify(jTailMainModel).add(Mockito.any(JTailModel.class));
    }

    @Test(expected = NullPointerException.class)
    public void testUpdateWithNullObservable() {
        JTailMainModel jTailMainModel = mock(JTailMainModel.class);
        IndexFileAction indexFileAction = new IndexFileAction(jTailMainModel, null);

        indexFileAction.update(null, 100);
    }

    @Test(expected = NullPointerException.class)
    public void testUpdateWithNullObject() {
        JTailMainModel jTailMainModel = mock(JTailMainModel.class);
        IndexFileAction indexFileAction = new IndexFileAction(jTailMainModel, null);

        indexFileAction.update(observable, null);
    }

    @Test(expected = FileNotFoundException.class)
    public void testUpdateWithFileNotFoundException() {
        JTailMainModel jTailMainModel = mock(JTailMainModel.class);
        Observable observable = mock(Observable.class);
        IndexFileAction indexFileAction = new IndexFileAction(jTailMainModel, null);

        when(observable).thenThrow(new FileNotFoundException("testFile.txt"));

        indexFileAction.update(observable, 100);
    }
}
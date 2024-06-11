package fr.pingtimeout.jtail.gui.action;

import static org.mockito.Mockito.*;
import org.junit.Test;
import org.junit.Assert.*;

public class IndexFileActionTest {

    @Test
    public void testIndexFileActionConstructor() {
        JTailMainModel jTailMainModel = Mockito.mock(JTailMainModel.class);
        OpenFileModel openFileModel = Mockito.mock(OpenFileModel.class);
        IndexFileAction indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
        assertNotNull(indexFileAction);
    }

    @Test
    public void testActionPerformed() {
        ActionEvent actionEvent = Mockito.mock(ActionEvent.class);
        IndexFileAction indexFileAction = new IndexFileAction(null, null);
        indexFileAction.actionPerformed(actionEvent);
        verify(indexFileAction).handleNoFileSelected();
    }

    @Test
    public void testUpdate() {
        Observable observable = Mockito.mock(Observable.class);
        Object arg = 100;
        IndexFileAction indexFileAction = Mockito.spy(new IndexFileAction(null, null));
        indexFileAction.update(observable, arg);
        verify(indexFileAction).handleFileIndexed(arg);
    }

    @Test(expected = NullPointerException.class)
    public void testIndexFileActionConstructorNullArgument() {
        new IndexFileAction(null, null);
    }

    @Test(expected = FileNotFoundException.class)
    public void testFileNotFoundException() throws FileNotFoundException {
        FileIndexer fileIndexer = Mockito.mock(FileIndexer.class);
        IndexFileAction indexFileAction = new IndexFileAction(null, null);
        doThrow(new FileNotFoundException("File not found")).when(fileIndexer).addObserver(indexFileAction);
        indexFileAction.actionPerformed(Mockito.mock(ActionEvent.class));
    }

    @Test(expected = Exception.class)
    public void testExceptionThrownByFileIndexerWorker() {
        FileIndexer fileIndexer = Mockito.mock(FileIndexer.class);
        IndexFileAction indexFileAction = new IndexFileAction(null, null);
        doThrow(new Exception("Exception message")).when(fileIndexer).addObserver(indexFileAction);
        indexFileAction.actionPerformed(Mockito.mock(ActionEvent.class));
    }

    @Test
    public void testNullPointerExceptionGettingSourceOfActionEvent() {
        IndexFileAction indexFileAction = new IndexFileAction(null, null);
        indexFileAction.actionPerformed(null);
        verify(indexFileAction).handleNoFileSelected();
    }

    @Test
    public void testMultipleNotificationsWithSamePercentage() {
        Observable observable = Mockito.mock(Observable.class);
        IndexFileAction indexFileAction = Mockito.spy(new IndexFileAction(null, null));
        indexFileAction.update(observable, 100);
        indexFileAction.update(observable, 100);
        verify(indexFileAction, times(2)).handleFileIndexed(100);
    }
}
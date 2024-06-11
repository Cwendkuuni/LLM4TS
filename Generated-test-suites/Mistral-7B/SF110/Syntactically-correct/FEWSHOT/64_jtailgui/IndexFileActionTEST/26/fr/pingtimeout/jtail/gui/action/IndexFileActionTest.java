package fr.pingtimeout.jtail.gui.action;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.Observable;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class IndexFileActionTest {

    @Mock
    private JTailMainModel jTailMainModel;

    @Mock
    private OpenFileModel openFileModel;

    @Mock
    private FileIndex fileIndex;

    @Mock
    private Observable observable;

    @Mock
    private FileIndexerWorker fileIndexerWorker;

    private IndexFileAction indexFileAction;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(openFileModel.getFile()).thenReturn(new File("test.file"));
        when(openFileModel.getIndexType()).thenReturn(IndexType.FILE_BASED);
        indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
    }

    @Test
    public void testActionPerformed_NoFileSelected() {
        ActionEvent actionEvent = mock(ActionEvent.class);
        indexFileAction.actionPerformed(actionEvent);
        verify(jTailMainModel, times(1)).add(isNull());
        InformationHandler.verify(1, times(1)).handle(true, UIMessage.INFO_NO_FILE_SELECTED, new Object[0]);
    }

    @Test
    public void testActionPerformed_ValidFileAndIndexType() throws FileNotFoundException {
        when(openFileModel.getFile()).thenReturn(new File("test.file"));
        when(openFileModel.getIndexType()).thenReturn(IndexType.FILE_BASED);
        when(fileIndexerWorker.execute()).thenReturn(true);

        ActionEvent actionEvent = mock(ActionEvent.class);
        indexFileAction.actionPerformed(actionEvent);

        verify(jTailMainModel, times(1)).add(argThat(m -> m instanceof JTailModel));
        verify(fileIndexerWorker, times(1)).execute();
        InformationHandler.verify(0, times(1)).handle(true, UIMessage.INFO_NO_FILE_SELECTED, new Object[0]);
    }

    @Test
    public void testActionPerformed_InvalidFileOrIndexType() throws FileNotFoundException {
        when(openFileModel.getFile()).thenReturn(null);
        when(openFileModel.getIndexType()).thenReturn(IndexType.MEMORY_BASED);

        ActionEvent actionEvent = mock(ActionEvent.class);
        indexFileAction.actionPerformed(actionEvent);

        verify(jTailMainModel, times(0)).add(any(JTailModel.class));
        verify(fileIndexerWorker, never()).execute();
        ExceptionHandler.verify(1, times(1)).handle(any(Exception.class), eq(UIMessage.ERROR_FILE_NOT_FOUND), eq("test.file"));
    }

    @Test
    public void testUpdate_FileIndexedSuccessfully() {
        when(openFileModel.getFile()).thenReturn(new File("test.file"));
        when(openFileModel.getIndexType()).thenReturn(IndexType.FILE_BASED);
        when(fileIndexerWorker.execute()).thenReturn(true);

        indexFileAction.update(observable, 100);

        verify(jTailMainModel, times(1)).add(argThat(m -> m instanceof JTailModel));
    }

    @Test
    public void testUpdate_FileIndexingFailed() {
        when(openFileModel.getFile()).thenReturn(new File("test.file"));
        when(openFileModel.getIndexType()).thenReturn(IndexType.FILE_BASED);
        when(fileIndexerWorker.execute()).thenThrow(new Exception());

        indexFileAction.update(observable, 100);

        verify(jTailMainModel, times(0)).add(any(JTailModel.class));
        ExceptionHandler.verify(1, times(1)).handle(any(Exception.class), eq(UIMessage.ERROR_GENERIC_MESSAGE), isNull());
    }

    @Test
    public void testUpdate_FileNotFoundDuringIndexing() {
        when(openFileModel.getFile()).thenReturn(new File("test.file"));
        when(openFileModel.getIndexType()).thenReturn(IndexType.FILE_BASED);
        when(fileIndexerWorker.execute()).thenThrow(new FileNotFoundException());

        indexFileAction.update(observable, 100);

        verify(jTailMainModel, times(0)).add(any(JTailModel.class));
        ExceptionHandler.verify(1, times(1)).handle(any(Exception.class), eq(UIMessage.ERROR_FILE_NOT_FOUND), anyString());
    }
}
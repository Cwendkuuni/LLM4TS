package fr.pingtimeout.jtail.gui.action.test;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import fr.pingtimeout.jtail.gui.action.IndexFileAction;
import fr.pingtimeout.jtail.gui.action.JTailMainModel;
import fr.pingtimeout.jtail.gui.action.OpenFileModel;
import fr.pingtimeout.jtail.gui.controller.JTailController;
import fr.pingtimeout.jtail.io.index.FileIndexer;
import fr.pingtimeout.jtail.io.index.FileIndexerWorker;
import fr.pingtimeout.jtail.io.index.RamFileIndex;
import fr.pingtimeout.jtail.io.index.RomFileIndex;

public class IndexFileActionTest {

    private IndexFileAction action;
    private OpenFileModel openFileModel;
    private JTailMainModel jTailMainModel;
    private JTailController jTailController;

    @Before
    public void setup() {
        openFileModel = mock(OpenFileModel.class);
        jTailMainModel = mock(JTailMainModel.class);
        jTailController = mock(JTailController.class);
        action = new IndexFileAction(jTailMainModel, openFileModel);
    }

    @Test
    public void testActionPerformed_NullFile() {
        when(openFileModel.getFile()).thenReturn(null);

        action.actionPerformed(mock(ActionEvent.class));

        verify(jTailMainModel, never()).add(any(JTailModel.class));
        verify(openFileModel).getFile();
        InformationHandler.verifyHandle(true, UIMessage.INFO_NO_FILE_SELECTED, anyArray());
    }

    @Test
    public void testActionPerformed_ValidFile() throws FileNotFoundException {
        when(openFileModel.getFile()).thenReturn(new File("test.txt"));

        FileIndexer fileIndexer = mock(FileIndexer.class);
        when(new FileIndexer(openFileModel.getFile(), RamFileIndex.class)).thenReturn(fileIndexer);

        action.actionPerformed(mock(ActionEvent.class));

        verify(jTailMainModel).add(any(JTailModel.class));
        verify(fileIndexer).addObserver(eq(action));
        FileIndexerWorker.verifyExecute(fileIndexerWorkerCaptor.capture());

        FileIndexerWorker fileIndexerWorker = fileIndexerWorkerCaptor.getValue();
        verify(fileIndexerWorker).addPropertyChangeListener(any());
        assertTrue(fileIndexerWorker.isDone());
    }

    @Test
    public void testActionPerformed_InvalidIndexType() {
        openFileModel.setIndexType(-1);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> new FileIndexer(openFileModel.getFile(), RomFileIndex.class));

        assertEquals("Invalid index type", exception.getMessage());
    }

    @Test
    public void testUpdate_Success() {
        File file = new File("test.txt");
        FileIndexer fileIndexer = mock(FileIndexer.class);
        when(fileIndexer.getFile()).thenReturn(file);
        when(fileIndexer.getIndex()).thenReturn(new FileIndex());
        when(new FileIndexer(openFileModel.getFile(), RamFileIndex.class)).thenReturn(fileIndexer);

        action.update(any(Observable.class), eq(100));

        JTailModel model = any(JTailModel.class);
        verify(jTailMainModel).add(model);
        verify(model).setFile(file);
    }

    @Test
    public void testUpdate_Failure_FileNotFound() {
        File file = new File("test.txt");
        FileIndexer fileIndexer = mock(FileIndexer.class);
        when(fileIndexer.getFile()).thenReturn(file);
        whenNew(FileIndexer.class).withArguments(file, RamFileIndex.class).thenThrow(new FileNotFoundException("File not found"));

        Exception exception = assertThrows(Exception.class, () -> action.update(any(Observable.class), eq(100)));

        assertEquals(UIMessage.ERROR_FILE_NOT_FOUND, exception.getMessage());
    }

    @Test
    public void testUpdate_Failure_Generic() {
        File file = new File("test.txt");
        FileIndexer fileIndexer = mock(FileIndexer.class);
        when(fileIndexer.getFile()).thenReturn(file);
        whenNew(FileIndexer.class).withArguments(file, RamFileIndex.class).thenThrow(new RuntimeException("Generic error"));

        Exception exception = assertThrows(Exception.class, () -> action.update(any(Observable.class), eq(100)));

        assertEquals(UIMessage.ERROR_GENERIC_MESSAGE, exception.getMessage());
    }
}
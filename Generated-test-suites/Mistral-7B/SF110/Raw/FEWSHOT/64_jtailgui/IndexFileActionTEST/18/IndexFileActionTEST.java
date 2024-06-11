package fr.pingtimeout.jtail.gui.action;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.event.*;
import java.io.File;
import java.io.IOException;

import javax.swing.*;
import javax.swing.event.ChangeEvent;

import fr.pingtimeout.jtail.gui.controller.*;
import fr.pingtimeout.jtail.gui.message.*;
import fr.pingtimeout.jtail.gui.model.*;
import fr.pingtimeout.jtail.io.*;
import fr.pingtimeout.jtail.io.index.*;

public class IndexFileActionTest {

    @Test
    public void testActionPerformed_NullFile() {
        IndexFileAction action = new IndexFileAction(mock(JTailMainModel.class), mock(OpenFileModel.class));
        ActionEvent actionEvent = mock(ActionEvent.class);

        // When actionPerformed is called with null file
        action.actionPerformed(actionEvent);

        // Then InformationHandler is called with "No file selected" message
        verify(InformationHandler.class, times(1)).handle(true, UIMessage.INFO_NO_FILE_SELECTED, new Object[0]);
    }

    @Test
    public void testActionPerformed_ValidFileAndIndexType() {
        ResourceBundle bundle = ResourceBundle.getBundle("fr.pingtimeout.jtail.gui.language");
        IndexFileAction action = new IndexFileAction(mock(JTailMainModel.class), mock(OpenFileModel.class));
        ActionEvent actionEvent = mock(ActionEvent.class);
        JDialog dialog = mock(JDialog.class);
        when(actionEvent.getSource()).thenReturn(dialog);

        // Given file and index type are valid
        File file = new File("test.txt");
        OpenFileModel openFileModel = mock(OpenFileModel.class);
        when(openFileModel.getFile()).thenReturn(file);
        when(openFileModel.getIndexType()).thenReturn(IndexType.FILE_BASED);

        // When actionPerformed is called
        action.actionPerformed(actionEvent);

        // Then JTailLogger.debug is called with expected messages
        verify(JTailLogger.class, times(2)).debug(anyString());

        // And dialog is hidden
        verify(dialog, times(1)).setVisible(false);

        // And correct indexer class is used
        Class<? extends FileIndex> indexerClass = null;
        switch (openFileModel.getIndexType()) {
            case MEMORY_BASED:
                fail("Memory-based index type should not be selected");
            case FILE_BASED:
                indexerClass = RomFileIndex.class;
                break;
            default:
                fail("Invalid index type selected");
        }
        verify(FileIndexer.class, times(1)).newFileIndexer(file, eq(indexerClass));

        // And FileIndexerWorker is executed
        FileIndexerWorker fileIndexerWorker = new FileIndexerWorker(mock(FileIndexer.class));
        fileIndexerWorker.execute();
        verify(fileIndexerWorker, times(1)).execute();
    }

    @Test
    public void testActionPerformed_InvalidFile() {
        IndexFileAction action = new IndexFileAction(mock(JTailMainModel.class), mock(OpenFileModel.class));
        ActionEvent actionEvent = mock(ActionEvent.class);
        JDialog dialog = mock(JDialog.class);
        when(actionEvent.getSource()).thenReturn(dialog);
        File file = new File("test.txt");
        OpenFileModel openFileModel = mock(OpenFileModel.class);
        when(openFileModel.getFile()).thenReturn(file);

        // When actionPerformed is called with invalid file
        try {
            new FileIndexer(file, null);
            fail("Expected FileNotFoundException");
        } catch (FileNotFoundException e) {
            // Expected
        }

        action.actionPerformed(actionEvent);

        // Then ExceptionHandler is called with "File not found" message and file name
        verify(ExceptionHandler.class, times(1)).handle(any(Exception.class), eq(UIMessage.ERROR_FILE_NOT_FOUND), eq(file.getName()));
    }

    @Test
    public void testUpdate_IndexingComplete() {
        IndexFileAction action = new IndexFileAction(mock(JTailMainModel.class), mock(OpenFileModel.class));
        Observer observer = mock(Observer.class);
        Observable observable = mock(Observable.class);
        when(observable.addObserver(eq(observer))).thenReturn(this);
        FileIndexer fileIndexer = mock(FileIndexer.class);
        when(fileIndexer.getFile()).thenReturn(new File("test.txt"));
        when(fileIndexer.addObserver(eq(action))).thenReturn(this);

        // Given indexing is completed
        ChangeEvent changeEvent = mock(ChangeEvent.class);
        when(changeEvent.getSource()).thenReturn(fileIndexer);
        when(changeEvent.getStateChange()).thenReturn(ChangeEvent.COMPLETE);

        // When update is called
        action.update(observable, changeEvent);

        // Then JTailLogger.debug is called with expected message
        verify(JTailLogger.class, times(1)).debug("Notification received : COMPLETE emitted by FileIndexer");

        // And JTailModel is added to JTailMainModel
        JTailModel jTailModel = mock(JTailModel.class);
        File file = new File("test.txt");
        LineReader lineReader = mock(LineReader.class);
        when(lineReader.getFile()).thenReturn(file);
        when(fileIndexer.getIndex()).thenReturn(lineReader);
        JTailModel newJTailModel = new JTailModel(file, lineReader);
        assertEquals(newJTailModel, action.jTailMainModel.getJTailModelList().get(action.jTailMainModel.getJTailModelList().size() - 1));
    }

    @Test
    public void testUpdate_IndexingFailed() {
        IndexFileAction action = new IndexFileAction(mock(JTailMainModel.class), mock(OpenFileModel.class));
        Observer observer = mock(Observer.class);
        Observable observable = mock(Observable.class);
        when(observable.addObserver(eq(observer))).thenReturn(this);
        FileIndexer fileIndexer = mock(FileIndexer.class);
        when(fileIndexer.getFile()).thenReturn(new File("test.txt"));
        when(fileIndexer.addObserver(eq(action))).thenReturn(this);

        // Given indexing failed
        ChangeEvent changeEvent = mock(ChangeEvent.class);
        when(changeEvent.getSource()).thenReturn(fileIndexer);

        // When update is called
        action.update(observable, changeEvent);

        // Then JTailLogger.debug is called with expected message
        verify(JTailLogger.class, times(1)).debug("Notification received : FAILED emitted by FileIndexer");

        // And no JTailModel is added to JTailMainModel
        JTailMainModel jTailMainModel = mock(JTailMainModel.class);
        assertEquals(0, jTailMainModel.getJTailModelList().size());

        // And ExceptionHandler is called with generic error message
        ExceptionHandler.handle(any(Exception.class), eq(UIMessage.ERROR_GENERIC_MESSAGE), new Object[0]);
    }
}
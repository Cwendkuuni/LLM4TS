package test.fr.pingtimeout.jtail.gui.action;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.Observer;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import fr.pingtimeout.jtail.gui.action.IndexFileAction;
import fr.pingtimeout.jtail.gui.controller.JTailMainModel;
import fr.pingtimeout.jtail.gui.message.UIMessage;
import fr.pingtimeout.jtail.gui.model.JTailModel;
import fr.pingtimeout.jtail.gui.observer.InformationHandler;
import fr.pingtimeout.jtail.gui.observer.ExceptionHandler;
import fr.pingtimeout.jtail.io.index.FileIndexer;
import fr.pingtimeout.jtail.io.index.FileIndexerWorker;
import fr.pingtimeout.jtail.io.index.RamFileIndex;
import fr.pingtimeout.jtail.io.index.RomFileIndex;
import fr.pingtimeout.jtail.util.JTailLogger;

public class IndexFileActionTest {

    private IndexFileAction indexFileAction;
    private JTailMainModel jTailMainModelMock;
    private OpenFileModel openFileModelMock;
    private InformationHandler informationHandlerMock;
    private ExceptionHandler exceptionHandlerMock;
    private JDialog dialogMock;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        jTailMainModelMock = mock(JTailMainModel.class);
        openFileModelMock = mock(OpenFileModel.class);
        dialogMock = mock(JDialog.class);
        indexFileAction = new IndexFileAction(jTailMainModelMock, openFileModelMock);
        informationHandlerMock = mock(InformationHandler.class);
        exceptionHandlerMock = mock(ExceptionHandler.class);
    }

    @Test
    public void testIndexFileActionConstructor() {
        verifyNoMoreInteractions(jTailMainModelMock, openFileModelMock, informationHandlerMock, exceptionHandlerMock, dialogMock);
    }

    @Test
    public void testActionPerformed_NullFile_NoFileSelectedMessage() {
        when(openFileModelMock.getFile()).thenReturn(null);
        ActionEvent actionEvent = mock(ActionEvent.class);

        indexFileAction.actionPerformed(actionEvent);

        verify(informationHandlerMock).handle(isTrue(), UIMessage.INFO_NO_FILE_SELECTED, new Object[0]);
    }

    @Test
    public void testActionPerformed_ValidFile_CorrectIndexerClassSelected() {
        when(openFileModelMock.getFile()).thenReturn(new File("testFile.txt"));
        when(openFileModelMock.getIndexType()).thenReturn(IndexType.MEMORY_BASED);

        ActionEvent actionEvent = mock(ActionEvent.class);

        indexFileAction.actionPerformed(actionEvent);

        verify(jTailLogger).debug("File : testFile.txt");
        verify(jTailLogger).debug("Index type : MEMORY_BASED");
        verify(dialogMock).setVisible(false);
        verify(indexFileAction).createFileIndexer(eq(RamFileIndex.class));
    }

    @Test
    public void testActionPerformed_ValidFile_InvalidIndexerClassSelected() {
        when(openFileModelMock.getFile()).thenReturn(new File("testFile.txt"));
        when(openFileModelMock.getIndexType()).thenReturn(IndexType.FILE_BASED);

        ActionEvent actionEvent = mock(ActionEvent.class);

        indexFileAction.actionPerformed(actionEvent);

        verify(jTailLogger).debug("File : testFile.txt");
        verify(jTailLogger).debug("Index type : FILE_BASED");
        verify(dialogMock).setVisible(false);
        verify(indexFileAction, never()).createFileIndexer(RamFileIndex.class);
        verify(indexFileAction, never()).createFileIndexer(RomFileIndex.class);
    }

    @Test(expected = FileNotFoundException.class)
    public void testActionPerformed_InvalidFile_FileNotFoundExceptionThrown() throws FileNotFoundException {
        when(openFileModelMock.getFile()).thenThrow(new FileNotFoundException("Test File not found"));
        ActionEvent actionEvent = mock(ActionEvent.class);

        indexFileAction.actionPerformed(actionEvent);

        verify(exceptionHandlerMock).handle(instanceOf(FileNotFoundException.class), UIMessage.ERROR_FILE_NOT_FOUND, "Test File not found");
    }

    @Test
    public void testCreateFileIndexer_MemoryBasedIndexer() {
        indexFileAction.createFileIndexer(RamFileIndex.class);

        verify(jTailLogger).debug("Creating memory-based indexer...");
    }

    @Test
    public void testCreateFileIndexer_FileBasedIndexer() {
        indexFileAction.createFileIndexer(RomFileIndex.class);

        verify(jTailLogger).debug("Creating file-based indexer...");
    }

    @Test
    public void testUpdate_FileIndexerCreated() {
        FileIndexer fileIndexer = mock(FileIndexer.class);
        FileIndexerWorker fileIndexerWorker = mock(FileIndexerWorker.class);

        when(fileIndexerWorker.getFileIndexer()).thenReturn(fileIndexer);
        when(fileIndexer.getFile()).thenReturn(new File("testFile.txt"));

        indexFileAction.update(fileIndexerWorker, 100);

        verify(jTailMainModelMock).add(argThat(model -> model instanceof JTailModel));
        verify(jTailLogger).debug("Notification received : 100 emitted by {}", fileIndexerWorker);
    }

    @Test(expected = FileNotFoundException.class)
    public void testUpdate_FileIndexerCreationFailed_FileNotFoundExceptionThrown() throws FileNotFoundException {
        FileIndexer fileIndexer = mock(FileIndexer.class);
        FileIndexerWorker fileIndexerWorker = mock(FileIndexerWorker.class);

        when(fileIndexerWorker.getFileIndexer()).thenReturn(fileIndexer);
        when(fileIndexer.getFile()).thenThrow(new FileNotFoundException("Test File not found"));

        indexFileAction.update(fileIndexerWorker, 100);

        verify(exceptionHandlerMock).handle(instanceOf(FileNotFoundException.class), UIMessage.ERROR_FILE_NOT_FOUND, "testFile.txt");
    }

    @Test
    public void testUpdate_FileIndexingFailed_GenericExceptionHandled() {
        FileIndexer fileIndexer = mock(FileIndexer.class);
        FileIndexerWorker fileIndexerWorker = mock(FileIndexerWorker.class);

        when(fileIndexerWorker.getFileIndexer()).thenReturn(fileIndexer);
        doThrow(new Exception("Test Exception")).when(fileIndexer).indexFile();

        indexFileAction.update(fileIndexerWorker, 100);

        verify(exceptionHandlerMock).handle(instanceOf(Exception.class), UIMessage.ERROR_GENERIC_MESSAGE, new Object[0]);
    }
}

enum IndexType {
    MEMORY_BASED,
    FILE_BASED
}
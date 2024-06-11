package fr.pingtimeout.jtail.gui.action;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import fr.pingtimeout.jtail.gui.controller.*;
import fr.pingtimeout.jtail.gui.message.*;
import fr.pingtimeout.jtail.gui.model.*;
import fr.pingtimeout.jtail.io.index.*;
import org.junit.*;
import org.mockito.*;

public class IndexFileActionTest {

    private ActionEvent actionEvent;
    private JDialog dialog;
    private OpenFileModel openFileModel;
    private JTailMainModel jTailMainModel;
    private IndexFileAction indexFileAction;
    private FileIndexer fileIndexer;
    private FileIndexerWorker fileIndexerWorker;

    @Before
    public void setUp() {
        openFileModel = mock(OpenFileModel.class);
        jTailMainModel = mock(JTailMainModel.class);
        actionEvent = mock(ActionEvent.class);
        dialog = mock(JDialog.class);
        indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
        when(openFileModel.getFile()).thenReturn(new File("testFile.txt"));
        when(openFileModel.getIndexType()).thenReturn(IndexType.FILE_BASED);
    }

    @Test
    public void testActionPerformedWithValidFileAndIndexType() {
        when(dialog.isVisible()).thenReturn(true);
        indexFileAction.actionPerformed(actionEvent);
        verify(jTailMainModel, times(1)).add(any(JTailModel.class));
    }

    @Test
    public void testActionPerformedWithNullFile() {
        when(dialog.isVisible()).thenReturn(true);
        when(openFileModel.getFile()).thenReturn(null);
        indexFileAction.actionPerformed(actionEvent);
        verify(InformationHandler, times(1)).handle(eq(true), any(UIMessage.class), anyArray());
    }

    @Test
    public void testActionPerformedWithInvalidIndexType() {
        when(dialog.isVisible()).thenReturn(true);
        when(openFileModel.getIndexType()).thenReturn(IndexType.INVALID);
        indexFileAction.actionPerformed(actionEvent);
        verifyNoInteractions(jTailMainModel);
    }

    @Test
    public void testActionPerformedWithExceptionDuringFileIndexing() {
        when(dialog.isVisible()).thenReturn(true);
        when(openFileModel.getFile()).thenReturn(new File("testFile.txt"));
        when(openFileModel.getIndexType()).thenReturn(IndexType.FILE_BASED);
        FileIndexer fileIndexer = mock(FileIndexer.class);
        doThrow(new FileNotFoundException("Invalid file path")).when(fileIndexer).init();
        doThrow(new Exception("Indexing failed")).when(fileIndexer).addObserver(any(Observer.class));
        when(fileIndexerClass()).thenReturn(FileIndexer.class);
        indexFileAction.actionPerformed(actionEvent);
        verify(ExceptionHandler, times(1)).handle(any(Throwable.class), any(UIMessage.class), anyArray());
    }

    @Test
    public void testUpdateWithSuccessfulFileIndexing() {
        when(dialog.isVisible()).thenReturn(true);
        when(openFileModel.getFile()).thenReturn(new File("testFile.txt"));
        when(openFileModel.getIndexType()).thenReturn(IndexType.FILE_BASED);
        FileIndexer fileIndexer = mock(FileIndexer.class);
        doReturn(fileIndexer).when(fileIndexerClass()).newInstance(eq(new File("testFile.txt")), eq(RamFileIndex.class));
        fileIndexerWorker = new FileIndexerWorker(fileIndexer);
        fileIndexerWorker.execute();
        indexFileAction.update(fileIndexer, 100);
        verify(jTailMainModel, times(1)).add(any(JTailModel.class));
    }

    @Test
    public void testUpdateWithExceptionDuringFileIndexing() {
        when(dialog.isVisible()).thenReturn(true);
        when(openFileModel.getFile()).thenReturn(new File("testFile.txt"));
        when(openFileModel.getIndexType()).thenReturn(IndexType.FILE_BASED);
        FileIndexer fileIndexer = mock(FileIndexer.class);
        doThrow(new FileNotFoundException("Invalid file path")).when(fileIndexer).init();
        doThrow(new Exception("Indexing failed")).when(fileIndexer).addObserver(any(Observer.class));
        when(fileIndexerClass()).thenReturn(FileIndexer.class);
        indexFileAction.update(fileIndexer, 100);
        verify(ExceptionHandler, times(1)).handle(any(Throwable.class), any(UIMessage.class), anyArray());
    }

    private Class<? extends FileIndexer> fileIndexerClass() {
        if (openFileModel.getIndexType() == IndexType.MEMORY_BASED) {
            return RamFileIndex.class;
        } else {
            return RomFileIndex.class;
        }
    }

    @After
    public void tearDown() {
        reset(openFileModel, jTailMainModel, fileIndexer, fileIndexerWorker, InformationHandler, ExceptionHandler);
    }
}
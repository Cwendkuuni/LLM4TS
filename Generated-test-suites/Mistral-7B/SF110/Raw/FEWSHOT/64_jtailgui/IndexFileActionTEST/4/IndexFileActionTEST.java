package fr.pingtimeout.jtail.gui.action;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import javax.swing.*;
import fr.pingtimeout.jtail.gui.controller.*;
import fr.pingtimeout.jtail.gui.message.*;
import fr.pingtimeout.jtail.gui.model.*;
import fr.pingtimeout.jtail.io.index.*;
import org.junit.Before;
import org.junit.Test;

public class IndexFileActionTest {

    private IndexFileAction indexFileAction;
    private JTailMainModel jTailMainModelMock;
    private OpenFileModel openFileModelMock;
    private File fileMock;

    @Before
    public void setUp() {
        jTailMainModelMock = mock(JTailMainModel.class);
        openFileModelMock = mock(OpenFileModel.class);
        fileMock = mock(File.class);

        indexFileAction = new IndexFileAction(jTailMainModelMock, openFileModelMock);
    }

    @Test
    public void testActionPerformedWithValidFile() {
        when(openFileModelMock.getFile()).thenReturn(fileMock);
        when(openFileModelMock.getIndexType()).thenReturn(IndexType.FILE_BASED);

        Component dialogMock = mock(Component.class);
        ActionEvent actionEventMock = mock(ActionEvent.class);

        indexFileAction.actionPerformed(actionEventMock);

        verify(jTailMainModelMock, times(1)).add(any(JTailModel.class));
        verify(openFileModelMock, times(1)).getFile();
        verify(openFileModelMock, times(1)).getIndexType();
        verify(dialogMock, times(1)).setVisible(false);
    }

    @Test
    public void testActionPerformedWithNullFile() {
        when(openFileModelMock.getFile()).thenReturn(null);

        indexFileAction.actionPerformed(mock(ActionEvent.class));

        verify(InformationHandler.class, times(1)).handle(eq(true), isA(UIMessage.class), anyArray());
    }

    @Test
    public void testUpdateWithIndexFileCreated() {
        FileIndexer fileIndexerMock = mock(FileIndexer.class);

        when(openFileModelMock.getFile()).thenReturn(fileMock);
        when(openFileModelMock.getIndexType()).thenReturn(IndexType.FILE_BASED);

        IndexFileAction.FileIndexerWorker fileIndexerWorkerMock = mock(IndexFileAction.FileIndexerWorker.class);

        doReturn(fileIndexerMock).when(indexFileAction).new FileIndexerWorker(any(FileIndexer.class));

        indexFileAction.update(any(Observable.class), eq(100));

        verify(jTailMainModelMock, times(1)).add(any(JTailModel.class));
        verify(fileIndexerMock, times(1)).getFile();
        verify(fileIndexerMock, times(1)).getIndex();
    }

    @Test
    public void testUpdateWithFileNotFoundException() {
        FileIndexer fileIndexerMock = mock(FileIndexer.class);
        FileNotFoundException fileNotFoundException = new FileNotFoundException("File not found");

        when(openFileModelMock.getFile()).thenReturn(fileMock);
        when(openFileModelMock.getIndexType()).thenReturn(IndexType.FILE_BASED);

        IndexFileAction.FileIndexerWorker fileIndexerWorkerMock = mock(IndexFileAction.FileIndexerWorker.class);

        doReturn(fileIndexerMock).when(indexFileAction).new FileIndexerWorker(any(FileIndexer.class));

        doThrow(fileNotFoundException).when(fileIndexerMock).init();

        indexFileAction.update(any(Observable.class), eq(100));

        verify(ExceptionHandler.class, times(1)).handle(eq(fileNotFoundException), isA(UIMessage.class), anyArray());
    }

    @Test
    public void testUpdateWithGenericException() {
        FileIndexer fileIndexerMock = mock(FileIndexer.class);
        Exception exception = new Exception("Generic exception");

        when(openFileModelMock.getFile()).thenReturn(fileMock);
        when(openFileModelMock.getIndexType()).thenReturn(IndexType.FILE_BASED);

        IndexFileAction.FileIndexerWorker fileIndexerWorkerMock = mock(IndexFileAction.FileIndexerWorker.class);

        doReturn(fileIndexerMock).when(indexFileAction).new FileIndexerWorker(any(FileIndexer.class));

        doThrow(exception).when(fileIndexerMock).init();

        indexFileAction.update(any(Observable.class), eq(100));

        verify(ExceptionHandler.class, times(1)).handle(eq(exception), isA(UIMessage.class), anyArray());
    }
}
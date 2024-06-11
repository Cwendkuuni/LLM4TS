package fr.pingtimeout.jtail.gui.action;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.Observable;

import fr.pingtimeout.jtail.controller.*;
import fr.pingtimeout.jtail.gui.message.*;
import fr.pingtimeout.jtail.gui.model.*;
import fr.pingtimeout.jtail.io.index.*;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

public class IndexFileActionTest {

    @Test
    public void testActionPerformedWithValidFileAndIndexType() {
        JTailMainModel jTailMainModelMock = mock(JTailMainModel.class);
        OpenFileModel openFileModel = new OpenFileModel("validFile.txt", IndexType.FILE_BASED);
        IndexFileAction action = new IndexFileAction(jTailMainModelMock, openFileModel);

        ActionEvent actionEventMock = mock(ActionEvent.class);
        Component dialogMock = mock(JDialog.class);

        doNothing().when(jTailMainModelMock).add(any(JTailModel.class));

        action.actionPerformed(actionEventMock);

        verify(jTailMainModelMock).add(argThat(new ModelMatcher()));
        verify(dialogMock).setVisible(false);
        verifyNoMoreInteractions(jTailMainModelMock);
        verifyNoMoreInteractions(dialogMock);
    }

    @Test
    public void testActionPerformedWithNullFile() {
        JTailMainModel jTailMainModelMock = mock(JTailMainModel.class);
        OpenFileModel openFileModel = new OpenFileModel(null, IndexType.FILE_BASED);
        IndexFileAction action = new IndexFileAction(jTailMainModelMock, openFileModel);

        ActionEvent actionEventMock = mock(ActionEvent.class);
        Component dialogMock = mock(JDialog.class);

        InformationHandler.handle = mock(InformationHandler.class);

        action.actionPerformed(actionEventMock);

        verify(InformationHandler.handle, times(1)).handle(true, UIMessage.INFO_NO_FILE_SELECTED, new Object[0]);
        verifyNoMoreInteractions(jTailMainModelMock);
        verifyNoMoreInteractions(dialogMock);
    }

    @Test
    public void testActionPerformedWithInvalidIndexType() {
        JTailMainModel jTailMainModelMock = mock(JTailMainModel.class);
        OpenFileModel openFileModel = new OpenFileModel("validFile.txt", IndexType.INVALID);
        IndexFileAction action = new IndexFileAction(jTailMainModelMock, openFileModel);

        ActionEvent actionEventMock = mock(ActionEvent.class);
        Component dialogMock = mock(JDialog.class);

        ExceptionHandler.handle = mock(ExceptionHandler.class);

        action.actionPerformed(actionEventMock);

        verify(ExceptionHandler.handle, times(1)).handle(any(Exception.class), eq(UIMessage.ERROR_INVALID_INDEX_TYPE), any(Object[].class));
        verifyNoMoreInteractions(jTailMainModelMock);
        verifyNoMoreInteractions(dialogMock);
    }

    @Test
    public void testActionPerformedWithFileNotFoundException() {
        JTailMainModel jTailMainModelMock = mock(JTailMainModel.class);
        OpenFileModel openFileModel = new OpenFileModel(new File("notFoundFile.txt"), IndexType.FILE_BASED);
        IndexFileAction action = new IndexFileAction(jTailMainModelMock, openFileModel);

        ActionEvent actionEventMock = mock(ActionEvent.class);
        Component dialogMock = mock(JDialog.class);

        ExceptionHandler.handle = mock(ExceptionHandler.class);

        when(openFileModel.getFile()).thenThrow(new FileNotFoundException());

        action.actionPerformed(actionEventMock);

        verify(ExceptionHandler.handle, times(1)).handle(instanceOf(FileNotFoundException.class), eq(UIMessage.ERROR_FILE_NOT_FOUND), eq("notFoundFile.txt"));
        verifyNoMoreInteractions(jTailMainModelMock);
        verifyNoMoreInteractions(dialogMock);
    }

    @Test
    public void testUpdateMethod() {
        JTailMainModel jTailMainModelMock = mock(JTailMainModel.class);
        OpenFileModel openFileModel = new OpenFileModel(new File("testFile.txt"), IndexType.FILE_BASED);
        IndexFileAction action = new IndexFileAction(jTailMainModelMock, openFileModel);

        FileIndexer fileIndexerMock = mock(FileIndexer.class);
        Observable observableMock = mock(Observable.class);

        when(observableMock.getObserverCount()).thenReturn(1);
        when(observableMock.addObserver(any(Observer.class))).thenReturn(true);

        FileIndexerWorker fileIndexerWorkerMock = mock(FileIndexerWorker.class);

        when(fileIndexerMock.getFile()).thenReturn(new File("testFile.txt"));
        when(fileIndexerMock.getIndex()).thenReturn(new FileIndex());

        doReturn(fileIndexerMock).when(action).createFileIndexer(eq(openFileModel.getFile()), eq(openFileModel.getIndexType()));
        doReturn(fileIndexerWorkerMock).when(action).createFileIndexerWorker(eq(fileIndexerMock));

        action.update(observableMock, 100);

        verify(jTailMainModelMock, times(1)).add(any(JTailModel.class));
        verifyNoMoreInteractions(jTailMainModelMock);
        verifyNoMoreInteractions(observableMock);
    }

    private static class ModelMatcher implements ArgumentMatcher<JTailModel> {
        @Override
        public boolean matches(JTailModel model) {
            return model != null;
        }
    }
}
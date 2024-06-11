package fr.pingtimeout.jtail.gui.action;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.Observable;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import fr.pingtimeout.jtail.controller.*;
import fr.pingtimeout.jtail.gui.action.IndexFileAction;
import fr.pingtimeout.jtail.gui.language.*;
import fr.pingtimeout.jtail.gui.message.*;
import fr.pingtimeout.jtail.gui.model.*;
import fr.pingtimeout.jtail.io.*;
import fr.pingtimeout.jtail.io.index.*;

public class IndexFileActionTest {

 private IndexFileAction indexFileAction;
 private JTailMainModel jTailMainModelMock;
 private OpenFileModel openFileModelMock;
 private Observer observerMock;
 private FileIndex indexerMock;
 private File fileMock;

 @Before
 public void setUp() {
 jTailMainModelMock = mock(JTailMainModel.class);
 openFileModelMock = mock(OpenFileModel.class);
 observerMock = mock(Observer.class);
 indexerMock = mock(FileIndex.class);
 fileMock = mock(File.class);

 indexFileAction = new IndexFileAction(jTailMainModelMock, openFileModelMock);
 }

 @Test
 public void testActionPerformedWithNullFile() {
    when(openFileModelMock.getFile()).thenReturn(null);
    indexFileAction.actionPerformed(mock(ActionEvent.class));
    verify(InformationHandler::handle, times(1)).handle(true, UIMessage.INFO_NO_FILE_SELECTED, new Object[0]);
 }

 @Test
 public void testActionPerformedWithValidFile() throws IOException {
    when(openFileModelMock.getFile()).thenReturn(fileMock);
    when(openFileModelMock.getIndexType()).thenReturn(FileIndexType.FILE_BASED);
    when(indexerMock.getClass()).thenReturn(RomFileIndex.class);
    when(indexerMock.addObserver(observerMock)).thenReturn(this);
    doThrow(new FileNotFoundException("Test error")).when(indexerMock).init(fileMock, RomFileIndex.class);

    indexFileAction.actionPerformed(mock(ActionEvent.class));

    verify(ExceptionHandler::handle, times(1)).handle(new FileNotFoundException("Test error"), UIMessage.ERROR_FILE_NOT_FOUND, fileMock.getName());
 }

 @Test
 public void testActionPerformedWithValidFileAndSuccessfulIndexing() throws IOException {
    when(openFileModelMock.getFile()).thenReturn(fileMock);
    when(openFileModelMock.getIndexType()).thenReturn(FileIndexType.FILE_BASED);
    when(indexerMock.getClass()).thenReturn(RomFileIndex.class);
    when(indexerMock.init(fileMock, RomFileIndex.class)).thenReturn(indexerMock);

    indexFileAction.actionPerformed(mock(ActionEvent.class));

    inOrder(jTailMainModelMock, fileIndexerMock).then(action -> action.add(any(JTailModel.class)))
            .then(action -> action.log("File : " + fileMock.getName()));
 }

 @Test
 public void testUpdateMethod() throws IOException {
    when(openFileModelMock.getFile()).thenReturn(fileMock);
    when(openFileModelMock.getIndexType()).thenReturn(FileIndexType.FILE_BASED);
    when(indexerMock.getClass()).thenReturn(RomFileIndex.class);
    when(indexerMock.init(fileMock, RomFileIndex.class)).thenReturn(indexerMock);

    indexFileAction.update(this, 100);

    inOrder(jTailMainModelMock).then(action -> action.add(any(JTailModel.class)));
 }
}
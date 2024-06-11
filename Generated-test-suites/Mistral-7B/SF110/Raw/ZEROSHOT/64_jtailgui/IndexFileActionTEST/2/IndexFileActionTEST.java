package fr.pingtimeout.jtail.gui.action;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.Observable;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import fr.pingtimeout.jtail.gui.controller.JTailMainModel;
import fr.pingtimeout.jtail.gui.message.UIMessage;
import fr.pingtimeout.jtail.gui.model.JTailModel;
import fr.pingtimeout.jtail.io.index.FileIndexer;
import fr.pingtimeout.jtail.io.index.FileIndexerWorker;
import fr.pingtimeout.jtail.io.index.RamFileIndex;
import fr.pingtimeout.jtail.io.index.RomFileIndex;
import fr.pingtimeout.jtail.util.JTailLogger;

public class IndexFileActionTest {

 private IndexFileAction action;
 private JTailMainModel jTailMainModelMock;
 private OpenFileModel openFileModelMock;
 private JDialog dialogMock;
 private FileIndexer fileIndexerMock;
 private Observable observableMock;

 @Before
 public void setUp() {
 jTailMainModelMock = mock(JTailMainModel.class);
 openFileModelMock = mock(OpenFileModel.class);
 dialogMock = mock(JDialog.class);
 fileIndexerMock = mock(FileIndexer.class);
 observableMock = mock(Observable.class);

 action = new IndexFileAction(jTailMainModelMock, openFileModelMock);
 }

 @Test
 public void testActionPerformedWithNullFile() {
 action.actionPerformed(new ActionEvent(dialogMock, 0, ""));
 verify(jTailMainModelMock, never()).add(any(JTailModel.class));
 verify(InformationHandlerMock, times(1)).handle(eq(true), eq(UIMessage.INFO_NO_FILE_SELECTED), anyArray());
 }

 @Test
 public void testActionPerformedWithValidFile() throws IOException {
 File file = new File("testFile.txt");
 when(openFileModelMock.getFile()).thenReturn(file);
 when(openFileModelMock.getIndexType()).thenReturn(IndexFileAction.FILE_BASED);
 when(fileIndexerMock.getFile()).thenReturn(file);
 when(fileIndexerMock.getClass()).thenReturn(RomFileIndex.class);
 when(observableMock.getObserverCount()).thenReturn(1);
 when(observableMock.notifyObservers(anyInt(), anyObject())).thenReturn(true);

 action.actionPerformed(new ActionEvent(dialogMock, 0, ""));

 ArgumentCaptor<JTailModel> modelCaptor = ArgumentCaptor.forClass(JTailModel.class);
 verify(jTailMainModelMock, times(1)).add(modelCaptor.capture());
 JTailModel capturedModel = modelCaptor.getValue();
 assertEquals(file, capturedModel.getFile());

 verify(fileIndexerMock, times(1)).addObserver(eq(action));
 FileIndexerWorker fileIndexerWorker = verify(fileIndexerMock, times(1)).startIndexing();
 verify(fileIndexerWorker).execute();
 }

 @Test
 public void testActionPerformedWithInvalidFileType() {
 when(openFileModelMock.getFile()).thenReturn(new File("testFile.txt"));
 when(openFileModelMock.getIndexType()).thenReturn(IndexFileAction.MEMORY_BASED);
 action.actionPerformed(new ActionEvent(dialogMock, 0, ""));
 verify(jTailMainModelMock, never()).add(any(JTailModel.class));
 verify(InformationHandlerMock, times(1)).handle(eq(true), eq(UIMessage.INFO_INVALID_INDEX_TYPE), anyArray());
 }

 @Test
 public void testUpdateWithSuccessIndexing() {
 File file = new File("testFile.txt");
 when(openFileModelMock.getFile()).thenReturn(file);
 when(openFileModelMock.getIndexType()).thenReturn(IndexFileAction.FILE_BASED);
 when(fileIndexerMock.getFile()).thenReturn(file);
 when(fileIndexerMock.getClass()).thenReturn(RomFileIndex.class);
 when(observableMock.getObserverCount()).thenReturn(1);
 when(observableMock.notifyObservers(anyInt(), anyObject())).thenReturn(true);

 action.update(observableMock, 100);
 verify(jTailMainModelMock, times(1)).add(any(JTailModel.class));
 }

 @Test
 public void testUpdateWithFailureIndexing() {
 when(openFileModelMock.getFile()).thenReturn(new File("testFile.txt"));
 when(openFileModelMock.getIndexType()).thenReturn(IndexFileAction.FILE_BASED);
 when(fileIndexerMock.getFile()).thenReturn(new File("testFile.txt"));
 when(fileIndexerMock.getClass()).thenReturn(RomFileIndex.class);
 when(observableMock.getObserverCount()).thenReturn(1);
 doThrow(new IOException("test error")).when(fileIndexerMock).startIndexing();

 action.update(observableMock, 100);
 verify(ExceptionHandlerMock, times(1)).handle(any(Exception.class), eq(UIMessage.ERROR_GENERIC_MESSAGE), anyArray());
 }
}
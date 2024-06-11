package fr.pingtimeout.jtail.gui.action;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Observable;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import fr.pingtimeout.jtail.gui.controller.JTailMainModel;
import fr.pingtimeout.jtail.gui.message.InformationHandler;
import fr.pingtimeout.jtail.gui.message.UIMessage;
import fr.pingtimeout.jtail.gui.model.JTailModel;
import fr.pingtimeout.jtail.gui.observer.Observer;
import fr.pingtimeout.jtail.io.index.FileIndex;
import fr.pingtimeout.jtail.io.index.FileIndexer;
import fr.pingtimeout.jtail.io.index.RamFileIndex;
import fr.pingtimeout.jtail.io.index.RomFileIndex;

public class IndexFileActionTest {

 private IndexFileAction indexFileAction;
 private JTailMainModel jTailMainModelMock;
 private OpenFileModel openFileModelMock;
 private FileIndexer fileIndexerMock;
 private FileIndex index;
 private Observer observerMock;
 private InformationHandler informationHandlerMock;

 @Before
 public void setup() {
 jTailMainModelMock = mock(JTailMainModel.class);
 openFileModelMock = mock(OpenFileModel.class);
 fileIndexerMock = mock(FileIndexer.class);
 observerMock = mock(Observer.class);
 informationHandlerMock = mock(InformationHandler.class);

 index = new File("testFile.txt");

 indexFileAction = new IndexFileAction(jTailMainModelMock, openFileModelMock);
 indexFileAction.addObserver(observerMock);
 }

 @Test
 public void testActionPerformedWithNullFile() {
 ActionEvent actionEvent = Mockito.mock(ActionEvent.class);
 when(openFileModelMock.getFile()).thenReturn(null);

 indexFileAction.actionPerformed(actionEvent);

 verify(informationHandlerMock, times(1)).handle(eq(true), eq(UIMessage.INFO_NO_FILE_SELECTED), isA(Object[].class));
 }

 @Test
 public void testActionPerformedWithValidFile() throws Exception {
 File file = new File("testFile.txt");
 when(openFileModelMock.getFile()).thenReturn(file);
 when(openFileModelMock.getIndexType()).thenReturn(IndexType.MEMORY_BASED);

 ActionEvent actionEvent = Mockito.mock(ActionEvent.class);
 JDialog dialogMock = Mockito.mock(JDialog.class);
 when(actionEvent.getSource()).thenReturn(dialogMock);
 doReturn(dialogMock).when(indexFileAction).findAncestorOfType(JDialog.class);

 indexFileAction.actionPerformed(actionEvent);

 ArgumentCaptor<FileIndexer> fileIndexerCaptor = ArgumentCaptor.forClass(FileIndexer.class);
 verify(fileIndexerMock, times(1)).addObserver(eq(observerMock));
 verify(fileIndexerMock, times(1)).start();

 FileIndexerWorker fileIndexerWorkerMock = mock(FileIndexerWorker.class);
 when(fileIndexerMock.createFileIndexerWorker()).thenReturn(fileIndexerWorkerMock);
 verify(fileIndexerWorkerMock, times(1)).execute();
 }

 @Test
 public void testActionPerformedWithFileAndFileBasedIndexType() throws Exception {
 File file = new File("testFile.txt");
 when(openFileModelMock.getFile()).thenReturn(file);
 when(openFileModelMock.getIndexType()).thenReturn(IndexType.FILE_BASED);

 ActionEvent actionEvent = Mockito.mock(ActionEvent.class);
 JDialog dialogMock = Mockito.mock(JDialog.class);
 when(actionEvent.getSource()).thenReturn(dialogMock);
 doReturn(dialogMock).when(indexFileAction).findAncestorOfType(JDialog.class);

 indexFileAction.actionPerformed(actionEvent);

 ArgumentCaptor<FileIndexer> fileIndexerCaptor = ArgumentCaptor.forClass(FileIndexer.class);
 verify(fileIndexerMock, times(1)).addObserver(eq(observerMock));
 verify(RomFileIndex.class, times(1)).newInstance(eq(file), eq(index), eq(observerMock));
 }

 @Test(expected = FileNotFoundException.class)
 public void testActionPerformedWithInvalidFile() throws Exception {
 File file = new File("testFileDoesNotExist.txt");
 when(openFileModelMock.getFile()).thenReturn(file);
 when(openFileModelMock.getIndexType()).thenReturn(IndexType.MEMORY_BASED);

 ActionEvent actionEvent = Mockito.mock(ActionEvent.class);
 JDialog dialogMock = Mockito.mock(JDialog.class);
 when(actionEvent.getSource()).thenReturn(dialogMock);
 doReturn(dialogMock).when(indexFileAction).findAncestorOfType(JDialog.class);

 indexFileAction.actionPerformed(actionEvent);

 verify(informationHandlerMock, times(1)).handle(eq(true), eq(UIMessage.ERROR_FILE_NOT_FOUND), eq(file.getName()));
 }

 @Test
 public void testUpdateWithFileIndexer() {
 FileIndexer fileIndexer = mock(FileIndexer.class);
 Observable observable = mock(Observable.class);
 when(observable.getObserverCount()).thenReturn(1);
 when(observable.setChanged(anyInt(), anyBoolean())).thenReturn(observable);
 when(fileIndexer.getFile()).thenReturn(index);

 indexFileAction.update(observable, 100);

 verify(jTailMainModelMock, times(1)).add(isA(JTailModel.class));
 }
}
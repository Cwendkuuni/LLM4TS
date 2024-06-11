package fr.pingtimeout.jtail.gui.action.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import fr.pingtimeout.jtail.gui.action.IndexFileAction;
import fr.pingtimeout.jtail.gui.controller.JTailMainModel;
import fr.pingtimeout.jtail.gui.message.UIMessage;
import fr.pingtimeout.jtail.gui.model.JTailModel;
import fr.pingtimeout.jtail.io.index.FileIndexer;
import fr.pingtimeout.jtail.io.FileIndexerWorker;
import fr.pingtimeout.jtail.io.index.FileIndexerObserver;
import fr.pingtimeout.jtail.io.index.RamFileIndex;
import fr.pingtimeout.jtail.io.index.RomFileIndex;
import fr.pingtimeout.jtail.util.JTailLogger;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Observable;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockitoAnnotations;

public class IndexFileActionTest {

 private IndexFileAction indexFileAction;
 private JTailMainModel jTailMainModelMock;
 private OpenFileModel openFileModelMock;
 private FileIndexerWorker fileIndexerWorkerMock;
 private FileIndexer fileIndexerMock;
 private FileIndexerObserver fileIndexerObserverMock;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 jTailMainModelMock = mock(JTailMainModel.class);
 openFileModelMock = mock(OpenFileModel.class);
 fileIndexerWorkerMock = mock(FileIndexerWorker.class);
 fileIndexerMock = mock(FileIndexer.class);
 fileIndexerObserverMock = mock(FileIndexerObserver.class);
 indexFileAction = new IndexFileAction(jTailMainModelMock, openFileModelMock);
 }

 @Test
 public void testActionPerformedWithValidFileAndIndexType() {
 // given
 when(openFileModelMock.getFile()).thenReturn(new File("test.txt"));
 when(openFileModelMock.getIndexType()).thenReturn(IndexType.FILE_BASED);
 when(fileIndexerMock.getClass()).thenReturn(RomFileIndex.class);

 ArgumentCaptor<ActionEvent> actionEventArgumentCaptor = ArgumentCaptor.forClass(ActionEvent.class);

 // when
 indexFileAction.actionPerformed(actionEventArgumentCaptor.capture());

 // then
 verify(jTailLogger).debug("File : test.txt");
 verify(jTailLogger).debug("Index type : FILE_BASED");
 verify(openFileModelMock).getFile();
 verify(openFileModelMock).getIndexType();
 verify(fileIndexerWorkerMock).execute();
 verify(fileIndexerMock).addObserver(fileIndexerObserverMock);

 FileIndexer expectedFileIndexer = new FileIndexer(new File("test.txt"), RomFileIndex.class);
 verify(fileIndexerMock).setFile(expectedFileIndexer.getFile());
 verify(fileIndexerMock).setIndexerClass(RomFileIndex.class);

 JTailModel expectedJTailModel = new JTailModel(expectedFileIndexer.getFile(), expectedFileIndexer.getIndex());
 assertEquals(1, jTailMainModelMock.getModels().size());
 assertEquals(expectedJTailModel, jTailMainModelMock.getModels().get(0));

 // no notification received yet
 verifyNoMoreInteractions(fileIndexerObserverMock);
 }

 @Test
 public void testActionPerformedWithNullFile() {
 // given
 when(openFileModelMock.getFile()).thenReturn(null);

 // when
 indexFileAction.actionPerformed(mock(ActionEvent.class));

 // then
 verify(jTailLogger).debug("File : null");
 verify(InformationHandler::handle).withArgument(eq(true)).withArgument(eq(UIMessage.INFO_NO_FILE_SELECTED));
 }

 @Test
 public void testActionPerformedWithInvalidIndexType() {
 // given
 when(openFileModelMock.getFile()).thenReturn(new File("test.txt"));
 when(openFileModelMock.getIndexType()).thenReturn(IndexType.INVALID);

 // when
 indexFileAction.actionPerformed(mock(ActionEvent.class));

 // then
 verify(jTailLogger).debug("File : test.txt");
 verify(jTailLogger).debug("Index type : INVALID");
 verify(ExceptionHandler::handle).withArgument(instanceOf(FileNotFoundException.class)).withArgument(eq(UIMessage.ERROR_FILE_NOT_FOUND)).withArgument(eq("test.txt"));
 }

 @Test
 public void testUpdateWithSuccessfulIndexCreation() {
 // given
 when(openFileModelMock.getFile()).thenReturn(new File("test.txt"));
 when(openFileModelMock.getIndexType()).thenReturn(IndexType.FILE_BASED);
 when(fileIndexerMock.getClass()).thenReturn(RomFileIndex.class);

 ArgumentCaptor<Observable> observableArgumentCaptor = ArgumentCaptor.forClass(Observable.class);

 // when
 indexFileAction.actionPerformed(mock(ActionEvent.class));
 fileIndexerWorkerMock.setStatus(100);
 indexFileAction.update(observableArgumentCaptor.capture(), anyObject());

 // then
 verify(fileIndexerObserverMock).update(eq(fileIndexerMock), eq(100));

 JTailModel expectedJTailModel = new JTailModel(fileIndexerMock.getFile(), fileIndexerMock.getIndex());
 assertEquals(1, jTailMainModelMock.getModels().size());
 assertEquals(expectedJTailModel, jTailMainModelMock.getModels().get(0));
 }

 @Test
 public void testUpdateWithFailedIndexCreation() {
 // given
 when(openFileModelMock.getFile()).thenReturn(new File("test.txt"));
 when(openFileModelMock.getIndexType()).thenReturn(IndexType.FILE_BASED);
 when(fileIndexerMock.getClass()).thenReturn(RomFileIndex.class);

 ArgumentCaptor<Observable> observableArgumentCaptor = ArgumentCaptor.forClass(Observable.class);

 // when
 indexFileAction.actionPerformed(mock(ActionEvent.class));
 fileIndexerWorkerMock.setStatus(101);
 indexFileAction.update(observableArgumentCaptor.capture(), anyObject());

 // then
 verify(ExceptionHandler::handle).withArgument(instanceOf(Exception.class)).withArgument(eq(UIMessage.ERROR_GENERIC_MESSAGE));
 }
}

enum IndexType {
 MEMORY_BASED,
 FILE_BASED,
 INVALID
}
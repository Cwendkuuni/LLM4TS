package fr.pingtimeout.jtail.gui.action;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import fr.pingtimeout.jtail.gui.controller.JTailMainModel;
import fr.pingtimeout.jtail.gui.message.InformationHandler;
import fr.pingtimeout.jtail.gui.message.UIMessage;
import fr.pingtimeout.jtail.gui.model.JTailModel;
import fr.pingtimeout.jtail.gui.observable.FileIndexer;
import fr.pingtimeout.jtail.gui.observable.FileIndexerWorker;
import fr.pingtimeout.jtail.gui.observable.FileIndexer.IndexType;
import fr.pingtimeout.jtail.io.index.FileIndexer;
import fr.pingtimeout.jtail.io.index.FileIndexerWorker.State;
import fr.pingtimeout.jtail.io.index.FileIndexerWorker.WorkerException;
import fr.pingtimeout.jtail.util.JTailLogger;

public class IndexFileActionTest {

 private IndexFileAction indexFileAction;
 @Mock
 private JTailMainModel jTailMainModel;
 @Mock
 private OpenFileModel openFileModel;
 @Mock
 private InformationHandler informationHandler;
 @Mock
 private FileIndexer fileIndexer;
 @Mock
 private FileIndexerWorker fileIndexerWorker;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
 when(openFileModel.getFile()).thenReturn(new File("testFile.txt"));
 when(openFileModel.getIndexType()).thenReturn(IndexType.FILE_BASED);
 }

 @Test
 public void testActionPerformedWithValidFileAndIndexType() {
 ActionEvent actionEvent = mock(ActionEvent.class);
 when(openFileModel.getFile()).thenReturn(new File("testFile.txt"));
 when(openFileModel.getIndexType()).thenReturn(IndexType.FILE_BASED);

 indexFileAction.actionPerformed(actionEvent);

 verify(jTailLogger, times(2)).debug(anyString());
 verify(informationHandler, times(0)).handle(anyBoolean(), eq(UIMessage.INFO_NO_FILE_SELECTED), anyArray());

 FileIndexer indexerClass = mock(FileIndexer.class);
 when(indexerClass.getClass()).thenReturn(RomFileIndex.class);

 FileIndexerWorker fileIndexerWorkerSpy = spy(new FileIndexerWorker(fileIndexer));
 doReturn(fileIndexerWorker).when(fileIndexer).createWorker();
 doReturn(State.WAITING).when(fileIndexerWorker).getState();

 indexFileAction.actionPerformed(actionEvent);

 verify(fileIndexer).addObserver(eq(indexFileAction));
 verify(fileIndexerWorker).execute();

 FileIndex file = new File("testFile.txt");
 when(fileIndexer.getFile()).thenReturn(file);

 FileIndexerWorker workerSpy = spy(fileIndexerWorker);
 doReturn(workerSpy).when(fileIndexer).createWorker();
 doReturn(State.COMPLETED).when(workerSpy).getState();
 doThrow(new WorkerException("Test Exception")).when(workerSpy).join();

 indexFileAction.actionPerformed(actionEvent);

 verify(jTailMainModel, times(1)).add(any(JTailModel.class));
 }

 @Test
 public void testActionPerformedWithNullFile() {
 ActionEvent actionEvent = mock(ActionEvent.class);

 indexFileAction.actionPerformed(actionEvent);

 verify(informationHandler, times(1)).handle(eq(true), eq(UIMessage.INFO_NO_FILE_SELECTED), anyArray());
 }

 @Test(expected = FileNotFoundException.class)
 public void testActionPerformedWithInvalidFile() {
 ActionEvent actionEvent = mock(ActionEvent.class);
 when(openFileModel.getFile()).thenReturn(new File("testFileDoesNotExist.txt"));

 indexFileAction.actionPerformed(actionEvent);
 }
}
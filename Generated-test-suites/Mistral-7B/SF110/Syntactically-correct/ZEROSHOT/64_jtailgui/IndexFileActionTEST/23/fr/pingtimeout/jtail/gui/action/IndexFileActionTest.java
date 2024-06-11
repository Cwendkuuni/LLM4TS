package fr.pingtimeout.jtail.gui.action;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import fr.pingtimeout.jtail.gui.controller.JTailMainModel;
import fr.pingtimeout.jtail.gui.model.JTailModel;
import fr.pingtimeout.jtail.gui.message.UIMessage;
import fr.pingtimeout.jtail.io.FileIndexer;
import fr.pingtimeout.jtail.io.FileIndexerWorker;
import fr.pingtimeout.jtail.io.index.FileIndexerObserver;
import fr.pingtimeout.jtail.io.index.RamFileIndex;
import fr.pingtimeout.jtail.io.index.RomFileIndex;

public class IndexFileActionTest {

 private IndexFileAction indexFileAction;
 private JTailMainModel jTailMainModelMock;
 private OpenFileModel openFileModelMock;
 private JDialog dialogMock;

 @Before
 public void setUp() {
  jTailMainModelMock = mock(JTailMainModel.class);
  openFileModelMock = mock(OpenFileModel.class);
  dialogMock = mock(JDialog.class);

  indexFileAction = new IndexFileAction(jTailMainModelMock, openFileModelMock);
 }

 @Test
 public void testActionPerformedWithNullFile() {
  ActionEvent actionEvent = mock(ActionEvent.class);

  indexFileAction.actionPerformed(actionEvent);

  verify(InformationHandler::handle, times(1)).handle(eq(true), eq(UIMessage.INFO_NO_FILE_SELECTED), isA(Object[].class));
 }

 @Test
 public void testActionPerformedWithValidFileAndIndexType() throws IOException {
  ActionEvent actionEvent = mock(ActionEvent.class);
  File file = new File("test.txt");
  when(openFileModelMock.getFile()).thenReturn(file);
  when(openFileModelMock.getIndexType()).thenReturn(IndexType.FILE_BASED);

  indexFileAction.actionPerformed(actionEvent);

  verify(JTailLogger::debug, times(1)).debug("File : {}", eq(file));
  verify(JTailLogger::debug, times(1)).debug("Index type : {}", eq(IndexType.FILE_BASED));

  Class<? extends FileIndex> indexerClass = RomFileIndex.class;
  FileIndexer fileIndexer = Mockito.spy(new FileIndexer(file, indexerClass));
  fileIndexer.addObserver(any(FileIndexerObserver.class));

  doNothing().when(fileIndexer).addObserver(any(FileIndexerObserver.class));

  FileIndexerWorker fileIndexerWorker = new FileIndexerWorker(fileIndexer);
  fileIndexerWorker.execute();

  verify(jTailMainModelMock, times(1)).add(any(JTailModel.class));
 }

 @Test
 public void testActionPerformedWithInvalidIndexType() {
  ActionEvent actionEvent = mock(ActionEvent.class);
  when(openFileModelMock.getFile()).thenReturn(new File("test.txt"));
  when(openFileModelMock.getIndexType()).thenReturn(IndexType.INVALID);

  indexFileAction.actionPerformed(actionEvent);

  verify(ExceptionHandler::handle, times(1)).handle(any(Exception.class), eq(UIMessage.ERROR_INVALID_INDEX_TYPE), isA(Object[].class));
 }

 @Test
 public void testUpdateMethodWithSuccessfulIndexing() throws IOException {
  File file = new File("test.txt");
  when(openFileModelMock.getFile()).thenReturn(file);
  when(openFileModelMock.getIndexType()).thenReturn(IndexType.FILE_BASED);

  FileIndexer fileIndexer = Mockito.spy(new FileIndexer(file, RomFileIndex.class));
  fileIndexer.addObserver(any(FileIndexerObserver.class));

  doNothing().when(fileIndexer).addObserver(any(FileIndexerObserver.class));

  FileIndexerWorker fileIndexerWorker = new FileIndexerWorker(fileIndexer);
  fileIndexerWorker.execute();

  indexFileAction.update(fileIndexer, 100);

  verify(jTailMainModelMock, times(1)).add(any(JTailModel.class));
 }

 @Test
 public void testUpdateMethodWithFailedIndexing() throws IOException {
  File file = new File("test.txt");
  when(openFileModelMock.getFile()).thenReturn(file);
  when(openFileModelMock.getIndexType()).thenReturn(IndexType.FILE_BASED);

  FileIndexer fileIndexer = Mockito.spy(new FileIndexer(file, RomFileIndex.class));
  doThrow(IOException.class).when(fileIndexer).addObserver(any(FileIndexerObserver.class));

  indexFileAction.update(fileIndexer, 100);

  verify(ExceptionHandler::handle, times(1)).handle(any(Exception.class), eq(UIMessage.ERROR_FILE_NOT_FOUND), eq(file.getName()));
 }
}

enum IndexType {
 FILE_BASED,
 MEMORY_BASED,
 INVALID
}
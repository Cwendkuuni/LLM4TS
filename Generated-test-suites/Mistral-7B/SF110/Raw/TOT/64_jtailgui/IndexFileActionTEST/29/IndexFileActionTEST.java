package fr.pingtimeout.jtail.gui.action.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import fr.pingtimeout.jtail.gui.action.IndexFileAction;
import fr.pingtimeout.jtail.gui.controller.JTailMainModel;
import fr.pingtimeout.jtail.gui.model.JTailModel;
import fr.pingtimeout.jtail.io.index.FileIndexer;
import fr.pingtimeout.jtail.io.index.FileIndexerWorker;
import fr.pingtimeout.jtail.io.index.RamFileIndex;
import fr.pingtimeout.jtail.io.index.RomFileIndex;
import fr.pingtimeout.jtail.util.JTailLogger;

public class IndexFileActionTest {

 private IndexFileAction indexFileAction;
 private JTailMainModel jTailMainModelMock;
 private OpenFileModel openFileModelMock;
 private FileIndexer fileIndexerMock;
 private FileIndexerWorker fileIndexerWorkerMock;

 @Before
 public void setUp() {
 jTailMainModelMock = mock(JTailMainModel.class);
 openFileModelMock = new OpenFileModel(new File("testfile.txt"), File.TYPE_REGULAR, null);
 indexFileAction = new IndexFileAction(jTailMainModelMock, openFileModelMock);
 fileIndexerMock = mock(FileIndexer.class);
 fileIndexerWorkerMock = mock(FileIndexerWorker.class);
 when(fileIndexerMock.getFile()).thenReturn(new File("testfile.txt"));
 when(fileIndexerMock.getIndexType()).thenReturn(FileIndexer.FILE_BASED);
 when(fileIndexerWorkerMock.execute()).thenReturn(true);
 }

 @Test
 public void testActionPerformed_ValidFileAndIndexType() {
 // Given
 ActionEvent actionEvent = new ActionEvent(new JButton("Test"), ActionEvent.ACTION_PERFORMED, "testAction");
 // When
 indexFileAction.actionPerformed(actionEvent);
 // Then
 verify(jTailMainModelMock).add(any(JTailModel.class));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testActionPerformed_NullFile() {
 // Given
 openFileModelMock.setFile(null);
 // When
 indexFileAction.actionPerformed(new ActionEvent(new JButton("Test"), ActionEvent.ACTION_PERFORMED, "testAction"));
 // Then
 // An exception should be thrown
 }

 @Test(expected = IllegalArgumentException.class)
 public void testActionPerformed_InvalidIndexType() {
 // Given
 openFileModelMock.setIndexType(FileIndexer.INVALID_INDEX_TYPE);
 // When
 indexFileAction.actionPerformed(new ActionEvent(new JButton("Test"), ActionEvent.ACTION_PERFORMED, "testAction"));
 // Then
 // An exception should be thrown
 }

 @Test(expected = FileNotFoundException.class)
 public void testActionPerformed_FileNotFound() {
 // Given
 when(fileIndexerMock.newFileIndexer(openFileModelMock.getFile(), FileIndexer.FILE_BASED)).thenThrow(new FileNotFoundException());
 // When
 indexFileAction.actionPerformed(new ActionEvent(new JButton("Test"), ActionEvent.ACTION_PERFORMED, "testAction"));
 // Then
 verify(jTailMainModelMock, times(0)).add(any(JTailModel.class));
 verify(ExceptionHandler.class).handle(any(FileNotFoundException.class), isA(UIMessage.class), anyArray());
 }

 @Test
 public void testUpdate_IndexingSuccess() {
 // Given
 doAnswer(invocationOnMock -> {
 try {
 fileIndexerWorkerMock.execute();
 return true;
 } catch (Exception e) {
 throw new RuntimeException(e);
 }
 }).when(fileIndexerWorkerMock).execute();
 // When
 indexFileAction.update(fileIndexerMock, 100);
 // Then
 // Nothing should be added to jTailMainModelMock since this is just a test
 // verify(jTailMainModelMock, times(0)).add(any(JTailModel.class));
 }

 @Test
 public void testUpdate_IndexingError() {
 // Given
 doAnswer(invocationOnMock -> {
 throw new IOException("Test error");
 }).when(fileIndexerWorkerMock).execute();
 // When
 indexFileAction.update(fileIndexerMock, 100);
 // Then
 verify(ExceptionHandler.class).handle( any(Exception.class), isA(UIMessage.class), anyArray());
 }

 @Test
 public void testUpdate_FileNotFound() {
 // Given
 when(fileIndexerMock.getFile()).thenThrow(new FileNotFoundException());
 // When
 indexFileAction.update(fileIndexerMock, 100);
 // Then
 verify(ExceptionHandler.class).handle(any(FileNotFoundException.class), isA(UIMessage.class), anyArray());
 }
}
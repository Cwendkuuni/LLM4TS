package fr.pingtimeout.jtail.gui.action.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.Observable;

import org.junit.Before;
import org.junit.Test;

import fr.pingtimeout.jtail.gui.action.IndexFileAction;
import fr.pingtimeout.jtail.gui.controller.JTailMainModel;
import fr.pingtimeout.jtail.gui.message.UIMessage;
import fr.pingtimeout.jtail.gui.model.JTailModel;
import fr.pingtimeout.jtail.io.index.FileIndexer;
import fr.pingtimeout.jtail.io.index.FileIndexerWorker;
import fr.pingtimeout.jtail.io.index.RamFileIndex;
import fr.pingtimeout.jtail.io.index.RomFileIndex;

public class IndexFileActionTest {

 private IndexFileAction indexFileAction;
 private JTailMainModel jTailMainModelMock;
 private OpenFileModel openFileModelMock;
 private FileIndexer fileIndexerMock;
 private FileIndexerWorker fileIndexerWorkerMock;

 @Before
 public void setUp() {
 jTailMainModelMock = mock(JTailMainModel.class);
 openFileModelMock = new OpenFileModel(null, null);
 indexFileAction = new IndexFileAction(jTailMainModelMock, openFileModelMock);
 fileIndexerMock = mock(FileIndexer.class);
 fileIndexerWorkerMock = mock(FileIndexerWorker.class);
 }

 @Test
 public void testActionPerformed_ValidFile_Success() throws IOException {
 // given
 File file = new File("test.txt");
 openFileModelMock.setFile(file);
 openFileModelMock.setIndexType(IndexType.FILE_BASED);
 when(fileIndexerMock.newFileIndexer(file, RamFileIndex.class)).thenReturn(fileIndexerMock);
 when(fileIndexerWorkerMock.execute()).thenReturn(true);

 // when
 indexFileAction.actionPerformed(new ActionEvent(new JButton(), ActionEvent.ACTION_PERFORMED, "test"));

 // then
 verify(jTailMainModelMock).add(any(JTailModel.class));
 verifyNoMoreInteractions(ExceptionHandler.class);
 }

 @Test
 public void testActionPerformed_NullFile_ErrorHandling() throws IOException {
 // given
 openFileModelMock.setFile(null);

 // when
 indexFileAction.actionPerformed(new ActionEvent(new JButton(), ActionEvent.ACTION_PERFORMED, "test"));

 // then
 verifyZeroInteractions(jTailMainModelMock);
 verify(InformationHandler.class).handle(eq(true), any(UIMessage.class), anyArray());
 }

 @Test
 public void testActionPerformed_InvalidIndexType_ErrorHandling() {
 // given
 openFileModelMock.setFile(new File("test.txt"));
 openFileModelMock.setIndexType(IndexType.INVALID);

 // when
 indexFileAction.actionPerformed(new ActionEvent(new JButton(), ActionEvent.ACTION_PERFORMED, "test"));

 // then
 verifyZeroInteractions(jTailMainModelMock);
 verify(ExceptionHandler.class).handle(any(Exception.class), eq(UIMessage.ERROR_INVALID_INDEX_TYPE), anyArray());
 }

 @Test
 public void testUpdate_FileIndexerSuccess_AddModelToJTailMainModel() throws IOException {
 // given
 when(fileIndexerMock.getFile()).thenReturn(new File("test.txt"));
 FileIndexer indexer = new FileIndexer(new File("test.txt"), RamFileIndex.class);
 when(fileIndexerMock.addObserver(any())).thenAnswer(invocation -> indexer);
 when(fileIndexIndexerWorkerMock.execute()).thenReturn(true);

 // when
 indexFileAction.update(fileIndexerMock, 100);

 // then
 verify(jTailMainModelMock).add(any(JTailModel.class));
 }

 @Test
 public void testUpdate_FileIndexerFailure_ErrorHandling() throws IOException {
 // given
 when(fileIndexerMock.getFile()).thenReturn(new File("test.txt"));
 when(fileIndexerMock.addObserver(any())).thenAnswer(invocation -> fileIndexerMock);
 when(fileIndexIndexerWorkerMock.execute()).thenReturn(false);

 // when
 indexFileAction.update(fileIndexerMock, 100);
 // then
 verifyZeroInteractions(jTailMainModelMock);
 verify(ExceptionHandler.class).handle(eq(fileIndexerMock.getException()), any(UIMessage.class), anyArray());
 }
}
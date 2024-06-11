package fr.pingtimeout.jtail.gui.action.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import fr.pingtimeout.jtail.gui.action.IndexFileAction;
import fr.pingtimeout.jtail.gui.action.OpenFileModel;
import fr.pingtimeout.jtail.gui.controller.JTailMainModel;
import fr.pingtimeout.jtail.gui.message.UIMessage;
import fr.pingtimeout.jtail.io.FileIndexer;
import fr.pingtimeout.jtail.io.FileIndexerWorker;
import fr.pingtimeout.jtail.io.FileIndexer.IndexType;
import fr.pingtimeout.jtail.io.index.FileIndex;
import fr.pingtimeout.jtail.io.index.RamFileIndex;
import fr.pingtimeout.jtail.io.index.RomFileIndex;
import fr.pingtimeout.jtail.io.line.LineReader;
import fr.pingtimeout.jtail.model.JTailModel;

public class IndexFileActionTest {

 private IndexFileAction action;
 private JTailMainModel jTailMainModelMock;
 private OpenFileModel openFileModelMock;

 @Before
 public void setUp() {
 jTailMainModelMock = mock(JTailMainModel.class);
 openFileModelMock = new OpenFileModel();
 action = new IndexFileAction(jTailMainModelMock, openFileModelMock);
 }

 @Test
 public void testActionPerformed_ValidFileAndIndexType_AddsModelToJTailMainModel() throws IOException {
 File file = Mockito.mock(File.class);
 when(openFileModelMock.getFile()).thenReturn(file);
 when(openFileModelMock.getIndexType()).thenReturn(IndexType.FILE_BASED);
 FileIndex index = Mockito.mock(FileIndex.class);
 FileIndexer fileIndexer = Mockito.mock(FileIndexer.class);
 when(fileIndexer.getIndex()).thenReturn(index);
 FileIndexerWorker fileIndexerWorker = Mockito.mock(FileIndexerWorker.class);
 when(fileIndexerWorker.execute()).thenReturn(true);
 when(fileIndexer.addObserver(any())).thenReturn(true);

 action.actionPerformed(new ActionEvent(new JButton(), 0, "Open File"));

 verify(jTailMainModelMock).add(argThat(m -> m instanceof JTailModel));
 }

 @Test(expected = IllegalStateException.class)
 public void testActionPerformed_NullFile_ThrowsException() {
 openFileModelMock.setFile(null);
 action.actionPerformed(new ActionEvent(new JButton(), 0, "Open File"));
 }

 @Test
 public void testActionPerformed_InvalidIndexType_DoesNotAddModelToJTailMainModel() throws IOException {
 openFileModelMock.setFile(Mockito.mock(File.class));
 openFileModelMock.setIndexType(IndexType.MEMORY_BASED);
 FileIndex index = Mockito.mock(FileIndex.class);
 FileIndexer fileIndexer = Mockito.mock(FileIndexer.class);
 when(fileIndexer.getIndex()).thenReturn(index);
 FileIndexerWorker fileIndexerWorker = Mockito.mock(FileIndexerWorker.class);
 when(fileIndexerWorker.execute()).thenReturn(true);
 when(fileIndexer.addObserver(any())).thenReturn(true);

 action.actionPerformed(new ActionEvent(new JButton(), 0, "Open File"));

 verify(jTailMainModelMock, never()).add(any());
 }

 @Test(expected = FileNotFoundException.class)
 public void testActionPerformed_FileNotFound_HandlesException() throws IOException {
 File file = Mockito.mock(File.class);
 when(openFileModelMock.getFile()).thenReturn(file);
 when(openFileModelMock.getIndexType()).thenReturn(IndexType.FILE_BASED);
 FileIndex index = Mockito.mock(FileIndex.class);
 FileIndexer fileIndexer = Mockito.mock(FileIndexer.class);
 when(fileIndexer.getIndex()).thenReturn(index);
 Exception exception = Mockito.mock(IOException.class);
 when(fileIndexer.addObserver(any())).thenThrow(exception);

 action.actionPerformed(new ActionEvent(new JButton(), 0, "Open File"));

 verify(ExceptionHandler.class).handle(argThat(e -> e instanceof IOException), eq(UIMessage.ERROR_FILE_NOT_FOUND), equalTo(file.getName()));
 }

 @Test
 public void testUpdate_FileIndexingFinished_AddsModelToJTailMainModel() {
 File file = Mockito.mock(File.class);
 FileIndex index = Mockito.mock(FileIndex.class);
 JTailModel jTailModel = Mockito.mock(JTailModel.class);
 when(openFileModelMock.getFile()).thenReturn(file);
 when(openFileModelMock.getIndexType()).thenReturn(IndexType.FILE_BASED);
 FileIndexer fileIndexer = Mockito.mock(FileIndexer.class);
 when(fileIndexer.getIndex()).thenReturn(index);
 when(fileIndexer.addObserver(any())).thenReturn(true);

 action.update(Mockito.mock(Observable.class), 100);

 verify(jTailMainModelMock).add(jTailModel);
 }

 @Test(expected = IllegalStateException.class)
 public void testUpdate_CalledBeforeFileIndexingStarted_ThrowsException() {
 action.update(Mockito.mock(Observable.class), 100);
 }
}
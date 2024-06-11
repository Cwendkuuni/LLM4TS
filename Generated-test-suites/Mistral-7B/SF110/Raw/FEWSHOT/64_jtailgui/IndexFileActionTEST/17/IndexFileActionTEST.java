package fr.pingtimeout.jtail.gui.action;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import fr.pingtimeout.jtail.gui.action.IndexFileAction;
import fr.pingtimeout.jtail.gui.controller.JTailMainModel;
import fr.pingtimeout.jtail.gui.model.FileIndexerWorker;
import fr.pingtimeout.jtail.gui.model.OpenFileModel;
import fr.pingtimeout.jtail.gui.message.UIMessage;
import fr.pingtimeout.jtail.io.FileIndexer;
import fr.pingtimeout.jtail.io.FileIndexer.FileIndexType;
import fr.pingtimeout.jtail.io.index.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class IndexFileActionTest {

 private OpenFileModel openFileModel;
 private JTailMainModel jTailMainModel;
 private IndexFileAction indexFileAction;
 private FileIndexer fileIndexer;
 private FileIndexType indexType;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 openFileModel = mock(OpenFileModel.class);
 jTailMainModel = mock(JTailMainModel.class);
 indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
 fileIndexer = mock(FileIndexer.class);
 indexType = FileIndexType.FILE_BASED;
 }

 @Test
 public void testActionPerformed_FileAndIndexTypeNotNull() {
 when(openFileModel.getFile()).thenReturn(new File("test.txt"));
 when(openFileModel.getIndexType()).thenReturn(indexType);

 indexFileAction.actionPerformed(mock(ActionEvent.class));

 verify(jTailMainModel).add(any(JTailModel.class));
 }

 @Test(expected = NullPointerException.class)
 public void testActionPerformed_FileIsNull() {
 when(openFileModel.getFile()).thenReturn(null);

 indexFileAction.actionPerformed(mock(ActionEvent.class));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testActionPerformed_InvalidIndexType() {
 when(openFileModel.getIndexType()).thenReturn(FileIndexType.INVALID);

 indexFileAction.actionPerformed(mock(ActionEvent.class));
 }

 @Test
 public void testActionPerformed_FileNotFoundException() {
 when(openFileModel.getFile()).thenReturn(new File("test.txt"));
 when(fileIndexer.addObserver(any())).thenThrow(new FileNotFoundException("File not found"));
 when(openFileModel.getIndexType()).thenReturn(indexType);

 indexFileAction.actionPerformed(mock(ActionEvent.class));

 verify(jTailLogger).debug("File : test.txt");
 verify(jTailLogger).debug("Index type : FILE_BASED");
 verifyZeroInteractions(jTailMainModel);
 }

 @Test
 public void testUpdate_FileIndexerCreated() {
 when(openFileModel.getFile()).thenReturn(new File("test.txt"));
 when(openFileModel.getIndexType()).thenReturn(indexType);
 when(fileIndexer.getFile()).thenReturn(new File("test.txt"));
 when(fileIndexer.getIndex()).thenReturn(new Index());

 FileIndexerWorker fileIndexerWorker = mock(FileIndexerWorker.class);
 doReturn(fileIndexerWorker).when(indexFileAction).new FileIndexerWorker(fileIndexer);

 indexFileAction.update(fileIndexer, 100);

 verify(fileIndexerWorker).execute();
 }

 @Test
 public void testUpdate_FileNotFoundException() {
 when(openFileModel.getFile()).thenReturn(new File("test.txt"));
 when(openFileModel.getIndexType()).thenReturn(indexType);
 when(fileIndexer.getFile()).thenThrow(new FileNotFoundException("File not found"));

 indexFileAction.update(fileIndexer, 100);

 verifyZeroInteractions(jTailMainModel);
 }

 @Test
 public void testUpdate_ExceptionThrown() {
 when(openFileModel.getFile()).thenReturn(new File("test.txt"));
 when(openFileModel.getIndexType()).thenReturn(indexType);
 when(fileIndexer.getFile()).thenReturn(new File("test.txt"));
 when(fileIndexer.getIndex()).thenThrow(new IOException("Exception thrown"));

 indexFileAction.update(fileIndexer, 100);

 verifyZeroInteractions(jTailMainModel);
 }
}
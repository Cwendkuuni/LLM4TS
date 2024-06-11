package fr.pingtimeout.jtail.gui.action;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JDialog;
import javax.swing.SwingUtilities;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import fr.pingtimeout.jtail.controller.JTailMainController;
import fr.pingtimeout.jtail.gui.JTailMainModel;
import fr.pingtimeout.jtail.gui.message.UIMessage;
import fr.pingtimeout.jtail.gui.model.JTailModel;
import fr.pingtimeout.jtail.io.FileIndexer;
import fr.pingtimeout.jtail.io.FileIndexerWorker;
import fr.pingtimeout.jtail.io.FileIndexer.IndexType;
import fr.pingtimeout.jtail.io.index.FileIndex;
import fr.pingtimeout.jtail.io.index.RamFileIndex;
import fr.pingtimeout.jtail.io.index.RomFileIndex;
import fr.pingtimeout.jtail.util.JTailLogger;

public class IndexFileActionTest {

 private IndexFileAction indexFileAction;
 private JTailMainController jTailMainControllerMock;
 private JTailMainModel jTailMainModelMock;
 private OpenFileModel openFileModelMock;
 private FileIndex indexMock;
 private FileIndexerWorker fileIndexerWorkerMock;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 jTailMainControllerMock = mock(JTailMainController.class);
 jTailMainModelMock = mock(JTailMainModel.class);
 openFileModelMock = mock(OpenFileModel.class);
 indexMock = mock(FileIndex.class);
 fileIndexerWorkerMock = mock(FileIndexerWorker.class);

 indexFileAction = new IndexFileAction(jTailMainModelMock, openFileModelMock);
 }

 @Test
 public void testActionPerformed_ValidFileAndIndexType() {
 File file = new File("testFile.txt");
 when(openFileModelMock.getFile()).thenReturn(file);
 when(openFileModelMock.getIndexType()).thenReturn(IndexType.FILE_BASED);

 JDialog dialogMock = mock(JDialog.class);

 ActionEvent actionEventMock = mock(ActionEvent.class);
 when(actionEventMock.getSource()).thenReturn(dialogMock);

 doAnswer(new Answer<Void>() {
 @Override
 public Void answer(InvocationOnMock invocation) throws Throwable {
 SwingUtilities.invokeLater(() -> indexFileAction.actionPerformed(actionEventMock));
 return null;
 }
 }).answer(fileIndexerWorkerMock.execute());

 FileIndexer fileIndexerMock = mock(FileIndexer.class);
 when(fileIndexerMock.getFile()).thenReturn(file);
 when(fileIndexerMock.addObserver(any())).thenReturn(fileIndexerMock);
 when(fileIndexerWorkerMock.getFileIndexer()).thenReturn(fileIndexerMock);

 doNothing().when(jTailMainModelMock).add(any());

 indexFileAction.actionPerformed(actionEventMock);

 verify(jTailMainModelMock).add(fileIndexerMock);
 }

 @Test(expected = NullPointerException.class)
 public void testActionPerformed_NullFile() {
 openFileModelMock.getFile = null;
 indexFileAction.actionPerformed(new ActionEvent(new JButton(), ActionEvent.ACTION_PERFORMED, "test"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testActionPerformed_InvalidIndexType() {
 when(openFileModelMock.getFile()).thenReturn(new File("testFile.txt"));
 when(openFileModelMock.getIndexType()).thenReturn(null);
 indexFileAction.actionPerformed(new ActionEvent(new JButton(), ActionEvent.ACTION_PERFORMED, "test"));
 }

 @Test(expected = FileNotFoundException.class)
 public void testActionPerformed_FileNotFound() throws IOException {
 when(openFileModelMock.getFile()).thenReturn(new File("testFile.txt"));
 when(openFileModelMock.getIndexType()).thenReturn(IndexType.FILE_BASED);
 doThrow(new FileNotFoundException("test")).when(fileIndexerMock).init(anyString(), any(Class.class));
 indexFileAction.actionPerformed(new ActionEvent(new JButton(), ActionEvent.ACTION_PERFORMED, "test"));
 }

 @Test
 public void testUpdate_IndexingCompleted() {
 File file = new File("testFile.txt");
 when(openFileModelMock.getFile()).thenReturn(file);
 when(openFileModelMock.getIndexType()).thenReturn(IndexType.FILE_BASED);
 when(fileIndexerMock.getFile()).thenReturn(file);
 when(fileIndexerMock.getIndex()).thenReturn(indexMock);
 when(jTailMainModelMock.getFileCount()).thenReturn(0);
 indexFileAction.update(fileIndexerMock, 100);
 assertEquals(1, jTailMainModelMock.getFileCount());
 }
}
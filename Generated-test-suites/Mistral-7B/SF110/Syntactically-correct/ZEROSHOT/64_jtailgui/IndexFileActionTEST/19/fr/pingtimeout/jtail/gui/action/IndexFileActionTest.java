package fr.pingtimeout.jtail.gui.action;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.event.*;
import java.io.*;
import java.util.*;

import org.junit.*;
import org.mockito.*;

import fr.pingtimeout.jtail.gui.controller.*;
import fr.pingtimeout.jtail.gui.message.*;
import fr.pingtimeout.jtail.gui.model.*;
import fr.pingtimeout.jtail.io.index.*;
import fr.pingtimeout.jtail.io.*;

public class IndexFileActionTest {

 private IndexFileAction indexFileAction;
 private JTailMainModel jTailMainModelMock;
 private OpenFileModel openFileModelMock;
 private JDialog dialogMock;
 private JFrame jFrameMock;
 private File fileMock;
 private FileIndexer fileIndexerMock;
 private FileIndexerWorker fileIndexerWorkerMock;

 @Before
 public void setUp() {
 jTailMainModelMock = mock(JTailMainModel.class);
 openFileModelMock = mock(OpenFileModel.class);
 fileIndexerMock = mock(FileIndexer.class);
 fileIndexerWorkerMock = mock(FileIndexerWorker.class);

 indexFileAction = new IndexFileAction(jTailMainModelMock, openFileModelMock);
 }

 @Test
 public void testActionPerformedWithValidFileAndIndexType() {
 // Given
 when(openFileModelMock.getFile()).thenReturn(fileMock);
 when(openFileModelMock.getIndexType()).thenReturn(IndexType.FILE_BASED);

 // When
 indexFileAction.actionPerformed(mock(ActionEvent.class));

 // Then
 verify(jTailMainModelMock).add(any(JTailModel.class));
 verify(fileIndexerMock).addObserver(eq(indexFileAction));
 verifyNoMoreInteractions(jTailMainModelMock);
 verifyNoMoreInteractions(fileIndexerMock);

 // When executing the worker
 doAnswer(invocation -> {
 FileIndexer fileIndexer = (FileIndexer) invocation.getArguments()[0];
 fileIndexer.indexFile();
 return null;
 }).when(fileIndexerWorkerMock).doWork();
 indexFileAction.fileIndexerWorker = fileIndexerWorkerMock;

 indexFileAction.actionPerformed(mock(ActionEvent.class));

 // Then the model is added to JTailMainModel
 verify(jTailMainModelMock).add(any(JTailModel.class));
 // The fileIndexer is indexed
 verify(fileIndexerMock).indexFile();
 }

 @Test
 public void testActionPerformedWithNullFile() {
 // Given
 when(openFileModelMock.getFile()).thenReturn(null);

 // When
 indexFileAction.actionPerformed(mock(ActionEvent.class));

 // Then nothing happens
 verifyNoInteractions(jTailMainModelMock);
 verifyNoInteractions(fileIndexerMock);
 }

 @Test
 public void testActionPerformedWithInvalidIndexType() {
 // Given
 when(openFileModelMock.getIndexType()).thenReturn(IndexType.INVALID);

 // When
 indexFileAction.actionPerformed(mock(ActionEvent.class));

 // Then nothing happens
 verifyNoInteractions(jTailMainModelMock);
 verifyNoInteractions(fileIndexerMock);
 }

 @Test
 public void testUpdateMethod() {
 // Given
 final int progress = 100;

 // When
 indexFileAction.update(fileIndexerMock, progress);

 // Then the JTailModel is added to JTailMainModel
 verify(jTailMainModelMock).add(any(JTailModel.class));
 }

 @Test
 public void testUpdateMethodWithNullFileIndexer() {
 // Given
 final int progress = 100;

 // When
 indexFileAction.update(null, progress);

 // Then nothing happens
 verifyNoInteractions(jTailMainModelMock);
 }

 @Test
 public void testUpdateMethodWithWrongProgress() {
 // Given
 final int progress = -1;

 // When
 indexFileAction.update(fileIndexerMock, progress);

 // Then nothing happens
 verifyNoInteractions(jTailMainModelMock);
 }
}
package fr.pingtimeout.jtail.gui.action;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.Observable;

import fr.pingtimeout.jtail.controller.*;
import fr.pingtimeout.jtail.gui.message.*;
import fr.pingtimeout.jtail.gui.model.*;
import fr.pingtimeout.jtail.io.index.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class IndexFileActionTest {

 private IndexFileAction indexFileAction;
 private JTailMainModel jTailMainModelMock;
 private OpenFileModel openFileModelMock;
 private FileIndexer fileIndexerMock;
 private FileIndexerWorker fileIndexerWorkerMock;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 jTailMainModelMock = mock(JTailMainModel.class);
 openFileModelMock = mock(OpenFileModel.class);
 fileIndexerMock = mock(FileIndexer.class);
 fileIndexerWorkerMock = mock(FileIndexerWorker.class);
 indexFileAction = new IndexFileAction(jTailMainModelMock, openFileModelMock);
 }

 @Test
 public void testActionPerformedWithValidFileAndIndexType() {
 String filePath = "/path/to/file.txt";
 String indexType = "FILE_BASED";
 when(openFileModelMock.getFile()).thenReturn(new File(filePath));
 when(openFileModelMock.getIndexType()).thenReturn(indexType);
 when(FileIndexer.class.getName()).thenReturn(RomFileIndex.class.getName());

 indexFileAction.actionPerformed(mock(ActionEvent.class));

 verify(jTailMainModelMock).add(any(JTailModel.class));
 verify(fileIndexerMock).addObserver(indexFileAction);
 verify(fileIndexerWorkerMock).execute();
 }

 @Test
 public void testActionPerformedWithNullFile() {
 indexFileAction.actionPerformed(mock(ActionEvent.class));

 verifyZeroInteractions(jTailMainModelMock);
 verifyZeroInteractions(fileIndexerMock);
 verifyZeroInteractions(fileIndexerWorkerMock);
 }

 @Test
 public void testUpdateWithIndexingCompleted() throws IOException {
 File file = mock(File.class);
 FileIndex index = mock(FileIndex.class);
 JTailModel jTailModel = mock(JTailModel.class);
 when(fileIndexerMock.getFile()).thenReturn(file);
 when(fileIndexerMock.getIndex()).thenReturn(index);
 when(new LineReader(file, index).readLine()).thenReturn("Line content");

 indexFileAction.update(fileIndexerMock, 100);

 verify(jTailMainModelMock).add(jTailModel);
 }

 @Test
 public void testUpdateWithFileNotFound() {
 File file = mock(File.class);
 when(fileIndexerMock.getFile()).thenReturn(file);
 doThrow(FileNotFoundException.class).when(fileIndexerMock).addObserver(any());

 indexFileAction.update(fileIndexerMock, 100);

 verifyZeroInteractions(jTailMainModelMock);
 }

 @Test
 public void testUpdateWithGenericException() {
 File file = mock(File.class);
 when(fileIndexerMock.getFile()).thenReturn(file);
 doThrow(new Exception()).when(fileIndexerMock).addObserver(any());

 indexFileAction.update(fileIndexerMock, 100);

 verifyZeroInteractions(jTailMainModelMock);
 }
}
package fr.pingtimeout.jtail.gui.action;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import fr.pingtimeout.jtail.gui.action.IndexFileAction;
import fr.pingtimeout.jtail.gui.controller.JTailMainModel;
import fr.pingtimeout.jtail.gui.model.OpenFileModel;
import fr.pingtimeout.jtail.gui.message.UIMessage;
import fr.pingtimeout.jtail.io.index.FileIndexer;
import fr.pingtimeout.jtail.io.FileIndexerWorker;
import fr.pingtimeout.jtail.io.index.FileIndexerObserver;
import fr.pingtimeout.jtail.io.FileIndexer.IndexType;
import fr.pingtimeout.jtail.io.index.RamFileIndex;
import fr.pingtimeout.jtail.io.index.RomFileIndex;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.Observable;

public class IndexFileActionTest {

 private JTailMainModel jTailMainModelMock;
 private OpenFileModel openFileModelMock;
 private IndexFileAction indexFileAction;

 @Before
 public void setUp() {
 jTailMainModelMock = mock(JTailMainModel.class);
 openFileModelMock = mock(OpenFileModel.class);
 indexFileAction = new IndexFileAction(jTailMainModelMock, openFileModelMock);
 }

 @Test
 public void testActionPerformedWithNullFile() {
 // Given
 when(openFileModelMock.getFile()).thenReturn(null);

 // When
 indexFileAction.actionPerformed(new ActionEvent(new JButton(), 0, ""));

 // Then
 verifyNoInteractions(jTailMainModelMock);
 }

 @Test
 public void testActionPerformedWithValidFileAndIndexType() throws IOException {
 // Given
 File file = new File("test.txt");
 when(openFileModelMock.getFile()).thenReturn(file);
 when(openFileModelMock.getIndexType()).thenReturn(IndexType.FILE_BASED);
 FileIndexer fileIndexer = mock(FileIndexer.class);
 FileIndexerWorker fileIndexerWorker = mock(FileIndexerWorker.class);
 doReturn(fileIndexer).when(indexFileAction).newFileIndexer(file, RomFileIndex.class);
 doReturn(fileIndexerWorker).when(indexFileAction).newFileIndexerWorker(fileIndexer);

 // When
 indexFileAction.actionPerformed(new ActionEvent(new JButton(), 0, ""));

 // Then
 verify(fileIndexerWorker).execute();
 }

 @Test(expected = FileNotFoundException.class)
 public void testActionPerformedWithInvalidFile() throws IOException {
 // Given
 File file = new File("test.txt");
 when(openFileModelMock.getFile()).thenReturn(file);
 when(openFileModelMock.getIndexType()).thenReturn(IndexType.FILE_BASED);
 doThrow(new FileNotFoundException("Test error")).when(indexFileAction).newFileIndexer(file, RomFileIndex.class);

 // When
 indexFileAction.actionPerformed(new ActionEvent(new JButton(), 0, ""));

 // Then
 // An exception is expected
 }

 @Test
 public void testUpdateMethod() throws IOException {
 // Given
 File file = new File("test.txt");
 when(openFileModelMock.getFile()).thenReturn(file);
 when(openFileModelMock.getIndexType()).thenReturn(IndexType.FILE_BASED);
 FileIndexer fileIndexer = mock(FileIndexer.class);
 doReturn(fileIndexer).when(indexFileAction).newFileIndexer(file, RomFileIndex.class);
 FileIndexerObserver fileIndexerObserverMock = mock(FileIndexerObserver.class);
 doReturn(fileIndexerObserverMock).when(jTailMainModelMock).getFileIndexerObserver();

 // When
 indexFileAction.update(new Observable("fileIndexer"), 100);

 // Then
 verify(fileIndexerObserverMock).fileIndexed(fileIndexer);
 }
}
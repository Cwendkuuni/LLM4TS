import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import fr.pingtimeout.jtail.gui.action.IndexFileAction;
import fr.pingtimeout.jtail.gui.controller.JTailMainModel;
import fr.pingtimeout.jtail.gui.model.JTailModel;
import fr.pingtimeout.jtail.io.index.FileIndexer;
import fr.pingtimeout.jtail.io.index.FileIndexerWorker;
import fr.pingtimeout.jtail.io.index.RamFileIndex;
import fr.pingtimeout.jtail.io.index.RomFileIndex;
import fr.pingtimeout.jtail.io.index.FileIndexer.IndexType;

public class IndexFileActionTest {

 private IndexFileAction indexFileAction;
 private JTailMainModel jTailMainModelMock;
 private OpenFileModel openFileModelMock;
 private FileIndexerWorker fileIndexerWorkerMock;

 @Before
 public void setUp() {
 jTailMainModelMock = mock(JTailMainModel.class);
 openFileModelMock = new OpenFileModel(null, IndexType.MEMORY_BASED);
 indexFileAction = new IndexFileAction(jTailMainModelMock, openFileModelMock);
 fileIndexerWorkerMock = mock(FileIndexerWorker.class);
 }

 @Test
 public void testActionPerformedWithValidFileAndIndexType() {
 // Given
 File file = Mockito.mock(File.class);
 openFileModelMock.setFile(file);
 openFileModelMock.setIndexType(IndexType.MEMORY_BASED);
 ArgumentCaptor<FileIndexer> indexerCaptor = ArgumentCaptor.forClass(FileIndexer.class);

 // When
 indexFileAction.actionPerformed(new ActionEvent(new JButton(), ActionEvent.ACTION_PERFORMED, "test"));

 // Then
 verify(fileIndexerWorkerMock).execute();
 verify(jTailMainModelMock).add(Mockito.any(JTailModel.class));
 }

 @Test(expected = NullPointerException.class)
 public void testActionPerformedWithNullFile() {
 // Given
 openFileModelMock.setFile(null);
 openFileModelMock.setIndexType(IndexType.MEMORY_BASED);

 // When
 indexFileAction.actionPerformed(new ActionEvent(new JButton(), ActionEvent.ACTION_PERFORMED, "test"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testActionPerformedWithInvalidIndexType() {
 // Given
 openFileModelMock.setFile(new File("test.txt"));
 openFileModelMock.setIndexType(null);

 // When
 indexFileAction.actionPerformed(new ActionEvent(new JButton(), ActionEvent.ACTION_PERFORMED, "test"));
 }

 @Test(expected = FileNotFoundException.class)
 public void testActionPerformedWithNonExistentFile() throws FileNotFoundException {
 // Given
 File nonExistentFile = new File("nonExistentFile.txt");
 openFileModelMock.setFile(nonExistentFile);
 openFileModelMock.setIndexType(IndexType.FILE_BASED);

 // When
 indexFileAction.actionPerformed(new ActionEvent(new JButton(), ActionEvent.ACTION_PERFORMED, "test"));
 }

 @Test
 public void testUpdateWithSuccessfulIndexing() throws IOException, InterruptedException {
 // Given
 File file = new File("test.txt");
 openFileModelMock.setFile(file);
 openFileModelMock.setIndexType(IndexType.MEMORY_BASED);
 Mockito.when(fileIndexerWorkerMock.getFileIndexer()).thenReturn(new RamFileIndex(file));

 // When
 indexFileAction.actionPerformed(new ActionEvent(new JButton(), ActionEvent.ACTION_PERFORMED, "test"));

 // Then
 verify(jTailMainModelMock).add(Mockito.any(JTailModel.class));
 }

 @Test
 public void testUpdateWithFailedIndexing() throws IOException, InterruptedException {
 // Given
 File file = new File("test.txt");
 openFileModelMock.setFile(file);
 openFileModelMock.setIndexType(IndexType.FILE_BASED);
 Mockito.when(fileIndexerWorkerMock.getFileIndexer()).thenThrow(new IOException("Test error"));

 // When
 indexFileAction.actionPerformed(new ActionEvent(new JButton(), ActionEvent.ACTION_PERFORMED, "test"));

 // Then
 // No JTailModel added to the JTailMainModel
 verify(jTailMainModelMock, Mockito.never()).add(Mockito.any(JTailModel.class));
 }
}
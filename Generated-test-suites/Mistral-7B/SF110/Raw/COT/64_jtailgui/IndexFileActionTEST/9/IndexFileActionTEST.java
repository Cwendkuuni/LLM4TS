import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import fr.pingtimeout.jtail.gui.action.IndexFileAction;
import fr.pingtimeout.jtail.gui.controller.JTailMainModel;
import fr.pingtimeout.jtail.gui.message.UIMessage;
import fr.pingtimeout.jtail.gui.model.JTailModel;
import fr.pingtimeout.jtail.io.index.FileIndexer;
import fr.pingtimeout.jtail.io.index.FileIndexerWorker;
import fr.pingtimeout.jtail.io.index.RamFileIndex;
import fr.pingtimeout.jtail.io.index.RomFileIndex;
import fr.pingtimeout.jtail.util.JTailLogger;
import fr.pingtimeout.jtail.util.ResourceBundleWrapper;

public class IndexFileActionTest {

 private IndexFileAction indexFileAction;
 private JTailMainModel jTailMainModelMock;
 private OpenFileModel openFileModelMock;
 private ResourceBundle bundleMock;
 private JDialog dialogMock;

 @Before
 public void setUp() {
 bundleMock = Mockito.mock(ResourceBundle.class);
 openFileModelMock = Mockito.mock(OpenFileModel.class);
 jTailMainModelMock = Mockito.mock(JTailMainModel.class);
 dialogMock = Mockito.mock(JDialog.class);

 indexFileAction = new IndexFileAction(jTailMainModelMock, openFileModelMock);
 }

 @Test
 public void testActionPerformedWithValidFileAndIndexType() {
 // Given
 Mockito.when(openFileModelMock.getFile()).thenReturn(new File("test.txt"));
 Mockito.when(openFileModelMock.getIndexType()).thenReturn(IndexType.MEMORY_BASED);

 // When
 indexFileAction.actionPerformed(new ActionEvent(dialogMock, 0, "test"));

 // Then
 Mockito.verify(jTailMainModelMock).add(Mockito.any(JTailModel.class));
 }

 @Test(expected = NullPointerException.class)
 public void testActionPerformedWithNullFile() {
 // Given
 Mockito.when(openFileModelMock.getFile()).thenReturn(null);

 // When
 indexFileAction.actionPerformed(new ActionEvent(dialogMock, 0, "test"));

 // Then
 // An exception should be thrown
 }

 @Test(expected = IllegalArgumentException.class)
 public void testActionPerformedWithInvalidIndexType() {
 // Given
 Mockito.when(openFileModelMock.getIndexType()).thenReturn(null);

 // When
 indexFileAction.actionPerformed(new ActionEvent(dialogMock, 0, "test"));

 // Then
 // An exception should be thrown
 }

 @Test
 public void testUpdateWithValidFileIndexer() {
 // Given
 FileIndexer fileIndexerMock = Mockito.mock(FileIndexer.class);
 Mockito.when(openFileModelMock.getFile()).thenReturn(new File("test.txt"));
 Mockito.when(openFileModelMock.getIndexType()).thenReturn(IndexType.FILE_BASED);
 Mockito.when(new FileIndexer(Mockito.any(File.class), Mockito.eq(RomFileIndex.class))).thenReturn(fileIndexerMock);

 // When
 indexFileAction.update(Mockito.any(Observable.class), Mockito.eq(100));

 // Then
 Mockito.verify(jTailMainModelMock).add(Mockito.any(JTailModel.class));
 }

 @Test(expected = FileNotFoundException.class)
 public void testUpdateWithFileNotFoundException() {
 // Given
 Mockito.when(openFileModelMock.getFile()).thenReturn(new File("test.txt"));
 Mockito.when(openFileModelMock.getIndexType()).thenReturn(IndexType.FILE_BASED);
 Mockito.when(new FileIndexer(Mockito.any(File.class), Mockito.eq(RomFileIndex.class))).thenThrow(new FileNotFoundException());

 // When
 indexFileAction.update(Mockito.any(Observable.class), Mockito.eq(100));

 // Then
 // An exception should be thrown
 }

 @Test
 public void testUpdateWithGenericException() {
 // Given
 FileIndexer fileIndexerMock = Mockito.mock(FileIndexer.class);
 Mockito.when(openFileModelMock.getFile()).thenReturn(new File("test.txt"));
 Mockito.when(openFileModelMock.getIndexType()).thenReturn(IndexType.FILE_BASED);
 Mockito.when(new FileIndexer(Mockito.any(File.class), Mockito.eq(RomFileIndex.class))).thenAnswer(invocationOnMock -> { throw new Exception(); });

 // When
 indexFileAction.update(Mockito.any(Observable.class), Mockito.eq(100));

 // Then
 // An exception should be handled
 Mockito.verify(JTailLogger.class).debug("Notification received : 100 emitted by FileIndexer");
 }
}

enum IndexType {
 MEMORY_BASED,
 FILE_BASED,
}
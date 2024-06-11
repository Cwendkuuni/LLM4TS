import org.junit.Test;
import org.junit.Assert;
import static org.mockito.Mockito.*;
import fr.pingtimeout.jtail.gui.action.IndexFileAction;
import fr.pingtimeout.jtail.gui.model.JTailMainModel;
import fr.pingtimeout.jtail.gui.model.OpenFileModel;
import fr.pingtimeout.jtail.io.index.FileIndex;
import fr.pingtimeout.jtail.io.FileIndexer;
import fr.pingtimeout.jtail.io.index.RamFileIndex;
import fr.pingtimeout.jtail.io.index.RomFileIndex;
import java.awt.event.ActionEvent;
import java.io.File;

public class IndexFileActionTest {

 private JTailMainModel jTailMainModelMock;
 private OpenFileModel openFileModelMock;

 @Test
 public void testIndexFileActionConstruction() {
 IndexFileAction indexFileAction = new IndexFileAction(jTailMainModelMock, openFileModelMock);
 Assert.assertNotNull(indexFileAction);
 }

 @Test
 public void testActionPerformedNullFile() {
 openFileModelMock = mock(OpenFileModel.class);
 jTailMainModelMock = mock(JTailMainModel.class);
 IndexFileAction indexFileAction = new IndexFileAction(jTailMainModelMock, openFileModelMock);
 ActionEvent actionEvent = mock(ActionEvent.class);
 doNothing().when(indexFileAction).actionPerformed(actionEvent);

 openFileModelMock.setFile(null);
 indexFileAction.actionPerformed(actionEvent);

 verify(jTailMainModelMock, never()).add(any(JTailModel.class));
 }

 @Test
 public void testActionPerformedNullIndexType() {
 openFileModelMock = mock(OpenFileModel.class);
 jTailMainModelMock = mock(JTailMainModel.class);
 IndexFileAction indexFileAction = new IndexFileAction(jTailMainModelMock, openFileModelMock);
 ActionEvent actionEvent = mock(ActionEvent.class);
 doNothing().when(indexFileAction).actionPerformed(actionEvent);

 openFileModelMock.setFile(new File("testFile"));
 openFileModelMock.setIndexType(null);
 indexFileAction.actionPerformed(actionEvent);

 verify(jTailMainModelMock, never()).add(any(JTailModel.class));
 }

 @Test
 public void testActionPerformedInvalidIndexType() {
 openFileModelMock = mock(OpenFileModel.class);
 jTailMainModelMock = mock(JTailMainModel.class);
 IndexFileAction indexFileAction = new IndexFileAction(jTailMainModelMock, openFileModelMock);
 ActionEvent actionEvent = mock(ActionEvent.class);
 doNothing().when(indexFileAction).actionPerformed(actionEvent);

 openFileModelMock.setFile(new File("testFile"));
 openFileModelMock.setIndexType("INVALID_TYPE");
 indexFileAction.actionPerformed(actionEvent);

 verify(jTailMainModelMock, never()).add(any(JTailModel.class));
 }

 @Test
 public void testActionPerformedValidFileAndIndex() {
 openFileModelMock = mock(OpenFileModel.class);
 jTailMainModelMock = mock(JTailMainModel.class);
 IndexFileAction indexFileAction = new IndexFileAction(jTailMainModelMock, openFileModelMock);
 ActionEvent actionEvent = mock(ActionEvent.class);
 doNothing().when(indexFileAction).actionPerformed(actionEvent);

 File file = new File("testFile");
 openFileModelMock.setFile(file);
 openFileModelMock.setIndexType(FileIndexer.FILE_BASED);

 FileIndex indexer = null;
 try {
 indexer = new FileIndexer(file, RomFileIndex.class);
 } catch (FileNotFoundException e) {
 e.printStackTrace();
 }

 when(openFileModelMock.getIndexType()).thenReturn(FileIndexer.FILE_BASED);
 when(indexer.addObserver(any())).thenAnswer(invocation -> invocation.getArgument(0));
 FileIndexerWorker fileIndexerWorkerMock = mock(FileIndexerWorker.class);
 doNothing().when(fileIndexerWorkerMock).execute();
 indexFileAction.actionPerformed(actionEvent);

 FileIndexer fileIndexer = indexer;
 JTailModel jTailModelMock = mock(JTailModel.class);
 jTailMainModelMock.add(jTailModelMock);
 verify(jTailMainModelMock).add(argThat(jTailModel -> jTailModel.getFile().equals(file)));
 }

 @Test
 public void testUpdateNullFileIndexer() {
 IndexFileAction indexFileAction = new IndexFileAction();
 Observable observableMock = mock(Observable.class);
 Observer observerMock = mock(Observer.class);
 indexFileAction.update(observableMock, null);
 verify(observerMock, never()).update(any(), anyInt(), any(FileIndex.class));
 }

 @Test
 public void testUpdateFileIndexer() {
 IndexFileAction indexFileAction = new IndexFileAction();
 Observable observableMock = mock(Observable.class);
 Observer observerMock = mock(Observer.class);
 indexFileAction.update(observableMock, 100, mock(FileIndex.class));
 verify(observerMock).update(eq(indexFileAction), anyInt(), any(FileIndex.class));
 }

}
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import fr.pingtimeout.jtail.gui.action.IndexFileAction;
import fr.pingtimeout.jtail.gui.action.JTailAbstractAction;
import fr.pingtimeout.jtail.gui.model.JTailMainModel;
import fr.pingtimeout.jtail.gui.model.OpenFileModel;
import fr.pingtimeout.jtail.io.FileIndexer;
import fr.pingtimeout.jtail.io.FileIndexerWorker;
import fr.pingtimeout.jtail.io.index.FileIndex;
import fr.pingtimeout.jtail.io.index.RamFileIndex;
import fr.pingtimeout.jtail.io.index.RomFileIndex;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.Observable;

public class IndexFileActionTest {

 private IndexFileAction indexFileAction;
 private JTailMainModel jTailMainModelMock;
 private OpenFileModel openFileModelMock;
 private Observable oMock;
 private FileIndex indexMock;

 @InjectMocks
 private IndexFileAction testee;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 jTailMainModelMock = Mockito.mock(JTailMainModel.class);
 openFileModelMock = Mockito.mock(OpenFileModel.class);
 oMock = Mockito.mock(Observable.class);
 indexMock = Mockito.mock(FileIndex.class);

 indexFileAction = new IndexFileAction(jTailMainModelMock, openFileModelMock);
 testee = new IndexFileAction(jTailMainModelMock, openFileModelMock) {
 @Override
 public void update(Observable observable, Object arg) {
 super.update(observable, arg);
 Mockito.verify(jTailMainModelMock).add(Mockito.any());
 }
 };
 }

 @Test
 public void testActionPerformed_FileAndIndexTypeProvided_ExecutesFileIndexerWorkerAndAddsModelToJTailMainModel() throws IOException {
 File file = Mockito.mock(File.class);
 Mockito.when(openFileModelMock.getFile()).thenReturn(file);
 Mockito.when(openFileModelMock.getIndexType()).thenReturn(IndexType.FILE_BASED);
 FileIndexer fileIndexer = Mockito.mock(FileIndexer.class);
 FileIndexerWorker fileIndexerWorker = Mockito.mock(FileIndexerWorker.class);

 Mockito.when(fileIndexer.addObserver(Mockito.any())).thenReturn(oMock);
 Mockito.when(fileIndexer.getFile()).thenReturn(file);
 Mockito.when(fileIndexer.getIndex()).thenReturn(indexMock);
 Mockito.when(new FileIndexer(file, RomFileIndex.class)).thenReturn(fileIndexer);

 testee.actionPerformed(new ActionEvent(Mockito.mock(Component.class), 0, "test", "test", 0, 0));

 Mockito.verify(jTailMainModelMock).add(Mockito.any());
 Mockito.verify(fileIndexerWorker).execute();
 }

 @Test
 public void testActionPerformed_NoFileSelected_HandlesNoFileSelectedException() {
 openFileModelMock.setFile(null);

 ActionEvent actionEvent = new ActionEvent(Mockito.mock(Component.class), 0, "test", "test", 0, 0);

 testee.actionPerformed(actionEvent);

 Mockito.verifyNoInteractions(jTailMainModelMock);
 }

 @Test(expected = FileNotFoundException.class)
 public void testActionPerformed_InvalidFile_ThrowsFileNotFoundException() throws IOException {
 File file = Mockito.mock(File.class);
 Mockito.when(openFileModelMock.getFile()).thenReturn(file);
 Mockito.when(openFileModelMock.getIndexType()).thenReturn(IndexType.FILE_BASED);
 Mockito.when(oMock.getObserverPosition(Mockito.eq(testee))).thenReturn(-1);

 Mockito.doThrow(new FileNotFoundException("test")).when(fileIndexer).addObserver(Mockito.any());

 testee.actionPerformed(new ActionEvent(Mockito.mock(Component.class), 0, "test", "test", 0, 0));
 }
}

enum IndexType {
 MEMORY_BASED,
 FILE_BASED,
}
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import fr.pingtimeout.jtail.gui.action.IndexFileAction;
import fr.pingtimeout.jtail.gui.controller.JTailMainModel;
import fr.pingtimeout.jtail.gui.model.JTailModel;
import fr.pingtimeout.jtail.gui.message.UIMessage;
import fr.pingtimeout.jtail.io.FileIndexer;
import fr.pingtimeout.jtail.io.FileIndexerWorker;
import fr.pingtimeout.jtail.io.index.FileIndex;
import fr.pingtimeout.jtail.io.index.RamFileIndex;
import fr.pingtimeout.jtail.io.index.RomFileIndex;

public class IndexFileActionTest {

 private IndexFileAction action;
 private JTailMainModel jTailMainModelMock;
 private OpenFileModel openFileModelMock;
 private FileIndex indexMock;
 private FileIndexerWorker fileIndexerWorkerMock;

 @Before
 public void setUp() {
 jTailMainModelMock = mock(JTailMainModel.class);
 openFileModelMock = mock(OpenFileModel.class);
 indexMock = mock(FileIndex.class);
 fileIndexerWorkerMock = mock(FileIndexerWorker.class);
 action = new IndexFileAction(jTailMainModelMock, openFileModelMock);
 }

 @Test
 public void testActionPerformedWithNullFile() {
 openFileModelMock.setFile(null);
 action.actionPerformed(Mockito.mock(ActionEvent.class));
 verify(InformationHandler.class).handle(true, UIMessage.INFO_NO_FILE_SELECTED, new Object[0]);
 }

 @Test
 public void testActionPerformedWithValidFileAndIndex() {
 openFileModelMock.setFile(Mockito.mock(File.class));
 openFileModelMock.setIndexType(IndexFileAction.IndexType.FILE_BASED);
 Mockito.when(openFileModelMock.getFile()).thenReturn(Mockito.mock(File.class));
 Mockito.when(openFileModelMock.getIndexType()).thenReturn(IndexFileAction.IndexType.FILE_BASED);
 Mockito.when(indexMock.getFile()).thenReturn(Mockito.mock(File.class));

 action.actionPerformed(Mockito.mock(ActionEvent.class));

 verify(JTailLogger.class).debug("File : {}", Mockito.eq(openFileModelMock.getFile()));
 verify(JTailLogger.class).debug("Index type : {}", Mockito.eq(IndexFileAction.IndexType.FILE_BASED));

 ArgumentCaptor<Observer> observerArgumentCaptor = ArgumentCaptor.forClass(Observer.class);
 verify(fileIndexerWorkerMock).addObserver(observerArgumentCaptor.capture());
 verify(fileIndexerWorkerMock).execute();

 Observer capturedObserver = observerArgumentCaptor.getValue();
 verify(this.jTailMainModelMock).add(Mockito.isA(JTailModel.class));

 FileIndexer fileIndexer = Mockito.mock(FileIndexer.class);
 verify(fileIndexerWorkerMock).getFileIndexer().thenReturn(fileIndexer);
 Mockito.when(fileIndexer.getFile()).thenReturn(openFileModelMock.getFile());
 Mockito.when(fileIndexer.getIndex()).thenReturn(indexMock);

 capturedObserver.update(fileIndexer, 100);
 }

 @Test
 public void testActionPerformedWithInvalidFileAndMemoryBasedIndex() {
 openFileModelMock.setFile(Mockito.mock(File.class));
 openFileModelMock.setIndexType(IndexFileAction.IndexType.MEMORY_BASED);
 Mockito.when(openFileModelMock.getFile()).thenThrow(new FileNotFoundException());

 action.actionPerformed(Mockito.mock(ActionEvent.class));

 verify(ExceptionHandler.class).handle(Mockito.isInstanceOf(FileNotFoundException.class), UIMessage.ERROR_FILE_NOT_FOUND, openFileModelMock.getFile().getName());
 }

 @Test
 public void testActionPerformedWithInvalidFileAndFileBasedIndex() {
 openFileModelMock.setFile(Mockito.mock(File.class));
 openFileModelMock.setIndexType(IndexFileAction.IndexType.FILE_BASED);
 Mockito.when(openFileModelMock.getFile()).thenThrow(new FileNotFoundException());

 try {
 FileIndexer fileIndexer = new FileIndexer(openFileModelMock.getFile(), RamFileIndex.class);
 }
 catch (FileNotFoundException e) {
 // expected
 }
 }

 @Test(expected = IllegalStateException.class)
 public void testActionPerformedWithNullIndexerClass() {
 openFileModelMock.setFile(Mockito.mock(File.class));
 openFileModelMock.setIndexType(IndexFileAction.IndexType.FILE_BASED);

 action.actionPerformed(Mockito.mock(ActionEvent.class));
 }
}
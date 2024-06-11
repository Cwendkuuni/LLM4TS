import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import fr.pingtimeout.jtail.ControllerMock;
import fr.pingtimeout.jtail.GUIMock;
import fr.pingtimeout.jtail.ModelMock;
import fr.pingtimeout.jtail.io.FileIndexerMock;
import fr.pingtimeout.jtail.io.FileIndexerWorkerMock;
import fr.pingtimeout.jtail.io.index.RomFileIndex;
import fr.pingtimeout.jtail.gui.action.IndexFileAction;
import fr.pingtimeout.jtail.gui.language.ResourceBundle;

public class IndexFileActionTest {

 private IndexFileAction indexFileAction;
 private ResourceBundle bundle;
 private ControllerMock controllerMock;
 private GUIMock guiMock;
 private ModelMock modelMock;
 private FileIndexerMock fileIndexerMock;
 private FileIndexerWorkerMock fileIndexerWorkerMock;

 @Before
 public void setUp() {
 this.bundle = mock(ResourceBundle.class);
 this.controllerMock = new ControllerMock();
 this.guiMock = new GUIMock();
 this.modelMock = new ModelMock();
 this.fileIndexerMock = new FileIndexerMock();
 this.fileIndexerWorkerMock = new FileIndexerWorkerMock();

 this.indexFileAction = new IndexFileAction(this.controllerMock, this.modelMock, this.bundle, this.guiMock, this.fileIndexerMock, this.fileIndexerWorkerMock);
 }

 @Test
 public void testActionPerformed_withValidFileAndIndexType_indexesFile() throws IOException {
 // Given
 File file = new File("test.txt");
 this.openFileModel.setFile(file);
 this.openFileModel.setIndexType(FileIndexAction.FILE_BASED);

 ArgumentCaptor<FileIndexer> indexerArgumentCaptor = ArgumentCaptor.forClass(FileIndexer.class);
 this.fileIndexerMock.setFile(file);
 this.fileIndexerMock.setIndexType(RomFileIndex.class);
 this.fileIndexerWorkerMock.setFileIndexer(this.fileIndexerMock);
 this.controllerMock.setFileIndexerWorker(this.fileIndexerWorkerMock);

 // When
 indexFileAction.actionPerformed(new ActionEvent(this.guiMock, 0, "test"));

 // Then
 verify(this.fileIndexerMock).addObserver(this.indexFileAction);
 verify(this.controllerMock).addModel(this.modelMock);
 }

 @Test
 public void testActionPerformed_withNullFile_displaysErrorMessage() {
 // Given
 this.openFileModel.setFile(null);

 // When
 indexFileAction.actionPerformed(new ActionEvent(this.guiMock, 0, "test"));

 // Then
 verify(this.informationHandlerMock).handle(true, UIMessage.INFO_NO_FILE_SELECTED, new Object[0]);
 }

 @Test
 public void testActionPerformed_withInvalidIndexType_displaysErrorMessage() {
 // Given
 this.openFileModel.setFile(new File("test.txt"));
 this.openFileModel.setIndexType(-1);

 // When
 indexFileAction.actionPerformed(new ActionEvent(this.guiMock, 0, "test"));

 // Then
 verify(this.exceptionHandlerMock).handle(new IOException("Invalid index type"), UIMessage.ERROR_INVALID_INDEX_TYPE, "test.txt");
 }

 @Test
 public void testUpdate_whenIndexingIsSuccessful_addsModelToJTailMainModel() {
 // Given
 this.openFileModel.setFile(new File("test.txt"));
 this.openFileModel.setIndexType(FileIndexAction.FILE_BASED);

 ArgumentCaptor<FileIndexer> indexerArgumentCaptor = ArgumentCaptor.forClass(FileIndexer.class);
 this.fileIndexerMock.setFile(new File("test.txt"));
 this.fileIndexerMock.setIndexType(RomFileIndex.class);
 this.fileIndexerWorkerMock.setFileIndexer(this.fileIndexerMock);
 this.controllerMock.setFileIndexerWorker(this.fileIndexerWorkerMock);
 this.modelMock.setFile(new File("test.txt"));
 this.modelMock.setLines(new ArrayList<>());

 indexFileAction.actionPerformed(new ActionEvent(this.guiMock, 0, "test"));
 verify(this.controllerMock).addModel(this.modelMock);
 }

 @Test
 public void testUpdate_whenIndexingFails_displaysErrorMessage() {
 // Given
 this.openFileModel.setFile(new File("test.txt"));
 this.openFileModel.setIndexType(FileIndexAction.FILE_BASED);
 this.fileIndexerMock.setFile(new File("test.txt"));
 this.fileIndexerMock.setIndexType(RomFileIndex.class);
 this.fileIndexerWorkerMock.setFileIndexer(this.fileIndexerMock);
 this.controllerMock.setFileIndexerWorker(this.fileIndexerWorkerMock);

 ArgumentCaptor<IOException> exceptionArgumentCaptor = ArgumentCaptor.forClass(IOException.class);
 this.exceptionHandlerMock.setThrowable(new FileNotFoundException("Test file not found"));
 this.fileIndexerWorkerMock.setExceptionHandler(this.exceptionHandlerMock);

 // When
 indexFileAction.actionPerformed(new ActionEvent(this.guiMock, 0, "test"));

 // Then
 verify(this.exceptionHandlerMock).handle(exceptionArgumentCaptor.capture(), UIMessage.ERROR_FILE_NOT_FOUND, "test.txt");
 }
}
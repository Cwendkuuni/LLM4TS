import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import fr.pingtimeout.jtail.gui.action.IndexFileAction;
import fr.pingtimeout.jtail.gui.controller.JTailMainModel;
import fr.pingtimeout.jtail.gui.model.JTailModel;
import fr.pingtimeout.jtail.gui.message.UIMessage;
import fr.pingtimeout.jtail.io.index.FileIndexer;
import fr.pingtimeout.jtail.io.index.FileIndexerWorker;
import fr.pingtimeout.jtail.io.index.RamFileIndex;
import fr.pingtimeout.jtail.io.index.RomFileIndex;
import fr.pingtimeout.jtail.util.JTailLogger;

public class IndexFileActionTest {

 private IndexFileAction indexFileAction;
 private JTailMainModel jTailMainModelMock;
 private OpenFileModel openFileModelMock;
 private FileIndexer fileIndexerMock;
 private FileIndexerWorker fileIndexerWorkerMock;

 @Before
 public void setup() {
 jTailMainModelMock = mock(JTailMainModel.class);
 openFileModelMock = mock(OpenFileModel.class);
 fileIndexerMock = mock(FileIndexer.class);
 fileIndexerWorkerMock = mock(FileIndexerWorker.class);

 indexFileAction = new IndexFileAction(jTailMainModelMock, openFileModelMock);
 }

 @Test
 public void testActionPerformed_ValidFileAndIndexType() {
 // Given
 String filePath = "test.txt";
 String indexType = "MEMORY_BASED";
 openFileModelMock.setFile(new File(filePath));
 openFileModelMock.setIndexType(indexType);

 // When
 indexFileAction.actionPerformed(new ActionEvent(new JButton(), ActionEvent.ACTION_PERFORMED, "test"));

 // Then
 JTailLogger.debugCallsExpected("File : " + filePath);
 JTailLogger.debugCallsExpected("Index type : " + indexType);
 verify(jTailMainModelMock).add(Mockito.any(JTailModel.class));
 }

 @Test
 public void testActionPerformed_NullFile() {
 // Given
 openFileModelMock.setFile(null);

 // When
 indexFileAction.actionPerformed(new ActionEvent(new JButton(), ActionEvent.ACTION_PERFORMED, "test"));

 // Then
 InformationHandler.handleCallsWith(true, UIMessage.INFO_NO_FILE_SELECTED);
 }

 @Test
 public void testActionPerformed_InvalidIndexType() {
 // Given
 String invalidIndexType = "INVALID_INDEX_TYPE";
 openFileModelMock.setFile(new File("test.txt"));
 openFileModelMock.setIndexType(invalidIndexType);

 // When
 indexFileAction.actionPerformed(new ActionEvent(new JButton(), ActionEvent.ACTION_PERFORMED, "test"));

 // Then
 ExceptionHandler.handleCallsWith(UIMessage.ERROR_INVALID_INDEX_TYPE);
 }

 @Test
 public void testUpdate_FileIndexingSuccess() {
 // Given
 File file = new File("test.txt");
 FileIndexer fileIndexer = new RamFileIndexer(file);
 openFileModelMock.setFile(file);
 openFileModelMock.setIndexType("MEMORY_BASED");

 indexFileAction.update(fileIndexer, 100);

 // Then
 JTailModel model = Mockito.any(JTailModel.class);
 jTailMainModelMock.add(model);
 }

 @Test
 public void testUpdate_FileIndexingFailure_FileNotFound() {
 // Given
 File file = new File("test.txt");
 FileIndexer fileIndexer = Mockito.mock(FileIndexer.class);
 Mockito.doThrow(new FileNotFoundException("Test file not found"))
 .when(fileIndexer).init();
 openFileModelMock.setFile(file);
 openFileModelMock.setIndexType("MEMORY_BASED");

 indexFileAction.update(fileIndexer, 100);

 // Then
 ExceptionHandler.handleCallsWith(new FileNotFoundException("Test file not found"), UIMessage.ERROR_FILE_NOT_FOUND, file.getName());
 }

 @Test
 public void testUpdate_FileIndexingFailure_Generic() {
 // Given
 File file = new File("test.txt");
 FileIndexer fileIndexer = Mockito.mock(FileIndexer.class);
 Mockito.doThrow(new Exception("Test exception"))
 .when(fileIndexer).init();
 openFileModelMock.setFile(file);
 openFileModelMock.setIndexType("MEMORY_BASED");

 indexFileAction.update(fileIndexer, 100);

 // Then
 ExceptionHandler.handleCallsWith(new Exception("Test exception"), UIMessage.ERROR_GENERIC_MESSAGE, new Object[0]);
 }
}
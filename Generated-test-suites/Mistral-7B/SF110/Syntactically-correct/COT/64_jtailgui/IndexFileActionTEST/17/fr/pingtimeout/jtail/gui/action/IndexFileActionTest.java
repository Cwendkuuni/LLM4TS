import org.junit.Test;
import org.junit.Assert;
import static org.mockito.Mockito.*;
import fr.pingtimeout.jtail.gui.action.IndexFileAction;
import fr.pingtimeout.jtail.gui.model.JTailMainModel;
import fr.pingtimeout.jtail.gui.model.OpenFileModel;
import fr.pingtimeout.jtail.io.index.FileIndexer;
import fr.pingtimeout.jtail.io.index.RamFileIndex;
import fr.pingtimeout.jtail.io.index.RomFileIndex;
import java.awt.event.ActionEvent;
import java.io.File;

public class IndexFileActionTest {

 private JTailMainModel jTailMainModel;
 private OpenFileModel openFileModel;

 @Before
 public void setUp() {
 jTailMainModel = mock(JTailMainModel.class);
 openFileModel = new OpenFileModel(null, null);
 }

 @Test
 public void testIndexFileActionConstructor() {
 IndexFileAction indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
 Assert.assertEquals("action.open.label", indexFileAction.getText());
 Assert.assertEquals("action.open.tooltip", indexFileAction.getToolTipText());
 Assert.assertEquals("action.open.mnemonic", indexFileAction.getMnemonicKey());
 Assert.assertEquals("action.open.accelerator", indexFileAction.getAcceleratorKey());
 Assert.assertEquals(IndexFileAction.bundle.getString("action.open.label"), indexFileAction.getValue(Action.NAME));
 Assert.assertNotNull(IndexFileAction.bundle);
 Assert.assertNotNull(IndexFileAction.OPEN_ICON);
 }

 @Test
 public void testActionPerformedWithNullFile() {
 IndexFileAction indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
 ActionEvent actionEvent = mock(ActionEvent.class);
 indexFileAction.actionPerformed(actionEvent);
 verify(jTailMainModel).add(null);
 InformationHandler.verify(1).handle(true, UIMessage.INFO_NO_FILE_SELECTED, new Object[0]);
 }

 @Test
 public void testActionPerformedWithNullIndexType() {
 OpenFileModel openFileModelWithNullIndexType = new OpenFileModel(null, null);
 IndexFileAction indexFileAction = new IndexFileAction(jTailMainModel, openFileModelWithNullIndexType);
 ActionEvent actionEvent = mock(ActionEvent.class);
 indexFileAction.actionPerformed(actionEvent);
 Assert.assertNull(indexerClass);
 }

 @Test
 public void testActionPerformedWithMemoryBasedIndexType() {
 OpenFileModel openFileModelWithMemoryBasedIndexType = new OpenFileModel(new File("testFile"), IndexFileAction.MemoryBasedIndexType.MEMORY_BASED);
 IndexFileAction indexFileAction = new IndexFileAction(jTailMainModel, openFileModelWithMemoryBasedIndexType);
 ActionEvent actionEvent = mock(ActionEvent.class);
 indexFileAction.actionPerformed(actionEvent);
 verify(jTailMainModel).add(any(JTailModel.class));
 Class<? extends FileIndex> indexerClass = RamFileIndex.class;
 }

 @Test
 public void testActionPerformedWithFileBasedIndexType() {
 OpenFileModel openFileModelWithFileBasedIndexType = new OpenFileModel(new File("testFile"), IndexFileAction.MemoryBasedIndexType.FILE_BASED);
 IndexFileAction indexFileAction = new IndexFileAction(jTailMainModel, openFileModelWithFileBasedIndexType);
 ActionEvent actionEvent = mock(ActionEvent.class);
 indexFileAction.actionPerformed(actionEvent);
 verify(jTailMainModel).add(any(JTailModel.class));
 Class<? extends FileIndex> indexerClass = RomFileIndex.class;
 }

 @Test(expected = FileNotFoundException.class)
 public void testActionPerformedWithNonExistentFile() {
 OpenFileModel openFileModelWithNonExistentFile = new OpenFileModel(new File("nonExistentFile"), IndexFileAction.MemoryBasedIndexType.MEMORY_BASED);
 IndexFileAction indexFileAction = new IndexFileAction(jTailMainModel, openFileModelWithNonExistentFile);
 ActionEvent actionEvent = mock(ActionEvent.class);
 indexFileAction.actionPerformed(actionEvent);
 ExceptionHandler.verify(1).handle(any(Exception.class), eq(UIMessage.ERROR_FILE_NOT_FOUND), anyString());
 }

 @Test
 public void testUpdateWithSuccessfulIndexing() {
 FileIndexer fileIndexer = mock(FileIndexer.class);
 IndexFileAction indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
 doReturn(fileIndexer).when(jTailMainModel).createFileIndexer(any(File.class), anyInt());
 when(fileIndexer.getFile()).thenReturn(new File("testFile"));
 indexFileAction.update(fileIndexer, 100);
 JTailModel jTailModel = any(JTailModel.class);
 verify(jTailMainModel).add(jTailModel);
 }

 @Test
 public void testUpdateWithFailedFileIndexing() {
 FileIndexer fileIndexer = mock(FileIndexer.class);
 IndexFileAction indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
 doReturn(fileIndexer).when(jTailMainModel).createFileIndexer(any(File.class), anyInt());
 doThrow(new Exception()).when(fileIndexer).index();
 indexFileAction.update(fileIndexer, 100);
 ExceptionHandler.verify(1).handle(any(Exception.class), eq(UIMessage.ERROR_GENERIC_MESSAGE), anyArray());
 }

 @Test
 public void testUpdateWithFailedFileReading() {
 FileIndexer fileIndexer = mock(FileIndexer.class);
 IndexFileAction indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
 doReturn(fileIndexer).when(jTailMainModel).createFileIndexer(any(File.class), anyInt());
 doThrow(new FileNotFoundException("testError")).when(fileIndexer).getFile();
 indexFileAction.update(fileIndexer, 100);
 ExceptionHandler.verify(1).handle(eq(FileNotFoundException.class), eq(UIMessage.ERROR_FILE_NOT_FOUND), anyString());
 }

}
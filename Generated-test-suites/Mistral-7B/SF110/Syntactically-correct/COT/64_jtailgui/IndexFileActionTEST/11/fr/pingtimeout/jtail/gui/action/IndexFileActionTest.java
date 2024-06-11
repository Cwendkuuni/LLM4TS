import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import fr.pingtimeout.jtail.gui.action.IndexFileAction;
import fr.pingtimeout.jtail.gui.model.JTailMainModel;
import fr.pingtimeout.jtail.io.index.FileIndexer;
import fr.pingtimeout.jtail.io.FileIndexerWorker;
import fr.pingtimeout.jtail.io.index.RamFileIndex;
import fr.pingtimeout.jtail.io.index.RomFileIndex;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

public class IndexFileActionTest {

 private IndexFileAction indexFileAction;
 private JTailMainModel jTailMainModel;
 private OpenFileModel openFileModel;

 @Before
 public void setUp() {
 jTailMainModel = new JTailMainModel();
 openFileModel = new OpenFileModel(null, FileIndexType.MEMORY_BASED);
 indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
 }

 @Test
 public void testActionPerformedWithNullFile() {
 openFileModel.setFile(null);
 indexFileAction.actionPerformed(new ActionEvent(new JButton(), ActionEvent.ACTION_PERFORMED, ""));
 InformationHandler.assertNoMessages();
 }

 @Test
 public void testActionPerformedWithValidFileAndMemoryBasedIndexType() throws IOException {
 File file = new File("testFile.txt");
 openFileModel.setFile(file);
 openFileModel.setIndexType(FileIndexType.MEMORY_BASED);
 indexFileAction.actionPerformed(new ActionEvent(new JButton(), ActionEvent.ACTION_PERFORMED, ""));
 InformationHandler.assertNoMessages();
 FileIndexer fileIndexer = mock(FileIndexer.class);
 when(fileIndexer.addObserver(any())).thenReturn(fileIndexer);
 when(fileIndexer.getFile()).thenReturn(file);
 FileIndexerWorker fileIndexerWorker = new FileIndexerWorker(fileIndexer);
 fileIndexerWorker.execute();
 verify(fileIndexer).addObserver(eq(indexFileAction));
 JTailModel model = new JTailModel(file, new LineReader(file, fileIndexer.getIndex()));
 assertTrue(jTailMainModel.getModels().contains(model));
 }

 @Test
 public void testActionPerformedWithValidFileAndFileBasedIndexType() throws IOException {
 File file = new File("testFile.txt");
 openFileModel.setFile(file);
 openFileModel.setIndexType(FileIndexType.FILE_BASED);
 indexFileAction.actionPerformed(new ActionEvent(new JButton(), ActionEvent.ACTION_PERFORMED, ""));
 InformationHandler.assertNoMessages();
 FileIndexer fileIndexer = mock(RomFileIndex.class);
 when(fileIndexer.addObserver(any())).thenReturn(fileIndexer);
 when(fileIndexer.getFile()).thenReturn(file);
 FileIndexerWorker fileIndexerWorker = new FileIndexerWorker(fileIndexer);
 fileIndexerWorker.execute();
 verify(fileIndexer).addObserver(eq(indexFileAction));
 JTailModel model = new JTailModel(file, new LineReader(file, fileIndexer.getIndex()));
 assertTrue(jTailMainModel.getModels().contains(model));
 }

 @Test(expected = FileNotFoundException.class)
 public void testActionPerformedWithInvalidFile() throws IOException {
 File file = new File("nonExistentFile.txt");
 openFileModel.setFile(file);
 openFileModel.setIndexType(FileIndexType.MEMORY_BASED);
 indexFileAction.actionPerformed(new ActionEvent(new JButton(), ActionEvent.ACTION_PERFORMED, ""));
 InformationHandler.assertErrorMessage("ERROR_FILE_NOT_FOUND", file.getName());
 }

 @Test(expected = IOException.class)
 public void testUpdateWithNullFile() {
 indexFileAction.update(new Observable(), 100);
 }

 @Test
 public void testUpdateWithValidFile() {
 File file = new File("testFile.txt");
 indexFileAction.update(new Observable(), 100);
 FileIndexer fileIndexer = mock(FileIndexer.class);
 when(fileIndexer.getFile()).thenReturn(file);
 JTailModel model = new JTailModel(file, new LineReader(file, fileIndexer.getIndex()));
 assertTrue(jTailMainModel.getModels().contains(model));
 }

 @After
 public void tearDown() {
 jTailMainModel.clear();
 }
}
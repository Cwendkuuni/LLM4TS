import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import fr.pingtimeout.jtail.gui.action.IndexFileAction;
import fr.pingtimeout.jtail.gui.language.ResourceBundle;
import fr.pingtimeout.jtail.gui.message.UIMessage;
import fr.pingtimeout.jtail.gui.model.JTailModel;
import fr.pingtimeout.jtail.gui.controller.JTailMainModel;
import fr.pingtimeout.jtail.io.index.FileIndex;
import fr.pingtimeout.jtail.io.index.FileIndexer;
import fr.pingtimeout.jtail.io.index.RamFileIndex;
import fr.pingtimeout.jtail.io.index.RomFileIndex;
import fr.pingtimeout.jtail.io.lineReader.LineReader;
import fr.pingtimeout.jtail.util.ExceptionHandler;
import fr.pingtimeout.jtail.util.InformationHandler;
import fr.pingtimeout.jtail.util.JTailLogger;

public class IndexFileActionTest {

 private IndexFileAction indexFileAction;
 private JTailMainModel jTailMainModel;
 private OpenFileModel openFileModel;
 private ResourceBundle bundle;
 private ExceptionHandler exceptionHandler;
 private InformationHandler informationHandler;

 @Before
 public void setUp() {
 jTailMainModel = Mockito.mock(JTailMainModel.class);
 openFileModel = new OpenFileModel();
 bundle = ResourceBundle.getBundle("fr.pingtimeout.jtail.gui.language");
 exceptionHandler = Mockito.mock(ExceptionHandler.class);
 informationHandler = Mockito.mock(InformationHandler.class);
 indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
 }

 @Test
 public void testActionPerformedWithNullFile() {
 openFileModel.setFile(null);
 indexFileAction.actionPerformed(Mockito.mock(ActionEvent.class));
 InformationHandler.assertHandledMessage(informationHandler, UIMessage.INFO_NO_FILE_SELECTED);
 }

 @Test
 public void testActionPerformedWithInvalidIndexType() {
 openFileModel.setIndexType(-1);
 indexFileAction.actionPerformed(Mockito.mock(ActionEvent.class));
 Assert.assertNull(indexFileAction.openFileModel.getIndexerClass());
 }

 @Test
 public void testActionPerformedWithValidIndexTypeAndFile() throws FileNotFoundException {
 openFileModel.setFile(new File("test.txt"));
 openFileModel.setIndexType(IndexFileAction.MemoryBasedIndex.MEMORY_BASED);
 indexFileAction.actionPerformed(Mockito.mock(ActionEvent.class));
 Mockito.verify(jTailMainModel).add(Mockito.any(JTailModel.class));
 }

 @Test(expected = FileNotFoundException.class)
 public void testActionPerformedWithInvalidFile() throws FileNotFoundException {
 openFileModel.setFile(new File("invalidFile.txt"));
 indexFileAction.actionPerformed(Mockito.mock(ActionEvent.class));
 ExceptionHandler.assertHandledException(exceptionHandler, FileNotFoundException.class);
 }

 @Test
 public void testUpdateWithSuccess() {
 File file = new File("test.txt");
 FileIndex fileIndex = Mockito.mock(FileIndex.class);
 FileIndexer fileIndexer = new FileIndexer(file, RamFileIndex.class);
 indexFileAction.update(fileIndexer, 100);
 JTailModel jTailModel = Mockito.mock(JTailModel.class);
 Mockito.when(fileIndexer.getFile()).thenReturn(file);
 Mockito.when(fileIndexer.getIndex()).thenReturn(fileIndex);
 Mockito.when(new LineReader(file, fileIndex)).thenReturn(new LineReader("content", file));
 Mockito.when(new JTailModel(file, Mockito.any(LineReader.class))).thenReturn(jTailModel);
 Mockito.when(jTailMainModel.add(jTailModel)).thenReturn(true);
 }

 @Test
 public void testUpdateWithFileNotFoundException() {
 File file = new File("test.txt");
 FileIndex fileIndex = Mockito.mock(FileIndex.class);
 FileIndexer fileIndexer = new FileIndexer(file, RamFileIndex.class);
 indexFileAction.update(fileIndexer, 100);
 ExceptionHandler.assertHandledException(exceptionHandler, FileNotFoundException.class);
 }

 @Test
 public void testUpdateWithGenericException() {
 File file = new File("test.txt");
 FileIndex fileIndex = Mockito.mock(FileIndex.class);
 FileIndexer fileIndexer = new FileIndexer(file, RamFileIndex.class);
 indexFileAction.update(fileIndexer, 100);
 ExceptionHandler.assertHandledException(exceptionHandler, Exception.class);
 }
}
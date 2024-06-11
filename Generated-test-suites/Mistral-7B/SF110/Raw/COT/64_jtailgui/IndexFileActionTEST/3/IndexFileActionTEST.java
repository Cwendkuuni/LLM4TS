package fr.pingtimeout.jtail.gui.action.test;

import static org.junit.Assert.*;
import fr.pingtimeout.jtail.gui.action.IndexFileAction;
import fr.pingtimeout.jtail.gui.model.JTailMainModel;
import fr.pingtimeout.jtail.io.index.FileIndexer;
import fr.pingtimeout.jtail.io.index.FileIndexerWorker;
import fr.pingtimeout.jtail.io.index.RamFileIndex;
import fr.pingtimeout.jtail.io.index.RomFileIndex;
import fr.pingtimeout.jtail.gui.message.InformationHandler;
import fr.pingtimeout.jtail.gui.message.UIMessage;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

public class IndexFileActionTest {

 private IndexFileAction indexFileAction;
 private JTailMainModel jTailMainModel;
 private OpenFileModel openFileModel;
 private InformationHandler informationHandler;

 @Before
 public void setUp() {
 jTailMainModel = Mockito.mock(JTailMainModel.class);
 openFileModel = new OpenFileModel(null, null);
 indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
 informationHandler = Mockito.mock(InformationHandler.class);
 }

 @Test
 public void testActionPerformed_NullFile() {
 openFileModel.setFile(null);
 indexFileAction.actionPerformed(new ActionEvent(new Object(), 0, ""));
 InformationHandler.handle.withArgs(true, UIMessage.INFO_NO_FILE_SELECTED).thenCallRealMethod();
 verify(informationHandler).handle(ArgumentMatchers.bool(true), ArgumentMatchers.instanceOf(UIMessage.class), (Object[])Mockito.any());
 }

 @Test
 public void testActionPerformed_ValidFile() throws IOException {
 File file = new File("test.txt");
 openFileModel.setFile(file);
 openFileModel.setIndexType(IndexFileAction.MemoryBasedIndexType.MEMORY_BASED);
 indexFileAction.actionPerformed(new ActionEvent(new Object(), 0, ""));
 Class<? extends FileIndex> indexerClass = RamFileIndex.class;
 FileIndexer fileIndexer = Mockito.mock(FileIndexer.class);
 FileIndexerWorker fileIndexerWorker = new FileIndexerWorker(fileIndexer);
 fileIndexerWorker.execute();
 verify(jTailMainModel).add(ArgumentMatchers.instanceOf(JTailModel.class));
 }

 @Test(expected = FileNotFoundException.class)
 public void testActionPerformed_InvalidFile() throws IOException {
 File file = new File("test.txt");
 openFileModel.setFile(file);
 openFileModel.setIndexType(IndexFileType.FILE_BASED);
 indexFileAction.actionPerformed(new ActionEvent(new Object(), 0, ""));
 ExceptionHandler.handle.withArgs(ArgumentMatchers.instanceOf(FileNotFoundException.class), UIMessage.ERROR_FILE_NOT_FOUND, file.getName()).thenCallRealMethod();
 }

 @Test
 public void testUpdate() throws IOException {
 File file = new File("test.txt");
 openFileModel.setFile(file);
 openFileModel.setIndexType(IndexFileAction.MemoryBasedIndexType.MEMORY_BASED);
 indexFileAction.update(Mockito.mock(Observable.class), 100);
 FileIndexer fileIndexer = Mockito.mock(FileIndexer.class);
 FileIndexerWorker fileIndexerWorker = new FileIndexerWorker(fileIndexer);
 fileIndexerWorker.execute();
 JTailModel model = Mockito.mock(JTailModel.class);
 jTailMainModel.add.withArgs(model).thenCallRealMethod();
 verify(model).setFile(file);
 }

 @Test(expected = IOException.class)
 public void testUpdate_InvalidFile() throws IOException {
 File file = new File("test.txt");
 openFileModel.setFile(file);
 openFileModel.setIndexType(IndexFileAction.MemoryBasedIndexType.MEMORY_BASED);
 indexFileAction.update(Mockito.mock(Observable.class), 100);
 FileIndexer fileIndexer = Mockito.mock(FileIndexer.class);
 FileIndexerWorker fileIndexerWorker = new FileIndexerWorker(fileIndexer);
 fileIndexerWorker.execute();
 ExceptionHandler.handle.withArgs(ArgumentMatchers.instanceOf(IOException.class), UIMessage.ERROR_GENERIC_MESSAGE, (Object[])Mockito.any()).thenCallRealMethod();
 }
}
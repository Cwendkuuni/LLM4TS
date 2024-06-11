package fr.pingtimeout.jtail.gui.action.test;

import static org.junit.Assert.*;

import fr.pingtimeout.jtail.gui.action.IndexFileAction;
import fr.pingtimeout.jtail.gui.model.JTailMainModel;
import fr.pingtimeout.jtail.gui.model.JTailModel;
import fr.pingtimeout.jtail.io.index.FileIndexer;
import fr.pingtimeout.jtail.io.index.FileIndexerWorker;
import fr.pingtimeout.jtail.io.index.FileIndexerObserver;
import fr.pingtimeout.jtail.io.index.RamFileIndex;
import fr.pingtimeout.jtail.io.index.RomFileIndex;
import fr.pingtimeout.jtail.io.LineReader;
import fr.pingtimeout.jtail.gui.controller.ExceptionHandler;
import fr.pingtimeout.jtail.gui.controller.InformationHandler;
import fr.pingtimeout.jtail.gui.message.UIMessage;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.Observable;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class IndexFileActionTest {

 private IndexFileAction indexFileAction;
 private JTailMainModel jTailMainModel;
 private OpenFileModel openFileModel;
 private File file;

 @Before
 public void setUp() {
 jTailMainModel = new JTailMainModel();
 openFileModel = new OpenFileModel();
 file = new File("testFile.txt");
 indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
 }

 @Test
 public void testActionPerformedNullFile() {
 openFileModel.setFile(null);
 indexFileAction.actionPerformed(new ActionEvent(new JButton(), ActionEvent.ACTION_PERFORMED, "Test"));
 InformationHandler.clear();
 assertEquals(1, InformationHandler.getInformationMessages().size());
 assertEquals(UIMessage.INFO_NO_FILE_SELECTED, InformationHandler.getInformationMessage(0).getMessage());
 }

 @Test
 public void testActionPerformedInvalidIndexType() {
 openFileModel.setFile(file);
 openFileModel.setIndexType(-1);
 indexFileAction.actionPerformed(new ActionEvent(new JButton(), ActionEvent.ACTION_PERFORMED, "Test"));
 ExceptionHandler.clear();
 assertEquals(1, ExceptionHandler.getExceptionMessages().size());
 assertEquals(UIMessage.ERROR_INVALID_INDEX_TYPE, ExceptionHandler.getExceptionMessage(0).getMessage());
 }

 @Test
 public void testActionPerformedValidFileAndIndexType() throws IOException {
 openFileModel.setFile(file);
 openFileModel.setIndexType(IndexFileAction.FILE_BASED);
 indexFileAction.actionPerformed(new ActionEvent(new JButton(), ActionEvent.ACTION_PERFORMED, "Test"));
 JTailModel jTailModel = jTailMainModel.getModelList().get(0);
 assertEquals(file.getName(), jTailModel.getFileName());
 }

 @Test(expected = FileNotFoundException.class)
 public void testActionPerformedFileNotFound() throws Exception {
 openFileModel.setFile(new File("nonExistentFile.txt"));
 openFileModel.setIndexType(IndexFileAction.FILE_BASED);
 indexFileAction.actionPerformed(new ActionEvent(new JButton(), ActionEvent.ACTION_PERFORMED, "Test"));
 }

 @Test
 public void testUpdateNullFileIndexer() {
 observeFileIndexer(null);
 assertEquals(0, jTailMainModel.getModelList().size());
 }

 @Test
 public void testUpdateValidFileIndexer() throws Exception {
 FileIndexer fileIndexer = new FileIndexer(file, RamFileIndex.class);
 observeFileIndexer(fileIndexer);
 assertEquals(1, jTailMainModel.getModelList().size());
 }

 @Test
 public void testUpdateException() {
 observeFileIndexer(new FileIndexer(file, null));
 assertEquals(0, jTailMainModel.getModelList().size());
 }

 private void observeFileIndexer(FileIndexer fileIndexer) {
 openFileModel.setFile(file);
 openFileModel.setIndexType(fileIndexer instanceof RamFileIndex ? IndexFileAction.MEMORY_BASED : IndexFileAction.FILE_BASED);
 indexFileAction.actionPerformed(new ActionEvent(new JButton(), ActionEvent.ACTION_PERFORMED, "Test"));
 indexFileAction.update(fileIndexer, 100);
 }

 @After
 public void tearDown() {
 jTailMainModel.clear();
 openFileModel = null;
 indexFileAction = null;
 file = null;
 }

}
package fr.pingtimeout.jtail.gui.action.test;

import static org.junit.Assert.*;

import fr.pingtimeout.jtail.gui.action.IndexFileAction;
import fr.pingtimeout.jtail.gui.model.JTailMainModel;
import fr.pingtimeout.jtail.gui.model.JTailModel;
import fr.pingtimeout.jtail.io.index.FileIndexer;
import fr.pingtimeout.jtail.io.index.FileIndexerWorker;
import fr.pingtimeout.jtail.io.index.RamFileIndex;
import fr.pingtimeout.jtail.io.index.RomFileIndex;
import fr.pingtimeout.jtail.io.line.LineReader;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.Observable;
import org.junit.Before;
import org.junit.Test;

public class IndexFileActionTest {

 private IndexFileAction indexFileAction;
 private JTailMainModel jTailMainModel;
 private OpenFileModel openFileModel;

 @Before
 public void setUp() {
 jTailMainModel = new JTailMainModel();
 openFileModel = new OpenFileModel(null, IndexFileAction.MEMORY_BASED);
 indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
 }

 @Test
 public void testActionPerformed_ValidFileAndIndexType() {
 File file = new File("validFile.txt");
 openFileModel.setFile(file);
 openFileModel.setIndexType(IndexFileAction.FILE_BASED);
 ActionEvent actionEvent = new ActionEvent(new JButton(), ActionEvent.ACTION_PERFORMED, "open");
 indexFileAction.actionPerformed(actionEvent);
 assertTrue(jTailMainModel.getModels().size() > 0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testActionPerformed_NullFile() {
 openFileModel.setFile(null);
 ActionEvent actionEvent = new ActionEvent(new JButton(), ActionEvent.ACTION_PERFORMED, "open");
 indexFileAction.actionPerformed(actionEvent);
 fail("An IllegalArgumentException should have been thrown.");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testActionPerformed_InvalidIndexType() {
 openFileModel.setFile(new File("validFile.txt"));
 openFileModel.setIndexType(123);
 ActionEvent actionEvent = new ActionEvent(new JButton(), ActionEvent.ACTION_PERFORMED, "open");
 indexFileAction.actionPerformed(actionEvent);
 fail("An IllegalArgumentException should have been thrown.");
 }

 @Test
 public void testActionPerformed_FileNotFound() {
 File file = new File("invalidFile.txt");
 openFileModel.setFile(file);
 openFileModel.setIndexType(IndexFileAction.FILE_BASED);
 ActionEvent actionEvent = new ActionEvent(new JButton(), ActionEvent.ACTION_PERFORMED, "open");
 indexFileAction.actionPerformed(actionEvent);
 assertEquals(0, jTailMainModel.getModels().size());
 }

 @Test
 public void testUpdate_FileIndexedSuccessfully() {
 File file = new File("validFile.txt");
 openFileModel.setFile(file);
 openFileModel.setIndexType(IndexFileAction.FILE_BASED);
 indexFileAction.actionPerformed(new ActionEvent(new JButton(), ActionEvent.ACTION_PERFORMED, "open"));
 FileIndexer fileIndexer = new FileIndexer(file, RamFileIndex.class);
 Observable o = fileIndexer;
 Observable obs = new Observable(o);
 obs.setChanged();
 obs.notifyObservers(100);
 JTailModel model = jTailMainModel.getModels().get(0);
 assertEquals(file.getAbsolutePath(), model.getFile().getAbsolutePath());
 }

 @Test
 public void testUpdate_FileIndexingFailed() {
 File file = new File("validFile.txt");
 openFileModel.setFile(file);
 openFileModel.setIndexType(IndexFileAction.FILE_BASED);
 indexFileAction.actionPerformed(new ActionEvent(new JButton(), ActionEvent.ACTION_PERFORMED, "open"));
 FileIndexer fileIndexer = null;
 Observable o = fileIndexer;
 Observable obs = new Observable(o);
 obs.setChanged();
 obs.notifyObservers(100);
 assertTrue(jTailMainModel.getModels().size() == 0);
 }

 @Test
 public void testUpdate_FileNotFoundAfterIndexing() {
 File file = new File("validFile.txt");
 openFileModel.setFile(file);
 openFileModel.setIndexType(IndexFileAction.FILE_BASED);
 indexFileAction.actionPerformed(new ActionEvent(new JButton(), ActionEvent.ACTION_PERFORMED, "open"));
 FileIndexer fileIndexer = new FileIndexer(file, RamFileIndex.class);
 try {
 throw new FileNotFoundException("File not found.");
 } catch (FileNotFoundException e) {
 Observable o = fileIndexer;
 Observable obs = new Observable(o);
 obs.setChanged();
 obs.notifyObservers(100);
 }
 assertTrue(jTailMainModel.getModels().size() == 0);
 }

 @Test
 public void testUpdate_ExceptionThrownDuringIndexing() {
 File file = new File("validFile.txt");
 openFileModel.setFile(file);
 openFileModel.setIndexType(IndexFileAction.FILE_BASED);
 indexFileAction.actionPerformed(new ActionEvent(new JButton(), ActionEvent.ACTION_PERFORMED, "open"));
 FileIndexer fileIndexer = new FileIndexer(file, RamFileIndex.class);
 try {
 throw new IOException("An I/O error occurred.");
 } catch (IOException e) {
 Observable o = fileIndexer;
 Observable obs = new Observable(o);
 obs.setChanged();
 obs.notifyObservers(100);
 }
 assertTrue(jTailMainModel.getModels().size() == 0);
 }

 @Test
 public void testUpdate_MultipleFilesIndexedSuccessfully() {
 File file1 = new File("validFile1.txt");
 File file2 = new File("validFile2.txt");
 openFileModel.setFile(file1);
 openFileModel.setIndexType(IndexFileAction.FILE_BASED);
 indexFileAction.actionPerformed(new ActionEvent(new JButton(), ActionEvent.ACTION_PERFORMED, "open"));
 openFileModel.setFile(file2);
 indexFileAction.actionPerformed(new ActionEvent(new JButton(), ActionEvent.ACTION_PERFORMED, "open"));
 FileIndexer fileIndexer1 = new FileIndexer(file1, RamFileIndex.class);
 FileIndexer fileIndexer2 = new FileIndexer(file2, RamFileIndex.class);
 Observable o1 = fileIndexer1;
 Observable o2 = fileIndexer2;
 Observable[] observers = {o1, o2};
 ObservableGroup obsGroup = new ObservableGroup(observers);
 obsGroup.addObserver(indexFileAction);
 obsGroup.setChanged();
 obsGroup.notifyObservers(100);
 assertEquals(2, jTailMainModel.getModels().size());
 }

 @Test
 public void testFileIndexerWorker_FileIndexingFailed() {
 FileIndexerWorker fileIndexerWorker = new FileIndexerWorker(new FileIndexer(new File("invalidFile.txt"), RamFileIndex.class));
 fileIndexerWorker.doInBackground();
 assertTrue(fileIndexerWorker.getState() == Worker.State.FAILED);
 }

}
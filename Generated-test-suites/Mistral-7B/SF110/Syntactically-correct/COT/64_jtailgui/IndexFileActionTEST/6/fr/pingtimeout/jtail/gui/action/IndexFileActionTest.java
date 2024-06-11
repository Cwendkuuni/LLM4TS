package fr.pingtimeout.jtail.gui.action.test;

import static org.junit.Assert.*;

import fr.pingtimeout.jtail.gui.action.IndexFileAction;
import fr.pingtimeout.jtail.gui.action.OpenFileModel;
import fr.pingtimeout.jtail.io.index.FileIndexer;
import fr.pingtimeout.jtail.io.index.FileIndexerWorker;
import fr.pingtimeout.jtail.io.index.RamFileIndex;
import fr.pingtimeout.jtail.io.index.RomFileIndex;
import fr.pingtimeout.jtail.gui.model.JTailModel;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.Observable;
import org.junit.Before;
import org.junit.Test;

public class IndexFileActionTest {

 private IndexFileAction indexFileAction;
 private OpenFileModel openFileModel;
 private Observable o; // For update method test

 @Before
 public void setup() {
 openFileModel = new OpenFileModel();
 indexFileAction = new IndexFileAction(new JTailMainModel(), openFileModel);
 o = new Observable();
 }

 // Testing constructor
 @Test
 public void testIndexFileActionConstructor() {
 assertNotNull(indexFileAction);
 }

 // Testing actionPerformed method with valid file and index type
 @Test
 public void testActionPerformedValidFileAndIndexType() throws IOException {
 File file = new File("test.txt");
 openFileModel.setFile(file);
 openFileModel.setIndexType(IndexFileAction.IndexType.FILE_BASED);
 ActionEvent actionEvent = new ActionEvent(new Object(), ActionEvent.ACTION_PERFORMED, "testAction");
 indexFileAction.actionPerformed(actionEvent);
 FileIndexer fileIndexer = new FileIndexer(file, RomFileIndex.class);
 FileIndexerWorker fileIndexerWorker = new FileIndexerWorker(fileIndexer);
 fileIndexerWorker.execute();
 assertFalse(fileIndexerWorker.isDone()); // FileIndexerWorker is still executing
 }

 // Testing actionPerformed method with null file
 @Test(expected = IllegalArgumentException.class)
 public void testActionPerformedNullFile() {
 openFileModel.setFile(null);
 ActionEvent actionEvent = new ActionEvent(new Object(), ActionEvent.ACTION_PERFORMED, "testAction");
 indexFileAction.actionPerformed(actionEvent);
 }

 // Testing actionPerformed method with invalid index type
 @Test(expected = IllegalArgumentException.class)
 public void testActionPerformedInvalidIndexType() {
 openFileModel.setFile(new File("test.txt"));
 openFileModel.setIndexType(null);
 ActionEvent actionEvent = new ActionEvent(new Object(), ActionEvent.ACTION_PERFORMED, "testAction");
 indexFileAction.actionPerformed(actionEvent);
 }

 // Testing update method with valid file indexer
 @Test
 public void testUpdateValidFileIndexer() {
 openFileModel.setFile(new File("test.txt"));
 openFileModel.setIndexType(IndexFileAction.IndexType.FILE_BASED);
 indexFileAction.update(o, 100);
 assertNotNull(o);
 assertNotNull(indexFileAction.jTailMainModel.get(0));
 assertTrue(indexFileAction.jTailMainModel.get(0) instanceof JTailModel);
 }

 // Testing update method with null file indexer
 @Test
 public void testUpdateNullFileIndexer() {
 openFileModel.setFile(new File("test.txt"));
 openFileModel.setIndexType(IndexFileAction.IndexType.FILE_BASED);
 indexFileAction.update(null, 100);
 assertNull(indexFileAction.jTailMainModel);
 }

}
package fr.pingtimeout.jtail.gui.action.test;

import static org.junit.Assert.*;

import fr.pingtimeout.jtail.gui.action.IndexFileAction;
import fr.pingtimeout.jtail.gui.model.JTailMainModel;
import fr.pingtimeout.jtail.gui.model.JTailModel;
import fr.pingtimeout.jtail.io.index.FileIndexer;
import fr.pingtimeout.jtail.io.index.FileIndexerWorker;
import fr.pingtimeout.jtail.io.index.RamFileIndex;
import fr.pingtimeout.jtail.io.index.RomFileIndex;
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

 @Before
 public void setUp() {
 jTailMainModel = new JTailMainModel();
 openFileModel = new OpenFileModel();
 indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
 }

 @Test
 public void testIndexFileActionConstructor() {
 assertNotNull(indexFileAction);
 }

 @Test
 public void testActionPerformedWithNullFile() {
 indexFileAction.actionPerformed(new ActionEvent(new Object(), ActionEvent.ACTION_PERFORMED, ""));
 InformationHandler.clear(); // Clear InformationHandler messages for assertions
 assertEquals(1, InformationHandler.getNumberOfMessages());
 assertEquals(UIMessage.INFO_NO_FILE_SELECTED, InformationHandler.getMessage(0).getType());
 }

 @Test
 public void testActionPerformedWithValidFile() {
 File testFile = new File("testFile.txt");
 openFileModel.setFile(testFile);
 indexFileAction.actionPerformed(new ActionEvent(new Object(), ActionEvent.ACTION_PERFORMED, ""));
 FileIndexer fileIndexer = null;
 try {
 fileIndexer = new FileIndexer(testFile, RamFileIndex.class);
 fileIndexer.addObserver(indexFileAction);
 } catch (IOException e) {
 e.printStackTrace();
 fail("FileIndexer creation failed.");
 }
 FileIndexerWorker fileIndexerWorker = new FileIndexerWorker(fileIndexer);
 fileIndexerWorker.execute();
 JTailModel jTailModel = jTailMainModel.getModel(0);
 assertNotNull(jTailModel);
 assertNotNull(jTailModel.getFile());
 assertSame(testFile, jTailModel.getFile());
 }

 @Test(expected = FileNotFoundException.class)
 public void testActionPerformedWithFileNotFoundException() {
 File testFile = new File("nonExistentFile.txt");
 openFileModel.setFile(testFile);
 indexFileAction.actionPerformed(new ActionEvent(new Object(), ActionEvent.ACTION_PERFORMED, ""));
 }

 @Test
 public void testUpdateMethod() {
 Observable observable = new Observable() {
 @Override
 public void notifyObservers(int i, Object o) {
 indexFileAction.update(this, o);
 }
 };
 observable.setChanged();
 observable.notifyObservers(100);
 FileIndexer fileIndexer = new FileIndexer(new File("testFile.txt"), RamFileIndex.class);
 fileIndexer.addObserver(indexFileAction);
 assertFalse(jTailMainModel.containsModel(new JTailModel(new File("testFile.txt"), null)));
 observable.setChanged();
 observable.notifyObservers(100);
 assertTrue(jTailMainModel.containsModel(new JTailModel(new File("testFile.txt"), null)));
 }

 @After
 public void tearDown() {
 jTailMainModel.clearModels();
 }

}
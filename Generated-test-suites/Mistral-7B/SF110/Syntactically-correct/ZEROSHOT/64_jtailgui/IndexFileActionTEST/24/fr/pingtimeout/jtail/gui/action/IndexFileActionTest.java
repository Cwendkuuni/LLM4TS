package fr.pingtimeout.jtail.gui.action.test;

import static org.junit.Assert.*;

import fr.pingtimeout.jtail.gui.action.IndexFileAction;
import fr.pingtimeout.jtail.gui.controller.JTailMainModel;
import fr.pingtimeout.jtail.gui.model.JTailModel;
import fr.pingtimeout.jtail.io.index.FileIndexer;
import fr.pingtimeout.jtail.io.index.FileIndexerWorker;
import fr.pingtimeout.jtail.io.index.RamFileIndex;
import fr.pingtimeout.jtail.io.index.RomFileIndex;
import fr.pingtimeout.jtail.gui.message.UIMessage;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import org.junit.Before;
import org.junit.Test;

public class IndexFileActionTest {

 private JTailMainModel jTailMainModel;
 private OpenFileModel openFileModel;
 private IndexFileAction indexFileAction;
 private JDialog dialog;
 private File file;

 @Before
 public void setUp() {
 jTailMainModel = new JTailMainModel();
 openFileModel = new OpenFileModel();
 indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
 dialog = new JDialog();
 file = new File("testFile.txt");
 }

 @Test
 public void testActionPerformedWithNullFile() {
 actionPerformed(new ActionEvent(dialog, 0, "ActionPerformed"));
 assertEquals(0, jTailMainModel.getSize());
 }

 @Test
 public void testActionPerformedWithValidFile() throws IOException {
 openFileModel.setFile(file);
 openFileModel.setIndexType(IndexFileAction.FILE_BASED);
 actionPerformed(new ActionEvent(dialog, 0, "ActionPerformed"));
 assertEquals(1, jTailMainModel.getSize());
 }

 @Test
 public void testActionPerformedWithInvalidFile() {
 openFileModel.setFile(null);
 actionPerformed(new ActionEvent(dialog, 0, "ActionPerformed"));
 assertEquals(0, jTailMainModel.getSize());
 }

 @Test
 public void testActionPerformedWithInvalidIndexType() {
 openFileModel.setFile(file);
 openFileModel.setIndexType(-1);
 actionPerformed(new ActionEvent(dialog, 0, "ActionPerformed"));
 assertEquals(0, jTailMainModel.getSize());
 }

 @Test
 public void testActionPerformedWithValidIndexTypeAndMemoryBasedIndex() throws IOException {
 openFileModel.setFile(file);
 openFileModel.setIndexType(IndexFileAction.MEMORY_BASED);
 actionPerformed(new ActionEvent(dialog, 0, "ActionPerformed"));
 assertEquals(1, jTailMainModel.getSize());
 }

 @Test
 public void testActionPerformedWithValidIndexTypeAndFileBasedIndex() throws IOException {
 openFileModel.setFile(file);
 openFileModel.setIndexType(IndexFileAction.FILE_BASED);
 actionPerformed(new ActionEvent(dialog, 0, "ActionPerformed"));
 assertEquals(1, jTailMainModel.getSize());
 }

 @Test
 public void testUpdateWithFileNotFoundException() throws IOException {
 FileIndexer fileIndexer = new FileIndexer(file, RamFileIndex.class);
 fileIndexer.addObserver(this);
 fileIndexer.notifyObservers(100, new FileNotFoundException("File not found"));
 assertEquals(0, jTailMainModel.getSize());
 }

 @Test
 public void testUpdateWithSuccessfulIndexing() throws IOException {
 FileIndexer fileIndexer = new FileIndexer(file, RamFileIndex.class);
 fileIndexer.addObserver(this);
 fileIndexer.notifyObservers(100, null);
 assertEquals(1, jTailMainModel.getSize());
 }

 @Test
 public void testUpdateWithException() throws IOException {
 FileIndexer fileIndexer = new FileIndexer(file, RomFileIndex.class);
 fileIndexer.addObserver(this);
 fileIndexer.notifyObservers(100, new IOException("An error occurred"));
 assertEquals(0, jTailMainModel.getSize());
 }

 private void actionPerformed(ActionEvent actionEvent) {
 EventQueue.invokeLater(() -> indexFileAction.actionPerformed(actionEvent));
 }
}
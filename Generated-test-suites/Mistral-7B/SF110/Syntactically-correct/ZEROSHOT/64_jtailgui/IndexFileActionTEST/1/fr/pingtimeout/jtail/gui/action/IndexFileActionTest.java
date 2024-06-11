package fr.pingtimeout.jtail.gui.action;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.Observable;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import fr.pingtimeout.jtail.gui.controller.JTailMainModel;
import fr.pingtimeout.jtail.gui.message.InformationHandler;
import fr.pingtimeout.jtail.gui.message.UIMessage;
import fr.pingtimeout.jtail.gui.model.JTailModel;
import fr.pingtimeout.jtail.gui.observer.JTailModelObserver;
import fr.pingtimeout.jtail.io.FileIndexer;
import fr.pingtimeout.jtail.io.FileIndexerWorker;
import fr.pingtimeout.jtail.io.index.FileIndex;
import fr.pingtimeout.jtail.io.index.RamFileIndex;
import fr.pingtimeout.jtail.io.index.RomFileIndex;
import fr.pingtimeout.jtail.util.JTailLogger;

public class IndexFileActionTest {

 private JTailMainModel jTailMainModel;
 private OpenFileModel openFileModel;
 private IndexFileAction indexFileAction;
 private JTailModelObserver jTailModelObserver;
 private FileIndexer fileIndexer;
 private FileIndex index;
 private File file;

 @Before
 public void setUp() {
 jTailMainModel = Mockito.mock(JTailMainModel.class);
 openFileModel = new OpenFileModel();
 indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
 jTailModelObserver = Mockito.mock(JTailModelObserver.class);
 fileIndexer = Mockito.mock(FileIndexer.class);
 index = Mockito.mock(FileIndex.class);
 file = new File("testFile.txt");

 when(fileIndexer.getFile()).thenReturn(file);
 when(fileIndexer.getIndex()).thenReturn(index);

 doNothing().when(InformationHandler.class).handle(anyBoolean(), any(UIMessage.class), any());
 doThrow(new IOException("Test exception")).when(fileIndexer).buildIndex();
 }

 @Test
 public void testConstructor() {
 assertNotNull(indexFileAction);
 assertEquals("action.open.label", indexFileAction.getText());
 assertEquals("action.open.tooltip", indexFileAction.getToolTipText());
 assertEquals('O', indexFileAction.getMnemonic());
 assertEquals("F1", indexFileAction.getAcceleratorKeyStroke().toString());
 assertNotNull(indexFileAction.getIcon());
 }

 @Test
 public void testActionPerformedWhenFileIsNull() {
 openFileModel.setFile(null);
 indexFileAction.actionPerformed(new ActionEvent(new JButton(), 1, "test"));
 verify(InformationHandler.class).handle(true, UIMessage.INFO_NO_FILE_SELECTED, new Object[0]);
 }

 @Test
 public void testActionPerformedWhenFileIsValid() throws Exception {
 openFileModel.setFile(file);
 openFileModel.setIndexType(IndexFileModel.FILE_BASED);
 indexFileAction.actionPerformed(new ActionEvent(new JButton(), 1, "test"));
 verify(JTailLogger.class).debug("File : {}", file);
 verify(JTailLogger.class).debug("Index type : FILE_BASED");
 verify(fileIndexer, times(1)).buildIndex();
 verify(jTailMainModel, times(1)).add(any(JTailModel.class));
 }

 @Test
 public void testActionPerformedWhenFileIsInvalid() {
 openFileModel.setFile(file);
 openFileModel.setIndexType(IndexFileModel.FILE_BASED);
 doThrow(new FileNotFoundException("Test exception")).when(fileIndexer).buildIndex();
 indexFileAction.actionPerformed(new ActionEvent(new JButton(), 1, "test"));
 verify(InformationHandler.class).handle(true, UIMessage.ERROR_FILE_NOT_FOUND, file.getName());
 }

 @Test
 public void testUpdate() throws Exception {
 when(fileIndexer.getProgress()).thenReturn(100);
 indexFileAction.update(fileIndexer, 100);
 verify(JTailLogger.class).debug("Notification received : 100 emitted by fileIndexer");
 verify(jTailMainModel, times(1)).add(any(JTailModel.class));
 }

 @Test
 public void testObserverRegistration() {
 indexFileAction.addObserver(jTailModelObserver);
 verify(fileIndexer, times(1)).addObserver(jTailModelObserver);
 }
}
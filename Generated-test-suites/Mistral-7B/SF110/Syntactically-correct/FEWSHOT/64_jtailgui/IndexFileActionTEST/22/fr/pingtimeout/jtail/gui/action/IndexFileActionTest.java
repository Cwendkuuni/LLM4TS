package fr.pingtimeout.jtail.gui.action;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import fr.pingtimeout.jtail.gui.controller.*;
import fr.pingtimeout.jtail.gui.message.*;
import fr.pingtimeout.jtail.gui.model.*;
import fr.pingtimeout.jtail.io.*;
import fr.pingtimeout.jtail.io.index.*;

public class IndexFileActionTest {

 private JTailMainModel jTailMainModel;
 private OpenFileModel openFileModel;
 private IndexFileAction indexFileAction;
 private ActionEvent actionEvent;
 private JDialog dialog;
 private FileIndexer fileIndexer;
 private FileIndexerWorker fileIndexerWorker;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 jTailMainModel = mock(JTailMainModel.class);
 openFileModel = mock(OpenFileModel.class);
 indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
 actionEvent = mock(ActionEvent.class);
 dialog = mock(JDialog.class);
 }

 @Test
 public void testActionPerformed() {
 assertEquals("action.open.label", indexFileAction.getText());
 assertEquals("action.open.tooltip", indexFileAction.getToolTipText());
 assertEquals('O', indexFileAction.getMnemonic());
 assertEquals(KeyEvent.VK_O, indexFileAction.getAcceleratorKey());
 assertNotNull(indexFileAction.getIcon());

 when(openFileModel.getFile()).thenReturn(new File("test.txt"));
 when(openFileModel.getIndexType()).thenReturn(IndexType.FILE_BASED);
 when(dialog.isVisible()).thenReturn(true);

 indexFileAction.actionPerformed(actionEvent);

 verify(jTailMainModel).add(any(JTailModel.class));
 verifyZeroInteractions(InformationHandler.class);
 verifyZeroInteractions(ExceptionHandler.class);

 FileIndexerWorker spyFileIndexerWorker = spy(new FileIndexerWorker(fileIndexer));
 doNothing().when(spyFileIndexerWorker).execute();

 indexFileAction.actionPerformed(actionEvent);

 verify(fileIndexer).addObserver(eq(indexFileAction));
 verifyZeroInteractions(UIMessage.class);
 }

 @Test
 public void testActionPerformedWithNullFile() {
 when(openFileModel.getFile()).thenReturn(null);
 indexFileAction.actionPerformed(actionEvent);

 verify(InformationHandler.class).handle(isA(Boolean.TYPE), eq(true), any(UIMessage.class), anyArray());
 }

 @Test
 public void testActionPerformedWithFileNotFoundException() {
 when(openFileModel.getFile()).thenReturn(new File("test.txt"));
 when(openFileModel.getIndexType()).thenReturn(IndexType.FILE_BASED);
 when(dialog.isVisible()).thenReturn(true);

 doThrow(new FileNotFoundException("test")).when(fileIndexer).init(any(File.class), any(Class.class));

 indexFileAction.actionPerformed(actionEvent);

 verify(ExceptionHandler.class).handle(any(Exception.class), isA(UIMessage.class), anyArray());
 }

 @Test
 public void testUpdate() {
 FileIndexer spyFileIndexer = spy(new FileIndexer(new File("test.txt"), RamFileIndex.class));
 doReturn(100).when(spyFileIndexer).getProgress();
 fileIndexer = spyFileIndexer;

 indexFileAction.update(spyFileIndexer, anyInt());

 verify(jTailMainModel).add(any(JTailModel.class));
 }
}
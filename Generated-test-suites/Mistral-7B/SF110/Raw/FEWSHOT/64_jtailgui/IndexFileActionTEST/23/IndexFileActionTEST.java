package fr.pingtimeout.jtail.gui.action;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.awt.event.*;
import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import fr.pingtimeout.jtail.controller.JTailMainModel;
import fr.pingtimeout.jtail.gui.message.InformationHandler;
import fr.pingtimeout.jtail.gui.message.UIMessage;
import fr.pingtimeout.jtail.io.index.FileIndexer;
import fr.pingtimeout.jtail.io.index.FileIndexerWorker;
import fr.pingtimeout.jtail.io.index.RamFileIndex;
import fr.pingtimeout.jtail.io.index.RomFileIndex;
import fr.pingtimeout.jtail.io.lineReader.LineReader;
import fr.pingtimeout.jtail.gui.model.JTailModel;
import fr.pingtimeout.jtail.gui.model.OpenFileModel;

public class IndexFileActionTest {

 private JTailMainModel jTailMainModel;
 private OpenFileModel openFileModel;
 private InformationHandler informationHandler;
 private FileIndexerWorker fileIndexerWorker;
 private FileIndexer fileIndexer;

 @Before
 public void setUp() {
 jTailMainModel = mock(JTailMainModel.class);
 openFileModel = mock(OpenFileModel.class);
 informationHandler = mock(InformationHandler.class);
 fileIndexerWorker = mock(FileIndexerWorker.class);
 fileIndexer = mock(FileIndexIndexer.class);
 when(openFileModel.getFile()).thenReturn(new File("testFile.txt"));
 when(openFileModel.getIndexType()).thenReturn(IndexType.FILE_BASED);
 }

 @After
 public void tearDown() {
 reset(jTailMainModel, openFileModel, informationHandler, fileIndexerWorker, fileIndexer);
 }

 @Test
 public void testActionPerformed_NullFile() {
 IndexFileAction indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
 ActionEvent actionEvent = new ActionEvent(new Object(), ActionEvent.ACTION_PERFORMED, "");
 indexFileAction.actionPerformed(actionEvent);
 verify(informationHandler).handle(isTrue(), UIMessage.INFO_NO_FILE_SELECTED, (Object[]) anyArray());
 }

 @Test
 public void testActionPerformed_ValidFile() throws IOException {
 IndexFileAction indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
 ActionEvent actionEvent = new ActionEvent(new Object(), ActionEvent.ACTION_PERFORMED, "");
 when(fileIndexer.newFileIndexer(eq(openFileModel.getFile()), eq(RomFileIndex.class))).thenReturn(fileIndexer);
 indexFileAction.actionPerformed(actionEvent);
 verify(jTailMainModel).add(argThat(m -> m instanceof JTailModel));
 }

 @Test(expected = FileNotFoundException.class)
 public void testActionPerformed_InvalidFile() throws IOException {
 IndexFileAction indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
 ActionEvent actionEvent = new ActionEvent(new Object(), ActionEvent.ACTION_PERFORMED, "");
 when(fileIndexer.newFileIndexer(eq(openFileModel.getFile()), eq(RomFileIndex.class))).thenThrow(new FileNotFoundException());
 indexFileAction.actionPerformed(actionEvent);
 verify(informationHandler).handle(isA(Exception.class), UIMessage.ERROR_FILE_NOT_FOUND, eq(openFileModel.getFile().getName()));
 }

 @Test
 public void testUpdate() {
 IndexFileAction indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
 indexFileAction.update(fileIndexer, 100);
 verify(jTailMainModel).add(argThat(m -> m instanceof JTailModel));
 }
}
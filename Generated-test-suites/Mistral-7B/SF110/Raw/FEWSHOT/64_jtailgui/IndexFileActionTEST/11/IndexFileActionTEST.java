package fr.pingtimeout.jtail.gui.action;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.event.*;
import java.io.*;
import java.util.*;
import org.junit.*;
import org.junit.Test;
import org.mockito.*;
import fr.pingtimeout.jtail.controller.*;
import fr.pingtimeout.jtail.gui.message.*;
import fr.pingtimeout.jtail.gui.model.*;
import fr.pingtimeout.jtail.io.index.*;

public class IndexFileActionTest {

 private JTailMainModel jTailMainModel;
 private OpenFileModel openFileModel;
 private IndexFileAction indexFileAction;
 private JDialog mockDialog;
 private File mockFile;
 private FileIndex fileIndexer;
 private FileIndexerWorker fileIndexerWorker;

 @Before
 public void setUp() {
 jTailMainModel = mock(JTailMainModel.class);
 openFileModel = mock(OpenFileModel.class);
 indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
 mockDialog = mock(JDialog.class);
 mockFile = mock(File.class);
 fileIndexer = mock(FileIndexer.class);
 fileIndexerWorker = mock(FileIndexerWorker.class);
 }

 @Test
 public void testActionPerformedWithNullFile() {
 indexFileAction.actionPerformed(new ActionEvent(mockDialog, 0, "test"));
 verifyZeroInteractions(jTailMainModel, openFileModel, fileIndexer, fileIndexerWorker, InformationHandler.class);
 }

 @Test
 public void testActionPerformedWithValidFile() throws Exception {
 when(openFileModel.getFile()).thenReturn(mockFile);
 when(openFileModel.getIndexType()).thenReturn(IndexType.FILE_BASED);
 when(fileIndexerClass(IndexType.FILE_BASED)).thenReturn(RomFileIndex.class);
 when(new FileIndexer(mockFile, RomFileIndex.class)).thenReturn(fileIndexer);
 doReturn(fileIndexerWorker).when(indexFileAction).new FileIndexerWorker(fileIndexer);
 indexFileAction.actionPerformed(new ActionEvent(mockDialog, 0, "test"));
 verify(jTailMainModel).add(any(JTailModel.class));
 verifyZeroInteractions(InformationHandler.class);
 }

 @Test
 public void testActionPerformedWithInvalidFileType() {
 when(openFileModel.getFile()).thenReturn(mockFile);
 when(openFileModel.getIndexType()).thenReturn(IndexType.INVALID);
 indexFileAction.actionPerformed(new ActionEvent(mockDialog, 0, "test"));
 verifyZeroInteractions(jTailMainModel, openFileModel, fileIndexer, fileIndexerWorker, ExceptionHandler.class);
 }

 @Test
 public void testActionPerformedWithFileNotFoundException() {
 when(openFileModel.getFile()).thenReturn(mockFile);
 when(openFileModel.getIndexType()).thenReturn(IndexType.FILE_BASED);
 when(fileIndexerClass(IndexType.FILE_BASED)).thenReturn(RomFileIndex.class);
 doThrow(new FileNotFoundException("Error message")).when(new FileIndexer(mockFile, RomFileIndex.class));
 indexFileAction.actionPerformed(new ActionEvent(mockDialog, 0, "test"));
 verify(InformationHandler.class).handle(eq(true), isA(UIMessage.class), anyArray());
 }

 @Test
 public void testUpdateWithFileIndexerCompletion() throws Exception {
 when(openFileModel.getFile()).thenReturn(mockFile);
 when(openFileModel.getIndexType()).thenReturn(IndexType.FILE_BASED);
 when(fileIndexerClass(IndexType.FILE_BASED)).thenReturn(RomFileIndex.class);
 doReturn(fileIndexerWorker).when(indexFileAction).new FileIndexerWorker(fileIndexer);
 doReturn(100).when(fileIndexerWorker).getProgress();
 indexFileAction.update(fileIndexer, 100);
 verify(jTailMainModel).add(any(JTailModel.class));
 }

 @After
 public void tearDown() {
 reset(jTailMainModel, openFileModel, fileIndexer, fileIndexerWorker, InformationHandler.class);
 }

 private Class<? extends FileIndex> fileIndexerClass(IndexType indexType) {
 switch (indexType) {
 case MEMORY_BASED:
 return RamFileIndex.class;
 case FILE_BASED:
 return RomFileIndex.class;
 default:
 throw new IllegalStateException("Invalid index type: " + indexType);
 }
 }

}
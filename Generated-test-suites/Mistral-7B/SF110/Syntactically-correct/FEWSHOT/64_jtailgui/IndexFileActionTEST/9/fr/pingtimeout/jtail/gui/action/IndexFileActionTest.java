package fr.pingtimeout.jtail.gui.action;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.event.*;
import java.io.*;
import java.util.*;
import org.junit.*;
import fr.pingtimeout.jtail.gui.controller.*;
import fr.pingtimeout.jtail.gui.message.*;
import fr.pingtimeout.jtail.gui.model.*;
import fr.pingtimeout.jtail.io.index.*;

public class IndexFileActionTest {

 private JTailMainModel jTailMainModel;
 private OpenFileModel openFileModel;
 private IndexFileAction indexFileAction;
 private JDialog dialog;
 private Controller controller;
 private File file;
 private FileIndexer fileIndexer;
 private FileIndexerWorker fileIndexerWorker;

 @Before
 public void setUp() {
 jTailMainModel = mock(JTailMainModel.class);
 openFileModel = mock(OpenFileModel.class);
 indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
 dialog = mock(JDialog.class);
 controller = mock(Controller.class);
 file = mock(File.class);
 fileIndexer = mock(FileIndexer.class);
 fileIndexerWorker = mock(FileIndexerWorker.class);
 }

 @Test
 public void testActionPerformedWithNullFile() {
 indexFileAction.actionPerformed(new ActionEvent(dialog, 0, "test", 0, 0));
 verify(InformationHandler::handle, times(1)).handle(true, UIMessage.INFO_NO_FILE_SELECTED, new Object[0]);
 }

 @Test
 public void testActionPerformedWithValidFileAndIndexType() {
 when(openFileModel.getFile()).thenReturn(file);
 when(openFileModel.getIndexType()).thenReturn(FileIndexType.MEMORY_BASED);

 indexFileAction.actionPerformed(new ActionEvent(dialog, 0, "test", 0, 0));
 verify(JTailLogger::debug, times(3)).debug(anyString());
 verify(controller::openFileIndex, times(1)).openFileIndex(file, FileIndexType.MEMORY_BASED);
 }

 @Test
 public void testActionPerformedWithValidFileAndFileBasedIndexType() {
 when(openFileModel.getFile()).thenReturn(file);
 when(openFileModel.getIndexType()).thenReturn(FileIndexType.FILE_BASED);

 indexFileAction.actionPerformed(new ActionEvent(dialog, 0, "test", 0, 0));
 verify(JTailLogger::debug, times(3)).debug(anyString());
 verify(controller::openFileIndex, times(1)).openFileIndex(file, FileIndexType.FILE_BASED);
 }

 @Test(expected = FileNotFoundException.class)
 public void testActionPerformedWithInvalidFile() {
 when(openFileModel.getFile()).thenReturn(file);
 when(openFileModel.getIndexType()).thenReturn(FileIndexType.MEMORY_BASED);
 doThrow(new FileNotFoundException("Test error message"))
         .when(fileIndexer)
         .init(any(File.class), eq(RamFileIndex.class));

 indexFileAction.actionPerformed(new ActionEvent(dialog, 0, "test", 0, 0));
 verify(ExceptionHandler::handle, times(1)).handle(any(Exception.class), eq(UIMessage.ERROR_FILE_NOT_FOUND), anyString());
 }

 @Test
 public void testUpdateWithSuccessfulFileIndexing() {
 when(openFileModel.getFile()).thenReturn(file);
 when(openFileModel.getIndexType()).thenReturn(FileIndexType.MEMORY_BASED);
 when(fileIndexer.getFile()).thenReturn(file);
 doReturn(fileIndexer).when(fileIndexerWorker).getFileIndexer();
 when(fileIndexer.getIndex()).thenReturn(new Index());

 indexFileAction.actionPerformed(new ActionEvent(dialog, 0, "test", 0, 0));
 verify(fileIndexer, times(1)).addObserver(eq(indexFileAction));
 verify(fileIndexerWorker, times(1)).execute();

 verify(JTailLogger::debug, times(1)).debug("Notification received : 100 emitted by fr.pingtimeout.jtail.io.index.FileIndexer@1b6d3588");
 verify(jTailMainModel, times(1)).add(any(JTailModel.class));
 }

 @Test
 public void testUpdateWithFailedFileIndexing() {
 when(openFileModel.getFile()).thenReturn(file);
 when(openFileModel.getIndexType()).thenReturn(FileIndexType.MEMORY_BASED);
 doThrow(new Exception("Test error message"))
         .when(fileIndexer)
         .init(any(File.class), eq(RamFileIndex.class));

 indexFileAction.actionPerformed(new ActionEvent(dialog, 0, "test", 0, 0));
 verify(ExceptionHandler::handle, times(1)).handle(any(Exception.class), eq(UIMessage.ERROR_GENERIC_MESSAGE), new Object[0]);
 }
}
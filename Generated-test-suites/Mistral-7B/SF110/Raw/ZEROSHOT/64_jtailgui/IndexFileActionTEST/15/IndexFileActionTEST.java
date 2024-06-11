package fr.pingtimeout.jtail.gui.action;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.Observable;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import fr.pingtimeout.jtail.gui.controller.JTailMainModel;
import fr.pingtimeout.jtail.gui.message.InformationHandler;
import fr.pingtimeout.jtail.gui.message.UIMessage;
import fr.pingtimeout.jtail.gui.model.JTailModel;
import fr.pingtimeout.jtail.gui.model.OpenFileModel;
import fr.pingtimeout.jtail.io.FileIndexer;
import fr.pingtimeout.jtail.io.FileIndexerWorker;
import fr.pingtimeout.jtail.io.FileIndexer.IndexType;
import fr.pingtimeout.jtail.io.index.FileIndex;
import fr.pingtimeout.jtail.io.index.RamFileIndex;
import fr.pingtimeout.jtail.io.index.RomFileIndex;
import fr.pingtimeout.jtail.util.JTailLogger;

public class IndexFileActionTest {

 private IndexFileAction action;
 private OpenFileModel openFileModel;
 private JTailMainModel mockJTailMainModel;
 private JDialog mockDialog;
 private Observable mockFileIndexer;
 private FileIndexer mockFileIndexer;
 private FileIndex mockFileIndex;
 private File file;
 private String fileName;

 @Before
 public void setUp() {
 openFileModel = mock(OpenFileModel.class);
 mockJTailMainModel = mock(JTailMainModel.class);
 mockDialog = mock(JDialog.class);
 mockFileIndexer = mock(Observable.class);
 mockFileIndex = mock(FileIndex.class);
 file = new File("file.txt");
 fileName = file.getName();

 action = new IndexFileAction(mockJTailMainModel, openFileModel);
 }

 @After
 public void tearDown() {
 action = null;
 openFileModel = null;
 mockJTailMainModel = null;
 mockDialog = null;
 mockFileIndexer = null;
 mockFileIndex = null;
 file = null;
 }

 @Test
 public void testActionPerformed_NullFile() {
 when(openFileModel.getFile()).thenReturn(null);
 action.actionPerformed(new ActionEvent(mockDialog, 0, "test"));
 verify(openFileModel).getFile();
 verify(InformationHandler::handle).withArguments(true, UIMessage.INFO_NO_FILE_SELECTED, new Object[0]);
 }

 @Test
 public void testActionPerformed_NullIndexType() {
 when(openFileModel.getFile()).thenReturn(file);
 when(openFileModel.getIndexType()).thenReturn(null);
 action.actionPerformed(new ActionEvent(mockDialog, 0, "test"));
 verify(openFileModel).getFile();
 verify(openFileModel).getIndexType();
 verify(InformationHandler::handle).withArguments(true, UIMessage.INFO_INVALID_INDEX_TYPE, new Object[0]);
 }

 @Test
 public void testActionPerformed_ValidFileAndIndexType() throws IOException {
 when(openFileModel.getFile()).thenReturn(file);
 when(openFileModel.getIndexType()).thenReturn(IndexType.MEMORY_BASED);
 FileIndexer fileIndexer = spy(new RamFileIndexer(file));
 when(mockFileIndexer.getObserverList()).thenReturn(new Observable[] { mockFileIndexer });
 when(mockFileIndexer.addObserver(action)).thenAnswer(invocation -> fileIndexer);

 action.actionPerformed(new ActionEvent(mockDialog, 0, "test"));
 verify(openFileModel).getFile();
 verify(openFileModel).getIndexType();
 verify(JTailLogger::debug).withArguments("File : ", file);
 verify(JTailLogger::debug).withArguments("Index type : ", IndexType.MEMORY_BASED);

 FileIndexerWorker fileIndexerWorker = new FileIndexerWorker(fileIndexer);
 fileIndexerWorker.execute();
 verify(fileIndexer).addObserver(action);
 }

 @Test
 public void testUpdate_PercentageComplete() {
 mockFileIndexer = mock(Observable.class);
 mockFileIndex = mock(FileIndex.class);
 when(mockFileIndexer.getObserverList()).thenReturn(new Observable[] { mockFileIndexer });
 when(mockFileIndexer.addObserver(action)).thenAnswer(invocation -> mockFileIndex);
 when(mockFileIndex.getPercentageComplete()).thenReturn(100);

 action.update(mockFileIndexer, Integer.valueOf(100));
 verify(mockJTailMainModel).add(new JTailModel(file, new LineReader(file, mockFileIndex)));
 }

 @Test
 public void testUpdate_FileNotFoundException() {
 mockFileIndexer = mock(Observable.class);
 mockFileIndex = mock(FileIndex.class);
 FileNotFoundException exception = new FileNotFoundException();
 when(mockFileIndexer.getObserverList()).thenReturn(new Observable[] { mockFileIndexer });
 when(mockFileIndexer.addObserver(action)).thenAnswer(invocation -> mockFileIndex);
 when(mockFileIndex.getPercentageComplete()).thenThrow(exception);

 action.update(mockFileIndexer, Integer.valueOf(100));
 verify(ExceptionHandler::handle).withArguments(exception, UIMessage.ERROR_FILE_NOT_FOUND, fileName);
 }

 @Test
 public void testUpdate_Exception() {
 mockFileIndexer = mock(Observable.class);
 mockFileIndex = mock(FileIndex.class);
 Exception exception = new Exception();
 when(mockFileIndexer.getObserverList()).thenReturn(new Observable[] { mockFileIndexer });
 when(mockFileIndexer.addObserver(action)).thenAnswer(invocation -> mockFileIndex);
 when(mockFileIndex.getPercentageComplete()).thenThrow(exception);

 action.update(mockFileIndexer, Integer.valueOf(100));
 verify(ExceptionHandler::handle).withArguments(exception, UIMessage.ERROR_GENERIC_MESSAGE, new Object[0]);
 }
}
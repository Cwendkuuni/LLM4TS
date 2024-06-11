package fr.pingtimeout.jtail.gui.action.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JDialog;
import javax.swing.JFrame;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import fr.pingtimeout.jtail.gui.action.IndexFileAction;
import fr.pingtimeout.jtail.gui.controller.JTailMainModel;
import fr.pingtimeout.jtail.gui.model.JTailModel;
import fr.pingtimeout.jtail.gui.message.UIMessage;
import fr.pingtimeout.jtail.io.FileIndexer;
import fr.pingtimeout.jtail.io.FileIndexerWorker;
import fr.pingtimeout.jtail.io.FileReader;
import fr.pingtimeout.jtail.io.index.FileIndex;
import fr.pingtimeout.jtail.io.index.RamFileIndex;
import fr.pingtimeout.jtail.io.index.RomFileIndex;
import fr.pingtimeout.jtail.util.JTailLogger;

public class IndexFileActionTest {

 @Mock
 private JTailMainModel jTailMainModel;

 @Mock
 private OpenFileModel openFileModel;

 @Mock
 private JDialog dialog;

 @Mock
 private FileIndex index;

 @Mock
 private File file;

 @Mock
 private LineReader lineReader;

 @Mock
 private JTailModel jTailModel;

 @Mock
 private UIMessage informationHandler;

 @Mock
 private UIMessage exceptionHandler;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 when(openFileModel.getFile()).thenReturn(file);
 when(openFileModel.getIndexType()).thenReturn(IndexType.MEMORY_BASED);
 when(openFileModel.getFile()).thenReturn(file);
 when(file.exists()).thenReturn(true);

 when(jTailMainModel.add(jTailModel)).thenReturn(true);

 when(dialog.isVisible()).thenReturn(true);
 when(dialog.getParent()).thenReturn(new JFrame());
 }

 @Test
 public void testActionPerformed_ValidFile_Success() {
 IndexFileAction action = new IndexFileAction(jTailMainModel, openFileModel);
 ActionEvent actionEvent = mock(ActionEvent.class);

 action.actionPerformed(actionEvent);

 verify(jTailLogger, times(2)).debug(anyString());
 verify(informationHandler, never()).handle(anyBoolean(), any(UIMessage.class), any(Object[].class));

 ArgumentCaptor<FileIndexer> fileIndexerCaptor = ArgumentCaptor.forClass(FileIndexer.class);
 verify(fileIndexerWorker).execute();
 verify(fileIndexerWorker).getFileIndexer(fileIndexerCaptor.capture());
 FileIndex fileIndex = fileIndexerCaptor.getValue();
 assertEquals(RamFileIndex.class, fileIndex.getClass());
 verify(fileIndexer).addObserver(action);
 verify(jTailMainModel).add(jTailModel);
 }

 @Test(expected = FileNotFoundException.class)
 public void testActionPerformed_InvalidFile_Failure() {
 when(file.exists()).thenReturn(false);
 IndexFileAction action = new IndexFileAction(jTailMainModel, openFileModel);
 ActionEvent actionEvent = mock(ActionEvent.class);

 action.actionPerformed(actionEvent);

 verify(jTailLogger, times(2)).debug(anyString());
 verify(exceptionHandler).handle(any(Exception.class), eq(UIMessage.ERROR_FILE_NOT_FOUND), any(Object[].class));
 }

 @Test
 public void testUpdate_Success() {
 IndexFileAction action = new IndexFileAction(jTailMainModel, openFileModel);
 FileIndexer fileIndexer = mock(FileIndexer.class);
 when(fileIndexer.getFile()).thenReturn(file);
 when(fileIndexer.getIndex()).thenReturn(index);
 when(lineReader.getLine()).thenReturn("");
 when(fileIndexer.addObserver(any(Observer.class))).thenAnswer(invocation -> fileIndexer);

 ArgumentCaptor<Observer> observerCaptor = ArgumentCaptor.forClass(Observer.class);
 action.update(fileIndexer, 100);

 verify(jTailLogger, times(3)).debug(anyString());
 verify(jTailMainModel).add(jTailModel);
 verify(observerCaptor.capture(), times(1)).update(any(Observable.class), any(Object[].class));
 JTailModel jTailModel = observerCaptor.getValue();
 assertNotNull(jTailModel);
 }

 @Test
 public void testUpdate_Failure() {
 IndexFileAction action = new IndexFileAction(jTailMainModel, openFileModel);
 FileIndexer fileIndexer = mock(FileIndexer.class);
 when(fileIndexer.getFile()).thenReturn(file);
 when(fileIndexer.getIndex()).thenThrow(new IOException("IOException"));

 ArgumentCaptor<Observer> observerCaptor = ArgumentCaptor.forClass(Observer.class);
 action.update(fileIndexer, 100);

 verify(jTailLogger, times(3)).debug(anyString());
 verify(exceptionHandler).handle(any(Exception.class), eq(UIMessage.ERROR_GENERIC_MESSAGE), any(Object[].class));
 }
}
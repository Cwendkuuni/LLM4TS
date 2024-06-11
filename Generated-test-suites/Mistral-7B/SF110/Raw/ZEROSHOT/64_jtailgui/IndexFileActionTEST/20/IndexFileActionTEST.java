package fr.pingtimeout.jtail.gui.action;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.Observable;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import fr.pingtimeout.jtail.gui.controller.*;
import fr.pingtimeout.jtail.gui.message.*;
import fr.pingtimeout.jtail.gui.model.*;
import fr.pingtimeout.jtail.io.*;
import fr.pingtimeout.jtail.io.index.*;

public class IndexFileActionTest {

 private IndexFileAction indexFileAction;
 @Mock
 private JTailMainModel jTailMainModel;
 @Mock
 private OpenFileModel openFileModel;
 @Mock
 private JDialog dialog;
 @Mock
 private LineReader lineReader;
 @Mock
 private JTailModel jTailModel;
 @Mock
 private FileIndex fileIndex;
 @Mock
 private FileIndexer fileIndexer;
 @Mock
 private FileIndexerWorker fileIndexerWorker;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
 }

 @Test
 public void testActionPerformed_ValidFileAndIndexType_IndexFile() throws IOException {
 File file = mock(File.class);
 OpenIndexType indexType = OpenIndexType.FILE_BASED;
 when(openFileModel.getFile()).thenReturn(file);
 when(openFileModel.getIndexType()).thenReturn(indexType);
 when(fileIndexer.addObserver(any())).thenReturn(fileIndexer);
 when(new FileIndexer(file, RamFileIndex.class)).thenReturn(fileIndexer);

 ActionEvent actionEvent = mock(ActionEvent.class);
 indexFileAction.actionPerformed(actionEvent);

 ArgumentCaptor<Observer> observerArgumentCaptor = ArgumentCaptor.forClass(Observer.class);
 verify(fileIndexer).addObserver(observerArgumentCaptor.capture());
 Observer observer = observerArgumentCaptor.getValue();

 verify(jTailMainModel).add(argThat(m -> m instanceof JTailModel));

 observer.update(fileIndexer, 100);
 verify(jTailMainModel).add(eq(jTailModel));
 }

 @Test(expected = FileNotFoundException.class)
 public void testActionPerformed_InvalidFile_ThrowFileNotFoundException() throws IOException {
 File file = mock(File.class);
 OpenIndexType indexType = OpenIndexType.FILE_BASED;
 when(openFileModel.getFile()).thenReturn(file);
 when(openFileModel.getIndexType()).thenReturn(indexType);
 when(new FileIndexer(file, RomFileIndex.class)).thenThrow(new FileNotFoundException());

 ActionEvent actionEvent = mock(ActionEvent.class);
 indexFileAction.actionPerformed(actionEvent);
 }

 @Test(expected = ExceptionInInitializerError.class)
 public void testStaticInitialization_ExceptionInInitializerError() {
 Class<?> clazz = null;
 try {
 clazz = Class.forName("fr.pingtimeout.jtail.gui.action.OpenFileAction");
 }
 catch (ClassNotFoundException e) {
 throw new ExceptionInInitializerError(e);
 }
 }

 @Test
 public void testUpdate_FileIndexingCompleted_AddModelToMainModel() {
 indexFileAction.update(fileIndexer, 100);
 verify(jTailMainModel).add(eq(jTailModel));
 }
}
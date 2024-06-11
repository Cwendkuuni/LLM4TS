package fr.pingtimeout.jtail.gui.action;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import fr.pingtimeout.jtail.gui.controller.JTailMainModel;
import fr.pingtimeout.jtail.gui.message.InformationHandler;
import fr.pingtimeout.jtail.gui.message.UIMessage;
import fr.pingtimeout.jtail.gui.model.JTailModel;
import fr.pingtimeout.jtail.gui.observer.Observer;
import fr.pingtimeout.jtail.io.FileIndexer;
import fr.pingtimeout.jtail.io.FileIndexerWorker;
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
 private InformationHandler informationHandler;

 @Mock
 private FileIndexer fileIndexer;

 @Mock
 private Observer observer;

 @Mock
 private JDialog dialog;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testActionPerformed_NullFile_InfoMessageShown() {
 // Given
 when(openFileModel.getFile()).thenReturn(null);
 when(openFileModel.getIndexType()).thenReturn(IndexType.FILE_BASED);

 // When
 IndexFileAction indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
 indexFileAction.actionPerformed(mock(ActionEvent.class));

 // Then
 verify(informationHandler).handle(eq(true), any(UIMessage.class), anyArray());
 }

 @Test
 public void testActionPerformed_ValidFile_CorrectIndexerUsed() {
 // Given
 when(openFileModel.getFile()).thenReturn(new File("test.txt"));
 when(openFileModel.getIndexType()).thenReturn(IndexType.MEMORY_BASED);

 // When
 IndexFileAction indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
 indexFileAction.actionPerformed(mock(ActionEvent.class));

 // Then
 verify(fileIndexerClass(IndexType.MEMORY_BASED)).isInstance(fileIndexer);
 }

 @Test
 public void testActionPerformed_ValidFile_FileIndexed() throws IOException {
 // Given
 File file = new File("test.txt");
 when(openFileModel.getFile()).thenReturn(file);
 when(openFileModel.getIndexType()).thenReturn(IndexType.FILE_BASED);
 when(fileIndexer.getFile()).thenReturn(file);
 when(fileIndexer.addObserver(any())).thenReturn(observer);
 FileIndex index = mock(FileIndex.class);
 when(fileIndexer.getIndex()).thenReturn(index);
 when(jTailMainModel.add(any())).thenReturn(true);

 // When
 IndexFileAction indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
 indexFileAction.actionPerformed(mock(ActionEvent.class));

 // Then
 verify(fileIndexer).addObserver(eq(observer));
 verify(jTailMainModel).add(argThat(jTailModel -> jTailModel instanceof JTailModel));
 }

 @Test
 public void testActionPerformed_ValidFile_InfoMessageNotShown() {
 // Given
 when(openFileModel.getFile()).thenReturn(new File("test.txt"));
 when(openFileModel.getIndexType()).thenReturn(IndexType.FILE_BASED);

 // When
 IndexFileAction indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
 indexFileAction.actionPerformed(mock(ActionEvent.class));

 // Then
 verify(informationHandler, never()).handle(eq(true), any(UIMessage.class), anyArray());
 }

 @Test
 public void testUpdate_FileIndexed_JTailModelAdded() throws IOException {
 // Given
 File file = new File("test.txt");
 when(openFileModel.getFile()).thenReturn(file);
 when(openFileModel.getIndexType()).thenReturn(IndexType.FILE_BASED);
 when(fileIndexer.getFile()).thenReturn(file);
 when(fileIndexer.getIndex()).thenReturn(new FileIndex());

 // When
 IndexFileAction indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
 indexFileAction.update(fileIndexer, 100);

 // Then
 verify(jTailMainModel).add(argThat(jTailModel -> jTailModel instanceof JTailModel));
 }

 @Test
 public void testUpdate_FileIndexingFailed_ErrorMessageShown() throws IOException {
 // Given
 File file = new File("test.txt");
 when(openFileModel.getFile()).thenReturn(file);
 when(openFileModel.getIndexType()).thenReturn(IndexType.FILE_BASED);
 when(fileIndexer.getFile()).thenReturn(file);
 when(fileIndexer.getIndex()).thenThrow(new IOException());

 // When
 IndexFileAction indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
 indexFileAction.update(fileIndexer, 100);

 // Then
 verify(exceptionHandler).handle(any(IOException.class), eq(UIMessage.ERROR_FILE_NOT_FOUND), eq(file.getName()));
 }

 @Test
 public void testUpdate_FileIndexingFailed_GenericErrorMessageShown() throws IOException {
 // Given
 File file = new File("test.txt");
 when(openFileModel.getFile()).thenReturn(file);
 when(openFileModel.getIndexType()).thenReturn(IndexType.FILE_BASED);
 when(fileIndexer.getFile()).thenReturn(file);
 when(fileIndexer.getIndex()).thenThrow(new RuntimeException());

 // When
 IndexFileAction indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
 indexFileAction.update(fileIndexer, 100);

 // Then
 verify(exceptionHandler).handle(any(RuntimeException.class), eq(UIMessage.ERROR_GENERIC_MESSAGE), anyArray());
 }

 private Class<? extends FileIndex> fileIndexerClass(IndexType indexType) {
 switch (indexType) {
 case MEMORY_BASED:
 return RamFileIndex.class;
 case FILE_BASED:
 return RomFileIndex.class;
 default:
 throw new IllegalStateException();
 }
 }
}
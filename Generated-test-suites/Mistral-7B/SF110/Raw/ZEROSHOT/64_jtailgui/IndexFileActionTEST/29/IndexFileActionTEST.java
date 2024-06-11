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
import org.mockito.Mockito;

import fr.pingtimeout.jtail.gui.controller.JTailMainModel;
import fr.pingtimeout.jtail.gui.message.InformationHandler;
import fr.pingtimeout.jtail.gui.message.UIMessage;
import fr.pingtimeout.jtail.gui.model.JTailModel;
import fr.pingtimeout.jtail.gui.model.OpenFileModel;
import fr.pingtimeout.jtail.io.index.FileIndexer;
import fr.pingtimeout.jtail.io.index.FileIndexerWorker;
import fr.pingtimeout.jtail.io.index.RamFileIndex;
import fr.pingtimeout.jtail.io.index.RomFileIndex;
import fr.pingtimeout.jtail.io.line.LineReader;
import fr.pingtimeout.jtail.util.JTailLogger;

public class IndexFileActionTest {

 private JTailMainModel jTailMainModel;
 private OpenFileModel openFileModel;
 private IndexFileAction indexFileAction;
 private JTailLogger mockLogger;
 private InformationHandler mockInformationHandler;
 private JDialog mockDialog;
 private File mockFile;
 private FileIndexer mockFileIndexer;
 private FileIndexerWorker mockFileIndexerWorker;

 @Before
 public void setUp() {
 jTailMainModel = mock(JTailMainModel.class);
 openFileModel = mock(OpenFileModel.class);
 indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
 mockLogger = mock(JTailLogger.class);
 mockInformationHandler = mock(InformationHandler.class);
 mockDialog = mock(JDialog.class);
 mockFile = mock(File.class);
 mockFileIndexer = mock(FileIndexer.class);
 mockFileIndexerWorker = mock(FileIndexerWorker.class);

 when(openFileModel.getFile()).thenReturn(mockFile);
 when(openFileModel.getIndexType()).thenReturn(IndexType.FILE_BASED);
 when(jTailMainModel.getModelCount()).thenReturn(0);
 when(InformationHandler.class.handle(Mockito.anyBoolean(), Mockito.eq(UIMessage.INFO_NO_FILE_SELECTED), Mockito.anyArray(Object.class))).thenReturn(true);
 when(mockDialog.isVisible()).thenReturn(false);
 when(mockFileIndexer.getFile()).thenReturn(mockFile);
 when(mockFileIndexer.getIndex()).thenReturn(Mockito.mock(LineReader.class));
 when(mockFileIndexerWorker.getIndexer()).thenReturn(mockFileIndexer);
 }

 @Test
 public void testActionPerformedWithValidFileAndIndexType() {
 // Given
 ArgumentCaptor<ActionEvent> eventArgumentCaptor = ArgumentCaptor.forClass(ActionEvent.class);

 when(openFileModel.getFile()).thenReturn(mockFile);
 when(openFileModel.getIndexType()).thenReturn(IndexType.FILE_BASED);
 when(mockDialog.isVisible()).thenReturn(false);

 // When
 indexFileAction.actionPerformed(eventArgumentCaptor.capture());

 // Then
 verify(jTailLogger).debug("File : {}", mockFile);
 verify(jTailLogger).debug("Index type : {}", IndexType.FILE_BASED);
 verifyZeroInteractions(mockInformationHandler);
 verifyZeroInteractions(jTailMainModel);

 ArgumentCaptor<FileIndexerWorker> fileIndexerWorkerArgumentCaptor = ArgumentCaptor.forClass(FileIndexerWorker.class);
 verify(jTailMainModel).add(fileIndexerWorkerArgumentCaptor.capture());

 FileIndexerWorker fileIndexerWorker = fileIndexerWorkerArgumentCaptor.getValue();
 verify(fileIndexerWorker).execute();
 }

 @Test
 public void testActionPerformedWithNullFile() {
 // Given
 when(openFileModel.getFile()).thenReturn(null);

 // When
 indexFileAction.actionPerformed(mock(ActionEvent.class));

 // Then
 verify(InformationHandler.class).handle(Mockito.eq(true), Mockito.eq(UIMessage.INFO_NO_FILE_SELECTED), Mockito.anyArray(Object.class));
 verifyZeroInteractions(jTailMainModel);
 }

 @Test
 public void testUpdateWithSuccessfulFileIndexing() {
 // Given
 when(openFileModel.getFile()).thenReturn(mockFile);
 when(openFileModel.getIndexType()).thenReturn(IndexType.FILE_BASED);
 when(mockDialog.isVisible()).thenReturn(false);
 when(mockFileIndexer.getFile()).thenReturn(mockFile);
 when(mockFileIndexer.getIndex()).thenReturn(Mockito.mock(LineReader.class));
 when(jTailMainModel.getModelCount()).thenReturn(0);

 // When
 indexFileAction.actionPerformed(mock(ActionEvent.class));
 verify(mockFileIndexer).addObserver(eq(indexFileAction));
 verify(mockFileIndexerWorker).execute();
 ArgumentCaptor<JTailModel> jTailModelArgumentCaptor = ArgumentCaptor.forClass(JTailModel.class);
 verify(jTailMainModel).add(jTailModelArgumentCaptor.capture());
 JTailModel jTailModel = jTailModelArgumentCaptor.getValue();

 // Then
 verify(mockFileIndexer).addObserver(eq(indexFileAction));
 verify(mockFileIndexerWorker).execute();
 verify(InformationHandler.class).handle(Mockito.eq(true), Mockito.eq(UIMessage.INFO_FILE_ADDED), new Object[]{jTailModel});
 }

 @Test
 public void testUpdateWithFailedFileIndexing() {
 // Given
 when(openFileModel.getFile()).thenReturn(mockFile);
 when(openFileModel.getIndexType()).thenReturn(IndexType.FILE_BASED);
 when(mockDialog.isVisible()).thenReturn(false);
 when(mockFileIndexer.getFile()).thenReturn(mockFile);
 when(mockFileIndexer.getIndex()).thenReturn(Mockito.mock(LineReader.class));
 when(jTailMainModel.getModelCount()).thenReturn(0);
 when(mockFileIndexerWorker.getIndexer()).thenThrow(new IOException());

 // When
 indexFileAction.actionPerformed(mock(ActionEvent.class));

 // Then
 verify(InformationHandler.class).handle(Mockito.eq(true), Mockito.eq(UIMessage.ERROR_GENERIC_MESSAGE), Mockito.anyArray(Object.class));
 verifyZeroInteractions(jTailMainModel);
 }

 @Test
 public void testUpdateWithFailedFileNotFoundException() {
 // Given
 when(openFileModel.getFile()).thenReturn(mockFile);
 when(openFileModel.getIndexType()).thenReturn(IndexType.FILE_BASED);
 when(mockDialog.isVisible()).thenReturn(false);
 when(mockFileIndexer.getFile()).thenThrow(new FileNotFoundException());
 when(jTailMainModel.getModelCount()).thenReturn(0);

 // When
 indexFileAction.actionPerformed(mock(ActionEvent.class));

 // Then
 verify(InformationHandler.class).handle(Mockito.eq(true), Mockito.eq(UIMessage.ERROR_FILE_NOT_FOUND), Mockito.eq(mockFile.getName()));
 verifyZeroInteractions(jTailMainModel);
 }

 @Test
 public void testObserverNotification() {
 // Given
 when(openFileModel.getFile()).thenReturn(mockFile);
 when(openFileModel.getIndexType()).thenReturn(IndexType.FILE_BASED);
 when(mockDialog.isVisible()).thenReturn(false);
 when(mockFileIndexer.getFile()).thenReturn(mockFile);
 when(mockFileIndexer.getIndex()).thenReturn(Mockito.mock(LineReader.class));
 when(jTailMainModel.getModelCount()).thenReturn(0);
 when(mockFileIndexerWorker.getIndexer()).thenReturn(mockFileIndexer);

 // When
 indexFileAction.actionPerformed(mock(ActionEvent.class));
 verify(mockFileIndexer).addObserver(eq(indexFileAction));

 // Then
 verify(mockFileIndexer).addObserver(eq(indexFileAction));
 verifyZeroInteractions(jTailMainModel);

 // When
 verify(indexFileAction).update(eq(mockFileIndexer), Mockito.eq(100));

 // Then
 JTailModel jTailModel = Mockito.mock(JTailModel.class);
 verify(jTailMainModel).add(jTailModel);
 }

 enum IndexType {
 MEMORY_BASED,
 FILE_BASED
 }
}
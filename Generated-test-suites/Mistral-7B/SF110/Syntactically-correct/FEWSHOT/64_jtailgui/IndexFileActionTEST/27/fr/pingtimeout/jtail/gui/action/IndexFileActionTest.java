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

import fr.pingtimeout.jtail.gui.controller.ExceptionHandler;
import fr.pingtimeout.jtail.gui.controller.InformationHandler;
import fr.pingtimeout.jtail.gui.language.UIMessage;
import fr.pingtimeout.jtail.gui.message.UIMessage.MessageType;
import fr.pingtimeout.jtail.gui.model.JTailModel;
import fr.pingtimeout.jtail.io.FileIndexer;
import fr.pingtimeout.jtail.io.FileIndexerWorker;
import fr.pingtimeout.jtail.io.FileIndexer.FileIndexType;
import fr.pingtimeout.jtail.io.index.FileIndex;
import fr.pingtimeout.jtail.io.index.RamFileIndex;
import fr.pingtimeout.jtail.io.index.RomFileIndex;
import fr.pingtimeout.jtail.gui.model.JTailMainModel;

public class IndexFileActionTest {

 private IndexFileAction indexFileAction;
 private JTailMainModel jTailMainModel;
 private OpenFileModel openFileModel;
 private ActionEvent actionEvent;
 private JDialog dialog;
 private File file;
 private Observable observable;
 private FileIndex index;
 private FileIndexer fileIndexer;
 private FileIndexerWorker fileIndexerWorker;

 @Before
 public void setUp() {
 jTailMainModel = mock(JTailMainModel.class);
 openFileModel = mock(OpenFileModel.class);
 actionEvent = mock(ActionEvent.class);
 dialog = mock(JDialog.class);
 file = new File("testFile.txt");
 observable = mock(Observable.class);
 index = mock(FileIndex.class);
 fileIndexer = mock(FileIndexer.class);
 fileIndexerWorker = mock(FileIndexerWorker.class);

 indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
 }

 @Test
 public void testActionPerformed() {
 // Given
 when(openFileModel.getFile()).thenReturn(file);
 when(openFileModel.getIndexType()).thenReturn(FileIndexType.FILE_BASED);
 when(dialog.isVisible()).thenReturn(true);

 // When
 indexFileAction.actionPerformed(actionEvent);

 // Then
 verify(jTailMainModel).add(any(JTailModel.class));
 verify(InformationHandler).handle(true, UIMessage.INFO_NO_FILE_SELECTED, new Object[0]);
 }

 @Test
 public void testActionPerformedWithFile() {
 // Given
 when(openFileModel.getFile()).thenReturn(file);
 when(openFileModel.getIndexType()).thenReturn(FileIndexType.FILE_BASED);
 when(indexerClass()).thenReturn(RomFileIndex.class);
 when(dialog.isVisible()).thenReturn(true);
 when(fileIndexerWorker.execute()).thenReturn(true);
 when(fileIndexer.getFile()).thenReturn(file);
 when(fileIndexer.getIndex()).thenReturn(index);

 // When
 indexFileAction.actionPerformed(actionEvent);

 // Then
 ArgumentCaptor<FileIndexer> fileIndexerCaptor = ArgumentCaptor.forClass(FileIndexer.class);
 verify(jTailMainModel).add(any(JTailModel.class));
 verify(fileIndexer).addObserver(indexFileAction);
 verify(fileIndexerWorker).execute();
 verify(fileIndexer).setFile(file);
 verify(fileIndexer).setIndex(index);
 }

 @Test
 public void testActionPerformedWithMemoryBasedIndex() {
 // Given
 when(openFileModel.getFile()).thenReturn(file);
 when(openFileModel.getIndexType()).thenReturn(FileIndexType.MEMORY_BASED);
 when(indexerClass()).thenReturn(RamFileIndex.class);
 when(dialog.isVisible()).thenReturn(true);
 when(fileIndexerWorker.execute()).thenReturn(true);

 // When
 indexFileAction.actionPerformed(actionEvent);

 // Then
 ArgumentCaptor<FileIndexer> fileIndexerCaptor = ArgumentCaptor.forClass(FileIndexer.class);
 verify(jTailMainModel).add(any(JTailModel.class));
 verify(fileIndexer).addObserver(indexFileAction);
 verify(fileIndexerWorker).execute();
 verify(fileIndexer).setFile(file);
 }

 @Test(expected = FileNotFoundException.class)
 public void testActionPerformedWithNotFoundException() {
 // Given
 when(openFileModel.getFile()).thenReturn(file);
 when(openFileModel.getIndexType()).thenReturn(FileIndexType.FILE_BASED);
 when(dialog.isVisible()).thenReturn(true);
 when(fileIndexerClass()).thenThrow(new FileNotFoundException("Exception message"));

 // When
 indexFileAction.actionPerformed(actionEvent);

 // Then
 verify(ExceptionHandler).handle(any(Exception.class), eq(UIMessage.ERROR_FILE_NOT_FOUND), eq(file.getName()));
 }

 @Test
 public void testUpdate() {
 // Given
 when(observable.getObserverCount()).thenReturn(1);
 when(observable.setChanged(anyInt(), anyBoolean())).thenReturn(true);

 // When
 indexFileAction.update(observable, 100);

 // Then
 verify(jTailMainModel).add(any(JTailModel.class));
 }

 private Class<? extends FileIndexer> indexerClass() {
 if (openFileModel.getIndexType() == FileIndexType.FILE_BASED) {
 return RomFileIndex.class;
 } else {
 return RamFileIndex.class;
 }
 }
}
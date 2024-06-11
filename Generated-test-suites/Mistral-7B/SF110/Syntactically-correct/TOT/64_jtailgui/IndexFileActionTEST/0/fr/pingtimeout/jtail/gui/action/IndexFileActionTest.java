package fr.pingtimeout.jtail.gui.action.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import fr.pingtimeout.jtail.gui.action.IndexFileAction;
import fr.pingtimeout.jtail.gui.controller.JTailMainModel;
import fr.pingtimeout.jtail.gui.message.UIMessage;
import fr.pingtimeout.jtail.gui.model.JTailModel;
import fr.pingtimeout.jtail.io.index.FileIndexer;
import fr.pingtimeout.jtail.io.index.FileIndexerWorker;
import fr.pingtimeout.jtail.io.index.RamFileIndex;

public class IndexFileActionTest {

 @Rule
 public MockitoRule mockitoRule = MockitoJUnit.rule();

 @Mock
 private JTailMainModel jTailMainModel;

 @Mock
 private OpenFileModel openFileModel;

 @Mock
 private FileIndexer fileIndexer;

 @Mock
 private FileIndexerWorker fileIndexerWorker;

 @Before
 public void setup() {
 openFileModel = mock(OpenFileModel.class);
 jTailMainModel = mock(JTailMainModel.class);
 fileIndexer = mock(FileIndexer.class);
 fileIndexerWorker = mock(FileIndexerWorker.class);

 new IndexFileAction(jTailMainModel, openFileModel);
 }

 @Test
 public void testActionPerformed_ValidFileWithMemoryBasedIndexType() {
 File file = mock(File.class);
 when(openFileModel.getFile()).thenReturn(file);
 when(openFileModel.getIndexType()).thenReturn(IndexType.MEMORY_BASED);

 ArgumentCaptor<ActionEvent> actionEventCaptor = ArgumentCaptor.forClass(ActionEvent.class);
 IndexFileAction indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
 indexFileAction.actionPerformed(actionEventCaptor.capture());

 verify(jTailLogger).debug("File : {}", file);
 verify(jTailLogger).debug("Index type : {}", IndexType.MEMORY_BASED);

 ArgumentCaptor<Component> dialogCaptor = ArgumentCaptor.forClass(Component.class);
 verify(dialog).setVisible(false);

 verifyNoMoreInteractions(InformationHandler::handle);

 verify(fileIndexerWorker).execute();
 verify(fileIndexer).addObserver(indexFileAction);
 verify(jTailMainModel).add(new JTailModel(file, fileIndexer));
 }

 @Test
 public void testActionPerformed_NullFile() {
 when(openFileModel.getFile()).thenReturn(null);

 IndexFileAction indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
 indexFileAction.actionPerformed(mock(ActionEvent.class));

 verify(InformationHandler).handle(true, UIMessage.INFO_NO_FILE_SELECTED, new Object[0]);
 }

 @Test
 public void testActionPerformed_InvalidFileWithMemoryBasedIndexType() {
 File file = mock(File.class);
 when(openFileModel.getFile()).thenReturn(file);
 when(openFileModel.getIndexType()).thenReturn(IndexType.MEMORY_BASED);

 doThrow(new FileNotFoundException("Test error")).when(fileIndexer).init(file, RamFileIndex.class);

 IndexFileAction indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
 indexFileAction.actionPerformed(mock(ActionEvent.class));

 verify(ExceptionHandler).handle(any(Exception.class), eq(UIMessage.ERROR_FILE_NOT_FOUND), anyString());
 }

 @Test
 public void testActionPerformed_ValidFileWithFileBasedIndexType() {
 File file = mock(File.class);
 when(openFileModel.getFile()).thenReturn(file);
 when(openFileModel.getIndexType()).thenReturn(IndexType.FILE_BASED);

 ArgumentCaptor<ActionEvent> actionEventCaptor = ArgumentCaptor.forClass(ActionEvent.class);
 IndexFileAction indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
 indexFileAction.actionPerformed(actionEventCaptor.capture());

 verify(jTailLogger).debug("File : {}", file);
 verify(jTailLogger).debug("Index type : {}", IndexType.FILE_BASED);

 ArgumentCaptor<Component> dialogCaptor = ArgumentCaptor.forClass(Component.class);
 verify(dialog).setVisible(false);

 verifyNoMoreInteractions(InformationHandler::handle);

 verify(fileIndexerWorker).execute();
 verify(fileIndexer).addObserver(indexFileAction);
 verify(jTailMainModel).add(new JTailModel(file, fileIndexer));
 }

 @Test(expected = AssertionError.class)
 public void testActionPerformed_InvalidFileWithFileBasedIndexType() {
 File file = mock(File.class);
 when(openFileModel.getFile()).thenReturn(file);
 when(openFileModel.getIndexType()).thenReturn(IndexType.FILE_BASED);

 doThrow(new FileNotFoundException("Test error")).when(fileIndexer).init(file, RomFileIndex.class);

 IndexFileAction indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
 indexFileAction.actionPerformed(mock(ActionEvent.class));
 }

 @Test
 public void testUpdate_IndexingCompleted() {
 ArgumentCaptor<Observable> observableCaptor = ArgumentCaptor.forClass(Observable.class);
 ArgumentCaptor<Object> argCaptor = ArgumentCaptor.forClass(Object.class);

 IndexFileAction indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
 indexFileAction.update(observableCaptor.capture(), argCaptor.capture());

 verify(jTailLogger).debug("Notification received : 100 emitted by {}", fileIndexer);

 JTailModel jTailModel = new JTailModel(fileIndexer.getFile(), fileIndexer);
 assertEquals(jTailModel, observableCaptor.getValue());
 }
}
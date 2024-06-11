package fr.pingtimeout.jtail.gui.action.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import fr.pingtimeout.jtail.gui.action.IndexFileAction;
import fr.pingtimeout.jtail.gui.model.JTailMainModel;
import fr.pingtimeout.jtail.gui.model.JTailModel;
import fr.pingtimeout.jtail.io.index.FileIndexer;
import fr.pingtimeout.jtail.io.index.FileIndexerWorker;
import fr.pingtimeout.jtail.io.index.RamFileIndex;
import fr.pingtimeout.jtail.io.index.RomFileIndex;

public class IndexFileActionTest {

 private IndexFileAction indexFileAction;
 private JTailMainModel jTailMainModel;
 private OpenFileModel openFileModel;
 private FileIndexer fileIndexer;
 private FileIndexerWorker fileIndexerWorker;

 @Before
 public void setUp() {
 jTailMainModel = mock(JTailMainModel.class);
 openFileModel = new OpenFileModel();
 indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
 fileIndexer = mock(FileIndexer.class);
 fileIndexerWorker = mock(FileIndexerWorker.class);
 }

 @Test
 public void testActionPerformed_ValidFileAndIndexType() {
 File file = new File("validFile.txt");
 openFileModel.setFile(file);
 openFileModel.setIndexType(IndexType.MEMORY_BASED);

 when(fileIndexer.addObserver(any())).thenReturn(fileIndexer);
 when(fileIndexerWorker.execute()).thenReturn(fileIndexerWorker.getResult());

 ActionEvent actionEvent = new ActionEvent(new JButton(), ActionEvent.ACTION_PERFORMED, "openFileAction");
 indexFileAction.actionPerformed(actionEvent);

 verify(jTailMainModel).add(argThat(m -> m instanceof JTailModel));
 }

 @Test(expected = FileNotFoundException.class)
 public void testActionPerformed_InvalidFile() {
 openFileModel.setFile(null);

 ActionEvent actionEvent = new ActionEvent(new JButton(), ActionEvent.ACTION_PERFORMED, "openFileAction");
 indexFileAction.actionPerformed(actionEvent);

 InformationHandler.handle(true, UIMessage.INFO_NO_FILE_SELECTED, new Object[0]);
 }

 @Test(expected = IllegalStateException.class)
 public void testActionPerformed_InvalidIndexType() {
 openFileModel.setFile(new File("validFile.txt"));
 openFileModel.setIndexType(IndexType.INVALID);

 ActionEvent actionEvent = new ActionEvent(new JButton(), ActionEvent.ACTION_PERFORMED, "openFileAction");
 indexFileAction.actionPerformed(actionEvent);

 ExceptionHandler.handle(any(Exception.class), UIMessage.ERROR_INVALID_INDEX_TYPE, new Object[0]);
 }

 @Test
 public void testUpdate_FileIndexingCompleted() {
 File file = new File("validFile.txt");
 openFileModel.setFile(file);
 openFileModel.setIndexType(IndexType.MEMORY_BASED);
 when(fileIndexer.getFile()).thenReturn(file);
 FileIndex index = mock(FileIndex.class);
 when(fileIndexer.getIndex()).thenReturn(index);
 LineReader lineReader = mock(LineReader.class);
 JTailModel model = mock(JTailModel.class);

 indexFileAction.update(fileIndexer, 100);

 verify(jTailMainModel).add(model);
 }

 @Test
 public void testUpdate_FileIndexingFailed() {
 File file = new File("validFile.txt");
 openFileModel.setFile(file);
 openFileModel.setIndexType(IndexType.MEMORY_BASED);
 when(fileIndexer.getFile()).thenThrow(new IOException());

 indexFileAction.update(fileIndexer, 100);

 ExceptionHandler.handle(any(Exception.class), UIMessage.ERROR_FILE_NOT_FOUND, file.getName());
 }

 @Test
 public void testUpdate_FileIndexingFailed_GenericError() {
 File file = new File("validFile.txt");
 openFileModel.setFile(file);
 openFileModel.setIndexType(IndexType.MEMORY_BASED);
 when(fileIndexer.getFile()).thenThrow(new RuntimeException());

 indexFileAction.update(fileIndexer, 100);

 ExceptionHandler.handle(any(Exception.class), UIMessage.ERROR_GENERIC_MESSAGE, new Object[0]);
 }

 @Test
 public void testGetOpenFileModel() {
 IndexFileAction indexFileAction = new IndexFileAction(new JTailMainModel(), new OpenFileModel());
 OpenFileModel openFileModel = indexFileAction.getOpenFileModel();
 assertEquals(openFileModel, indexFileAction.getOpenFileModel());
 }

}
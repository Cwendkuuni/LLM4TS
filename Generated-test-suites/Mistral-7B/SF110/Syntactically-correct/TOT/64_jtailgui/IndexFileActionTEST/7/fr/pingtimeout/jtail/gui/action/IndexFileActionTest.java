package fr.pingtimeout.jtail.gui.action.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.util.Observable;

import org.junit.Before;
import org.junit.Test;

import fr.pingtimeout.jtail.gui.action.IndexFileAction;
import fr.pingtimeout.jtail.gui.action.OpenFileModel;
import fr.pingtimeout.jtail.gui.controller.JTailMainModel;
import fr.pingtimeout.jtail.gui.model.JTailModel;
import fr.pingtimeout.jtail.io.index.FileIndexer;
import fr.pingtimeout.jtail.io.index.FileIndexerObserver;
import fr.pingtimeout.jtail.io.index.RamFileIndex;
import fr.pingtimeout.jtail.io.index.RomFileIndex;
import fr.pingtimeout.jtail.util.JTailLogger;

public class IndexFileActionTest {

 private IndexFileAction action;
 private OpenFileModel openFileModel;
 private JTailMainModel jTailMainModel;
 private Observable o;
 private FileIndexer fileIndexer;
 private File indexFile;

 @Before
 public void setUp() {
 JTailLogger.init();
 openFileModel = mock(OpenFileModel.class);
 jTailMainModel = mock(JTailMainModel.class);
 action = new IndexFileAction(jTailMainModel, openFileModel);
 o = mock(Observable.class);
 fileIndexer = mock(FileIndexer.class);
 indexFile = new File("testFile.txt");
 }

 @Test
 public void testActionPerformed_ValidFileAndIndexType_CreatesFileIndexerAndAddsToModel() {
 // Given
 when(openFileModel.getFile()).thenReturn(indexFile);
 when(openFileModel.getIndexType()).thenReturn(OpenFileModel.IndexType.FILE_BASED);

 // When
 action.actionPerformed(new ActionEvent(new JButton(), ActionEvent.ACTION_PERFORMED, "test"));

 // Then
 verify(jTailMainModel, times(1)).add(any(JTailModel.class));
 verify(fileIndexer, times(1)).addObserver(any(FileIndexerObserver.class));
 }

 @Test(expected = FileNotFoundException.class)
 public void testActionPerformed_InvalidFile_ThrowsFileNotFoundException() {
 // Given
 when(openFileModel.getFile()).thenThrow(new FileNotFoundException());

 // When
 action.actionPerformed(new ActionEvent(new JButton(), ActionEvent.ACTION_PERFORMED, "test"));

 // Then
 // The test should throw the FileNotFoundException
 }

 @Test
 public void testActionPerformed_InvalidIndexType_DoesNotCreateFileIndexer() {
 // Given
 when(openFileModel.getFile()).thenReturn(indexFile);
 when(openFileModel.getIndexType()).thenReturn(OpenFileModel.IndexType.MEMORY_BASED);

 // When
 action.actionPerformed(new ActionEvent(new JButton(), ActionEvent.ACTION_PERFORMED, "test"));

 // Then
 verify(fileIndexer, never()).addObserver(any(FileIndexerObserver.class));
 }

 @Test
 public void testUpdate_FileIndexingComplete_AddsJTailModelToMainModel() {
 // Given
 when(openFileModel.getFile()).thenReturn(indexFile);
 when(openFileModel.getIndexType()).thenReturn(OpenFileModel.IndexType.FILE_BASED);
 when(fileIndexer.getFile()).thenReturn(indexFile);
 when(fileIndexer.getIndex()).thenReturn(new String[0]);
 o.addObserver(action);

 // When
 action.update(o, 100);

 // Then
 verify(jTailMainModel, times(1)).add(any(JTailModel.class));
 }
}
package fr.pingtimeout.jtail.gui.action;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.event.*;
import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import fr.pingtimeout.jtail.controller.JTailMainModel;
import fr.pingtimeout.jtail.gui.message.InformationHandler;
import fr.pingtimeout.jtail.gui.message.UIMessage;
import fr.pingtimeout.jtail.gui.model.JTailModel;
import fr.pingtimeout.jtail.io.FileIndexer;
import fr.pingtimeout.jtail.io.FileIndexerWorker;
import fr.pingtimeout.jtail.io.FileIndexer.IndexType;
import fr.pingtimeout.jtail.io.index.FileIndex;
import fr.pingtimeout.jtail.io.index.RamFileIndex;
import fr.pingtimeout.jtail.io.index.RomFileIndex;
import fr.pingtimeout.jtail.util.JTailLogger;
import fr.pingtimeout.jtail.util.LineReader;

public class IndexFileActionTest {

 private IndexFileAction indexFileAction;
 private JTailMainModel jTailMainModelMock;
 private OpenFileModel openFileModelMock;
 private InformationHandler informationHandlerMock;
 private FileIndexer fileIndexerMock;
 private FileIndexerWorker fileIndexerWorkerMock;

 @Before
 public void setUp() {
 jTailMainModelMock = mock(JTailMainModel.class);
 openFileModelMock = mock(OpenFileModel.class);
 informationHandlerMock = mock(InformationHandler.class);
 fileIndexerMock = mock(FileIndexer.class);
 fileIndexerWorkerMock = mock(FileIndexerWorker.class);

 indexFileAction = new IndexFileAction(jTailMainModelMock, openFileModelMock);
 indexFileAction.addObserver(informationHandlerMock);
 }

 @Test
 public void testActionPerformedWithNullFile() {
 // Given
 when(openFileModelMock.getFile()).thenReturn(null);

 // When
 indexFileAction.actionPerformed(mock(ActionEvent.class));

 // Then
 verify(informationHandlerMock, times(1)).handle(eq(true), eq(UIMessage.INFO_NO_FILE_SELECTED), anyArray());
 }

 @Test
 public void testActionPerformedWithValidFile() throws IOException {
 // Given
 File file = Mockito.mock(File.class);
 when(openFileModelMock.getFile()).thenReturn(file);
 when(openFileModelMock.getIndexType()).thenReturn(IndexType.FILE_BASED);
 when(fileIndexerMock.newFileIndexer(file, RomFileIndex.class)).thenReturn(fileIndexerMock);

 // When
 indexFileAction.actionPerformed(mock(ActionEvent.class));

 // Then
 verify(jTailMainModelMock, times(1)).add(eq(new JTailModel(file, new LineReader(file, fileIndexerMock.getIndex()))));
 }

 @Test(expected = FileNotFoundException.class)
 public void testActionPerformedWithInvalidFile() throws IOException {
 // Given
 File file = Mockito.mock(File.class);
 when(openFileModelMock.getFile()).thenReturn(file);
 when(openFileModelMock.getIndexType()).thenReturn(IndexType.FILE_BASED);
 doThrow(new FileNotFoundException("Test exception")).when(fileIndexerMock).newFileIndexer(file, RomFileIndex.class);

 // When
 indexFileAction.actionPerformed(mock(ActionEvent.class));

 // Then
 // Exception is expected to be thrown
 }

 @Test
 public void testUpdate() throws IOException {
 // Given
 File file = Mockito.mock(File.class);
 when(openFileModelMock.getFile()).thenReturn(file);
 when(openFileModelMock.getIndexType()).thenReturn(IndexType.MEMORY_BASED);
 FileIndex index = Mockito.mock(FileIndex.class);
 when(fileIndexerMock.getIndex()).thenReturn(index);
 LineReader lineReader = Mockito.mock(LineReader.class);
 when(lineReader.readLine()).thenReturn("Test line");
 when(fileIndexerMock.newFileIndexer(file, RamFileIndex.class)).thenReturn(fileIndexerMock);

 // When
 indexFileAction.update(fileIndexerMock, 100);

 // Then
 verify(jTailMainModelMock, times(1)).add(eq(new JTailModel(file, lineReader)));
 }
}
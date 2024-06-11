package fr.pingtimeout.jtail.gui.action;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import fr.pingtimeout.jtail.gui.action.*;
import fr.pingtimeout.jtail.gui.controller.*;
import fr.pingtimeout.jtail.io.index.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import org.junit.*;
import org.mockito.*;

public class IndexFileActionTest {

 private static final String FILE_NAME = "testfile.txt";
 private static final int INDEX_TYPE_MEMORY_BASED = 0;
 private static final int INDEX_TYPE_FILE_BASED = 1;

 @Before
 public void setUp() {
 JTailMainModel jTailMainModelMock = mock(JTailMainModel.class);
 OpenFileModel openFileModelMock = mock(OpenFileModel.class);
 when(openFileModelMock.getFile()).thenReturn(new File(FILE_NAME));
 when(openFileModelMock.getIndexType()).thenReturn(INDEX_TYPE_MEMORY_BASED);

 IndexFileAction indexFileAction = new IndexFileAction(jTailMainModelMock, openFileModelMock);
 this.indexFileAction = indexFileAction;
 }

 @Test
 public void testActionPerformedWithNullFile() {
 assertNull(this.indexFileAction.openFileModel);
 this.indexFileAction.actionPerformed(mock(ActionEvent.class));
 verify(this.jTailMainModel, never()).add(any(JTailModel.class));
 }

 @Test
 public void testActionPerformedWithValidFileAndIndexType() throws Exception {
 File file = new File(FILE_NAME);
 when(this.openFileModelMock.getFile()).thenReturn(file);
 when(this.openFileModelMock.getIndexType()).thenReturn(INDEX_TYPE_FILE_BASED);

 FileIndex index = mock(FileIndex.class);
 FileIndexer fileIndexer = mock(FileIndexer.class);
 when(fileIndexer.getIndex()).thenReturn(index);
 when(fileIndexer.addObserver(any(Observer.class))).thenReturn(fileIndexer);
 when(new FileIndexer(file, RomFileIndex.class)).thenReturn(fileIndexer);

 JTailModel jTailModel = mock(JTailModel.class);
 this.jTailMainModel.add(jTailModel);

 ActionEvent actionEventMock = mock(ActionEvent.class);
 this.indexFileAction.actionPerformed(actionEventMock);

 verify(this.jTailMainModel).add(jTailModel);
 }

 @Test(expected = FileNotFoundException.class)
 public void testActionPerformedWithInvalidFile() throws Exception {
 when(this.openFileModelMock.getFile()).thenReturn(new File("invalidfile.txt"));
 this.indexFileAction.actionPerformed(mock(ActionEvent.class));
 }

 @Test
 public void testUpdateWithSuccessfulIndexing() throws Exception {
 when(this.openFileModelMock.getFile()).thenReturn(new File(FILE_NAME));
 when(this.openFileModelMock.getIndexType()).thenReturn(INDEX_TYPE_MEMORY_BASED);
 FileIndex index = mock(FileIndex.class);
 FileIndexer fileIndexer = mock(FileIndexer.class);
 when(fileIndexer.getIndex()).thenReturn(index);
 when(new FileIndexer(file, RamFileIndex.class)).thenReturn(fileIndexer);

 LineReader lineReaderMock = mock(LineReader.class);
 JTailModel jTailModel = mock(JTailModel.class);
 when(jTailModel.getFile()).thenReturn(file);
 when(jTailModel.getLineReader()).thenReturn(lineReaderMock);

 this.indexFileAction.update(fileIndexer, 100);

 verify(this.jTailMainModel).add(jTailModel);
 }

 @Test
 public void testUpdateWithFailedIndexing() {
 FileIndex index = mock(FileIndex.class);
 FileIndexer fileIndexer = mock(FileIndexer.class);
 when(fileIndexer.getIndex()).thenReturn(index);
 when(new FileIndexer(new File(FILE_NAME), RamFileIndex.class)).thenThrow(new Exception());

 this.indexFileAction.update(fileIndexer, 100);

 verify(this.jTailMainModel, never()).add(any(JTailModel.class));
 }

 @After
 public void tearDown() throws Exception {
 reset(this.jTailMainModelMock);
 reset(this.openFileModelMock);
 }

}
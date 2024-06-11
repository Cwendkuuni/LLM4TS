package fr.pingtimeout.jtail.gui.action.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JDialog;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import fr.pingtimeout.jtail.gui.action.IndexFileAction;
import fr.pingtimeout.jtail.gui.controller.JTailMainModel;
import fr.pingtimeout.jtail.gui.model.JTailModel;
import fr.pingtimeout.jtail.gui.message.UIMessage;
import fr.pingtimeout.jtail.io.FileIndexer;
import fr.pingtimeout.jtail.io.FileIndexerWorker;
import fr.pingtimeout.jtail.io.index.FileIndex;
import fr.pingtimeout.jtail.io.index.RamFileIndex;
import fr.pingtimeout.jtail.io.index.RomFileIndex;
import fr.pingtimeout.jtail.util.JTailLogger;

public class IndexFileActionTest {

 private IndexFileAction indexFileAction;
 private JTailMainModel jTailMainModelMock;
 private OpenFileModel openFileModelMock;
 private File fileMock;

 @Before
 public void setUp() {
 jTailMainModelMock = mock(JTailMainModel.class);
 openFileModelMock = Mockito.spy(new OpenFileModel());
 fileMock = mock(File.class);

 indexFileAction = new IndexFileAction(jTailMainModelMock, openFileModelMock);
 }

 @Test
 public void testActionPerformed_ValidFileAndIndexType_CreateFileIndexer() throws IOException {
 File file = new File("test.txt");
 OpenFileModel openFileModel = new OpenFileModel();
 openFileModel.setFile(file);
 openFileModel.setIndexType(IndexFileAction.IndexType.FILE_BASED);

 ArgumentCaptor<File> fileArgumentCaptor = ArgumentCaptor.forClass(File.class);
 ArgumentCaptor<Class<? extends FileIndex>> indexerClassArgumentCaptor = ArgumentCaptor.forClass(Class.class);

 when(openFileModelMock.getFile()).thenReturn(file);
 when(openFileModelMock.getIndexType()).thenReturn(IndexFileAction.IndexType.FILE_BASED);

 indexFileAction.actionPerformed(new ActionEvent(fileMock, 0, ""));

 verify(jTailMainModelMock).add(any(JTailModel.class));
 verify(openFileModelMock, times(1)).setFile(file);
 verify(openFileModelMock, times(1)).setIndexType(IndexFileAction.IndexType.FILE_BASED);
 verify(jTailLogger).debug(eq("File : "), eq(file));
 verify(jTailLogger).debug(eq("Index type : "), eq(IndexFileAction.IndexType.FILE_BASED));

 FileIndex index = mock(FileIndex.class);
 FileIndexer fileIndexer = new FileIndexer(file, RomFileIndex.class);
 fileIndexer.addObserver(indexFileAction);

 verify(fileIndexer, times(1)).addObserver(jTailMainModelMock);
 verify(fileIndexerWorker).execute();

 ArgumentCaptor<FileIndexer> fileIndexerArgumentCaptor = ArgumentCaptor.forClass(FileIndexer.class);
 verify(jTailMainModelMock).add(fileIndexerArgumentCaptor.capture());

 JTailModel jTailModel = fileIndexerArgumentCaptor.getValue();
 assertEquals(file, jTailModel.getFile());
 assertEquals(index, jTailModel.getIndex());
 }

 @Test
 public void testActionPerformed_NullFile_DisplayNoFileSelectedMessage() {
 openFileModelMock.setFile(null);

 indexFileAction.actionPerformed(new ActionEvent(fileMock, 0, ""));

 verifyNoMoreInteractions(jTailMainModelMock);
 verify(InformationHandler).handle(eq(true), eq(UIMessage.INFO_NO_FILE_SELECTED), anyArray());
 }

 @Test
 public void testActionPerformed_InvalidFile_DisplayFileNotFoundMessage() {
 File file = mock(File.class);
 when(file.exists()).thenReturn(false);
 openFileModelMock.setFile(file);
 openFileModelMock.setIndexType(IndexFileAction.IndexType.FILE_BASED);

 doThrow(new FileNotFoundException("Test FileNotFoundException")) // NOSONAR
 .when(fileIndexer).init(file, RomFileIndex.class);

 indexFileAction.actionPerformed(new ActionEvent(fileMock, 0, ""));

 verifyNoMoreInteractions(jTailMainModelMock);
 verify(ExceptionHandler).handle(eq(new FileNotFoundException("Test FileNotFoundException")), eq(UIMessage.ERROR_FILE_NOT_FOUND), eq(file.getName()));
 }

 @Test
 public void testUpdate_IndexingCompleted_AddModelToJTailMainModel() {
 FileIndex index = mock(FileIndex.class);
 FileIndexer fileIndexer = mock(FileIndexer.class);
 File file = mock(File.class);
 JTailModel jTailModel = new JTailModel(file, index);

 when(fileIndexer.getFile()).thenReturn(file);

 indexFileAction.update(fileIndexer, 100);
 verify(jTailMainModelMock).add(jTailModel);
 }

 @Test
 public void testUpdate_IndexingFailed_DisplayErrorMessage() throws IOException {
 FileIndex index = mock(FileIndex.class);
 FileIndexer fileIndexer = mock(FileIndexer.class);
 File file = mock(File.class);
 JTailModel jTailModel = new JTailModel(file, index);

 doThrow(new IOException("Test IOException")) // NOSONAR
 .when(fileIndexer).init(file, RamFileIndex.class);

 indexFileAction.update(fileIndexer, 100);
 verifyNoMoreInteractions(jTailMainModelMock);
 verify(ExceptionHandler).handle(eq(new IOException("Test IOException")), eq(UIMessage.ERROR_GENERIC_MESSAGE), anyArray());
 }
}
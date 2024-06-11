package fr.pingtimeout.jtail.gui.action;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.event.*;
import java.io.File;
import java.io.IOException;

import fr.pingtimeout.jtail.gui.controller.*;
import fr.pingtimeout.jtail.gui.message.*;
import fr.pingtimeout.jtail.gui.model.*;
import fr.pingtimeout.jtail.io.index.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class IndexFileActionTest {

 private IndexFileAction indexFileAction;
 private JTailMainModel jTailMainModelMock;
 private OpenFileModel openFileModelMock;
 private JDialog dialogMock;
 private FileIndexer fileIndexerMock;
 private FileIndexerWorker fileIndexerWorkerMock;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 jTailMainModelMock = mock(JTailMainModel.class);
 openFileModelMock = mock(OpenFileModel.class);
 dialogMock = mock(JDialog.class);
 fileIndexerMock = mock(FileIndexer.class);
 fileIndexerWorkerMock = mock(FileIndexerWorker.class);
 indexFileAction = new IndexFileAction(jTailMainModelMock, openFileModelMock);
 }

 @Test
 public void testActionPerformedWithNullFile() {
 // Given openFileModel has null file
 when(openFileModelMock.getFile()).thenReturn(null);

 // When actionPerformed is called
 indexFileAction.actionPerformed(mock(ActionEvent.class));

 // Then no message is displayed
 verify(InformationHandler::handle, never()).handle(true, UIMessage.INFO_NO_FILE_SELECTED, (Object[])anyArray());
 }

 @Test
 public void testActionPerformedWithValidFile() throws FileNotFoundException, IOException {
 // Given openFileModel has a valid file
 when(openFileModelMock.getFile()).thenReturn(new File("test.txt"));
 when(openFileModelMock.getIndexType()).thenReturn(IndexType.FILE_BASED);
 when(fileIndexerMock.new FileIndexer(any(File.class), eq(RomFileIndex.class)))
 .thenReturn(fileIndexerMock);
 when(fileIndexerWorkerMock.execute()).thenReturn(true);

 // When actionPerformed is called
 doAnswer(invocation -> {
 dialogMock.setVisible(false);
 return null;
 }).doNothing().when(dialogMock).setVisible(eq(false));
 indexFileAction.actionPerformed(mock(ActionEvent.class));

 // Then file is indexed and added to jTailMainModel
 verify(jTailMainModelMock).add(any(JTailModel.class));
 }

 @Test(expected = FileNotFoundException.class)
 public void testActionPerformedWithInvalidFile() {
 // Given openFileModel has an invalid file
 when(openFileModelMock.getFile()).thenReturn(new File("invalidFile.txt"));
 when(openFileModelMock.getIndexType()).thenReturn(IndexType.FILE_BASED);
 when(fileIndexerMock.new FileIndexer(any(File.class), eq(RomFileIndex.class)))
 .thenThrow(new FileNotFoundException("Error message"));

 // When actionPerformed is called
 indexFileAction.actionPerformed(mock(ActionEvent.class));

 // Then error message is displayed
 verify(ExceptionHandler::handle).withArgument(eq(new FileNotFoundException("Error message")),
 eq(UIMessage.ERROR_FILE_NOT_FOUND), eq("invalidFile.txt"));
 }

 @Test(expected = Exception.class)
 public void testActionPerformedWithGenericException() {
 // Given openFileModel has a valid file
 when(openFileModelMock.getFile()).thenReturn(new File("test.txt"));
 when(openFileModelMock.getIndexType()).thenReturn(IndexType.FILE_BASED);
 when(fileIndexerMock.new FileIndexer(any(File.class), eq(RomFileIndex.class)))
 .thenReturn(fileIndexerMock);
 when(fileIndexerWorkerMock.execute()).thenThrow(new IOException("Error message"));

 // When actionPerformed is called
 indexFileAction.actionPerformed(mock(ActionEvent.class));

 // Then error message is displayed
 verify(ExceptionHandler::handle).withArgument(eq(new IOException("Error message")),
 eq(UIMessage.ERROR_GENERIC_MESSAGE), (Object[])anyArray());
 }
}
package fr.pingtimeout.jtail.gui.action;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.event.*;
import java.io.*;
import java.util.*;

import org.junit.Before;
import org.junit.Test;

import fr.pingtimeout.jtail.controller.*;
import fr.pingtimeout.jtail.gui.message.*;
import fr.pingtimeout.jtail.gui.model.*;
import fr.pingtimeout.jtail.io.index.*;
import fr.pingtimeout.jtail.util.*;
import fr.pingtimeout.jtail.io.*;

public class IndexFileActionTest {

 private IndexFileAction indexFileAction;
 private JTailMainModel jTailMainModelMock;
 private OpenFileModel openFileModelMock;
 private File fileMock;
 private ActionEvent actionEventMock;
 private JDialog jDialogMock;
 private FileIndexer fileIndexerMock;
 private FileIndexerWorker fileIndexerWorkerMock;

 @Before
 public void setUp() {
 jTailMainModelMock = mock(JTailMainModel.class);
 openFileModelMock = mock(OpenFileModel.class);
 fileMock = mock(File.class);
 actionEventMock = mock(ActionEvent.class);
 jDialogMock = mock(JDialog.class);
 fileIndexerMock = mock(FileIndexer.class);
 fileIndexerWorkerMock = mock(FileIndexerWorker.class);

 indexFileAction = new IndexFileAction(jTailMainModelMock, openFileModelMock) {
 @Override
 public void update(Observable o, Object arg) {
 super.update(o, arg);
 }
 };
 }

 @Test
 public void testActionPerformedWithNullFile() {
 indexFileAction.actionPerformed(actionEventMock);
 verify(jTailMainModelMock).add(null);
 verify(InformationHandler::handle, times(1)).withArguments(true, UIMessage.INFO_NO_FILE_SELECTED, new Object[0]);
 }

 @Test
 public void testActionPerformedWithValidFileAndIndexType() {
 when(openFileModelMock.getFile()).thenReturn(fileMock);
 when(openFileModelMock.getIndexType()).thenReturn(IndexType.FILE_BASED);
 when(fileIndexerMock.getClass()).thenReturn(RomFileIndex.class);

 indexFileAction.actionPerformed(actionEventMock);
 verify(jTailMainModelMock).add(argThat(model -> model instanceof JTailModel));
 verify(fileIndexerMock).addObserver(indexFileAction);
 verify(fileIndexerWorkerMock).execute();
 }

 @Test(expected = FileNotFoundException.class)
 public void testActionPerformedWithInvalidFile() {
 when(openFileModelMock.getFile()).thenThrow(new FileNotFoundException());
 indexFileAction.actionPerformed(actionEventMock);
 verify(ExceptionHandler::handle, times(1)).withArguments(any(FileNotFoundException.class), eq(UIMessage.ERROR_FILE_NOT_FOUND), anyString());
 }

 @Test
 public void testUpdateWithFileIndexingComplete() {
 when(openFileModelMock.getFile()).thenReturn(fileMock);
 when(fileIndexerMock.getIndex()).thenReturn(new ByteArrayIndex());
 when(fileIndexerMock.getFile()).thenReturn(fileMock);
 when(new LineReader(fileMock, fileIndexer.getIndex()).readLine()).thenReturn("line1");
 when(new LineReader(fileMock, fileIndexer.getIndex()).readLine()).thenReturn("line2");
 when(new LineReader(fileMock, fileIndexer.getIndex()).readLine()).thenReturn(null);

 indexFileAction.update(fileIndexerMock, 100);
 verify(jTailMainModelMock).add(argThat(model -> model instanceof JTailModel));
 }

 @Test
 public void testUpdateWithFileIndexingError() {
 when(openFileModelMock.getFile()).thenReturn(fileMock);
 when(fileIndexerMock.getIndex()).thenThrow(new IOException());

 indexFileAction.update(fileIndexerMock, 100);
 verify(ExceptionHandler::handle, times(1)).withArguments(any(Exception.class), eq(UIMessage.ERROR_GENERIC_MESSAGE), new Object[0]);
 }

 @Test
 public void testUpdateWithFileIndexingNotCompleted() {
 when(openFileModelMock.getFile()).thenReturn(fileMock);
 when(fileIndexerMock.getIndex()).thenReturn(null);

 indexFileAction.update(fileIndexerMock, 100);
 verifyZeroInteractions(jTailMainModelMock);
 }

 @Test
 public void testStripLeadingDialogFromSource() {
 indexFileAction.actionPerformed(actionEventMock);
 assertEquals(jDialogMock, indexFileAction.getSource());
 verify(jDialogMock).setVisible(false);
 }
}
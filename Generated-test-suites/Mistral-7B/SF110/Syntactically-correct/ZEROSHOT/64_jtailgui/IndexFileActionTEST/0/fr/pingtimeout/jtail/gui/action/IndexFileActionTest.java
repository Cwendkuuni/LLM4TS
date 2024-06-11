package fr.pingtimeout.jtail.gui.action;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import fr.pingtimeout.jtail.gui.message.UIMessage;
import fr.pingtimeout.jtail.gui.controller.JTailMainModel;
import fr.pingtimeout.jtail.gui.model.OpenFileModel;
import fr.pingtimeout.jtail.io.index.FileIndexer;
import fr.pingtimeout.jtail.io.index.FileIndexerWorker;
import fr.pingtimeout.jtail.io.index.RamFileIndex;
import fr.pingtimeout.jtail.io.index.RomFileIndex;
import fr.pingtimeout.jtail.io.LineReader;
import fr.pingtimeout.jtail.io.JTailModel;
import fr.pingtimeout.jtail.util.JTailLogger;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockitoAnnotations;

public class IndexFileActionTest {

 private IndexFileAction indexFileAction;
 private JTailMainModel jTailMainModelMock;
 private OpenFileModel openFileModelMock;
 private FileIndexer fileIndexerMock;
 private FileIndexerWorker fileIndexerWorkerMock;

 @Before
 public void setUp() {
 JTailLogger.debug = mock(PrintStream.class);
 MockitoAnnotations.initMocks(this);

 jTailMainModelMock = mock(JTailMainModel.class);
 openFileModelMock = mock(OpenFileModel.class);
 when(openFileModelMock.getFile()).thenReturn(new File("testFile.txt"));
 when(openFileModelMock.getIndexType()).thenReturn(FileIndex.FILE_BASED);

 indexFileAction = new IndexFileAction(jTailMainModelMock, openFileModelMock);

 fileIndexerMock = mock(FileIndexer.class);
 fileIndexerWorkerMock = mock(FileIndexerWorker.class);
 }

 @Test
 public void testActionPerformedWithNullFile() {
 ActionEvent actionEventMock = mock(ActionEvent.class);
 doNothing().when(jTailMainModelMock).add(any(JTailModel.class));
 doNothing().when(InformationHandler.class).handle(anyBoolean(), any(UIMessage.class), any(Object[].class));
 indexFileAction.actionPerformed(actionEventMock);
 verify(InformationHandler.class).handle(true, isA(UIMessage.INFO_NO_FILE_SELECTED), any(Object[].class));
 }

 @Test
 public void testActionPerformedWithValidFileAndIndexType() throws IOException {
 ActionEvent actionEventMock = mock(ActionEvent.class);
 doNothing().when(jTailMainModelMock).add(any(JTailModel.class));
 doNothing().when(ExceptionHandler.class).handle(any(Exception.class), any(UIMessage.class), any(Object[].class));
 doNothing().when(FileIndexer.class).newInstance(eq(new File("testFile.txt")), eq(RamFileIndex.class));
 FileIndexer fileIndexer = fileIndexerMock;
 when(fileIndexerMock.addObserver(any(Observer.class))).thenReturn(fileIndexer);
 when(fileIndexerMock.getFile()).thenReturn(new File("testFile.txt"));
 when(fileIndexerMock.getIndex()).thenReturn(new byte[0][]);

 ArgumentCaptor<FileIndexer> fileIndexerArgumentCaptor = ArgumentCaptor.forClass(FileIndexer.class);
 indexFileAction.actionPerformed(actionEventMock);
 verify(jTailMainModelMock).add(any(JTailModel.class));
 verify(fileIndexerMock).addObserver(eq(indexFileAction));
 verify(fileIndexerWorkerMock).execute();
 verify(fileIndexer).addObserver(fileIndexerArgumentCaptor.capture());
 FileIndexer capturedFileIndexer = fileIndexerArgumentCaptor.getValue();
 LineReader lineReaderMock = mock(LineReader.class);
 JTailModel jTailModelMock = mock(JTailModel.class);
 when(lineReaderMock.readLine()).thenReturn("testLine");
 when(new LineReader(any(File.class), any(byte[][].class))).thenReturn(lineReaderMock);
 when(new JTailModel(any(File.class), any(LineReader.class))).thenReturn(jTailModelMock);
 verify(jTailMainModelMock).add(eq(jTailModelMock));
 }

 @Test
 public void testUpdateWithFileNotFoundException() {
 Observable observableMock = mock(Observable.class);
 when(fileIndexerMock.getFile()).thenThrow(new FileNotFoundException());
 indexFileAction.update(observableMock, 100);
 verify(ExceptionHandler.class).handle(any(FileNotFoundException.class), isA(UIMessage.ERROR_FILE_NOT_FOUND), any(Object[].class));
 }

 @Test
 public void testUpdateWithGenericException() {
 Observable observableMock = mock(Observable.class);
 when(fileIndexerMock.getFile()).thenThrow(new Exception());
 indexFileAction.update(observableMock, 100);
 verify(ExceptionHandler.class).handle(any(Exception.class), isA(UIMessage.ERROR_GENERIC_MESSAGE), any(Object[].class));
 }
}
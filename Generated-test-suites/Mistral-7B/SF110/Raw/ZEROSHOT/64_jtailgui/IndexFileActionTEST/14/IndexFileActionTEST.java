package fr.pingtimeout.jtail.gui.action;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.Observable;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import fr.pingtimeout.jtail.gui.controller.JTailMainModel;
import fr.pingtimeout.jtail.gui.message.InformationHandler;
import fr.pingtimeout.jtail.gui.message.UIMessage;
import fr.pingtimeout.jtail.gui.model.JTailModel;
import fr.pingtimeout.jtail.io.index.FileIndexer;
import fr.pingtimeout.jtail.io.index.FileIndexerWorker;
import fr.pingtimeout.jtail.io.index.RamFileIndex;
import fr.pingtimeout.jtail.io.index.RomFileIndex;
import fr.pingtimeout.jtail.io.line.LineReader;

public class IndexFileActionTest {

 private IndexFileAction indexFileAction;
 private JTailMainModel jTailMainModelMock;
 private OpenFileModel openFileModelMock;
 private InformationHandler informationHandlerMock;
 private Observable fileIndexerMock;
 private FileIndexerWorker fileIndexerWorkerMock;

 @Before
 public void setup() {
 jTailMainModelMock = mock(JTailMainModel.class);
 openFileModelMock = mock(OpenFileModel.class);
 informationHandlerMock = mock(InformationHandler.class);
 fileIndexerMock = mock(Observable.class);
 fileIndexerWorkerMock = mock(FileIndexerWorker.class);

 indexFileAction = new IndexFileAction(jTailMainModelMock, openFileModelMock) {
 @Override
 public void update(Observable o, Object arg) {
 super.update(o, arg);
 informationHandlerMock.handle(true, UIMessage.INFO_NOTIFICATION, new Object[0]);
 }
 };
 indexFileAction.addObserver(informationHandlerMock);
 when(openFileModelMock.getFile()).thenReturn(new File("testFile.txt"));
 when(openFileModelMock.getIndexType()).thenReturn(IndexFileAction.IndexType.MEMORY_BASED);
 }

 @Test
 public void testActionPerformedWithNullFile() {
 indexFileAction.actionPerformed(mock(ActionEvent.class));
 verify(informationHandlerMock).handle(true, UIMessage.INFO_NO_FILE_SELECTED, new Object[0]);
 }

 @Test
 public void testActionPerformedWithValidFileAndIndexType() throws IOException {
 FileIndexer fileIndexer = mock(FileIndexer.class);
 when(fileIndexerMock.getFile()).thenReturn(new File("testFile.txt"));
 when(fileIndexerMock.getIndex()).thenReturn(new byte[0]);
 when(fileIndexer.addObserver(any())).thenReturn(fileIndexer);
 when(RamFileIndex.class.newInstance()).thenReturn(fileIndexer);

 indexFileAction.actionPerformed(mock(ActionEvent.class));
 verify(jTailMainModelMock).add(any(JTailModel.class));
 }

 @Test(expected = FileNotFoundException.class)
 public void testActionPerformedWithInvalidFile() throws IOException {
 FileIndexer fileIndexer = mock(FileIndexer.class);
 when(fileIndexerMock.getFile()).thenReturn(new File("testFile.txt"));
 when(fileIndexerMock.getIndex()).thenReturn(new byte[0]);
 when(fileIndexer.addObserver(any())).thenReturn(fileIndexer);
 when(RomFileIndex.class.newInstance()).thenThrow(new FileNotFoundException("Test error message"));

 indexFileAction.actionPerformed(mock(ActionEvent.class));
 verify(informationHandlerMock).handle(true, UIMessage.ERROR_FILE_NOT_FOUND, "testFile.txt");
 }

 @Test
 public void testUpdateMethod() {
 indexFileAction.update(fileIndexerMock, 100);
 verify(informationHandlerMock).handle(true, UIMessage.INFO_NOTIFICATION, new Object[0]);
 }
}
package fr.pingtimeout.jtail.gui.action.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.Observable;

import org.junit.Before;
import org.junit.Test;

import fr.pingtimeout.jtail.gui.action.IndexFileAction;
import fr.pingtimeout.jtail.gui.controller.JTailMainModel;
import fr.pingtimeout.jtail.gui.message.UIMessage;
import fr.pingtimeout.jtail.gui.model.JTailModel;
import fr.pingtimeout.jtail.io.index.FileIndexer;
import fr.pingtimeout.jtail.io.index.FileIndexerWorker;
import fr.pingtimeout.jtail.io.index.FileIndexer.IndexType;
import fr.pingtimeout.jtail.util.JTailLogger;

public class IndexFileActionTest {

 private IndexFileAction action;
 private JTailMainModel model;
 private OpenFileModel openFileModel;
 private Observable o;
 private FileIndexer fileIndexer;
 private FileIndexerWorker fileIndexerWorker;

 @Before
 public void setUp() {
 JTailLogger.debug = mock(PrintStream.class); // Replace this with real logger when available
 action = new IndexFileAction(new JTailMainModel(), new OpenFileModel());
 model = new JTailMainModel();
 openFileModel = new OpenFileModel();
 o = mock(Observable.class);
 fileIndexer = mock(FileIndexer.class);
 fileIndexerWorker = new FileIndexerWorker(fileIndexer);
 }

 @Test
 public void testActionPerformedWithNullFile() {
 when(openFileModel.getFile()).thenReturn(null);
 action.actionPerformed(new ActionEvent(new JButton(), 1, "Open File"));
 verify(InformationHandler.class).handle(true, isA(UIMessage.class), any());
 }

 @Test
 public void testActionPerformedWithValidFile() throws IOException {
 when(openFileModel.getFile()).thenReturn(new File("test.txt"));
 when(openFileModel.getIndexType()).thenReturn(IndexType.FILE_BASED);
 File expectedFile = new File("test.txt");
 when(fileIndexer.getFile()).thenReturn(expectedFile);

 action.actionPerformed(new ActionEvent(new JButton(), 1, "Open File"));
 verify(JTailLogger.class).debug("File : " + eq(expectedFile));
 verify(JTailLogger.class).debug("Index type : FILE_BASED");
 verify(o).addObserver(eq(action));
 verify(fileIndexerWorker).execute();
 }

 @Test
 public void testActionPerformedWithInvalidIndexType() {
 when(openFileModel.getFile()).thenReturn(new File("test.txt"));
 when(openFileModel.getIndexType()).thenReturn(IndexType.INVALID);

 action.actionPerformed(new ActionEvent(new JButton(), 1, "Open File"));
 verify(ExceptionHandler.class).handle(any(Exception.class), isA(UIMessage.class), any());
 }

 @Test
 public void testUpdate() throws IOException {
 when(openFileModel.getFile()).thenReturn(new File("test.txt"));
 when(openFileModel.getIndexType()).thenReturn(IndexType.FILE_BASED);
 when(fileIndexer.getFile()).thenReturn(new File("test.txt"));
 when(fileIndexer.getIndex()).thenReturn(new byte[0][]);
 when(fileIndexer.addObserver(any())).thenAnswer((invocation) -> true);

 action.actionPerformed(new ActionEvent(new JButton(), 1, "Open File"));
 verify(fileIndexer).addObserver(eq(action));
 verify(fileIndexerWorker).execute();

 verify(o).notifyObservers(argThat((arg) -> arg instanceof Integer && ((Integer)arg).intValue() == 100));
 FileIndexer expectedFileIndexer = fileIndexer;
 JTailModel expectedModel = new JTailModel(expectedFileIndexer.getFile(), new LineReader(expectedFileIndexer.getFile(), expectedFileIndexer.getIndex()));
 model.add(expectedModel);
 }

 @Test
 public void testUpdateWithException() throws IOException {
 when(openFileModel.getFile()).thenReturn(new File("test.txt"));
 when(openFileModel.getIndexType()).thenReturn(IndexType.FILE_BASED);
 when(fileIndexer.getFile()).thenReturn(new File("test.txt"));
 when(fileIndexer.getIndex()).thenThrow(new IOException("Test exception"));

 action.actionPerformed(new ActionEvent(new JButton(), 1, "Open File"));
 verify(ExceptionHandler.class).handle(any(Exception.class), isA(UIMessage.class), any());
 }
}
package fr.pingtimeout.jtail.gui.action;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.Observable;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import fr.pingtimeout.jtail.gui.controller.InformationHandler;
import fr.pingtimeout.jtail.gui.controller.ExceptionHandler;
import fr.pingtimeout.jtail.gui.controller.UIMessage;
import fr.pingtimeout.jtail.gui.model.JTailMainModel;
import fr.pingtimeout.jtail.gui.model.OpenFileModel;
import fr.pingtimeout.jtail.io.index.FileIndexer;
import fr.pingtimeout.jtail.io.index.FileIndexerWorker;
import fr.pingtimeout.jtail.io.index.FileIndexerObserver;
import fr.pingtimeout.jtail.io.index.FileIndexerWorker.State;
import fr.pingtimeout.jtail.io.index.RamFileIndex;
import fr.pingtimeout.jtail.io.index.RomFileIndex;
import fr.pingtimeout.jtail.io.line.LineReader;
import fr.pingtimeout.jtail.io.line.LineReaderFactory;
import fr.pingtimeout.jtail.gui.language.ResourceBundle;
import fr.pingtimeout.jtail.util.JTailLogger;

public class IndexFileActionTest {

 private IndexFileAction indexFileAction;
 private OpenFileModel openFileModel;
 private JTailMainModel jTailMainModel;
 private FileIndexerWorker fileIndexerWorker;
 private FileIndexerObserver fileIndexerObserver;
 private FileIndexer fileIndexer;
 private LineReader lineReader;
 private JTailModel jTailModel;
 private JDialog dialog;

 @Before
 public void setUp() {
 openFileModel = mock(OpenFileModel.class);
 jTailMainModel = mock(JTailMainModel.class);
 indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
 fileIndexerWorker = mock(FileIndexerWorker.class);
 fileIndexerObserver = mock(FileIndexerObserver.class);
 fileIndexer = mock(FileIndexer.class);
 lineReader = mock(LineReader.class);
 jTailModel = mock(JTailModel.class);
 dialog = mock(JDialog.class);

 when(openFileModel.getFile()).thenReturn(new File("testFile.txt"));
 when(openFileModel.getIndexType()).thenReturn(IndexFileType.FILE_BASED);
 when(fileIndexer.getFile()).thenReturn(new File("testFile.txt"));
 when(fileIndexer.getIndex()).thenReturn(new int[1][1]);
 when(fileIndexer.addObserver(any(FileIndexerObserver.class))).thenReturn(this.fileIndexerObserver);
 when(fileIndexerWorker.getState()).thenReturn(State.COMPLETED);
 when(fileIndexerWorker.execute()).thenReturn(fileIndexerWorker);
 when(fileIndexerObserver.update(any(Observable.class), anyInt(), any(Object[].class))).thenReturn(true);
 when(new LineReaderFactory().getLineReader(any(File.class), any(int[][].class))).thenReturn(lineReader);

 indexFileAction.addObserver(fileIndexerObserver);
 }

 @Test
 public void testActionPerformedWithNullFile() {
 indexFileAction.actionPerformed(new ActionEvent(dialog, 0, "test"));
 verify(InformationHandler.class).handle(true, UIMessage.INFO_NO_FILE_SELECTED, new Object[0]);
 }

 @Test
 public void testActionPerformedWithValidFile() throws IOException {
 indexFileAction.actionPerformed(new ActionEvent(dialog, 0, "test"));
 ArgumentCaptor<FileIndexer> fileIndexerArgumentCaptor = ArgumentCaptor.forClass(FileIndexer.class);
 verify(fileIndexerWorker).execute();
 verify(fileIndexerObserver).update(eq(fileIndexer), eq(100), any(Object[].class));
 verify(jTailMainModel).add(eq(jTailModel));
 }

 @Test(expected = FileNotFoundException.class)
 public void testActionPerformedWithInvalidFile() {
 indexFileAction.actionPerformed(new ActionEvent(dialog, 0, "test"));
 verify(ExceptionHandler.class).handle(any(FileNotFoundException.class), eq(UIMessage.ERROR_FILE_NOT_FOUND), anyString());
 }

 @Test
 public void testUpdateWithSuccess() {
 indexFileAction.update(fileIndexer, 100, new Object[0]);
 verify(JTailLogger.class).debug("Notification received : 100 emitted by FileIndexer");
 }
}
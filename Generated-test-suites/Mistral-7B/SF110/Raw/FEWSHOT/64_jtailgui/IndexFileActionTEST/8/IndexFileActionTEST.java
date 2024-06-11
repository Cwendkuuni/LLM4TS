package fr.pingtimeout.jtail.gui.action;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import fr.pingtimeout.jtail.gui.controller.JTailMainModel;
import fr.pingtimeout.jtail.gui.model.OpenFileModel;
import fr.pingtimeout.jtail.gui.message.UIMessage;
import fr.pingtimeout.jtail.io.FileIndexer;
import fr.pingtimeout.jtail.io.index.FileIndexerWorker;
import fr.pingtimeout.jtail.io.index.RamFileIndex;
import fr.pingtimeout.jtail.io.index.RomFileIndex;
import fr.pingtimeout.jtail.io.LineReader;
import fr.pingtimeout.jtail.io.JTailModel;
import fr.pingtimeout.jtail.io.IOException;

public class IndexFileActionTest {

 private JTailMainModel jTailMainModel;
 private OpenFileModel openFileModel;
 private IndexFileAction indexFileAction;
 private JDialog dialog;

 @Before
 public void setUp() {
 jTailMainModel = mock(JTailMainModel.class);
 openFileModel = mock(OpenFileModel.class);
 dialog = mock(JDialog.class);
 indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
 }

 @Test
 public void testActionPerformedNullFile() {
 indexFileAction.actionPerformed(mock(ActionEvent.class));
 verify(jTailMainModel).add(null);
 InformationHandler.handle(true, UIMessage.INFO_NO_FILE_SELECTED, new Object[0]);
 }

 @Test
 public void testActionPerformedValidFile() throws IOException {
 when(openFileModel.getFile()).thenReturn(new File("testFile.txt"));
 Class<? extends FileIndex> indexerClass = RamFileIndex.class;
 FileIndexer fileIndexer = mock(FileIndexer.class);
 when(fileIndexer.addObserver(any(Observer.class))).thenReturn(fileIndexer);
 when(fileIndexer.getFile()).thenReturn(new File("testFile.txt"));
 when(fileIndexer.getIndex()).thenReturn(new byte[0][]);

 indexFileAction.actionPerformed(mock(ActionEvent.class));
 JTailModel jTailModel = new JTailModel(new File("testFile.txt"), new LineReader(new File("testFile.txt"), new byte[0][]));
 verify(jTailMainModel).add(jTailModel);
 }

 @Test(expected = FileNotFoundException.class)
 public void testActionPerformedFileNotFound() throws IOException {
 when(openFileModel.getFile()).thenReturn(new File("nonExistentFile.txt"));
 Class<? extends FileIndex> indexerClass = RomFileIndex.class;
 FileIndexer fileIndexer = mock(FileIndexer.class);
 when(fileIndexer.addObserver(any(Observer.class))).thenReturn(fileIndexer);
 when(fileIndexer.getFile()).thenReturn(new File("nonExistentFile.txt"));
 when(fileIndexer.getIndex()).thenReturn(new byte[0][]);

 indexFileAction.actionPerformed(mock(ActionEvent.class));
 }

 @Test(expected = Exception.class)
 public void testActionPerformedException() throws IOException {
 when(openFileModel.getFile()).thenReturn(new File("testFile.txt"));
 Class<? extends FileIndex> indexerClass = RomFileIndex.class;
 FileIndexer fileIndexer = mock(FileIndexer.class);
 when(fileIndexer.addObserver(any(Observer.class))).thenReturn(fileIndexer);
 when(fileIndexer.getFile()).thenReturn(new File("testFile.txt"));
 when(fileIndexer.getIndex()).thenThrow(new Exception());

 indexFileAction.actionPerformed(mock(ActionEvent.class));
 }

 @Test
 public void testUpdate() {
 indexFileAction.update(mock(Observable.class), 100);
 }

}
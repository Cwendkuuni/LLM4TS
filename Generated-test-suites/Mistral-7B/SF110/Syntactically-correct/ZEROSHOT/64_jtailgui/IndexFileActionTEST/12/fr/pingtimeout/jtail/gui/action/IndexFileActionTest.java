package fr.pingtimeout.jtail.gui.action;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.Observable;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import fr.pingtimeout.jtail.gui.controller.JTailMainModel;
import fr.pingtimeout.jtail.gui.message.InformationHandler;
import fr.pingtimeout.jtail.gui.message.UIMessage;
import fr.pingtimeout.jtail.gui.model.JTailModel;
import fr.pingtimeout.jtail.io.index.FileIndexer;
import fr.pingtimeout.jtail.io.index.FileIndexerWorker;
import fr.pingtimeout.jtail.io.index.FileIndexer.IndexType;
import fr.pingtimeout.jtail.io.index.RamFileIndex;
import fr.pingtimeout.jtail.io.index.RomFileIndex;
import fr.pingtimeout.jtail.util.JTailLogger;
import fr.pingtimeout.jtail.util.LineReader;

public class IndexFileActionTest {

 private IndexFileAction indexFileAction;
 @Mock
 private JTailMainModel jTailMainModel;
 @Mock
 private OpenFileModel openFileModel;
 @Mock
 private InformationHandler informationHandler;
 @Mock
 private Observable fileIndexer;
 @Mock
 private FileIndexer fileIndexer;
 @Mock
 private FileIndexerWorker fileIndexerWorker;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 when(openFileModel.getFile()).thenReturn(new File("testFile.txt"));
 when(openFileModel.getIndexType()).thenReturn(IndexType.FILE_BASED);
 indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
 }

 @Test
 public void testActionPerformedWithNullFile() {
 ActionEvent actionEvent = mock(ActionEvent.class);
 doNothing().when(informationHandler).handle(eq(true), isA(UIMessage.class), anyArray());
 indexFileAction.actionPerformed(actionEvent);
 verify(informationHandler).handle(eq(true), isA(UIMessage.class), anyArray());
 }

 @Test
 public void testActionPerformedWithValidFile() throws IOException {
 ActionEvent actionEvent = mock(ActionEvent.class);
 Component dialog = mock(Component.class);
 when(actionEvent.getSource()).thenReturn(dialog);
 doNothing().when(dialog).setVisible(eq(false));
 doNothing().when(informationHandler).handle(eq(true), isA(UIMessage.class), anyArray());
 Class<? extends FileIndex> indexerClass = RomFileIndex.class;
 FileIndexer fileIndexer = mock(FileIndexer.class);
 FileIndexerWorker fileIndexerWorker = mock(FileIndexerWorker.class);
 when(fileIndexer.addObserver(any())).thenReturn(fileIndexer);
 when(fileIndexerWorker.execute()).thenReturn(true);
 when(fileIndexer.getFile()).thenReturn(new File("testFile.txt"));
 when(fileIndexer.getIndex()).thenReturn(new byte[0][]);
 when(fileIndexer.getIndexType()).thenReturn(IndexType.FILE_BASED);
 when(fileIndexerWorker.getFileIndexer()).thenReturn(fileIndexer);
 when(fileIndexerClass.newInstance(any(), eq(RomFileIndex.class))).thenReturn(fileIndexer);

 indexFileAction.actionPerformed(actionEvent);

 verify(jTailMainModel, times(1)).add(eq(new JTailModel(new File("testFile.txt"), new LineReader(new File("testFile.txt"), new byte[0][]))));
 }

 @Test(expected = FileNotFoundException.class)
 public void testActionPerformedWithInvalidFile() throws IOException {
 ActionEvent actionEvent = mock(ActionEvent.class);
 Component dialog = mock(Component.class);
 when(actionEvent.getSource()).thenReturn(dialog);
 doNothing().when(dialog).setVisible(eq(false));
 doNothing().when(informationHandler).handle(eq(true), isA(UIMessage.class), anyArray());
 Class<? extends FileIndex> indexerClass = RomFileIndex.class;
 FileIndexer fileIndexer = mock(FileIndexer.class);
 FileIndexerWorker fileIndexerWorker = mock(FileIndexerWorker.class);
 when(fileIndexer.addObserver(any())).thenReturn(fileIndexer);
 when(fileIndexerWorker.execute()).thenReturn(false);
 when(fileIndexer.getFile()).thenThrow(new FileNotFoundException());
 when(fileIndexerClass.newInstance(any(), eq(RomFileIndex.class))).thenReturn(fileIndexer);

 indexFileAction.actionPerformed(actionEvent);
 }

 @Test
 public void testUpdate() {
 Observable observable = mock(Observable.class);
 Observable o = mock(Observable.class);
 when(fileIndexer.getObservable()).thenReturn(observable);
 when(fileIndexer).thenReturn(fileIndexer);
 IndexFileAction indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
 indexFileAction.update(observable, 100);
 verify(jTailMainModel, times(1)).add(any(JTailModel.class));
 }

}
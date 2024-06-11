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
import fr.pingtimeout.jtail.gui.message.UIMessage;
import fr.pingtimeout.jtail.gui.model.JTailModel;
import fr.pingtimeout.jtail.io.index.FileIndexer;
import fr.pingtimeout.jtail.io.index.FileIndexerWorker;
import fr.pingtimeout.jtail.io.index.RamFileIndex;
import fr.pingtimeout.jtail.io.index.RomFileIndex;
import fr.pingtimeout.jtail.util.JTailLogger;

public class IndexFileActionTest {

 private IndexFileAction indexFileAction;
 private JTailMainModel jTailMainModelMock;
 private OpenFileModel openFileModelMock;
 private FileIndexer fileIndexerMock;
 private FileIndexerWorker fileIndexerWorkerMock;

 @Before
 public void setUp() {
 openFileModelMock = Mockito.mock(OpenFileModel.class);
 jTailMainModelMock = Mockito.mock(JTailMainModel.class);
 fileIndexerMock = Mockito.mock(FileIndexer.class);
 fileIndexerWorkerMock = Mockito.mock(FileIndexerWorker.class);

 indexFileAction = new IndexFileAction(jTailMainModelMock, openFileModelMock);
 }

 @Test
 public void testActionPerformed_NullFile() {
 when(openFileModelMock.getFile()).thenReturn(null);
 indexFileAction.actionPerformed(mock(ActionEvent.class));
 verify(InformationHandler::handle, times(1)).handle(true, UIMessage.INFO_NO_FILE_SELECTED, new Object[0]);
 }

 @Test
 public void testActionPerformed_ValidFile() throws IOException {
 File file = new File("test.txt");
 when(openFileModelMock.getFile()).thenReturn(file);
 when(openFileModelMock.getIndexType()).thenReturn(IndexType.FILE_BASED);

 JTailLogger.debug("File : {}", argThat(equalTo(file)));
 JTailLogger.debug("Index type : {}", argThat(equalTo(IndexType.FILE_BASED)));

 Component dialog = mock(Component.class);
 indexFileAction.actionPerformed(mock(ActionEvent.class));
 verify(dialog).setVisible(false);

 Class<? extends FileIndex> indexerClass = RomFileIndex.class;
 FileIndexer fileIndexer = new FileIndexer(file, indexerClass);
 fileIndexer.addObserver(indexFileAction);
 verify(fileIndexer).addObserver(indexFileAction);

 FileIndexerWorker fileIndexerWorker = fileIndexerWorkerMock;
 fileIndexerWorker.execute();
 verify(fileIndexerWorker).execute();

 JTailModel jTailModel = mock(JTailModel.class);
 JTailModel model = new JTailModel(file, new LineReader(file, fileIndexer.getIndex()));
 jTailMainModelMock.add(model);
 verify(jTailMainModelMock).add(model);
 }

 @Test
 public void testActionPerformed_InvalidIndexType() {
 when(openFileModelMock.getIndexType()).thenReturn(IndexType.INVALID);
 indexFileAction.actionPerformed(mock(ActionEvent.class));
 verify(ExceptionHandler::handle, times(1)).handle(argThat(instanceOf(IOException.class)), argThat(equalTo(UIMessage.ERROR_INVALID_INDEX_TYPE)), argThat(equalTo(openFileModelMock.getIndexType())));
 }

 @Test
 public void testUpdate() {
 Observable observable = mock(Observable.class);
 FileIndexer fileIndexer = mock(FileIndexer.class);
 fileIndexer.getFile();
 observable.setChanged(true);
 observable.notifyObservers(100);

 indexFileAction.update(observable, 100);
 verify(jTailMainModelMock).add(argThat(instanceOf(JTailModel.class)));
 }
}
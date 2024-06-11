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

import fr.pingtimeout.jtail.gui.controller.JTailMainModel;
import fr.pingtimeout.jtail.gui.message.UIMessage;
import fr.pingtimeout.jtail.gui.model.JTailModel;
import fr.pingtimeout.jtail.gui.message.InformationHandler;
import fr.pingtimeout.jtail.gui.message.ExceptionHandler;
import fr.pingtimeout.jtail.io.index.FileIndexer;
import fr.pingtimeout.jtail.io.index.RamFileIndex;
import fr.pingtimeout.jtail.io.index.RomFileIndex;
import fr.pingtimeout.jtail.io.line.LineReader;
import fr.pingtimeout.jtail.io.index.FileIndexerWorker;

public class IndexFileActionTest {

 private IndexFileAction indexFileAction;
 private JTailMainModel jTailMainModelMock;
 private OpenFileModel openFileModelMock;
 private FileIndexer fileIndexerMock;
 private FileIndexerWorker fileIndexerWorkerMock;
 private InformationHandler informationHandlerMock;
 private ExceptionHandler exceptionHandlerMock;

 @Before
 public void setup() {
 jTailMainModelMock = mock(JTailMainModel.class);
 openFileModelMock = mock(OpenFileModel.class);
 fileIndexerMock = mock(FileIndexer.class);
 fileIndexerWorkerMock = mock(FileIndexerWorker.class);
 informationHandlerMock = mock(InformationHandler.class);
 exceptionHandlerMock = mock(ExceptionHandler.class);

 indexFileAction = new IndexFileAction(jTailMainModelMock, openFileModelMock);
 }

 @Test
 public void testActionPerformed() {
 File file = new File("testFile.txt");
 openFileModelMock.setFile(file);
 openFileModelMock.setIndexType(IndexType.FILE_BASED);

 ArgumentCaptor<FileIndexer> fileIndexerArgumentCaptor = ArgumentCaptor.forClass(FileIndexer.class);
 ArgumentCaptor<Class<? extends FileIndexer>> indexerClassArgumentCaptor = ArgumentCaptor.forClass(Class.class);

 when(openFileModelMock.getFile()).thenReturn(file);
 when(openFileModelMock.getIndexType()).thenReturn(IndexType.FILE_BASED);
 when(FileIndexer.class.getDeclaredConstructor(File.class, Class.class)).thenReturn(RomFileIndex.class);

 indexFileAction.actionPerformed(new ActionEvent(new JButton(), ActionEvent.ACTION_PERFORMED, "test"));

 verify(fileIndexerWorkerMock).execute();
 verify(fileIndexerMock).addObserver(eq(indexFileAction));
 verify(fileIndexerMock).setFile(file);
 verify(fileIndexerMock).setIndexType(IndexType.FILE_BASED);

 verify(openFileModelMock).setIndexType(IndexType.FILE_BASED);
 verify(openFileModelMock).setFile(file);

 ArgumentCaptor<File> fileArgumentCaptor = ArgumentCaptor.forClass(File.class);
 ArgumentCaptor<JTailModel> jTailModelArgumentCaptor = ArgumentCaptor.forClass(JTailModel.class);

 verify(jTailMainModelMock).add(jTailModelArgumentCaptor.capture());
 LineReader lineReader = mock(LineReader.class);
 JTailModel jTailModel = new JTailModel(file, lineReader);

 verify(fileIndexerMock).getFile();
 try {
 FileIndexer indexer = new FileIndexer(file, RomFileIndex.class);
 LineReader lineReader1 = new LineReader(file, indexer.getIndex());
 JTailModel jTailModel1 = new JTailModel(file, lineReader1);
 jTailModelArgumentCaptor.getValue().setFile(file);
 jTailModelArgumentCaptor.getValue().setLineReader(lineReader1);
 } catch (FileNotFoundException e) {
 e.printStackTrace();
 } catch (Exception e1) {
 e1.printStackTrace();
 }

 verify(informationHandlerMock, never()).handle(anyBoolean(), any(UIMessage.class), any(Object[].class));
 verify(exceptionHandlerMock, never()).handle(any(Exception.class), any(UIMessage.class), any(Object[].class));
 }

 @Test
 public void testActionPerformedWithNullFile() {
 openFileModelMock.setFile(null);

 indexFileAction.actionPerformed(new ActionEvent(new JButton(), ActionEvent.ACTION_PERFORMED, "test"));

 verify(openFileModelMock, times(1)).setFile(null);
 verify(informationHandlerMock).handle(true, UIMessage.INFO_NO_FILE_SELECTED, new Object[0]);
 }

 @Test
 public void testActionPerformedWithInvalidIndexType() {
 openFileModelMock.setFile(new File("testFile.txt"));
 openFileModelMock.setIndexType(IndexType.INVALID);

 indexFileAction.actionPerformed(new ActionEvent(new JButton(), ActionEvent.ACTION_PERFORMED, "test"));

 verify(openFileModelMock, times(1)).setIndexType(IndexType.INVALID);
 verify(exceptionHandlerMock).handle(any(Exception.class), eq(UIMessage.ERROR_INVALID_INDEX_TYPE), any(Object[].class));
 }

 @Test
 public void testUpdate() throws IOException {
 File file = new File("testFile.txt");
 FileIndexer fileIndexer = mock(FileIndexer.class);
 JTailModel jTailModel = mock(JTailModel.class);
 Observable observable = mock(Observable.class);

 when(fileIndexer.getFile()).thenReturn(file);
 when(fileIndexer.getIndex()).thenReturn(new byte[0]);
 when(observable.getClass()).thenReturn(Observable.class);
 when(observable.addObserver(any(Observer.class))).thenReturn(this);

 indexFileAction.update(observable, 100);

 verify(jTailMainModelMock).add(jTailModel);
 }

 @Test
 public void testUpdateWithNullObservable() {
 indexFileAction.update(null, 100);

 verify(jTailMainModelMock, never()).add(any(JTailModel.class));
 }
}
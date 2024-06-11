import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import fr.pingtimeout.jtail.gui.action.IndexFileAction;
import fr.pingtimeout.jtail.gui.controller.JTailMainModel;
import fr.pingtimeout.jtail.gui.message.UIMessage;
import fr.pingtimeout.jtail.gui.model.JTailModel;
import fr.pingtimeout.jtail.io.index.FileIndexer;
import fr.pingtimeout.jtail.io.FileIndexerWorker;
import fr.pingtimeout.jtail.io.index.FileIndexerObserver;
import fr.pingtimeout.jtail.io.index.RamFileIndex;
import fr.pingtimeout.jtail.io.index.RomFileIndex;
import fr.pingtimeout.jtail.io.LineReader;
import fr.pingtimeout.jtail.io.JTailLogger;
import fr.pingtimeout.jtail.gui.message.InformationHandler;
import fr.pingtimeout.jtail.gui.message.ExceptionHandler;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Observable;

public class IndexFileActionTest {

 private IndexFileAction indexFileAction;
 private JTailMainModel jTailMainModelMock;
 private OpenFileModel openFileModelMock;
 private InformationHandler informationHandlerMock;
 private ExceptionHandler exceptionHandlerMock;
 private JTailMainModel jTailMainModel;
 private Observable observableMock;

 @Before
 public void setUp() {
 jTailMainModelMock = Mockito.mock(JTailMainModel.class);
 openFileModelMock = Mockito.mock(OpenFileModel.class);
 informationHandlerMock = Mockito.mock(InformationHandler.class);
 exceptionHandlerMock = Mockito.mock(ExceptionHandler.class);
 observableMock = Mockito.mock(Observable.class);
 jTailMainModel = new JTailMainModel();

 indexFileAction = new IndexFileAction(jTailMainModel, openFileModelMock);
 JTailLogger.setLogger(Mockito.mock(JTailLogger.class));
 InformationHandler.setHandler(informationHandlerMock);
 ExceptionHandler.setHandler(exceptionHandlerMock);
 }

 @Test
 public void testActionPerformedWithNullFile() {
 openFileModelMock.setFile(null);
 ActionEvent actionEvent = new ActionEvent(new Object(), 0, "");
 indexFileAction.actionPerformed(actionEvent);
 Mockito.verify(informationHandlerMock).handle(true, UIMessage.INFO_NO_FILE_SELECTED, new Object[0]);
 }

 @Test
 public void testActionPerformedWithValidFile() throws Exception {
 openFileModelMock.setFile(Mockito.mock(File.class));
 openFileModelMock.setIndexType(IndexType.MEMORY_BASED);
 ActionEvent actionEvent = new ActionEvent(new Object(), 0, "");
 indexFileAction.actionPerformed(actionEvent);
 ArgumentCaptor<FileIndexerObserver> observerCaptor = ArgumentCaptor.forClass(FileIndexerObserver.class);
 Mockito.verify(jTailMainModel).add(observerCaptor.capture());
 FileIndexerObserver fileIndexerObserver = observerCaptor.getValue();
 Mockito.verify(fileIndexerObserver).update(observableMock, 100);
 }

 @Test(expected = FileNotFoundException.class)
 public void testActionPerformedWithInvalidFile() throws Exception {
 openFileModelMock.setFile(Mockito.mock(File.class));
 openFileModelMock.setIndexType(IndexType.FILE_BASED);
 ActionEvent actionEvent = new ActionEvent(new Object(), 0, "");
 indexFileAction.actionPerformed(actionEvent);
 Mockito.verify(exceptionHandlerMock).handle(Mockito.argThat(e -> e instanceof FileNotFoundException), UIMessage.ERROR_FILE_NOT_FOUND, Mockito.argThat(f -> f.getName().equals("Invalid File")));
 }

 @Test
 public void testUpdateMethod() throws Exception {
 File file = Mockito.mock(File.class);
 FileIndexer fileIndexerMock = Mockito.mock(FileIndexer.class);
 observableMock.addObserver(indexFileAction);
 indexFileAction.update(observableMock, 100);
 ArgumentCaptor<File> fileCaptor = ArgumentCaptor.forClass(File.class);
 ArgumentCaptor<LineReader> lineReaderCaptor = ArgumentCaptor.forClass(LineReader.class);
 Mockito.verify(jTailMainModel).add(Mockito.argThat(m -> m instanceof JTailModel), fileCaptor.capture(), lineReaderCaptor.capture());
 FileIndexerWorker fileIndexerWorkerMock = Mockito.mock(FileIndexerWorker.class);
 fileIndexerMock.setFileIndexerWorker(fileIndexerWorkerMock);
 Mockito.verifyZeroInteractions(fileIndexerMock);
 FileIndexerWorker.execute(fileIndexerWorkerMock);
 Mockito.verify(fileReaderCaptor.getValue()).readLine();
 }

 @Test
 public void testUpdateMethodWithFileNotFoundException() throws Exception {
 File file = Mockito.mock(File.class);
 FileIndexer fileIndexerMock = Mockito.mock(FileIndexer.class);
 observableMock.addObserver(indexFileAction);
 Mockito.when(fileIndexerMock.getFile()).thenThrow(new FileNotFoundException());
 indexFileAction.update(observableMock, 100);
 Mockito.verifyZeroInteractions(jTailMainModel);
 Mockito.verify(exceptionHandlerMock).handle(Mockito.argThat(e -> e instanceof FileNotFoundException), UIMessage.ERROR_FILE_NOT_FOUND, file.getName());
 }

 @Test
 public void testUpdateMethodWithGenericException() throws Exception {
 File file = Mockito.mock(File.class);
 FileIndexer fileIndexerMock = Mockito.mock(FileIndexer.class);
 observableMock.addObserver(indexFileAction);
 Mockito.when(fileIndexerMock.getFile()).thenThrow(new Exception());
 indexFileAction.update(observableMock, 100);
 Mockito.verifyZeroInteractions(jTailMainModel);
 Mockito.verify(exceptionHandlerMock).handle(Mockito.argThat(e -> e instanceof Exception), UIMessage.ERROR_GENERIC_MESSAGE, new Object[0]);
 }

 @Test
 public void testGetIndexTypeWithMemoryBasedIndexType() {
 openFileModelMock.setIndexType(IndexType.MEMORY_BASED);
 Assert.assertEquals(IndexType.MEMORY_BASED, indexFileAction.getIndexType());
 }

 @Test
 public void testGetIndexTypeWithFileBasedIndexType() {
 openFileModelMock.setIndexType(IndexType.FILE_BASED);
 Assert.assertEquals(IndexType.FILE_BASED, indexFileAction.getIndexType());
 }
}

enum IndexType {
 MEMORY_BASED,
 FILE_BASED
}
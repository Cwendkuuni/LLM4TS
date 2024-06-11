import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.io.File;
import java.io.IOException;
import java.util.Observable;

import fr.pingtimeout.jtail.gui.action.IndexFileAction;
import fr.pingtimeout.jtail.gui.controller.JTailMainModel;
import fr.pingtimeout.jtail.gui.model.FileIndexer;
import fr.pingtimeout.jtail.gui.model.JTailModel;
import fr.pingtimeout.jtail.gui.message.UIMessage;
import fr.pingtimeout.jtail.io.index.FileIndexerWorker;
import fr.pingtimeout.jtail.io.index.FileIndexerWorker.State;

public class IndexFileActionTest {

 private IndexFileAction action;
 private JTailMainModel jTailMainModelMock;
 private OpenFileModel openFileModelMock;
 private FileIndexer fileIndexerMock;
 private FileIndexerWorker fileIndexerWorkerMock;

 @Before
 public void setUp() {
 action = new IndexFileAction(jTailMainModelMock, openFileModelMock);
 jTailMainModelMock = mock(JTailMainModel.class);
 openFileModelMock = mock(OpenFileModel.class);
 fileIndexerMock = mock(FileIndexer.class);
 fileIndexerWorkerMock = mock(FileIndexerWorker.class);
 }

 @Test
 public void testActionPerformed_ValidFileSelectedAndValidIndexType() throws IOException {
 File file = new File("testFile.txt");
 openFileModelMock.setFile(file);
 openFileModelMock.setIndexType(FileIndexer.IndexType.FILE_BASED);

 ArgumentCaptor<FileIndexer> fileIndexerArgumentCaptor = ArgumentCaptor.forClass(FileIndexer.class);
 action.actionPerformed(new ActionEvent(openFileModelMock, 1, "test"));

 verify(fileIndexerMock).addObserver(action);
 verify(jTailMainModelMock).add(Mockito.any(JTailModel.class));
 verify(fileIndexerWorkerMock).execute();
 }

 @Test
 public void testActionPerformed_NullFileSelected() {
 action.actionPerformed(new ActionEvent(openFileModelMock, 1, "test"));

 verify(openFileModelMock, Mockito.times(1)).getFile();
 InformationHandler.handle(true, UIMessage.INFO_NO_FILE_SELECTED, new Object[0]);
 }

 @Test
 public void testActionPerformed_InvalidIndexType() {
 openFileModelMock.setIndexType(FileIndexer.IndexType.INVALID);
 action.actionPerformed(new ActionEvent(openFileModelMock, 1, "test"));

 verify(openFileModelMock, Mockito.times(1)).getIndexType();
 }

 @Test
 public void testUpdate_IndexingSuccess() throws IOException {
 File file = new File("testFile.txt");
 openFileModelMock.setFile(file);
 openFileModelMock.setIndexType(FileIndexer.IndexType.FILE_BASED);

 ArgumentCaptor<FileIndexer> fileIndexerArgumentCaptor = ArgumentCaptor.forClass(FileIndexer.class);
 action.actionPerformed(new ActionEvent(openFileModelMock, 1, "test"));

 verify(fileIndexerMock).addObserver(action);
 verify(jTailMainModelMock).add(Mockito.any(JTailModel.class));
 verify(fileIndexerWorkerMock).execute();

 verify(fileIndexerMock).setFile(file);
 verify(fileIndexerMock).startIndexing();
 verify(fileIndexerMock).getIndex();

 FileIndexerWorker worker = Mockito.capture(fileIndexerMock.getWorker());
 verify(worker).getState().equals(State.SUCCESS);
 }

 @Test
 public void testUpdate_IndexingError() throws IOException {
 File file = new File("testFile.txt");
 openFileModelMock.setFile(file);
 openFileModelMock.setIndexType(FileIndexer.IndexType.FILE_BASED);

 ArgumentCaptor<FileIndexer> fileIndexerArgumentCaptor = ArgumentCaptor.forClass(FileIndexer.class);
 action.actionPerformed(new ActionEvent(openFileModelMock, 1, "test"));

 verify(fileIndexerMock).addObserver(action);
 verify(jTailMainModelMock).add(Mockito.any(JTailModel.class));
 verify(fileIndexerWorkerMock).execute();

 verify(fileIndexerMock).setFile(file);
 verify(fileIndexerMock).startIndexing();

 FileIndexerWorker worker = Mockito.capture(fileIndexerMock.getWorker());
 verify(worker.getState()).isInstanceOf(State.ERROR);
 ExceptionHandler.handle(Mockito.capture(worker.getException()), UIMessage.ERROR_GENERIC_MESSAGE, new Object[0]);
 }
}
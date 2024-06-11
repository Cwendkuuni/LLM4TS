import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import fr.pingtimeout.jtail.gui.action.IndexFileAction;
import fr.pingtimeout.jtail.gui.model.JTailMainModel;
import fr.pingtimeout.jtail.gui.model.OpenFileModel;
import fr.pingtimeout.jtail.io.FileIndexer;
import fr.pingtimeout.jtail.io.FileIndexerWorker;
import fr.pingtimeout.jtail.io.FileIndexer.IndexType;
import fr.pingtimeout.jtail.io.index.RamFileIndex;
import fr.pingtimeout.jtail.io.index.RomFileIndex;
import fr.pingtimeout.jtail.gui.controller.JTailLogger;
import fr.pingtimeout.jtail.gui.message.InformationHandler;
import fr.pingtimeout.jtail.gui.message.UIMessage;

public class IndexFileActionTest {

 private IndexFileAction indexFileAction;
 private JTailMainModel jTailMainModelMock;
 private OpenFileModel openFileModelMock;
 private FileIndexer fileIndexerMock;
 private FileIndexerWorker fileIndexerWorkerMock;

 @Before
 public void setUp() {
 jTailMainModelMock = mock(JTailMainModel.class);
 openFileModelMock = mock(OpenFileModel.class);
 fileIndexerMock = mock(FileIndexer.class);
 fileIndexerWorkerMock = mock(FileIndexerWorker.class);

 indexFileAction = new IndexFileAction(jTailMainModelMock, openFileModelMock);
 }

 @Test
 public void testActionPerformed_ValidFileAndIndexType() {
 File file = mock(File.class);
 when(openFileModelMock.getFile()).thenReturn(file);
 when(openFileModelMock.getIndexType()).thenReturn(IndexType.FILE_BASED);
 when(fileIndexerMock.newFileIndexer(file, RomFileIndex.class)).thenReturn(fileIndexerMock);

 indexFileAction.actionPerformed(mock(ActionEvent.class));

 verify(jTailMainModelMock).add(any(JTailModel.class));
 verify(fileIndexerMock).addObserver(indexFileAction);
 verify(fileIndexerWorkerMock).execute();
 }

 @Test(expected = NullPointerException.class)
 public void testActionPerformed_NullFile() {
 when(openFileModelMock.getFile()).thenReturn(null);

 indexFileAction.actionPerformed(mock(ActionEvent.class));

 verify(InformationHandler::handle, times(1)).handle(true, UIMessage.INFO_NO_FILE_SELECTED, new Object[0]);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testActionPerformed_InvalidIndexType() {
 when(openFileModelMock.getIndexType()).thenReturn(null);

 indexFileAction.actionPerformed(mock(ActionEvent.class));

 verify(InformationHandler::handle, times(1)).handle(true, UIMessage.INFO_INVALID_INDEX_TYPE, new Object[0]);
 }

 @Test(expected = FileNotFoundException.class)
 public void testActionPerformed_FileNotFound() {
 when(openFileModelMock.getFile()).thenThrow(new FileNotFoundException());

 indexFileAction.actionPerformed(mock(ActionEvent.class));

 verify(ExceptionHandler::handle, times(1)).handle(any(Exception.class), isA(String.class), any(String.class));
 }

 @Test
 public void testUpdate_FileIndexedSuccessfully() {
 File file = mock(File.class);
 FileIndexer fileIndexer = mock(FileIndexer.class);
 LineReader lineReader = mock(LineReader.class);
 JTailModel jTailModel = mock(JTailModel.class);

 when(openFileModelMock.getFile()).thenReturn(file);
 when(openFileModelMock.getIndexType()).thenReturn(IndexType.FILE_BASED);
 when(fileIndexerMock.newFileIndexer(file, RomFileIndex.class)).thenReturn(fileIndexerMock);
 when(fileIndexerMock.getIndex()).thenReturn(new byte[0][]);
 when(fileIndexerMock.getFile()).thenReturn(file);
 when(fileIndexerMock.addObserver(any()))
         .thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));
 when(new LineReader(file, fileIndexerMock.getIndex())).thenReturn(lineReader);

 indexFileAction.actionPerformed(mock(ActionEvent.class));

 verify(jTailMainModelMock).add(jTailModel);
 }

 @Test
 public void testUpdate_FileIndexingFailed() {
 File file = mock(File.class);
 FileIndexer fileIndexer = mock(FileIndexer.class);

 when(openFileModelMock.getFile()).thenReturn(file);
 when(openFileModelMock.getIndexType()).thenReturn(IndexType.FILE_BASED);
 when(fileIndexerMock.newFileIndexer(file, RomFileIndex.class)).thenThrow(new Exception());

 indexFileAction.actionPerformed(mock(ActionEvent.class));

 verify(ExceptionHandler::handle, times(1)).handle(any(Exception.class), isA(String.class), new Object[0]);
 }
}
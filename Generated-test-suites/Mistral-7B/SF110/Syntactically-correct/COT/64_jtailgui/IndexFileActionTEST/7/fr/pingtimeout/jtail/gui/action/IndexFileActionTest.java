import org.junit.Test;
import org.junit.Assert;
import static org.mockito.Mockito.*;
import fr.pingtimeout.jtail.gui.action.IndexFileAction;
import fr.pingtimeout.jtail.gui.model.JTailMainModel;
import fr.pingtimeout.jtail.gui.model.OpenFileModel;
import fr.pingtimeout.jtail.io.index.FileIndexer;
import fr.pingtimeout.jtail.io.index.RamFileIndex;
import fr.pingtimeout.jtail.io.index.RomFileIndex;
import fr.pingtimeout.jtail.io.LineReader;
import fr.pingtimeout.jtail.gui.message.UIMessage;
import fr.pingtimeout.jtail.util.JTailLogger;
import java.awt.event.ActionEvent;
import java.io.File;

public class IndexFileActionTest {

 private JTailMainModel jTailMainModelMock;
 private OpenFileModel openFileModelMock;

 @Before
 public void setup() {
 jTailMainModelMock = mock(JTailMainModel.class);
 openFileModelMock = mock(OpenFileModel.class);
 }

 @Test
 public void testActionPerformedWithNullFile() {
 IndexFileAction indexFileAction = new IndexFileAction(jTailMainModelMock, openFileModelMock);
 indexFileAction.actionPerformed(new ActionEvent(new JButton(), ActionEvent.ACTION_PERFORMED, ""));
 verify(openFileModelMock, times(1)).getFile();
 InformationHandler.handle(true, UIMessage.INFO_NO_FILE_SELECTED, new Object[0]);
 }

 @Test
 public void testActionPerformedWithNullIndexType() {
 openFileModelMock = new OpenFileModel();
 IndexFileAction indexFileAction = new IndexFileAction(jTailMainModelMock, openFileModelMock);
 indexFileAction.actionPerformed(new ActionEvent(new JButton(), ActionEvent.ACTION_PERFORMED, ""));
 verify(openFileModelMock, times(1)).getFile();
 verify(openFileModelMock, times(1)).getIndexType();
 Assert.assertNull(indexFileAction.openFileModel.getIndexType());
 }

 @Test
 public void testActionPerformedWithValidFileAndIndexType() {
 openFileModelMock = spy(new OpenFileModel());
 when(openFileModelMock.getFile()).thenReturn(new File("testFile.txt"));
 when(openFileModelMock.getIndexType()).thenReturn(IndexFileAction.FILE_BASED);
 IndexFileAction indexFileAction = new IndexFileAction(jTailMainModelMock, openFileModelMock);
 indexFileAction.actionPerformed(new ActionEvent(new JButton(), ActionEvent.ACTION_PERFORMED, ""));
 verify(jTailMainModelMock, times(1)).add(any(JTailModel.class));
 FileIndexer fileIndexer = mock(FileIndexer.class);
 when(openFileModelMock.getFile()).thenReturn(new File("testFile.txt"));
 when(openFileModelMock.getIndexType()).thenReturn(IndexFileAction.FILE_BASED);
 when(fileIndexer.addObserver(any())).thenReturn(fileIndexer);
 when(FileIndexer.class.newInstance(eq(new File("testFile.txt")), eq(RomFileIndex.class))).thenReturn(fileIndexer);
 indexFileAction.actionPerformed(new ActionEvent(new JButton(), ActionEvent.ACTION_PERFORMED, ""));
 verify(openFileModelMock, times(1)).getFile();
 verify(openFileModelMock, times(1)).getIndexType();
 verify(fileIndexer, times(1)).addObserver(any());
 }

 @Test(expected = FileNotFoundException.class)
 public void testActionPerformedWithInvalidFile() {
 openFileModelMock = spy(new OpenFileModel());
 when(openFileModelMock.getFile()).thenReturn(new File("testFile.txt"));
 when(openFileModelMock.getIndexType()).thenReturn(IndexFileAction.FILE_BASED);
 IndexFileAction indexFileAction = new IndexFileAction(jTailMainModelMock, openFileModelMock);
 doThrow(new FileNotFoundException("File not found")).when(FileIndexer.class).newInstance(any(), any());
 indexFileAction.actionPerformed(new ActionEvent(new JButton(), ActionEvent.ACTION_PERFORMED, ""));
 ExceptionHandler.handle(any(Exception.class), eq(UIMessage.ERROR_FILE_NOT_FOUND), anyString());
 }

 @Test(expected = Exception.class)
 public void testUpdateWithException() {
 openFileModelMock = spy(new OpenFileModel());
 when(openFileModelMock.getFile()).thenReturn(new File("testFile.txt"));
 when(openFileModelMock.getIndexType()).thenReturn(IndexFileAction.FILE_BASED);
 FileIndexer fileIndexer = mock(FileIndexer.class);
 when(openFileModelMock.getFile()).thenReturn(new File("testFile.txt"));
 when(openFileModelMock.getIndexType()).thenReturn(IndexFileAction.FILE_BASED);
 when(fileIndexer.addObserver(any())).thenReturn(fileIndexer);
 when(FileIndexer.class.newInstance(eq(new File("testFile.txt")), eq(RamFileIndex.class))).thenThrow(new Exception("Error"));
 IndexFileAction indexFileAction = new IndexFileAction(jTailMainModelMock, openFileModelMock);
 indexFileAction.update(Mockito.any(Observable.class), Mockito.anyInt());
 ExceptionHandler.handle(any(Exception.class), eq(UIMessage.ERROR_GENERIC_MESSAGE), anyArray());
 }
}
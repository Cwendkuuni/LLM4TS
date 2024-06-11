import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import fr.pingtimeout.jtail.gui.action.IndexFileAction;
import fr.pingtimeout.jtail.gui.controller.JTailMainModel;
import fr.pingtimeout.jtail.gui.model.JTailModel;
import fr.pingtimeout.jtail.gui.message.UIMessage;
import fr.pingtimeout.jtail.io.FileIndexer;
import fr.pingtimeout.jtail.io.FileIndexerWorker;
import fr.pingtimeout.jtail.io.index.FileIndex;
import fr.pingtimeout.jtail.io.index.RamFileIndex;
import fr.pingtimeout.jtail.io.index.RomFileIndex;

public class IndexFileActionTest {

 private IndexFileAction indexFileAction;
 private JTailMainModel jTailMainModel;
 private OpenFileModel openFileModel;
 private FileIndexer fileIndexer;
 private File file;

 @Before
 public void setUp() {
 jTailMainModel = mock(JTailMainModel.class);
 openFileModel = new OpenFileModel();
 indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
 file = new File("testFile.txt");
 fileIndexer = mock(FileIndexer.class);
 }

 @Test
 public void testActionPerformedWithValidFile() throws IOException {
 // Given
 openFileModel.setFile(file);
 openFileModel.setIndexType(IndexType.FILE_BASED);

 // When
 ActionEvent actionEvent = Mockito.mock(ActionEvent.class);
 indexFileAction.actionPerformed(actionEvent);

 // Then
 verify(jTailMainModel).add(Mockito.any(JTailModel.class));
 verify(fileIndexer).addObserver(indexFileAction);
 FileIndex index = Mockito.mock(FileIndex.class);
 Mockito.when(fileIndexer.getIndex()).thenReturn(index);
 FileIndexerWorker fileIndexerWorker = Mockito.mock(FileIndexerWorker.class);
 Mockito.when(fileIndexer.createWorker()).thenReturn(fileIndexerWorker);
 Mockito.when(fileIndexerWorker.execute()).thenReturn(true);
 }

 @Test
 public void testActionPerformedWithNullFile() {
 // Given
 openFileModel.setFile(null);

 // When
 ActionEvent actionEvent = Mockito.mock(ActionEvent.class);
 indexFileAction.actionPerformed(actionEvent);

 // Then
 verify(jTailMainModel, Mockito.never()).add(Mockito.any(JTailModel.class));
 InformationHandler.handle(true, UIMessage.INFO_NO_FILE_SELECTED, new Object[0]);
 }

 @Test
 public void testActionPerformedWithInvalidIndexType() {
 // Given
 openFileModel.setFile(file);
 openFileModel.setIndexType(-1);

 // When
 ActionEvent actionEvent = Mockito.mock(ActionEvent.class);
 indexFileAction.actionPerformed(actionEvent);

 // Then
 verify(jTailMainModel, Mockito.never()).add(Mockito.any(JTailModel.class));
 ExceptionHandler.handle(new IllegalArgumentException("Invalid index type"), UIMessage.ERROR_INVALID_ARGUMENT, new Object[]{"indexType"});
 }

 @Test
 public void testUpdateWithSuccess() {
 // Given
 openFileModel.setFile(file);
 openFileModel.setIndexType(IndexType.FILE_BASED);
 Mockito.when(fileIndexer.getIndex()).thenReturn(new FileIndex());

 // When
 indexFileAction.update(fileIndexer, 100);

 // Then
 JTailModel jTailModel = Mockito.mock(JTailModel.class);
 Mockito.when(jTailModel.getFileName()).thenReturn(file.getName());
 Mockito.when(jTailMainModel.getActiveModel()).thenReturn(jTailModel);
 verify(jTailMainModel).add(jTailModel);
 }

 @Test
 public void testUpdateWithFileNotFoundException() {
 // Given
 openFileModel.setFile(file);
 openFileModel.setIndexType(IndexType.FILE_BASED);
 Mockito.when(fileIndexer.getIndex()).thenThrow(new FileNotFoundException());

 // When
 indexFileAction.update(fileIndexer, 100);

 // Then
 ExceptionHandler.handle(new FileNotFoundException(), UIMessage.ERROR_FILE_NOT_FOUND, file.getName());
 }
}
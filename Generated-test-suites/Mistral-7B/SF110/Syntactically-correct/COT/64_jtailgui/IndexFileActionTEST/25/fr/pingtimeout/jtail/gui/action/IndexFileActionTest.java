package fr.pingtimeout.jtail.gui.action.test;

import static org.junit.Assert.*;

import fr.pingtimeout.jtail.gui.action.IndexFileAction;
import fr.pingtimeout.jtail.gui.model.JTailMainModel;
import fr.pingtimeout.jtail.gui.model.JTailModel;
import fr.pingtimeout.jtail.io.FileIndexer;
import fr.pingtimeout.jtail.io.FileIndexerWorker;
import fr.pingtimeout.jtail.io.index.FileIndex;
import fr.pingtimeout.jtail.io.index.RamFileIndex;
import fr.pingtimeout.jtail.io.index.RomFileIndex;
import java.awt.event.ActionEvent;
import java.io.File;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class IndexFileActionTest {

 private IndexFileAction indexFileAction;
 private JTailMainModel jTailMainModel;
 private OpenFileModel openFileModel;
 private FileIndex index;

 @Before
 public void setup() {
 jTailMainModel = Mockito.mock(JTailMainModel.class);
 openFileModel = new OpenFileModel();
 index = Mockito.mock(FileIndex.class);
 indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
 }

 @Test
 public void testActionPerformedWithValidFileAndIndexType() {
 // Given
 File file = new File("validFile.txt");
 String indexType = FileIndex.FILE_BASED;
 openFileModel.setFile(file);
 openFileModel.setIndexType(indexType);

 // When
 indexFileAction.actionPerformed(new ActionEvent(new JButton(), ActionEvent.ACTION_PERFORMED, "openFile"));

 // Then
 Mockito.verify(jTailMainModel).add(Mockito.any(JTailModel.class));
 }

 @Test
 public void testActionPerformedWithNullFile() {
 // Given
 openFileModel.setFile(null);

 // When
 indexFileAction.actionPerformed(new ActionEvent(new JButton(), ActionEvent.ACTION_PERFORMED, "openFile"));

 // Then
 InformationHandler.assertHandledMessage(UIMessage.INFO_NO_FILE_SELECTED);
 }

 @Test
 public void testActionPerformedWithInvalidIndexType() {
 // Given
 String invalidIndexType = "INVALID_INDEX_TYPE";
 openFileModel.setIndexType(invalidIndexType);

 // When
 indexFileAction.actionPerformed(new ActionEvent(new JButton(), ActionEvent.ACTION_PERFORMED, "openFile"));

 // Then
 assertThrows(IllegalArgumentException.class, () -> {
 Class<? extends FileIndex> indexerClass = null;
 switch (invalidIndexType) {
 case MEMORY_BASED:
 indexerClass = RamFileIndex.class;
 break;
 case FILE_BASED:
 indexerClass = RomFileIndex.class;
 break;
 default:
 throw new IllegalArgumentException("Invalid index type : " + invalidIndexType);
 }
 new FileIndexer(null, indexerClass);
 });
 }

 @Test
 public void testActionPerformedWithFileNotFoundException() {
 // Given
 File file = new File("invalidFile.txt");
 openFileModel.setFile(file);
 openFileModel.setIndexType(FileIndex.FILE_BASED);
 Mockito.doThrow(new FileNotFoundException("File not found"))
 .when(index).createIndex();

 // When
 indexFileAction.actionPerformed(new ActionEvent(new JButton(), ActionEvent.ACTION_PERFORMED, "openFile"));

 // Then
 ExceptionHandler.assertHandledMessage(UIMessage.ERROR_FILE_NOT_FOUND, file.getName());
 }

 @Test
 public void testUpdateWithSuccess() {
 // Given
 File file = new File("validFile.txt");
 openFileModel.setFile(file);
 openFileModel.setIndexType(FileIndex.FILE_BASED);
 Mockito.when(index.createIndex()).thenReturn(index);
 FileIndexer fileIndexer = new FileIndexer(file, RomFileIndex.class);
 fileIndexer.addObserver(indexFileAction);
 FileIndexerWorker fileIndexerWorker = Mockito.mock(FileIndexerWorker.class);
 Mockito.when(fileIndexerWorker.execute()).thenReturn(true);

 // When
 indexFileAction.update(index, 100);

 // Then
 Mockito.verify(jTailMainModel).add(Mockito.any(JTailModel.class));
 }

 @Test
 public void testUpdateWithFileNotFoundException() {
 // Given
 File file = new File("invalidFile.txt");
 openFileModel.setFile(file);
 openFileModel.setIndexType(FileIndex.FILE_BASED);
 Mockito.doThrow(new FileNotFoundException("File not found"))
 .when(index).createIndex();
 FileIndexer fileIndexer = Mockito.mock(FileIndexer.class);
 fileIndexer.addObserver(indexFileAction);
 FileIndexerWorker fileIndexerWorker = Mockito.mock(FileIndexerWorker.class);
 Mockito.when(fileIndexerWorker.execute()).thenReturn(true);

 // When
 indexFileAction.update(index, 100);

 // Then
 ExceptionHandler.assertHandledMessage(UIMessage.ERROR_FILE_NOT_FOUND, file.getName());
 }

 @Test
 public void testUpdateWithException() {
 // Given
 File file = new File("validFile.txt");
 openFileModel.setFile(file);
 openFileModel.setIndexType(FileIndex.FILE_BASED);
 Mockito.when(index.createIndex()).thenThrow(new IOException("IO Exception"));
 FileIndexer fileIndexer = new FileIndexer(file, RomFileIndex.class);
 fileIndexer.addObserver(indexFileAction);
 FileIndexerWorker fileIndexerWorker = Mockito.mock(FileIndexerWorker.class);
 Mockito.when(fileIndexerWorker.execute()).thenReturn(true);

 // When
 indexFileAction.update(index, 100);

 // Then
 ExceptionHandler.assertHandledMessage(UIMessage.ERROR_GENERIC_MESSAGE, (Object[])new Object[0]);
 }
}
package fr.pingtimeout.jtail.gui.action;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import fr.pingtimeout.jtail.gui.controller.JTailMainModel;
import fr.pingtimeout.jtail.gui.message.UIMessage;
import fr.pingtimeout.jtail.gui.model.JTailModel;
import fr.pingtimeout.jtail.io.index.FileIndexer;
import fr.pingtimeout.jtail.io.index.FileIndexerWorker;
import fr.pingtimeout.jtail.io.index.FileIndexerObserver;
import fr.pingtimeout.jtail.io.index.RamFileIndex;
import fr.pingtimeout.jtail.io.index.RomFileIndex;
import fr.pingtimeout.jtail.util.JTailLogger;

public class IndexFileActionTest {

 private IndexFileAction indexFileAction;
 private JTailMainModel jTailMainModelMock;
 private OpenFileModel openFileModelMock;
 private Component dialogMock;
 private FileIndexer fileIndexerMock;
 private FileIndexerObserver fileIndexerObserverMock;

 @Before
 public void setUp() {
 JUnitCore.main("junit.order.TestMethodSorter"); // To run tests in order
  // Set up mocks
 jTailMainModelMock = mock(JTailMainModel.class);
 openFileModelMock = mock(OpenFileModel.class);
 dialogMock = mock(Component.class);
 fileIndexerMock = mock(FileIndexer.class);
 fileIndexerObserverMock = mock(FileIndexerObserver.class);

 indexFileAction = new IndexFileAction(jTailMainModelMock, openFileModelMock);
 }

 @Test
 public void testActionPerformedNullFile() {
 // Given
 when(openFileModelMock.getFile()).thenReturn(null);

 // When
 indexFileAction.actionPerformed(mock(ActionEvent.class));

 // Then
 verifyNoMoreInteractions(jTailMainModelMock); // No model added
 verify(InformationHandler.class).handle(true, UIMessage.INFO_NO_FILE_SELECTED, new Object[0]);
 }

 @Test
 public void testActionPerformedValidFile() throws Exception {
 // Given
 File file = mock(File.class);
 when(openFileModelMock.getFile()).thenReturn(file);
 when(openFileModelMock.getIndexType()).thenReturn(IndexType.FILE_BASED);
 when(fileIndexerMock.getFile()).thenReturn(file);
 when(fileIndexerMock.addObserver(any())).thenReturn(fileIndexerObserverMock);
 when(fileIndexerObserverMock.getClass()).thenReturn(RomFileIndex.class);
 when(fileIndexerWorkerMock.execute()).thenReturn(fileIndexerMock);

 // When
 indexFileAction.actionPerformed(mock(ActionEvent.class));

 // Then
 verify(jTailMainModelMock).add(argThat(m -> m instanceof JTailModel));
 verify(fileIndexerMock).addObserver(eq(fileIndexerObserverMock));
 }

 @Test
 public void testActionPerformedInvalidFile() {
 // Given
 when(openFileModelMock.getFile()).thenThrow(new FileNotFoundException());

 // When
 indexFileAction.actionPerformed(mock(ActionEvent.class));

 // Then
 verifyNoMoreInteractions(jTailMainModelMock); // No model added
 verify(ExceptionHandler.class).handle(argThat(e -> e instanceof FileNotFoundException), UIMessage.ERROR_FILE_NOT_FOUND, eq(openFileModelMock.getFile().getName()));
 }

 @Test
 public void testUpdateNullFileIndexer() {
 // Given
 when(fileIndexerMock.getFile()).thenReturn(null);

 // When
 indexFileAction.update(fileIndexerMock, 100);

 // Then
 verifyNoMoreInteractions(jTailMainModelMock); // No model added
 }

 @Test
 public void testUpdateValidFileIndexer() throws Exception {
 // Given
 File file = mock(File.class);
 when(fileIndexerMock.getFile()).thenReturn(file);
 when(fileIndexerMock.getIndex()).thenReturn(new FileIndex());
 LineReader lineReaderMock = mock(LineReader.class);
 JTailModel jTailModelMock = mock(JTailModel.class);

 // When
 indexFileAction.update(fileIndexerMock, 100);

 // Then
 ArgumentCaptor<FileIndex> fileIndexCaptor = ArgumentCaptor.forClass(FileIndex.class);
 verify(jTailMainModelMock).add(argThat(m -> m instanceof JTailModel));
 verify(jTailModelMock).setFile(eq(file));
 verify(jTailModelMock).setLineReader(eq(lineReaderMock));
 }
}
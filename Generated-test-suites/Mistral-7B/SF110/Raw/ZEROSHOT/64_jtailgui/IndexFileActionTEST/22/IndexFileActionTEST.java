package fr.pingtimeout.jtail.gui.action;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.Observable;

import org.junit.*;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import fr.pingtimeout.jtail.gui.controller.*;
import fr.pingtimeout.jtail.gui.message.*;
import fr.pingtimeout.jtail.gui.model.*;
import fr.pingtimeout.jtail.io.index.*;
import fr.pingtimeout.jtail.io.*;

public class IndexFileActionTest {

 private IndexFileAction indexFileAction;
 private JTailMainModel jTailMainModelMock;
 private OpenFileModel openFileModelMock;
 private JDialog dialogMock;

 @Before
 public void setUp() {
 jTailMainModelMock = Mockito.mock(JTailMainModel.class);
 openFileModelMock = Mockito.mock(OpenFileModel.class);
 dialogMock = Mockito.mock(JDialog.class);

 indexFileAction = new IndexFileAction(jTailMainModelMock, openFileModelMock);
 }

 @Test
 public void testActionPerformedNullFile() {
 openFileModelMock.setFile(null);
 indexFileAction.actionPerformed(mock(ActionEvent.class));

 verify(jTailMainModelMock).add(Mockito.never());
 verifyNoMoreInteractions(jTailMainModelMock);
 verifyNoMoreInteractions(openFileModelMock);
 verifyNoMoreInteractions(dialogMock);
 }

 @Test
 public void testActionPerformedValidFile() throws FileNotFoundException {
 File file = new File("test.txt");
 openFileModelMock.setFile(file);
 openFileModelMock.setIndexType(IndexType.FILE_BASED);

 ArgumentCaptor<String> fileCaptor = ArgumentCaptor.forClass(String.class);
 ArgumentCaptor<String> indexTypeCaptor = ArgumentCaptor.forClass(String.class);
 ArgumentCaptor<String> labelCaptor = ArgumentCaptor.forClass(String.class);
 ArgumentCaptor<String> tooltipCaptor = ArgumentCaptor.forClass(String.class);
 ArgumentCaptor<Character> mnemonicCaptor = ArgumentCaptor.forClass(Character.class);
 ArgumentCaptor<String> acceleratorCaptor = ArgumentCaptor.forClass(String.class);
 ArgumentCaptor<ImageIcon> iconCaptor = ArgumentCaptor.forClass(ImageIcon.class);

 when(openFileModelMock.getFile()).thenReturn(file);
 when(openFileModelMock.getIndexType()).thenReturn(IndexType.FILE_BASED);
 when(dialogMock.getParent()).thenReturn(null);

 indexFileAction.actionPerformed(mock(ActionEvent.class));

 verify(jTailMainModelMock).add(Mockito.atLeastOnce());
 verify(jTailMainModelMock).add(argThat(model -> model instanceof JTailModel));

 verify(jTailLogger).debug(fileCaptor.capture(), eq("Index type : "));
 verify(jTailLogger).debug(eq("File :"), eq(file));
 verify(InformationHandler).handle(eq(true), eq(UIMessage.INFO_NO_FILE_SELECTED), Mockito.anyArray());

 ArgumentCaptor<Class<? extends FileIndex>> indexerClassCaptor = ArgumentCaptor.forClass(Class.class);
 verify(FileIndexer.class).newInstance(file, eq(RomFileIndex.class));
 verify(fileIndexerWorker).execute();

 ArgumentCaptor<Observer> observerCaptor = ArgumentCaptor.forClass(Observer.class);
 verify(fileIndexer).addObserver(observerCaptor.capture());

 assertEquals("action.open.label", indexFileAction.getText());
 assertEquals("action.open.tooltip", indexFileAction.getToolTipText());
 assertEquals('O', indexFileAction.getMnemonic());
 assertEquals("Ctrl+O", indexFileAction.getAcceleratorKeyStroke().toString());
 assertEquals(OPEN_ICON, indexFileAction.getIcon());

 verify(dialogMock).setVisible(false);

 ArgumentCaptor<File> fileCaptor2 = ArgumentCaptor.forClass(File.class);
 ArgumentCaptor<FileIndexer> fileIndexerCaptor = ArgumentCaptor.forClass(FileIndexer.class);
 verify(jTailMainModelMock).add(fileIndexerCaptor.capture());

 FileIndexer fileIndexer = fileIndexerCaptor.getValue();
 verify(fileIndexer).addObserver(Mockito.eq(indexFileAction));

 ArgumentCaptor<LineReader> lineReaderCaptor = ArgumentCaptor.forClass(LineReader.class);
 ArgumentCaptor<JTailModel> modelCaptor = ArgumentCaptor.forClass(JTailModel.class);
 verify(fileIndexer).getIndex();
 verify(new LineReader(file, fileIndexer.getIndex())).getLineReader();
 verify(jTailModelFactory).createJTailModel(file, lineReaderCaptor.capture(), fileIndexer);
 verify(jTailMainModelMock).add(modelCaptor.capture());

 assertEquals(file, modelCaptor.getValue().getFile());
 assertEquals(lineReaderCaptor.getValue(), modelCaptor.getValue().getLineReader());
 }

 @Test
 public void testActionPerformedInvalidFileType() {
 openFileModelMock.setFile(new File("test.txt"));
 openFileModelMock.setIndexType(IndexType.MEMORY_BASED);
 indexFileAction.actionPerformed(mock(ActionEvent.class));

 verify(jTailMainModelMock).add(Mockito.never());
 verifyNoMoreInteractions(jTailMainModelMock);
 verifyNoMoreInteractions(openFileModelMock);
 verifyNoMoreInteractions(dialogMock);
 }

 @Test
 public void testUpdate() {
 FileIndexer fileIndexerMock = Mockito.mock(FileIndexer.class);
 Observable oMock = Mockito.mock(Observable.class);
 ArgumentCaptor<Observer> observerCaptor = ArgumentCaptor.forClass(Observer.class);

 when(fileIndexer.getFile()).thenReturn(new File("test.txt"));
 when(oMock.getObservers()).thenReturn(Arrays.asList(indexFileAction));
 when(oMock.addObserver(observerCaptor.capture())).thenReturn(this);
 when(fileIndexer.getIndex()).thenReturn(new byte[0][]);
 when(fileIndexer.hasNextIndex()).thenReturn(false);

 indexFileAction.update(oMock, 100);

 verify(jTailLogger).debug(eq("Notification received : 100 emitted by "), eq(fileIndexerMock));
 verify(jTailMainModelMock).add(Mockito.never());
 verify(jTailMainModelMock).remove(Mockito.never());

 ArgumentCaptor<FileIndexer> fileIndexerCaptor = ArgumentCaptor.forClass(FileIndexer.class);
 ArgumentCaptor<File> fileCaptor = ArgumentCaptor.forClass(File.class);
 verify(jTailMainModelMock).add(fileIndexerCaptor.capture());

 FileIndexer fileIndexer = fileIndexerCaptor.getValue();
 assertEquals(fileIndexer, fileIndexerCaptor.getValue());
 assertEquals(new File("test.txt"), fileCaptor.getValue());
 }

 @Test(expected = IOException.class)
 public void testUpdateFileNotFoundException() throws IOException {
 FileIndexer fileIndexerMock = Mockito.mock(FileIndexer.class);
 Observable oMock = Mockito.mock(Observable.class);

 when(fileIndexer.getFile()).thenReturn(new File("test.txt"));
 when(oMock.getObservers()).thenReturn(Arrays.asList(indexFileAction));
 when(fileIndexer.getIndex()).thenThrow(new FileNotFoundException());
 when(fileIndexer.hasNextIndex()).thenReturn(false);

 indexFileAction.update(oMock, 100);

 verify(ExceptionHandler).handle(eq(new FileNotFoundException()), eq(UIMessage.ERROR_FILE_NOT_FOUND), eq("test.txt"));
 }

 @After
 public void tearDown() {
 indexFileAction = null;
 jTailMainModelMock = null;
 openFileModelMock = null;
 dialogMock = null;
 }
}
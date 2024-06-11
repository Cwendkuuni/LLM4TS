package fr.pingtimeout.jtail.gui.action;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import fr.pingtimeout.jtail.gui.controller.JTailMainModel;
import fr.pingtimeout.jtail.gui.message.InformationHandler;
import fr.pingtimeout.jtail.gui.message.UIMessage;
import fr.pingtimeout.jtail.gui.model.JTailModel;
import fr.pingtimeout.jtail.io.FileIndexer;
import fr.pingtimeout.jtail.io.FileIndexerWorker;
import fr.pingtimeout.jtail.io.LineReader;
import fr.pingtimeout.jtail.io.index.FileIndexerObserver;
import fr.pingtimeout.jtail.io.index.RamFileIndex;
import fr.pingtimeout.jtail.io.index.RomFileIndex;

public class IndexFileActionTest {

 private IndexFileAction indexFileAction;
 @Mock
 private JTailMainModel jTailMainModel;
 @Mock
 private OpenFileModel openFileModel;
 @Mock
 private InformationHandler informationHandler;
 @Mock
 private JDialog dialog;
 @Mock
 private FileIndexer fileIndexer;
 @Mock
 private FileIndexerWorker fileIndexerWorker;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
 when(openFileModel.getFile()).thenReturn(new File("testFile.txt"));
 when(openFileModel.getIndexType()).thenReturn(IndexType.FILE_BASED);
 when(fileIndexer.getIndex()).thenReturn(new byte[10]);
 }

 @Test
 public void testActionPerformedWithValidFileAndIndexType() {
 // Given
 ActionEvent actionEvent = new ActionEvent(dialog, MouseEvent.MOUSE_CLICKED, null);
 indexFileAction.actionPerformed(actionEvent);

 // Then
 ArgumentCaptor<String> fileArgumentCaptor = ArgumentCaptor.forClass(String.class);
 ArgumentCaptor<String> indexTypeArgumentCaptor = ArgumentCaptor.forClass(String.class);
 ArgumentCaptor<String> labelArgumentCaptor = ArgumentCaptor.forClass(String.class);
 ArgumentCaptor<String> tooltipArgumentCaptor = ArgumentCaptor.forClass(String.class);
 ArgumentCaptor<Integer> mnemonicArgumentCaptor = ArgumentCaptor.forClass(Integer.class);
 ArgumentCaptor<String> acceleratorArgumentCaptor = ArgumentCaptor.forClass(String.class);
 ArgumentCaptor<ImageIcon> iconArgumentCaptor = ArgumentCaptor.forClass(ImageIcon.class);

 verify(JTailLogger).debug("File : {}", fileArgumentCaptor.capture("testFile.txt"));
 verify(JTailLogger).debug("Index type : {}", indexTypeArgumentCaptor.capture(IndexType.FILE_BASED.toString()));

 verify(informationHandler).handle(true, UIMessage.INFO_NO_FILE_SELECTED, new Object[0]);

 FileIndexer indexerClass = null;
 switch (indexTypeArgumentCaptor.getValue()) {
 case "FILE_BASED":
 indexerClass = RomFileIndex.class;
 break;
 case "MEMORY_BASED":
 indexerClass = RamFileIndex.class;
 break;
 }
 try {
 FileIndexer fileIndexer = new FileIndexer(new File("testFile.txt"), indexerClass);
 fileIndexer.addObserver(this.indexFileAction);
 } catch (FileNotFoundException e) {
 // Test should not reach this point
 }

 FileIndexerWorker fileIndexerWorker = new FileIndexerWorker(fileIndexer);
 fileIndexerWorker.execute();

 verify(jTailMainModel).add(ArgumentCaptor.capture(JTailModel.class));
 JTailModel capturedModel = (JTailModel) fileArgumentCaptor.getValue();
 LineReader lineReader = new LineReader(new File("testFile.txt"), fileIndexer.getIndex());
 JTailModel expectedModel = new JTailModel(new File("testFile.txt"), lineReader);
 assertEquals(expectedModel, capturedModel);
 }

 @Test
 public void testActionPerformedWithNullFile() {
 // Given
 openFileModel.setFile(null);
 ActionEvent actionEvent = new ActionEvent(dialog, MouseEvent.MOUSE_CLICKED, null);

 // When // Then
 verify(informationHandler).handle(true, UIMessage.INFO_NO_FILE_SELECTED, new Object[0]);
 }

 @Test
 public void testUpdateMethodWhenIndexingIsSuccessful() {
 // Given
 indexFileAction.update(fileIndexer, 100);

 // Then
 verify(jTailMainModel).add(ArgumentCaptor.capture(JTailModel.class));
 JTailModel capturedModel = (JTailModel) fileArgumentCaptor.getValue();
 LineReader lineReader = new LineReader(new File("testFile.txt"), fileIndexer.getIndex());
 JTailModel expectedModel = new JTailModel(new File("testFile.txt"), lineReader);
 assertEquals(expectedModel, capturedModel);
 }

 @Test
 public void testUpdateMethodWhenFileNotFoundExceptionIsThrown() {
 // Given
 when(fileIndexer.getFile()).thenThrow(new FileNotFoundException());
 indexFileAction.update(fileIndexer, 100);

 // Then
 verify(ExceptionHandler).handle(ArgumentCaptor.capture(Exception.class), ArgumentCaptor.capture(UIMessage.class), ArgumentCaptor.capture(String.class));
 Exception exception = ArgumentCaptor.getValue();
 UIMessage uiMessage = ArgumentCaptor.getValue();
 String fileName = ArgumentCaptor.getValue();
 assertEquals(UIMessage.ERROR_FILE_NOT_FOUND, uiMessage);
 assertEquals("testFile.txt", fileName);
 assertEquals(exception.getMessage(), "testFile.txt (No such file or directory)");
 }

 @Test
 public void testUpdateMethodWhenGenericExceptionIsThrown() {
 // Given
 when(fileIndexer.getFile()).thenThrow(new IOException("Generic error"));
 indexFileAction.update(fileIndexer, 100);

 // Then
 verify(ExceptionHandler).handle(ArgumentCaptor.capture(Exception.class), ArgumentCaptor.capture(UIMessage.class), ArgumentCaptor.capture(Object[].class));
 Exception exception = ArgumentCaptor.getValue();
 UIMessage uiMessage = ArgumentCaptor.getValue();
 Object[] arguments = ArgumentCaptor.getValue();
 assertEquals(UIMessage.ERROR_GENERIC_MESSAGE, uiMessage);
 assertEquals(arguments.length, 0);
 assertEquals(exception.getMessage(), "Generic error");
 }
}
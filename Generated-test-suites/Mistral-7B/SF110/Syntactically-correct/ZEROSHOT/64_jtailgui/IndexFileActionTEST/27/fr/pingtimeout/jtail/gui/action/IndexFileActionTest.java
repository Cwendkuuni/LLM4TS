package fr.pingtimeout.jtail.gui.action;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import fr.pingtimeout.jtail.controller.JTailMainModel;
import fr.pingtimeout.jtail.gui.message.InformationHandler;
import fr.pingtimeout.jtail.gui.message.UIMessage;
import fr.pingtimeout.jtail.gui.model.JTailModel;
import fr.pingtimeout.jtail.io.FileIndexer;
import fr.pingtimeout.jtail.io.FileIndexerWorker;
import fr.pingtimeout.jtail.io.FileIndexer.IndexType;
import fr.pingtimeout.jtail.io.index.FileIndex;
import fr.pingtimeout.jtail.io.index.RamFileIndex;
import fr.pingtimeout.jtail.io.index.RomFileIndex;

public class IndexFileActionTest {

 private IndexFileAction indexFileAction;
 private JTailMainModel jTailMainModelMock;
 private OpenFileModel openFileModelMock;
 private InformationHandler informationHandlerMock;
 private FileIndexer fileIndexerMock;
 private FileIndex index;

 @Before
 public void setUp() {
 jTailMainModelMock = Mockito.mock(JTailMainModel.class);
 openFileModelMock = Mockito.mock(OpenFileModel.class);
 informationHandlerMock = Mockito.mock(InformationHandler.class);
 index = Mockito.mock(FileIndex.class);
 fileIndexerMock = Mockito.spy(new FileIndexer(new File("testFile.txt"), RamFileIndex.class));
 fileIndexerMock.addObserver(Mockito.mock(Observer.class));

 indexFileAction = new IndexFileAction(jTailMainModelMock, openFileModelMock) {
 @Override
 public void update(Observable o, Object arg) {
 super.update(o, arg);
 informationHandlerMock.handle(true, UIMessage.INFO_FILE_LOADED, new Object[0]);
 }
 };

 doReturn(informationHandlerMock).when(indexFileAction).getInformationHandler();
 doReturn(openFileModelMock).when(indexFileAction).getOpenFileModel();
 }

 @Test
 public void testActionPerformedWithNullFile() {
 ActionEvent actionEvent = Mockito.mock(ActionEvent.class);
 indexFileAction.actionPerformed(actionEvent);
 verify(informationHandlerMock).handle(true, UIMessage.INFO_NO_FILE_SELECTED, new Object[0]);
 }

 @Test
 public void testActionPerformedWithValidFileAndIndexType() throws IOException {
 ActionEvent actionEvent = Mockito.mock(ActionEvent.class);
 File file = new File("testFile.txt");
 doReturn(file).when(openFileModelMock).getFile();
 doReturn(IndexType.MEMORY_BASED).when(openFileModelMock).getIndexType();

 indexFileAction.actionPerformed(actionEvent);
 verify(jTailMainModelMock).add(Mockito.isA(JTailModel.class));
 verify(fileIndexerMock).addObserver(Mockito.any());
 FileIndexerWorker fileIndexerWorker = verify(fileIndexerMock, times(1)).getWorker();
 fileIndexerWorker.execute();
 }

 @Test(expected = FileNotFoundException.class)
 public void testActionPerformedWithInvalidFile() throws IOException {
 ActionEvent actionEvent = Mockito.mock(ActionEvent.class);
 File file = new File("invalidFile.txt");
 doReturn(file).when(openFileModelMock).getFile();
 doReturn(IndexType.FILE_BASED).when(openFileModelMock).getIndexType();

 indexFileAction.actionPerformed(actionEvent);
 verify(informationHandlerMock).handle(UIMessage.ERROR_FILE_NOT_FOUND, new Object[]{file.getName()});
 }

 @Test
 public void testUpdateWithPercentage100() {
 FileIndexer fileIndexer = Mockito.mock(FileIndexer.class);
 observe(fileIndexer, 100);
 indexFileAction.update(fileIndexer, 100);
 verify(jTailMainModelMock).add(Mockito.isA(JTailModel.class));
 }

 private void observe(Observable observable, int arg) {
 doReturn(indexFileAction).when(observable).getObserver();
 doReturn(index).when(observable).getIndex();
 doReturn(fileIndexerMock).when(observable).getFileIndexer();
}

}
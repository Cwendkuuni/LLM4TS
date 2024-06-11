import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentsMatchers;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import fr.pingtimeout.jtail.gui.action.IndexFileAction;
import fr.pingtimeout.jtail.gui.controller.JTailMainModel;
import fr.pingtimeout.jtail.gui.message.UIMessage;
import fr.pingtimeout.jtail.io.index.FileIndexer;
import fr.pingtimeout.jtail.io.index.FileIndexerWorker;
import fr.pingtimeout.jtail.io.index.RamFileIndex;
import fr.pingtimeout.jtail.io.index.RomFileIndex;
import fr.pingtimeout.jtail.io.lineReader.LineReader;
import fr.pingtimeout.jtail.gui.model.JTailModel;
import fr.pingtimeout.jtail.util.JTailLogger;

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
 File file = new File("testFile.txt");
 openFileModelMock = Mockito.spy(new OpenFileModel("testFile.txt", RamFileIndex.class));

 indexFileAction.actionPerformed(Mockito.mock(ActionEvent.class));

 verify(jTailMainModelMock).add(Mockito.any(JTailModel.class));
 verify(openFileModelMock).getFile();
 verify(openFileModelMock).getIndexType();

 Mockito.verify(fileIndexerMock, Mockito.times(1)).addObserver(indexFileAction);
 Mockito.verify(fileIndexerWorkerMock, Mockito.times(1)).execute();
 }

 @Test(expected = FileNotFoundException.class)
 public void testActionPerformed_InvalidFile() {
 openFileModelMock = Mockito.spy(new OpenFileModel(null, RomFileIndex.class));

 indexFileAction.actionPerformed(Mockito.mock(ActionEvent.class));

 verify(jTailMainModelMock, Mockito.never()).add(Mockito.any(JTailModel.class));
 verify(InformationHandlerMock, Mockito.times(1)).handle(Mockito.eq(true), ArgumentsMatchers.any(UIMessage.class), Mockito.anyArray(Object.class));
 }

 @Test(expected = InstantiationException.class)
 public void testActionPerformed_InvalidIndexType() {
 openFileModelMock = Mockito.spy(new OpenFileModel("testFile.txt", null));

 indexFileAction.actionPerformed(Mockito.mock(ActionEvent.class));

 verify(jTailMainModelMock, Mockito.never()).add(Mockito.any(JTailModel.class));
 verify(ExceptionHandlerMock, Mockito.times(1)).handle(Mockito.any(Exception.class), ArgumentsMatchers.eq(UIMessage.ERROR_GENERIC_MESSAGE), Mockito.anyArray(Object.class));
 }
}
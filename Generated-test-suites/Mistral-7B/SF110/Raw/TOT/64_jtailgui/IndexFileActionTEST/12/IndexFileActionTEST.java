import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import fr.pingtimeout.jtail.gui.action.IndexFileAction;
import fr.pingtimeout.jtail.gui.controller.JTailMainModel;
import fr.pingtimeout.jtail.gui.message.InformationHandler;
import fr.pingtimeout.jtail.gui.message.UIMessage;
import fr.pingtimeout.jtail.io.FileIndexer;
import fr.pingtimeout.jtail.io.FileIndexerWorker;
import fr.pingtimeout.jtail.io.index.FileIndex;
import fr.pingtimeout.jtail.io.index.RamFileIndex;
import fr.pingtimeout.jtail.io.index.RomFileIndex;

public class IndexFileActionTest {

 private JTailMainModel jTailMainModel;
 private OpenFileModel openFileModel;
 private IndexFileAction indexFileAction;
 private InformationHandler informationHandler;

 @Before
 public void setUp() {
 jTailMainModel = mock(JTailMainModel.class);
 openFileModel = new OpenFileModel(null,null);
 informationHandler = mock(InformationHandler.class);
 indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
 }

 @Test
 public void testActionPerformed_ValidFileAndIndexType() {
 File file = new File("test.txt");
 openFileModel.setFile(file);
 openFileModel.setIndexType(IndexType.FILE_BASED);
 ArgumentCaptor<File> fileCaptor = ArgumentCaptor.forClass(File.class);
 ArgumentCaptor<Class<? extends FileIndex>> indexerClassCaptor = ArgumentCaptor.forClass(Class.class);
 try {
 indexFileAction.actionPerformed(new ActionEvent(new JButton(), 0, "Open File", 0));
 } catch (IOException e) {
 e.printStackTrace();
 }
 verify(jTailMainModel).add(Mockito.any(JTailModel.class));
 verify(informationHandler).handle(false, UIMessage.INFO_FILE_IMPORTED_SUCCESSFULLY, new Object[0]);
 verify(fileIndexerWorker).execute();
 verify(openFileModel).setProcessing(true);
 verify(openFileModel).setFile(fileCaptor.capture());
 verify(openFileModel).setIndexType(indexerClassCaptor.capture());
 FileIndex index = new FileIndex();
 FileIndexer fileIndexer = new FileIndexer(file, indexerClassCaptor.getValue());
 fileIndexer.addObserver(indexFileAction);
 }

 @Test
 public void testActionPerformed_NullFile() {
 openFileModel.setFile(null);
 indexFileAction.actionPerformed(new ActionEvent(new JButton(), 0, "Open File", 0));
 verify(informationHandler).handle(true, UIMessage.INFO_NO_FILE_SELECTED, new Object[0]);
 }

 @Test
 public void testActionPerformed_InvalidIndexType() {
 openFileModel.setFile(new File("test.txt"));
 openFileModel.setIndexType(IndexType.INVALID);
 indexFileAction.actionPerformed(new ActionEvent(new JButton(), 0, "Open File", 0));
 verify(informationHandler).handle(true, UIMessage.INFO_INVALID_INDEX_TYPE, new Object[0]);
 }
}

class OpenFileModel {
 private File file;
 private IndexType indexType;
 private boolean processing;

 // getters and setters
}

enum IndexType {
 MEMORY_BASED,
 FILE_BASED,
 INVALID
}
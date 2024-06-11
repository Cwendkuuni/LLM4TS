package fr.pingtimeout.jtail.gui.action;

import static org.junit.Assert.*;
import org.junit.Test;

import fr.pingtimeout.jtail.gui.model.JTailMainModel;
import fr.pingtimeout.jtail.gui.model.OpenFileModel;
import fr.pingtimeout.jtail.io.index.FileIndexer;
import fr.pingtimeout.jtail.io.index.FileIndexerWorker;
import fr.pingtimeout.jtail.io.index.RamFileIndex;
import fr.pingtimeout.jtail.io.index.RomFileIndex;
import fr.pingtimeout.jtail.gui.message.InformationHandler;
import fr.pingtimeout.jtail.gui.message.UIMessage;

public class IndexFileActionTest {

    @Test
    public void testInit() {
        JTailMainModel jTailMainModel = mock(JTailMainModel.class);
        OpenFileModel openFileModel = mock(OpenFileModel.class);
        IndexFileAction indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
        assertEquals("action.open.label", indexFileAction.getText());
        assertNotNull(indexFileAction.getIcon());
    }

    @Test
    public void testActionPerformed_NullFile() {
        JTailMainModel jTailMainModel = mock(JTailMainModel.class);
        OpenFileModel openFileModel = mock(OpenFileModel.class);
        IndexFileAction indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
        ActionEvent actionEvent = mock(ActionEvent.class);
        indexFileAction.actionPerformed(actionEvent);
        InformationHandler.assertHandled(true, UIMessage.INFO_NO_FILE_SELECTED, new Object[0]);
    }

    @Test
    public void testActionPerformed_InvalidIndexType() {
        JTailMainModel jTailMainModel = mock(JTailMainModel.class);
        OpenFileModel openFileModel = new OpenFileModel();
        openFileModel.setFile(new File("test.txt"));
        openFileModel.setIndexType(Integer.MAX_VALUE); // Invalid index type
        IndexFileAction indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
        ActionEvent actionEvent = mock(ActionEvent.class);
        indexFileAction.actionPerformed(actionEvent);
        assertNull(indexFileAction.getIndexerClass());
    }

    @Test
    public void testActionPerformed_MemoryBasedIndex() {
        JTailMainModel jTailMainModel = mock(JTailMainModel.class);
        OpenFileModel openFileModel = new OpenFileModel();
        openFileModel.setFile(new File("test.txt"));
        openFileModel.setIndexType(IndexFileAction.MemoryBased);
        IndexFileAction indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
        ActionEvent actionEvent = mock(ActionEvent.class);
        indexFileAction.actionPerformed(actionEvent);
        assertEquals(RamFileIndex.class, indexFileAction.getIndexerClass());
    }

    @Test
    public void testActionPerformed_FileBasedIndex() {
        JTailMainModel jTailMainModel = mock(JTailMainModel.class);
        OpenFileModel openFileModel = new OpenFileModel();
        openFileModel.setFile(new File("test.txt"));
        openFileModel.setIndexType(IndexFileAction.FileBased);
        IndexFileAction indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
        ActionEvent actionEvent = mock(ActionEvent.class);
        indexFileAction.actionPerformed(actionEvent);
        assertEquals(RomFileIndex.class, indexFileAction.getIndexerClass());
    }

    @Test
    public void testActionPerformed_FileNotFoundException() {
        JTailMainModel jTailMainModel = mock(JTailMainModel.class);
        OpenFileModel openFileModel = new OpenFileModel();
        openFileModel.setFile(new File("test.txt"));
        IndexFileAction indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
        ActionEvent actionEvent = mock(ActionEvent.class);
        FileIndexerWorker fileIndexerWorker = mock(FileIndexerWorker.class);
        when(fileIndexerWorker.execute()).thenThrow(new FileNotFoundException("Test error"));
        doNothing().when(indexFileAction).handleException(eq(UIMessage.ERROR_FILE_NOT_FOUND), anyArray());
        indexFileAction.actionPerformed(actionEvent);
        InformationHandler.assertHandled(true, UIMessage.ERROR_FILE_NOT_FOUND, "test.txt");
    }

    @Test
    public void testUpdate_IndexingCompleted() {
        JTailMainModel jTailMainModel = mock(JTailMainModel.class);
        OpenFileModel openFileModel = new OpenFileModel();
        FileIndexer fileIndexer = mock(FileIndexer.class);
        FileIndexerWorker fileIndexerWorker = mock(FileIndexerWorker.class);
        when(fileIndexerWorker.execute()).thenReturn(fileIndexer);
        IndexFileAction indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
        indexFileAction.update(fileIndexer, 100);
        JTailModel jTailModel = new JTailModel(new File("test.txt"), new LineReader(new File("test.txt"), fileIndexer.getIndex()));
        assertEquals(jTailModel, jTailMainModel.getLastAddedModel());
    }

    @Test
    public void testUpdate_ExceptionThrown() {
        JTailMainModel jTailMainModel = mock(JTailMainModel.class);
        OpenFileModel openFileModel = new OpenFileModel();
        IndexFileAction indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
        FileIndexer fileIndexer = mock(FileIndexer.class);
        FileIndexerWorker fileIndexerWorker = mock(FileIndexerWorker.class);
        when(fileIndexerWorker.execute()).thenThrow(new Exception("Test error"));
        doNothing().when(indexFileAction).handleException(eq(UIMessage.ERROR_GENERIC_MESSAGE), anyArray());
        indexFileAction.update(fileIndexer, 100);
        ExceptionHandler.assertHandled(true, UIMessage.ERROR_GENERIC_MESSAGE, new Object[0]);
    }
}
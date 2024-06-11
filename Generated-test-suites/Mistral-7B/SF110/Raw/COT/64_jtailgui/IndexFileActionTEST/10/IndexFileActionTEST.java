package fr.pingtimeout.jtail.gui.action.test;

import static org.junit.Assert.*;

import fr.pingtimeout.jtail.gui.action.IndexFileAction;
import fr.pingtimeout.jtail.gui.model.JTailMainModel;
import fr.pingtimeout.jtail.gui.model.JTailModel;
import fr.pingtimeout.jtail.io.index.FileIndexer;
import fr.pingtimeout.jtail.io.index.FileIndexerWorker;
import fr.pingtimeout.jtail.io.index.RamFileIndex;
import fr.pingtimeout.jtail.io.index.RomFileIndex;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

public class IndexFileActionTest {

    private IndexFileAction indexFileAction;
    private JTailMainModel jTailMainModel;
    private OpenFileModel openFileModel;

    @Before
    public void setup() {
        jTailMainModel = Mockito.mock(JTailMainModel.class);
        openFileModel = Mockito.mock(OpenFileModel.class);
        indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
    }

    @Test
    public void testActionPerformedNullFile() {
        ActionEvent actionEvent = Mockito.mock(ActionEvent.class);
        indexFileAction.actionPerformed(actionEvent);
        InformationHandler.assertHandled(true, UIMessage.INFO_NO_FILE_SELECTED);
    }

    @Test
    public void testActionPerformedInvalidIndexType() {
        openFileModel.setFile(new File("testFile.txt"));
        openFileModel.setIndexType(-1);
        ActionEvent actionEvent = Mockito.mock(ActionEvent.class);
        indexFileAction.actionPerformed(actionEvent);
        assertNull(indexFileAction.openFileModel.getIndexerClass());
    }

    @Test
    public void testActionPerformedValidIndexType() {
        openFileModel.setFile(new File("testFile.txt"));
        openFileModel.setIndexType(IndexFileAction.MemoryBasedIndexType.MEMORY_BASED);
        ActionEvent actionEvent = Mockito.mock(ActionEvent.class);
        indexFileAction.actionPerformed(actionEvent);
        assertEquals(RamFileIndex.class, indexFileAction.openFileModel.getIndexerClass());
    }

    @Test(expected = FileNotFoundException.class)
    public void testActionPerformedInvalidFile() throws FileNotFoundException {
        openFileModel.setFile(new File("testFile.txt"));
        openFileModel.setIndexType(IndexFileAction.MemoryBasedIndexType.MEMORY_BASED);
        ActionEvent actionEvent = Mockito.mock(ActionEvent.class);
        indexFileAction.actionPerformed(actionEvent);
        FileIndexer fileIndexer = Mockito.mock(FileIndexer.class);
        when(fileIndexer.addObserver(indexFileAction)).thenReturn(true);
        when(fileIndexer.getFile()).thenThrow(new FileNotFoundException("Test File Not Found"));
        when(indexFileAction.openFileModel.getFile()).thenReturn(new File("testFile.txt"));
        when(indexFileAction.openFileModel.getIndexType()).thenReturn(IndexFileAction.MemoryBasedIndexType.MEMORY_BASED);

        indexFileAction.actionPerformed(actionEvent);
        ExceptionHandler.verifyHandled(UIMessage.ERROR_FILE_NOT_FOUND, "testFile.txt");
    }

    @Test
    public void testUpdateMethod() {
        FileIndexer fileIndexer = Mockito.mock(FileIndexer.class);
        when(fileIndexer.getFile()).thenReturn(new File("testFile.txt"));
        openFileModel.setFile(new File("testFile.txt"));
        openFileModel.setIndexType(IndexFileAction.MemoryBasedIndexType.MEMORY_BASED);
        JTailModel jTailModel = Mockito.mock(JTailModel.class);

        indexFileAction.update(fileIndexer, 100);
        JTailMainModel jTailMainModel = Mockito.mock(JTailMainModel.class);
        when(jTailMainModel.add(jTailModel)).thenReturn(true);
        indexFileAction.jTailMainModel = jTailMainModel;

        indexFileAction.update(fileIndexer, 100);
        verify(jTailMainModel).add(jTailModel);
    }

    @Test
    public void testFileIndexerWorkerExecution() throws IOException, InterruptedException {
        FileIndexer fileIndexer = Mockito.mock(FileIndexer.class);
        FileIndexerWorker fileIndexerWorker = new FileIndexerWorker(fileIndexer);
        fileIndexerWorker.execute();
        verify(fileIndexer).addObserver(indexFileAction);
    }

}
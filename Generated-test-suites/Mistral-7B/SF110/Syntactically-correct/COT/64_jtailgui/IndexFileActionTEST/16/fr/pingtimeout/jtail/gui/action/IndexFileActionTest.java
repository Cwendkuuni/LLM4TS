package fr.pingtimeout.jtail.gui.action.test;

import static org.junit.Assert.*;

import fr.pingtimeout.jtail.gui.action.IndexFileAction;
import fr.pingtimeout.jtail.gui.controller.JTailMainModel;
import fr.pingtimeout.jtail.gui.model.JTailModel;
import fr.pingtimeout.jtail.io.index.FileIndexer;
import fr.pingtimeout.jtail.io.index.FileIndexerWorker;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.awt.event.ActionEvent;
import java.io.File;

public class IndexFileActionTest {

    private IndexFileAction indexFileAction;
    private JTailMainModel jTailMainModel;
    private OpenFileModel openFileModel;

    @Before
    public void setup() {
        jTailMainModel = Mockito.mock(JTailMainModel.class);
        openFileModel = new OpenFileModel(null, FileIndexType.FILE_BASED);
        indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
    }

    @Test
    public void testActionPerformedWithValidFileAndIndexType() {
        // Given
        File file = Mockito.mock(File.class);
        openFileModel.setFile(file);
        FileIndexType indexType = FileIndexType.FILE_BASED;
        openFileModel.setIndexType(indexType);
        Class<? extends FileIndex> indexerClass = RomFileIndex.class;

        // When
        indexFileAction.actionPerformed(new ActionEvent(new JButton(), ActionEvent.ACTION_PERFORMED, "test"));

        // Then
        assertEquals(indexType, openFileModel.getIndexType());
        assertEquals(indexerClass, openFileModel.getIndexerClass());
        assertTrue(fileIndexerWorkerExecuted());
    }

    @Test
    public void testActionPerformedWithNullFile() {
        // Given
        openFileModel.setFile(null);

        // When
        indexFileAction.actionPerformed(new ActionEvent(new JButton(), ActionEvent.ACTION_PERFORMED, "test"));

        // Then
        assertNull(openFileModel.getFile());
        InformationHandler.assertHandleCalledWith(true, UIMessage.INFO_NO_FILE_SELECTED, new Object[0]);
    }

    @Test
    public void testActionPerformedWithInvalidIndexType() {
        // Given
        openFileModel.setFile(new File("test.txt"));
        openFileModel.setIndexType(null);

        // When
        indexFileAction.actionPerformed(new ActionEvent(new JButton(), ActionEvent.ACTION_PERFORMED, "test"));

        // Then
        assertNull(openFileModel.getIndexType());
        InformationHandler.assertHandleCalledWith(true, UIMessage.INFO_INVALID_INDEX_TYPE, new Object[0]);
    }

    @Test(expected = FileNotFoundException.class)
    public void testActionPerformedWithNonExistentFile() {
        // Given
        openFileModel.setFile(new File("non_existent_file.txt"));

        // When
        indexFileAction.actionPerformed(new ActionEvent(new JButton(), ActionEvent.ACTION_PERFORMED, "test"));

        // Then
        // Exception should be thrown
    }

    @Test
    public void testUpdateWithSuccessfullyIndexedFile() {
        // Given
        File file = Mockito.mock(File.class);
        openFileModel.setFile(file);
        FileIndex index = Mockito.mock(FileIndex.class);
        FileIndexer fileIndexer = Mockito.mock(FileIndexer.class);
        FileIndexerWorker fileIndexerWorker = new FileIndexerWorker(fileIndexer);
        fileIndexerWorker.setStatus(FileIndexerWorker.Status.FINISHED);

        // When
        openFileModel.setIndex(index);
        indexFileAction.update(fileIndexer, 100);

        // Then
        JTailModel jTailModel = Mockito.mock(JTailModel.class);
        this.jTailMainModel.add(jTailModel);
        assertEquals(1, this.jTailMainModel.getSize());
    }

    @Test
    public void testUpdateWithFailedIndexing() {
        // Given
        File file = Mockito.mock(File.class);
        openFileModel.setFile(file);
        FileIndex index = Mockito.mock(FileIndex.class);
        FileIndexer fileIndexer = Mockito.mock(FileIndexer.class);
        FileIndexerWorker fileIndexerWorker = new FileIndexerWorker(fileIndexer);
        fileIndexerWorker.setStatus(FileIndexerWorker.Status.FAILED);

        // When
        openFileModel.setIndex(index);
        indexFileAction.update(fileIndexer, 100);

        // Then
        ExceptionHandler.assertHandleCalledWith(fileIndexer.getException(), UIMessage.ERROR_GENERIC_MESSAGE, new Object[0]);
    }

    private boolean fileIndexerWorkerExecuted() {
        return Mockito.verify(openFileModel, Mockito.times(1)).setIndexerWorker(Mockito.any(FileIndexerWorker.class));
    }
}
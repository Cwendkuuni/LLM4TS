package fr.pingtimeout.jtail.gui.action.test;

import static org.junit.Assert.*;
import fr.pingtimeout.jtail.gui.action.IndexFileAction;
import fr.pingtimeout.jtail.gui.model.JTailMainModel;
import fr.pingtimeout.jtail.gui.model.JTailModel;
import fr.pingtimeout.jtail.io.index.FileIndexer;
import fr.pingtimeout.jtail.io.index.FileIndexerWorker;
import fr.pingtimeout.jtail.io.index.FileIndexerObserver;
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
    private FileIndexer fileIndexer;
    private FileIndexerWorker fileIndexerWorker;

    @Before
    public void setUp() {
        jTailMainModel = Mockito.mock(JTailMainModel.class);
        openFileModel = Mockito.mock(OpenFileModel.class);
        indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
        fileIndexer = Mockito.mock(FileIndexer.class);
        fileIndexerWorker = Mockito.mock(FileIndexerWorker.class);
    }

    @Test
    public void testActionPerformed_NullFile() {
        ActionEvent actionEvent = Mockito.mock(ActionEvent.class);
        when(openFileModel.getFile()).thenReturn(null);
        indexFileAction.actionPerformed(actionEvent);
        verify(jTailMainModel).add(Mockito.isNull());
        InformationHandler.verifyHandle(true, UIMessage.INFO_NO_FILE_SELECTED);
    }

    @Test
    public void testActionPerformed_InvalidIndexType() {
        ActionEvent actionEvent = Mockito.mock(ActionEvent.class);
        when(openFileModel.getFile()).thenReturn(new File("test.txt"));
        when(openFileModel.getIndexType()).thenReturn(-1);
        indexFileAction.actionPerformed(actionEvent);
        verify(jTailMainModel, never()).add(Mockito.any());
        ExceptionHandler.verifyHandle(new IllegalArgumentException("Invalid index type"), UIMessage.ERROR_INVALID_INDEX_TYPE);
    }

    @Test
    public void testActionPerformed_MemoryBasedIndex() throws IOException {
        ActionEvent actionEvent = Mockito.mock(ActionEvent.class);
        when(openFileModel.getFile()).thenReturn(new File("test.txt"));
        when(openFileModel.getIndexType()).thenReturn(IndexFileAction.IndexType.MEMORY_BASED);

        when(fileIndexerWorker.execute()).thenReturn(true);
        when(fileIndexer.addObserver(Mockito.any())).thenReturn(fileIndexer);
        when(fileIndexer.getIndex()).thenReturn(new byte[0]);

        indexFileAction.actionPerformed(actionEvent);

        verify(jTailMainModel).add(Mockito.notNull());
        JTailModel jTailModel = Mockito.argThat(jTailModel -> jTailModel instanceof JTailModel);
        verify(jTailModel).setFilePath(Mockito.eq("test.txt"));
        verify(fileIndexer).addObserver(indexFileAction);

        FileIndexerObserver observer = Mockito.argThat(observer -> observer instanceof IndexFileAction);
        verify(fileIndexer).addObserver(observer);
    }

    @Test
    public void testActionPerformed_FileBasedIndex() throws IOException {
        ActionEvent actionEvent = Mockito.mock(ActionEvent.class);
        when(openFileModel.getFile()).thenReturn(new File("test.txt"));
        when(openFileModel.getIndexType()).thenReturn(IndexFileAction.IndexType.FILE_BASED);

        when(fileIndexerWorker.execute()).thenReturn(true);
        when(fileIndexer.addObserver(Mockito.any())).thenReturn(fileIndexer);
        when(fileIndexer.getIndex()).thenReturn(new byte[0]);

        indexFileAction.actionPerformed(actionEvent);

        verify(jTailMainModel).add(Mockito.notNull());
        JTailModel jTailModel = Mockito.argThat(jTailModel -> jTailModel instanceof JTailModel);
        verify(jTailModel).setFilePath(Mockito.eq("test.txt"));
        verify(fileIndexer).addObserver(indexFileAction);

        FileIndexerObserver observer = Mockito.argThat(observer -> observer instanceof IndexFileAction);
        verify(fileIndexer).addObserver(observer);
    }

    @Test(expected = FileNotFoundException.class)
    public void testActionPerformed_FileNotFoundException() throws IOException {
        ActionEvent actionEvent = Mockito.mock(ActionEvent.class);
        when(openFileModel.getFile()).thenReturn(new File("test.txt"));
        when(openFileModel.getIndexType()).thenReturn(IndexFileAction.IndexType.MEMORY_BASED);

        when(fileIndexerWorker.execute()).thenThrow(new FileNotFoundException());

        indexFileAction.actionPerformed(actionEvent);

        ExceptionHandler.verifyHandle(new FileNotFoundException("test.txt"), UIMessage.ERROR_FILE_NOT_FOUND);
    }

    @Test
    public void testUpdate() {
        indexFileAction.update(fileIndexer, 100);
        verify(jTailMainModel).add(Mockito.notNull());
    }
}
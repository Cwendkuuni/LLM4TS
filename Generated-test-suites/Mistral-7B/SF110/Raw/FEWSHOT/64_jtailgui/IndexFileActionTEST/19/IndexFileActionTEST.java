package fr.pingtimeout.jtail.gui.action;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.Observable;

import org.junit.Before;
import org.junit.Test;

import fr.pingtimeout.jtail.gui.controller.JTailMainModel;
import fr.pingtimeout.jtail.gui.message.InformationHandler;
import fr.pingtimeout.jtail.gui.message.UIMessage;
import fr.pingtimeout.jtail.gui.model.JTailModel;
import fr.pingtimeout.jtail.io.index.FileIndexer;
import fr.pingtimeout.jtail.io.index.FileIndexerWorker;
import fr.pingtimeout.jtail.io.index.RamFileIndex;
import fr.pingtimeout.jtail.io.index.RomFileIndex;
import fr.pingtimeout.jtail.io.line.LineReader;

public class IndexFileActionTest {

    private IndexFileAction indexFileAction;
    private JTailMainModel jTailMainModel;
    private OpenFileModel openFileModel;
    private FileIndexer fileIndexer;
    private File file;

    @Before
    public void setup() {
        jTailMainModel = mock(JTailMainModel.class);
        openFileModel = new OpenFileModel();
        file = new File("testFile.txt");
        indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
    }

    @Test
    public void testActionPerformedNullFile() {
        indexFileAction.actionPerformed(mock(ActionEvent.class));
        verify(jTailMainModel, never()).add(any(JTailModel.class));
    }

    @Test
    public void testActionPerformedValidFile() {
        openFileModel.setFile(file);
        openFileModel.setIndexType(IndexFileModel.IndexType.FILE_BASED);

        when(jTailMainModel.add(any(JTailModel.class)))
                .thenReturn(true);

        indexFileAction.actionPerformed(mock(ActionEvent.class));

        verify(jTailMainModel, times(1)).add(any(JTailModel.class));
        verify(jTailMainModel, never()).add(any(JTailModel.class), eq(false));
        verify(openFileModel, times(1)).getFile();
        verify(openFileModel, times(1)).getIndexType();

        // Verify that the correct indexer class is used
        IndexType indexType = IndexType.FILE_BASED;
        FileIndexer expectedIndexer = new RomFileIndexer(file);
        when(openFileModel.getIndexType()).thenReturn(indexType);
        FileIndexer actualIndexer = indexFileAction.new FileIndexer(openFileModel.getFile(), indexerClass);
        assertEquals(expectedIndexer.getClass(), actualIndexer.getClass());

        // Verify that the fileIndexer is added as an observer
        verify(actualIndexer, times(1)).addObserver(indexFileAction);

        FileIndexerWorker fileIndexerWorker = new FileIndexerWorker(actualIndexer);
        fileIndexerWorker.execute();

        // Verify that the FileIndexerWorker is executed
        verify(actualIndexer, times(1)).execute();
    }

    @Test
    public void testActionPerformedInvalidIndexType() {
        openFileModel.setFile(file);
        openFileModel.setIndexType(IndexFileModel.IndexType.INVALID);

        indexFileAction.actionPerformed(mock(ActionEvent.class));

        verify(jTailMainModel, never()).add(any(JTailModel.class));
        verify(InformationHandler, times(1)).handle(eq(true), any(UIMessage.class), any(Object[].class));
    }

    @Test
    public void testUpdate() {
        openFileModel.setFile(file);
        openFileModel.setIndexType(IndexFileModel.IndexType.FILE_BASED);

        indexFileAction.update(mock(Observable.class), 100);

        verify(jTailMainModel, times(1)).add(any(JTailModel.class));
    }
}
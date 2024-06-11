package fr.pingtimeout.jtail.gui.action.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Observable;

import org.junit.Before;
import org.junit.Test;

import fr.pingtimeout.jtail.controller.JTailMainModel;
import fr.pingtimeout.jtail.gui.action.IndexFileAction;
import fr.pingtimeout.jtail.gui.message.UIMessage;
import fr.pingtimeout.jtail.io.index.FileIndexer;
import fr.pingtimeout.jtail.io.index.FileIndexerWorker;
import fr.pingtimeout.jtail.io.index.RamFileIndex;
import fr.pingtimeout.jtail.io.index.RomFileIndex;
import fr.pingtimeout.jtail.gui.model.JTailModel;
import fr.pingtimeout.jtail.util.JTailLogger;
import fr.pingtimeout.jtail.gui.message.InformationHandler;
import fr.pingtimeout.jtail.gui.message.ExceptionHandler;

public class IndexFileActionTest {

    private IndexFileAction indexFileAction;
    private JTailMainModel jTailMainModel;
    private OpenFileModel openFileModel;
    private Observable fileIndexer;
    private FileIndexer fileIndexerMock;
    private FileIndexerWorker fileIndexerWorkerMock;

    @Before
    public void setUp() {
        jTailMainModel = mock(JTailMainModel.class);
        openFileModel = new OpenFileModel();
        indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);

        fileIndexerMock = mock(FileIndexer.class);
        fileIndexerWorkerMock = mock(FileIndexerWorker.class);
        fileIndexer = fileIndexerMock;
    }

    @Test
    public void testActionPerformedWithNullFile() {
        ActionEvent event = new ActionEvent(new Object(), ActionEvent.ACTION_PERFORMED, "actionPerformed");
        indexFileAction.actionPerformed(event);
        InformationHandler.assertHandledMessage(UIMessage.INFO_NO_FILE_SELECTED);
    }

    @Test
    public void testActionPerformedWithValidFile() throws FileNotFoundException {
        openFileModel.setFile(new File("test.txt"));
        ActionEvent event = new ActionEvent(new Object(), ActionEvent.ACTION_PERFORMED, "actionPerformed");

        indexFileAction.actionPerformed(event);
        FileIndexer indexer = null;
        switch (openFileModel.getIndexType()) {
            case MEMORY_BASED: {
                indexer = new RamFileIndex();
                break;
            }
            case FILE_BASED: {
                indexer = new RomFileIndex();
                break;
            }
        }
        verify(fileIndexerMock).__init__(openFileModel.getFile(), indexer.getClass());

        FileIndexerWorker worker = new FileIndexerWorker(fileIndexer);
        worker.execute();

        verify(fileIndexerMock).addObserver(indexFileAction);
    }

    @Test(expected = FileNotFoundException.class)
    public void testActionPerformedWithMissingFile() throws FileNotFoundException, InterruptedException {
        openFileModel.setFile(new File("missingFile.txt"));
        ActionEvent event = new ActionEvent(new Object(), ActionEvent.ACTION_PERFORMED, "actionPerformed");

        indexFileAction.actionPerformed(event);
        ExceptionHandler.assertHandledMessage(UIMessage.ERROR_FILE_NOT_FOUND, "missingFile.txt");
    }

    @Test
    public void testUpdateWithSuccessfulFileIndexing() throws IOException {
        openFileModel.setFile(new File("test.txt"));
        openFileModel.setIndexType(IndexFileAction.FILE_BASED);

        indexFileAction.actionPerformed(new ActionEvent(new Object(), ActionEvent.ACTION_PERFORMED, "actionPerformed"));

        verify(fileIndexerMock).addObserver(indexFileAction);
        verify(jTailMainModel).add(new JTailModel(new File("test.txt"), new LineReader(new File("test.txt"), fileIndexer.getIndex())));
    }

    @Test(expected = FileNotFoundException.class)
    public void testUpdateWithMissingFileIndexing() throws IOException, InterruptedException {
        openFileModel.setFile(new File("test.txt"));
        openFileModel.setIndexType(IndexFileAction.FILE_BASED);

        indexFileAction.actionPerformed(new ActionEvent(new Object(), ActionEvent.ACTION_PERFORMED, "actionPerformed"));

        ExceptionHandler.assertHandledMessage(UIMessage.ERROR_FILE_NOT_FOUND, "test.txt");
        verify(jTailMainModel, never()).add(any(JTailModel.class));
    }

    @Test
    public void testUpdateWithExceptionDuringFileIndexing() throws IOException, InterruptedException {
        openFileModel.setFile(new File("test.txt"));
        openFileModel.setIndexType(IndexFileAction.FILE_BASED);

        IndexFileAction.ExceptionHandler exceptionHandler = mock(IndexFileAction.ExceptionHandler.class);

        JTailLogger.setInstance(exceptionHandler);

        indexFileAction.actionPerformed(new ActionEvent(new Object(), ActionEvent.ACTION_PERFORMED, "actionPerformed"));

        verify(exceptionHandler).handle(any(Throwable.class), eq(UIMessage.ERROR_GENERIC_MESSAGE), anyArray());
    }
}
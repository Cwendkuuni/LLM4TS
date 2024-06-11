package fr.pingtimeout.jtail.gui.action;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.Observer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import fr.pingtimeout.jtail.controller.JTailMainModel;
import fr.pingtimeout.jtail.gui.action.IndexFileAction;
import fr.pingtimeout.jtail.gui.language.ResourceBundle;
import fr.pingtimeout.jtail.gui.message.UIMessage;
import fr.pingtimeout.jtail.io.index.FileIndex;
import fr.pingtimeout.jtail.io.index.FileIndexer;
import fr.pingtimeout.jtail.io.index.RamFileIndex;
import fr.pingtimeout.jtail.io.index.RomFileIndex;
import fr.pingtimeout.jtail.io.line.LineReader;
import fr.pingtimeout.jtail.gui.model.JTailModel;
import fr.pingtimeout.jtail.gui.message.InformationHandler;
import fr.pingtimeout.jtail.gui.message.ExceptionHandler;
import fr.pingtimeout.jtail.gui.controller.OpenFileModel;

public class IndexFileActionTest {

    private FileIndexer mockedFileIndexer;
    private File mockedFile;
    private OpenFileModel mockedOpenFileModel;
    private JTailMainModel mockedJTailMainModel;
    private IndexFileAction indexFileAction;
    private Observer mockedObserver;
    private InformationHandler mockedInformationHandler;
    private ExceptionHandler mockedExceptionHandler;

    @Before
    public void setUp() {
        mockedFile = mock(File.class);
        mockedOpenFileModel = mock(OpenFileModel.class);
        mockedJTailMainModel = mock(JTailMainModel.class);
        mockedObserver = mock(Observer.class);
        mockedInformationHandler = mock(InformationHandler.class);
        mockedExceptionHandler = mock(ExceptionHandler.class);

        when(mockedOpenFileModel.getFile()).thenReturn(mockedFile);
        when(mockedOpenFileModel.getIndexType()).thenReturn(IndexType.FILE_BASED);

        mockedFileIndexer = spy(new FileIndexer(mockedFile, RomFileIndex.class));

        indexFileAction = new IndexFileAction(mockedJTailMainModel, mockedOpenFileModel);
        indexFileAction.setInformationHandler(mockedInformationHandler);
        indexFileAction.setExceptionHandler(mockedExceptionHandler);
    }

    @After
    public void tearDown() {
        indexFileAction = null;
        mockedFileIndexer = null;
        mockedOpenFileModel = null;
        mockedJTailMainModel = null;
        mockedObserver = null;
        mockedInformationHandler = null;
        mockedExceptionHandler = null;
    }

    @Test
    public void testActionPerformedWithValidFile() {
        ActionEvent mockedActionEvent = mock(ActionEvent.class);

        indexFileAction.actionPerformed(mockedActionEvent);

        verify(mockedOpenFileModel).getFile();
        verify(mockedJTailMainModel).add(argThat(model -> model instanceof JTailModel));
        verify(mockedInformationHandler).handle(isTrue(), UIMessage.INFO_FILE_LOADED, mockedFile.getName());
        verifyNoMoreInteractions(mockedExceptionHandler);
    }

    @Test
    public void testActionPerformedWithNullFile() {
        when(mockedOpenFileModel.getFile()).thenReturn(null);

        indexFileAction.actionPerformed(mock(ActionEvent.class));

        verify(mockedInformationHandler).handle(isTrue(), UIMessage.INFO_NO_FILE_SELECTED, new Object[0]);
    }

    @Test
    public void testActionPerformedWithInvalidFileType() {
        when(mockedOpenFileModel.getIndexType()).thenReturn(IndexType.MEMORY_BASED);

        indexFileAction.actionPerformed(mock(ActionEvent.class));

        verifyZeroInteractions(mockedJTailMainModel);
        verifyNoMoreInteractions(mockedInformationHandler, mockedExceptionHandler);
    }

    @Test
    public void testUpdateWithSuccessfulIndexing() throws IOException {
        FileIndexerWorker mockedFileIndexerWorker = mock(FileIndexerWorker.class);
        when(mockedFileIndexerWorker.getFileIndexer()).thenReturn(mockedFileIndexer);

        indexFileAction.update(mockedFileIndexer, 100);

        verify(mockedJTailMainModel).add(argThat(model -> model instanceof JTailModel));
        verifyNoMoreInteractions(mockedExceptionHandler);
    }

    @Test
    public void testUpdateWithFailedIndexing() throws IOException {
        when(mockedFileIndexer.getFile()).thenThrow(new FileNotFoundException("Error opening file"));

        indexFileAction.update(mockedFileIndexer, 100);

        verify(mockedExceptionHandler).handle(argThat(e -> e instanceof FileNotFoundException), UIMessage.ERROR_FILE_NOT_FOUND, mockedFile.getName());
    }

    @Test
    public void testUpdateWithException() throws IOException {
        when(mockedFileIndexer.getFile()).thenReturn(mockedFile);
        when(mockedFileIndexer.getIndex()).thenThrow(new Exception("Error indexing file"));

        indexFileAction.update(mockedFileIndexer, 100);

        verify(mockedExceptionHandler).handle(argThat(e -> e instanceof Exception), UIMessage.ERROR_GENERIC_MESSAGE, new Object[0]);
    }
}
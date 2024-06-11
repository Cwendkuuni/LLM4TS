import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import fr.pingtimeout.jtail.gui.action.IndexFileAction;
import fr.pingtimeout.jtail.gui.action.IndexFileAction.OpenFileModel;
import fr.pingtimeout.jtail.gui.controller.JTailMainModel;
import fr.pingtimeout.jtail.gui.model.JTailModel;
import fr.pingtimeout.jtail.io.FileIndexer;
import fr.pingtimeout.jtail.io.FileIndexerWorker;
import fr.pingtimeout.jtail.io.FileNotFoundException;
import fr.pingtimeout.jtail.io.LineReader;
import fr.pingtimeout.jtail.io.index.FileIndex;
import fr.pingtimeout.jtail.io.index.RamFileIndex;
import fr.pingtimeout.jtail.io.index.RomFileIndex;
import fr.pingtimeout.jtail.util.JTailLogger;
import fr.pingtimeout.jtail.gui.message.InformationHandler;
import fr.pingtimeout.jtail.gui.message.UIMessage;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.Observable;

import static org.mockito.Mockito.*;

public class IndexFileActionTest {

    private IndexFileAction indexFileAction;
    private OpenFileModel openFileModel;
    private JTailMainModel jTailMainModel;
    private InformationHandler informationHandler;
    private FileIndexer fileIndexer;
    private FileIndex index;
    private FileIndexerWorker fileIndexerWorker;

    @Before
    public void setUp() {
        openFileModel = mock(OpenFileModel.class);
        jTailMainModel = mock(JTailMainModel.class);
        informationHandler = mock(InformationHandler.class);
        index = mock(FileIndex.class);
        fileIndexer = mock(FileIndexer.class);
        when(fileIndexer.getIndex()).thenReturn(index);
        fileIndexerWorker = mock(FileIndexerWorker.class);
        indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
        indexFileAction.addObserver(informationHandler);
    }

    @Test
    public void testActionPerformed_NullFile_NoIndexType_ShowsNoFileSelectedMessage() {
        when(openFileModel.getFile()).thenReturn(null);
        when(openFileModel.getIndexType()).thenReturn(null);

        indexFileAction.actionPerformed(mock(ActionEvent.class));

        verify(informationHandler, times(1)).handle(true, UIMessage.INFO_NO_FILE_SELECTED, new Object[0]);
    }

    @Test
    public void testActionPerformed_ValidFile_MemoryBasedIndexType_CreatesRamFileIndex_AddsJTailModelToJTailMainModel() {
        File file = new File("test.txt");
        when(openFileModel.getFile()).thenReturn(file);
        when(openFileModel.getIndexType()).thenReturn(IndexFileAction.IndexType.MEMORY_BASED);

        ArgumentCaptor<FileIndexer> fileIndexerArgumentCaptor = ArgumentCaptor.forClass(FileIndexer.class);
        indexFileAction.actionPerformed(mock(ActionEvent.class));

        verify(jTailMainModel, times(1)).add(argThat(jTailModel -> jTailModel instanceof JTailModel));
        verify(fileIndexer, times(1)).addObserver(informationHandler);
        verify(fileIndexerWorker, times(1)).execute();
        verify(fileIndexer, times(1)).getIndex();
        verify(fileIndexer).setFile(file);
        verify(fileIndexer, times(1)).setIndex(eq(index));
        verify(fileIndexer, times(1)).start();
        verifyNoMoreInteractions(fileIndexer);

        ArgumentCaptor<File> fileArgumentCaptor = ArgumentCaptor.forClass(File.class);
        verify(informationHandler, times(1)).handle(eq(true), eq(UIMessage.INFO_FILE_LOADED), fileArgumentCaptor.capture());
        File loadedFile = fileArgumentCaptor.getValue();
        assertEquals(file.getAbsolutePath(), loadedFile.getAbsolutePath());
    }

    @Test
    public void testActionPerformed_ValidFile_FileBasedIndexType_CreatesRomFileIndex_AddsJTailModelToJTailMainModel() {
        File file = new File("test.txt");
        when(openFileModel.getFile()).thenReturn(file);
        when(openFileModel.getIndexType()).thenReturn(IndexFileAction.IndexType.FILE_BASED);

        ArgumentCaptor<FileIndexer> fileIndexerArgumentCaptor = ArgumentCaptor.forClass(FileIndexer.class);
        indexFileAction.actionPerformed(mock(ActionEvent.class));

        verify(jTailMainModel, times(1)).add(argThat(jTailModel -> jTailModel instanceof JTailModel));
        verify(fileIndexer, times(1)).addObserver(informationHandler);
        verify(fileIndexerWorker, times(1)).execute();
        verify(fileIndexer).setFile(file);
        verify(fileIndexer).setIndex(eq(index));
        verify(fileIndexer, times(1)).start();
        verifyNoMoreInteractions(fileIndexer);

        ArgumentCaptor<File> fileArgumentCaptor = ArgumentCaptor.forClass(File.class);
        verify(informationHandler, times(1)).handle(eq(true), eq(UIMessage.INFO_FILE_LOADED), fileArgumentCaptor.capture());
        File loadedFile = fileArgumentCaptor.getValue();
        assertEquals(file.getAbsolutePath(), loadedFile.getAbsolutePath());

        verify(fileIndexer, times(1)).close();
    }

    @Test(expected = FileNotFoundException.class)
    public void testActionPerformed_InvalidFile_ThrowsFileNotFoundException() {
        File file = new File("test.txt");
        when(openFileModel.getFile()).thenReturn(file);
        when(openFileModel.getIndexType()).thenReturn(IndexFileAction.IndexType.MEMORY_BASED);

        doThrow(new FileNotFoundException("Test error message")).when(fileIndexer).setFile(file);

        indexFileAction.actionPerformed(mock(ActionEvent.class));

        ArgumentCaptor<String> errorMessageArgumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(informationHandler, times(1)).handle(eq(true), eq(UIMessage.ERROR_FILE_NOT_FOUND), errorMessageArgumentCaptor.capture());
        String errorMessage = errorMessageArgumentCaptor.getValue();
        assertEquals("Test error message", errorMessage);
    }

    @Test(expected = IOException.class)
    public void testUpdate_ExceptionThrownDuringJTailModelCreation_ShowsErrorMessage() {
        File file = new File("test.txt");
        when(openFileModel.getFile()).thenReturn(file);
        when(openFileModel.getIndexType()).thenReturn(IndexFileAction.IndexType.MEMORY_BASED);

        when(fileIndexer.getFile()).thenReturn(file);
        when(fileIndexer.getIndex()).thenReturn(index);

        doThrow(new IOException("Test error message")).when(fileIndexer).start();

        indexFileAction.update(fileIndexer, 100);

        ArgumentCaptor<String> errorMessageArgumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(informationHandler, times(1)).handle(eq(true), eq(UIMessage.ERROR_GENERIC_MESSAGE), errorMessageArgumentCaptor.capture());
        String errorMessage = errorMessageArgumentCaptor.getValue();
        assertEquals("Test error message", errorMessage);
    }
}
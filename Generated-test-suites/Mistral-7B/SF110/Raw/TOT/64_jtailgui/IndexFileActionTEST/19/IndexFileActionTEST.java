package fr.pingtimeout.jtail.gui.action.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import org.junit.*;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.stub.Answer;

import fr.pingtimeout.jtail.gui.action.IndexFileAction;
import fr.pingtimeout.jtail.gui.controller.JTailMainModel;
import fr.pingtimeout.jtail.gui.model.JTailModel;
import fr.pingtimeout.jtail.gui.message.UIMessage;
import fr.pingtimeout.jtail.io.FileIndexer;
import fr.pingtimeout.jtail.io.FileIndexerWorker;
import fr.pingtimeout.jtail.io.index.FileIndex;
import fr.pingtimeout.jtail.io.index.RamFileIndex;
import fr.pingtimeout.jtail.io.index.RomFileIndex;
import fr.pingtimeout.jtail.util.JTailLogger;

public class IndexFileActionTest {

    private JTailMainModel jTailMainModel;
    private OpenFileModel openFileModel;
    private IndexFileAction indexFileAction;
    private JDialog dialog;

    @Before
    public void setUp() {
        jTailMainModel = Mockito.mock(JTailMainModel.class);
        openFileModel = new OpenFileModel();
        indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
        dialog = Mockito.mock(JDialog.class);
    }

    @Test
    public void testActionPerformedWithNullFile() {
        indexFileAction.actionPerformed(new ActionEvent(dialog, 0, ""));
        verify(jTailMainModel, never()).add(any(JTailModel.class));
        verify(InformationHandler::handle, times(1)).with(true, UIMessage.INFO_NO_FILE_SELECTED, new Object[0]);
    }

    @Test
    public void testActionPerformedWithValidFile() throws IOException {
        File testFile = new File("test.txt");
        openFileModel.setFile(testFile);
        when(testFile.exists()).thenReturn(true);
        when(dialog.isVisible()).thenReturn(false);
        indexFileAction.actionPerformed(new ActionEvent(dialog, 0, ""));
        ArgumentCaptor<File> fileArgumentCaptor = ArgumentCaptor.forClass(File.class);
        verify(FileIndexer::new).times(1).with(fileArgumentCaptor.capture(), eq(RamFileIndex.class));
        FileIndex index = fileArgumentCaptor.getValue();
        verify(index).addObserver(indexFileAction);
        FileIndexerWorker worker = new FileIndexerWorker(index);
        worker.execute();
        verify(jTailMainModel, times(1)).add(any(JTailModel.class));
    }

    @Test(expected = FileNotFoundException.class)
    public void testActionPerformedWithInvalidFile() throws IOException {
        File testFile = new File("test.txt");
        openFileModel.setFile(testFile);
        when(testFile.exists()).thenReturn(false);
        indexFileAction.actionPerformed(new ActionEvent(dialog, 0, ""));
    }

    @Test
    public void testUpdateWithSuccess() {
        File testFile = new File("test.txt");
        openFileModel.setFile(testFile);
        when(testFile.exists()).thenReturn(true);
        when(dialog.isVisible()).thenReturn(false);

        FileIndex index = Mockito.mock(FileIndex.class);
        when(index.getFile()).thenReturn(testFile);
        when(index.getIndex()).thenReturn(new Object[0]);

        FileIndexer fileIndexer = Mockito.mock(FileIndexer.class);
        when(fileIndexer.getFile()).thenReturn(testFile);
        when(fileIndexer.getIndex()).thenReturn(index);

        when(jTailMainModel.getFileIndexers()).thenReturn(new ArrayList<>());

        indexFileAction.update(Mockito.eq(index), Mockito.anyInt());

        ArgumentCaptor<FileIndexer> fileIndexerArgumentCaptor = ArgumentCaptor.forClass(FileIndexer.class);
        ArgumentCaptor<File> fileArgumentCaptor = ArgumentCaptor.forClass(File.class);
        ArgumentCaptor<LineReader> lineReaderArgumentCaptor = ArgumentCaptor.forClass(LineReader.class);

        verify(jTailMainModel).add(eq(Mockito.instanceOf(JTailModel.class)));

        verify(FileIndexerWorker::new).times(1).with(eq(fileIndexer));

        FileIndexerWorker worker = fileIndexerArgumentCaptor.getValue();
        worker.execute();

        verify(jTailMainModel, times(1)).add(any(JTailModel.class));
    }

    @Test(expected = FileNotFoundException.class)
    public void testUpdateWithFileNotFound() {
        File testFile = new File("test.txt");
        openFileModel.setFile(testFile);
        when(testFile.exists()).thenReturn(false);

        FileIndex index = Mockito.mock(FileIndex.class);
        when(index.getFile()).thenReturn(testFile);
        when(index.getIndex()).thenReturn(new Object[0]);

        indexFileAction.update(Mockito.eq(index), Mockito.anyInt());
    }

    @Test(expected = Exception.class)
    public void testUpdateWithException() {
        File testFile = new File("test.txt");
        openFileModel.setFile(testFile);
        when(testFile.exists()).thenReturn(true);
        when(dialog.isVisible()).thenReturn(false);

        FileIndex index = Mockito.mock(FileIndex.class);
        when(index.getFile()).thenReturn(testFile);
        when(index.getIndex()).thenThrow(new Exception());

        indexFileAction.update(Mockito.eq(index), Mockito.anyInt());
    }

    @After
    public void tearDown() {
        JTailLogger.reset();
    }
}
package fr.pingtimeout.jtail.gui.action;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import fr.pingtimeout.jtail.gui.action.IndexFileAction;
import fr.pingtimeout.jtail.gui.controller.JTailMainModel;
import fr.pingtimeout.jtail.gui.message.UIMessage;
import fr.pingtimeout.jtail.io.index.FileIndexer;
import fr.pingtimeout.jtail.io.index.FileIndexerWorker;
import fr.pingtimeout.jtail.io.index.RomFileIndex;
import fr.pingtimeout.jtail.io.index.RamFileIndex;
import fr.pingtimeout.jtail.gui.model.JTailModel;
import fr.pingtimeout.jtail.io.LineReader;
import fr.pingtimeout.jtail.io.FileNotFoundException;

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Observable;
import java.util.Observer;

public class IndexFileActionTest {

    @Test
    public void testIndexFileActionConstruction() {
        JTailMainModel jTailMainModelMock = mock(JTailMainModel.class);
        OpenFileModel openFileModelMock = mock(OpenFileModel.class);

        IndexFileAction indexFileAction = new IndexFileAction(jTailMainModelMock, openFileModelMock);

        assertEquals("action.open.label", indexFileAction.getText());
        assertEquals("action.open.tooltip", indexFileAction.getToolTipText());
        assertEquals("action.open.mnemonic", indexFileAction.getMnemonic());
        assertEquals("action.open.accelerator", indexFileAction.getAcceleratorKey());
        assertEquals(IndexFileAction.bundle.getString("action.open.label"), indexFileAction.getValue(Action.NAME));
        assertEquals(IndexFileAction.OPEN_ICON, indexFileAction.getValue(Action.ICON_KEY));
    }

    @Test
    public void testActionPerformedWithNullFile() {
        JTailMainModel jTailMainModelMock = mock(JTailMainModel.class);
        OpenFileModel openFileModelMock = mock(OpenFileModel.class);

        IndexFileAction indexFileAction = new IndexFileAction(jTailMainModelMock, openFileModelMock);

        indexFileAction.actionPerformed(new ActionEvent(new JButton(), 0, "Test"));

        verify(jTailMainModelMock, times(0)).add(any(JTailModel.class));
    }

    @Test
    public void testActionPerformedWithValidFileAndIndexType() {
        JTailMainModel jTailMainModelMock = mock(JTailMainModel.class);
        OpenFileModel openFileModelMock = mock(OpenFileModel.class);

        File file = new File("testFile.txt");
        when(openFileModelMock.getFile()).thenReturn(file);
        when(openFileModelMock.getIndexType()).thenReturn(IndexFileAction.IndexType.FILE_BASED);

        IndexFileAction indexFileAction = new IndexFileAction(jTailMainModelMock, openFileModelMock);

        indexFileAction.actionPerformed(new ActionEvent(new JButton(), 0, "Test"));

        verify(jTailMainModelMock, times(1)).add(any(JTailModel.class));
        verify(openFileModelMock, times(1)).getFile();
        verify(openFileModelMock, times(1)).getIndexType();

        FileIndexer fileIndexerMock = mock(FileIndexer.class);
        when(fileIndexer.getFile()).thenReturn(file);
        when(fileIndexer.getIndex()).thenReturn(new byte[0][0]);

        FileIndexerWorker fileIndexerWorkerMock = mock(FileIndexerWorker.class);
        when(fileIndexerWorkerMock.execute()).thenReturn(true);

        doReturn(fileIndexerMock).when(jTailMainModelMock).createFileIndexer(eq(file), eq(RomFileIndex.class));
        doReturn(fileIndexerWorkerMock).when(jTailMainModelMock).createFileIndexerWorker(eq(fileIndexerMock));

        LineReader lineReaderMock = mock(LineReader.class);
        JTailModel jTailModelMock = mock(JTailModel.class);

        try {
            when(fileIndexerMock.getIndex()).thenReturn(new byte[0][0]);
            when(fileIndexerMock.indexFile(any(File.class), any(LineReader.class), anyInt(), anyInt())).thenReturn(100);
            when(fileIndexerWorkerMock.getFileIndexer()).thenReturn(fileIndexerMock);
            when(fileIndexerMock.getFile()).thenReturn(file);
            when(lineReaderMock.readLine()).thenReturn("Test Line");
            when(lineReaderMock.hasNextLine()).thenReturn(true);
            when(lineReaderMock.nextLine()).thenAnswer((Answer<String>) invocationOnMock -> lineReaderMock.readLine());
            when(lineReaderMock.close()).thenReturn(true);

            when(jTailMainModelMock.createLineReader(any(File.class), any(FileIndexer.class))).thenReturn(lineReaderMock);
            when(jTailModelMock.getFile()).thenReturn(file);

            indexFileAction.actionPerformed(new ActionEvent(new JButton(), 0, "Test"));

            verify(jTailMainModelMock, times(1)).add(eq(jTailModelMock));
            verify(fileIndexerMock, times(1)).indexFile(any(File.class), any(LineReader.class), anyInt(), anyInt());
            verify(jTailMainModelMock, times(1)).add(eq(jTailModelMock));
            verify(fileIndexerMock, times(1)).getFile();
            verify(fileIndexerWorkerMock, times(1)).execute();
        } catch (FileNotFoundException e) {
            fail("FileNotFoundException should not be thrown");
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testActionPerformedWithValidFileAndInvalidIndexType() {
        JTailMainModel jTailMainModelMock = mock(JTailMainModel.class);
        OpenFileModel openFileModelMock = mock(OpenFileModel.class);

        File file = new File("testFile.txt");
        when(openFileModelMock.getFile()).thenReturn(file);
        when(openFileModelMock.getIndexType()).thenReturn(IndexFileAction.IndexType.MEMORY_BASED);

        IndexFileAction indexFileAction = new IndexFileAction(jTailMainModelMock, openFileModelMock);

        indexFileAction.actionPerformed(new ActionEvent(new JButton(), 0, "Test"));

        verify(jTailMainModelMock, times(0)).add(any(JTailModel.class));
        verify(openFileModelMock, times(1)).getFile();
        verify(openFileModelMock, times(1)).getIndexType();

        verifyNoMoreInteractions(jTailMainModelMock);
    }

    @Test
    public void testUpdateWithFileIndexerCreationSuccess() {
        JTailMainModel jTailMainModelMock = mock(JTailMainModel.class);
        OpenFileModel openFileModelMock = mock(OpenFileModel.class);

        File file = new File("testFile.txt");
        when(openFileModelMock.getFile()).thenReturn(file);
        when(openFileModelMock.getIndexType()).thenReturn(IndexFileAction.IndexType.FILE_BASED);

        IndexFileAction indexFileAction = new IndexFileAction(jTailMainModelMock, openFileModelMock);

        FileIndexer fileIndexerMock = mock(FileIndexer.class);
        when(fileIndexer.getFile()).thenReturn(file);
        when(fileIndexer.getIndex()).thenReturn(new byte[0][0]);

        IndexFileAction.Observer mockObserver = mock(IndexFileAction.Observer.class);

        doReturn(fileIndexerMock).when(jTailMainModelMock).createFileIndexer(eq(file), eq(RomFileIndex.class));
        doReturn(mockObserver).when(jTailMainModelMock).createFileIndexObserver();

        indexFileAction.update(jTailMainModelMock, 100);

        verify(jTailMainModelMock, times(1)).addObserver(eq(mockObserver));
        verify(jTailMainModelMock, times(1)).createFileIndexer(any(File.class), any(Class.class));
        verify(fileIndexerMock, times(1)).addObserver(eq(mockObserver));
    }

    @Test
    public void testUpdateWithFileIndexerCreationFailure() {
        JTailMainModel jTailMainModelMock = mock(JTailMainModel.class);
        OpenFileModel openFileModelMock = mock(OpenFileModel.class);

        File file = new File("testFile.txt");
        when(openFileModelMock.getFile()).thenReturn(file);
        when(openFileModelMock.getIndexType()).thenReturn(IndexFileAction.IndexType.FILE_BASED);

        IndexFileAction indexFileAction = new IndexFileAction(jTailMainModelMock, openFileModelMock);

        FileIndexer fileIndexerMock = mock(FileIndexer.class);

        doThrow(new FileNotFoundException("File not found"))
                .when(jTailMainModelMock)
                .createFileIndexer(eq(file), eq(RomFileIndex.class));

        indexFileAction.update(jTailMainModelMock, 100);

        verify(jTailMainModelMock, times(1)).handle(anyBoolean(), eq(UIMessage.INFO_NO_FILE_SELECTED), any());
        verify(jTailMainModelMock, never()).createFileIndexObserver();
    }

    @Test
    public void testUpdateWithFileIndexingSuccess() {
        JTailMainModel jTailMainModelMock = mock(JTailMainModel.class);
        OpenFileModel openFileModelMock = mock(OpenFileModel.class);

        File file = new File("testFile.txt");
        when(openFileModelMock.getFile()).thenReturn(file);
        when(openFileModelMock.getIndexType()).thenReturn(IndexFileAction.IndexType.FILE_BASED);

        IndexFileAction indexFileAction = new IndexFileAction(jTailMainModelMock, openFileModelMock);

        FileIndexer fileIndexerMock = mock(FileIndexer.class);
        when(fileIndexer.getFile()).thenReturn(file);
        when(fileIndexer.getIndex()).thenReturn(new byte[0][0]);

        LineReader lineReaderMock = mock(LineReader.class);
        JTailModel jTailModelMock = mock(JTailModel.class);

        when(jTailMainModelMock.createFileIndexer(any(File.class), any(Class.class))).thenReturn(fileIndexerMock);
        when(fileIndexerMock.indexFile(any(File.class), any(LineReader.class), anyInt(), anyInt())).thenReturn(100);
        when(jTailMainModelMock.createLineReader(any(File.class), any(FileIndexer.class))).thenReturn(lineReaderMock);
        when(jTailMainModelMock.createJTailModel(any(File.class), any(LineReader.class))).thenReturn(jTailModelMock);

        indexFileAction.update(jTailMainModelMock, 100);

        verify(jTailMainModelMock, times(1)).add(eq(jTailModelMock));
        verify(fileIndexerMock, times(1)).indexFile(any(File.class), any(LineReader.class), anyInt(), anyInt());
        verify(jTailMainModelMock, times(1)).add(eq(jTailModelMock));
    }

    @Test
    public void testUpdateWithFileIndexingFailure() {
        JTailMainModel jTailMainModelMock = mock(JTailMainModel.class);
        OpenFileModel openFileModelMock = mock(OpenFileModel.class);

        File file = new File("testFile.txt");
        when(openFileModelMock.getFile()).thenReturn(file);
        when(openFileModelMock.getIndexType()).thenReturn(IndexFileAction.IndexType.FILE_BASED);

        IndexFileAction indexFileAction = new IndexFileAction(jTailMainModelMock, openFileModelMock);

        FileIndexer fileIndexerMock = mock(FileIndexer.class);
        when(fileIndexer.getFile()).thenReturn(file);
        when(fileIndexer.getIndex()).thenReturn(new byte[0][0]);

        LineReader lineReaderMock = mock(LineReader.class);
        JTailModel jTailModelMock = mock(JTailModel.class);

        when(jTailMainModelMock.createFileIndexer(any(File.class), any(Class.class))).thenReturn(fileIndexerMock);
        doThrow(new Exception("Generic Exception")).when(fileIndexerMock).indexFile(any(File.class), any(LineReader.class), anyInt(), anyInt());

        indexFileAction.update(jTailMainModelMock, 100);

        verify(jTailMainModelMock, times(1)).handle(anyBoolean(), eq(UIMessage.ERROR_GENERIC_MESSAGE), any());
        verify(jTailMainModelMock, never()).add(eq(jTailModelMock));
    }
}
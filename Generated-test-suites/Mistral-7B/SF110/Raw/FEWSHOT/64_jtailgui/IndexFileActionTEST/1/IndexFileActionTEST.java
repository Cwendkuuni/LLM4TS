package fr.pingtimeout.jtail.gui.action;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.event.*;
import java.io.*;
import fr.pingtimeout.jtail.gui.message.*;
import fr.pingtimeout.jtail.gui.controller.*;
import fr.pingtimeout.jtail.io.index.*;
import java.util.*;
import fr.pingtimeout.jtail.io.*;
import fr.pingtimeout.jtail.gui.model.*;

public class IndexFileActionTest {

    private JTailMainModel jTailMainModelMock;
    private OpenFileModel openFileModelMock;
    private IndexFileAction indexFileAction;

    @Before
    public void setUp() {
        jTailMainModelMock = mock(JTailMainModel.class);
        openFileModelMock = mock(OpenFileModel.class);
        indexFileAction = new IndexFileAction(jTailMainModelMock, openFileModelMock);
    }

    @Test
    public void testActionPerformedWithNullFileAndNullIndexType() {
        indexFileAction.actionPerformed(mock(ActionEvent.class));
        verify(jTailMainModelMock).add(null);
        verify(openFileModelMock, never()).getFile();
        verify(openFileModelMock, never()).getIndexType();
    }

    @Test
    public void testActionPerformedWithNullFileAndValidIndexType() {
        when(openFileModelMock.getIndexType()).thenReturn(FileIndexType.FILE_BASED);
        indexFileAction.actionPerformed(mock(ActionEvent.class));
        verify(jTailMainModelMock).add(null);
        verify(openFileModelMock).getFile();
        verify(openFileModelMock).getIndexType();
    }

    @Test
    public void testActionPerformedWithValidFileAndNullIndexType() {
        File file = mock(File.class);
        when(openFileModelMock.getFile()).thenReturn(file);
        when(openFileModelMock.getIndexType()).thenReturn(null);

        doThrow(new FileNotFoundException("Test error message"))
                .when(fileIndexerMock)
                .init(file, RamFileIndex.class);

        indexFileAction.actionPerformed(mock(ActionEvent.class));

        verify(jTailMainModelMock, never()).add(any(JTailModel.class));
        verify(openFileModelMock).getFile();
        verify(openFileModelMock).getIndexType();
        verify(jTailMainModelMock).add(null);
        verify(ExceptionHandler.class).handle(any(FileNotFoundException.class), eq(UIMessage.ERROR_FILE_NOT_FOUND), eq(file.getName()));
    }

    @Test
    public void testActionPerformedWithValidFileAndValidIndexType() throws FileNotFoundException, IOException {
        File file = mock(File.class);
        FileIndex index = mock(FileIndex.class);
        when(openFileModelMock.getFile()).thenReturn(file);
        when(openFileModelMock.getIndexType()).thenReturn(FileIndexType.MEMORY_BASED);
        FileIndexer fileIndexerMock = mock(FileIndexer.class);

        doReturn(fileIndexerMock).when(fileIndexerFactory).createFileIndexer(file, RamFileIndex.class);

        indexFileAction.actionPerformed(mock(ActionEvent.class));

        verify(jTailMainModelMock).add(any(JTailModel.class));
        verify(openFileModelMock).getFile();
        verify(openFileModelMock).getIndexType();
        verify(fileIndexerMock).addObserver(eq(indexFileAction));

        LineReader lineReaderMock = mock(LineReader.class);
        JTailModel jTailModelMock = mock(JTailModel.class);

        doReturn(lineReaderMock).when(fileIndexerMock).getLineReader();
        doReturn(jTailModelMock).when(new JTailModelFactory()).create(file, lineReaderMock);

        verify(jTailModelMock).setFile(file);
        verify(jTailModelMock).setLineReader(lineReaderMock);
        verify(jTailMainModelMock).add(jTailModelMock);
    }

    @Test
    public void testUpdateWithNullObserver() {
        indexFileAction.update(this, null);
        verifyNoMoreInteractions(jTailMainModelMock);
    }

    @Test
    public void testUpdateWithValidObserver() {
        when(openFileModelMock.getFile()).thenReturn(new File("testFile.index"));
        indexFileAction.update(this, 100);

        verify(jTailMainModelMock).add(any(JTailModel.class));
    }

}
package fr.pingtimeout.jtail.gui.action;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import fr.pingtimeout.jtail.gui.action.IndexFileAction;
import fr.pingtimeout.jtail.gui.controller.JTailMainModel;
import fr.pingtimeout.jtail.gui.model.FileIndexerWorker;
import fr.pingtimeout.jtail.gui.model.JTailModel;
import fr.pingtimeout.jtail.gui.message.UIMessage;
import fr.pingtimeout.jtail.io.FileIndexer;
import fr.pingtimeout.jtail.io.FileIndexerObserver;
import fr.pingtimeout.jtail.io.index.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

public class IndexFileActionTest {

    @Test
    public void testActionPerformedWithNullFile() {
        JTailMainModel jTailMainModelMock = mock(JTailMainModel.class);
        OpenFileModel openFileModel = new OpenFileModel(null, FileIndexType.FILE_BASED);
        IndexFileAction indexFileAction = new IndexFileAction(jTailMainModelMock, openFileModel);
        ActionEvent actionEventMock = mock(ActionEvent.class);

        indexFileAction.actionPerformed(actionEventMock);

        verify(jTailMainModelMock).handle(true, UIMessage.INFO_NO_FILE_SELECTED, new Object[0]);
    }

    @Test
    public void testActionPerformedWithValidFile() throws Exception {
        JTailMainModel jTailMainModelMock = mock(JTailMainModel.class);
        OpenFileModel openFileModel = new OpenFileModel(new File("testFile.txt"), FileIndexType.MEMORY_BASED);
        IndexFileAction indexFileAction = new IndexFileAction(jTailMainModelMock, openFileModel);
        ActionEvent actionEventMock = mock(ActionEvent.class);

        Component dialogMock = mock(Component.class);
        when(actionEventMock.getSource()).thenReturn(dialogMock);

        Class<? extends FileIndex> indexerClass = RamFileIndex.class;
        FileIndexer fileIndexer = mock(FileIndexer.class);
        when(fileIndexer.getFile()).thenReturn(openFileModel.getFile());

        FileIndexerWorker fileIndexerWorker = new FileIndexerWorker(fileIndexer);
        doReturn(fileIndexerWorker).when(indexFileAction).new FileIndexerWorker(fileIndexer);

        indexFileAction.actionPerformed(actionEventMock);

        verify(jTailMainModelMock).add(new JTailModel(openFileModel.getFile(), new LineReader(openFileModel.getFile(), fileIndexer.getIndex())));
        verify(fileIndexerWorker).execute();
    }

    @Test
    public void testUpdateWithFileNotFoundException() {
        JTailMainModel jTailMainModelMock = mock(JTailMainModel.class);
        IndexFileAction indexFileAction = new IndexFileAction(jTailMainModelMock, null);
        Observer observerMock = mock(Observer.class);
        Observable observableMock = mock(Observable.class);
        FileIndexer fileIndexer = mock(FileIndexer.class);
        FileIndexerObserver fileIndexerObserverMock = mock(FileIndexerObserver.class);
        when(fileIndexer.addObserver(observerMock)).thenReturn(this);
        when(observableMock).thenReturn(fileIndexer);

        doThrow(new FileNotFoundException("File not found")).when(fileIndexer).getFile();

        indexFileAction.update(observableMock, 100);

        verify(jTailMainModelMock).handle(true, UIMessage.ERROR_FILE_NOT_FOUND, "File not found");
    }

    @Test
    public void testUpdateWithGenericException() {
        JTailMainModel jTailMainModelMock = mock(JTailMainModel.class);
        IndexFileAction indexFileAction = new IndexFileAction(jTailMainModelMock, null);
        Observer observerMock = mock(Observer.class);
        Observable observableMock = mock(Observable.class);
        FileIndexer fileIndexer = mock(FileIndexer.class);
        FileIndexerObserver fileIndexerObserverMock = mock(FileIndexerObserver.class);
        when(fileIndexer.addObserver(observerMock)).thenReturn(this);
        when(observableMock).thenReturn(fileIndexer);

        Exception exception = new IOException("An error occurred");

        doThrow(exception).when(fileIndexer).getFile();

        indexFileAction.update(observableMock, 100);

        verify(jTailMainModelMock).handle(true, UIMessage.ERROR_GENERIC_MESSAGE, new Object[0]);
    }
}
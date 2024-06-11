package fr.pingtimeout.jtail.gui.action;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import fr.pingtimeout.jtail.gui.action.IndexFileAction;
import fr.pingtimeout.jtail.gui.controller.JTailMainModel;
import fr.pingtimeout.jtail.gui.model.FileIndexerWorker;
import fr.pingtimeout.jtail.gui.model.JTailModel;
import fr.pingtimeout.jtail.io.index.FileIndexer;
import fr.pingtimeout.jtail.io.FileIndexerObserver;
import fr.pingtimeout.jtail.io.index.FileIndexerWorker;
import fr.pingtimeout.jtail.io.LineReader;
import fr.pingtimeout.jtail.io.index.RamFileIndex;
import fr.pingtimeout.jtail.io.index.RomFileIndex;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

public class IndexFileActionTest {

    @org.junit.Before
    public void setUp() {
        JTailMainModel jTailMainModelMock = mock(JTailMainModel.class);
        OpenFileModel openFileModel = new OpenFileModel();
        IndexFileAction indexFileAction = new IndexFileAction(jTailMainModelMock, openFileModel);
    }

    @org.junit.Test
    public void testActionPerformed_NullFile() {
        ActionEvent actionEventMock = mock(ActionEvent.class);
        IndexFileAction indexFileAction = new IndexFileAction(mock(JTailMainModel.class), new OpenFileModel());
        indexFileAction.actionPerformed(actionEventMock);
        verify(InformationHandler::handle, times(1)).handle(true, UIMessage.INFO_NO_FILE_SELECTED, new Object[0]);
    }

    @org.junit.Test
    public void testActionPerformed_ValidFile() throws IOException {
        ActionEvent actionEventMock = mock(ActionEvent.class);
        Component dialogMock = mock(Component.class);
        IndexFileAction indexFileAction = new IndexFileAction(mock(JTailMainModel.class), new OpenFileModel("validFile.txt"));
        doReturn(dialogMock).when(actionEventMock).getSource();
        doReturn(RamFileIndex.class).when(indexFileAction).getIndexerClass(OpenFileModel.FILE_BASED);

        FileIndexer fileIndexer = mock(FileIndexer.class);
        FileIndexerWorker fileIndexerWorker = new FileIndexerWorker(fileIndexer);
        doReturn(fileIndexerWorker).when(indexFileAction).new FileIndexerWorker(fileIndexer);
        doReturn(true).when(fileIndexer).open();

        indexFileAction.actionPerformed(actionEventMock);

        verify(JTailLogger::debug, times(2)).debug("File : {}", eq("validFile.txt"));
        verify(JTailLogger::debug, times(1)).debug("Index type : FILE_BASED");
        verify(dialogMock, times(1)).setVisible(eq(false));
        verify(fileIndexer, times(1)).addObserver(eq(indexFileAction));
        verify(fileIndexerWorker, times(1)).execute();

        JTailModel jTailModel = new JTailModel("validFile.txt", new LineReader("validFile.txt", fileIndexer.getIndex()));
        verify(jTailMainModelMock, times(1)).add(eq(jTailModel));
    }

    @org.junit.Test
    public void testActionPerformed_InvalidFile() throws IOException {
        ActionEvent actionEventMock = mock(ActionEvent.class);
        Component dialogMock = mock(Component.class);
        IndexFileAction indexFileAction = new IndexFileAction(mock(JTailMainModel.class), new OpenFileModel("invalidFile.txt"));
        doReturn(dialogMock).when(actionEventMock).getSource();

        FileIndexer fileIndexer = mock(FileIndexer.class);
        FileIndexerWorker fileIndexerWorker = new FileIndexerWorker(fileIndexer);
        doReturn(fileIndexerWorker).when(indexFileAction).new FileIndexerWorker(fileIndexer);
        doThrow(new FileNotFoundException("Invalid File")).when(fileIndexer).open();

        indexFileAction.actionPerformed(actionEventMock);

        verify(ExceptionHandler::handle, times(1)).handle(eq(new FileNotFoundException("Invalid File")), eq(UIMessage.ERROR_FILE_NOT_FOUND), eq("invalidFile.txt"));
    }

    @org.junit.Test
    public void testUpdate_Success() {
        Observable observableMock = mock(Observable.class);
        Observer observerMock = mock(Observer.class);
        IndexFileAction indexFileAction = new IndexFileAction(mock(JTailMainModel.class), new OpenFileModel("validFile.txt"));

        FileIndexer fileIndexer = mock(FileIndexer.class);
        doReturn(fileIndexer).when(indexFileAction).getFileIndexer();
        doReturn(new File("validFile.txt")).when(fileIndexer).getFile();
        doReturn(new LineReader(new File("validFile.txt"), fileIndexer.getIndex())).when(fileIndexer).getLineReader();

        indexFileAction.update(observableMock, 100);

        verify(JTailLogger::debug, times(1)).debug("Notification received : 100 emitted by {}", observableMock);
        verify(jTailMainModelMock, times(1)).add(new JTailModel(new File("validFile.txt"), new LineReader("validFile.txt", fileIndexer.getIndex())));
    }

    @org.junit.Test
    public void testUpdate_Failure() {
        Observable observableMock = mock(Observable.class);
        Observer observerMock = mock(Observer.class);
        IndexFileAction indexFileAction = new IndexFileAction(mock(JTailMainModel.class), new OpenFileModel("validFile.txt"));

        FileIndexer fileIndexer = mock(FileIndexer.class);
        doReturn(fileIndexer).when(indexFileAction).getFileIndexer();
        doThrow(new IOException("IOException")).when(fileIndexer).open();

        indexFileAction.update(observableMock, 100);

        verify(ExceptionHandler::handle, times(1)).handle(eq(new IOException("IOException")), eq(UIMessage.ERROR_GENERIC_MESSAGE), new Object[0]);
    }

    public static class OpenFileModel {
        private File file;
        private int indexType;

        public OpenFileModel() {
            this.file = null;
            this.indexType = -1;
        }

        public OpenFileModel(String fileName) {
            this.file = new File(fileName);
            this.indexType = 0;
        }

        public File getFile() {
            return this.file;
        }

        public int getIndexType() {
            return this.indexType;
        }

        public void setFile(File file) {
            this.file = file;
        }

        public void setIndexType(int indexType) {
            this.indexType = indexType;
        }

        public Class<? extends FileIndex> getIndexerClass(int indexType) {
            switch (indexType) {
                case 0:
                    return RamFileIndex.class;
                case 1:
                    return RomFileIndex.class;
                default:
                    throw new IllegalArgumentException("Unknown index type: " + indexType);
            }
        }
    }
}
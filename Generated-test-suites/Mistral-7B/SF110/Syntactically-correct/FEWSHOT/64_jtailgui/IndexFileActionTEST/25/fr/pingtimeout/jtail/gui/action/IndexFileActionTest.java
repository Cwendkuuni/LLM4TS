package fr.pingtimeout.jtail.gui.action;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import fr.pingtimeout.jtail.controller.JTailMainModel;
import fr.pingtimeout.jtail.io.FileIndexer;
import fr.pingtimeout.jtail.io.FileIndexerWorker;
import fr.pingtimeout.jtail.io.FileIndexer.IndexType;
import fr.pingtimeout.jtail.io.index.RamFileIndex;
import fr.pingtimeout.jtail.io.index.RomFileIndex;
import fr.pingtimeout.jtail.gui.message.InformationHandler;
import fr.pingtimeout.jtail.gui.message.UIMessage;
import fr.pingtimeout.jtail.gui.model.JTailModel;
import fr.pingtimeout.jtail.io.LineReader;

public class IndexFileActionTest {

    private IndexFileAction indexFileAction;
    private JTailMainModel jTailMainModel;
    private OpenFileModel openFileModel;
    private FileIndexer fileIndexer;
    private FileIndexerWorker fileIndexerWorker;
    private InformationHandler informationHandler;

    @Before
    public void setUp() {
        jTailMainModel = mock(JTailMainModel.class);
        openFileModel = new OpenFileModel(null, IndexType.FILE_BASED);
        indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
        informationHandler = mock(InformationHandler.class);
    }

    @Test
    public void testActionPerformedWithNullFile() {
        ActionEvent actionEvent = mock(ActionEvent.class);
        indexFileAction.actionPerformed(actionEvent);
        verify(informationHandler, times(1)).handle(true, UIMessage.INFO_NO_FILE_SELECTED, new Object[0]);
    }

    @Test
    public void testActionPerformedWithValidFileAndIndexType() throws IOException {
        File file = new File("test.txt");
        openFileModel.setFile(file);

        ActionEvent actionEvent = mock(ActionEvent.class);

        when(jTailMainModel.add(any(JTailModel.class))).thenReturn(true);

        Class<? extends FileIndex> indexerClass = null;
        switch (openFileModel.getIndexType()) {
            case MEMORY_BASED: {
                indexerClass = RamFileIndex.class;
                break;
            }
            case FILE_BASED: {
                indexerClass = RomFileIndex.class;
                break;
            }
        }

        fileIndexer = mock(FileIndexer.class);
        when(fileIndexer.getFile()).thenReturn(file);
        when(fileIndexer.addObserver(any())).thenReturn(fileIndexer);

        when(fileIndexer.getIndex()).thenReturn(new byte[0][0]);

        when(new FileIndexer(file, indexerClass).getClass()).thenReturn(FileIndexer.class);

        fileIndexerWorker = new FileIndexerWorker(fileIndexer);
        fileIndexerWorker.execute();
        verify(jTailMainModel, times(1)).add(any(JTailModel.class));
    }

    @Test
    public void testUpdateWithFileNotFoundException() {
        FileIndexer fileIndexer = mock(FileIndexer.class);
        when(fileIndexer.getFile()).thenReturn(new File("test.txt"));
        when(fileIndexer.getIndex()).thenThrow(new FileNotFoundException());

        indexFileAction.update(fileIndexer, 100);
        verify(informationHandler, times(1)).handle(true, UIMessage.ERROR_FILE_NOT_FOUND, new Object[]{"test.txt"});
    }

    @Test
    public void testUpdateWithGenericException() {
        FileIndexer fileIndexer = mock(FileIndexer.class);
        when(fileIndexer.getFile()).thenReturn(new File("test.txt"));
        when(fileIndexer.getIndex()).thenThrow(new Exception());

        indexFileAction.update(fileIndexer, 100);
        verify(informationHandler, times(1)).handle(true, UIMessage.ERROR_GENERIC_MESSAGE, new Object[0]);
    }
}
package fr.pingtimeout.jtail.gui.action;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import fr.pingtimeout.jtail.gui.controller.JTailMainModel;
import fr.pingtimeout.jtail.gui.message.InformationHandler;
import fr.pingtimeout.jtail.gui.message.UIMessage;
import fr.pingtimeout.jtail.gui.model.JTailModel;
import fr.pingtimeout.jtail.io.index.FileIndex;
import fr.pingtimeout.jtail.io.index.FileIndexer;
import fr.pingtimeout.jtail.io.index.RamFileIndex;
import fr.pingtimeout.jtail.io.index.RomFileIndex;

public class IndexFileActionTest {

    private IndexFileAction indexFileAction;
    private JTailMainModel jTailMainModel;
    private OpenFileModel openFileModel;
    private InformationHandler informationHandler;

    @Before
    public void setUp() {
        jTailMainModel = mock(JTailMainModel.class);
        openFileModel = mock(OpenFileModel.class);
        informationHandler = mock(InformationHandler.class);

        indexFileAction = new IndexFileAction(jTailMainModel, openFileModel) {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                indexFileAction.super.actionPerformed(actionEvent);
            }
        };
        indexFileAction.addObserver(mock(Observer.class));
    }

    @Test
    public void testActionPerformedNullFile() {
        when(openFileModel.getFile()).thenReturn(null);
        indexFileAction.actionPerformed(mock(ActionEvent.class));
        verify(informationHandler).handle(true, UIMessage.INFO_NO_FILE_SELECTED, new Object[0]);
    }

    @Test
    public void testActionPerformedValidFileAndIndexType() {
        when(openFileModel.getFile()).thenReturn(new File("testFile.txt"));
        when(openFileModel.getIndexType()).thenReturn(IndexFileAction.IndexType.MEMORY_BASED);
        FileIndexer fileIndexer = mock(FileIndexer.class);
        when(fileIndexer.getFile()).thenReturn(new File("testFile.txt"));
        when(fileIndexer.addObserver(any(Observer.class))).thenReturn(fileIndexer);

        indexFileAction.actionPerformed(mock(ActionEvent.class));
        verify(jTailMainModel).add(isA(JTailModel.class));
        verify(fileIndexer).addObserver(indexFileAction);
    }

    @Test
    public void testActionPerformedInvalidIndexType() {
        when(openFileModel.getFile()).thenReturn(new File("testFile.txt"));
        when(openFileModel.getIndexType()).thenReturn(IndexFileType.FILE_BASED);

        try {
            indexFileAction.actionPerformed(mock(ActionEvent.class));
            fail();
        } catch (AssertionError e) {
            // expected
        }
    }

    @Test
    public void testUpdate() {
        FileIndexer fileIndexer = mock(FileIndexer.class);
        when(fileIndexer.getFile()).thenReturn(new File("testFile.txt"));

        indexFileAction.update(fileIndexer, 100);
        verify(jTailMainModel).add(isA(JTailModel.class));
    }

    @Test
    public void testUpdateInvalidFileIndexer() {
        FileIndexer fileIndexer = mock(FileIndexer.class);

        try {
            indexFileAction.update(fileIndexer, 100);
            fail();
        } catch (AssertionError e) {
            // expected
        }
    }
}
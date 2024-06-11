import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import fr.pingtimeout.jtail.gui.action.IndexFileAction;
import fr.pingtimeout.jtail.gui.controller.JTailMainModel;
import fr.pingtimeout.jtail.gui.model.JTailModel;
import fr.pingtimeout.jtail.io.index.FileIndex;
import fr.pingtimeout.jtail.io.index.FileIndexer;
import fr.pingtimeout.jtail.io.index.RamFileIndex;
import fr.pingtimeout.jtail.io.index.RomFileIndex;
import fr.pingtimeout.jtail.util.ResourceBundleUtil;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

@RunWith(MockitoJUnitRunner.class)
public class IndexFileActionTest {

    @Mock
    private JTailMainModel jTailMainModel;

    @Mock
    private OpenFileModel openFileModel;

    @Mock
    private InformationHandler informationHandler;

    @Mock
    private ExceptionHandler exceptionHandler;

    @Mock
    private UIMessage uiMessage;

    @Mock
    private JDialog dialog;

    private IndexFileAction indexFileAction;

    @Before
    public void setUp() {
        Mockito.when(openFileModel.getFile()).thenReturn(new File("testFile.txt"));
        Mockito.when(openFileModel.getIndexType()).thenReturn(IndexType.FILE_BASED);
        indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
        Mockito.when(jTailMainModel.add(Mockito.any(JTailModel.class))).thenReturn(true);
        Mockito.when(informationHandler.handle(Mockito.anyBoolean(), Mockito.any(UIMessage.class), Mockito.any(Object[].class))).thenReturn(true);
        Mockito.when(exceptionHandler.handle(Mockito.any(Exception.class), Mockito.any(UIMessage.class), Mockito.any(Object.class))).thenReturn(true);
    }

    @Test
    public void testActionPerformed_ValidFile_ValidIndexType_Success() {
        indexFileAction.actionPerformed(new ActionEvent(dialog, 0, "testAction"));
        ArgumentCaptor<UIMessage> messageCaptor = ArgumentCaptor.forClass(UIMessage.class);
        Mockito.verify(informationHandler).handle(Mockito.eq(true), Mockito.eq(messageCaptor.capture()), Mockito.any(Object[].class));
        Assert.assertEquals(UIMessage.INFO_FILE_OPENED, messageCaptor.getValue());
    }

    @Test
    public void testActionPerformed_NullFile_Failure() {
        Mockito.when(openFileModel.getFile()).thenReturn(null);
        indexFileAction.actionPerformed(new ActionEvent(dialog, 0, "testAction"));
        ArgumentCaptor<UIMessage> messageCaptor = ArgumentCaptor.forClass(UIMessage.class);
        Mockito.verify(informationHandler).handle(Mockito.eq(true), Mockito.eq(messageCaptor.capture()), Mockito.any(Object[].class));
        Assert.assertEquals(UIMessage.INFO_NO_FILE_SELECTED, messageCaptor.getValue());
    }

    @Test
    public void testActionPerformed_InvalidIndexType_Failure() {
        Mockito.when(openFileModel.getIndexType()).thenReturn(IndexType.INVALID);
        indexFileAction.actionPerformed(new ActionEvent(dialog, 0, "testAction"));
        ArgumentCaptor<UIMessage> messageCaptor = ArgumentCaptor.forClass(UIMessage.class);
        Mockito.verify(informationHandler).handle(Mockito.eq(true), Mockito.eq(messageCaptor.capture()), Mockito.any(Object[].class));
        Assert.assertEquals(UIMessage.ERROR_INVALID_INDEX_TYPE, messageCaptor.getValue());
    }

    @Test(expected = FileNotFoundException.class)
    public void testActionPerformed_InvalidFile_Failure() {
        Mockito.when(openFileModel.getFile()).thenThrow(new FileNotFoundException("File not found"));
        indexFileAction.actionPerformed(new ActionEvent(dialog, 0, "testAction"));
        Mockito.verify(exceptionHandler).handle(Mockito.any(FileNotFoundException.class), Mockito.eq(UIMessage.ERROR_FILE_NOT_FOUND), Mockito.eq("testFile.txt"));
    }

    @Test
    public void testUpdate_IndexingSuccess_ModelAddedToJTailMainModel() {
        FileIndex index = new RamFileIndex();
        FileIndexer fileIndexer = new FileIndexer(new File("testFile.txt"), RamFileIndex.class);
        indexFileAction.update(fileIndexer, 100);
        Mockito.verify(jTailMainModel).add(Mockito.any(JTailModel.class));
    }

    @Test(expected = FileNotFoundException.class)
    public void testUpdate_FileNotFoundException_ErrorMessageShown() {
        FileIndex index = new RamFileIndex();
        FileIndexer fileIndexer = new FileIndexer(new File("testFile.txt"), RamFileIndex.class);
        Mockito.when(fileIndexer.getFile()).thenThrow(new FileNotFoundException("File not found"));
        indexFileAction.update(fileIndexer, 100);
        ArgumentCaptor<UIMessage> messageCaptor = ArgumentCaptor.forClass(UIMessage.class);
        Mockito.verify(exceptionHandler).handle(Mockito.any(FileNotFoundException.class), Mockito.eq(UIMessage.ERROR_FILE_NOT_FOUND), Mockito.any(Object.class));
    }

    @Test(expected = Exception.class)
    public void testUpdate_GenericException_ErrorMessageShown() {
        FileIndex index = new RamFileIndex();
        FileIndexer fileIndexer = new FileIndexer(new File("testFile.txt"), RamFileIndex.class);
        Mockito.when(fileIndexer.getFile()).thenThrow(new IOException("Generic error"));
        indexFileAction.update(fileIndexer, 100);
        ArgumentCaptor<UIMessage> messageCaptor = ArgumentCaptor.forClass(UIMessage.class);
        Mockito.verify(exceptionHandler).handle(Mockito.any(Exception.class), Mockito.eq(UIMessage.ERROR_GENERIC_MESSAGE), Mockito.any(Object[].class));
    }
}

enum IndexType {
    MEMORY_BASED,
    FILE_BASED,
    INVALID
}
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import fr.pingtimeout.jtail.gui.action.IndexFileAction;
import fr.pingtimeout.jtail.gui.action.OpenFileModel;
import fr.pingtimeout.jtail.gui.controller.JTailMainModel;
import fr.pingtimeout.jtail.io.index.FileIndexer;
import fr.pingtimeout.jtail.io.index.FileIndexerWorker;
import fr.pingtimeout.jtail.io.index.RamFileIndex;
import fr.pingtimeout.jtail.io.index.RomFileIndex;
import fr.pingtimeout.jtail.gui.model.JTailModel;

public class IndexFileActionTest {
    private static final ResourceBundle bundle = ResourceBundle.getBundle("fr.pingtimeout.jtail.gui.language");
    private IndexFileAction indexFileAction;
    private OpenFileModel openFileModel;
    private JTailMainModel jTailMainModel;

    @Before
    public void setup() {
        openFileModel = mock(OpenFileModel.class);
        jTailMainModel = mock(JTailMainModel.class);
        indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
    }

    @Test
    public void testActionPerformed_ValidFileAndIndexType_Success() {
        // Given: Valid file and index type
        String filePath = "testfile.txt";
        File file = new File(filePath);
        OpenFileModel validOpenFileModel = new OpenFileModel(file, IndexType.MEMORY_BASED);
        when(openFileModel.getFile()).thenReturn(file);
        when(openFileModel.getIndexType()).thenReturn(IndexType.MEMORY_BASED);

        // When: Action performed
        indexFileAction.actionPerformed(new ActionEvent(new JButton(), ActionEvent.ACTION_PERFORMED, "openFile"));

        // Then: File indexing is initiated
        verify(openFileModel, times(1)).getFile();
        verify(openFileModel, times(1)).getIndexType();
        verify(jTailMainModel, times(1)).add(any(JTailModel.class));

        // And: No exceptions are thrown
        verifyNoMoreInteractions(System.err);
    }

    @Test
    public void testActionPerformed_NullFile_Error() {
        // Given: Null file
        when(openFileModel.getFile()).thenReturn(null);

        // When: Action performed
        indexFileAction.actionPerformed(new ActionEvent(new JButton(), ActionEvent.ACTION_PERFORMED, "openFile"));

        // Then: No file is indexed and error message is displayed
        verify(jTailMainModel, never()).add(any(JTailModel.class));
        verify(System.err, times(1)).println(UIMessage.INFO_NO_FILE_SELECTED.getMessage());
    }

    @Test
    public void testActionPerformed_InvalidIndexType_Error() {
        // Given: Invalid index type
        OpenFileModel invalidOpenFileModel = new OpenFileModel(new File("testfile.txt"), IndexType.INVALID);
        when(openFileModel.getFile()).thenReturn(new File("testfile.txt"));
        when(openFileModel.getIndexType()).thenReturn(IndexType.INVALID);

        // When: Action performed
        indexFileAction.actionPerformed(new ActionEvent(new JButton(), ActionEvent.ACTION_PERFORMED, "openFile"));

        // Then: No file is indexed and error message is displayed
        verify(jTailMainModel, never()).add(any(JTailModel.class));
        verify(System.err, times(1)).println(UIMessage.ERROR_INVALID_INDEX_TYPE.getMessage());
    }

    @Test
    public void testUpdate_FileIndexingSuccess_ModelAdded() {
        // Given: File indexing is successful
        File file = new File("testfile.txt");
        OpenFileModel openFileModel = new OpenFileModel(file, IndexType.MEMORY_BASED);
        FileIndexer fileIndexer = mock(FileIndexer.class);
        when(openFileModel.getFile()).thenReturn(file);
        when(openFileModel.getIndexType()).thenReturn(IndexType.MEMORY_BASED);
        when(new FileIndexer(file, RamFileIndex.class)).thenReturn(fileIndexer);

        // When: File indexing is initiated
        indexFileAction.actionPerformed(new ActionEvent(new JButton(), ActionEvent.ACTION_PERFORMED, "openFile"));

        // Then: Model is added to JTailMainModel
        verify(jTailMainModel, times(1)).add(any(JTailModel.class));
        verify(fileIndexer, times(1)).addObserver(indexFileAction);
    }

    @Test
    public void testUpdate_FileIndexingFailed_ErrorMessageDisplayed() {
        // Given: File indexing fails
        File file = new File("testfile.txt");
        OpenFileModel openFileModel = new OpenFileModel(file, IndexType.MEMORY_BASED);
        when(openFileModel.getFile()).thenReturn(file);
        when(openFileModel.getIndexType()).thenReturn(IndexType.MEMORY_BASED);
        when(new FileIndexer(file, RamFileIndex.class)).thenThrow(new FileNotFoundException());

        // When: File indexing is initiated
        indexFileAction.actionPerformed(new ActionEvent(new JButton(), ActionEvent.ACTION_PERFORMED, "openFile"));

        // Then: No model is added to JTailMainModel
        verify(jTailMainModel, never()).add(any(JTailModel.class));
        verify(System.err, times(1)).println(UIMessage.ERROR_FILE_NOT_FOUND.getMessage());
    }
}

enum IndexType {
    MEMORY_BASED, FILE_BASED, INVALID
}
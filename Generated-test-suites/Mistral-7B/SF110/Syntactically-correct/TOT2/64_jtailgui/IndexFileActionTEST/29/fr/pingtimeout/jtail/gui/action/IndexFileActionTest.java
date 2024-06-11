
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import fr.pingtimeout.jtail.gui.action.IndexFileAction;
import fr.pingtimeout.jtail.gui.model.JTailMainModel;
import fr.pingtimeout.jtail.gui.model.OpenFileModel;
import fr.pingtimeout.jtail.io.index.FileIndexer;
import fr.pingtimeout.jtail.io.FileIndexerWorker;
import fr.pingtimeout.jtail.io.FileIndexerWorker.RunWorkable;
import fr.pingtimeout.jtail.io.index.RomFileIndex;
import fr.pingtimeout.jtail.io.index.RamFileIndex;
import fr.pingtimeout.jtail.io.index.FileIndexerFactory;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

public class IndexFileActionTest {

    @Test(expected = NullPointerException.class)
    public void testIndexFileActionConstructorNullJTailMainModel() {
        OpenFileModel openFileModel = mock(OpenFileModel.class);
        IndexFileAction indexFileAction = new IndexFileAction(null, openFileModel);
    }

    @Test(expected = NullPointerException.class)
    public void testIndexFileActionConstructorNullOpenFileModel() {
        JTailMainModel jTailMainModel = mock(JTailMainModel.class);
        IndexFileAction indexFileAction = new IndexFileAction(jTailMainModel, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIndexFileActionConstructorUnsupportedIndexType() {
        JTailMainModel jTailMainModel = mock(JTailMainModel.class);
        OpenFileModel openFileModel = mock(OpenFileModel.class);
        when(openFileModel.getIndexType()).thenReturn(123); // Unsupported index type
        IndexFileAction indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
    }

    @Test(expected = FileNotFoundException.class)
    public void testFileIndexerCreationWithNonExistentFile() throws FileNotFoundException {
        JTailMainModel jTailMainModel = mock(JTailMainModel.class);
        OpenFileModel openFileModel = new OpenFileModel("non-existent-file.txt", FileIndexer.IndexType.FILE_BASED);
        when(openFileModel.getFile()).thenThrow(new FileNotFoundException());
        IndexFileAction indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
    }

    @Test
    public void testFileIndexerCreationWithSupportedFile() throws IOException {
        JTailMainModel jTailMainModel = mock(JTailMainModel.class);
        OpenFileModel openFileModel = new OpenFileModel(new File("test-file.txt"), FileIndexer.IndexType.FILE_BASED);
        doAnswer(invocation -> {
            Indexer indexer = null;
            switch ((int)invocation.getArgumentAt(0, 1)) {
                case FileIndexer.IndexType.MEMORY_BASED:
                    indexer = new RamFileIndex();
                    break;
                case FileIndexer.IndexType.FILE_BASED:
                    indexer = new RomFileIndex();
                    break;
            }
            return indexer;
        }).when(IndexFileAction.class).createIndexer(eq(FileIndexer.IndexType.FILE_BASED));
        IndexFileAction indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
        FileIndexer indexer = indexFileAction.indexer;
        assertNotNull(indexer);
    }

    @Test(expected = InterruptedException.class)
    public void testFileIndexerWorkerInterrupted() throws InterruptedException {
        JTailMainModel jTailMainModel = mock(JTailMainModel.class);
        OpenFileModel openFileModel = new OpenFileModel(new File("test-file.txt"), FileIndexer.IndexType.FILE_BASED);
        IndexFileAction indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
        FileIndexerWorker fileIndexerWorker = new FileIndexerWorker(indexFileAction);
        Thread thread = new Thread(fileIndexerWorker);
        thread.interrupt();
    }

    @Test
    public void testFileIndexingSuccess() throws IOException, InterruptedException {
        JTailMainModel jTailMainModel = mock(JTailMainModel.class);
        OpenFileModel openFileModel = new OpenFileModel(new File("test-file.txt"), FileIndexer.IndexType.FILE_BASED);
        IndexFileAction indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
        FileIndexerWorker fileIndexerWorker = new FileIndexerWorker(indexFileAction);
        Thread thread = new Thread(fileIndexerWorker);
        thread.start();
        thread.join();
        FileIndexer indexer = indexFileAction.indexer;
        File file = indexer.getFile();
        assertTrue(file.exists());
        JTailModel model = indexFileAction.jTailMainModel.get(0);
        assertTrue(model instanceof JTailModel);
    }

    @Test(expected = IOException.class)
    public void testFileIndexingFailure() throws IOException, InterruptedException {
        JTailMainModel jTailMainModel = mock(JTailMainModel.class);
        OpenFileModel openFileModel = new OpenFileModel(new File("test-file.txt"), FileIndexer.IndexType.FILE_BASED);
        doThrow(new IOException("Test error")).when(IndexFileAction.class).createIndexer(eq(FileIndexer.IndexType.FILE_BASED));
        IndexFileAction indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
        FileIndexerWorker fileIndexerWorker = new FileIndexerWorker(indexFileAction);
        Thread thread = new Thread(fileIndexerWorker);
        thread.start();
        thread.join();
    }
}

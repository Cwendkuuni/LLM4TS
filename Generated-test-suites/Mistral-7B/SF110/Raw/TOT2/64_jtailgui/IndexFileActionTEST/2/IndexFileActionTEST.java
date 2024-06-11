import org.junit.Test;
import static org.junit.Assert.assertNotNull;

public class IndexFileActionTest {
    @Test
    public void testConstructor() {
        JTailMainModel jTailMainModel = new JTailMainModel();
        OpenFileModel openFileModel = new OpenFileModel();
        IndexFileAction indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
        assertNotNull(indexFileAction);
    }
}
import org.junit.Test;
import static org.junit.Assert.*;

public class IndexFileActionTest {
    

    @Test
    public void testConstructor() {
        JTailMainModel jTailMainModel = mock(JTailMainModel.class);
        OpenFileModel openFileModel = mock(OpenFileModel.class);

        IndexFileAction indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);

        assertNotNull(indexFileAction);
    }

    
}
import static org.junit.Assert.*;

import com.puppycrawl.tools.checkstyle.api.FileText;
import org.junit.Test;

public class FileTextTest {

    @Test
    public void testFileTextConstruction() throws IOException {
        File sampleFile = new File("sample.txt");
        FileText fileText = new FileText(sampleFile, "UTF-8");
        assertEquals(sampleFile.getName(), fileText.getFile().getName());
        assertEquals("Expected file content", fileText.getFullText());
    }
}
import org.junit.Assert.*;
import com.puppycrawl.tools.checkstyle.api.FileText;
import java.io.File;

public class FileTextTest {
    @Test
    public void testFileTextConstructorWithValidFileAndCharset() throws IOException {
        File file = new File("testfile.txt");
        String charsetName = "UTF-8";
        FileText fileText = new FileText(file, charsetName);
        // add assertions here
    }
}
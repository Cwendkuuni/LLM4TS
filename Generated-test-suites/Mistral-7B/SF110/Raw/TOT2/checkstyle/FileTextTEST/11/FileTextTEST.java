import org.junit.Assert;
import org.junit.Test;

public class FileTextTest {
    @Test
    public void testFileTextConstruction() throws IOException {
        File file = new File("testFile.txt");
        FileText fileText = new FileText(file, "UTF-8");
        Assert.assertNotNull(fileText.getFile());
        Assert.assertNotNull(fileText.getCharset());
        Assert.assertNotNull(fileText.getFullText());
        Assert.assertArrayNotEquals(new String[0], fileText.toLinesArray());
    }
}
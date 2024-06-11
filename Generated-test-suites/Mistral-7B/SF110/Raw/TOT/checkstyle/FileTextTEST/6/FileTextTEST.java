import org.junit.Assert;
import org.junit.Test;
import com.puppycrawl.tools.checkstyle.api.FileText;

public class FileTextTest {
    @Test
    public void testFileTextConstruction() throws IOException {
        // given
        File file = new File("test.txt"); // replace with an existing file
        String charsetName = "UTF-8";

        // when
        FileText fileText = new FileText(file, charsetName);

        // then
        Assert.assertNotNull(fileText);
        Assert.assertNotNull(fileText.getFile());
        Assert.assertNotNull(fileText.getCharset());
        Assert.assertNotNull(fileText.getFullText());
        Assert.assertNotNull(fileText.toLinesArray());
        Assert.assertEquals(true, fileText.getFile().exists());
    }
}
import org.junit.Test;
import org.junit.Assert.*;
import org.apache.commons.compress.archivers.test.TestFiles;

public class ArchiveUtilsTest {
    @Test
    public void testToString() {
        ArchiveEntry entry = TestFiles.getDefaultTestFile("TestFile.txt");
        String expected = "- 1024 TestFile.txt";
        String result = ArchiveUtils.toString(entry);
        assertEquals(expected, result);
    }
}
import org.junit.Test;
import org.junit.Assert;
import org.fixsuite.message.Library;
import java.io.File;

public class LibraryTest {

    @Test
    public void testLoadFromDirectorySuccess() {
        Library library = new Library();
        String libraryPath = "testData";
        boolean result = library.loadFromDirectory(libraryPath);
        Assert.assertTrue(result);
    }
}
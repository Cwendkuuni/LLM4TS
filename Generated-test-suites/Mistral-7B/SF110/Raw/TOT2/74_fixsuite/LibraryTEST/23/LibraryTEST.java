import org.junit.Test;
import org.junit.Assert;
import org.fixsuite.message.Library;
import java.io.File;

public class LibraryTest {
    @Test
    public void testLoadFromDirectory() {
        Library library = new Library();
        String libraryPath = "test_library_path";
        boolean result = library.loadFromDirectory(libraryPath);
        Assert.assertTrue(result);
    }
}
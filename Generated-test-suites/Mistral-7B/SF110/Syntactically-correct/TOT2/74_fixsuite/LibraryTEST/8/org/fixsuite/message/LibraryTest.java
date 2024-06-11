import org.junit.Test;
import org.junit.Assert.*;
import org.fixsuite.message.Library;
import java.io.File;

public class LibraryTest {
    @Test
    public void testLoadFromDirectory() {
        Library library = new Library();
        String libraryPath = "path/to/library";
        assertTrue(library.loadFromDirectory(libraryPath));
    }
}
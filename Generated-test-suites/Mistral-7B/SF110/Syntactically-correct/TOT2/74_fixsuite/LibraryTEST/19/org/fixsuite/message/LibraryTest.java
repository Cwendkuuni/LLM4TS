import org.junit.Test;
import static org.junit.Assert.*;
import org.fixsuite.message.Library;

public class LibraryTest {

    @Test
    public void testLibraryConstructor() {
        Library library = new Library();
        assertNotNull(library);
    }

    @Test
    public void testLoadFromDirectory() {
        Library library = new Library();
        String libraryPath = "testDirectory";
        assertTrue(library.loadFromDirectory(libraryPath));
    }

    @Test
    public void testGetDictionaries() {
        Library library = new Library();
        library.loadFromDirectory("testDirectory");
        List<DictionaryInfo> dictionaries = library.getDictionaries();
        assertFalse(dictionaries.isEmpty());
    }

    @Test
    public void testGetDictionary() {
        Library library = new Library();
        library.loadFromDirectory("testDirectory");
        DictionaryInfo dictionary = library.getDictionary("testVersion");
        assertNotNull(dictionary);
    }

    // Test remaining methods here
}
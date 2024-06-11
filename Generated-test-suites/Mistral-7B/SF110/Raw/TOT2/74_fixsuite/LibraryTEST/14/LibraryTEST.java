import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Assert.*;
import org.fixsuite.message.Library;
import org.fixsuite.message.info.DictionaryInfo;
import java.io.File;
import java.util.List;

public class LibraryTest {

    @Test
    public void testLibraryConstructor() {
        Library library = new Library();
        assertNotNull(library);
    }

    @Test (expected = NullPointerException.class)
    public void testLibraryConstructorWithNullLogger() {
        Library library = new Library(null);
    }

    @Test
    public void testLoadFromDirectoryWithValidPath() {
        String libraryPath = "test/resources/valid_path";
        Library library = new Library();
        assertTrue(library.loadFromDirectory(libraryPath));
    }

    @Test (expected = FileNotFoundException.class)
    public void testLoadFromDirectoryWithEmptyDirectory() {
        String libraryPath = "test/resources/empty_directory";
        Library library = new Library();
        assertFalse(library.loadFromDirectory(libraryPath));
    }

    @Test (expected = IllegalArgumentException.class)
    public void testLoadFromDirectoryWithInvalidPath() {
        String libraryPath = "test/invalid_path";
        Library library = new Library();
        assertFalse(library.loadFromDirectory(libraryPath));
    }

    @Test
    public void testGetDictionaries() {
        String libraryPath = "test/resources/valid_path";
        Library library = new Library();
        library.loadFromDirectory(libraryPath);
        List<DictionaryInfo> dictionaries = library.getDictionsaries();
        assertFalse(dictionaries.isEmpty());
    }

    @Test (expected = NullPointerException.class)
    public void testGetDictionaryWithNullLibrary() {
        Library library = null;
        String version = "1.0";
        Library.DictionaryInfo dictionary = library.getDictionary(version);
        assertNull(dictionary);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testGetDictionaryWithInvalidVersion() {
        String libraryPath = "test/resources/valid_path";
        Library library = new Library();
        library.loadFromDirectory(libraryPath);
        String version = "invalid_version";
        Library.DictionaryInfo dictionary = library.getDictionary(version);
        assertNull(dictionary);
    }

    @Test
    public void testArrangeFiles() {
        // Basic test case
        File[] files = new File[] {new File("test/resources/Fields.xml"), new File("test/resources/Enums.xml")};
        Library library = new Library();
        List<File> arrangedFiles = library.arrangeFiles(files);
        assertEquals(2, arrangedFiles.size());
    }

    @Test (expected = NullPointerException.class)
    public void testArrangeFilesWithNullFiles() {
        Library library = new Library();
        File[] files = null;
        List<File> arrangedFiles = library.arrangeFiles(files);
        assertNull(arrangedFiles);
    }

    @Test
    public void testLoadDirectory() {
        // Basic test case
        List<File> files = Arrays.asList(new File("test/resources/Fields.xml"), new File("test/resources/Enums.xml"));
        Library library = new Library();
        library.loadDirectory(files, "1.0");
        Library.DictionaryInfo dictionary = library.getDictionary("1.0");
        assertNotNull(dictionary);
    }

    @Test (expected = NullPointerException.class)
    public void testLoadDirectoryWithNullFiles() {
        List<File> files = null;
        Library library = new Library();
        library.loadDirectory(files, "1.0");
        Library.DictionaryInfo dictionary = library.getDictionary("1.0");
        assertNull(dictionary);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testLoadDirectoryWithInvalidVersion() {
        List<File> files = Arrays.asList(new File("test/resources/Fields.xml"), new File("test/resources/Enums.xml"));
        Library library = new Library();
        library.loadDirectory(files, "invalid_version");
        Library.DictionaryInfo dictionary = library.getDictionary("invalid_version");
        assertNull(dictionary);
    }

}
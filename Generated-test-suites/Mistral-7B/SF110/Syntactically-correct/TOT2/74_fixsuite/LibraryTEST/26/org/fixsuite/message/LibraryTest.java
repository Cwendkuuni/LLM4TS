
import org.junit.Test;
import org.junit.Assert.*;
import org.fixsuite.message.Library;
import java.io.File;
import java.util.List;
import java.util.Arrays;

public class LibraryTest {
    @Test
    public void testLibraryConstructor() {
        Library library = new Library();
        assertNotNull(library);
    }

    @Test
    public void testLoadFromDirectory() {
        Library library = new Library();
        String libraryPath = "test_library_path";
        assertTrue(library.loadFromDirectory(libraryPath));
    }

    @Test
    public void testGetDictionaries() {
        Library library = new Library();
        library.loadFromDirectory("test_library_path");
        List<DictionaryInfo> dictionaries = library.getDictionaries();
        assertNotNull(dictionaries);
        assertTrue(dictionaries.size() > 0);
    }

    @Test
    public void testGetDictionary() {
        Library library = new Library();
        library.loadFromDirectory("test_library_path");
        String version = "test_version";
        DictionaryInfo dictionary = library.getDictionary(version);
        assertNotNull(dictionary);
        assertNotNull(dictionary.getVersion());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLoadFromDirectory_EmptyLibraryPath() {
        Library library = new Library();
        String libraryPath = "";
        library.loadFromDirectory(libraryPath);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLoadFromDirectory_NonExistentLibraryPath() {
        Library library = new Library();
        String libraryPath = "non_existent_library_path";
        library.loadFromDirectory(libraryPath);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLoadFromDirectory_MissingFileExtensions() {
        Library library = new Library();
        String libraryPath = "/test_library_path/";
        library.loadFromDirectory(libraryPath);
    }

    @Test(expected = FileNotFoundException.class)
    public void testLoadFromDirectory_InvalidXMLFiles() {
        Library library = new Library();
        String libraryPath = "/test_library_path/";
        File xmlFile = new File(libraryPath + "invalid_xml_file.txt");
        library.loadFromDirectory(libraryPath);
        library.loadDirectory(Arrays.asList(xmlFile), "test_xml_version");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLoadFromDirectory_LoadingDictionariesWithTheSameVersion() {
        Library library = new Library();
        String libraryPath = "/test_library_path/";
        File xmlFile1 = new File(libraryPath + "xml_file_1.xml");
        File xmlFile2 = new File(libraryPath + "xml_file_2.xml");
        library.loadFromDirectory(libraryPath);
        library.loadDirectory(Arrays.asList(xmlFile1), "test_xml_version");
        library.loadDirectory(Arrays.asList(xmlFile2), "test_xml_version");
    }

    @Test
    public void testLoadFromDirectory_NegativeMemoryAllocation() {
        Library library = new Library();
        String libraryPath = "/test_library_path/";
        long startMemory = Runtime.getRuntime().freeMemory();
        library.loadFromDirectory(libraryPath);
        List<DictionaryInfo> dictionaries = library.getDictionaries();
        long endMemory = Runtime.getRuntime().freeMemory();
        assertFalse("Negative memory allocation", (endMemory - startMemory) < 0);
    }
}

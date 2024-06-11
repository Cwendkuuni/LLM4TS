import org.fixsuite.message.Library;
import org.fixsuite.message.info.DictionaryInfo;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

public class LibraryTest {
    private Library library;

    @Before
    public void setUp() {
        library = new Library();
    }

    @Test
    public void testLoadFromDirectory() {
        String libraryPath = "path/to/library"; // Replace with the path to a valid directory
        boolean result = library.loadFromDirectory(libraryPath);
        List<DictionaryInfo> dictionaries = library.getDictionaries();

        // Check if the method has loaded the dictionaries correctly
        Assert.assertTrue(result);
        Assert.assertFalse(dictionaries.isEmpty());
        Assert.assertEquals(5, dictionaries.size());

        // Test with an empty directory
        String emptyDirectoryPath = "path/to/empty_directory";
        result = library.loadFromDirectory(emptyDirectoryPath);
        dictionaries = library.getDictionaries();
        Assert.assertFalse(result);
        Assert.assertTrue(dictionaries.isEmpty());

        // Test with an invalid path
        String invalidPath = "path/to/invalid_path";
        result = library.loadFromDirectory(invalidPath);
        dictionaries = library.getDictionaries();
        Assert.assertFalse(result);
        Assert.assertTrue(dictionaries.isEmpty());

        // Test with a malformed XML file
        String malformedXmlPath = "path/to/malformed_xml_file";
        File malformedXmlFile = new File(malformedXmlPath);
        List<File> arrangedFiles = library.arrangeFiles(malformedXmlFile.listFiles());
        library.loadDirectory(arrangedFiles, "version");
        dictionaries = library.getDictionaries();
        Assert.assertTrue(dictionaries.isEmpty());
    }
}
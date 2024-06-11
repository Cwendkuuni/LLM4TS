import org.fixsuite.message.Library;
import org.fixsuite.message.info.DictionaryInfo;
import org.junit.Assert;
import org.junit.Test;

public class LibraryTest {
    
    @Test
    public void testLoadFromDirectory() {
        String libraryPath = "test/resources/library"; // Valid directory path
        Library library = new Library();
        library.loadFromDirectory(libraryPath);
        List<DictionaryInfo> dictionaries = library.getDictionaries();
        Assert.assertFalse(dictionaries.isEmpty());
    }

    @Test(expected = IllegalStateException.class)
    public void testLoadFromInvalidDirectory() {
        String libraryPath = "test/resources/invalid"; // Invalid directory path
        Library library = new Library();
        library.loadFromDirectory(libraryPath);
        // This test case is expected to throw an IllegalStateException
    }

    @Test
    public void testLoadEmptyDirectory() {
        String libraryPath = "test/resources/empty"; // Empty directory
        Library library = new Library();
        library.loadFromDirectory(libraryPath);
        List<DictionaryInfo> dictionaries = library.getDictionaries();
        Assert.assertTrue(dictionaries.isEmpty());
    }
    
}
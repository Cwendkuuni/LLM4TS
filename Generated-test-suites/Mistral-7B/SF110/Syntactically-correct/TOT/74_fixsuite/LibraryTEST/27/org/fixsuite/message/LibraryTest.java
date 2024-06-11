import org.fixsuite.message.Library;
import org.fixsuite.message.info.DictionaryInfo;
import org.junit.Assert;
import org.junit.Test;

public class LibraryTest {
    

    @Test
    public void testLoadFromDirectory() {
        String libraryPath = "path/to/library";
        Library library = new Library();
        Assert.assertTrue(library.loadFromDirectory(libraryPath));
        List<DictionaryInfo> dictionaries = library.getDictionaries();
        Assert.assertFalse(dictionaries.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLoadFromDirectoryEmptyDirectory() {
        String libraryPath = "path/to/empty_directory";
        Library library = new Library();
        library.loadFromDirectory(libraryPath);
        Assert.assertTrue(library.dictionaries.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLoadFromDirectoryInvalidPath() {
        String libraryPath = "path/to/invalid_path";
        Library library = new Library();
        library.loadFromDirectory(libraryPath);
        Assert.assertTrue(library.dictionaries.isEmpty());
    }

    @Test(expected = NullPointerException.class)
    public void testLoadFromDirectoryNullFiles() {
        String libraryPath = "path/to/library";
        File[] files = null;
        Library library = new Library();
        library.loadFromDirectory(libraryPath, files);
        Assert.assertTrue(library.dictionaries.isEmpty());
    }

    @Test
    public void testGetDictionaries() {
        String libraryPath = "path/to/library";
        Library library = new Library();
        library.loadFromDirectory(libraryPath);
        List<DictionaryInfo> dictionaries = library.getDictionaries();
        Assert.assertFalse(dictionaries.isEmpty());
    }

    
}
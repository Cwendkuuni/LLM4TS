
package org.fixsuite.message;
import org.junit.*;
import static org.junit.Assert.*;
import java.io.File;
import java.util.List;

public class LibraryTest {
    private Library library;

    @Before
    public void setup() {
        this.library = new Library();
    }

    @Test
    public void testLibraryConstructor() {
        Assert.assertNotNull(this.library);
        Assert.assertEquals(0, this.library.dictionaries.size());
    }

    @Test
    public void testLoadEmptyDirectory() {
        boolean result = this.library.loadFromDirectory("src/test/resources/empty_directory");
        Assert.assertEquals(false, result);
    }

    @Test
    public void testLoadDirectory() {
        File testDirectory = new File("src/test/resources/test_directory");
        File[] files = testDirectory.listFiles();
        this.library.loadFromDirectory(testDirectory.getAbsolutePath());
        Assert.assertNotNull(this.library.dictionaries);
        Assert.assertTrue(this.library.dictionaries.size() > 0);
    }

    @Test
    public void testLoadInvalidDirectory() {
        boolean result = this.library.loadFromDirectory("src/test/resources/invalid_directory");
        Assert.assertEquals(false, result);
    }

    @Test
    public void testLoadDirectoryWithInvalidFileName() {
        File testDirectory = new File("src/test/resources/test_directory_invalid_filename");
        boolean result = this.library.loadFromDirectory(testDirectory.getAbsolutePath());
        Assert.assertEquals(false, result);
    }

    @Test
    public void testGetDictionaries() {
        File testDirectory = new File("src/test/resources/test_directory");
        this.library.loadFromDirectory(testDirectory.getAbsolutePath());
        List<DictionaryInfo> dictionaries = this.library.getDictionaries();
        Assert.assertNotNull(dictionaries);
        Assert.assertTrue(dictionaries.size() > 0);
    }

    @Test
    public void testGetDictionary() {
        File testDirectory = new File("src/test/resources/test_directory");
        this.library.loadFromDirectory(testDirectory.getAbsolutePath());
        DictionaryInfo dictionary = this.library.getDictionary("1.0");
        Assert.assertNotNull(dictionary);
    }
}
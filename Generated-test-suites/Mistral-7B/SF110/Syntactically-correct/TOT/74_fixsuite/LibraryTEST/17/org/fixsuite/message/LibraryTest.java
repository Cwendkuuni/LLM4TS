import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.fixsuite.message.Library;
import org.fixsuite.message.info.DictionaryInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LibraryTest {
    private static final String TEST_LIBRARY_PATH = "path/to/test/library";
    private Library library;
    private Logger log;

    @Before
    public void setUp() {
        this.library = new Library();
        this.log = LoggerFactory.getLogger(Library.class);
        Library.logger = this.log;
    }

    @Test
    public void testLoadFromDirectory_ValidLibraryPath_LoadsDictionaries() {
        this.library.loadFromDirectory(TEST_LIBRARY_PATH);
        List<DictionaryInfo> dictionaries = this.library.getDictionaries();
        assertTrue("No dictionaries were loaded", !dictionaries.isEmpty());
        assertNotNull("DictionaryInfo for a valid version was not loaded", this.library.getDictionary("someValidVersion"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLoadFromDirectory_EmptyDirectory_ThrowsException() {
        File testDirectory = new File(TEST_LIBRARY_PATH + "/emptyDirectory");
        this.library.loadFromDirectory(testDirectory.getAbsolutePath());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLoadFromDirectory_InvalidDirectoryPath_ThrowsException() {
        this.library.loadFromDirectory("/invalid/directory/path");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLoadFromDirectory_LibraryContainsNoDictionaries_ThrowsException() {
        this.library.loadFromDirectory(TEST_LIBRARY_PATH);
        this.library.loadFromDirectory(TEST_LIBRARY_PATH); // second call should fail
    }
}
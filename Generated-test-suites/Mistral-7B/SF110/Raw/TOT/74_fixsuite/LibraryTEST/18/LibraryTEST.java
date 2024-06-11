import org.fixsuite.message.Library;
import org.fixsuite.message.info.DictionaryInfo;
import org.junit.Assert;
import org.junit.Test;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class LibraryTest {

    @Test
    public void testLoadFromDirectory() throws IOException {
        // Given
        String libraryPath = "test_library_path";
        Library library = new Library();

        // When
        boolean result = library.loadFromDirectory(libraryPath);

        // Then
        Assert.assertTrue("Loading from directory should be successful", result);
        List<DictionaryInfo> dictionaries = library.getDictionaries();
        Assert.assertFalse("No dictionaries should be present before loading", dictionaries.isEmpty());
        Assert.assertEquals("Number of dictionaries loaded should be 5", 5, dictionaries.size());

        // Edge cases
        File emptyDirectory = new File("empty_directory");
        result = library.loadFromDirectory(emptyDirectory.getAbsolutePath());
        Assert.assertFalse("Loading from an empty directory should fail", result);

        File nonExistentDirectory = new File("non_existent_directory");
        result = library.loadFromDirectory(nonExistentDirectory.getAbsolutePath());
        Assert.assertFalse("Loading from a non-existent directory should fail", result);

        // Error cases
        File corruptFile1 = new File("corrupt_file1.xml");
        File corruptFile2 = new File("corrupt_file2.xml");
        File[] files = {corruptFile1, corruptFile2};
        List<File> arrangedFiles = library.arrangeFiles(files);
        library.loadDirectory(arrangedFiles, "test_version");
        DictionaryInfo dictionary = library.getDictionary("test_version");
        Assert.assertNull("Dictionary should be null if loading fails", dictionary);
    }
}
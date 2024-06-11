import org.fixsuite.message.Library;
import org.fixsuite.message.info.DictionaryInfo;
import org.junit.Assert;
import org.junit.Test;

public class LibraryTest {
    @Test
    public void testLoadFromDirectory() {
        // Given
        String libraryPath = "path/to/valid/directory";
        Library library = new Library();

        // When
        boolean result = library.loadFromDirectory(libraryPath);

        // Then
        Assert.assertTrue(result);
        Assert.assertFalse(library.dictionaries.isEmpty());
        List<DictionaryInfo> dictionaries = library.getDictionaries();
        Assert.assertEquals(5, dictionaries.size());
        for (DictionaryInfo dictionary : dictionaries) {
            Assert.assertNotNull(dictionary);
            String version = dictionary.getVersion();
            Assert.assertNotNull(version);
        }
    }
}
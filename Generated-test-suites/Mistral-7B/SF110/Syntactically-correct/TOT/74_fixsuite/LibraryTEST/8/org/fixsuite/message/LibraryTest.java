import org.fixsuite.message.Library;
import org.junit.Assert;
import org.junit.Test;

public class LibraryTest {
    
    @Test
    public void testLoadFromDirectory() throws Exception {
        String libraryPath = "test/resources/libraries"; // Replace with the actual path to your test library directory
        Library library = new Library();
        boolean result = library.loadFromDirectory(libraryPath);
        Assert.assertTrue(result);
        List<DictionaryInfo> dictionaries = library.getDictionaries();
        Assert.assertFalse(dictionaries.isEmpty());
        
    }
}
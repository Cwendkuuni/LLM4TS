import static org.junit.Assert.*;
import org.fixsuite.message.Library;
import org.fixsuite.message.info.DictionaryInfo;
import org.junit.Test;
import org.mockito.Mockito;

public class LibraryTest {
    @Test
    public void testLoadFromDirectorySuccess() {
        // Setup
        String libraryPath = System.getProperty("user.dir") + "/test-data";
        File mockDirectory = Mockito.mock(File.class);
        when(mockDirectory.listFiles()).thenReturn(new File[] {
                new File("Fields.xml"), new File("Enums.xml"), new File("Components.xml"),
                new File("MsgType.xml"), new File("MsgContents.xml")
        });

        Library library = new Library();
        // Test
        boolean result = library.loadFromDirectory(libraryPath);
        List<DictionaryInfo> dictionaries = library.getDictionaries();
        // Verify
        assertTrue(result);
        assertFalse(dictionaries.isEmpty());
        // Cleanup
        Mockito.verify(mockDirectory).listFiles();
    }
    
    // Add test cases for edge cases and error scenarios here
}
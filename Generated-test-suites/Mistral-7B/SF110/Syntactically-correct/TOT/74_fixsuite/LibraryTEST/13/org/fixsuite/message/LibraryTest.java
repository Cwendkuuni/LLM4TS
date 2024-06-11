import org.fixsuite.message.Library;
import org.fixsuite.message.info.DictionaryInfo;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class LibraryTest {
    private Library library;
    private List<File> files;
    private static final String LIBRARY_PATH = "testData";

    @Before
    public void setup() {
        this.library = new Library();
        this.files = Mockito.mock(List.class);
        Mockito.when(this.files.size()).thenReturn(5);
        Mockito.when(this.files.get(0)).thenReturn(Mockito.mock(File.class));
        Mockito.when(this.files.get(1)).thenReturn(Mockito.mock(File.class));
        // Set up mocks for other files
    }

    @Test
    public void testLoadFromDirectorySuccess() {
        // Given
        Mockito.when(this.files.get(0).isDirectory()).thenReturn(true);
        Mockito.when(this.files.get(0).listFiles()).thenReturn(new File[0]);
        // Set up mocks for other files and their listFiles()
        this.library.loadFromDirectory(LIBRARY_PATH);

        // Then
        Assert.assertTrue(this.library.getDictionaries().size() > 0);
    }

    // Add test cases for edge cases and error scenarios
}
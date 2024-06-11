import org.fixsuite.message.Library;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Library.class})
public class LibraryTest {

    @Test
    public void testLoadFromDirectory() throws Exception {
        // Given
        String libraryPath = "test/resources/library";
        Library library = new Library();
        PowerMockito.mockStatic(Library.class);
        PowerMockito.when(Library.class, "loadDirectory", library, Mockito.anyListOf(File.class), Mockito.anyString())
                .thenReturn(true);

        // When
        boolean result = library.loadFromDirectory(libraryPath);

        // Then
        Assert.assertTrue(result);
        PowerMockito.verifyStatic(Library.class, "loadDirectory", library, Mockito.anyListOf(File.class), libraryPath);
    }

    // Additional test cases for edge and error scenarios can be added here
}
import org.fixsuite.message.Library;
import org.fixsuite.message.LibraryTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LibraryTest {
    private Library library;
    private static final String VALID_LIBRARY_PATH = "path/to/valid/directory";
    private static final String EMPTY_LIBRARY_PATH = "path/to/empty/directory";
    private static final String INVALID_LIBRARY_PATH = "path/to/invalid/directory";

    @BeforeEach
    public void setUp() {
        Library.logger = mock(Logger.class);
        library = new Library();
    }

    @Test
    public void testLoadFromDirectoryWithValidDirectory() {
        // Given
        when(Library.logger::debug).thenAnswer(i -> {});
        when(Library.logger::info).thenAnswer(i -> {});

        // When
        assertTrue(library.loadFromDirectory(VALID_LIBRARY_PATH));

        // Then
        List<DictionaryInfo> dictionaries = library.getDictionaries();
        assertFalse(dictionaries.isEmpty());

        // Clean up
        verify(Library.logger, times(1)).debug("Loaded " + anyInt() + " FIX dictionaries.");
        verify(Library.logger, times(1)).info("Loading time: " + anyInt() + " milliseconds.");
    }

    @Test
    public void testLoadFromDirectoryWithEmptyDirectory() {
        // Given
        when(Library.logger::debug).thenAnswer(i -> {});
        when(Library.logger::error).thenAnswer(i -> {});

        // When
        assertFalse(library.loadFromDirectory(EMPTY_LIBRARY_PATH));

        // Then
        assertTrue(Library.logger.getErrorMessages()[0].contains("Supplied directory is empty."));
    }

    @Test
    public void testLoadFromDirectoryWithInvalidDirectory() {
        // Given
        when(Library.logger::debug).thenAnswer(i -> {});
        when(Library.logger::error).thenAnswer(i -> {});

        // When
        assertFalse(library.loadFromDirectory(INVALID_LIBRARY_PATH));

        // Then
        assertTrue(Library.logger.getErrorMessages()[0].contains("Supplied path is not a directory."));
    }
}
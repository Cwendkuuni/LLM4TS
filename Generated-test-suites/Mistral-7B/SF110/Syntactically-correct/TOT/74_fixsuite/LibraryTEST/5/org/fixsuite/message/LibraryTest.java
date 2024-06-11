import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.fixsuite.message.Library;
import org.fixsuite.message.info.DictionaryInfo;
import org.fixsuite.message.parsers.fpl.MainParser;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class LibraryTest {

    private static final String LIBRARY_PATH = "test/resources/library";
    private static final List<File> FILES = Arrays.asList(
            new File("Fields.xml"),
            new File("Enums.xml"),
            new File("Components.xml"),
            new File("MsgType.xml"),
            new File("MsgContents.xml")
    );
    private Library library;
    private Map<String, DictionaryInfo> dictionaries;
    private Logger logger;

    @Before
    public void setUp() {
        library = new Library();
        dictionaries = new HashMap<>();
        logger = Mockito.mock(Logger.class);
        Library.logger = logger;
    }

    @Test
    public void testLoadFromDirectory() {
        when(logger.debug(anyString())).thenReturn(library);
        when(logger.error(anyString())).thenReturn(library);
        when(logger.info(anyString())).thenReturn(library);

        library.loadFromDirectory(LIBRARY_PATH);

        verify(logger, times(4)).debug(anyString());
        verify(logger, times(0)).error(anyString());
        verify(logger, times(1)).info(anyString());

        assertEquals(5, dictionaries.size());
        for (File file : FILES) {
            DictionaryInfo dictionary = library.getDictionary(file.getName().split("\\.")[0]);
            assertNotNull(dictionary);
            assertTrue(dictionary.isLoaded());
        }
    }

    @After
    public void tearDown() {
        library = null;
        dictionaries = null;
        Library.logger = null;
    }
}
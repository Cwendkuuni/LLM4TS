package org.fixsuite.message;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.fixsuite.message.info.*;
import org.fixsuite.message.parsers.fpl.MainParser;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;

public class LibraryTest {

    private Library library;
    private List<File> files;
    private File directory;
    private Logger logger;
    private MainParser parser;

    @Before
    public void setUp() {
        this.library = new Library();
        this.files = new ArrayList<>();
        this.directory = mock(File.class);
        when(this.directory.isDirectory()).thenReturn(true);
        this.files.addAll(Arrays.asList(new File[] { new File("Fields.xml"), new File("Enums.xml"), new File("Components.xml"), new File("MsgType.xml"), new File("MsgContents.xml") }));
        this.parser = mock(MainParser.class);
        when(this.parser.parse(any(DictionaryInfo.class), any(File.class))).thenReturn(true);
        this.logger = LoggerFactory.getLogger(Library.class);
    }

    @Test
    public void testLoadFromDirectoryEmptyDirectory() {
        when(this.directory.listFiles()).thenReturn(new File[0]);

        boolean result = this.library.loadFromDirectory(this.directory.getAbsolutePath());
        assertTrue(result);
        verify(this.logger, times(1)).debug("Supplied directory is empty.");
        assertEquals(0, this.library.getDictionaries().size());
    }

    @Test
    public void testLoadFromDirectoryNonExistingDirectory() {
        when(this.directory.listFiles()).thenThrow(new FileNotFoundException());

        boolean result = this.library.loadFromDirectory(this.directory.getAbsolutePath());
        assertFalse(result);
        verify(this.logger, times(1)).error("Supplied path is not a directory.");
        assertEquals(0, this.library.getDictionaries().size());
    }

    @Test
    public void testLoadFromDirectoryInvalidFilesOrder() {
        when(this.directory.listFiles()).thenReturn(this.files.toArray(new File[0]));

        this.library.loadFromDirectory(this.directory.getAbsolutePath());

        verify(this.logger, times(5)).debug("Loaded dictionary from {version}.");
        assertEquals(5, this.library.getDictionaries().size());
    }

    @Test
    public void testLoadFromDirectoryValidFilesOrder() {
        when(this.directory.listFiles()).thenReturn(this.files.toArray(new File[0]));

        this.library.loadFromDirectory(this.directory.getAbsolutePath());

        verify(this.logger, times(5)).debug("Loaded dictionary from {version}.");
        assertEquals(5, this.library.getDictionaries().size());
        verify(this.parser, times(5)).parse(any(DictionaryInfo.class), any(File.class));
    }

    @Test
    public void testLoadFromDirectoryMissingFile() throws IOException {
        this.files.remove(3);
        when(this.directory.listFiles()).thenReturn(this.files.toArray(new File[0]));

        boolean result = this.library.loadFromDirectory(this.directory.getAbsolutePath());
        assertFalse(result);
        verify(this.logger, times(1)).error("Unable to load dictionary from {version}.");
        assertEquals(4, this.library.getDictionaries().size());
    }

    @Test
    public void testGetDictionaries() {
        when(this.directory.listFiles()).thenReturn(this.files.toArray(new File[0]));

        this.library.loadFromDirectory(this.directory.getAbsolutePath());

        List<DictionaryInfo> dictionaries = this.library.getDictionaries();
        assertEquals(5, dictionaries.size());
        for (DictionaryInfo dictionary : dictionaries) {
            assertNotNull(dictionary);
        }
    }

    @Test
    public void testGetDictionary() {
        when(this.directory.listFiles()).thenReturn(this.files.toArray(new File[0]));

        this.library.loadFromDirectory(this.directory.getAbsolutePath());

        DictionaryInfo dictionary = this.library.getDictionary("4.4");
        assertNotNull(dictionary);
        assertEquals("4.4", dictionary.getVersion());
    }
}
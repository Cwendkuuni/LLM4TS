package org.fixsuite.message;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.fixsuite.message.info.*;
import org.fixsuite.message.parsers.fpl.MainParser;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class LibraryTest {

    private static final String LIBRARY_PATH = "test-data";
    private static final int NUM_DICTIONARIES = 3;
    private static final String[] DICTIONARY_VERSIONS = {"v1.0", "v2.0", "v3.0"};
    private Library library;
    private MainParser parserMock;
    private List<File> arrangedFiles;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        parserMock = mock(MainParser.class);
        library = new Library();
        library.logger = LoggerFactory.getLogger(Library.class);
        arrangedFiles = new ArrayList<>();
    }

    @Test
    public void testLoadFromDirectory() {
        File path = mock(File.class);
        when(path.isDirectory()).thenReturn(true);
        when(path.listFiles()).thenReturn(new File[0]);

        List<File> files = Arrays.asList(new File(LIBRARY_PATH), path);
        when(library.arrangeFiles(files.toArray(new File[0]))).thenReturn(arrangedFiles);
        when(arrangedFiles.get(1)).thenReturn(new File("Fields.xml"));
        when(arrangedFiles.get(2)).thenReturn(new File("Enums.xml"));
        when(arrangedFiles.get(3)).thenReturn(new File("Components.xml"));
        when(arrangedFiles.get(4)).thenReturn(new File("MsgType.xml"));
        when(arrangedFiles.get(5)).thenReturn(new File("MsgContents.xml"));

        when(parserMock.parse(any(DictionaryInfo.class), any(File.class))).thenReturn(true);

        library.loadDirectory(arrangedFiles, LIBRARY_PATH);

        assertEquals(NUM_DICTIONARIES, library.getDictionaries().size());
        for (String version : DICTIONARY_VERSIONS) {
            DictionaryInfo dictionary = library.getDictionary(version);
            assertNotNull(dictionary);
        }
    }

    @Test
    public void testLoadFromDirectory_EmptyDirectory() {
        File path = mock(File.class);
        when(path.isDirectory()).thenReturn(true);
        when(path.listFiles()).thenReturn(new File[0]);

        List<File> files = Arrays.asList(new File(LIBRARY_PATH), path);
        when(library.arrangeFiles(files.toArray(new File[0]))).thenReturn(arrangedFiles);

        library.loadDirectory(arrangedFiles, LIBRARY_PATH);

        assertTrue(library.getDictionaries().isEmpty());
    }

    @Test
    public void testLoadFromDirectory_InvalidPath() {
        File invalidPath = mock(File.class);
        when(invalidPath.isDirectory()).thenReturn(false);

        List<File> files = Arrays.asList(new File(LIBRARY_PATH), invalidPath);
        when(library.arrangeFiles(files.toArray(new File[0]))).thenReturn(arrangedFiles);

        library.loadDirectory(arrangedFiles, LIBRARY_PATH);

        assertTrue(library.getDictionaries().isEmpty());
    }

    @Test(expected = IOException.class)
    public void testLoadFromDirectory_IOException() throws IOException {
        when(parserMock.parse(any(DictionaryInfo.class), any(File.class))).thenThrow(new IOException());

        List<File> arrangedFiles = new ArrayList<>();
        arrangedFiles.add(new File("Fields.xml"));
        arrangedFiles.add(new File("Enums.xml"));
        arrangedFiles.add(new File("Components.xml"));
        arrangedFiles.add(new File("MsgType.xml"));
        arrangedFiles.add(new File("MsgContents.xml"));

        library.loadDirectory(arrangedFiles, LIBRARY_PATH);

        fail("Unexpected result: No IOException was thrown.");
    }
}
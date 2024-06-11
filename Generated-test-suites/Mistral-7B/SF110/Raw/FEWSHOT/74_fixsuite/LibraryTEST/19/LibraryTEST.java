package org.fixsuite.message;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import org.fixsuite.message.info.*;
import org.junit.Test;
import org.mockito.Mockito;

public class LibraryTest {

    @Test
    public void testLoadFromDirectory() {
        String libraryPath = "testData";
        Library library = new Library();
        when(Library.class.getClassLoader().getResource(Library.FIELDS_FILE_NAME)).thenReturn(new File("testData/Fields.xml"));
        when(Library.class.getClassLoader().getResource(Library.ENUMS_FILE_NAME)).thenReturn(new File("testData/Enums.xml"));
        when(Library.class.getClassLoader().getResource(Library.COMPONENTS_FILE_NAME)).thenReturn(new File("testData/Components.xml"));
        when(Library.class.getClassLoader().getResource(Library.MSG_TYPE_FILE_NAME)).thenReturn(new File("testData/MsgType.xml"));
        when(Library.class.getClassLoader().getResource(Library.MSG_CONTENTS_FILE_NAME)).thenReturn(new File("testData/MsgContents.xml"));

        List<File> arrangedFiles = library.arrangeFiles(new File(libraryPath).listFiles());
        List<File> expectedFiles = Arrays.asList(
                new File("testData/Fields.xml"),
                new File("testData/Enums.xml"),
                new File("testData/Components.xml"),
                new File("testData/MsgType.xml"),
                new File("testData/MsgContents.xml")
        );

        assertEquals(expectedFiles, arrangedFiles);

        when(Library.logger.isDebugEnabled()).thenReturn(true);
        library.loadFromDirectory(libraryPath);

        verify(Library.logger, times(1)).debug("Loading " + Library.FIELDS_FILE_NAME);
        verify(Library.logger, times(1)).debug("Loading " + Library.ENUMS_FILE_NAME);
        verify(Library.logger, times(1)).debug("Loading " + Library.COMPONENTS_FILE_NAME);
        verify(Library.logger, times(1)).debug("Loading " + Library.MSG_TYPE_FILE_NAME);
        verify(Library.logger, times(1)).debug("Loading " + Library.MSG_CONTENTS_FILE_NAME);

        List<DictionaryInfo> dictionaries = library.getDictionaries();
        assertFalse(dictionaries.isEmpty());
    }

    @Test
    public void testLoadFromDirectory_emptyDirectory() {
        String libraryPath = "testDataEmpty";
        Library library = new Library();

        List<File> arrangedFiles = library.arrangeFiles(new File(libraryPath).listFiles());
        assertEquals(Collections.emptyList(), arrangedFiles);

        library.loadFromDirectory(libraryPath);

        verify(Library.logger, times(1)).error("Supplied directory is empty.");
        assertTrue(library.dictionaries.isEmpty());
    }

    @Test
    public void testLoadFromDirectory_invalidPath() {
        String libraryPath = "testDataInvalid";
        Library library = new Library();

        File file = new File(libraryPath);
        assertFalse(file.isDirectory());

        library.loadFromDirectory(libraryPath);

        verify(Library.logger, times(1)).error("Supplied path is not a directory.");
        assertTrue(library.dictionaries.isEmpty());
    }

    @Test
    public void testLoadFromDirectory_multipleDirectories() {
        String libraryPath = "testDataMultiple";
        Library library = new Library();

        List<File> files = new ArrayList<>();
        files.add(new File("testDataMultiple/dir1"));
        files.add(new File("testDataMultiple/dir2"));

        List<File> arrangedFiles = library.arrangeFiles(files.toArray(new File[0]));
        List<File> expectedFiles = new ArrayList<>();
        expectedFiles.add(new File("testDataMultiple/dir1/Fields.xml"));
        expectedFiles.add(new File("testDataMultiple/dir1/Enums.xml"));
        expectedFiles.add(new File("testDataMultiple/dir1/Components.xml"));
        expectedFiles.add(new File("testDataMultiple/dir1/MsgType.xml"));
        expectedFiles.add(new File("testDataMultiple/dir1/MsgContents.xml"));
        expectedFiles.add(new File("testDataMultiple/dir2/Fields.xml"));
        expectedFiles.add(new File("testDataMultiple/dir2/Enums.xml"));
        expectedFiles.add(new File("testDataMultiple/dir2/Components.xml"));
        expectedFiles.add(new File("testDataMultiple/dir2/MsgType.xml"));
        expectedFiles.add(new File("testDataMultiple/dir2/MsgContents.xml"));

        Collections.sort(expectedFiles, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });

        assertEquals(expectedFiles, arrangedFiles);

        when(Library.logger.isDebugEnabled()).thenReturn(true);
        library.loadFromDirectory(libraryPath);

        List<DictionaryInfo> dictionaries = library.getDictionaries();
        assertFalse(dictionaries.isEmpty());
    }
}
import org.fixsuite.message.Library;
import org.fixsuite.message.info.DictionaryInfo;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LibraryTest {

    private Library library;
    private List<File> arrangedFiles;
    private File fieldsXmlFile;
    private File enumsXmlFile;
    private File componentsXmlFile;
    private File msgTypeXmlFile;
    private File msgContentsXmlFile;

    @Before
    public void setup() {
        library = new Library();
        arrangedFiles = new ArrayList<>();
        fieldsXmlFile = mock(File.class);
        enumsXmlFile = mock(File.class);
        componentsXmlFile = mock(File.class);
        msgTypeXmlFile = mock(File.class);
        msgContentsXmlFile = mock(File.class);
        when(fieldsXmlFile.getName()).thenReturn("Fields.xml");
        when(enumsXmlFile.getName()).thenReturn("Enums.xml");
        when(componentsXmlFile.getName()).thenReturn("Components.xml");
        when(msgTypeXmlFile.getName()).thenReturn("MsgType.xml");
        when(msgContentsXmlFile.getName()).thenReturn("MsgContents.xml");
        arrangedFiles.add(fieldsXmlFile);
        arrangedFiles.add(enumsXmlFile);
        arrangedFiles.add(componentsXmlFile);
        arrangedFiles.add(msgTypeXmlFile);
        arrangedFiles.add(msgContentsXmlFile);
    }

    @Test
    public void testLoadFromDirectorySuccess() {
        when(library.loadDirectory(arrangedFiles, "VERSION")).thenReturn(true);
        DictionaryInfo dictionary = library.getDictionary("VERSION");
        assertNotNull(dictionary);
        assertEquals("VERSION", dictionary.getVersion());
    }

    @Test
    public void testLoadFromDirectoryFailure() {
        when(library.loadDirectory(arrangedFiles, "VERSION")).thenReturn(false);
        DictionaryInfo dictionary = library.getDictionary("VERSION");
        assertNull(dictionary);
    }

    @Test
    public void testLoadFromDirectoryEmptyDirectory() {
        File[] emptyFiles = new File[0];
        library.loadDirectory(Arrays.asList(emptyFiles), "VERSION");
        assertFalse(library.dictionaries.isEmpty());
        assertNull(library.getDictionary("VERSION"));
    }

    @Test
    public void testLoadFromDirectoryInvalidPath() {
        File invalidPath = mock(File.class);
        when(invalidPath.isDirectory()).thenReturn(false);
        Library.logger.debug("Supplied path is not a directory.");
        library.loadDirectory(Arrays.asList(invalidPath), "VERSION");
        assertFalse(library.dictionaries.isEmpty());
        assertNull(library.getDictionary("VERSION"));
    }
}
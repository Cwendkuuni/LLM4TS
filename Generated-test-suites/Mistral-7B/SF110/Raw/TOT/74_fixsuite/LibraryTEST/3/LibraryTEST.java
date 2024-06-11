import org.fixsuite.message.Library;
import org.fixsuite.message.info.DictionaryInfo;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class LibraryTest {
    private Library library;
    private List<File> arrangedFiles;
    private File directory;

    @Before
    public void setUp() {
        library = new Library();
        directory = new File("testDirectory");
        arrangedFiles = new ArrayList<>();

        Mockito.when(directory.listFiles()).thenAnswer(new Answer<File[]>() {
            @Override
            public File[] answer(InvocationOnMock invocation) throws Throwable {
                return arrangedFiles.toArray(new File[arrangedFiles.size()]);
            }
        });
    }

    @Test
    public void testLoadFromDirectorySuccess() {
        File[] files = {new File("Fields.xml"), new File("Enums.xml"), new File("Components.xml"), new File("MsgType.xml"), new File("MsgContents.xml")};
        arrangedFiles.addAll(List.of(files));

        library.loadFromDirectory(directory.getAbsolutePath());

        List<DictionaryInfo> dictionaries = library.getDictionaries();
        DictionaryInfo dictionary = dictionaries.get(0);

        assertTrue(library.loadFromDirectory(directory.getAbsolutePath()));
        assertEquals(1, dictionaries.size());
        assertNotNull(dictionary);
        assertEquals("TestVersion", dictionary.getVersion());
        assertEquals(5, dictionary.getFiles().size());
    }

    @Test
    public void testLoadFromDirectoryNoFiles() {
        library.loadFromDirectory(directory.getAbsolutePath());

        assertTrue(library.loadFromDirectory(directory.getAbsolutePath()));
        assertFalse(library.getDictionaries().isEmpty());
        assertNull(library.getDictionaries().get(0));
    }

    @Test
    public void testLoadFromDirectoryInvalidDirectory() {
        Mockito.when(directory.isDirectory()).thenReturn(false);

        assertFalse(library.loadFromDirectory(directory.getAbsolutePath()));
    }
}
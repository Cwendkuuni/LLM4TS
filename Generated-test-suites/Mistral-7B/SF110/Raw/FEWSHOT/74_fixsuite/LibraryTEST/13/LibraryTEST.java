package org.fixsuite.message;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.fixsuite.message.info.DictionaryInfo;
import org.fixsuite.message.info.DictionaryVersion;
import org.fixsuite.message.parsers.fpl.MainParser;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;

public class LibraryTest {

    private static final String LIBRARY_PATH = "test_library_path";
    private static final String FIELDS_XML = "Fields.xml";
    private static final String ENUMS_XML = "Enums.xml";
    private static final String COMPONENTS_XML = "Components.xml";
    private static final String MSG_TYPE_XML = "MsgType.xml";
    private static final String MSG_CONTENTS_XML = "MsgContents.xml";

    private Library library;
    private Logger logger;
    private MainParser mainParserMock;

    @Before
    public void setUp() {
        logger = mock(Logger.class);
        mainParserMock = mock(MainParser.class);
        Library.logger = logger;
        library = new Library();
    }

    @Test
    public void testLoadFromDirectory_EmptyDirectory() {
        File path = new File(LIBRARY_PATH);
        when(logger.isDebugEnabled()).thenReturn(true);
        when(logger.debug(String.class)).thenAnswer(invocation -> {});
        library.loadFromDirectory(LIBRARY_PATH);
        verify(logger).error("Supplied directory is empty.");
    }

    @Test
    public void testLoadFromDirectory_InvalidPath() {
        File invalidPath = new File("invalid_path");
        when(logger.isDebugEnabled()).thenReturn(true);
        when(logger.debug(String.class)).thenAnswer(invocation -> {});
        library.loadFromDirectory(invalidPath.getAbsolutePath());
        verify(logger).error("Supplied path is not a directory.");
    }

    @Test
    public void testLoadFromDirectory_NoFiles() {
        File validPath = new File(LIBRARY_PATH);
        when(logger.isDebugEnabled()).thenReturn(true);
        when(logger.debug(String.class)).thenAnswer(invocation -> {});
        when(mainParserMock.parse(ArgumentCaptor.forClass(DictionaryInfo.class), ArgumentCaptor.forClass(File.class))).thenReturn(new DictionaryInfo("version", true));
        when(logger.isErrorEnabled()).thenReturn(true);
        when(logger.error(String.class)).thenAnswer(invocation -> {});

        List<File> arrangedFiles = Arrays.asList(new File(LIBRARY_PATH, FIELDS_XML), new File(LIBRARY_PATH, ENUMS_XML), new File(LIBRARY_PATH, COMPONENTS_XML), new File(LIBRARY_PATH, MSG_TYPE_XML), new File(LIBRARY_PATH, MSG_CONTENTS_XML));
        library.loadFromDirectory(LIBRARY_PATH);
        verify(logger, times(5)).debug(String.format("Loading file: %s", eq(arrangedFiles.get(i))));
        verify(logger, never()).info("Loading time: ...");
        verify(logger, never()).debug("Approximate memory allocation: ...");
    }

    @Test
    public void testLoadFromDirectory_LoadDictionary() throws IOException {
        File validPath = new File(LIBRARY_PATH);
        when(logger.isDebugEnabled()).thenReturn(true);
        when(logger.debug(String.class)).thenAnswer(invocation -> {});
        when(logger.isInfoEnabled()).thenReturn(true);
        when(logger.info(String.class)).thenAnswer(invocation -> {});
        when(logger.isErrorEnabled()).thenReturn(false);

        when(mainParserMock.parse(ArgumentCaptor.forClass(DictionaryInfo.class), ArgumentCaptor.forClass(File.class))).thenAnswer(i -> {
            DictionaryVersion version = i.getArgument(0).getVersion();
            if (version.equals(new DictionaryVersion("v1"))) {
                return new DictionaryInfo("v1", true);
            }
            else if (version.equals(new DictionaryVersion("v2"))) {
                return new DictionaryInfo("v2", false);
            }
            return null;
        });

        List<File> arrangedFiles = Arrays.asList(new File(LIBRARY_PATH, FIELDS_XML), new File(LIBRARY_PATH, ENUMS_XML), new File(LIBRARY_PATH, COMPONENTS_XML), new File(LIBRARY_PATH, MSG_TYPE_XML), new File(LIBRARY_PATH, MSG_CONTENTS_XML));
        FileUtils.writeStringToFile(arrangedFiles.get(0), FileUtils.readFileToString(new File(getClass().getResource("/fields.xml").getFile()), Charset.forName("UTF-8")));
        FileUtils.writeStringToFile(arrangedFiles.get(1), FileUtils.readFileToString(new File(getClass().getResource("/enums.xml").getFile()), Charset.forName("UTF-8")));
        FileUtils.writeStringToFile(arrangedFiles.get(2), FileUtils.readFileToString(new File(getClass().getResource("/components.xml").getFile()), Charset.forName("UTF-8")));
        FileUtils.writeStringToFile(arrangedFiles.get(3), FileUtils.readFileToString(new File(getClass().getResource("/msgType.xml").getFile()), Charset.forName("UTF-8")));
        FileUtils.writeStringToFile(arrangedFiles.get(4), FileUtils.readFileToString(new File(getClass().getResource("/msgContents.xml").getFile()), Charset.forName("UTF-8")));

        library.loadFromDirectory(LIBRARY_PATH);

        verify(logger, times(5)).debug(String.format("Loading file: %s", eq(arrangedFiles.get(i))));
        verify(logger).info("Loading time: ...");
        verify(logger).debug("Approximate memory allocation: ...");
        List<DictionaryInfo> dictionaries = library.getDictionaries();
        assertEquals(2, dictionaries.size());
        assertTrue(dictionaries.get(0).isLoaded());
        assertFalse(dictionaries.get(1).isLoaded());
    }
}
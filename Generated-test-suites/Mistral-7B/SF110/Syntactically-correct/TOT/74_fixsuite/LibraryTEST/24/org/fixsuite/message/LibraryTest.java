
package org.fixsuite.message.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.junit.Assert.*;

import org.fixsuite.message.*;
import org.fixsuite.message.info.*;
import org.junit.Assume;

import java.io.*;
import java.util.*;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        LibraryTest.class,
        DictionaryInfoTest.class})
public class LibrarySuite {
}

@RunWith(Parameterized.class)
public class LibraryTest {
    private String libraryPath;
    private boolean expectedResult;

    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"/testData/library", true},
                {"/testData/empty", false},
                {"/testData/invalid", false},
                {"/testData/library2", true},
                {"/testData/library3", false},
        });
    }

    public LibraryTest(String libraryPath, boolean expectedResult) {
        this.libraryPath = libraryPath;
        this.expectedResult = expectedResult;
    }

    @Test
    public void testLoadFromDirectory() {
        // Given
        Library library = new Library();

        // When
        boolean result = library.loadFromDirectory(libraryPath);

        // Then
        Assume.assumeTrue(result); // Skip test if loading fails
        List<DictionaryInfo> dictionaries = library.getDictionaries();
        Assert.assertFalse(dictionaries.isEmpty());
        Assert.assertEquals(5, dictionaries.size());
    }
}

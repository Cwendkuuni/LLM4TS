package org.apache.commons.compress.archivers.ar;

import org.apache.commons.compress.archivers.ArArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArArchiveTestUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Tests for {@link ArArchiveInputStream}.
 */
public class ArArchiveInputStreamTest {

    private InputStream inputStream;
    private ArArchiveInputStream arArchiveInputStream;

    @Before
    public void setUp() throws Exception {
        String testData = ArArchiveTestUtils.AR_TEST_DATA;
        inputStream = new ByteArrayInputStream(testData.getBytes());
        arArchiveInputStream = new ArArchiveInputStream(inputStream);
    }

    @After
    public void tearDown() throws Exception {
        if (arArchiveInputStream != null) {
            arArchiveInputStream.close();
        }
    }

    @Test
    public void testGetNextArEntry() throws IOException {
        ArchiveEntry entry = arArchiveInputStream.getNextArEntry();
        Assert.assertNotNull(entry);
        Assert.assertTrue(entry instanceof ArArchiveEntry);

        ArArchiveEntry arEntry = (ArArchiveEntry) entry;
        Assert.assertEquals("test1.txt", arEntry.getName());
        Assert.assertEquals(13L, arEntry.getSize());

        entry = arArchiveInputStream.getNextArEntry();
        Assert.assertEquals(null, entry);
    }

    @Test
    public void testGetNextEntry() throws IOException {
        ArchiveEntry entry = arArchiveInputStream.getNextEntry();
        Assert.assertNotNull(entry);
        Assert.assertTrue(entry instanceof ArArchiveEntry);

        ArArchiveEntry arEntry = (ArArchiveEntry) entry;
        Assert.assertEquals("test1.txt", arEntry.getName());
        Assert.assertEquals(13L, arEntry.getSize());

        entry = arArchiveInputStream.getNextEntry();
        Assert.assertEquals(null, entry);
    }

    @Test(expected = IOException.class)
    public void testGetNextArEntryAfterEOF() throws IOException {
        arArchiveInputStream.getNextArEntry(); // consume first entry
        arArchiveInputStream.getNextArEntry(); // consume second entry
        arArchiveInputStream.getNextArEntry(); // should throw an exception
    }

    @Test(expected = IOException.class)
    public void testGetNextEntryAfterEOF() throws IOException {
        arArchiveInputStream.getNextArEntry(); // consume first entry
        arArchiveInputStream.getNextArEntry(); // consume second entry
        arArchiveInputStream.getNextEntry(); // should throw an exception
    }

    @Test
    public void testMatchesSignature() {
        byte[] testData = ArArchiveTestUtils.AR_TEST_DATA.getBytes();
        boolean result = ArArchiveInputStream.matches(testData, testData.length);
        Assert.assertTrue(result);
    }

    @Test
    public void testDoesNotMatchSignature() {
        byte[] testData = new byte[]{'A', 'B', 'C', 'D', 'E', 'F'};
        boolean result = ArArchiveInputStream.matches(testData, testData.length);
        Assert.assertFalse(result);
    }
}
package org.apache.commons.compress.changes;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarEntry;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.compress.utils.TestUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for ChangeSetPerformer
 */
public class ChangeSetPerformerTest {

    private static final String TEST_FILE = "testfile.txt";
    private static final String TEST_DIR = "testdir/";
    private static final String TEST_DIR_IN_TEST_DIR = "testdir/testdir/";
    private static final byte[] TEST_CONTENT = "Test content".getBytes();

    private ChangeSet changeSet;

    @Before
    public void setUp() throws Exception {
        changeSet = TestUtils.createChangeSet();
    }

    @After
    public void tearDown() throws Exception {
        changeSet = null;
    }

    /**
     * Test constructor
     */
    @Test
    public void testConstructor() {
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        assertNull(performer.changes);
    }

    /**
     * Test constructor with valid ChangeSet
     */
    @Test
    public void testConstructorWithValidChangeSet() {
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        assertEquals(changeSet.getChanges().size(), performer.changes.size());
    }

    /**
     * Test perform method with empty ChangeSet
     */
    @Test
    public void testPerformWithEmptyChangeSet() throws IOException {
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        ByteArrayInputStream input = new ByteArrayInputStream(new byte[0]);
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        ChangeSetResults results = performer.perform(new TarArchiveInputStream(input), new TarArchiveOutputStream(output));

        assertNull(results);
    }

    /**
     * Test perform method with InputStream and OutputStream
     */
    @Test
    public void testPerform() throws IOException {
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        ByteArrayInputStream input = new ByteArrayInputStream(TEST_CONTENT);
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        ChangeSetResults results = performer.perform(new TarArchiveInputStream(input), new TarArchiveOutputStream(output));

        byte[] expected = new byte[TEST_CONTENT.length + 2]; // 2 bytes for file header
        System.arraycopy(TEST_CONTENT, 0, expected, 0, TEST_CONTENT.length);
        TarEntry entry = new TarEntry(TEST_FILE);
        entry.setSize(TEST_CONTENT.length);
        InputStream inputStream = new ByteArrayInputStream(expected);
        ArchiveEntry archiveEntry = new TarEntry(TEST_FILE);
        IOUtils.copy(inputStream, output);
        output.flush();

        assertArrayEquals( Arrays.copyOfRange(expected, 0, expected.length), results.getResult() );
        assertEquals(1, results.getAddedFromChangeSet().size());
        assertEquals(TEST_FILE, results.getAddedFromChangeSet().iterator().next());
        assertEquals(1, results.getAddedFromStream().size());
        assertEquals(TEST_FILE, results.getAddedFromStream().iterator().next());
    }

    /**
     * Test perform method with InputStream and OutputStream with an existing file
     */
    @Test
    public void testPerformWithExistingFile() throws IOException {
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        ByteArrayInputStream input = new ByteArrayInputStream(TEST_CONTENT);
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        ChangeSetResults results = performer.perform(new TarArchiveInputStream(input), new TarArchiveOutputStream(output));

        ByteArrayInputStream input2 = new ByteArrayInputStream(TEST_CONTENT);
        ByteArrayOutputStream output2 = new ByteArrayOutputStream();

        ChangeSetResults results2 = performer.perform(new TarArchiveInputStream(input2), new TarArchiveOutputStream(output2));

        assertNull(results2.getResult());
        assertArrayEquals( Arrays.copyOfRange(TEST_CONTENT, 0, TEST_CONTENT.length), results.getResult() );
        assertEquals(1, results.getAddedFromChangeSet().size());
        assertEquals(TEST_FILE, results.getAddedFromChangeSet().iterator().next());
        assertEquals(1, results.getAddedFromStream().size());
        assertEquals(TEST_FILE, results.getAddedFromStream().iterator().next());
    }

    /**
     * Test perform method with InputStream and OutputStream with a deleted file
     */
    @Test
    public void testPerformWithDeletedFile() throws IOException {
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        ByteArrayInputStream input = new ByteArrayInputStream(new byte[0]);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ChangeSet changeSetWithDelete = TestUtils.createChangeSetWithDelete(TEST_FILE);
        performer = new ChangeSetPerformer(changeSetWithDelete);

        ChangeSetResults results = performer.perform(new TarArchiveInputStream(input), new TarArchiveOutputStream(output));

        assertNull(results.getResult());
        assertFalse(results.getAddedFromChangeSet().isEmpty());
        assertFalse(results.getAddedFromStream().isEmpty());
    }

    /**
     * Test perform method with InputStream and OutputStream with a replaced file
     */
    @Test
    public void testPerformWithReplacedFile() throws IOException {
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        ByteArrayInputStream input = new ByteArrayInputStream(TEST_CONTENT);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ChangeSet changeSetWithReplace = TestUtils.createChangeSetWithReplace(TEST_FILE);
        performer = new ChangeSetPerformer(changeSetWithReplace);

        ChangeSetResults results = performer.perform(new TarArchiveInputStream(input), new TarArchiveOutputStream(output));

        assertArrayEquals( Arrays.copyOfRange(TEST_CONTENT, 0, TEST_CONTENT.length), results.getResult() );
        assertEquals(1, results.getAddedFromChangeSet().size());
        assertEquals(TEST_FILE, results.getAddedFromChangeSet().iterator().next());
        assertEquals(1, results.getAddedFromStream().size());
        assertEquals(TEST_FILE, results.getAddedFromStream().iterator().next());
    }

    /**
     * Test perform method with InputStream and OutputStream with a directory
     */
    @Test
    public void testPerformWithDirectory() throws IOException {
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        ByteArrayInputStream input = new ByteArrayInputStream(TEST_CONTENT);
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        ChangeSet changeSetWithDirectory = TestUtils.createChangeSetWithDirectory(TEST_DIR);
        performer = new ChangeSetPerformer(changeSetWithDirectory);

        TarArchiveInputStream archiveInputStream = new TarArchiveInputStream(new ByteArrayInputStream(TestUtils.getTestArchiveWithDirectory(TEST_DIR).toByteArray()));
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(output);
        ChangeSetResults results = performer.perform(archiveInputStream, tarArchiveOutputStream);

        TarEntry testDirEntry = archiveInputStream.getNextEntry();
        assertEquals(TEST_DIR, testDirEntry.getName());
        assertNull(testDirEntry.getSize());
        assertNull(results.getResult());
        assertEquals(1, results.getAddedFromChangeSet().size());
        assertEquals(TEST_DIR, results.getAddedFromChangeSet().iterator().next());
        assertEquals(1, results.getAddedFromStream().size());
        assertEquals(TEST_DIR, results.getAddedFromStream().iterator().next());
    }

    /**
     * Test perform method with InputStream and OutputStream with a directory and a file
     */
    @Test
    public void testPerformWithDirectoryAndFile() throws IOException {
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        ByteArrayInputStream input = new ByteArrayInputStream(TEST_CONTENT);
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        ChangeSet changeSetWithDirectoryAndFile = TestUtils.createChangeSetWithDirectoryAndFile(TEST_DIR, TEST_FILE);
        performer = new ChangeSetPerformer(changeSetWithDirectoryAndFile);

        TarArchiveInputStream archiveInputStream = new TarArchiveInputStream(new ByteArrayInputStream(TestUtils.getTestArchiveWithDirectoryAndFile(TEST_DIR, TEST_FILE).toByteArray()));
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(output);
        ChangeSetResults results = performer.perform(archiveInputStream, tarArchiveOutputStream);

        TarEntry testDirEntry = archiveInputStream.getNextEntry();
        assertEquals(TEST_DIR, testDirEntry.getName());
        assertNull(testDirEntry.getSize());

        TarEntry testFileEntry = archiveInputStream.getNextEntry();
        assertEquals(TEST_FILE, testFileEntry.getName());
        assertEquals(TEST_CONTENT.length, testFileEntry.getSize());

        assertNull(results.getResult());
        assertEquals(1, results.getAddedFromChangeSet().size());
        assertEquals(TEST_DIR, results.getAddedFromChangeSet().iterator().next());
        assertEquals(1, results.getAddedFromStream().size());
        assertEquals(TEST_FILE, results.getAddedFromStream().iterator().next());
    }

    /**
     * Test perform method with InputStream and OutputStream with a directory and a deleted file
     */
    @Test
    public void testPerformWithDirectoryAndDeletedFile() throws IOException {
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        ByteArrayInputStream input = new ByteArrayInputStream(new byte[0]);
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        ChangeSet changeSetWithDirectoryAndDeletedFile = TestUtils.createChangeSetWithDirectoryAndDeletedFile(TEST_DIR, TEST_FILE);
        performer = new ChangeSetPerformer(changeSetWithDirectoryAndDeletedFile);

        TarArchiveInputStream archiveInputStream = new TarArchiveInputStream(new ByteArrayInputStream(TestUtils.getTestArchiveWithDirectoryAndDeletedFile(TEST_DIR, TEST_FILE).toByteArray()));
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(output);

        try {
            performer.perform(archiveInputStream, tarArchiveOutputStream);
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }

        assertEquals(1, archiveInputStream.getNextEntry() == null ? 1 : 0);
        assertNull(results.getResult());
        assertEquals(Collections.emptySet(), results.getAddedFromChangeSet());
        assertEquals(Collections.emptySet(), results.getAddedFromStream());
    }

    /**
     * Test perform method with InputStream and OutputStream with a directory and a replaced file
     */
    @Test
    public void testPerformWithDirectoryAndReplacedFile() throws IOException {
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        ByteArrayInputStream input = new ByteArrayInputStream(new byte[0]);
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        ChangeSet changeSetWithDirectoryAndReplacedFile = TestUtils.createChangeSetWithDirectoryAndReplacedFile(TEST_DIR, TEST_FILE);
        performer = new ChangeSetPerformer(changeSetWithDirectoryAndReplacedFile);

        TarArchiveInputStream archiveInputStream = new TarArchiveInputStream(new ByteArrayInputStream(TestUtils.getTestArchiveWithDirectoryAndReplacedFile(TEST_DIR, TEST_FILE).toByteArray()));
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(output);

        ChangeSetResults results = performer.perform(archiveInputStream, tarArchiveOutputStream);

        TarEntry testDirEntry = archiveInputStream.getNextEntry();
        assertEquals(TEST_DIR, testDirEntry.getName());
        assertNull(testDirEntry.getSize());

        TarEntry testFileEntry = archiveInputStream.getNextEntry();
        assertEquals(TEST_FILE, testFileEntry.getName());
        assertEquals(TEST_CONTENT.length, testFileEntry.getSize());

        assertArrayEquals( Arrays.copyOfRange(TEST_CONTENT, 0, TEST_CONTENT.length), results.getResult() );
        assertEquals(1, results.getAddedFromChangeSet().size());
        assertEquals(TEST_DIR, results.getAddedFromChangeSet().iterator().next());
        assertEquals(1, results.getAddedFromStream().size());
        assertEquals(TEST_FILE, results.getAddedFromStream().iterator().next());
    }

    /**
     * Test isDeletedLater method
     */
    @Test
    public void testIsDeletedLater() {
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        Set<Change> changes = performer.changes;
        Changes tests = new Changes();
        tests.add(new Change(Change.TYPE_ADD, TEST_FILE));
        tests.add(new Change(Change.TYPE_DELETE, TEST_FILE));

        Set<Change> set = new LinkedHashSet<>();
        set.addAll(changes);
        set.addAll(tests.getChanges());

        assertTrue(performer.isDeletedLater(new TarEntry(TEST_FILE), set));
        assertTrue(performer.isDeletedLater(new TarEntry(TEST_DIR + TEST_FILE), set));
        assertFalse(performer.isDeletedLater(new TarEntry(TEST_DIR + "testfile.txt"), set));
        assertFalse(performer.isDeletedLater(new TarEntry(TEST_DIR + "testfile2.txt"), set));
    }

    /**
     * Test copyStream method
     */
    @Test
    public void testCopyStream() throws IOException {
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        InputStream input = new ByteArrayInputStream(TEST_CONTENT);
        ArchiveEntry entry = new TarEntry(TEST_FILE);

        performer.copyStream(input, new TarArchiveOutputStream(output), entry);

        byte[] expected = new byte[TEST_CONTENT.length + 2]; // 2 bytes for file header
        System.arraycopy(TEST_CONTENT, 0, expected, 0, TEST_CONTENT.length);
        InputStream inputStream = new ByteArrayInputStream(expected);

        byte[] result = output.toByteArray();

        assertArrayEquals(expected, result);
    }
}
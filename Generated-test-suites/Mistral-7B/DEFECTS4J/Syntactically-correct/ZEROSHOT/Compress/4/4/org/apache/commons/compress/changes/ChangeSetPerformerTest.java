package org.apache.commons.compress.changes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarInputStream;
import org.apache.commons.compress.archivers.tar.TarOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.compress.utils.TestUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for ChangeSetPerformer
 *
 */
public class ChangeSetPerformerTest {

    private ChangeSet changeSet;
    private ChangeSetPerformer performer;
    private ArchiveInputStream inputStream;
    private ArchiveOutputStream outputStream;
    private ByteArrayOutputStream buffer;

    @Before
    public void setUp() throws IOException {
        changeSet = new ChangeSet();

        // Add changes
        changeSet.addChange(new Change(Change.TYPE_ADD, "file1.txt", null,
                IOUtils.toInputStream("Test data for file1.".getBytes())));
        changeSet.addChange(new Change(Change.TYPE_DELETE, "file1.txt", null, null));
        changeSet.addChange(new Change(Change.TYPE_ADD, "file2.txt", null,
                IOUtils.toInputStream("Test data for file2.".getBytes())));
        changeSet.addChange(new Change(Change.TYPE_ADD, "dir1/file3.txt", null,
                IOUtils.toInputStream("Test data for file3.".getBytes())));
        changeSet.addChange(new Change(Change.TYPE_DELETE_DIR, "dir1", null, null));

        // Create performer
        performer = new ChangeSetPerformer(changeSet);

        // Create input and output streams
        buffer = new ByteArrayOutputStream();
        outputStream = new TarOutputStream(buffer);
        inputStream = new TarInputStream(new ByteArrayInputStream(buffer.toByteArray()));
    }

    @After
    public void tearDown() throws IOException {
        IOUtils.closeQuietly(inputStream);
        IOUtils.closeQuietly(outputStream);
        IOUtils.closeQuietly(buffer.toOutputStream());
        IOUtils.closeQuietly(changeSet.getInput());
        IOUtils.closeQuietly(changeSet.getOutput());
    }

    /**
     * Test perform method
     *
     * @throws IOException
     */
    @Test
    public void testPerform() throws IOException {
        // Perform changes
        ChangeSetResults results = performer.perform(inputStream, outputStream);

        // Check added files from change set
        ArchiveEntry entry1 = outputStream.getNextArchiveEntry();
        assertNotNull(entry1);
        assertEquals("file1.txt", entry1.getName());
        assertTrue(results.hasBeenAdded("file1.txt"));

        entry1 = outputStream.getNextArchiveEntry();
        assertNotNull(entry1);
        assertEquals("file2.txt", entry1.getName());
        assertTrue(results.hasBeenAdded("file2.txt"));

        // Check deleted file
        ArchiveEntry entry2 = inputStream.getNextEntry();
        assertNull(entry2);

        // Check deleted directory
        assertFalse(outputStream.hasNextEntry());

        // Check added files from stream
        entry1 = outputStream.getNextArchiveEntry();
        assertNotNull(entry1);
        assertEquals("file3.txt", entry1.getName());
        assertTrue(results.hasBeenAdded("dir1/file3.txt"));

        // Check not added files
        ArchiveEntry entry3 = inputStream.getNextEntry();
        assertNotNull(entry3);
        assertEquals("file1.txt", entry3.getName());
        assertFalse(results.hasBeenAdded("file1.txt"));

        // Check results
        assertEquals(2, results.countAddedFromChangeSet());
        assertEquals(1, results.countAddedFromStream());
        assertEquals(1, results.countDeleted());
        assertEquals(1, results.countDeletedDirs());
    }

    /**
     * Test isDeletedLater method
     */
    @Test
    public void testIsDeletedLater() {
        // Test file
        Set changes = new LinkedHashSet(Arrays.asList(
                new Change(Change.TYPE_ADD, "file1.txt", null, null),
                new Change(Change.TYPE_DELETE, "file1.txt", null, null)));
        ChangeSetPerformer performer = new ChangeSetPerformer(new ChangeSet(changes));
        ArchiveEntry entry = new TarEntry("file1.txt");
        assertTrue(performer.isDeletedLater(changes, entry));

        // Test folder
        changes.add(new Change(Change.TYPE_DELETE_DIR, "folder", null, null));
        assertTrue(performer.isDeletedLater(changes, new TarEntry("folder/file.txt")));

        // Test different name
        assertFalse(performer.isDeletedLater(changes, new TarEntry("file2.txt")));

        // Test empty change set
        changes.clear();
        assertFalse(performer.isDeletedLater(changes, new TarEntry("file1.txt")));
    }
}
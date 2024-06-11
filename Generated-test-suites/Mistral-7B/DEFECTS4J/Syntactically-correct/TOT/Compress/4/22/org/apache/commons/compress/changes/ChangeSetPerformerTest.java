package org.apache.commons.compress.changes;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipEntry;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test class for ChangeSetPerformer
 */
public class ChangeSetPerformerTest {

    private ChangeSet changeSet;
    private byte[] inputData;
    private InputStream inputStream;
    private ArchiveInputStream archiveInput;
    private ArchiveOutputStream archiveOutput;

    @Before
    public void setup() throws IOException {
        changeSet = new ChangeSet();
        inputData = new byte[10];
        Arrays.fill(inputData, (byte) 'a');
        inputStream = new ByteArrayInputStream(inputData);
        archiveInput = new ZipFileArchiveInputStream(new ByteArrayInputStream(new byte[0]), true);
        archiveOutput = new ZipFileArchiveOutputStream(new ByteArrayOutputStream(), true);
    }

    @After
    public void teardown() throws IOException {
        IOUtils.closeQuietly(archiveInput);
        IOUtils.closeQuietly(inputStream);
        IOUtils.closeQuietly(archiveOutput);
        changeSet = null;
    }

    @Test
    public void testPerformAddReplaceMode() throws IOException {
        Change addChange = new Change("test.txt", Change.TYPE_ADD, Change.REPLACE_MODE);
        changeSet.addChange(addChange);
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        ArchiveEntry addEntry = new ZipEntry("test.txt");
        InputStream addInput = new ByteArrayInputStream(inputData);

        ChangeSetResults results = performer.perform(archiveInput, archiveOutput);
        ArchiveEntry outputEntry = archiveOutput.getNextEntry();
        assertTrue(results.hasBeenAdded("test.txt"));
        assertEquals(inputData, IOUtils.toByteArray(archiveOutput.getInputStream()));
        assertEquals(addEntry.getName(), outputEntry.getName());

        // Cleanup
        archiveOutput.close();
        IOUtils.closeQuietly(archiveOutput);
    }

    @Test
    public void testPerformAddNotReplaceMode() throws IOException {
        Change addChange = new Change("test.txt", Change.TYPE_ADD);
        changeSet.addChange(addChange);
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        ArchiveEntry addEntry = new ZipEntry("test.txt");
        InputStream addInput = new ByteArrayInputStream(inputData);

        ChangeSetResults results = performer.perform(archiveInput, archiveOutput);
        ArchiveEntry outputEntry = archiveOutput.getNextEntry();
        assertTrue(results.hasBeenAdded("test.txt"));
        assertEquals(inputData, IOUtils.toByteArray(archiveOutput.getInputStream()));
        assertEquals(addEntry.getName(), outputEntry.getName());

        // Cleanup
        archiveOutput.close();
        IOUtils.closeQuietly(archiveOutput);
    }

    @Test
    public void testPerformDelete() throws IOException {
        Change addChange = new Change("test.txt", Change.TYPE_ADD);
        changeSet.addChange(addChange);

        Change deleteChange = new Change("test.txt", Change.TYPE_DELETE);
        changeSet.addChange(deleteChange);
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        ArchiveEntry addEntry = new ZipEntry("test.txt");
        InputStream addInput = new ByteArrayInputStream(inputData);
        archiveInput.putNextEntry(addEntry);
        IOUtils.copy(addInput, archiveInput);
        archiveInput.closeArchiveEntry();

        ChangeSetResults results = performer.perform(archiveInput, archiveOutput);
        ArchiveEntry outputEntry = archiveInput.getNextEntry();
        assertFalse(results.hasBeenAdded("test.txt"));
        assertFalse(results.hasBeenDeleted("test.txt"));
        assertNull(archiveOutput.getNextEntry());

        // Cleanup
        archiveOutput.close();
        IOUtils.closeQuietly(archiveOutput);
    }

    @Test
    public void testPerformDeleteDir() throws IOException {
        Change addChange1 = new Change("test/dir1", Change.TYPE_ADD);
        changeSet.addChange(addChange1);

        Change addChange2 = new Change("test/dir1/file1.txt", Change.TYPE_ADD);
        changeSet.addChange(addChange2);

        Change deleteChange = new Change("test/dir1", Change.TYPE_DELETE_DIR);
        changeSet.addChange(deleteChange);
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        ArchiveEntry addEntry1 = new ZipEntry("test/dir1/");
        InputStream addInput1 = new ByteArrayInputStream(new byte[0]);
        archiveInput.putNextEntry(addEntry1);
        IOUtils.copy(addInput1, archiveInput);
        archiveInput.closeArchiveEntry();

        ArchiveEntry addEntry2 = new ZipEntry("test/dir1/file1.txt");
        InputStream addInput2 = new ByteArrayInputStream(inputData);
        archiveInput.putNextEntry(addEntry2);
        IOUtils.copy(addInput2, archiveInput);
        archiveInput.closeArchiveEntry();

        ChangeSetResults results = performer.perform(archiveInput, archiveOutput);
        ArchiveEntry outputEntry1 = archiveInput.getNextEntry();
        ArchiveEntry outputEntry2 = archiveInput.getNextEntry();
        assertFalse(results.hasBeenAdded("test/dir1/file1.txt"));
        assertFalse(results.hasBeenAdded("test/dir1"));
        assertNull(archiveOutput.getNextEntry());
        assertEquals("test/dir1/", outputEntry1.getName());
        assertEquals("test/dir1/file1.txt", outputEntry2.getName());

        // Cleanup
        archiveOutput.close();
        IOUtils.closeQuietly(archiveOutput);
    }

    @Test
    public void testPerformAddAndDelete() throws IOException {
        Change addChange = new Change("test.txt", Change.TYPE_ADD);
        changeSet.addChange(addChange);

        Change deleteChange = new Change("test.txt", Change.TYPE_DELETE);
        changeSet.addChange(deleteChange);
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        ArchiveEntry addEntry = new ZipEntry("test.txt");
        InputStream addInput = new ByteArrayInputStream(inputData);
        archiveInput.putNextEntry(addEntry);
        IOUtils.copy(addInput, archiveInput);
        archiveInput.closeArchiveEntry();

        ChangeSetResults results = performer.perform(archiveInput, archiveOutput);
        ArchiveEntry outputEntry = archiveInput.getNextEntry();
        assertFalse(results.hasBeenAdded("test.txt"));
        assertFalse(results.hasBeenDeleted("test.txt"));
        assertNull(archiveOutput.getNextEntry());

        // Cleanup
        archiveOutput.close();
        IOUtils.closeQuietly(archiveOutput);
    }

    @Test(expected = IllegalStateException.class)
    public void testPerformAfterEntriesAdded() throws IOException {
        Change addChange = new Change("test.txt", Change.TYPE_ADD);
        changeSet.addChange(addChange);
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        ArchiveEntry addEntry = new ZipEntry("test.txt");
        InputStream addInput = new ByteArrayInputStream(inputData);
        archiveInput.putNextEntry(addEntry);
        IOUtils.copy(addInput, archiveInput);
        archiveInput.closeArchiveEntry();

        ChangeSetResults results = performer.perform(archiveInput, archiveOutput);

        // This should throw an IllegalStateException as perform finishes the stream
        Change addChange2 = new Change("test2.txt", Change.TYPE_ADD);
        changeSet.addChange(addChange2);
    }
}
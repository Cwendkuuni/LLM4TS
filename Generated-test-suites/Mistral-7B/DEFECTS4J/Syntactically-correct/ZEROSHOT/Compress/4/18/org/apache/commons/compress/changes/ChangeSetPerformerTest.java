package org.apache.commons.compress.changes;

import static org.junit.Assert.*;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Test class for ChangeSetPerformer
 */
public class ChangeSetPerformerTest {

    private ChangeSet changeSet;
    private ChangeSetResults result;
    private InputStream inputStream;
    private ArchiveInputStream archiveInput;
    private ArchiveOutputStream archiveOutput;
    private ChangeSetPerformer performer;

    @Before
    public void setUp() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream archiveOutputStream = new TarArchiveOutputStream(byteArrayOutputStream);
        archiveOutputStream.putNextEntry(new TarArchiveEntry("file1.txt"));
        archiveOutputStream.write("Test file 1".getBytes());
        archiveOutputStream.closeArchiveEntry();
        archiveOutputStream.finish();
        inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        archiveInput = new TarArchiveInputStream(inputStream);
        changeSet = new ChangeSet();
        Change addChange = new Change(Change.TYPE_ADD, "file1.txt", null, null);
        Change replaceChange = new Change(Change.TYPE_ADD, "file1.txt", "file2.txt", null);
        replaceChange.setReplaceMode(true);
        Change deleteChange = new Change(Change.TYPE_DELETE, "file1.txt", null, null);
        Change deleteDirChange = new Change(Change.TYPE_DELETE_DIR, "dir1", null, null);
        changeSet.addChange(addChange);
        changeSet.addChange(replaceChange);
        changeSet.addChange(deleteChange);
        changeSet.addChange(deleteDirChange);
        performer = new ChangeSetPerformer(changeSet);
        result = new ChangeSetResults();
    }

    @Test
    public void testPerform_AddAndDelete() throws IOException {
        performer.perform(archiveInput, archiveOutput);
        ArchiveEntry entry = archiveInput.getNextEntry();
        assertEquals("file1.txt", entry.getName());
        assertNull(entry);
        assertEquals(2, result.getAddedFromChangeSet().size());
        assertEquals(1, result.getDeleted().size());
    }

    @Test
    public void testPerform_AddAndReplace() throws IOException {
        performer.perform(archiveInput, archiveOutput);
        ArchiveEntry entry = archiveInput.getNextEntry();
        assertNull(entry);
        assertEquals(2, result.getAddedFromChangeSet().size());
        assertEquals(0, result.getDeleted().size());

        ArchiveEntry outputEntry = archiveOutput.getNextEntry();
        assertEquals("file1.txt", outputEntry.getName());
        assertNotNull(outputEntry);
        assertEquals("file2.txt", outputEntry.getName());
    }

    @Test
    public void testPerform_DeleteAndAdd() throws IOException {
        performer.perform(archiveInput, archiveOutput);
        ArchiveEntry entry = archiveInput.getNextEntry();
        assertNull(entry);
        assertEquals(0, result.getAddedFromChangeSet().size());
        assertEquals(1, result.getDeleted().size());

        ArchiveEntry outputEntry = archiveOutput.getNextEntry();
        assertNull(outputEntry);
    }

    @Test
    public void testPerform_AddAndDeleteDir() throws IOException {
        performer.perform(archiveInput, archiveOutput);
        ArchiveEntry entry = archiveInput.getNextEntry();
        assertNull(entry);
        assertEquals(1, result.getDeleted().size());
        assertEquals(0, result.getAddedFromChangeSet().size());
    }

    @Test
    public void testPerform_AddAndDeleteAndReplace() throws IOException {
        performer.perform(archiveInput, archiveOutput);
        ArchiveEntry entry = archiveInput.getNextEntry();
        assertNull(entry);
        assertEquals(1, result.getAddedFromChangeSet().size());
        assertEquals(1, result.getDeleted().size());
        assertEquals(0, result.getDeletedFromStream().size());

        ArchiveEntry outputEntry = archiveOutput.getNextEntry();
        assertEquals("file1.txt", outputEntry.getName());
        assertNotNull(outputEntry);
    }

    @Test
    public void testPerform_MultipleFiles() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream archiveOutputStream = new TarArchiveOutputStream(byteArrayOutputStream);
        archiveOutputStream.putNextEntry(new TarArchiveEntry("file1.txt"));
        archiveOutputStream.write("Test file 1".getBytes());
        archiveOutputStream.putNextEntry(new TarArchiveEntry("file2.txt"));
        archiveOutputStream.write("Test file 2".getBytes());
        archiveOutputStream.finish();
        inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        archiveInput = new TarArchiveInputStream(inputStream);

        performer = new ChangeSetPerformer(changeSet);
        performer.perform(archiveInput, archiveOutput);

        assertEquals(2, result.getAddedFromChangeSet().size());
        assertEquals(2, result.getAddedFromStream().size());
        assertEquals(0, result.getDeleted().size());
        assertEquals(0, result.getDeletedFromStream().size());
    }

    @Test
    public void testPerform_MultipleFilesWithDeletion() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream archiveOutputStream = new TarArchiveOutputStream(byteArrayOutputStream);
        archiveOutputStream.putNextEntry(new TarArchiveEntry("file1.txt"));
        archiveOutputStream.write("Test file 1".getBytes());
        archiveOutputStream.putNextEntry(new TarArchiveEntry("file2.txt"));
        archiveOutputStream.write("Test file 2".getBytes());
        archiveOutputStream.finish();
        inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        archiveInput = new TarArchiveInputStream(inputStream);

        performer = new ChangeSetPerformer(changeSet);
        performer.perform(archiveInput, archiveOutput);

        assertEquals(1, result.getAddedFromChangeSet().size());
        assertEquals(1, result.getAddedFromStream().size());
        assertEquals(1, result.getDeleted().size());
        assertEquals(0, result.getDeletedFromStream().size());

        ArchiveEntry entry = archiveInput.getNextEntry();
        assertNull(entry);
    }

    @Test
    public void testPerform_MultipleFilesWithDeletionAndReplace() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream archiveOutputStream = new TarArchiveOutputStream(byteArrayOutputStream);
        archiveOutputStream.putNextEntry(new TarArchiveEntry("file1.txt"));
        archiveOutputStream.write("Test file 1".getBytes());
        archiveOutputStream.putNextEntry(new TarArchiveEntry("file2.txt"));
        archiveOutputStream.write("Test file 2".getBytes());
        archiveOutputStream.finish();
        inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        archiveInput = new TarArchiveInputStream(inputStream);

        performer = new ChangeSetPerformer(changeSet);
        performer.perform(archiveInput, archiveOutput);

        assertEquals(1, result.getAddedFromChangeSet().size());
        assertEquals(1, result.getAddedFromStream().size());
        assertEquals(1, result.getDeleted().size());
        assertEquals(0, result.getDeletedFromStream().size());

        ArchiveEntry outputEntry = archiveOutput.getNextEntry();
        assertEquals("file1.txt", outputEntry.getName());
        assertNotNull(outputEntry);
    }

    @Test
    public void testPerform_MultipleFilesWithDeletionAndReplaceAndAdd() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream archiveOutputStream = new TarArchiveOutputStream(byteArrayOutputStream);
        archiveOutputStream.putNextEntry(new TarArchiveEntry("file1.txt"));
        archiveOutputStream.write("Test file 1".getBytes());
        archiveOutputStream.putNextEntry(new TarArchiveEntry("file2.txt"));
        archiveOutputStream.write("Test file 2".getBytes());
        archiveOutputStream.finish();
        inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        archiveInput = new TarArchiveInputStream(inputStream);

        performer = new ChangeSetPerformer(changeSet);
        performer.perform(archiveInput, archiveOutput);

        assertEquals(2, result.getAddedFromChangeSet().size());
        assertEquals(2, result.getAddedFromStream().size());
        assertEquals(1, result.getDeleted().size());
        assertEquals(0, result.getDeletedFromStream().size());

        ArchiveEntry outputEntry = archiveOutput.getNextEntry();
        assertEquals("file1.txt", outputEntry.getName());
        assertNotNull(outputEntry);
        ArchiveEntry outputEntry2 = archiveOutput.getNextEntry();
        assertEquals("file2.txt", outputEntry2.getName());
        assertNotNull(outputEntry2);
    }

    @Test
    public void testPerform_EmptyChangeSet() throws IOException {
        changeSet = new ChangeSet();
        performer = new ChangeSetPerformer(changeSet);

        performer.perform(archiveInput, archiveOutput);

        assertEquals(0, result.getAddedFromChangeSet().size());
        assertEquals(0, result.getAddedFromStream().size());
        assertEquals(0, result.getDeleted().size());
        assertEquals(0, result.getDeletedFromStream().size());
        assertEquals(0, archiveOutput.size());
    }
}
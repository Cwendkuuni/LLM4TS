package org.apache.commons.compress.changes;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for ChangeSetPerformer
 */
public class ChangeSetPerformerTest {

    private InputStream inputStream;
    private ArchiveInputStream archiveInput;
    private ArchiveOutputStream archiveOutput;
    private ChangeSet changeSet;

    @Before
    public void setUp() throws Exception {
        InputStream testInput = new ByteArrayInputStream(
                "TestFileContent".getBytes());
        changeSet = mock(ChangeSet.class);
        Set<Change> changes = new HashSet<Change>();
        Change addChange = new Change("TestFile.txt", Change.TYPE_ADD,
                false, new ByteArrayInputStream("TestFileContent".getBytes()));
        Change deleteChange = new Change("TestFile.txt", Change.TYPE_DELETE,
                null, null);
        changes.add(addChange);
        changes.add(deleteChange);
        when(changeSet.getChanges()).thenReturn(changes);

        inputStream = testInput;
        archiveInput = new ArchiveInputStream(inputStream,
                CompressorStreamFactory.create("identity"));
        archiveOutput = new ArchiveOutputStream("test.tar",
                CompressorStreamFactory.create("identity"));
    }

    @After
    public void tearDown() throws Exception {
        IOUtils.closeQuietly(archiveInput);
        IOUtils.closeQuietly(archiveOutput);
        IOUtils.closeQuietly(inputStream);
    }

    @Test
    public void testPerformAddFile() throws Exception {
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        ChangeSetResults result = performer.perform(archiveInput, archiveOutput);

        ArchiveEntry entry = archiveInput.getNextEntry();
        assertTrue(result.hasBeenAdded("TestFile.txt"));
        assertTrue(result.hasBeenAdded("TestFile.txt"));
        // Verify the file was written to the output
        byte[] expectedContent = "TestFileContent".getBytes();
        byte[] actualContent = IOUtils.toByteArray(archiveOutput
                .getInputStream(new ArchiveEntry("TestFile.txt")));
        assertArrayEquals(expectedContent, actualContent);
    }

    @Test
    public void testPerformDeleteFile() throws Exception {
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        ChangeSetResults result = performer.perform(archiveInput, archiveOutput);

        ArchiveEntry entry = archiveInput.getNextEntry();
        assertTrue(result.hasBeenDeleted("TestFile.txt"));
        assertFalse(result.hasBeenAdded("TestFile.txt"));
        // Verify the file was not written to the output
        assertTrue(archiveOutput.getNextArchiveEntry() == null);
    }

    @Test
    public void testPerformDeleteDirectory() throws Exception {
        Change set = mock(ChangeSet.class);
        Set<Change> changes = new HashSet<Change>();
        Change deleteDirectoryChange = new Change("TestDir/", Change.TYPE_DELETE_DIR,
                null, null);
        changes.add(deleteDirectoryChange);
        when(set.getChanges()).thenReturn(changes);

        ChangeSetPerformer performer = new ChangeSetPerformer(set);
        ChangeSetResults result = performer.perform(archiveInput, archiveOutput);

        ArchiveEntry entry = archiveInput.getNextEntry();
        while (entry != null) {
            if (entry.isDirectory()) {
                assertTrue(result.hasBeenDeleted("TestDir/"));
                assertFalse(result.hasBeenAdded("TestDir/"));
                // Verify the directory was not written to the output
                assertTrue(archiveOutput.getNextArchiveEntry() == null);
                break;
            }
            entry = archiveInput.getNextEntry();
        }
    }

    @Test
    public void testPerformMultipleChanges() throws Exception {
        Change addChange1 = new Change("TestFile1.txt", Change.TYPE_ADD,
                false, new ByteArrayInputStream("TestFileContent1".getBytes()));
        Change addChange2 = new Change("TestFile2.txt", Change.TYPE_ADD,
                false, new ByteArrayInputStream("TestFileContent2".getBytes()));
        Change deleteChange = new Change("TestFile1.txt", Change.TYPE_DELETE,
                null, null);
        Set<Change> changes = new HashSet<Change>();
        changes.add(addChange1);
        changes.add(addChange2);
        changes.add(deleteChange);
        when(changeSet.getChanges()).thenReturn(changes);

        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        ChangeSetResults result = performer.perform(archiveInput, archiveOutput);

        ArchiveEntry entry1 = archiveInput.getNextEntry();
        assertTrue(result.hasBeenAdded("TestFile1.txt"));
        assertTrue(result.hasBeenAdded("TestFile1.txt"));
        ArchiveEntry entry2 = archiveInput.getNextEntry();
        assertTrue(result.hasBeenAdded("TestFile2.txt"));
        assertTrue(result.hasBeenAdded("TestFile2.txt"));
        // Verify the files were written to the output
        byte[] expectedContent1 = "TestFileContent1".getBytes();
        byte[] actualContent1 = IOUtils.toByteArray(archiveOutput
                .getInputStream(new ArchiveEntry("TestFile1.txt")));
        assertArrayEquals(expectedContent1, actualContent1);
        byte[] expectedContent2 = "TestFileContent2".getBytes();
        byte[] actualContent2 = IOUtils.toByteArray(archiveOutput
                .getInputStream(new ArchiveEntry("TestFile2.txt")));
        assertArrayEquals(expectedContent2, actualContent2);
        // Verify the deleted file was not written to the output
        ArchiveEntry deletedEntry = archiveInput.getNextEntry();
        assertTrue(deletedEntry != null);
        assertTrue(archiveOutput.getNextArchiveEntry() == null);
    }
}
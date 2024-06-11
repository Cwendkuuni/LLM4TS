package org.apache.commons.compress.changes;

import static org.junit.Assert.*;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Test class for ChangeSetPerformer.
 */
public class ChangeSetPerformerTest {

    private ChangeSet changeSet;
    private ChangeSetPerformer performer;

    @Before
    public void setUp() {
        changeSet = new ChangeSet();
        changeSet.addChange(new Change(Change.TYPE_ADD, "file1.txt", null,
                new ByteArrayInputStream(new byte[0]), null, true));
        changeSet.addChange(new Change(Change.TYPE_DELETE, "file1.txt", null,
                null, null, false));
        performer = new ChangeSetPerformer(changeSet);
    }

    @Test
    public void testPerform() throws IOException {
        // Prepare test data
        ArchiveInputStream input = new TarArchiveInputStream(
                new ByteArrayInputStream(getTestData().getBytes()));
        ArchiveOutputStream output = new TarArchiveOutputStream(
                new ByteArrayOutputStream());

        // Test perform method
        ChangeSetResults results = performer.perform(input, output);

        // Verify results
        TarArchiveEntry inputEntry1 = (TarArchiveEntry) input.getNextEntry();
        TarArchiveEntry outputEntry1 = (TarArchiveEntry) output.getNextEntry();
        TarArchiveEntry outputEntry2 = (TarArchiveEntry) output.getNextEntry();

        assertEquals("file1.txt", inputEntry1.getName());
        assertEquals("file1.txt", outputEntry1.getName());
        assertNull(outputEntry2);

        assertFalse(results.hasBeenAdded("file1.txt"));
        assertTrue(results.hasBeenDeleted("file1.txt"));

        // Verify the test data
        byte[] testData = getTestData();

        InputStream inputStream = new ByteArrayInputStream(testData);
        ArchiveInputStream newInput = new TarArchiveInputStream(inputStream);
        TarArchiveEntry newInputEntry = (TarArchiveEntry) newInput.getNextEntry();

        assertEquals("file1.txt", newInputEntry.getName());
        assertNull(newInput.getNextEntry());

        // Verify that no other entries were added or deleted
        TarArchiveEntry expectedInputEntry2 = new TarArchiveEntry("file2.txt");
        TarArchiveEntry expectedOutputEntry2 = new TarArchiveEntry("file2.txt");

        TarArchiveEntry inputEntry2 = (TarArchiveEntry) input.getNextEntry();
        TarArchiveEntry outputEntry2 = (TarArchiveEntry) output.getNextEntry();

        assertSame(expectedInputEntry2, inputEntry2);
        assertSame(expectedOutputEntry2, outputEntry2);
    }

    private byte[] getTestData() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutput = new TarArchiveOutputStream(outputStream);
        tarOutput.putNextEntry(new TarArchiveEntry("file1.txt"));
        tarOutput.write(new byte[0], 0, 0);
        tarOutput.closeArchiveEntry();
        tarOutput.finish();
        return outputStream.toByteArray();
    }
}
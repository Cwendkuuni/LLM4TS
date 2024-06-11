package org.apache.commons.compress.changes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.compress.utils.TestUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for ChangeSetPerformer
 */
public class ChangeSetPerformerTest {

    private ArchiveInputStream mockArchiveInputStream;
    private ArchiveOutputStream mockArchiveOutputStream;
    private ChangeSet mockChangeSet;
    private ChangeSetPerformer performer;
    private Set<Change> changes;

    @Before
    public void setUp() {
        mockArchiveInputStream = mock(ArchiveInputStream.class);
        mockArchiveOutputStream = mock(ArchiveOutputStream.class);
        mockChangeSet = mock(ChangeSet.class);
        changes = new HashSet<>();

        when(mockChangeSet.getChanges()).thenReturn(changes);
        performer = new ChangeSetPerformer(mockChangeSet);
    }

    @After
    public void tearDown() throws Exception {
        IOUtils.closeQuietly(mockArchiveInputStream);
        IOUtils.closeQuietly(mockArchiveOutputStream);
    }

    @Test
    public void testPerformWithEmptyChangeSet() throws IOException {
        performer.perform(mockArchiveInputStream, mockArchiveOutputStream);
        TestUtils.assertEmptyStream(mockArchiveOutputStream);
    }

    @Test
    public void testPerformWithAddChange() throws IOException {
        Change addChange = new Change("test.txt", Change.TYPE_ADD, null, null, null, false);
        changes.add(addChange);

        InputStream inputStream = new ByteArrayInputStream("Test content".getBytes());
        ArchiveEntry archiveEntry = mock(ArchiveEntry.class);
        when(archiveEntry.getName()).thenReturn("test.txt");

        when(mockArchiveInputStream.getNextEntry()).thenReturn(archiveEntry);

        performer.perform(mockArchiveInputStream, mockArchiveOutputStream);
        TestUtils.assertStreamContentEqual("Test content", mockArchiveOutputStream);
    }

    @Test
    public void testPerformWithAddAndReplaceChange() throws IOException {
        Change addChange = new Change("test.txt", Change.TYPE_ADD, null, null, null, false);
        changes.add(addChange);

        Change replaceChange = new Change("test.txt", Change.TYPE_ADD, null, null, "test1.txt", true);
        changes.add(replaceChange);

        InputStream inputStream1 = new ByteArrayInputStream("Test content 1".getBytes());
        InputStream inputStream2 = new ByteArrayInputStream("Test content 2".getBytes());

        ArchiveEntry archiveEntry = mock(ArchiveEntry.class);
        when(archiveEntry.getName()).thenReturn("test.txt");

        when(mockArchiveInputStream.getNextEntry()).thenReturn(archiveEntry).thenReturn(null);

        when(mockChangeSet.getChange("test1.txt")).thenReturn(replaceChange);

        performer.perform(mockArchiveInputStream, mockArchiveOutputStream);
        TestUtils.assertStreamContentEqual("Test content 2", mockArchiveOutputStream);
    }

    @Test
    public void testPerformWithDeleteChange() throws IOException {
        Change deleteChange = new Change("test.txt", Change.TYPE_DELETE, null, null, null, false);
        changes.add(deleteChange);

        ArchiveEntry archiveEntry = mock(ArchiveEntry.class);
        when(archiveEntry.getName()).thenReturn("test.txt");

        when(mockArchiveInputStream.getNextEntry()).thenReturn(archiveEntry);

        performer.perform(mockArchiveInputStream, mockArchiveOutputStream);
        TestUtils.assertEmptyStream(mockArchiveOutputStream);
    }

    @Test
    public void testPerformWithDeleteAndAddChange() throws IOException {
        Change deleteChange = new Change("test.txt", Change.TYPE_DELETE, null, null, null, false);
        changes.add(deleteChange);

        Change addChange = new Change("test.txt", Change.TYPE_ADD, null, null, null, false);
        changes.add(addChange);

        ArchiveEntry archiveEntry = mock(ArchiveEntry.class);
        when(archiveEntry.getName()).thenReturn("test.txt");

        when(mockArchiveInputStream.getNextEntry()).thenReturn(archiveEntry).thenReturn(null);

        performer.perform(mockArchiveInputStream, mockArchiveOutputStream);
        TestUtils.assertEmptyStream(mockArchiveOutputStream);
    }

    @Test
    public void testPerformWithAddChangeAlreadyProcessed() throws IOException {
        Change addChange = new Change("test.txt", Change.TYPE_ADD, null, null, null, false);
        changes.add(addChange);

        InputStream inputStream = new ByteArrayInputStream("Test content".getBytes());
        ArchiveEntry archiveEntry = mock(ArchiveEntry.class);
        when(archiveEntry.getName()).thenReturn("test.txt");

        when(mockArchiveInputStream.getNextEntry()).thenReturn(archiveEntry);

        when(mockChangeSet.getChange("test.txt")).thenReturn(addChange);

        performer.perform(mockArchiveInputStream, mockArchiveOutputStream);
        TestUtils.assertStreamContentEqual("Test content", mockArchiveOutputStream);
        
        mockArchiveInputStream = mock(ArchiveInputStream.class);
        mockArchiveOutputStream = mock(ArchiveOutputStream.class);
        performer = new ChangeSetPerformer(mockChangeSet);

        performer.perform(mockArchiveInputStream, mockArchiveOutputStream);
        TestUtils.assertEmptyStream(mockArchiveOutputStream);
    }
}
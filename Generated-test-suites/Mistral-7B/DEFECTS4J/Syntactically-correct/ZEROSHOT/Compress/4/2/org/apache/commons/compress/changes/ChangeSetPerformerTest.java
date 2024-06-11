package org.apache.commons.compress.changes;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.compress.ChangeSet;
import org.apache.commons.compress.Change;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class ChangeSetPerformerTest {

    private ArchiveInputStream mockArchiveInputStream;
    private ArchiveOutputStream mockArchiveOutputStream;
    private ChangeSet mockChangeSet;
    private ChangeSetPerformer performer;

    @Before
    public void setUp() throws Exception {
        mockArchiveInputStream = Mockito.mock(ArchiveInputStream.class);
        mockArchiveOutputStream = Mockito.mock(ArchiveOutputStream.class);
        mockChangeSet = Mockito.mock(ChangeSet.class);
        Mockito.when(mockChangeSet.getChanges()).thenReturn(new LinkedHashSet<>());

        performer = new ChangeSetPerformer(mockChangeSet);
    }

    @Test
    public void testPerformWithEmptyChangeSet() throws IOException {
        PerformTest performTest = new PerformTest();
        performTest.testPerform(performer, mockArchiveInputStream, mockArchiveOutputStream);
        Mockito.verify(mockArchiveInputStream, Mockito.never()).getNextEntry();
        Mockito.verify(mockArchiveOutputStream, Mockito.never()).putArchiveEntry(Mockito.any(ArchiveEntry.class));
        Mockito.verify(mockArchiveOutputStream, Mockito.never()).closeArchiveEntry();
    }

    @Test
    public void testPerformWithNoChangesToPerform() throws IOException {
        Set changes = new LinkedHashSet<>();
        Change addChange = Mockito.mock(Change.class);
        changes.add(addChange);
        Mockito.when(mockChangeSet.getChanges()).thenReturn(changes);

        PerformTest performTest = new PerformTest();
        performTest.testPerform(performer, mockArchiveInputStream, mockArchiveOutputStream);
        Mockito.verify(mockArchiveInputStream, Mockito.never()).getNextEntry();
        Mockito.verify(mockArchiveOutputStream, Mockito.never()).putArchiveEntry(Mockito.any(ArchiveEntry.class));
        Mockito.verify(mockArchiveOutputStream, Mockito.never()).closeArchiveEntry();
    }

    @Test
    public void testPerformWithAddChange() throws IOException {
        Set changes = new LinkedHashSet<>();
        Change addChange = Mockito.mock(Change.class);
        Change entryChange = Mockito.mock(Change.class);
        changes.add(addChange);
        changes.add(entryChange);
        Mockito.when(mockChangeSet.getChanges()).thenReturn(changes);

        PerformTest performTest = new PerformTest();
        performTest.testPerform(performer, mockArchiveInputStream, mockArchiveOutputStream);

        ArgumentCaptor<ArchiveEntry> archiveEntryArgumentCaptor = ArgumentCaptor.forClass(ArchiveEntry.class);
        Mockito.verify(mockArchiveOutputStream, Mockito.times(1)).putArchiveEntry(archiveEntryArgumentCaptor.capture());
        ArchiveEntry capturedEntry = archiveEntryArgumentCaptor.getValue();
        Mockito.verify(mockArchiveOutputStream, Mockito.times(1)).closeArchiveEntry();

        assertEquals("expected add change entry name", addChange.getEntry().getName(), capturedEntry.getName());
    }

    @Test
    public void testPerformWithDeletedChange() throws IOException {
        Set changes = new LinkedHashSet<>();
        Change addChange = Mockito.mock(Change.class);
        Change deleteChange = Mockito.mock(Change.class);
        changes.add(addChange);
        changes.add(deleteChange);
        Mockito.when(mockChangeSet.getChanges()).thenReturn(changes);

        PerformTest performTest = new PerformTest();
        performTest.testPerform(performer, mockArchiveInputStream, mockArchiveOutputStream);

        Mockito.verify(mockArchiveInputStream, Mockito.times(1)).getNextEntry();
        Mockito.verify(mockArchiveOutputStream, Mockito.never()).putArchiveEntry(Mockito.any(ArchiveEntry.class));
        Mockito.verify(mockArchiveOutputStream, Mockito.never()).closeArchiveEntry();
    }

    @Test(expected = IOException.class)
    public void testPerformWithIOException() throws IOException {
        Set changes = new LinkedHashSet<>();
        Change addChange = Mockito.mock(Change.class);
        changes.add(addChange);
        Mockito.when(mockChangeSet.getChanges()).thenReturn(changes);

        PerformTest performTest = new PerformTest();
        performTest.testPerformWithIOException(performer, mockArchiveInputStream, mockArchiveOutputStream);
    }

    @Test
    public void testIsDeletedLaterWithNoDeletions() {
        Set changes = new LinkedHashSet<>();
        Change addChange = Mockito.mock(Change.class);
        changes.add(addChange);
        Mockito.when(mockChangeSet.getChanges()).thenReturn(changes);

        assertFalse("expected no deletions to be found", performer.isDeletedLater(changes, null));
    }

    @Test
    public void testIsDeletedLaterWithDeletionBeforeAdd() {
        Set changes = new LinkedHashSet<>();
        Change addChange = Mockito.mock(Change.class);
        Change deleteChange = Mockito.mock(Change.class);
        changes.add(addChange);
        changes.add(deleteChange);
        Mockito.when(mockChangeSet.getChanges()).thenReturn(changes);

        ArchiveEntry entry = Mockito.mock(ArchiveEntry.class);
        Mockito.when(entry.getName()).thenReturn("test.txt");

        assertTrue("expected deletion to be found before add", performer.isDeletedLater(changes, entry));
    }

    @Test
    public void testIsDeletedLaterWithAddBeforeDeletion() {
        Set changes = new LinkedHashSet<>();
        Change addChange = Mockito.mock(Change.class);
        Change deleteChange = Mockito.mock(Change.class);
        changes.add(addChange);
        changes.add(deleteChange);
        Mockito.when(mockChangeSet.getChanges()).thenReturn(changes);

        ArchiveEntry entry = Mockito.mock(ArchiveEntry.class);
        Mockito.when(entry.getName()).thenReturn("test.txt");

        assertFalse("expected add to not be found before deletion", performer.isDeletedLater(changes, entry));
    }

    @Test
    public void testIsDeletedLaterWithNoMatchingDeletion() {
        Set changes = new LinkedHashSet<>();
        Change addChange = Mockito.mock(Change.class);
        Change deleteChange = Mockito.mock(Change.class);
        changes.add(addChange);
        changes.add(deleteChange);
        Mockito.when(mockChangeSet.getChanges()).thenReturn(changes);

        ArchiveEntry entry = Mockito.mock(ArchiveEntry.class);
        Mockito.when(entry.getName()).thenReturn("test2.txt");

        assertFalse("expected no matching deletion to be found", performer.isDeletedLater(changes, entry));
    }

    @Test
    public void testCopyStream() throws IOException {
        InputStream input = new ByteArrayInputStream("test".getBytes());
        ArchiveOutputStream output = Mockito.mock(ArchiveOutputStream.class);
        ArchiveEntry entry = Mockito.mock(ArchiveEntry.class);

        performer.copyStream(input, output, entry);

        Mockito.verify(output, Mockito.times(1)).putNextEntry(Mockito.eq(entry));
        Mockito.verify(output, Mockito.times(1)).write(Mockito.anyByteArray(), Mockito.eq(0), Mockito.anyInt());
        Mockito.verify(output, Mockito.times(1)).closeArchiveEntry();
    }

    private static class PerformTest {

        public void testPerform(ChangeSetPerformer performer, ArchiveInputStream input, ArchiveOutputStream output) throws IOException {
            performer.perform(input, output);
        }

        public void testPerformWithIOException(ChangeSetPerformer performer, ArchiveInputStream input, ArchiveOutputStream output) throws IOException {
            Mockito.doThrow(new IOException("test IOException")).when(input).getNextEntry();
            performer.perform(input, output);
        }
    }
}
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ChangeSetPerformerTest {

    private ChangeSet changeSet;
    private ChangeSetPerformer performer;
    private ArchiveInputStream inputStreamMock;
    private ArchiveOutputStream outputStreamMock;

    @Before
    public void setup() {
        changeSet = Mockito.mock(ChangeSet.class);
        performer = new ChangeSetPerformer(changeSet);
        inputStreamMock = Mockito.mock(ArchiveInputStream.class);
        outputStreamMock = Mockito.mock(ArchiveOutputStream.class);
    }

    @Test
    public void testPerform_addReplaceMode() throws IOException {
        // Given
        ArchiveEntry entry = Mockito.mock(ArchiveEntry.class);
        InputStream input = Mockito.mock(InputStream.class);
        Mockito.when(changeSet.getChanges()).thenReturn(getChangesSet());
        Mockito.when(input.available()).thenReturn(1024);
        Mockito.when(inputStreamMock.getNextEntry()).thenReturn(entry);
        Mockito.when(inputStreamMock.read(Mockito.anyByteArray(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(1024);
        Mockito.when(inputStreamMock.close()).thenReturn(true);
        Mockito.when(outputStreamMock.putArchiveEntry(entry)).thenReturn(entry);
        Mockito.when(outputStreamMock.write(Mockito.anyByteArray(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(1024);
        Mockito.when(outputStreamMock.closeArchiveEntry()).thenReturn(true);

        // When
        ChangeSetResults result = performer.perform(inputStreamMock, outputStreamMock);

        // Then
        assertEquals(1, result.getAddedFromChangeSet().size());
        assertEquals("testEntry.txt", result.getAddedFromChangeSet().iterator().next());
        assertNull(result.getDeleted());
        assertTrue(result.wasPerformed());
    }

    @Test
    public void testPerform_addNoReplaceMode() throws IOException {
        // Given
        ArchiveEntry entry = Mockito.mock(ArchiveEntry.class);
        InputStream input = Mockito.mock(InputStream.class);
        Mockito.when(changeSet.getChanges()).thenReturn(getChangesSetNoReplaceMode());
        Mockito.when(input.available()).thenReturn(1024);
        Mockito.when(inputStreamMock.getNextEntry()).thenReturn(entry);
        Mockito.when(inputStreamMock.read(Mockito.anyByteArray(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(1024);
        Mockito.when(inputStreamMock.close()).thenReturn(true);
        Mockito.when(outputStreamMock.putArchiveEntry(entry)).thenReturn(entry);
        Mockito.when(outputStreamMock.write(Mockito.anyByteArray(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(1024);
        Mockito.when(outputStreamMock.closeArchiveEntry()).thenReturn(true);

        // When
        ChangeSetResults result = performer.perform(inputStreamMock, outputStreamMock);

        // Then
        assertEquals(1, result.getAddedFromChangeSet().size());
        assertEquals("testEntry.txt", result.getAddedFromChangeSet().iterator().next());
        assertEquals(1, result.getAddedFromStream().size());
        assertEquals("testEntry.txt", result.getAddedFromStream().iterator().next());
        assertFalse(result.wasPerformed());
    }

    @Test
    public void testPerform_delete() throws IOException {
        // Given
        ArchiveEntry entry = Mockito.mock(ArchiveEntry.class);
        Mockito.when(changeSet.getChanges()).thenReturn(getChangesSetWithDelete());
        Mockito.when(inputStreamMock.getNextEntry()).thenReturn(entry);
        Mockito.when(inputStreamMock.close()).thenReturn(true);

        // When
        ChangeSetResults result = performer.perform(inputStreamMock, outputStreamMock);

        // Then
        assertEquals(0, result.getAddedFromChangeSet().size());
        assertEquals(0, result.getAddedFromStream().size());
        assertEquals("testEntry.txt", result.getDeleted());
        assertTrue(result.wasPerformed());
    }

    @Test
    public void testPerform_deleteDir() throws IOException {
        // Given
        ArchiveEntry entry = Mockito.mock(ArchiveEntry.class);
        ArchiveEntry subEntry = Mockito.mock(ArchiveEntry.class);
        Mockito.when(changeSet.getChanges()).thenReturn(getChangesSetWithDeleteDir());
        Mockito.when(inputStreamMock.getNextEntry()).thenReturn(entry);
        Mockito.when(entry.getName()).thenReturn("/testDir/testEntry.txt");
        Mockito.when(inputStreamMock.getNextEntry()).thenReturn(subEntry);
        Mockito.when(subEntry.getName()).thenReturn("subTestEntry.txt");
        Mockito.when(inputStreamMock.close()).thenReturn(true);

        // When
        ChangeSetResults result = performer.perform(inputStreamMock, outputStreamMock);

        // Then
        assertEquals(0, result.getAddedFromChangeSet().size());
        assertEquals(0, result.getAddedFromStream().size());
        assertEquals("/testDir/testEntry.txt", result.getDeleted());
        assertTrue(result.wasPerformed());
    }

    @Test(expected = IOException.class)
    public void testPerform_exception() throws IOException {
        // Given
        ArchiveEntry entry = Mockito.mock(ArchiveEntry.class);
        Mockito.when(changeSet.getChanges()).thenReturn(getChangesSetWithException());
        Mockito.when(inputStreamMock.getNextEntry()).thenReturn(entry);
        Mockito.when(inputStreamMock.read(Mockito.anyByteArray(), Mockito.anyInt(), Mockito.anyInt())).thenThrow(new IOException());

        // When
        performer.perform(inputStreamMock, outputStreamMock);

        // Then
        // Exception should be thrown
    }

    private Set<Change> getChangesSet() {
        Set<Change> changes = new HashSet<>();
        changes.add(Change.builder().add("/testEntry.txt").build());
        return changes;
    }

    private Set<Change> getChangesSetNoReplaceMode() {
        Set<Change> changes = new HashSet<>();
        changes.add(Change.builder().add("/testEntry.txt").build());
        changes.add(Change.builder().add("/testEntry.txt").withReplaceMode(false).build());
        return changes;
    }

    private Set<Change> getChangesSetWithDelete() {
        Set<Change> changes = new HashSet<>();
        changes.add(Change.builder().delete("testEntry.txt").build());
        return changes;
    }

    private Set<Change> getChangesSetWithDeleteDir() {
        Set<Change> changes = new HashSet<>();
        changes.add(Change.builder().deleteDir("/testDir").build());
        return changes;
    }

    private Set<Change> getChangesSetWithException() {
        Set<Change> changes = new HashSet<>();
        changes.add(Change.builder().add("/testEntry.txt").build());
        changes.add(Change.builder().delete("testEntry.txt").withException(new IOException()).build());
        return changes;
    }
}
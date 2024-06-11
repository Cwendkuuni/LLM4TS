package org.apache.commons.compress.changes;

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
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
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
        changeSet = Mockito.spy(new ChangeSet(Collections.singletonList(
                new Change(Change.TYPE_ADD, new FileName("test.txt"), null, null, false))));
        performer = new ChangeSetPerformer(changeSet);
        inputStreamMock = Mockito.mock(ArchiveInputStream.class);
        outputStreamMock = Mockito.mock(ArchiveOutputStream.class);
    }

    @Test
    public void testPerform_AddFile() throws IOException {
        // Given
        ArchiveEntry testEntry = Mockito.mock(ArchiveEntry.class);
        Mockito.when(testEntry.getName()).thenReturn("test.txt");
        Mockito.when(inputStreamMock.getNextEntry()).thenReturn(testEntry);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // When
        ChangeSetResults results = performer.perform(inputStreamMock, outputStreamMock);

        // Then
        Mockito.verify(inputStreamMock, Mockito.times(1)).getNextEntry();
        Mockito.verify(outputStreamMock, Mockito.times(1)).putArchiveEntry(testEntry);
        Mockito.verify(outputStreamMock, Mockito.times(1)).closeArchiveEntry();
        Mockito.verify(changeSet, Mockito.times(1)).getChanges();
        assertTrue(results.hasBeenAdded("test.txt"));
    }

    @Test
    public void testPerform_AddFile_ReplaceMode() throws IOException {
        // Given
        ArchiveEntry testEntry = Mockito.mock(ArchiveEntry.class);
        Mockito.when(testEntry.getName()).thenReturn("test.txt");
        Mockito.when(inputStreamMock.getNextEntry()).thenReturn(testEntry);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        Change testChange = new Change(Change.TYPE_ADD, new FileName("test.txt"), null, null, true);
        changeSet.setChanges(Collections.singletonList(testChange));
        performer = new ChangeSetPerformer(changeSet);

        // When
        ChangeSetResults results = performer.perform(inputStreamMock, outputStreamMock);

        // Then
        Mockito.verify(inputStreamMock, Mockito.times(1)).getNextEntry();
        Mockito.verify(outputStreamMock, Mockito.times(1)).putArchiveEntry(testEntry);
        Mockito.verify(outputStreamMock, Mockito.times(1)).closeArchiveEntry();
        Mockito.verify(changeSet, Mockito.times(1)).getChanges();
        assertTrue(results.hasBeenAdded("test.txt"));
        assertTrue(results.addedFromChangeSet("test.txt"));
    }

    @Test
    public void testPerform_DeleteFile() throws IOException {
        // Given
        ArchiveEntry testEntry = Mockito.mock(ArchiveEntry.class);
        Mockito.when(testEntry.getName()).thenReturn("test.txt");
        Mockito.when(inputStreamMock.getNextEntry()).thenReturn(testEntry);
        Mockito.when(inputStreamMock.readAllBytes()).thenReturn(new byte[0]);

        // When
        performer.perform(inputStreamMock, outputStreamMock);

        // Then
        Mockito.verify(inputStreamMock, Mockito.times(1)).getNextEntry();
        Mockito.verify(inputStreamMock, Mockito.times(1)).readAllBytes();
        Mockito.verifyNoMoreInteractions(outputStreamMock);
        assertTrue(changeSet.getChanges().isEmpty());
        assertTrue(changeSet.isCompleted());
        assertFalse(changeSet.hasNextChange());
    }

    @Test(expected = IOException.class)
    public void testPerform_Exception() throws IOException {
        // Given
        Mockito.when(inputStreamMock.getNextEntry()).thenThrow(new IOException());

        // When
        performer.perform(inputStreamMock, outputStreamMock);

        // Then
        Mockito.verify(inputStreamMock, Mockito.times(1)).getNextEntry();
        Mockito.verify(outputStreamMock, Mockito.never()).putArchiveEntry(Mockito.any(ArchiveEntry.class));
        Mockito.verify(outputStreamMock, Mockito.never()).closeArchiveEntry();
        assertTrue(changeSet.getChanges().isEmpty());
        assertTrue(changeSet.isCompleted());
        assertFalse(changeSet.hasNextChange());
    }

    @Test
    public void testPerform_MultipleFiles() throws IOException {
        // Given
        ArchiveEntry test1 = Mockito.mock(ArchiveEntry.class);
        Mockito.when(test1.getName()).thenReturn("test1.txt");
        ArchiveEntry test2 = Mockito.mock(ArchiveEntry.class);
        Mockito.when(test2.getName()).thenReturn("test2.txt");
        Mockito.when(inputStreamMock.getNextEntry()).thenReturn(test1).thenReturn(test2);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // When
        ChangeSetResults results = performer.perform(inputStreamMock, outputStreamMock);

        // Then
        Mockito.verify(inputStreamMock, Mockito.times(2)).getNextEntry();
        Mockito.verify(outputStreamMock, Mockito.times(2)).putArchiveEntry(test1);
        Mockito.verify(outputStreamMock, Mockito.times(2)).putArchiveEntry(test2);
        Mockito.verify(outputStreamMock, Mockito.times(2)).closeArchiveEntry();
        Mockito.verify(changeSet, Mockito.times(1)).getChanges();
        assertTrue(results.hasBeenAdded("test1.txt"));
        assertTrue(results.hasBeenAdded("test2.txt"));
    }

    @Test
    public void testPerform_MultipleFiles_ReplaceMode() throws IOException {
        // Given
        ArchiveEntry test1 = Mockito.mock(ArchiveEntry.class);
        Mockito.when(test1.getName()).thenReturn("test1.txt");
        ArchiveEntry test2 = Mockito.mock(ArchiveEntry.class);
        Mockito.when(test2.getName()).thenReturn("test2.txt");
        Mockito.when(inputStreamMock.getNextEntry()).thenReturn(test1).thenReturn(test2);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        Change testChange1 = new Change(Change.TYPE_ADD, new FileName("test1.txt"), null, null, true);
        Change testChange2 = new Change(Change.TYPE_ADD, new FileName("test2.txt"), null, null, true);
        changeSet.setChanges(Arrays.asList(testChange1, testChange2));
        performer = new ChangeSetPerformer(changeSet);

        // When
        ChangeSetResults results = performer.perform(inputStreamMock, outputStreamMock);

        // Then
        Mockito.verify(inputStreamMock, Mockito.times(2)).getNextEntry();
        Mockito.verify(outputStreamMock, Mockito.times(2)).putArchiveEntry(test1);
        Mockito.verify(outputStreamMock, Mockito.times(2)).putArchiveEntry(test2);
        Mockito.verify(outputStreamMock, Mockito.times(2)).closeArchiveEntry();
        Mockito.verify(changeSet, Mockito.times(1)).getChanges();
        assertTrue(results.hasBeenAdded("test1.txt"));
        assertTrue(results.hasBeenAdded("test2.txt"));
        assertTrue(results.addedFromChangeSet("test1.txt"));
        assertTrue(results.addedFromChangeSet("test2.txt"));
    }

    @Test
    public void testPerform_FileAlreadyAdded() throws IOException {
        // Given
        ArchiveEntry testEntry = Mockito.mock(ArchiveEntry.class);
        Mockito.when(testEntry.getName()).thenReturn("test.txt");
        Mockito.when(inputStreamMock.getNextEntry()).thenReturn(testEntry);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        Change testChange = new Change(Change.TYPE_ADD, new FileName("test.txt"), null, null, false);
        changeSet.setChanges(Collections.singletonList(testChange));
        performer = new ChangeSetPerformer(changeSet);

        performer.perform(inputStreamMock, outputStreamMock);
        performer.perform(inputStreamMock, outputStreamMock);

        // Then
        Mockito.verify(inputStreamMock, Mockito.times(2)).getNextEntry();
        Mockito.verify(outputStreamMock, Mockito.times(2)).putArchiveEntry(testEntry);
        Mockito.verify(outputStreamMock, Mockito.times(2)).closeArchiveEntry();
        Mockito.verify(changeSet, Mockito.times(1)).getChanges();
        assertTrue(results.hasBeenAdded("test.txt"));
        assertFalse(results.addedFromChangeSet("test.txt"));
    }

    @Test
    public void testPerform_Directory() throws IOException {
        // Given
        ArchiveEntry testDir = Mockito.mock(ArchiveEntry.class);
        Mockito.when(testDir.getName()).thenReturn("test/");
        Mockito.when(inputStreamMock.getNextEntry()).thenReturn(testDir);

        // When
        performer.perform(inputStreamMock, outputStreamMock);

        // Then
        Mockito.verify(inputStreamMock, Mockito.times(1)).getNextEntry();
        Mockito.verify(outputStreamMock, Mockito.never()).putArchiveEntry(Mockito.any(ArchiveEntry.class));
        Mockito.verify(outputStreamMock, Mockito.never()).closeArchiveEntry();
    }

    @Test
    public void testPerform_DeleteDirectory() throws IOException {
        // Given
        ArchiveEntry testDir = Mockito.mock(ArchiveEntry.class);
        Mockito.when(testDir.getName()).thenReturn("test/");
        Mockito.when(inputStreamMock.getNextEntry()).thenReturn(testDir);
        Mockito.when(inputStreamMock.readAllBytes()).thenReturn(new byte[0]);

        // When
        performer.perform(inputStreamMock, outputStreamMock);

        // Then
        Mockito.verify(inputStreamMock, Mockito.times(1)).getNextEntry();
        Mockito.verify(inputStreamMock, Mockito.times(1)).readAllBytes();
        Mockito.verifyNoMoreInteractions(outputStreamMock);
        assertTrue(changeSet.getChanges().isEmpty());
        assertTrue(changeSet.isCompleted());
        assertFalse(changeSet.hasNextChange());
    }

    @Test
    public void testPerform_DeleteDirectoryWithFile() throws IOException {
        // Given
        ArchiveEntry testDir = Mockito.mock(ArchiveEntry.class);
        Mockito.when(testDir.getName()).thenReturn("test/test1.txt");
        ArchiveEntry testFile = Mockito.mock(ArchiveEntry.class);
        Mockito.when(testFile.getName()).thenReturn("test/test2.txt");
        Mockito.when(inputStreamMock.getNextEntry()).thenReturn(testDir).thenReturn(testFile);
        Mockito.when(inputStreamMock.readAllBytes()).thenReturn(new byte[0]);

        // When
        performer.perform(inputStreamMock, outputStreamMock);

        // Then
        Mockito.verify(inputStreamMock, Mockito.times(2)).getNextEntry();
        Mockito.verify(inputStreamMock, Mockito.times(1)).readAllBytes();
        Mockito.verify(outputStreamMock, Mockito.never()).putArchiveEntry(Mockito.any(ArchiveEntry.class));
        Mockito.verify(outputStreamMock, Mockito.never()).closeArchiveEntry();
        assertTrue(changeSet.getChanges().isEmpty());
        assertTrue(changeSet.isCompleted());
        assertFalse(changeSet.hasNextChange());
    }

    @Test
    public void testPerform_DeleteDirectoryWithFileWithAddFile() throws IOException {
        // Given
        ArchiveEntry testDir = Mockito.mock(ArchiveEntry.class);
        Mockito.when(testDir.getName()).thenReturn("test/");
        ArchiveEntry testFile = Mockito.mock(ArchiveEntry.class);
        Mockito.when(testFile.getName()).thenReturn("test/test1.txt");
        Mockito.when(inputStreamMock.getNextEntry()).thenReturn(testDir).thenReturn(testFile);
        ArchiveEntry testAddFile = Mockito.mock(ArchiveEntry.class);
        Mockito.when(testAddFile.getName()).thenReturn("test/test2.txt");
        Mockito.when(inputStreamMock.getNextEntry()).thenReturn(testAddFile);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // When
        performer.perform(inputStreamMock, outputStreamMock);

        // Then
        Mockito.verify(inputStreamMock, Mockito.times(3)).getNextEntry();
        Mockito.verify(outputStreamMock, Mockito.times(1)).putArchiveEntry(testAddFile);
        Mockito.verify(outputStreamMock, Mockito.times(1)).closeArchiveEntry();
        Mockito.verify(changeSet, Mockito.times(1)).getChanges();
        assertTrue(results.addedFromChangeSet("test/test2.txt"));
        assertTrue(changeSet.getChanges().size() == 1);
        assertTrue(changeSet.getChanges().get(0).getType() == Change.TYPE_ADD);
    }
}
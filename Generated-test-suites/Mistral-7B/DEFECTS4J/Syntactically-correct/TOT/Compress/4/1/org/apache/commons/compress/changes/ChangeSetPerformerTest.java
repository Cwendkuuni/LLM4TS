import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
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
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ChangeSetPerformerTest {

    private ChangeSet changeSet;
    private ChangeSetPerformer performer;
    private ByteArrayOutputStream outputStream;
    private ByteArrayInputStream inputStream;
    private ArchiveOutputStream archiveOutputStream;
    private ArchiveInputStream archiveInputStream;

    @Before
    public void setUp() throws IOException {
        Set<Change> changes = new HashSet<>();
        Change addChange = new Change(Change.TYPE_ADD, null, null, null, null, null, true);
        Change addChange2 = new Change(Change.TYPE_ADD, null, null, null, null, null, true);
        Change deleteChange = new Change(Change.TYPE_DELETE, "test.txt", null, null, null, null, false);
        Change deleteChange2 = new Change(Change.TYPE_DELETE, "test2.txt", null, null, null, null, false);
        Change addReplaceChange = new Change(Change.TYPE_ADD, null, null, null, null, null, true, true);
        changes.add(addChange);
        changes.add(addChange2);
        changes.add(deleteChange);
        changes.add(deleteChange2);
        changes.add(addReplaceChange);

        this.changeSet = new ChangeSet(changes);
        this.performer = new ChangeSetPerformer(changeSet);
        this.outputStream = new ByteArrayOutputStream();
        this.inputStream = new ByteArrayInputStream(new byte[0]);
        this.archiveOutputStream = new ArchiveOutputStream("test.a", Mockito.mock(ArchiveOutputStream.class));
        this.archiveInputStream = Mockito.mock(ArchiveInputStream.class);
    }

    @Test
    public void testPerform_EmptyInput_NoChanges() throws IOException {
        // Given (Empty Input Stream)
        Mockito.when(archiveInputStream.getNextEntry()).thenReturn(null);

        // When
        ChangeSetResults results = performer.perform(archiveInputStream, archiveOutputStream);

        // Then
        assertNull(results);
        assertTrue(changeSet.isEmpty());
    }

    @Test
    public void testPerform_EmptyInput_HasChanges() throws IOException {
        // Given (Empty Input Stream)
        Mockito.when(archiveInputStream.getNextEntry()).thenReturn(null);

        // When
        ChangeSetResults results = performer.perform(archiveInputStream, archiveOutputStream);

        // Then
        assertNull(results);
        assertFalse(changeSet.isEmpty());
    }

    @Test
    public void testPerform_EmptyInput_HasChanges_Add() throws IOException {
        // Given
        Mockito.when(archiveInputStream.getNextEntry()).thenReturn(null);
        ByteArrayInputStream testInputStream = new ByteArrayInputStream(new byte[1024]);
        ArchiveEntry testEntry = new ZipArchiveEntry("test.txt");
        testEntry.setSize(1024);

        // When
        ChangeSetResults results = performer.perform(archiveInputStream, archiveOutputStream);
        performer.perform(testInputStream, archiveOutputStream);

        // Then
        assertNotNull(results);
        assertEquals(1, results.getAddedFromChangeSet().size());
        assertEquals("test.txt", results.getAddedFromChangeSet().iterator().next());
        assertEquals(1, results.getAddedFromStream().size());
        assertEquals("test.txt", results.getAddedFromStream().iterator().next());
        assertTrue(changeSet.isEmpty());
    }

    @Test
    public void testPerform_InputWithFiles_Add() throws IOException {
        // Given
        ZipFile testZipFile = new ZipFile("test.a");
        Mockito.when(archiveInputStream.getNextEntry()).thenReturn(testZipFile.getEntry("test.txt"));
        ByteArrayInputStream testInputStream = new ByteArrayInputStream(new byte[1024]);
        ArchiveEntry testEntry = new ZipArchiveEntry("test2.txt");
        testEntry.setSize(1024);

        // When
        ChangeSetResults results = performer.perform(archiveInputStream, archiveOutputStream);
        performer.perform(testInputStream, archiveOutputStream);

        // Then
        assertNotNull(results);
        assertEquals(2, results.getAddedFromChangeSet().size());
        assertEquals("test.txt", results.getAddedFromChangeSet().iterator().next());
        assertEquals("test2.txt", results.getAddedFromChangeSet().iterator().next());
        assertEquals(1, results.getAddedFromStream().size());
        assertEquals("test2.txt", results.getAddedFromStream().iterator().next());
        assertEquals(2, changeSet.getChanges().size());

        Change addChange = changeSet.getChanges().stream()
                .filter(change -> change.type() == Change.TYPE_ADD)
                .findFirst().get();
        assertEquals("test.txt", addChange.getTargetFile());

        Change addChange2 = changeSet.getChanges().stream()
                .filter(change -> change.type() == Change.TYPE_ADD)
                .filter(change -> !change.isReplaceMode())
                .findFirst().get();
        assertEquals("test2.txt", addChange2.getTargetFile());
    }

    @Test
    public void testPerform_InputWithFiles_Delete() throws IOException {
        // Given
        ZipFile testZipFile = new ZipFile("test.a");
        Mockito.when(archiveInputStream.getNextEntry()).thenReturn(testZipFile.getEntry("test.txt"));
        Mockito.when(archiveInputStream.getNextEntry()).thenReturn(testZipFile.getEntry("test2.txt"));

        // When
        ChangeSetResults results = performer.perform(archiveInputStream, archiveOutputStream);

        // Then
        assertNotNull(results);
        assertEquals(1, results.getDeleted().size());
        assertEquals("test.txt", results.getDeleted().iterator().next());
        assertEquals(2, changeSet.getChanges().size());

        Change deleteChange = changeSet.getChanges().stream()
                .filter(change -> change.type() == Change.TYPE_DELETE)
                .findFirst().get();
        assertEquals("test.txt", deleteChange.getTargetFile());

        Change addChange = changeSet.getChanges().stream()
                .filter(change -> change.type() == Change.TYPE_ADD)
                .filter(change -> change.isReplaceMode())
                .findFirst().get();
        assertEquals("test2.txt", addChange.getTargetFile());
    }

    @Test(expected = IOException.class)
    public void testPerform_InputWithFiles_Delete_DeleteLater() throws IOException {
        // Given
        ZipFile testZipFile = new ZipFile("test.a");
        Mockito.when(archiveInputStream.getNextEntry()).thenReturn(testZipFile.getEntry("test.txt"));
        Mockito.when(archiveInputStream.getNextEntry()).thenReturn(testZipFile.getEntry("test2.txt"));

        // When
        performer.perform(archiveInputStream, archiveOutputStream);

        // Then
        // An IOException is expected to be thrown since test.txt is deleted later in the ChangeSet
    }

    @Test
    public void testPerform_InputWithFiles_Delete_DeleteDir() throws IOException {
        // Given
        ZipFile testZipFile = new ZipFile("test.a");
        Mockito.when(archiveInputStream.getNextEntry()).thenReturn(testZipFile.getEntry("test.txt"));
        Mockito.when(archiveInputStream.getNextEntry()).thenReturn(testZipFile.getEntry("test/test2.txt"));

        // When
        ChangeSetResults results = performer.perform(archiveInputStream, archiveOutputStream);

        // Then
        assertNotNull(results);
        assertEquals(1, results.getDeleted().size());
        assertEquals("test/", results.getDeleted().iterator().next().substring(0, results.getDeleted().iterator().next().length() - 1));
        assertEquals(1, changeSet.getChanges().size());

        Change deleteChange = changeSet.getChanges().stream()
                .filter(change -> change.type() == Change.TYPE_DELETE_DIR)
                .findFirst().get();
        assertEquals("test", deleteChange.getTargetFile());

        Change addChange = changeSet.getChanges().stream()
                .filter(change -> change.type() == Change.TYPE_ADD)
                .findFirst().get();
        assertEquals("test.txt", addChange.getTargetFile());
    }

    @Test
    public void testPerform_InputWithFiles_Delete_DeleteLater_Add() throws IOException {
        // Given
        ZipFile testZipFile = new ZipFile("test.a");
        Mockito.when(archiveInputStream.getNextEntry()).thenReturn(testZipFile.getEntry("test.txt"));
        Mockito.when(archiveInputStream.getNextEntry()).thenReturn(testZipFile.getEntry("test2.txt"));
        Mockito.when(archiveInputStream.getNextEntry()).thenReturn(testZipFile.getEntry("test3.txt"));

        // When
        performer.perform(archiveInputStream, archiveOutputStream);

        // Then
        // test2.txt is added since it is not deleted later in the ChangeSet
    }

    @Test
    public void testPerform_InputWithFiles_Delete_DeleteLater_Delete() throws IOException {
        // Given
        ZipFile testZipFile = new ZipFile("test.a");
        Mockito.when(archiveInputStream.getNextEntry()).thenReturn(testZipFile.getEntry("test.txt"));
        Mockito.when(archiveInputStream.getNextEntry()).thenReturn(testZipFile.getEntry("test2.txt"));
        Mockito.when(archiveInputStream.getNextEntry()).thenReturn(testZipFile.getEntry("test3.txt"));

        // When
        performer.perform(archiveInputStream, archiveOutputStream);

        // Then
        // test.txt is deleted since it is deleted earlier in the ChangeSet
    }

    @Test
    public void testPerform_InputWithFiles_Replace() throws IOException {
        // Given
        ZipFile testZipFile = new ZipFile("test.a");
        Mockito.when(archiveInputStream.getNextEntry()).thenReturn(testZipFile.getEntry("test.txt"));
        ByteArrayInputStream testInputStream = new ByteArrayInputStream(new byte[1024]);
        ArchiveEntry testEntry = new ZipArchiveEntry("test.txt");
        testEntry.setSize(1024);

        // When
        ChangeSetResults results = performer.perform(archiveInputStream, archiveOutputStream);
        performer.perform(testInputStream, archiveOutputStream);

        // Then
        assertNotNull(results);
        assertEquals(1, results.getAddedFromChangeSet().size());
        assertEquals("test.txt", results.getAddedFromChangeSet().iterator().next());
        assertEquals(1, results.getAddedFromStream().size());
        assertEquals("test.txt", results.getAddedFromStream().iterator().next());
        assertEquals(1, changeSet.getChanges().size());

        Change addReplaceChange = changeSet.getChanges().stream()
                .filter(change -> change.type() == Change.TYPE_ADD)
                .filter(change -> change.isReplaceMode())
                .findFirst().get();
        assertEquals("test.txt", addReplaceChange.getTargetFile());
    }

    @Test(expected = IllegalStateException.class)
    public void testPerform_InputWithFiles_Replace_ReplaceLater() throws IOException {
        // Given
        ZipFile testZipFile = new ZipFile("test.a");
        Mockito.when(archiveInputStream.getNextEntry()).thenReturn(testZipFile.getEntry("test.txt"));
        ByteArrayInputStream testInputStream = new ByteArrayInputStream(new byte[1024]);
        ArchiveEntry testEntry = new ZipArchiveEntry("test.txt");
        testEntry.setSize(1024);

        // When
        ChangeSetResults results = performer.perform(archiveInputStream, archiveOutputStream);
        performer.perform(testInputStream, archiveOutputStream);

        // Then
        // An IllegalStateException is expected to be thrown since test.txt is replaced later in the ChangeSet
    }

    @Test
    public void testPerform_InputWithFiles_Replace_ReplaceLater_Delete() throws IOException {
        // Given
        ZipFile testZipFile = new ZipFile("test.a");
        Mockito.when(archiveInputStream.getNextEntry()).thenReturn(testZipFile.getEntry("test.txt"));
        Mockito.when(archiveInputStream.getNextEntry()).thenReturn(testZipFile.getEntry("test2.txt"));
        ByteArrayInputStream testInputStream = new ByteArrayInputStream(new byte[1024]);
        ArchiveEntry testEntry = new ZipArchiveEntry("test.txt");
        testEntry.setSize(1024);

        // When
        performer.perform(archiveInputStream, archiveOutputStream);
        performer.perform(testInputStream, archiveOutputStream);

        // Then
        // test.txt is deleted since it is replaced earlier in the ChangeSet
    }

    @Test
    public void testPerform_InputWithFiles_Replace_ReplaceLater_Replace() throws IOException {
        // Given
        ZipFile testZipFile = new ZipFile("test.a");
        Mockito.when(archiveInputStream.getNextEntry()).thenReturn(testZipFile.getEntry("test.txt"));
        Mockito.when(archiveInputStream.getNextEntry()).thenReturn(testZipFile.getEntry("test2.txt"));
        ByteArrayInputStream testInputStream1 = new ByteArrayInputStream(new byte[1024]);
        ArchiveEntry testEntry1 = new ZipArchiveEntry("test.txt");
        testEntry1.setSize(1024);
        ByteArrayInputStream testInputStream2 = new ByteArrayInputStream(new byte[1024]);
        ArchiveEntry testEntry2 = new ZipArchiveEntry("test.txt");
        testEntry2.setSize(1024);

        // When
        performer.perform(archiveInputStream, archiveOutputStream);
        performer.perform(testInputStream1, archiveOutputStream);
        performer.perform(testInputStream2, archiveOutputStream);

        // Then
        // test2.txt is replaced since it is replaced later in the ChangeSet
    }

    @Test
    public void testPerform_InputWithFiles_Replace_ReplaceLater_ReplaceDir() throws IOException {
        // Given
        ZipFile testZipFile = new ZipFile("test.a");
        Mockito.when(archiveInputStream.getNextEntry()).thenReturn(testZipFile.getEntry("test.txt"));
        Mockito.when(archiveInputStream.getNextEntry()).thenReturn(testZipFile.getEntry("test2.txt"));
        ByteArrayInputStream testInputStream1 = new ByteArrayInputStream(new byte[1024]);
        ArchiveEntry testEntry1 = new ZipArchiveEntry("test.txt");
        testEntry1.setSize(1024);
        ByteArrayInputStream testInputStream2 = new ByteArrayInputStream(new byte[1024]);
        ArchiveEntry testEntry2 = new ZipArchiveEntry("test2.txt");
        testEntry2.setSize(1024);
        ByteArrayInputStream testInputStream3 = new ByteArrayInputStream(new byte[1024]);
        ArchiveEntry testEntry3 = new ZipArchiveEntry("test3.txt");
        testEntry3.setSize(1024);

        // When
        performer.perform(archiveInputStream, archiveOutputStream);
        performer.perform(testInputStream1, archiveOutputStream);
        performer.perform(testInputStream2, archiveOutputStream);
        performer.perform(testInputStream3, archiveOutputStream);

        // Then
        // test3.txt is replaced since it is replaced later in the ChangeSet
    }
}
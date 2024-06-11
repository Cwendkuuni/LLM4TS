import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ChangeSetPerformerTest {

    private ChangeSetPerformer performer;
    private ChangeSet changeSet;
    private ArchiveInputStream inputStream;
    private ArchiveOutputStream outputStream;
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Before
    public void setUp() throws IOException {
        changeSet = new ChangeSet();
        performer = new ChangeSetPerformer(changeSet);

        Set<Change> changes = new LinkedHashSet<>();
        changes.add(new Change("test1.txt", Change.TYPE_ADD, "test1.txt", null, null));
        changes.add(new Change("test2.txt", Change.TYPE_DELETE, null, "test2.txt", null));
        changes.add(new Change("test3.txt", Change.TYPE_ADD, "test3.txt", null, null));
        changes.add(new Change("test4.txt", Change.TYPE_DELETE, null, "test4.txt", null));
        changes.add(new Change("test5.txt", Change.TYPE_ADD, "test5.txt", null, null));
        changeSet.setChanges(changes);

        inputStream = Mockito.mock(ArchiveInputStream.class);
        outputStream = Mockito.mock(ArchiveOutputStream.class);
    }

    @Test
    public void testPerform_AddAndDelete() throws IOException {
        // Given
        Mockito.when(inputStream.getNextEntry()).thenReturn(new ArchiveEntry("test1.txt")).thenReturn(new ArchiveEntry("test2.txt"))
                .thenReturn(new ArchiveEntry("test3.txt")).thenReturn(new ArchiveEntry("test4.txt")).thenReturn(null);

        File tempFile = folder.newFile("input.tar");
        Mockito.when(inputStream.getInputStream()).thenAnswer(invocation -> new FileInputStream(tempFile));

        File outputFile = folder.newFile("output.tar");
        Mockito.when(outputStream.getArchiveEntry()).thenAnswer(invocation -> new ArchiveEntry("test1.txt"));
        Mockito.when(outputStream.putArchiveEntry(ArgumentMatchers.any(ArchiveEntry.class))).thenAnswer(invocation -> {
            ArchiveEntry entry = (ArchiveEntry) invocation.getArguments()[0];
            return new FileOutputStream(new File(outputFile.getAbsolutePath() + "/" + entry.getName()));
        });

        // When
        ChangeSetResults results = performer.perform(inputStream, outputStream);

        // Then
        assertTrue(results.isSuccess());
        assertEquals(3, results.getAdded().size());
        assertEquals(2, results.getDeleted().size());
        assertFalse(results.containsError());

        // Check files in output
        ArchiveInputStream outputInputStream = new ArchiveInputStream(new FileInputStream(outputFile));
        ArchiveEntry entry;
        while ((entry = outputInputStream.getNextEntry()) != null) {
            if (entry.getName().equals("test1.txt")) {
                assertNotNull(entry.getInputStream());
                IOUtils.closeQuietly(entry.getInputStream());
            } else if (entry.getName().equals("test3.txt")) {
                assertNotNull(entry.getInputStream());
                IOUtils.closeQuietly(entry.getInputStream());
            } else if (entry.getName().equals("test5.txt")) {
                assertNotNull(entry.getInputStream());
                IOUtils.closeQuietly(entry.getInputStream());
            } else {
                assertNull(entry.getInputStream());
            }
        }
        IOUtils.closeQuietly(outputInputStream);

        // Check files in input
        ArchiveEntry inputEntry1 = new ArchiveEntry("test1.txt");
        ArchiveEntry inputEntry2 = new ArchiveEntry("test2.txt");
        ArchiveEntry inputEntry3 = new ArchiveEntry("test3.txt");
        ArchiveEntry inputEntry4 = new ArchiveEntry("test4.txt");
        ArchiveEntry inputEntry5 = new ArchiveEntry("test5.txt");

        Set<String> expectedAdded = new LinkedHashSet<>(Arrays.asList("test1.txt", "test3.txt", "test5.txt"));
        Set<String> expectedDeleted = new LinkedHashSet<>(Arrays.asList("test2.txt", "test4.txt"));

        assertEquals(expectedAdded, results.getAdded());
        assertEquals(expectedDeleted, results.getDeleted());

        // Test replace mode
        changes.remove(changes.size() - 1);
        changeSet.setChanges(changes);
        performer = new ChangeSetPerformer(changeSet);

        Set<String> expectedAddedReplace = new LinkedHashSet<>(Arrays.asList("test1.txt", "test3.txt"));
        Set<String> expectedDeletedReplace = new LinkedHashSet<>();

        Mockito.when(inputStream.getNextEntry()).thenReturn(inputEntry1).thenReturn(inputEntry2).thenReturn(inputEntry3)
                .thenReturn(inputEntry4).thenReturn(inputEntry5).thenReturn(null);

        results = performer.perform(inputStream, outputStream);

        assertEquals(expectedAddedReplace, results.getAdded());
        assertEquals(Collections.emptySet(), results.getDeleted());
    }

    @Test
    public void testPerform_DeleteLater() throws IOException {
        // Given
        Set<Change> changes = new LinkedHashSet<>();
        changes.add(new Change("test1.txt", Change.TYPE_ADD, "test1.txt", null, null));
        changes.add(new Change("test2.txt", Change.TYPE_ADD, "test2.txt", null, null));
        changes.add(new Change("test3.txt", Change.TYPE_DELETE, null, "test3.txt", null));
        changes.add(new Change("test4.txt", Change.TYPE_ADD, "test4.txt", null, null));
        changeSet.setChanges(changes);
        performer = new ChangeSetPerformer(changeSet);

        Mockito.when(inputStream.getNextEntry()).thenReturn(new ArchiveEntry("test1.txt")).thenReturn(new ArchiveEntry("test2.txt"))
                .thenReturn(new ArchiveEntry("test3.txt")).thenReturn(new ArchiveEntry("test4.txt")).thenReturn(null);

        File tempFile = folder.newFile("input.tar");
        Mockito.when(inputStream.getInputStream()).thenAnswer(invocation -> new FileInputStream(tempFile));

        File outputFile = folder.newFile("output.tar");
        Mockito.when(outputStream.getArchiveEntry()).thenAnswer(invocation -> new ArchiveEntry("test1.txt"));
        Mockito.when(outputStream.putArchiveEntry(ArgumentMatchers.any(ArchiveEntry.class))).thenAnswer(invocation -> {
            ArchiveEntry entry = (ArchiveEntry) invocation.getArguments()[0];
            return new FileOutputStream(new File(outputFile.getAbsolutePath() + "/" + entry.getName()));
        });

        // When
        ChangeSetResults results = performer.perform(inputStream, outputStream);

        // Then
        assertTrue(results.isSuccess());
        assertEquals(2, results.getAdded().size());
        assertEquals(1, results.getDeleted().size());
        assertFalse(results.containsError());

        // Check files in output
        ArchiveInputStream outputInputStream = new ArchiveInputStream(new FileInputStream(outputFile));
        ArchiveEntry entry;
        while ((entry = outputInputStream.getNextEntry()) != null) {
            if (entry.getName().equals("test1.txt")) {
                assertNotNull(entry.getInputStream());
                IOUtils.closeQuietly(entry.getInputStream());
            } else if (entry.getName().equals("test2.txt")) {
                assertNotNull(entry.getInputStream());
                IOUtils.closeQuietly(entry.getInputStream());
            } else {
                assertNull(entry.getInputStream());
            }
        }
        IOUtils.closeQuietly(outputInputStream);

        // Check files in input
        ArchiveEntry inputEntry1 = new ArchiveEntry("test1.txt");
        ArchiveEntry inputEntry2 = new ArchiveEntry("test2.txt");
        ArchiveEntry inputEntry3 = new ArchiveEntry("test3.txt");
        ArchiveEntry inputEntry4 = new ArchiveEntry("test4.txt");

        Set<String> expectedAdded = new LinkedHashSet<>(Arrays.asList("test1.txt", "test2.txt"));
        Set<String> expectedDeleted = new LinkedHashSet<>(Arrays.asList("test3.txt"));

        assertEquals(expectedAdded, results.getAdded());
        assertEquals(expectedDeleted, results.getDeleted());
    }

    @Test
    public void testPerform_Error() throws IOException {
        // Given
        Set<Change> changes = new LinkedHashSet<>();
        changes.add(new Change("test1.txt", Change.TYPE_ADD, "test1.txt", null, null));
        changeSet.setChanges(changes);
        performer = new ChangeSetPerformer(changeSet);

        Mockito.when(inputStream.getNextEntry()).thenReturn(new ArchiveEntry("test1.txt"));

        // When
        ChangeSetResults results = performer.perform(inputStream, outputStream);

        // Then
        assertFalse(results.isSuccess());
        assertTrue(results.containsError());

        // Check that error is thrown from perform method
        try {
            performer.perform(inputStream, outputStream);
            fail("Exception expected");
        } catch (IOException e) {
            // expected
        }

        // Test continue after error
        Mockito.when(inputStream.getNextEntry()).thenReturn(new ArchiveEntry("test2.txt"));

        results = performer.perform(inputStream, outputStream);

        // Then
        assertTrue(results.isSuccess());
        assertEquals(1, results.getAdded().size());
        assertFalse(results.containsError());
    }

    @Test
    public void testPerform_EmptyInput() throws IOException {
        // Given
        Set<Change> changes = new LinkedHashSet<>();
        changeSet.setChanges(changes);
        performer = new ChangeSetPerformer(changeSet);

        // When
        ChangeSetResults results = performer.perform(inputStream, outputStream);

        // Then
        assertTrue(results.isSuccess());
        assertFalse(results.containsError());
        assertTrue(results.getAdded().isEmpty());
        assertTrue(results.getDeleted().isEmpty());
    }
}
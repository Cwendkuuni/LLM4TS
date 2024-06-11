package org.apache.commons.compress.changes;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link ChangeSetPerformer} class.
 */
public class ChangeSetPerformerTest {

    private ChangeSet changeSet;
    private ChangeSetResults results;
    private InputStream inputStream;
    private InputStream inputStreamAdd;
    private ArchiveInputStream archiveInputStream;
    private ArchiveOutputStream archiveOutputStream;
    private ChangeSetPerformer performer;

    @Before
    public void setUp() throws IOException {
        changeSet = new ChangeSet();
        inputStream = mock(InputStream.class);
        inputStreamAdd = mock(InputStream.class);
        archiveInputStream = mock(ArchiveInputStream.class);
        archiveOutputStream = mock(ArchiveOutputStream.class);
        results = mock(ChangeSetResults.class);

        changeSet.addChange(new Change(Change.TYPE_ADD, "testFile.txt", null, inputStreamAdd, null));
        changeSet.addChange(new Change(Change.TYPE_DELETE, "testFile.txt", null, null, null));

        performer = new ChangeSetPerformer(changeSet);
    }

    @Test
    public void testPerform_emptyChangeSet() throws IOException {
        when(archiveInputStream.getNextEntry()).thenReturn(null);
        when(archiveOutputStream.putArchiveEntry(any(ArchiveEntry.class))).thenAnswer(invocation -> null);

        performer.perform(archiveInputStream, archiveOutputStream);

        verify(archiveInputStream).close();
        verify(archiveOutputStream).finish();
    }

    @Test
    public void testPerform_oneAddChange() throws IOException {
        when(archiveInputStream.getNextEntry()).thenReturn(null);
        when(archiveOutputStream.putArchiveEntry(any(ArchiveEntry.class))).thenAnswer(invocation -> {
            ArchiveEntry entry = invocation.getArgument(0);
            if (entry.getName().equals("testFile.txt")) {
                return new TarArchiveEntry("testFile.txt");
            }
            return null;
        });

        InputStream testInput = new ByteArrayInputStream(new byte[10]);

        when(inputStreamAdd.readAllBytes()).thenReturn(new byte[10]);
        when(inputStreamAdd.available()).thenReturn(10);

        performer.perform(archiveInputStream, archiveOutputStream);

        verify(archiveInputStream).close();
        verify(archiveOutputStream).finish();
        verify(results).addedFromChangeSet("testFile.txt");

        InputStream actualInput = performer.getInputStreamForEntry("testFile.txt");
        assertSame(testInput, actualInput);
    }

    @Test
    public void testPerform_oneDeleteChange() throws IOException {
        when(archiveInputStream.getNextEntry()).thenReturn(mock(ArchiveEntry.class));
        when(archiveOutputStream.putArchiveEntry(any(ArchiveEntry.class))).thenAnswer(invocation -> null);

        performer.perform(archiveInputStream, archiveOutputStream);

        verify(archiveInputStream).close();
        verify(archiveOutputStream).finish();
        verify(results).deleted("testFile.txt");
    }

    @Test
    public void testPerform_addChangeBeforeDelete() throws IOException {
        when(archiveInputStream.getNextEntry()).thenReturn(mock(ArchiveEntry.class));
        when(archiveInputStream.getNextEntry()).thenReturn(null);

        when(archiveOutputStream.putArchiveEntry(any(ArchiveEntry.class))).thenAnswer(invocation -> {
            ArchiveEntry entry = invocation.getArgument(0);
            if (entry.getName().equals("testFile.txt")) {
                return new TarArchiveEntry("testFile.txt");
            }
            return null;
        });

        InputStream testInput = new ByteArrayInputStream(new byte[10]);

        when(inputStreamAdd.readAllBytes()).thenReturn(new byte[10]);
        when(inputStreamAdd.available()).thenReturn(10);

        performer.perform(archiveInputStream, archiveOutputStream);

        verify(archiveInputStream).close();
        verify(archiveOutputStream).finish();
        verify(results).addedFromChangeSet("testFile.txt");
        verify(results, never()).deleted("testFile.txt");
    }

    @Test
    public void testPerform_deleteChangeBeforeAdd() throws IOException {
        when(archiveInputStream.getNextEntry()).thenReturn(mock(ArchiveEntry.class));
        when(archiveInputStream.getNextEntry()).thenReturn(null);

        when(archiveOutputStream.putArchiveEntry(any(ArchiveEntry.class))).thenAnswer(invocation -> null);

        performer.perform(archiveInputStream, archiveOutputStream);

        verify(archiveInputStream).close();
        verify(archiveOutputStream).finish();
        verify(results, never()).addedFromChangeSet("testFile.txt");
        verify(results).deleted("testFile.txt");
    }

    @Test
    public void testPerform_deleteChangeAfterAdd() throws IOException {
        when(archiveInputStream.getNextEntry()).thenReturn(null);

        when(archiveOutputStream.putArchiveEntry(any(ArchiveEntry.class))).thenAnswer(invocation -> {
            ArchiveEntry entry = invocation.getArgument(0);
            if (entry.getName().equals("testFile.txt")) {
                return new TarArchiveEntry("testFile.txt");
            }
            return null;
        });

        InputStream testInput = new ByteArrayInputStream(new byte[10]);

        when(inputStreamAdd.readAllBytes()).thenReturn(new byte[10]);
        when(inputStreamAdd.available()).thenReturn(10);

        performer.perform(archiveInputStream, archiveOutputStream);

        verify(archiveInputStream).close();
        verify(archiveOutputStream).finish();
        verify(results, never()).addedFromChangeSet("testFile.txt");
        verify(results).deleted("testFile.txt");
    }

    @Test
    public void testPerform_addChangeWithReplaceMode_fileAlreadyExists() throws IOException {
        when(archiveInputStream.getNextEntry()).thenReturn(null);
        when(archiveOutputStream.putArchiveEntry(any(ArchiveEntry.class))).thenAnswer(invocation -> {
            ArchiveEntry entry = invocation.getArgument(0);
            if (entry.getName().equals("testFile.txt")) {
                return new TarArchiveEntry("testFile.txt");
            }
            return null;
        });

        InputStream testInput = new ByteArrayInputStream(new byte[10]);
        InputStream testInputReplace = new ByteArrayInputStream(new byte[10]);

        when(inputStreamAdd.readAllBytes()).thenReturn(new byte[10]);
        when(inputStreamAdd.available()).thenReturn(10);

        when(inputStreamAdd.readAllBytes()).beforeEach(invocation -> testInputReplace = invocation.getMock());
        when(inputStreamAdd.available()).thenAnswer(invocation -> testInputReplace.available());

        performer.perform(archiveInputStream, archiveOutputStream);

        verify(archiveInputStream).close();
        verify(archiveOutputStream).finish();
        verify(results, never()).addedFromChangeSet("testFile.txt");
        verify(results, never()).deleted("testFile.txt");
        verify(archiveOutputStream, never()).putArchiveEntry(any(ArchiveEntry.class));
    }

    @Test
    public void testPerform_addChangeWithReplaceMode_fileDoesntExist() throws IOException {
        when(archiveInputStream.getNextEntry()).thenReturn(null);
        when(archiveOutputStream.putArchiveEntry(any(ArchiveEntry.class))).thenAnswer(invocation -> {
            ArchiveEntry entry = invocation.getArgument(0);
            return new TarArchiveEntry("testFile.txt");
        });

        InputStream testInput = new ByteArrayInputStream(new byte[10]);
        InputStream testInputReplace = new ByteArrayInputStream(new byte[10]);

        when(inputStreamAdd.readAllBytes()).thenReturn(new byte[10]);
        when(inputStreamAdd.available()).thenReturn(10);

        when(inputStreamAdd.readAllBytes()).beforeEach(invocation -> testInputReplace = invocation.getMock());
        when(inputStreamAdd.available()).thenAnswer(invocation -> testInputReplace.available());

        performer.perform(archiveInputStream, archiveOutputStream);

        verify(archiveInputStream).close();
        verify(archiveOutputStream).finish();
        verify(results).addedFromChangeSet("testFile.txt");
        verify(results).addedFromStream("testFile.txt");
        verify(archiveOutputStream).putArchiveEntry(any(ArchiveEntry.class));
    }

    @Test
    public void testPerform_deleteChangeWithReplaceMode_fileDoesntExist() throws IOException {
        when(archiveInputStream.getNextEntry()).thenReturn(null);

        performer.perform(archiveInputStream, archiveOutputStream);

        verify(archiveInputStream).close();
        verify(archiveOutputStream).finish();
        verify(results, never()).deleted("testFile.txt");
    }

    @Test
    public void testPerform_deleteChangeWithReplaceMode_fileExists() throws IOException {
        when(archiveInputStream.getNextEntry()).thenReturn(mock(ArchiveEntry.class));

        when(archiveOutputStream.putArchiveEntry(any(ArchiveEntry.class))).thenAnswer(invocation -> null);

        performer.perform(archiveInputStream, archiveOutputStream);

        verify(archiveInputStream).close();
        verify(archiveOutputStream).finish();
        verify(results).deleted("testFile.txt");
    }

    @Test
    public void testPerform_deleteDirectoryChange_fileDoesntExist() throws IOException {
        when(archiveInputStream.getNextEntry()).thenReturn(null);

        performer.perform(archiveInputStream, archiveOutputStream);

        verify(archiveInputStream).close();
        verify(archiveOutputStream).finish();
        verify(results, never()).deleted("/testFile.txt");
    }

    @Test
    public void testPerform_deleteDirectoryChange_fileExists() throws IOException {
        when(archiveInputStream.getNextEntry()).thenReturn(mock(ArchiveEntry.class));

        when(archiveOutputStream.putArchiveEntry(any(ArchiveEntry.class))).thenAnswer(invocation -> null);

        performer.perform(archiveInputStream, archiveOutputStream);

        verify(archiveInputStream).close();
        verify(archiveOutputStream).finish();
        verify(results).deleted("/testFile.txt");
    }

    @Test
    public void testPerform_deleteDirectoryChange_directoryIsEmpty() throws IOException {
        Set changes = new HashSet(Arrays.asList(new Change(Change.TYPE_DELETE_DIR, "/testDir/", null, null, null)));
        ChangeSetPerformer performer = new ChangeSetPerformer(new ChangeSet(changes));
        when(archiveInputStream.getNextEntry()).thenReturn(null);

        performer.perform(archiveInputStream, archiveOutputStream);

        verify(archiveInputStream).close();
        verify(archiveOutputStream).finish();
    }

    @Test
    public void testPerform_deleteDirectoryChange_directoryIsNotEmpty() throws IOException {
        Set changes = new HashSet(Arrays.asList(new Change(Change.TYPE_DELETE_DIR, "/testDir/", null, null, null),
                new Change(Change.TYPE_ADD, "/testDir/testFile.txt", null, null, null)));
        ChangeSetPerformer performer = new ChangeSetPerformer(new ChangeSet(changes));
        when(archiveInputStream.getNextEntry()).thenReturn(mock(ArchiveEntry.class));

        performer.perform(archiveInputStream, archiveOutputStream);

        verify(archiveInputStream).close();
        verify(archiveOutputStream).finish();
        verify(results).deleted("/testDir/testFile.txt");
    }

    @Test
    public void testIsDeletedLater_noLaterDeletions() throws IOException {
        ArchiveEntry testEntry = mock(ArchiveEntry.class);
        Set changes = Collections.singleton(new Change(Change.TYPE_DELETE, null, null, null, null));

        performer = new ChangeSetPerformer(new ChangeSet(changes));

        assertFalse(performer.isDeletedLater(testEntry));
    }

    @Test
    public void testIsDeletedLater_laterDeletionWithoutReplaceMode() throws IOException {
        Set changes = new HashSet(Arrays.asList(new Change(Change.TYPE_DELETE, "testFile.txt", null, null, null),
                new Change(Change.TYPE_DELETE, null, null, null, null),
                new Change(Change.TYPE_ADD, "testFile.txt", null, null, null)));

        performer = new ChangeSetPerformer(new ChangeSet(changes));

        ArchiveEntry entry = mock(ArchiveEntry.class);
        when(entry.getName()).thenReturn("testFile.txt");

        assertFalse(performer.isDeletedLater(entry));
    }

    @Test
    public void testIsDeletedLater_laterDeletionWithReplaceMode() throws IOException {
        Set changes = new HashSet(Arrays.asList(new Change(Change.TYPE_DELETE, "testFile.txt", null, null, null),
                new Change(Change.TYPE_DELETE, null, null, null, null),
                new Change(Change.TYPE_ADD, "testFile.txt", null, null, null)));

        performer = new ChangeSetPerformer(new ChangeSet(changes));

        ArchiveEntry entry = mock(ArchiveEntry.class);
        when(entry.getName()).thenReturn("testFile.txt");

        assertTrue(performer.isDeletedLater(entry));
    }

    @Test
    public void testIsDeletedLater_laterDirectoryDeletionWithoutReplaceMode() throws IOException {
        Set changes = new HashSet(Arrays.asList(new Change(Change.TYPE_DELETE_DIR, "/testDir/", null, null, null),
                new Change(Change.TYPE_DELETE_DIR, null, null, null, null),
                new Change(Change.TYPE_ADD, "/testDir/testFile.txt", null, null, null)));

        performer = new ChangeSetPerformer(new ChangeSet(changes));

        ArchiveEntry entry = mock(ArchiveEntry.class);
        when(entry.getName()).thenReturn("/testDir/testFile.txt");

        assertFalse(performer.isDeletedLater(entry));
    }

    @Test
    public void testIsDeletedLater_laterDirectoryDeletionWithReplaceMode() throws IOException {
        Set changes = new HashSet(Arrays.asList(new Change(Change.TYPE_DELETE_DIR, "/testDir/", null, null, null),
                new Change(Change.TYPE_DELETE_DIR, null, null, null, null),
                new Change(Change.TYPE_ADD, "/testDir/testFile.txt", null, null, null)));

        performer = new ChangeSetPerformer(new ChangeSet(changes));

        ArchiveEntry entry = mock(ArchiveEntry.class);
        when(entry.getName()).thenReturn("/testDir/testFile.txt");

        assertTrue(performer.isDeletedLater(entry));
    }

    @Test
    public void testCopyStream() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[10]);
        ArchiveEntry archiveEntry = mock(ArchiveEntry.class);

        performer.copyStream(inputStream, archiveOutputStream, archiveEntry);

        byte[] result = outputStream.toByteArray();
        assertNull(inputStream.readAllBytes());
        assertArrayEquals(result, inputStreamAdd.readAllBytes());
    }
}
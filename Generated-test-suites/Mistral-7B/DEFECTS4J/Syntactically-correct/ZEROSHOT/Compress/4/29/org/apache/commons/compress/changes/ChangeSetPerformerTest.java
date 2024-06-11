package org.apache.commons.compress.changes;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class ChangeSetPerformerTest {

    @Mock
    private ArchiveInputStream mockArchiveInputStream;

    @Mock
    private ArchiveOutputStream mockArchiveOutputStream;

    private ChangeSetPerformer performer;

    private void setup(ChangeSet changeSet) {
        performer = new ChangeSetPerformer(changeSet);
    }

    @Test
    public void testPerform_noChanges() throws Exception {
        SetupNoChanges();

        ChangeSetResults result = performer.perform(mockArchiveInputStream, mockArchiveOutputStream);

        assertEquals(Collections.emptySet(), result.getAddedFromChangeSet());
        assertEquals(Collections.emptySet(), result.getDeleted());
        assertEquals(Collections.emptySet(), result.getAddedFromStream());
    }

    @Test
    public void testPerform_addChange() throws Exception {
        SetupAddChange("test.txt", new ByteArrayInputStream(new byte[0]), null);

        ChangeSetResults result = performer.perform(mockArchiveInputStream, mockArchiveOutputStream);

        assertEquals(Collections.singleton("test.txt"), result.getAddedFromChangeSet());
        assertEquals(Collections.emptySet(), result.getDeleted());
        assertEquals(Collections.emptySet(), result.getAddedFromStream());

        Mockito.verify(mockArchiveOutputStream).putNextEntry(ArgumentMatchers.isA(ArchiveEntry.class));
        Mockito.verifyNoMoreInteractions(mockArchiveOutputStream);

        Mockito.verifyZeroInteractions(mockArchiveInputStream);
    }

    @Test
    public void testPerform_deleteChange() throws Exception {
        SetupDeleteChange("test.txt", null, new ByteArrayInputStream(new byte[0]));

        ChangeSetResults result = performer.perform(mockArchiveInputStream, mockArchiveOutputStream);

        assertEquals(Collections.emptySet(), result.getAddedFromChangeSet());
        assertEquals(Collections.singleton("test.txt"), result.getDeleted());
        assertEquals(Collections.emptySet(), result.getAddedFromStream());

        Mockito.verifyZeroInteractions(mockArchiveOutputStream);

        Mockito.verify(mockArchiveInputStream).getNextEntry();
        Mockito.verify(mockArchiveOutputStream).closeArchiveEntry();
    }

    @Test
    public void testPerform_deleteDirectoryChange() throws Exception {
        SetupDeleteDirectoryChange("test/");

        ChangeSetResults result = performer.perform(mockArchiveInputStream, mockArchiveOutputStream);

        assertEquals(Collections.emptySet(), result.getAddedFromChangeSet());
        assertEquals(Collections.singleton("/test"), result.getDeleted());
        assertEquals(Collections.emptySet(), result.getAddedFromStream());

        Mockito.verifyZeroInteractions(mockArchiveOutputStream);

        Mockito.verify(mockArchiveInputStream).getNextEntry();
        Mockito.verify(mockArchiveOutputStream).closeArchiveEntry();
    }

    @Test(expected = IllegalStateException.class)
    public void testPerform_consecutiveAddChange() throws Exception {
        SetupAddChange("test.txt", new ByteArrayInputStream(new byte[0]), null);
        SetupAddChange("test2.txt", new ByteArrayInputStream(new byte[0]), null);

        performer.perform(mockArchiveInputStream, mockArchiveOutputStream);

        fail("Expected IllegalStateException for consecutive add change");
    }

    @Test(expected = IllegalStateException.class)
    public void testPerform_consecutiveDeleteChange() throws Exception {
        SetupDeleteChange("test.txt", null, new ByteArrayInputStream(new byte[0]));
        SetupDeleteChange("test.txt", null, new ByteArrayInputStream(new byte[0]));

        performer.perform(mockArchiveInputStream, mockArchiveOutputStream);

        fail("Expected IllegalStateException for consecutive delete change");
    }

    @Test(expected = IllegalStateException.class)
    public void testPerform_deleteAndAddChange() throws Exception {
        SetupDeleteChange("test.txt", null, new ByteArrayInputStream(new byte[0]));
        SetupAddChange("test.txt", new ByteArrayInputStream(new byte[0]), null);

        performer.perform(mockArchiveInputStream, mockArchiveOutputStream);

        fail("Expected IllegalStateException for delete and add change");
    }

    @Test
    public void testPerform_addChangeFromStream() throws Exception {
        SetupAddChangeFromStream("test.txt", new ByteArrayInputStream("Hello World".getBytes()), null);

        ChangeSetResults result = performer.perform(mockArchiveInputStream, mockArchiveOutputStream);

        assertEquals(Collections.emptySet(), result.getAddedFromChangeSet());
        assertEquals(Collections.emptySet(), result.getDeleted());
        assertEquals(Collections.singleton("test.txt"), result.getAddedFromStream());

        Mockito.verify(mockArchiveOutputStream).putNextEntry(ArgumentMatchers.isA(ArchiveEntry.class));
        Mockito.verify(mockArchiveOutputStream).write(ArgumentMatchers.anyByteArray());
        Mockito.verifyNoMoreInteractions(mockArchiveOutputStream);

        Mockito.verifyZeroInteractions(mockArchiveInputStream);
    }

    @Test
    public void testPerform_addChangeFromStreamAndChangeSet() throws Exception {
        SetupAddChangeFromStream("test.txt", new ByteArrayInputStream("Hello World".getBytes()), null);
        SetupAddChange("test.txt", new ByteArrayInputStream(new byte[0]), null);

        ChangeSetResults result = performer.perform(mockArchiveInputStream, mockArchiveOutputStream);

        assertEquals(Collections.singleton("test.txt"), result.getAddedFromChangeSet());
        assertEquals(Collections.singleton("test.txt"), result.getAddedFromStream());
        assertEquals(Collections.emptySet(), result.getDeleted());

        Mockito.verify(mockArchiveOutputStream).putNextEntry(ArgumentMatchers.isA(ArchiveEntry.class));
        Mockito.verify(mockArchiveOutputStream).write(ArgumentMatchers.anyByteArray());
        Mockito.verifyNoMoreInteractions(mockArchiveOutputStream);

        Mockito.verifyZeroInteractions(mockArchiveInputStream);
    }

    @Test
    public void testPerform_deleteLaterChange() throws Exception {
        SetupAddChange("test.txt", new ByteArrayInputStream(new byte[0]), null);
        SetupDeleteChange("test.txt", null, new ByteArrayInputStream(new byte[0]));

        ChangeSetResults result = performer.perform(mockArchiveInputStream, mockArchiveOutputStream);

        assertEquals(Collections.emptySet(), result.getAddedFromChangeSet());
        assertEquals(Collections.emptySet(), result.getDeleted());
        assertEquals(Collections.singleton("test.txt"), result.getAddedFromStream());

        Mockito.verifyZeroInteractions(mockArchiveOutputStream);

        Mockito.verify(mockArchiveInputStream).getNextEntry();
        Mockito.verify(mockArchiveOutputStream).closeArchiveEntry();
    }

    private void SetupNoChanges() {
        ChangeSet changeSet = new ChangeSet();
        setup(changeSet);
    }

    private void SetupAddChange(String fileName, InputStream inputStream, ArchiveEntry entry) {
        Change change = new Change(Change.TYPE_ADD, new FileName(fileName), inputStream, entry, false);
        ChangeSet changeSet = new ChangeSet();
        changeSet.addChange(change);
        setup(changeSet);
    }

    private void SetupDeleteChange(String fileName, InputStream inputStream, ArchiveEntry entry) {
        Change change = new Change(Change.TYPE_DELETE, new FileName(fileName), null, entry, false);
        ChangeSet changeSet = new ChangeSet();
        changeSet.addChange(change);
        setup(changeSet);
    }

    private void SetupDeleteDirectoryChange(String directoryPath) {
        Change change = new Change(Change.TYPE_DELETE_DIR, new FileName(directoryPath), null, null, false);
        ChangeSet changeSet = new ChangeSet();
        changeSet.addChange(change);
        setup(changeSet);
    }

    private void SetupAddChangeFromStream(String fileName, InputStream inputStream, ArchiveEntry entry) {
        Change change = new Change(Change.TYPE_ADD, new FileName(fileName), inputStream, entry, false);
        ChangeSet changeSet = new ChangeSet();
        changeSet.addChange(change);
        setup(changeSet);
    }
}
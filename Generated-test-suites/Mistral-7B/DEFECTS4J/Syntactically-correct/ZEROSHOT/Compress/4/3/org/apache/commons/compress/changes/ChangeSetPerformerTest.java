import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.compress.utils.TestUtils;
import org.junit.Assert;
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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ChangeSetPerformerTest {

    private ChangeSetPerformer performer;
    private ChangeSet changeSet;
    private ArchiveInputStream inputStreamMock;
    private ArchiveOutputStream outputStreamMock;

    @Before
    public void setUp() throws Exception {
        changeSet = mock(ChangeSet.class);
        performer = new ChangeSetPerformer(changeSet);

        inputStreamMock = mock(ArchiveInputStream.class);
        outputStreamMock = mock(ArchiveOutputStream.class);
    }

    @Test
    public void testPerform_AddAndDelete() throws Exception {
        Set<Change> changes = new HashSet<>();
        changes.add(TestUtils.createChange("test.txt", "test input.txt", Change.TYPE_ADD, false));
        changes.add(TestUtils.createChange("test.txt", null, Change.TYPE_DELETE, false));

        when(changeSet.getChanges()).thenReturn(changes);

        ArchiveEntry entryMock = mock(ArchiveEntry.class);
        when(inputStreamMock.getNextEntry()).thenReturn(entryMock);
        when(entryMock.getName()).thenReturn("test.txt");

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        when(outputStreamMock.getOutputStream()).thenReturn(outputStream);

        ChangeSetResults result = performer.perform(inputStreamMock, outputStreamMock);

        Assert.assertFalse(Arrays.asList(result.getDeleted()).contains("test.txt"));
        Assert.assertEquals(Collections.singletonList("test.txt"), result.getAddedFromChangeSet());
        Assert.assertEquals(Collections.singletonList("test.txt"), result.getAddedFromStream());

        Mockito.verify(outputStreamMock).putArchiveEntry(entryMock);
        Mockito.verifyNoMoreInteractions(inputStreamMock);
        Mockito.verifyNoMoreInteractions(outputStreamMock);
    }

    @Test
    public void testPerform_AddAndReplace() throws Exception {
        Set<Change> changes = new HashSet<>();
        changes.add(TestUtils.createChange("test.txt", "test input.txt", Change.TYPE_ADD, true));
        changes.add(TestUtils.createChange("test.txt", "test output.txt", Change.TYPE_ADD, false));

        when(changeSet.getChanges()).thenReturn(changes);

        ArchiveEntry entryMock = mock(ArchiveEntry.class);
        when(inputStreamMock.getNextEntry()).thenReturn(entryMock);
        when(entryMock.getName()).thenReturn("test.txt");

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        when(outputStreamMock.getOutputStream()).thenReturn(outputStream);

        ByteArrayInputStream inputStream = new ByteArrayInputStream("test input.txt".getBytes());
        when(inputStreamMock.isAvailable()).thenReturn(inputStream.available());
        when(inputStreamMock.readAllBytes()).thenReturn("test input.txt".getBytes());

        ChangeSetResults result = performer.perform(inputStreamMock, outputStreamMock);

        Assert.assertEquals(Collections.singletonList("test output.txt"), result.getAddedFromChangeSet());
        Assert.assertEquals(Collections.singletonList("test output.txt"), result.getAddedFromStream());
        Assert.assertEquals("test output.txt", result.getAddedFromStream().get(0));

        Mockito.verify(outputStreamMock).putArchiveEntry(entryMock);
        Mockito.verify(outputStreamMock).write(new byte[0]);
        Mockito.verify(outputStreamMock).closeArchiveEntry();
        Mockito.verify(outputStreamMock).flush();
    }

    @Test(expected = IOException.class)
    public void testPerform_NoEntry() throws Exception {
        Set<Change> changes = new HashSet<>();
        changes.add(TestUtils.createChange("test.txt", null, Change.TYPE_DELETE, false));

        when(changeSet.getChanges()).thenReturn(changes);
        when(inputStreamMock.getNextEntry()).thenReturn(null);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        when(outputStreamMock.getOutputStream()).thenReturn(outputStream);

        performer.perform(inputStreamMock, outputStreamMock);

        Mockito.verifyNoMoreInteractions(inputStreamMock);
        Mockito.verifyNoMoreInteractions(outputStreamMock);
    }

    @Test
    public void testPerform_MultipleEntries() throws Exception {
        Set<Change> changes = new HashSet<>();
        changes.add(TestUtils.createChange("test1.txt", null, Change.TYPE_DELETE, false));
        changes.add(TestUtils.createChange("test2.txt", "test2 input.txt", Change.TYPE_ADD, false));
        changes.add(TestUtils.createChange("test2.txt", "test2 output.txt", Change.TYPE_ADD, false));

        when(changeSet.getChanges()).thenReturn(changes);

        ArchiveEntry entry1Mock = mock(ArchiveEntry.class);
        when(inputStreamMock.getNextEntry()).thenReturn(entry1Mock).thenReturn(entry1Mock).thenReturn(null);
        when(entry1Mock.getName()).thenReturn("test1.txt");

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        when(outputStreamMock.getOutputStream()).thenReturn(outputStream);

        ByteArrayInputStream inputStream = new ByteArrayInputStream("test2 input.txt".getBytes());
        when(inputStreamMock.isAvailable()).thenReturn(inputStream.available());
        when(inputStreamMock.readAllBytes()).thenReturn("test2 input.txt".getBytes());

        ChangeSetResults result = performer.perform(inputStreamMock, outputStreamMock);

        Assert.assertEquals(Arrays.asList("test2 output.txt"), result.getAddedFromChangeSet());
        Assert.assertEquals(Arrays.asList("test2 output.txt"), result.getAddedFromStream());
        Assert.assertEquals("test2 output.txt", result.getAddedFromStream().get(0));

        Mockito.verify(outputStreamMock, Mockito.times(2)).putArchiveEntry(entry1Mock);
        Mockito.verify(outputStreamMock).write(new byte[0]);
        Mockito.verify(outputStreamMock).closeArchiveEntry();
        Mockito.verify(outputStreamMock).flush();
        Mockito.verifyNoMoreInteractions(inputStreamMock);
    }

    @Test(expected = IOException.class)
    public void testPerform_IOException() throws Exception {
        Set<Change> changes = new HashSet<>();
        changes.add(TestUtils.createChange("test.txt", null, Change.TYPE_DELETE, false));

        when(changeSet.getChanges()).thenReturn(changes);

        ArchiveEntry entryMock = mock(ArchiveEntry.class);
        when(inputStreamMock.getNextEntry()).thenReturn(entryMock);
        when(entryMock.getName()).thenReturn("test.txt");

        ByteArrayOutputStream outputStream = mock(ByteArrayOutputStream.class);
        when(outputStreamMock.getOutputStream()).thenThrow(new IOException());

        performer.perform(inputStreamMock, outputStreamMock);

        Mockito.verify(inputStreamMock).close();
        Mockito.verifyNoMoreInteractions(inputStreamMock);
        Mockito.verifyNoMoreInteractions(outputStreamMock);
    }
}
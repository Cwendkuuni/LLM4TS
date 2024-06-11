import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
public class ChangeSetPerformerTest {
    @Test
    public void testPerform() throws Exception {
        ChangeSet changeSet = Mockito.mock(ChangeSet.class);
        Set changes = new LinkedHashSet<>();
        Change addChange = new Change("add", "inputAdd.txt", null, null);
        Change deleteChange = new Change("delete", "inputDelete.txt", "outputDelete.txt", null);
        changes.add(addChange);
        changes.add(deleteChange);
        Mockito.when(changeSet.getChanges()).thenReturn(changes);

        InputStream inputStream = Mockito.mock(InputStream.class);
        ArchiveInputStream archiveInputStream = Mockito.mock(ArchiveInputStream.class);
        Mockito.when(archiveInputStream.getNextEntry()).thenReturn(new ArchiveEntry("inputAdd.txt"));
        Mockito.when(archiveInputStream.getNextEntry()).thenReturn(new ArchiveEntry("inputDelete.txt"));
        Mockito.when(archiveInputStream.getNextEntry()).thenReturn(null);
        Mockito.when(inputStream.readAllBytes()).thenReturn(new byte[0]);
        Mockito.when(inputStream.available()).thenReturn(0);

        OutputStream outputStream = Mockito.mock(OutputStream.class);
        ArchiveOutputStream archiveOutputStream = Mockito.mock(ArchiveOutputStream.class);
        Mockito.when(archiveOutputStream.putArchiveEntry(Mockito.any(ArchiveEntry.class))).thenReturn(new ArchiveEntry("outputAdd.txt"));
        Mockito.when(archiveOutputStream.putArchiveEntry(Mockito.any(ArchiveEntry.class))).thenReturn(new ArchiveEntry("outputDelete.txt"));
        Mockito.when(archiveOutputStream.closeArchiveEntry()).thenReturn(null);
        Mockito.when(archiveOutputStream.finishStream()).thenReturn(true);

        ChangeSetPerformer changeSetPerformer = new ChangeSetPerformer(changeSet);
        ChangeSetResults result = changeSetPerformer.perform(archiveInputStream, archiveOutputStream, inputStream, outputStream);

        Assert.assertEquals(ChangeSetResults.Status.SUCCESS, result.getStatus());
        Assert.assertTrue(result.hasAdded("outputAdd.txt"));
        Assert.assertTrue(result.hasDeleted("outputDelete.txt"));

        Mockito.verify(inputStream).close();
        Mockito.verify(outputStream).close();
    }

    // Testing edge cases and exception handling scenarios here
}
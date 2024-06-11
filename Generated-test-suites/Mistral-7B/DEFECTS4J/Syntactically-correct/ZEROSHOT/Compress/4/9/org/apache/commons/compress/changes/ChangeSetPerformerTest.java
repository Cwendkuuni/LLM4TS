import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.compress.changes.Change;
import org.apache.commons.compress.changes.ChangeSet;
import org.apache.commons.compress.changes.ChangeSetPerformer;
import org.apache.commons.compress.utils.TestUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public class ChangeSetPerformerTest {

    private ChangeSet changeSet;
    private ChangeSetPerformer performer;

    @Before
    public void setUp() throws IOException {
        Change addChange = new Change(Change.TYPE_ADD, "test.txt", null, null, null, null);
        Change addReplaceChange = new Change(Change.TYPE_ADD, "test.txt", "test2.txt", null, null, null, true);
        Change deleteChange = new Change(Change.TYPE_DELETE, "test.txt", null, null, null, null);
        Change deleteDirChange = new Change(Change.TYPE_DELETE_DIR, "dir/", null, null, null, null);

        Set changes = new LinkedHashSet<>();
        changes.add(addChange);
        changes.add(addReplaceChange);
        changes.add(deleteChange);
        changes.add(deleteDirChange);

        this.changeSet = new ChangeSet(changes);
        this.performer = new ChangeSetPerformer(changeSet);
    }

    @Test
    public void testPerform_NoChanges() throws IOException {
        ByteArrayOutputStream inputStream = new ByteArrayOutputStream();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        ChangeSetResults result = performer.perform(new ArchiveInputStream(new ByteArrayInputStream(TestUtils.getEmptyArchive())), new ArchiveOutputStream(outputStream));

        Assert.assertEquals(Collections.emptySet(), result.getAddedFromChangeSet());
        Assert.assertEquals(Collections.emptySet(), result.getAddedFromStream());
        Assert.assertEquals(Collections.emptySet(), result.getDeleted());
    }

    @Test
    public void testPerform_AddFile() throws IOException {
        ByteArrayOutputStream inputStream = new ByteArrayOutputStream();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        InputStream testFileInputStream = new ByteArrayInputStream(new byte[]{0x61, 0x62, 0x63});

        ArchiveEntry testFileEntry = new ArchiveEntry("test.txt");
        testFileEntry.setSize(3);

        inputStream.write(TestUtils.getArchiveEntryData(new Object[]{testFileEntry}));
        inputStream.write(TestUtils.getArchiveEntryData(new Object[]{new ArchiveEntry("another.txt")}));

        ChangeSetResults result = performer.perform(new ArchiveInputStream(inputStream), new ArchiveOutputStream(outputStream));

        Assert.assertEquals(Arrays.asList("test.txt"), result.getAddedFromChangeSet());
        Assert.assertEquals(Arrays.asList("test.txt"), result.getAddedFromStream());
        Assert.assertEquals(Collections.emptySet(), result.getDeleted());

        byte[] resultContent = outputStream.toByteArray();
        byte[] expectedContent = new byte[]{0x61, 0x62, 0x63, 0x61, 0x62, 0x63, 0};
        Assert.assertArrayEquals(expectedContent, Arrays.copyOfRange(resultContent, 0, resultContent.length - 1));
    }

    @Test
    public void testPerform_AddReplaceFile() throws IOException {
        ByteArrayOutputStream inputStream = new ByteArrayOutputStream();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        InputStream testFileInputStream = new ByteArrayInputStream(new byte[]{0x61, 0x62, 0x63});
        InputStream testFileInputStreamReplace = new ByteArrayInputStream(new byte[]{0x65, 0x65, 0x65});

        ArchiveEntry testFileEntry = new ArchiveEntry("test.txt");
        testFileEntry.setSize(3);

        ArchiveEntry testFileEntryReplace = new ArchiveEntry("test2.txt");
        testFileEntryReplace.setSize(3);

        inputStream.write(TestUtils.getArchiveEntryData(new Object[]{testFileEntry, new ArchiveEntry("another.txt")}));

        ChangeSetResults result = performer.perform(new ArchiveInputStream(inputStream), new ArchiveOutputStream(outputStream));

        Assert.assertEquals(Collections.singleton("test.txt"), result.getAddedFromChangeSet());
        Assert.assertEquals(Collections.emptySet(), result.getAddedFromStream());
        Assert.assertEquals(Collections.emptySet(), result.getDeleted());

        byte[] resultContent = outputStream.toByteArray();
        byte[] expectedContent = new byte[]{0x65, 0x65, 0x65, 0x61, 0x62, 0x63, 0};
        Assert.assertArrayEquals(expectedContent, Arrays.copyOfRange(resultContent, 0, resultContent.length - 1));
    }

    @Test
    public void testPerform_DeleteFile() throws IOException {
        ByteArrayOutputStream inputStream = new ByteArrayOutputStream();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        InputStream testFileInputStream = new ByteArrayInputStream(new byte[]{0x61, 0x62, 0x63});

        ArchiveEntry testFileEntry = new ArchiveEntry("test.txt");
        testFileEntry.setSize(3);

        inputStream.write(TestUtils.getArchiveEntryData(new Object[]{testFileEntry, new ArchiveEntry("another.txt")}));

        ChangeSetResults result = performer.perform(new ArchiveInputStream(inputStream), new ArchiveOutputStream(outputStream));

        Assert.assertEquals(Collections.emptySet(), result.getAddedFromChangeSet());
        Assert.assertEquals(Collections.emptySet(), result.getAddedFromStream());
        Assert.assertEquals(Arrays.asList("test.txt"), result.getDeleted());

        byte[] resultContent = outputStream.toByteArray();
        Assert.assertArrayEquals(new byte[]{0}, resultContent);
    }

    @Test
    public void testPerform_DeleteDirectory() throws IOException {
        ByteArrayOutputStream inputStream = new ByteArrayOutputStream();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        InputStream testFileInputStream = new ByteArrayInputStream(new byte[]{0x61, 0x62, 0x63});

        ArchiveEntry testFileEntry = new ArchiveEntry("dir/test.txt");
        testFileEntry.setSize(3);

        ArchiveEntry dirEntry = new ArchiveEntry("dir/");
        dirEntry.setNameStart("dir/");
        dirEntry.setSize(0);

        inputStream.write(TestUtils.getArchiveEntryData(new Object[]{testFileEntry, dirEntry, new ArchiveEntry("another.txt")}));

        ChangeSetResults result = performer.perform(new ArchiveInputStream(inputStream), new ArchiveOutputStream(outputStream));

        Assert.assertEquals(Collections.emptySet(), result.getAddedFromChangeSet());
        Assert.assertEquals(Collections.emptySet(), result.getAddedFromStream());
        Assert.assertEquals(Arrays.asList("dir/test.txt"), result.getDeleted());

        byte[] resultContent = outputStream.toByteArray();
        Assert.assertArrayEquals(new byte[]{0}, resultContent);
    }

    @Test
    public void testPerform_AddFileAfterDelete() throws IOException {
        ByteArrayOutputStream inputStream = new ByteArrayOutputStream();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        InputStream testFileInputStream = new ByteArrayInputStream(new byte[]{0x61, 0x62, 0x63});

        ArchiveEntry testFileEntry = new ArchiveEntry("test.txt");
        testFileEntry.setSize(3);

        ArchiveEntry dirEntry = new ArchiveEntry("dir/");
        dirEntry.setNameStart("dir/");
        dirEntry.setSize(0);

        inputStream.write(TestUtils.getArchiveEntryData(new Object[]{testFileEntry, dirEntry, new ArchiveEntry("another.txt")}));

        ChangeSetResults result = performer.perform(new ArchiveInputStream(inputStream), new ArchiveOutputStream(outputStream));

        Assert.assertEquals(Collections.emptySet(), result.getAddedFromChangeSet());
        Assert.assertEquals(Collections.emptySet(), result.getAddedFromStream());
        Assert.assertEquals(Arrays.asList("test.txt"), result.getDeleted());

        inputStream.reset();
        inputStream.write(TestUtils.getArchiveEntryData(new Object[]{testFileEntry}));

        result = performer.perform(new ArchiveInputStream(inputStream), new ArchiveOutputStream(outputStream));

        Assert.assertEquals(Collections.singleton("test.txt"), result.getAddedFromChangeSet());
        Assert.assertEquals(Collections.emptySet(), result.getAddedFromStream());
        Assert.assertEquals(Collections.emptySet(), result.getDeleted());

        byte[] resultContent = outputStream.toByteArray();
        byte[] expectedContent = new byte[]{0x61, 0x62, 0x63, 0};
        Assert.assertArrayEquals(expectedContent, Arrays.copyOfRange(resultContent, 0, resultContent.length - 1));
    }
}
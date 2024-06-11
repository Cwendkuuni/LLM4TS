import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.compress.compressors.gzip.GzipOutputStream;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public class ChangeSetPerformerTest {

    private ChangeSetPerformer performer;
    private Set<Change> changeSet;

    @Before
    public void setUp() {
        changeSet = new LinkedHashSet<>();
        changeSet.add(new Change(Change.TYPE_ADD, new String[]{"test.txt"}, null, null, false));
        changeSet.add(new Change(Change.TYPE_DELETE, null, "test.txt", null, false));
        performer = new ChangeSetPerformer(new MockChangeSet(changeSet));
    }

    @Test
    public void testPerform_NoChanges() throws IOException {
        ArchiveInputStream in = Mockito.mock(ArchiveInputStream.class);
        ArchiveOutputStream out = Mockito.mock(ArchiveOutputStream.class);

        Mockito.when(in.getNextEntry()).thenReturn(null);

        ChangeSetResults result = performer.perform(in, out);

        Mockito.verify(in, Mockito.times(1)).close();
        Mockito.verify(out, Mockito.times(1)).finish();
        Assert.assertEquals(Arrays.asList(), result.getAddedFromChangeSet());
        Assert.assertEquals(Collections.emptyList(), result.getDeleted());
        Assert.assertEquals(Collections.emptyList(), result.getAddedFromStream());
    }

    @Test
    public void testPerform_AddFile() throws IOException {
        ArchiveInputStream in = createArchiveInput("test.txt", "test data");
        ArchiveOutputStream out = new ArchiveOutputStream(new ByteArrayOutputStream());

        ChangeSetResults result = performer.perform(in, out);

        Mockito.verify(in, Mockito.times(1)).close();
        Mockito.verify(out, Mockito.times(1)).finish();
        Assert.assertEquals(Arrays.asList("test.txt"), result.getAddedFromChangeSet());
        Assert.assertEquals(Collections.emptyList(), result.getDeleted());
        Assert.assertEquals(Collections.singletonList("test.txt"), result.getAddedFromStream());
    }

    @Test
    public void testPerform_DeleteFile() throws IOException {
        ArchiveInputStream in = createArchiveInput("test.txt", "test data");
        ArchiveOutputStream out = new ArchiveOutputStream(new ByteArrayOutputStream());

        ChangeSetResults result = performer.perform(in, out);

        Mockito.verify(in, Mockito.times(1)).close();
        Mockito.verify(out, Mockito.times(1)).finish();
        Assert.assertEquals(Collections.emptyList(), result.getAddedFromChangeSet());
        Assert.assertEquals(Collections.singletonList("test.txt"), result.getDeleted());
        Assert.assertEquals(Collections.emptyList(), result.getAddedFromStream());
    }

    @Test(expected = IOException.class)
    public void testPerform_InputError() throws IOException {
        ArchiveInputStream in = Mockito.mock(ArchiveInputStream.class);
        ArchiveOutputStream out = Mockito.mock(ArchiveOutputStream.class);

        Mockito.when(in.getNextEntry()).thenThrow(new IOException("Input error"));

        performer.perform(in, out);
    }

    @Test(expected = IOException.class)
    public void testPerform_OutputError() throws IOException {
        ArchiveInputStream in = createArchiveInput("test.txt", "test data");
        ArchiveOutputStream out = new ArchiveOutputStream(new ByteArrayOutputStream()) {
            @Override
            public void finish() throws IOException {
                throw new IOException("Output error");
            }
        };

        performer.perform(in, out);
    }

    private ArchiveInputStream createArchiveInput(String name, String content) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ArchiveOutputStream aos = new ArchiveOutputStream(os);
        aos.putNextEntry(new MyArchiveEntry(name));
        IOUtils.write(content.getBytes(), aos, content.length());
        aos.close();
        InputStream is = new ByteArrayInputStream(os.toByteArray());
        return new ArchiveInputStream(is) {
            @Override
            public ArchiveEntry getNextEntry() throws IOException {
                return new MyArchiveEntry(name);
            }
        };
    }

    private static class MyArchiveEntry extends ArchiveEntry {
        public MyArchiveEntry(String name) {
            super(name, -1, null);
        }

        @Override
        public long getSize() {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getName() {
            return getName();
        }
    }
}

class MockChangeSet implements ChangeSet {
    private final Set<Change> changes;

    public MockChangeSet(Set<Change> changes) {
        this.changes = changes;
    }

    @Override
    public Set getChanges() {
        return changes;
    }
}

class Change {
    private final int type;
    private final String[] targetFiles;
    private final String targetPath;
    private final ArchiveEntry entry;
    private final boolean isReplaceMode;

    public Change(int type, String[] targetFiles, String targetPath, ArchiveEntry entry, boolean isReplaceMode) {
        this.type = type;
        this.targetFiles = targetFiles;
        this.targetPath = targetPath;
        this.entry = entry;
        this.isReplaceMode = isReplaceMode;
    }

    public int type() {
        return type;
    }

    public String[] getTargetFiles() {
        return targetFiles;
    }

    public String getTargetPath() {
        return targetPath;
    }

    public ArchiveEntry getEntry() {
        return entry;
    }

    public boolean isReplaceMode() {
        return isReplaceMode;
    }
}
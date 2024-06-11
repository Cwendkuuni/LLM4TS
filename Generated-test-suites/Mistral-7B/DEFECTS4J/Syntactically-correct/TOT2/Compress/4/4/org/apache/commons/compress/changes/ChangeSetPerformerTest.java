package org.junit.tests;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.test.TestFiles;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.compress.utils.byteArrays.ByteArrayInputStream;
import org.apache.commons.compress.utils.byteArrays.ByteArrayOutputStream;
import org.apache.commons.compress.utils.io.IdentityFileFilter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class ChangeSetPerformerTest {
    private ChangeSet changeSet;
    private ChangeSetPerformer performer;

    @Before
    public void setup() {
        Set<Change> changes = new HashSet<>();
        changes.add(new Change("add.txt", "add.txt", Change.TYPE_ADD, true));
        changeSet = new ChangeSet("test", changes);
        performer = new ChangeSetPerformer(changeSet);
    }

    @Test
    public void testConstructor() {
        Assert.assertNotNull(performer);
    }

    @Test
    public void testPerform_NormalOperation() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Test data".getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ArchiveInputStream archiveInputStream = new ArchiveInputStream(new ByteArrayInputStream(TestFiles.TEST_ZIP.getBytes()), new IdentityFileFilter());
        ArchiveOutputStream archiveOutputStream = new ArchiveOutputStream(outputStream, new DefaultArchiveOutputStreamFactory().createArchiveOutputStream("test.zip"));

        ChangeSetResults results = performer.perform(archiveInputStream, archiveOutputStream);
        Assert.assertEquals("Test data", IOUtils.toString(new ByteArrayInputStream(outputStream.toByteArray())));
        Assert.assertTrue(results.hasBeenAdded("add.txt"));
    }

    @Test(expected = IOException.class)
    public void testPerform_IOException() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Test data".getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ArchiveInputStream archiveInputStream = new ArchiveInputStream(new ByteArrayInputStream(TestFiles.TEST_ZIP.getBytes()), new IdentityFileFilter());
        ArchiveOutputStream archiveOutputStream = new ArchiveOutputStream(new ByteArrayInputStream(new byte[0]), new DefaultArchiveOutputStreamFactory().createArchiveOutputStream("test.zip"));

        performer.perform(archiveInputStream, archiveOutputStream);
    }

    @Test
    public void testPerform_NoChanges() throws IOException {
        Set<Change> changes = new HashSet<>();
        ChangeSet changeSet = new ChangeSet("test", changes);
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        ByteArrayInputStream inputStream = new ByteArrayInputStream("Test data".getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ArchiveInputStream archiveInputStream = new ArchiveInputStream(new ByteArrayInputStream(TestFiles.TEST_ZIP.getBytes()), new IdentityFileFilter());
        ArchiveOutputStream archiveOutputStream = new ArchiveOutputStream(outputStream, new DefaultArchiveOutputStreamFactory().createArchiveOutputStream("test.zip"));

        ChangeSetResults results = performer.perform(archiveInputStream, archiveOutputStream);

        Assert.assertFalse(results.hasBeenAdded("add.txt"));
    }

    // ... Add other test cases for edge cases and exceptions
}
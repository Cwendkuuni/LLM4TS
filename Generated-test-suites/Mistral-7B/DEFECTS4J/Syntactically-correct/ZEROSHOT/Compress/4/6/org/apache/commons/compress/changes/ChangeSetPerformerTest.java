import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.compress.utils.TestUtilities;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ChangeSetPerformerTest {

    private ChangeSet changeSet;
    private ChangeSetPerformer changeSetPerformer;
    private ArchiveInputStream input;
    private ArchiveOutputStream output;
    private ByteArrayOutputStream outputStream;
    private ByteArrayInputStream inputStream;

    @Before
    public void createChangeSetAndPerformer() throws IOException {
        changeSet = new ChangeSet();
        changeSet.addChange(new Change("test.txt", "test.txt", Change.TYPE_ADD, false));
        changeSet.addChange(new Change("test2.txt", "test2.txt", Change.TYPE_DELETE, false));
        changeSet.addChange(new Change("test3.dir/test3.txt", "test3.dir/test3.txt", Change.TYPE_DELETE_DIR, false));

        changeSetPerformer = new ChangeSetPerformer(changeSet);
    }

    @After
    public void closeResources() throws IOException {
        if (input != null) {
            input.close();
        }
        if (output != null) {
            output.finish();
            output.close();
        }
        if (outputStream != null) {
            outputStream.close();
        }
        if (inputStream != null) {
            inputStream.close();
        }
    }

    @Test
    public void testPerform_addAndDelete() throws IOException {
        outputStream = new ByteArrayOutputStream();
        output = new ArchiveOutputStream("test.tar", TestUtilities.createGzipCompressor());
        output.setOutputStream(outputStream);

        inputStream = new ByteArrayInputStream(TestUtilities.createTestTarFile().toBytes());
        input = new ArchiveInputStream(inputStream);

        ChangeSetResults results = changeSetPerformer.perform(input, output);

        assertNotNull(results);
        assertEquals(Arrays.asList("test.txt"), results.getAddedFromChangeSet());
        assertEquals(Arrays.asList("test2.txt"), results.getDeleted());
        assertEquals(Collections.emptyList(), results.getAddedFromStream());

        Assert.assertEquals(0, outputStream.size());
    }

    @Test
    public void testPerform_addAndDeleteWithCopyStream() throws IOException {
        outputStream = new ByteArrayOutputStream();
        output = new ArchiveOutputStream("test.tar", TestUtilities.createGzipCompressor());
        output.setOutputStream(outputStream);

        inputStream = new ByteArrayInputStream(TestUtilities.createTestTarFile().toBytes());
        input = new ArchiveInputStream(inputStream);

        ChangeSetPerformer changeSetPerformerCopy = new ChangeSetPerformer(changeSet);
        ChangeSetResults resultsCopy = changeSetPerformerCopy.perform(input, output);

        assertNotNull(resultsCopy);
        assertEquals(Arrays.asList("test.txt"), resultsCopy.getAddedFromChangeSet());
        assertEquals(Arrays.asList("test2.txt"), resultsCopy.getDeleted());
        assertEquals(Collections.emptyList(), resultsCopy.getAddedFromStream());

        Assert.assertEquals(0, outputStream.size());

        ArchiveEntry testEntry = input.getNextEntry();
        assertNotNull(testEntry);

        ByteArrayInputStream testInputStream = new ByteArrayInputStream(TestUtilities.createTestTxtFile().toBytes());
        ByteArrayOutputStream testOutputStream = new ByteArrayOutputStream();

        ByteArrayInputStream testInput = new ByteArrayInputStream(TestUtilities.createTestTarFile().toBytes());
        ArchiveInputStream testInputArchive = new ArchiveInputStream(testInput);
        ArchiveOutputStream testOutputArchive = new ArchiveOutputStream("test.tar", TestUtilities.createGzipCompressor());
        testOutputArchive.setOutputStream(testOutputStream);

        changeSetPerformer.perform(testInputArchive, testOutputArchive);
        testInputArchive.close();
        testOutputArchive.finish();
        testOutputArchive.close();

        ByteArrayInputStream testOutput = new ByteArrayInputStream(testOutputStream.toByteArray());
        ArchiveInputStream testOutputArchive2 = new ArchiveInputStream(testOutput);
        ArchiveEntry testEntry2 = testOutputArchive2.getNextEntry();

        byte[] testData = TestUtilities.getTestFileAsBytes("test.txt");

        Assert.assertArrayEquals(testData, IOUtils.toByteArray(testEntry2.getInputStream()));
    }
}
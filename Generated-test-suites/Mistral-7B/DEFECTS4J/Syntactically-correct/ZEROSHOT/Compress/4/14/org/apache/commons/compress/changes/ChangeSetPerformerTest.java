import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.compress.utils.TestUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ChangeSetPerformerTest {

    @Mock
    private ArchiveInputStream inputMock;

    @Mock
    private ArchiveOutputStream outputMock;

    @InjectMocks
    private ChangeSetPerformer performer;

    private ByteArrayOutputStream outputStream;
    private ChangeSet changeSet;

    @Before
    public void setUp() throws Exception {
        outputStream = new ByteArrayOutputStream();
        changeSet = mock(ChangeSet.class);
        performer = new ChangeSetPerformer(changeSet);
    }

    @After
    public void tearDown() throws Exception {
        TestUtils.cleanTempFiles();
    }

    @Test
    public void testPerform_NoChanges() throws IOException {
        // Given
        setUpTestStreams();

        // When
        ChangeSetResults result = performer.perform(inputMock, outputMock);

        // Then
        assertEquals(ChangeSetResults.EMPTY, result);
        verify(outputMock).finish();
    }

    @Test
    public void testPerform_AddFile() throws IOException {
        // Given
        setUpTestStreams();
        ArchiveEntry entry = mockArchiveEntry("test.txt");
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Test content".getBytes());

        // When
        whenInputReturnsEntry(inputMock, entry);
        whenInputReturnsInput(inputMock, inputStream);
        ChangeSetResults result = performer.perform(inputMock, outputMock);

        // Then
        assertEquals(Collections.singletonList("test.txt"), result.getAddedFromChangeSet());
        verify(outputMock).putArchiveEntry(entry);
        verify(outputMock).write(any(byte[].class));
        verify(outputMock).closeArchiveEntry();
    }

    @Test
    public void testPerform_DeleteFile() throws IOException {
        // Given
        setUpTestStreams();
        ArchiveEntry entry = mockArchiveEntry("test.txt");

        // When
        whenInputReturnsEntry(inputMock, entry);
        ChangeSetResults result = performer.perform(inputMock, outputMock);

        // Then
        assertEquals(Collections.singletonList("test.txt"), result.getDeleted());
        verify(outputMock).finish();
        verify(outputMock).close();
    }

    @Test
    public void testPerform_AddAndDeleteFile() throws IOException {
        // Given
        setUpTestStreams();
        ArchiveEntry addEntry = mockArchiveEntry("test_add.txt");
        ArchiveEntry deleteEntry = mockArchiveEntry("test_delete.txt");

        // When
        whenInputReturnsEntry(inputMock, addEntry);
        whenInputReturnsEntry(inputMock, deleteEntry);
        ChangeSetResults result = performer.perform(inputMock, outputMock);

        // Then
        assertEquals(Collections.singletonList("test_add.txt"), result.getAddedFromChangeSet());
        assertEquals(Collections.singletonList("test_delete.txt"), result.getDeleted());
        verify(outputMock).finish();
        verify(outputMock).close();
    }

    @Test
    public void testPerform_AddDirectory() throws IOException {
        // Given
        setUpTestStreams();
        ArchiveEntry directoryEntry = mockArchiveEntry("test_directory/");

        // When
        whenInputReturnsEntry(inputMock, directoryEntry);
        ChangeSetResults result = performer.perform(inputMock, outputMock);

        // Then
        assertEquals(Collections.singletonList("test_directory/"), result.getAddedFromChangeSet());
        verify(outputMock).putArchiveEntry(directoryEntry);
        verify(outputMock).startEntry();
        verify(outputMock).write(any(byte[].class));
        verify(outputMock).closeArchiveEntry();
    }

    @Test
    public void testPerform_DeleteDirectory() throws IOException {
        // Given
        setUpTestStreams();
        ArchiveEntry directoryEntry = mockArchiveEntry("test_directory/");

        // When
        whenInputReturnsEntry(inputMock, directoryEntry);
        ChangeSetResults result = performer.perform(inputMock, outputMock);

        // Then
        assertEquals(Collections.emptyList(), result.getAddedFromChangeSet());
        verify(outputMock).finish();
        verify(outputMock).close();
    }

    @Test
    public void testPerform_AddAndDeleteDirectory() throws IOException {
        // Given
        setUpTestStreams();
        ArchiveEntry addDirectoryEntry = mockArchiveEntry("test_add_directory/");
        ArchiveEntry deleteDirectoryEntry = mockArchiveEntry("test_delete_directory/");

        // When
        whenInputReturnsEntry(inputMock, addDirectoryEntry);
        whenInputReturnsEntry(inputMock, deleteDirectoryEntry);
        ChangeSetResults result = performer.perform(inputMock, outputMock);

        // Then
        assertEquals(Collections.singletonList("test_add_directory/"), result.getAddedFromChangeSet());
        assertEquals(Collections.emptyList(), result.getDeleted());
        verify(outputMock).finish();
        verify(outputMock).close();
    }

    @Test(expected = IOException.class)
    public void testPerform_IOException() throws IOException {
        // Given
        setUpTestStreams();
        whenInputThrowsIOException(inputMock);

        // When
        performer.perform(inputMock, outputMock);

        // Then
        // Exception should be thrown
    }

    private void setUpTestStreams() {
        outputMock = new MockArchiveOutputStream(outputStream);
        performer = new ChangeSetPerformer(changeSet);
    }

    private void whenInputReturnsEntry(ArchiveInputStream inputMock, ArchiveEntry entry) {
        when(inputMock.getNextEntry()).thenReturn(entry);
    }

    private void whenInputReturnsInput(ArchiveInputStream inputMock, InputStream inputStream) {
        when(inputMock.readAllBytes()).thenReturn(inputStream.readAllBytes());
    }

    private void whenInputThrowsIOException(ArchiveInputStream inputMock) {
        when(inputMock.getNextEntry()).thenThrow(new IOException());
    }

    private ArchiveEntry mockArchiveEntry(String name) {
        ArchiveEntry mockEntry = mock(ArchiveEntry.class);
        when(mockEntry.getName()).thenReturn(name);
        return mockEntry;
    }
}

class MockArchiveOutputStream extends ArchiveOutputStream {

    private ByteArrayOutputStream outputStream;

    public MockArchiveOutputStream(ByteArrayOutputStream outputStream) {
        super(outputStream);
        this.outputStream = outputStream;
    }

    @Override
    public void finish() throws IOException {
        // No-op
    }

    @Override
    public void close() throws IOException {
        // No-op
    }

    @Override
    public void write(byte[] buffer, int offset, int length) throws IOException {
        outputStream.write(buffer, offset, length);
    }
}
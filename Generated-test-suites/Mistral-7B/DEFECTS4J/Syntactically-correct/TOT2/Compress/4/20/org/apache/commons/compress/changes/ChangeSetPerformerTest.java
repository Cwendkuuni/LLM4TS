import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Test;
import org.junit.Before;
import org.junit.Rule;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests for ChangeSetPerformer class.
 */
public class ChangeSetPerformerTest {

    private ChangeSetPerformer performer;
    private ArchiveInputStream inputStream;
    private ArchiveOutputStream outputStream;

    @Rule
    public ExternalResource cleanUp = new ExternalResource() {
        @Override
        protected void after() {
            try {
                IOUtils.closeQuietly(inputStream);
                IOUtils.closeQuietly(outputStream);
            } catch (IOException e) {
                // Ignore
            }
        }
    };

    @Before
    public void setUp() throws IOException {
        ChangeSet changeSet = mock(ChangeSet.class);
        performer = new ChangeSetPerformer(changeSet);

        inputStream = mock(ArchiveInputStream.class);
        outputStream = mock(ArchiveOutputStream.class);
    }

    @Test
    public void testPerform_EmptyChangeSet() throws IOException {
        // Given: An empty ChangeSet
        when(changeSet.getChanges()).thenReturn(new LinkedHashSet<Change>());

        // When: Perform operation on empty input and output streams
        ChangeSetResults result = performer.perform(inputStream, outputStream);

        // Then: No changes are made to the streams
        assertNotNull(result);
        assertEquals(0, result.size());
        verifyNoDataWrittenTo(outputStream);
    }

    @Test
    public void testPerform_SimpleAddition() throws IOException {
        // Given: A ChangeSet with an ADD change for an existing file
        Change change = mock(Change.class);
        when(change.type()).thenReturn(Change.TYPE_ADD);
        when(change.isReplaceMode()).thenReturn(false);
        when(change.getInput()).thenReturn(mock(InputStream.class));
        when(change.getEntry()).thenReturn(mock(ArchiveEntry.class));

        Set<Change> changes = new LinkedHashSet<Change>();
        changes.add(change);
        when(changeSet.getChanges()).thenReturn(changes);

        // When: Perform operation on input and output streams with the given change
        ChangeSetResults result = performer.perform(inputStream, outputStream);

        // Then: The change is performed on the input stream and written to the output stream
        verify(inputStream).close();
        verify(outputStream).putNextEntry(change.getEntry());
        verify(outputStream, times(change.getEntry().getSize())).write(anyByteArray());
        verify(outputStream).closeArchiveEntry();
        assertEquals(1, result.size());
        assertEquals(change.getEntry().getName(), result.getAddedFromChangeSet().get(0));
    }

    // Add test cases for other methods and edge cases here...

    // Util methods for mocking
    private void verifyNoDataWrittenTo(ArchiveOutputStream outputStream) {
        verify(outputStream, never()).write(anyByteArray());
    }
}
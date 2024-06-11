
package ghm.follow.gui;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import ghm.follow.config.FollowAppAttributes;
import ghm.follow.gui.TabbedPane;
import ghm.follow.gui.FileFollowingPane;
import java.io.File;

public class TabbedPaneTest {

    private TabbedPane tabbedPane;
    private FileFollowingPane fileFollowingPane;
    private FollowAppAttributes attributes;

    @Before
    public void setup() {
        this.attributes = new FollowAppAttributes();
        this.tabbedPane = new TabbedPane(this.attributes);
        this.fileFollowingPane = new FileFollowingPane();
    }

    @Test
    public void testFindComponentAt_ValidCoordinates_ReturnsComponent() {
        // Given
        this.tabbedPane.add(this.fileFollowingPane, "Tab 1");

        // When
        Component component = this.tabbedPane.findComponentAt(0, 0);

        // Then
        assertEquals(this.fileFollowingPane, component);
    }

    @Test
    public void testFindComponentAt_InvalidCoordinates_ReturnsNull() {
        // Given
        this.tabbedPane.add(this.fileFollowingPane, "Tab 1");

        // When
        Component component = this.tabbedPane.findComponentAt(-1, -1);

        // Then
        assertNull(component);
    }

    @Test
    public void testFindComponentAt_ComponentOutsideOfTabbedPane_ReturnsNull() {
        // Given
        this.tabbedPane.add(this.fileFollowingPane, "Tab 1");

        // When
        Component component = this.tabbedPane.findComponentAt(Integer.MAX_VALUE, Integer.MAX_VALUE);

        // Then
        assertNull(component);
    }

    @Test
    public void testSetSelectedIndex_ValidIndex_SetsSelectedIndexAndCallsHandleSelectedFile() {
        // Given
        this.tabbedPane.add(this.fileFollowingPane, "Tab 1");

        // When
        this.tabbedPane.setSelectedIndex(0);

        // Then
        assertEquals(this.fileFollowingPane, this.tabbedPane.getSelectedComponent());
        File lastDirectory = this.attributes.getLastFileChooserDirectory();
        assertNotNull(lastDirectory);
        assertEquals(lastDirectory.getAbsolutePath(), new File(".").getAbsolutePath());
    }

    @Test
    public void testSetSelectedIndex_InvalidIndex_DoesNotCallHandleSelectedFile() {
        // Given
        this.tabbedPane.add(this.fileFollowingPane, "Tab 1");

        // When
        this.tabbedPane.setSelectedIndex(-1);

        // Then
        assertNull(this.tabbedPane.getSelectedComponent());
        assertEquals(this.attributes.getLastFileChooserDirectory(), null);
    }

    @Test
    public void testSetSelectedComponent_ValidComponent_SetsSelectedComponentAndCallsHandleSelectedFile() {
        // Given
        this.tabbedPane.add(this.fileFollowingPane, "Tab 1");

        // When
        this.tabbedPane.setSelectedComponent(this.fileFollowingPane);

        // Then
        assertEquals(this.fileFollowingPane, this.tabbedPane.getSelectedComponent());
        File lastDirectory = this.attributes.getLastFileChooserDirectory();
        assertNotNull(lastDirectory);
        assertEquals(lastDirectory.getAbsolutePath(), new File(".").getAbsolutePath());
    }

    @Test
    public void testSetSelectedComponent_NullComponent_DoesNotCallHandleSelectedFile() {
        // Given
        this.tabbedPane.add(this.fileFollowingPane, "Tab 1");

        // When
        this.tabbedPane.setSelectedComponent(null);

        // Then
        assertNull(this.tabbedPane.getSelectedComponent());
        assertEquals(this.attributes.getLastFileChooserDirectory(), null);
    }

    @Test
    public void testSetSelectedFile_NullFile_DoesNotCallHandleSelectedFile() {
        // Given
        this.tabbedPane.add(this.fileFollowingPane, "Tab 1");

        // When
        this.tabbedPane.setSelectedFile(null);

        // Then
        assertNull(this.tabbedPane.getSelectedComponent());
        assertEquals(this.attributes.getLastFileChooserDirectory(), null);
    }

    @Test
    public void testSetSelectedFile_ValidFile_SetsSelectedComponentAndCallsHandleSelectedFile() {
        // Given
        File file = new File("testfile.txt");
        this.tabbedPane.add(new FileFollowingPane(file), "Tab 1");

        // When
        this.tabbedPane.setSelectedFile(file);

        // Then
        assertEquals(this.tabbedPane.getComponentAt(0), this.tabbedPane.getSelectedComponent());
        File lastDirectory = this.attributes.getLastFileChooserDirectory();
        assertNotNull(lastDirectory);
        assertEquals(lastDirectory.getAbsolutePath(), file.getParentFile().getAbsolutePath());
    }

    @Test
    public void testRemoveTabAt_ValidIndex_RemovesTabAndCallsHandleSelectedFile() {
        // Given
        this.tabbedPane.add(this.fileFollowingPane, "Tab 1");
        this.tabbedPane.add(new FileFollowingPane(new File("testfile2.txt")), "Tab 2");

        // When
        this.tabbedPane.removeTabAt(0);

        // Then
        assertEquals(this.tabbedPane.getComponentAt(0), this.tabbedPane.getSelectedComponent());
        assertEquals(this.attributes.getLastFileChooserDirectory(), new File("."));
    }

    @Test
    public void testRemoveTabAt_InvalidIndex_DoesNotCallHandleSelectedFile() {
        // Given
        this.tabbedPane.add(this.fileFollowingPane, "Tab 1");
        this.tabbedPane.add(new FileFollowingPane(new File("testfile2.txt")), "Tab 2");

        // When
        this.tabbedPane.removeTabAt(-1);

        // Then
        assertEquals(this.tabbedPane.getComponentCount(), 2);
        assertEquals(this.tabbedPane.getSelectedComponent(), this.tabbedPane.getComponentAt(0));
        assertEquals(this.attributes.getLastFileChooserDirectory(), new File("."));
    }
}

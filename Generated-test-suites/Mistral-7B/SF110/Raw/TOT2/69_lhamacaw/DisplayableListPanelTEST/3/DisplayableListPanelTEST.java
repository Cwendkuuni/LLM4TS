package macaw.util;

import org.junit.*;
import static org.junit.Assert.*;
import macaw.businessLayer.*;
import macaw.system.*;
import javax.swing.*;
import java.util.*;
import org.mockito.Mockito;

public class DisplayableListPanelTest {
    private DisplayableListPanel displayableListPanel;

    @Before
    public void setup() {
        displayableListPanel = new DisplayableListPanel(new SessionProperties(), new DisplayableListParentForm(), "Displayable List Panel", true);
    }

    // Test for getPanel()
    @Test
    public void testGetPanel() {
        JPanel panel = displayableListPanel.getPanel();
        assertNotNull(panel);
    }

    // Test for addListItem() with null listItemAdder
    @Test(expected = MacawException.class)
    public void testAddListItem_NullListItemAdder() {
        displayableListPanel.setDisplayableListItemAdder(null);
        try {
            displayableListPanel.addListItem();
        } catch (MacawException e) {
            // MacawException is expected
        }
    }

    // Test for addListItem() when commitChanges() throws an exception
    @Test
    public void testAddListItem_CommitChangesException() {
        Mockito.doThrow(new Exception("Commit Changes Exception")).when(displayableListPanel.parentForm).commitChanges();
        try {
            displayableListPanel.addListItem();
        } catch (MacawException e) {
            // MacawException is expected
        }
    }

    // Test for addListItem() with null listOwnerName
    @Test(expected = MacawException.class)
    public void testAddListItem_NullListOwnerName() {
        try {
            displayableListPanel.addListItem();
            displayableListPanel.setListOwnerName(null);
            displayableListPanel.addListItem();
        } catch (MacawException e) {
            // MacawException is expected
        }
    }

    // Test for addListItem() with empty list
    @Test
    public void testAddListItem_EmptyList() {
        displayableListPanel.displayableList.clear();
        try {
            displayableListPanel.addListItem();
        } catch (MacawException e) {
            // MacawException is expected
        }
    }

    // Test for editSelectedListItem() when no items are selected
    @Test
    public void testEditSelectedListItem_UnselectedItems() {
        try {
            displayableListPanel.editSelectedListItem();
            fail("An exception should have been thrown.");
        } catch (MacawException e) {
            // MacawException is expected
        }
    }

    // Test for viewSelectedListItem() when no items are selected
    @Test
    public void testViewSelectedListItem_UnselectedItems() {
        try {
            displayableListPanel.viewSelectedListItem();
            fail("An exception should have been thrown.");
        } catch (MacawException e) {
            // MacawException is expected
        }
    }

    // Test for deleteSelectedListItems() when no items are selected
    @Test
    public void testDeleteSelectedListItems_UnselectedItems() {
        try {
            displayableListPanel.deleteSelectedListItems();
            fail("An exception should have been thrown.");
        } catch (MacawException e) {
            // MacawException is expected
        }
    }

    @After
    public void teardown() {
        displayableListPanel = null;
    }
}
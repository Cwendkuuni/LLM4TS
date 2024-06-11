package macaw.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class DisplayableListPanelTest {
    private DisplayableListPanel displayableListPanel;

    @Test
    public void testConstructor() {
        // Testing constructor with allowWriteAccess and allowRecordEditing as true
        DisplayableListPanel displayableListPanel1 = new DisplayableListPanel(new SessionProperties(), new DisplayableListParentForm(), "Test Panel", true, true);
        assertNotNull(displayableListPanel1);

        // Testing constructor with allowWriteAccess as true and allowRecordEditing as false
        DisplayableListPanel displayableListPanel2 = new DisplayableListPanel(new SessionProperties(), new DisplayableListParentForm(), "Test Panel", true, false);
        assertNotNull(displayableListPanel2);

        // Testing constructor with allowWriteAccess and allowRecordEditing as false
        DisplayableListPanel displayableListPanel3 = new DisplayableListPanel(new SessionProperties(), new DisplayableListParentForm(), "Test Panel", false, false);
        assertNotNull(displayableListPanel3);
    }

    @Test
    public void testSetNewRecord() {
        displayableListPanel = new DisplayableListPanel(new SessionProperties(), new DisplayableListParentForm(), "Test Panel", true, true);
        displayableListPanel.setNewRecord(true);
        assertTrue(displayableListPanel.isNewRecord);
        displayableListPanel.setNewRecord(false);
        assertFalse(displayableListPanel.isNewRecord);
    }

    @Test
    public void testGetPanel() {
        displayableListPanel = new DisplayableListPanel(new SessionProperties(), new DisplayableListParentForm(), "Test Panel", true, true);
        JPanel panel = displayableListPanel.getPanel();
        assertNotNull(panel);
    }

    @Test
    public void testSetDisplayableListItemSelector() {
        displayableListPanel = new DisplayableListPanel(new SessionProperties(), new DisplayableListParentForm(), "Test Panel", true, true);
        DisplayableListItemSelector listItemSelector = new DisplayableListItemSelector();
        displayableListPanel.setDisplayableListItemSelector(listItemSelector);
        assertEquals(listItemSelector, displayableListPanel.listItemSelector);
    }

    @Test(expected = MacawException.class)
    public void testAddListItem_missingAdder() {
        displayableListPanel = new DisplayableListPanel(new SessionProperties(), new DisplayableListParentForm(), "Test Panel", true, false);
        displayableListPanel.addListItem();
    }

    @Test
    public void testEditSelectedListItem() {
        // Assuming that there is a listItemEditor and a selectedListItem
        displayableListPanel = new DisplayableListPanel(new SessionProperties(), new DisplayableListParentForm(), "Test Panel", true, true);
        Displayable selectedListItem = new Displayable();
        displayableListPanel.editSelectedListItem();
    }

    @Test(expected = MacawException.class)
    public void testEditSelectedListItem_missingEditor() {
        displayableListPanel = new DisplayableListPanel(new SessionProperties(), new DisplayableListParentForm(), "Test Panel", true, false);
        displayableListPanel.editSelectedListItem();
    }

    @Test
    public void testViewSelectedListItem() {
        // Assuming that there is a listItemViewer and a selectedListItem
        displayableListPanel = new DisplayableListPanel(new SessionProperties(), new DisplayableListParentForm(), "Test Panel", true, true);
        Displayable selectedListItem = new Displayable();
        displayableListPanel.viewSelectedListItem();
    }

    @Test(expected = MacawException.class)
    public void testViewSelectedListItem_missingViewer() {
        displayableListPanel = new DisplayableListPanel(new SessionProperties(), new DisplayableListParentForm(), "Test Panel", true, false);
        displayableListPanel.viewSelectedListItem();
    }

    @Test
    public void testDeleteSelectedListItems() {
        // Assuming that there are selected list items and a listItemDeleter
        displayableListPanel = new DisplayableListPanel(new SessionProperties(), new DisplayableListParentForm(), "Test Panel", true, true);
        displayableListPanel.deleteSelectedListItems();
    }

    @Test(expected = MacawException.class)
    public void testDeleteSelectedListItems_missingDeleter() {
        displayableListPanel = new DisplayableListPanel(new SessionProperties(), new DisplayableListParentForm(), "Test Panel", true, false);
        displayableListPanel.deleteSelectedListItems();
    }
}
package macaw.util;

import macaw.businessLayer.*;
import static org.junit.Assert.*;
import org.junit.Test;
import java.awt.*;
import java.util.*;

public class DisplayableListPanelTest {

    @Test
    public void testDisplayableListPanel() {
        DisplayableListPanel displayableListPanel = new DisplayableListPanel(new SessionProperties(), null, "Test Panel", true, true);
        assertNotNull(displayableListPanel);
    }

    @Test
    public void testInit() {
        SessionProperties sessionProperties = new SessionProperties();
        DisplayableListParentForm parentForm = new DisplayableListParentForm();
        String panelTitleText = "Test Panel";
        boolean allowWriteAccess = true;
        boolean allowRecordEditing = true;
        DisplayableListPanel displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, panelTitleText, allowWriteAccess, allowRecordEditing);

        assertEquals(sessionProperties, displayableListPanel.sessionProperties);
        assertEquals(parentForm, displayableListPanel.parentForm);
        assertEquals(panelTitleText, displayableListPanel.panelTitleText);
        assertTrue(allowWriteAccess == displayableListPanel.allowWriteAccess);
        assertTrue(allowRecordEditing == displayableListPanel.allowRecordEditing);
    }

    @Test
    public void testGetPanel() {
        SessionProperties sessionProperties = new SessionProperties();
        DisplayableListParentForm parentForm = new DisplayableListParentForm();
        String panelTitleText = "Test Panel";
        boolean allowWriteAccess = true;
        boolean allowRecordEditing = true;
        DisplayableListPanel displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, panelTitleText, allowWriteAccess, allowRecordEditing);

        JPanel panel = displayableListPanel.getPanel();
        assertNotNull(panel);
    }

    @Test
    public void testSetDisplayableListItemSelector() {
        SessionProperties sessionProperties = new SessionProperties();
        DisplayableListParentForm parentForm = new DisplayableListParentForm();
        String panelTitleText = "Test Panel";
        boolean allowWriteAccess = true;
        boolean allowRecordEditing = true;
        DisplayableListPanel displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, panelTitleText, allowWriteAccess, allowRecordEditing);

        DisplayableListItemSelector listItemSelector = new DisplayableListItemSelector();
        displayableListPanel.setDisplayableListItemSelector(listItemSelector);

        assertEquals(listItemSelector, displayableListPanel.listItemSelector);
    }

    @Test
    public void testSetDisplayableListItemAdder() {
        SessionProperties sessionProperties = new SessionProperties();
        DisplayableListParentForm parentForm = new DisplayableListParentForm();
        String panelTitleText = "Test Panel";
        boolean allowWriteAccess = true;
        boolean allowRecordEditing = true;
        DisplayableListPanel displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, panelTitleText, allowWriteAccess, allowRecordEditing);

        DisplayableListItemAdder listItemAdder = new DisplayableListItemAdder();
        displayableListPanel.setDisplayableListItemAdder(listItemAdder);

        assertEquals(listItemAdder, displayableListPanel.listItemAdder);
    }

    @Test
    public void testSetDisplayableListItemEditor() {
        SessionProperties sessionProperties = new SessionProperties();
        DisplayableListParentForm parentForm = new DisplayableListParentForm();
        String panelTitleText = "Test Panel";
        boolean allowWriteAccess = true;
        boolean allowRecordEditing = true;
        DisplayableListPanel displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, panelTitleText, allowWriteAccess, allowRecordEditing);

        DisplayableListItemEditor listItemEditor = new DisplayableListItemEditor();
        displayableListPanel.setDisplayableListItemEditor(listItemEditor);

        assertEquals(listItemEditor, displayableListPanel.listItemEditor);
    }

    @Test
    public void testSetDisplayableListItemViewer() {
        SessionProperties sessionProperties = new SessionProperties();
        DisplayableListParentForm parentForm = new DisplayableListParentForm();
        String panelTitleText = "Test Panel";
        boolean allowWriteAccess = true;
        boolean allowRecordEditing = true;
        DisplayableListPanel displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, panelTitleText, allowWriteAccess, allowRecordEditing);

        DisplayableListItemViewer listItemViewer = new DisplayableListItemViewer();
        displayableListPanel.setDisplayableListItemViewer(listItemViewer);

        assertEquals(listItemViewer, displayableListPanel.listItemViewer);
    }

    @Test
    public void testSetDisplayableListItemDeleter() {
        SessionProperties sessionProperties = new SessionProperties();
        DisplayableListParentForm parentForm = new DisplayableListParentForm();
        String panelTitleText = "Test Panel";
        boolean allowWriteAccess = true;
        boolean allowRecordEditing = true;
        DisplayableListPanel displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, panelTitleText, allowWriteAccess, allowRecordEditing);

        DisplayableListItemDeleter listItemDeleter = new DisplayableListItemDeleter();
        displayableListPanel.setDisplayableListItemDeleter(listItemDeleter);

        assertEquals(listItemDeleter, displayableListPanel.listItemDeleter);
    }

    @Test
    public void testAddListItem() {
        SessionProperties sessionProperties = new SessionProperties();
        DisplayableListParentForm parentForm = new DisplayableListParentForm();
        String panelTitleText = "Test Panel";
        boolean allowWriteAccess = true;
        boolean allowRecordEditing = true;
        DisplayableListPanel displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, panelTitleText, allowWriteAccess, allowRecordEditing);
        DisplayableListItemAdder listItemAdder = new DisplayableListItemAdder();

        displayableListPanel.setDisplayableListItemAdder(listItemAdder);

        try {
            displayableListPanel.addListItem();
        } catch (MacawException e) {
            fail("Expected no exception");
        }

        assertTrue(displayableListPanel.displayableList.size() > 0);
    }

    @Test
    public void testEditSelectedListItem() {
        SessionProperties sessionProperties = new SessionProperties();
        DisplayableListParentForm parentForm = new DisplayableListParentForm();
        String panelTitleText = "Test Panel";
        boolean allowWriteAccess = true;
        boolean allowRecordEditing = true;
        DisplayableListPanel displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, panelTitleText, allowWriteAccess, allowRecordEditing);
        DisplayableListItemEditor listItemEditor = new DisplayableListItemEditor();

        displayableListPanel.setDisplayableListItemEditor(listItemEditor);

        try {
            Displayable listItem = new Displayable();
            displayableListPanel.displayableList.add(listItem);
            displayableListPanel.editSelectedListItem();
        } catch (MacawException e) {
            fail("Expected no exception");
        }

        fail("Expected exception");
    }

    @Test
    public void testViewSelectedListItem() {
        SessionProperties sessionProperties = new SessionProperties();
        DisplayableListParentForm parentForm = new DisplayableListParentForm();
        String panelTitleText = "Test Panel";
        boolean allowWriteAccess = true;
        boolean allowRecordEditing = true;
        DisplayableListPanel displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, panelTitleText, allowWriteAccess, allowRecordEditing);
        DisplayableListItemViewer listItemViewer = new DisplayableListItemViewer();

        displayableListPanel.setDisplayableListItemViewer(listItemViewer);

        try {
            Displayable listItem = new Displayable();
            displayableListPanel.displayableList.add(listItem);
            displayableListPanel.viewSelectedListItem();
        } catch (MacawException e) {
            fail("Expected no exception");
        }

        fail("Expected exception");
    }

    @Test
    public void testDeleteSelectedListItems() {
        SessionProperties sessionProperties = new SessionProperties();
        DisplayableListParentForm parentForm = new DisplayableListParentForm();
        String panelTitleText = "Test Panel";
        boolean allowWriteAccess = true;
        boolean allowRecordEditing = true;
        DisplayableListPanel displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, panelTitleText, allowWriteAccess, allowRecordEditing);
        DisplayableListItemDeleter listItemDeleter = new DisplayableListItemDeleter();

        displayableListPanel.setDisplayableListItemDeleter(listItemDeleter);

        try {
            Displayable listItem = new Displayable();
            displayableListPanel.displayableList.add(listItem);
            displayableListPanel.deleteSelectedListItems();
        } catch (MacawException e) {
            fail("Expected no exception");
        }

        assertTrue(displayableListPanel.displayableList.size() == 0);
    }
}
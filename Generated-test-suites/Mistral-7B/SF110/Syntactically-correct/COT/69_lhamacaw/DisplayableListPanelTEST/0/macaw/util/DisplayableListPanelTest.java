package macaw.util;

import static org.junit.Assert.*;

import macaw.businessLayer.*;
import macaw.system.Log;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class DisplayableListPanelTest {

    private DisplayableListPanel displayableListPanel;
    private SessionProperties sessionProperties;
    private DisplayableListParentForm parentForm;
    private String panelTitleText;
    private boolean allowWriteAccess;
    private boolean allowRecordEditing;

    @Before
    public void setUp() {
        sessionProperties = new SessionProperties();
        parentForm = new MockDisplayableListParentForm();
        panelTitleText = "Test Panel Title";
        allowWriteAccess = true;
        allowRecordEditing = false;
        displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, panelTitleText, allowWriteAccess, allowRecordEditing);
    }

    @Test
    public void testDisplayableListPanelConstructorWithoutAllowRecordEditing() {
        assertNotNull(displayableListPanel);
        assertEquals(panelTitleText, displayableListPanel.getPanelTitleText());
        assertFalse(displayableListPanel.isAllowRecordEditing());
    }

    @Test
    public void testDisplayableListPanelConstructorWithAllowRecordEditing() {
        allowRecordEditing = true;
        displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, panelTitleText, allowWriteAccess, allowRecordEditing);
        assertTrue(displayableListPanel.isAllowRecordEditing());
    }

    @Test
    public void testInitMethod() {
        displayableListPanel = spy(new DisplayableListPanel(sessionProperties, parentForm, panelTitleText, allowWriteAccess, allowRecordEditing));

        ArgumentCaptor<String> selectTextCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<ActionListener> actionListenerCaptor = ArgumentCaptor.forClass(ActionListener.class);

        displayableListPanel.init(sessionProperties, parentForm, panelTitleText, allowWriteAccess, allowRecordEdging);

        verify(displayableListPanel, times(3)).createButton(eq(MacawMessages.getMessage("general.buttons.select")));
        verify(displayableListPanel, times(1)).createButton(eq(MacawMessages.getMessage("general.buttons.add")));
        verify(displayableListPanel, times(allowRecordEditing ? 1 : 0)).createButton(eq(MacawMessages.getMessage("general.buttons.edit")));
        verify(displayableListPanel, times(allowRecordEditing ? 1 : 0)).createButton(eq(MacawMessages.getMessage("general.buttons.view")));
        verify(displayableListPanel, times(1)).addActionListener(actionListenerCaptor.capture());
        verify(displayableListPanel, times(5)).addActionListener(displayableListPanel);

        List<ActionListener> actionListeners = actionListenerCaptor.getAllValues();
        for (ActionListener actionListener : actionListeners) {
            assertEquals(displayableListPanel, actionListener);
        }
    }

    @Test(expected = MacawException.class)
    public void testAddListItemWithoutListItemAdder() {
        displayableListPanel.setNewRecord(false);
        displayableListPanel.addListItem();
    }

    @Test(expected = MacawException.class)
    public void testEditSelectedListItemWithoutListItemEditor() {
        displayableListPanel.displayableList.add(new DisplayableMock());
        displayableListPanel.displayableList.setSelectedIndex(0);
        displayableListPanel.editSelectedListItem();
    }

    @Test(expected = MacawException.class)
    public void testViewSelectedListItemWithoutListItemViewer() {
        DisplayableMock displayable = new DisplayableMock();
        displayableListPanel.displayableList.add(displayable);
        displayableListPanel.displayableList.setSelectedIndex(0);
        displayableListPanel.viewSelectedListItem();
    }

    @Test(expected = MacawException.class)
    public void testDeleteSelectedListItemsWithoutListItemDeleter() {
        List<DisplayableMock> displayables = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            displayables.add(new DisplayableMock());
        }
        displayableListPanel.displayableList.setListData(displayables.toArray(new Displayable[0]));
        displayableListPanel.deleteSelectedListItems();
    }

    @Test
    public void testUpdateButtonStates() {
        displayableListPanel.displayableList.add(new DisplayableMock());
        displayableListPanel.isNewRecord = false;
        displayableListPanel.updateButtonStates();

        assertTrue(displayableListPanel.add.isEnabled());
        assertTrue(displayableListPanel.select.isEnabled());
        assertTrue(displayableListPanel.delete.isEnabled());
        assertFalse(displayableListPanel.edit.isEnabled());
        assertFalse(displayableListPanel.view.isEnabled());

        displayableListPanel.isNewRecord = true;
        displayableListPanel.updateButtonStates();

        assertFalse(displayableListPanel.add.isEnabled());
        assertFalse(displayableListPanel.delete.isEnabled());
        assertFalse(displayableListPanel.edit.isEnabled());
        assertFalse(displayableListPanel.view.isEnabled());
        assertFalse(displayableListPanel.select.isEnabled());
    }

    @Test
    public void testSetDisplayableItems() {
        List<DisplayableMock> displayables = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            displayables.add(new DisplayableMock());
        }
        displayableListPanel.setDisplayableItems(displayables);

        assertEquals(displayables, displayableListPanel.displayableList.getAllItems());
    }

    @Test
    public void testSave() {
        List<DisplayableMock> displayables = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            displayables.add(new DisplayableMock());
        }
        displayableListPanel.displayableList.setAllItems(displayables.toArray(new Displayable[0]));
        displayableListPanel.save();

        assertEquals(displayables, displayableListPanel.masterList);
    }

    @Test
    public void testGetAllListItems() {
        List<DisplayableMock> displayables = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            displayables.add(new DisplayableMock());
        }
        displayableListPanel.displayableList.setAllItems(displayables.toArray(new Displayable[0]));

        List<Displayable> allListItems = displayableListPanel.getAllListItems();
        assertEquals(displayables, allListItems);
    }

    @Test
    public void testSetListOwnerName() {
        String listOwnerName = "Test List Owner Name";
        displayableListPanel.setListOwnerName(listOwnerName);
        assertEquals(listOwnerName, displayableListPanel.listOwnerName);
    }
}
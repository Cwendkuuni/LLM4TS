package macaw.util;

import static org.junit.Assert.*;
import org.junit.Test;

import macaw.businessLayer.*;
import macaw.system.*;
import javax.swing.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;

public class DisplayableListPanelTest {

    @Test
    public void testInit() {
        SessionProperties sessionProperties = new SessionProperties();
        DisplayableListParentForm parentForm = mock(DisplayableListParentForm.class);
        String panelTitleText = "Test Panel Title";
        boolean allowWriteAccess = true;
        DisplayableListPanel displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, panelTitleText, allowWriteAccess);

        assertEquals(panelTitleText, displayableListPanel.getPanelTitleText());
        assertTrue(displayableListPanel.isAllowWriteAccess());
        assertNull(displayableListPanel.getListItemSelector());
        assertNull(displayableListPanel.getListItemAdder());
        assertNull(displayableListPanel.getListItemEditor());
        assertNull(displayableListPanel.getListItemViewer());
        assertNull(displayableListPanel.getListItemDeleter());
        assertNull(displayableListPanel.getPanel());
        assertNull(displayableListPanel.getDisplayableList());
        assertNull(displayableListPanel.getSelectButton());
        assertNull(displayableListPanel.getAddButton());
        assertNull(displayableListPanel.getEditButton());
        assertNull(displayableListPanel.getViewButton());
        assertNull(displayableListPanel.getDeleteButton());

        DisplayableListPanel displayableListPanel2 = new DisplayableListPanel(sessionProperties, parentForm, panelTitleText, allowWriteAccess, false);
        assertTrue(displayableListPanel2.isAllowRecordEditing());
        assertFalse(displayableListPanel2.isAllowWriteAccess());
    }

    @Test
    public void testAddListItem() {
        SessionProperties sessionProperties = new SessionProperties();
        DisplayableListParentForm parentForm = mock(DisplayableListParentForm.class);
        String panelTitleText = "Test Panel Title";
        boolean allowWriteAccess = true;
        DisplayableListPanel displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, panelTitleText, allowWriteAccess);
        DisplayableListItemAdder listItemAdder = mock(DisplayableListItemAdder.class);

        doNothing().when(listItemAdder).addItems(eq("Test List Owner"), eq(displayableListPanel.getDisplayableList()));

        displayableListPanel.setListItemAdder(listItemAdder);
        displayableListPanel.addListItem();

        verify(listItemAdder).addItems(eq("Test List Owner"), eq(displayableListPanel.getDisplayableList()));
    }

    @Test(expected = MacawException.class)
    public void testAddListItemMissingAdder() {
        SessionProperties sessionProperties = new SessionProperties();
        DisplayableListParentForm parentForm = mock(DisplayableListParentForm.class);
        String panelTitleText = "Test Panel Title";
        boolean allowWriteAccess = true;
        DisplayableListPanel displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, panelTitleText, allowWriteAccess);

        displayableListPanel.addListItem();

        sessionProperties.displayErrorDialog(any(MacawException.class));
    }

    @Test
    public void testEditSelectedListItem() {
        SessionProperties sessionProperties = new SessionProperties();
        DisplayableListParentForm parentForm = mock(DisplayableListParentForm.class);
        String panelTitleText = "Test Panel Title";
        boolean allowWriteAccess = true;
        DisplayableListPanel displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, panelTitleText, allowWriteAccess);
        DisplayableListItemEditor listItemEditor = mock(DisplayableListItemEditor.class);
        Displayable selectedListItem = mock(Displayable.class);

        doNothing().when(listItemEditor).editListItem(eq("Test List Owner"), eq(selectedListItem), eq(displayableListPanel.getDisplayableList()));

        displayableListPanel.setListItemEditor(listItemEditor);
        displayableListPanel.editSelectedListItem();

        verify(listItemEditor).editListItem(eq("Test List Owner"), eq(selectedListItem), eq(displayableListPanel.getDisplayableList()));
    }

    @Test(expected = MacawException.class)
    public void testEditSelectedListItemMissingEditor() {
        SessionProperties sessionProperties = new SessionProperties();
        DisplayableListParentForm parentForm = mock(DisplayableListParentForm.class);
        String panelTitleText = "Test Panel Title";
        boolean allowWriteAccess = true;
        DisplayableListPanel displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, panelTitleText, allowWriteAccess);

        displayableListPanel.editSelectedListItem();

        sessionProperties.displayErrorDialog(any(MacawException.class));
    }

    @Test
    public void testViewSelectedListItem() {
        SessionProperties sessionProperties = new SessionProperties();
        DisplayableListParentForm parentForm = mock(DisplayableListParentForm.class);
        String panelTitleText = "Test Panel Title";
        boolean allowWriteAccess = true;
        DisplayableListPanel displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, panelTitleText, allowWriteAccess);
        DisplayableListItemViewer listItemViewer = mock(DisplayableListItemViewer.class);
        Displayable selectedListItem = mock(Displayable.class);

        doNothing().when(listItemViewer).viewListItem(eq(selectedListItem));

        displayableListPanel.setListItemViewer(listItemViewer);
        displayableListPanel.viewSelectedListItem();

        verify(listItemViewer).viewListItem(eq(selectedListItem));
    }

    @Test(expected = MacawException.class)
    public void testViewSelectedListItemMissingViewer() {
        SessionProperties sessionProperties = new SessionProperties();
        DisplayableListParentForm parentForm = mock(DisplayableListParentForm.class);
        String panelTitleText = "Test Panel Title";
        boolean allowWriteAccess = true;
        DisplayableListPanel displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, panelTitleText, allowWriteAccess);

        displayableListPanel.viewSelectedListItem();

        sessionProperties.displayErrorDialog(any(MacawException.class));
    }

    @Test
    public void testDeleteSelectedListItems() {
        SessionProperties sessionProperties = new SessionProperties();
        DisplayableListParentForm parentForm = mock(DisplayableListParentForm.class);
        String panelTitleText = "Test Panel Title";
        boolean allowWriteAccess = true;
        DisplayableListPanel displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, panelTitleText, allowWriteAccess);
        DisplayableListItemDeleter listItemDeleter = mock(DisplayableListItemDeleter.class);

        doNothing().when(listItemDeleter).deleteListItems(eq("Test List Owner"), eq(displayableListPanel.getDisplayableList()));

        displayableListPanel.setListItemDeleter(listItemDeleter);
        displayableListPanel.deleteSelectedListItems();

        verify(listItemDeleter).deleteListItems(eq("Test List Owner"), eq(displayableListPanel.getDisplayableList()));
    }

    @Test(expected = MacawException.class)
    public void testDeleteSelectedListItemsMissingDeleter() {
        SessionProperties sessionProperties = new SessionProperties();
        DisplayableListParentForm parentForm = mock(DisplayableListParentForm.class);
        String panelTitleText = "Test Panel Title";
        boolean allowWriteAccess = true;
        DisplayableListPanel displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, panelTitleText, allowWriteAccess);

        displayableListPanel.deleteSelectedListItems();

        sessionProperties.displayErrorDialog(any(MacawException.class));
    }

    @Test
    public void testSelectListItems() {
        SessionProperties sessionProperties = new SessionProperties();
        DisplayableListParentForm parentForm = mock(DisplayableListParentForm.class);
        String panelTitleText = "Test Panel Title";
        boolean allowWriteAccess = true;
        DisplayableListPanel displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, panelTitleText, allowWriteAccess);
        DisplayableListItemSelector listItemSelector = mock(DisplayableListItemSelector.class);

        doNothing().when(listItemSelector).selectListItems(eq("Test List Owner"), eq(displayableListPanel.getDisplayableList()));

        displayableListPanel.setListItemSelector(listItemSelector);
        displayableListPanel.selectListItems();

        verify(listItemSelector).selectListItems(eq("Test List Owner"), eq(displayableListPanel.getDisplayableList()));
    }

    @Test(expected = MacawException.class)
    public void testSelectListItemsMissingSelector() {
        SessionProperties sessionProperties = new SessionProperties();
        DisplayableListParentForm parentForm = mock(DisplayableListParentForm.class);
        String panelTitleText = "Test Panel Title";
        boolean allowWriteAccess = true;
        DisplayableListPanel displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, panelTitleText, allowWriteAccess);

        displayableListPanel.selectListItems();

        sessionProperties.displayErrorDialog(any(MacawException.class));
    }
}
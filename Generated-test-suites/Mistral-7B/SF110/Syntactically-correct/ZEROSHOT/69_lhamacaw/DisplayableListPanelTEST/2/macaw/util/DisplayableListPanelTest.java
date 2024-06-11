package macaw.util;

import static org.junit.Assert.*;
import macaw.businessLayer.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class DisplayableListPanelTest {

    private DisplayableListPanel displayableListPanel;
    private SessionProperties sessionProperties;
    private DisplayableListParentForm parentForm;
    private UserInterfaceFactory userInterfaceFactory;
    private Log log;
    private JButton select;
    private JButton add;
    private JButton edit;
    private JButton view;
    private JButton delete;
    private ArrayList<Displayable> masterList;
    private DisplayableList displayableList;

    @Before
    public void setup() {
        sessionProperties = Mockito.mock(SessionProperties.class);
        parentForm = Mockito.mock(DisplayableListParentForm.class);
        userInterfaceFactory = Mockito.mock(UserInterfaceFactory.class);
        log = Mockito.mock(Log.class);
        masterList = new ArrayList<>();
        displayableList = Mockito.mock(DisplayableList.class);

        displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel Title", true);
        displayableListPanel.init(sessionProperties, parentForm, "Test Panel Title", true, false);

        Mockito.when(sessionProperties.getLog()).thenReturn(log);
        Mockito.when(sessionProperties.getProperty("currentUser")).thenReturn(new User("Test User"));
        Mockito.when(userInterfaceFactory.createButton(Mockito.anyString())).thenAnswer(invocation -> {
            ButtonType buttonType = invocation.getArgument(0).equals("add") ? ButtonType.ADD : ButtonType.EDIT;
            JButton button = Mockito.mock(JButton.class);
            Mockito.when(button.getText()).thenReturn(MacawMessages.getMessage(invocation.getArgument(0)));
            Mockito.when(button.addActionListener(Mockito.any())).thenAnswer(invocation -> {
                ActionEvent event = new ActionEvent(button, ActionEvent.ACTION_PERFORMED, "Test Button");
                invocation.getArgument(0).addActionListener(e -> displayableListPanel.actionPerformed(event));
            });
            return button;
        });
        Mockito.when(userInterfaceFactory.createLabel(Mockito.anyString())).thenReturn(Mockito.mock(JLabel.class));
        Mockito.when(userInterfaceFactory.createPanel()).thenReturn(Mockito.mock(JPanel.class));
        Mockito.when(userInterfaceFactory.createGridBagConstraints()).thenReturn(Mockito.mock(GridBagConstraints.class));

        select = Mockito.mock(JButton.class);
        add = Mockito.mock(JButton.class);
        edit = Mockito.mock(JButton.class);
        view = Mockito.mock(JButton.class);
        delete = Mockito.mock(JButton.class);

        Mockito.when(displayableList.isEmpty()).thenReturn(false);
        Mockito.when(displayableList.getSelectedItem()).thenReturn(new Displayable());
        Mockito.when(displayableList.getAllItems()).thenReturn(masterList);
        Mockito.when(displayableList.itemsAreSelected()).thenReturn(true);

        Mockito.when(displayableList.getListScrollPane()).thenReturn(Mockito.mock(JScrollPane.class));
        Mockito.when(displayableList.getDisplayItems(Mockito.anyList())).thenAnswer(invocation -> {
            List<Displayable> list = invocation.getArgument(0);
            masterList.clear();
            masterList.addAll(list);
            return masterList;
        });

        displayableListPanel.setDisplayableList(displayableList);
    }

    @Test
    public void testDisplayableListPanelConstruction() {
        assertNotNull(displayableListPanel);
    }

    @Test
    public void testSetDisplayableListItemSelector() {
        displayableListPanel.setDisplayableListItemSelector(Mockito.mock(DisplayableListItemSelector.class));
        assertNotNull(displayableListPanel.listItemSelector);
    }

    @Test(expected = MacawException.class)
    public void testAddListItemMissingAdder() {
        displayableListPanel.addListItem();
    }

    @Test
    public void testEditSelectedListItem() {
        Displayable displayable = new Displayable();
        Mockito.when(displayableList.getSelectedItem()).thenReturn(displayable);

        displayableListPanel.setDisplayableListItemEditor(Mockito.mock(DisplayableListItemEditor.class));

        displayableListPanel.editSelectedListItem();

        Mockito.verify(displayableList).commitChanges();
        Mockito.verify(displayableListItemEditor).editListItem("Test List Owner Name", displayable, displayableList);
    }

    @Test(expected = MacawException.class)
    public void testEditSelectedListItemMissingEditor() {
        displayableListPanel.editSelectedListItem();
    }

    @Test
    public void testViewSelectedListItem() {
        displayableListPanel.setDisplayableListItemViewer(Mockito.mock(DisplayableListItemViewer.class));

        displayableListPanel.viewSelectedListItem();

        Mockito.verify(displayableListItemViewer).viewListItem(new Displayable());
    }

    @Test(expected = MacawException.class)
    public void testViewSelectedListItemMissingViewer() {
        displayableListPanel.viewSelectedListItem();
    }

    @Test
    public void testDeleteSelectedListItems() {
        displayableListPanel.setDisplayableListItemDeleter(Mockito.mock(DisplayableListItemDeleter.class));

        displayableListPanel.deleteSelectedListItems();

        Mockito.verify(parentForm).commitChanges();
        Mockito.verify(displayableListItemDeleter).deleteListItems("Test List Owner Name", displayableList);
    }

    @Test
    public void testSelectListItems() {
        displayableListPanel.setDisplayableListItemSelector(Mockito.mock(DisplayableListItemSelector.class));

        displayableListPanel.selectListItems();

        Mockito.verify(displayableListItemSelector).selectListItems("Test List Owner Name", displayableList);
    }

    @Test
    public void testUpdateButtonStates() {
        displayableListPanel.setDisplayableListItemSelector(Mockito.mock(DisplayableListItemSelector.class));
        displayableListPanel.setDisplayableListItemAdder(Mockito.mock(DisplayableListItemAdder.class));
        displayableListPanel.setDisplayableListItemEditor(Mockito.mock(DisplayableListItemEditor.class));
        displayableListPanel.setDisplayableListItemViewer(Mockito.mock(DisplayableListItemViewer.class));
        displayableListPanel.setDisplayableListItemDeleter(Mockito.mock(DisplayableListItemDeleter.class));

        displayableListPanel.updateButtonStates();

        Mockito.verify(select).setEnabled(false);
        Mockito.verify(add).setEnabled(false);
        Mockito.verify(edit).setEnabled(false);
        Mockito.verify(view).setEnabled(false);
        Mockito.verify(delete).setEnabled(false);

        displayableListPanel.isNewRecord = false;
        displayableListPanel.updateButtonStates();

        Mockito.verify(select).setEnabled(true);
        Mockito.verify(add).setEnabled(true);
        Mockito.verify(edit).setEnabled(false);
        Mockito.verify(view).setEnabled(false);
        Mockito.verify(delete).setEnabled(false);

        Mockito.when(displayableList.isEmpty()).thenReturn(false);

        displayableListPanel.updateButtonStates();

        Mockito.verify(select).setEnabled(true);
        Mockito.verify(add).setEnabled(true);
        Mockito.verify(edit).setEnabled(true);
        Mockito.verify(view).setEnabled(true);
        Mockito.verify(delete).setEnabled(true);
    }

    @Test
    public void testSetDisplayableItems() {
        List<Displayable> items = Arrays.asList(new Displayable(), new Displayable(), new Displayable());
        displayableListPanel.setDisplayableItems(items);

        assertEquals(items, displayableListPanel.displayableList.getAllItems());
    }

    @Test
    public void testSave() {
        displayableListPanel.save();

        assertEquals(masterList, displayableListPanel.masterList);
    }

    @Test
    public void testGetAllListItems() {
        List<Displayable> items = Arrays.asList(new Displayable(), new Displayable(), new Displayable());
        displayableListPanel.displayableList.setDisplayItems(items);

        List<Displayable> result = displayableListPanel.getAllListItems();

        assertEquals(items, result);
    }

    @Test
    public void testSetListOwnerName() {
        displayableListPanel.setListOwnerName("Test List Owner Name");

        assertEquals("Test List Owner Name", displayableListPanel.listOwnerName);
    }

    @Test
    public void testActionPerformed() {
        displayableListPanel.select = Mockito.mock(JButton.class);
        displayableListPanel.add = Mockito.mock(JButton.class);
        displayableListPanel.edit = Mockito.mock(JButton.class);
        displayableListPanel.view = Mockito.mock(JButton.class);
        displayableListPanel.delete = Mockito.mock(JButton.class);

        ActionEvent event = Mockito.mock(ActionEvent.class);

        displayableListPanel.actionPerformed(event);

        if (displayableListPanel.select == event.getSource()) {
            Mockito.verify(displayableListPanel).selectListItems();
        }
        else if (displayableListPanel.add == event.getSource()) {
            Mockito.verify(displayableListPanel).addListItem();
        }
        else if (displayableListPanel.edit == event.getSource()) {
            Mockito.verify(displayableListPanel).editSelectedListItem();
        }
        else if (displayableListPanel.view == event.getSource()) {
            Mockito.verify(displayableListPanel).viewSelectedListItem();
        }
        else if (displayableListPanel.delete == event.getSource()) {
            Mockito.verify(displayableListPanel).deleteSelectedListItems();
        }
    }
}
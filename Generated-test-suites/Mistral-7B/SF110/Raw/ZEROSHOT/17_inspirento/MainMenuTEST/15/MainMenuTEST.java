package com.allenstudio.ir.ui;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.allenstudio.ir.core.*;
import com.allenstudio.ir.util.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.net.*;
import java.util.*;

@RunWith(MockitoJUnitRunner.class)
public class MainMenuTest {

    @Mock
    private InspirentoMediator mediator;

    @InjectMocks
    private MainMenu mainMenu;

    @Test
    public void testMainMenuConstruction() {
        mainMenu = new MainMenu(mediator);
        assertNotNull(mainMenu);
    }

    @Test
    public void testCreateMenu() {
        String menu = "File";
        JMenu expectedMenu = new JMenu(menu);
        when(mediator.getUIResources()).thenReturn(new UIResourcesMock());

        JMenu actualMenu = mainMenu.createMenu(menu);

        assertEquals(expectedMenu, actualMenu);
        verify(mediator, times(1)).getUIResources();
    }

    @Test(expected = MissingResourceException.class)
    public void testCreateMenuWithMissingLabel() {
        String menu = "MissingMenu";
        when(mediator.getUIResources()).thenReturn(new UIResourcesMock());

        mainMenu.createMenu(menu);
    }

    @Test
    public void testAddShortcutAndIcon() {
        String key = "File.Open";
        JMenuItem item = mock(JMenuItem.class);

        when(mediator.getUIResources()).thenReturn(new UIResourcesMock());
        String shortcut = "Ctrl+O";
        String iconPath = "icon.png";

        mainMenu.addShortcutAndIcon(item, key);

        assertEquals(KeyStroke.getKeyStroke(shortcut), item.getAccelerator());
        assertNull(item.getIcon());

        verify(mediator, times(1)).getUIResources();

        when(mediator.getUIResources()).thenThrow(new MissingResourceException("", "", key + "Icon"));

        mainMenu.addShortcutAndIcon(item, key);

        assertNull(item.getIcon());
    }

    @Test
    public void testCreateMenuItemWithCheckbox() {
        String key = "Preferences.ShowGrid";
        JCheckBoxMenuItem item = mock(JCheckBoxMenuItem.class);
        when(mediator.getUIResources()).thenReturn(new UIResourcesMock());
        String label = "Show Grid";

        mainMenu.addShortcutAndIcon(item, key);

        verify(item, times(1)).setText(eq(label));

        String shortcut = "Ctrl+G";
        when(item.getAccelerator()).thenReturn(KeyStroke.getKeyStroke(shortcut));

        JMenuItem expectedMenuItem = new JCheckBoxMenuItem(label);
        expectedMenuItem.setAccelerator(KeyStroke.getKeyStroke(shortcut));

        JMenuItem actualMenuItem = mainMenu.createMenuItemWithCheckbox(label, key);

        assertEquals(expectedMenuItem, actualMenuItem);
        verify(item, times(1)).setText(eq(label));
        verify(item, times(1)).setAccelerator(eq(KeyStroke.getKeyStroke(shortcut)));
        verify(mediator, times(1)).getUIResources();
    }

    @Test
    public void testCreateMenuItemWithoutShortcut() {
        String key = "Help.About";
        JMenuItem item = mock(JMenuItem.class);
        when(mediator.getUIResources()).thenReturn(new UIResourcesMock());
        String label = "About";

        mainMenu.addShortcutAndIcon(item, key);

        verify(item, times(0)).setAccelerator(any(KeyStroke.class));

        JMenuItem expectedMenuItem = new JMenuItem(label);

        JMenuItem actualMenuItem = mainMenu.createMenuItem(label, key);

        assertEquals(expectedMenuItem, actualMenuItem);
        verify(item, times(1)).setText(eq(label));
        verify(mediator, times(1)).getUIResources();
    }

    @Test
    public void testCreateMenuItemWithShortcut() {
        String key = "Edit.Undo";
        JMenuItem item = mock(JMenuItem.class);
        when(mediator.getUIResources()).thenReturn(new UIResourcesMock());
        String label = "Undo";
        String shortcut = "Ctrl+Z";

        mainMenu.addShortcutAndIcon(item, key);

        assertEquals(KeyStroke.getKeyStroke(shortcut), item.getAccelerator());

        JMenuItem expectedMenuItem = new JMenuItem(label);
        expectedMenuItem.setAccelerator(KeyStroke.getKeyStroke(shortcut));

        JMenuItem actualMenuItem = mainMenu.createMenuItem(label, key);

        assertEquals(expectedMenuItem, actualMenuItem);
        verify(item, times(1)).setText(eq(label));
        verify(item, times(1)).setAccelerator(eq(KeyStroke.getKeyStroke(shortcut)));
        verify(mediator, times(1)).getUIResources();
    }

    @Test
    public void testCreateMenuItemWithSeparator() {
        String key = "separator";

        JMenuItem expectedMenuItem = new JSeparator();

        JMenuItem actualMenuItem = mainMenu.createMenuItem(key, key);

        assertEquals(expectedMenuItem, actualMenuItem);
        verifyNoMoreInteractions(mediator);
    }
}

class UIResourcesMock extends UIResources {
    @Override
    public String getString(String key) {
        if ("menu".equals(key)) {
            return "File,Edit,Help";
        }
        if (key.startsWith("File")) {
            return "File*Open,File*Save,File.SaveAs>";
        }
        if (key.startsWith("Edit")) {
            return "Edit*Undo,Edit*Redo";
        }
        if (key.startsWith("Help")) {
            return "Help*About";
        }
        return super.getString(key);
    }

    @Override
    public String getString(String baseKey, String suffix) {
        if (baseKey.equals("File")) {
            switch (suffix) {
                case "Label":
                    return "Open";
                case "Shortcut":
                    return "Ctrl+O";
                case "Icon":
                    return "icon_open.png";
            }
        }
        if (baseKey.equals("Edit")) {
            switch (suffix) {
                case "Label":
                    return "Undo";
                case "Shortcut":
                    return "Ctrl+Z";
                case "Icon":
                    return "icon_undo.png";
            }
        }
        if (baseKey.equals("Help")) {
            switch (suffix) {
                case "Label":
                    return "About";
                case "Shortcut":
                    return "Ctrl+A";
                case "Icon":
                    return "icon_about.png";
            }
        }
        return super.getString(baseKey, suffix);
    }
}
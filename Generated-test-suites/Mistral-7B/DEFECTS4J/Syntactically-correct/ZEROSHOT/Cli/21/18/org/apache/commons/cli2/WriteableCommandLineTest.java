import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class WriteableCommandLineTest {

    @Mock
    private CommandLine commandLine;
    @Mock
    private Option option;
    @Mock
    private List defaultValues;

    private WriteableCommandLine writeableCommandLine;

    @Before
    public void setUp() {
        writeableCommandLine = Mockito.spy(new WriteableCommandLine() {
            @Override
            public void addOption(Option option) {
                Mockito.doAnswer(new Answer<Void>() {
                    @Override
                    public Void answer(InvocationOnMock invocation) throws Throwable {
                        WriteableCommandLine.this.addOption(option);
                        return null;
                    }
                }).when(commandLine).addOption(ArgumentMatchers.eq(option));
            }

            @Override
            public void addValue(Option option, Object value) {
                Mockito.doAnswer(new Answer<Void>() {
                    @Override
                    public Void answer(InvocationOnMock invocation) throws Throwable {
                        WriteableCommandLine.this.addValue(option, value);
                        return null;
                    }
                }).when(commandLine).addValue(ArgumentMatchers.eq(option), ArgumentMatchers.eq(value));
            }

            @Override
            public List getUndefaultedValues(Option option) {
                return defaultValues;
            }

            @Override
            public void setDefaultValues(Option option, List defaultValues) {
                WriteableCommandLine.this.defaultValues = defaultValues;
            }

            @Override
            public void addSwitch(Option option, boolean value) throws IllegalStateException {
                Mockito.doAnswer(new Answer<Void>() {
                    @Override
                    public Void answer(InvocationOnMock invocation) throws Throwable {
                        WriteableCommandLine.this.addSwitch(option, value);
                        return null;
                    }
                }).when(commandLine).addSwitch(ArgumentMatchers.eq(option), ArgumentMatchers.eq(value));
            }

            @Override
            public void setDefaultSwitch(Option option, Boolean defaultSwitch) {
                WriteableCommandLine.this.defaultSwitch = defaultSwitch;
            }

            @Override
            public void addProperty(Option option, String property, String value) {
                Mockito.doAnswer(new Answer<Void>() {
                    @Override
                    public Void answer(InvocationOnMock invocation) throws Throwable {
                        WriteableCommandLine.this.addProperty(option, property, value);
                        return null;
                    }
                }).when(commandLine).addProperty(ArgumentMatchers.eq(option), ArgumentMatchers.eq(property), ArgumentMatchers.eq(value));
            }

            @Override
            public void addProperty(String property, String value) {
                WriteableCommandLine.this.properties = new MutableMap<String, String>() {};
                WriteableCommandLine.this.properties.put(property, value);
            }

            @Override
            public boolean looksLikeOption(String argument) {
                return WriteableCommandLine.this.looksLikeOption(argument);
            }

            @Override
            public Option getCurrentOption() {
                return option;
            }

            @Override
            public void setCurrentOption(Option currentOption) {
                WriteableCommandLine.this.currentOption = currentOption;
            }
        });
    }

    @Test
    public void testAddOption() {
        writeableCommandLine.addOption(option);
        assertEquals(option, writeableCommandLine.getCurrentOption());
    }

    @Test(expected = IllegalStateException.class)
    public void testAddOptionTwice() {
        writeableCommandLine.addOption(option);
        writeableCommandLine.addOption(option);
    }

    @Test
    public void testAddValue() {
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "value");
        assertEquals("value", writeableCommandLine.getUndefaultedValues(option).get(0));
    }

    @Test
    public void testAddSwitch() {
        writeableCommandLine.addOption(option);
        writeableCommandLine.addSwitch(option, true);
        assertTrue(writeableCommandLine.getCurrentOption().isSet());
    }

    @Test
    public void testSetDefaultSwitch() {
        writeableCommandLine.setDefaultSwitch(option, true);
        assertTrue(writeableCommandLine.getOptionState(option).isSetByDefault());
    }

    @Test
    public void testAddProperty() {
        writeableCommandLine.addProperty(option, "property", "value");
        assertEquals("value", writeableCommandLine.getPropertyValues(option.getLongOpt()).get(0));
    }

    @Test
    public void testAddDefaultProperty() {
        writeableCommandLine.addProperty("property", "value");
        assertEquals("value", writeableCommandLine.getProperty("property"));
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(writeableCommandLine.looksLikeOption("--option"));
        assertFalse(writeableCommandLine.looksLikeOption("--option value"));
        assertFalse(writeableCommandLine.looksLikeOption("-option"));
        assertFalse(writeableCommandLine.looksLikeOption("-o"));
        assertFalse(writeableCommandLine.looksLikeOption("option"));
        assertFalse(writeableCommandLine.looksLikeOption("value"));
    }

    @Test
    public void testNoCurrentOption() {
        assertNull(writeableCommandLine.getCurrentOption());
    }

    @Test
    public void testSetCurrentOption() {
        writeableCommandLine.addOption(option);
        writeableCommandLine.setCurrentOption(option);
        assertEquals(option, writeableCommandLine.getCurrentOption());
    }
}
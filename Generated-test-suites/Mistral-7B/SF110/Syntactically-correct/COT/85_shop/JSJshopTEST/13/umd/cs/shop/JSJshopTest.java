package umd.cs.shop; // make sure to adjust the package name if needed

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.*;
import java.net.*;
import java.util.*;

@RunWith(MockitoJUnitRunner.class)
public class JSJshopTest {

    @Mock
    private JSPlanningDomain dom;

    @Mock
    private JSPlanningProblem prob;

    @Mock
    private JSListPlanningProblem probSet;

    @Mock
    private JSJshopNode tree;

    @Mock
    private JSPairPlanTSListNodes solution;

    @InjectMocks
    private JSJshop jsJshop;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testMain() {
        // Given
        String domainDefFileName = "domain.shp";
        String problemDefFileName = "problem.shp";

        // When
        jsJshop.main(new String[]{"domainDefFileName", "problemDefFileName"});

        // Then
        verify(dom, times(1)).parseFile(domainDefFileName);
        verify(prob, times(1)).parseFile(problemDefFileName);
        verifyNoMoreInteractions(dom, prob);
    }

    @Test
    public void testJSJshop() {
        // Given
        String domainDefFileName = "domain.shp";
        String problemDefFileName = "problem.shp";
        when(dom.solveAll(prob, anyBoolean())).thenReturn(Collections.singletonList(solution));

        // When
        jsJshop = new JSJshop(domainDefFileName, problemDefFileName);

        // Then
        assertEquals(solution, jsJshop.getSolution());
        verify(dom, times(1)).solveAll(prob, anyBoolean());
        verifyNoMoreInteractions(dom, prob);
    }

    @Test
    public void testJSJshopWithException() {
        // Given
        String domainDefFileName = "domain.shp";
        String problemDefFileName = "problem.shp";
        when(dom.solveAll(prob, anyBoolean()))
                .thenThrow(new NumberFormatException("Invalid parameter"));

        // When & Then
        try {
            jsJshop = new JSJshop(domainDefFileName, problemDefFileName);
            fail("Expected NumberFormatException");
        } catch (NumberFormatException e) {
            assertEquals("Invalid parameter", e.getMessage());
            verify(dom, times(1)).solveAll(prob, anyBoolean());
            verifyNoMoreInteractions(dom, prob);
        }
    }

    @Test
    public void testParser() {
        // Given
        JSUtil mockUtil = mock(JSUtil.class);
        JSJshop jsJshop = new JSJshop() {{
            setUpParseTable(mockUtil);
        }};

        // When
        jsJshop.testParser();

        // Then
        verify(mockUtil, times(1)).initParseTable(any(StreamTokenizer.class));
        verifyNoMoreInteractions(mockUtil);
    }

    @Test(expected = JSParserError.class)
    public void testParserWithError() {
        // Given
        JSUtil mockUtil = mock(JSUtil.class);
        JSJshop jsJshop = new JSJshop() {{
            setUpParseTable(mockUtil);
        }};

        // When
        jsJshop.parserFile("errorFile.shp");

        // Then (exception should be thrown)
    }
}
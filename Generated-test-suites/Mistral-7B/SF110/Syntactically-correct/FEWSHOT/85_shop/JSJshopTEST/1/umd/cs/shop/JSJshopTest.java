package umd.cs.shop;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.*;
import java.util.*;

@RunWith(MockitoJUnitRunner.class)
public class JSJshopTest {

    @Test
    public void testParserFile() {
        JSJshop jsJshop = new JSJshop();
        StreamTokenizer mockTokenizer = Mockito.mock(StreamTokenizer.class);
        JSUtil mockJsUtil = Mockito.mock(JSUtil.class);

        // Given
        Mockito.when(mockTokenizer.nextToken()).thenReturn(-1);
        JSJshopVars.flagLevel = 0;

        // When
        boolean result = jsJshop.parserFile("testFile.shp", mockJsUtil);

        // Then
        Mockito.verify(mockTokenizer, Mockito.times(1)).close();
        assertFalse(result);
    }

    @Test
    public void testSetFile() {
        JSJshop jsJshop = new JSJshop();
        String fileName = "testFile.shp";
        JSTaskAtom mockPredicate = Mockito.mock(JSTaskAtom.class);

        // Given
        JSJshopNode mockTree = Mockito.mock(JSJshopNode.class);
        JSPairPlanTSListNodes mockPairList = Mockito.mock(JSPairPlanTSListNodes.class);
        JSPlan mockPlan = Mockito.mock(JSPlan.class);
        JSPairPlanTSListNodes mockListPairs = Mockito.mock(JSListPlanningProblem.class);
        JSListLogicalAtoms mockAddList = Mockito.mock(JSListLogicalAtoms.class);
        JSListLogicalAtoms mockDeleteList = Mockito.mock(JSListLogicalAtoms.class);

        Mockito.when(mockListPairs.size()).thenReturn(1);
        Mockito.when(mockListPairs.elementAt(0)).thenReturn(mockPairList);
        Mockito.when(mockPairList.planS()).thenReturn(mockPlan);
        Mockito.when(mockPlan.isFailure()).thenReturn(false);
        Mockito.when(mockPlan.printPlan()).thenReturn("");
        Mockito.when(mockPairList.listNodes()).thenReturn(Arrays.asList(mockTree));

        // When
        JSPairPlanTSListNodes result = jsJshop.setFile(fileName, mockPredicate);

        // Then
        Mockito.verify(mockJsUtil, Mockito.times(1)).flag("File parsed");
        Mockito.verify(mockJsUtil, Mockito.times(1)).print("<- domain");
        Mockito.verify(mockJsUtil, Mockito.times(1)).print("<- problem");
        Mockito.verify(mockJsUtil, Mockito.times(1)).println("***************FINAL SOLUTION*******");
        Mockito.verify(mockJsUtil, Mockito.times(1)).println("********* list-tree FINAL SOLUTION*******");
        Mockito.verify(mockJsUtil, Mockito.times(1)).println("********* tree FINAL SOLUTION*******");
        Mockito.verify(mockJsUtil, Mockito.times(1)).print(mockTree.toString());
        assertEquals(mockPairList, result);
    }
}
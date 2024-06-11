package glengineer.agents.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import glengineer.agents.Agent;
import glengineer.agents.ComponentAgent;
import glengineer.agents.GroupAgent;
import glengineer.agents.GroupCreatorAndElementAdder;
import glengineer.agents.GapAgent;
import glengineer.agents.Settings;
import glengineer.agents.Setters.FunctionsOnGroupAndElement;
import glengineer.agents.Setters.FunctionsOnGroupAndElementImplementation;
import glengineer.agents.setters.TemporaryGapAgent;
import glengineer.agents.settings.GroupLayoutEngineer;
import glengineer.agents.settings.TemporaryGapsToGaps;

public class GroupAgentTest {

 private GroupAgent testGroupAgent;
 private ComponentAgent testComponentAgent;
 private GroupAgent testGroupAgent2;
 private GroupAgent testGroupAgent3;
 private Agent testGapAgent;
 private List<Agent> testList;
 private GroupAgent testParallelGroupAgent;
 private GroupAgent testSequentialGroupAgent;
 private ParallelGroupAgent testParallelGroupAgent2;
 private SequentialGroupAgent testSequentialGroupAgent2;
 private TemporaryGapAgent testTemporaryGapAgent;
 private TemporaryGapAgent testTemporaryGapAgent2;
 private Settings testSettings;
 private GroupCreatorAndElementAdder testCreatorAndAdder;
 private GroupLayoutEngineer testEngineer;
 private TemporaryGapsToGaps testMap;

 @Before
 public void setup() {
 testGroupAgent = new GroupAgent() {
 @Override
 protected FunctionsOnGroupAndElement getFunctionsOnGroupImplemenation() {
 return new FunctionsOnGroupAndElementImplementation(this, null);
 }
 };
 testComponentAgent = new ComponentAgent();
 testGroupAgent2 = new GroupAgent() {
 @Override
 protected FunctionsOnGroupAndElement getFunctionsOnGroupImplemenation() {
 return new FunctionsOnGroupAndElementImplementation(this, null);
 }
 };
 testGroupAgent3 = new GroupAgent() {
 @Override
 protected FunctionsOnGroupAndElement getFunctionsOnGroupImplemenation() {
 return new FunctionsOnGroupAndElementImplementation(this, null);
 }
 };
 testGapAgent = new GapAgent();
 testList = new ArrayList<Agent>();
 testParallelGroupAgent = new ParallelGroupAgent() {
 @Override
 protected FunctionsOnGroupAndElement getFunctionsOnGroupImplemenation() {
 return new FunctionsOnGroupAndElementImplementation(this, null);
 }
 };
 testSequentialGroupAgent = new SequentialGroupAgent() {
 @Override
 protected FunctionsOnGroupAndElement getFunctionsOnGroupImplemenation() {
 return new FunctionsOnGroupAndElementImplementation(this, null);
 }
 };
 testParallelGroupAgent2 = new ParallelGroupAgent() {
 @Override
 protected FunctionsOnGroupAndElement getFunctionsOnGroupImplemenation() {
 return new FunctionsOnGroupAndElementImplementation(this, null);
 }
 };
 testSequentialGroupAgent2 = new SequentialGroupAgent() {
 @Override
 protected FunctionsOnGroupAndElement getFunctionsOnGroupImplemenation() {
 return new FunctionsOnGroupAndElementImplementation(this, null);
 }
 };
 testTemporaryGapAgent = new TemporaryGapAgent();
 testTemporaryGapAgent2 = new TemporaryGapAgent();
 testSettings = new Settings();
 testCreatorAndAdder = new GroupCreatorAndElementAdder() {
 @Override
 public void addGroupToGroup(GroupLayout.Group group, Settings settings, GroupLayout.Group parentGroup) {
 // empty implementation
 }
 };
 testEngineer = new GroupLayoutEngineer();
 testMap = new TemporaryGapsToGaps();
 }

 @Test
 public void testGetFirstName() {
 assertNull(testGroupAgent.getFirstName());
 testGroupAgent.addAgent(testComponentAgent);
 assertEquals(testComponentAgent.getFirstName(), testGroupAgent.getFirstName());
 }

 @Test
 public void testGetLastName() {
 assertNull(testGroupAgent.getLastName());
 testGroupAgent.addAgent(testComponentAgent);
 assertEquals(testComponentAgent.getLastName(), testGroupAgent.getLastName());
 }

 @Test
 public void testIsComponent() {
 assertFalse(testGroupAgent.isComponent("testName"));
 testGroupAgent.addAgent(testComponentAgent);
 assertFalse(testGroupAgent.isComponent("testName"));
 testGroupAgent.addAgent(testComponentAgent);
 assertTrue(testGroupAgent.isComponent("testName"));
 }

 @Test
 public void testIsGroup() {
 assertFalse(testGroupAgent.isGroup("testFirstName", "testLastName"));
 testGroupAgent.addAgent(testComponentAgent);
 testGroupAgent.addAgent(testGroupAgent2);
 testGroupAgent.addAgent(testGroupAgent3);
 assertTrue(testGroupAgent.isGroup("testFirstName", "testLastName"));
 }

 @Test
 public void testIsGap() {
 assertFalse(testGroupAgent.isGap());
 testGroupAgent.addAgent(testGapAgent);
 assertTrue(testGroupAgent.isGap());
 }

 @Test
 public void testEquals() {
 GroupAgent groupAgent = new GroupAgent() {
 @Override
 protected FunctionsOnGroupAndElement getFunctionsOnGroupImplemenation() {
 return new FunctionsOnGroupAndElementImplementation(this, null);
 }
 };
 assertFalse(testGroupAgent.equals(testGroupAgent2));
 testGroupAgent.addAgent(testComponentAgent);
 testGroupAgent2.addAgent(testComponentAgent);
 assertTrue(testGroupAgent.equals(testGroupAgent2));
 }

 @Test
 public void testAddAgent() {
 testList.clear();
 testGroupAgent.addAgent(testComponentAgent);
 testList.add(testComponentAgent);
 assertEquals(testList, testGroupAgent.getChildren());
 }

 @Test
 public void testGetChildren() {
 assertEquals(new LinkedList<Agent>(), testGroupAgent.getChildren());
 testGroupAgent.addAgent(testComponentAgent);
 testGroupAgent.addAgent(testComponentAgent);
 assertEquals(testList, testGroupAgent.getChildren());
 }

 @Test
 public void testGetNumberOfChildren() {
 assertEquals(0, testGroupAgent.getNumberOfChildren());
 testGroupAgent.addAgent(testComponentAgent);
 testGroupAgent.addAgent(testComponentAgent);
 assertEquals(2, testGroupAgent.getNumberOfChildren());
 }

 @Test
 public void testGetComponent() {
 assertNull(testGroupAgent.getComponent("testName"));
 testGroupAgent.addAgent(testComponentAgent);
 testGroupAgent.addAgent(testComponentAgent);
 assertEquals(testComponentAgent, testGroupAgent.getComponent("testName"));
 }

 @Test
 public void testGetGroup() {
 assertNull(testGroupAgent.getGroup("testFirstName", "testLastName"));
 testGroupAgent.addAgent(testComponentAgent);
 testGroupAgent.addAgent(testGroupAgent2);
 testGroupAgent.addAgent(testGroupAgent3);
 assertNull(testGroupAgent.getGroup("testFirstName", "testLastName"));
 assertNull(testGroupAgent.getGroup("testFirstName2", "testLastName2"));
 assertTrue(testGroupAgent.getGroup("testFirstName", "testLastName").equals(testGroupAgent2));
 }

 @Test
 public void testFindDependingComponentByName() {
 assertNull(testGroupAgent.findDependingComponentByName("testName"));
 testGroupAgent.addAgent(testComponentAgent);
 testGroupAgent.addAgent(testComponentAgent);
 testGroupAgent.addAgent(testComponentAgent);
 assertEquals(testComponentAgent, testGroupAgent.findDependingComponentByName("testName"));
 }

 @Test
 public void testFindDependingGroupByNames() {
 assertNull(testGroupAgent.findDependingGroupByNames("testFirstName", "testLastName"));
 testGroupAgent.addAgent(testComponentAgent);
 testGroupAgent.addAgent(testGroupAgent2);
 testGroupAgent.addAgent(testGroupAgent3);
 assertNull(testGroupAgent.findDependingGroupByNames("testFirstName2", "testLastName2"));
 assertTrue(testGroupAgent.findDependingGroupByNames("testFirstName", "testLastName").equals(testGroupAgent2));
 }

 @Test
 public void testFindDependingParallelGroupByNames() {
 assertNull(testParallelGroupAgent.findDependingParallelGroupByNames("testFirstName", "testLastName"));
 testParallelGroupAgent.addAgent(testComponentAgent);
 testParallelGroupAgent.addAgent(testParallelGroupAgent2);
 testParallelGroupAgent.addAgent(testSequentialGroupAgent);
 testParallelGroupAgent.addAgent(testSequentialGroupAgent2);
 assertNull(testParallelGroupAgent.findDependingParallelGroupByNames("testFirstName2", "testLastName2"));
 assertTrue(testParallelGroupAgent.findDependingParallelGroupByNames("testFirstName", "testLastName").equals(testParallelGroupAgent2));
 }

 @Test
 public void testFindDependingSequentialGroupByNames() {
 assertNull(testSequentialGroupAgent.findDependingSequentialGroupByNames("testFirstName", "testLastName"));
 testSequentialGroupAgent.addAgent(testComponentAgent);
 testSequentialGroupAgent.addAgent(testParallelGroupAgent);
 testSequentialGroupAgent.addAgent(testParallelGroupAgent2);
 testSequentialGroupAgent.addAgent(testGroupAgent);
 testSequentialGroupAgent.addAgent(testGroupAgent2);
 assertNull(testSequentialGroupAgent.findDependingSequentialGroupByNames("testFirstName2", "testLastName2"));
 assertTrue(testSequentialGroupAgent.findDependingSequentialGroupByNames("testFirstName", "testLastName").equals(testGroupAgent2));
 }

 @Test
 public void testRemoveGapsFromParallelGroups() {
 testParallelGroupAgent.addAgent(testComponentAgent);
 testParallelGroupAgent.addAgent(testGapAgent);
 testParallelGroupAgent.addAgent(testGapAgent);
 testParallelGroupAgent.addAgent(testComponentAgent);
 testParallelGroupAgent.removeGapsFromParallelGroups();
 assertEquals(2, testParallelGroupAgent.getNumberOfChildren());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRemoveGapsFromParallelGroupsWithInvalidChildren() {
 testParallelGroupAgent.addAgent(testComponentAgent);
 testParallelGroupAgent.addAgent(testComponentAgent);
 testParallelGroupAgent.removeGapsFromParallelGroups();
 }

 @Test
 public void testPassTheCallToSubgroups() {
 testParallelGroupAgent.addAgent(testParallelGroupAgent2);
 testParallelGroupAgent.addAgent(testSequentialGroupAgent);
 testParallelGroupAgent.removeAll(testSequentialGroupAgent.getChildren());
 testParallelGroupAgent2.removeAll(testParallelGroupAgent2.getChildren());
 testParallelGroupAgent2.addAgent(testComponentAgent);
 testParallelGroupAgent2.addAgent(testComponentAgent);
 testParallelGroupAgent.passTheCallToSubgroups();
 assertEquals(2, testParallelGroupAgent.getNumberOfChildren());
 }

 @Test
 public void testRemoveGapsFromParallelSubgroupsWithOtherContent() {
 testParallelGroupAgent.addAgent(testParallelGroupAgent2);
 testParallelGroupAgent.addAgent(testSequentialGroupAgent);
 testParallelGroupAgent2.addAgent(testComponentAgent);
 testParallelGroupAgent2.addAgent(testComponentAgent);
 testParallelGroupAgent.removeAllGaps();
 assertEquals(2, testParallelGroupAgent.getNumberOfChildren());
 }

 @Test
 public void testReplaceParallelSubgroupsWithGapsByTheirSingleGaps() {
 testParallelGroupAgent.addAgent(testParallelGroupAgent2);
 testParallelGroupAgent.addAgent(testSequentialGroupAgent);
 testParallelGroupAgent2.addAgent(testComponentAgent);
 testParallelGroupAgent2.addAgent(testComponentAgent);
 testParallelGroupAgent.removeAll(testSequentialGroupAgent.getChildren());
 testParallelGroupAgent.replaceParallelSubgroupsWithGapsByTheirSingleGaps();
 assertEquals(3, testParallelGroupAgent.getNumberOfChildren());
 }

 @Test
 public void testReplaceContinuousSequencesOfGapsBySingleGaps() {
 testParallelGroupAgent.addAgent(testParallelGroupAgent2);
 testParallelGroupAgent.addAgent(testSequentialGroupAgent);
 testSequentialGroupAgent.addAgent(testGapAgent);
 testSequentialGroupAgent.addAgent(testGapAgent);
 testSequentialGroupAgent.addAgent(testComponentAgent);
 testParallelGroupAgent.replaceContinuousSequencesOfGapsBySingleGaps();
 assertEquals(4, testParallelGroupAgent.getNumberOfChildren());
 }

 @Test
 public void testReplaceGroupsWithSingleElementsByThatElements() {
 testParallelGroupAgent.addAgent(testParallelGroupAgent2);
 testParallelGroupAgent.addAgent(testSequentialGroupAgent);
 testSequentialGroupAgent.addAgent(testGroupAgent);
 testSequentialGroupAgent.addAgent(testGroupAgent);
 testSequentialGroupAgent.addAgent(testComponentAgent);
 testParallelGroupAgent.replaceGroupsWithSingleElementsByThatElements();
 assertEquals(3, testParallelGroupAgent.getNumberOfChildren());
 }

 @Test
 public void testReplaceTemporaryGapsByRealGaps() {
 testParallelGroupAgent.addAgent(testParallelGroupAgent2);
 testParallelGroupAgent.addAgent(testSequentialGroupAgent);
 testSequentialGroupAgent.addAgent(testTemporaryGapAgent);
 testSequentialGroupAgent.addAgent(testTemporaryGapAgent2);
 testParallelGroupAgent.replaceTemporaryGapsByRealGaps(testMap);
 assertEquals(3, testParallelGroupAgent.getNumberOfChildren());
 }

 @Test
 public void testAddPrecedingGap() {
 testGroupAgent.addGapBefore(testGapAgent, testComponentAgent);
 testList.clear();
 testGroupAgent.addAgent(testComponentAgent);
 testGroupAgent.addAgent(testGapAgent);
 assertEquals(testList, testGroupAgent.getChildren());
 }

 @Test
 public void testAddFollowingGap() {
 testGroupAgent.addGapAfter(testGapAgent, testComponentAgent);
 testList.clear();
 testGroupAgent.addAgent(testComponentAgent);
 testGroupAgent.addAgent(testComponentAgent);
 testGroupAgent.addAgent(testGapAgent);
 assertEquals(testList, testGroupAgent.getChildren());
 }

 @Test
 public void testAddPrecedingGapWithMinMax() {
 testGroupAgent.addGapBefore(new GapAgent(1, 2, 3), testComponentAgent);
 testList.clear();
 testGroupAgent.addAgent(testComponentAgent);
 testGroupAgent.addAgent(new GapAgent(1, 2, 3));
 testGroupAgent.addAgent(testComponentAgent);
 assertEquals(testList, testGroupAgent.getChildren());
 }

 @Test
 public void testAddFollowingGapWithMinMax() {
 testGroupAgent.addGapAfter(new GapAgent(1, 2, 3), testComponentAgent);
 testList.clear();
 testGroupAgent.addAgent(testComponentAgent);
 testGroupAgent.addAgent(testComponentAgent);
 testGroupAgent.addAgent(new GapAgent(1, 2, 3));
 assertEquals(testList, testGroupAgent.getChildren());
 }

 @Test
 public void testAddComponentToGroup() {
 GroupLayout.Group parentGroup = new GroupLayout.Group();
 testCreatorAndAdder.addGroupToGroup(testGroupAgent.groupContent(testCreatorAndAdder), testSettings, parentGroup);
 assertEquals(parentGroup, testGroupAgent.getParent());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddComponentToGroupWithInvalidParent() {
 GroupCreatorAndElementAdder testInvalidCreatorAndAdder = new GroupCreatorAndElementAdder() {
 @Override
 public void addGroupToGroup(GroupLayout.Group group, Settings settings, GroupLayout.Group parentGroup) {
 // empty implementation
 }
 };
 testGroupAgent.addAgent(testComponentAgent);
 testGroupAgent.addContentToGroup(null, testCreatorAndAdder);
 }
}
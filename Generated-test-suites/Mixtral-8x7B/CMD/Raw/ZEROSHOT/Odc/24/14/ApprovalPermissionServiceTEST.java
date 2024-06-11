import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.oceanbase.odc.common.lang.Pair;
import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceCandidateEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceCandidateRepository;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceRepository;
import com.oceanbase.odc.metadb.iam.RoleRepository;
import com.oceanbase.odc.metadb.iam.UserEntity;
import com.oceanbase.odc.metadb.iam.UserRepository;
import com.oceanbase.odc.metadb.iam.UserRoleEntity;
import com.oceanbase.odc.metadb.iam.UserRoleRepository;
import com.oceanbase.odc.metadb.iam.resourcerole.UserResourceRoleEntity;
import com.oceanbase.odc.metadb.iam.resourcerole.UserResourceRoleRepository;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.iam.ResourceRoleService;
import com.oceanbase.odc.service.iam.UserService;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.iam.model.Role;
import com.oceanbase.odc.service.iam.model.User;

public class ApprovalPermissionServiceTest {

    private ApprovalPermissionService approvalPermissionService;

    private UserTaskInstanceCandidateRepository userTaskCandidateRepository = mock(UserTaskInstanceCandidateRepository.class);
    private UserTaskInstanceRepository userTaskInstanceRepository = mock(UserTaskInstanceRepository.class);
    private UserRepository userRepository = mock(UserRepository.class);
    private RoleRepository roleRepository = mock(RoleRepository.class);
    private UserRoleRepository userRoleRepository = mock(UserRoleRepository.class);
    private UserResourceRoleRepository userResourceRoleRepository = mock(UserResourceRoleRepository.class);
    private UserService userService = mock(UserService.class);
    private ResourceRoleService resourceRoleService = mock(ResourceRoleService.class);
    private AuthenticationFacade authenticationFacade = mock(AuthenticationFacade.class);

    @Before
    public void setup() {
        approvalPermissionService = new ApprovalPermissionService();
        approvalPermissionService.userTaskCandidateRepository = userTaskCandidateRepository;
        approvalPermissionService.userTaskInstanceRepository = userTaskInstanceRepository;
        approvalPermissionService.userRepository = userRepository;
        approvalPermissionService.roleRepository = roleRepository;
        approvalPermissionService.userRoleRepository = userRoleRepository;
        approvalPermissionService.userResourceRoleRepository = userResourceRoleRepository;
        approvalPermissionService.userService = userService;
        approvalPermissionService.resourceRoleService = resourceRoleService;
        approvalPermissionService.authenticationFacade = authenticationFacade;
    }

    @Test
    public void testGetCandidates() {
        Long approvalInstanceId = 1L;
        Boolean enabled = true;
        when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId))
                .thenReturn(new ArrayList<UserTaskInstanceCandidateEntity>() {{
                    add(new UserTaskInstanceCandidateEntity() {{
                        setRoleId(1L);
                        setUserId(1L);
                    }});
                }});
        when(userRepository.findByRoleIdsAndEnabled(new HashSet<Long>() {{
            add(1L);
        }}, enabled)).thenReturn(new ArrayList<UserEntity>() {{
            add(new UserEntity() {{
                setId(1L);
            }});
        }});
        when(userRepository.findByUserIdsAndEnabled(new HashSet<Long>() {{
            add(1L);
        }}, enabled)).thenReturn(new ArrayList<UserEntity>() {{
            add(new UserEntity() {{
                setId(1L);
            }});
        }});
        when(userRepository.findByUserIdsOrRoleIds(new HashSet<Long>() {{
            add(1L);
        }}, new HashSet<Long>() {{
            add(1L);
        }}, enabled)).thenReturn(new ArrayList<UserEntity>() {{
            add(new UserEntity() {{
                setId(1L);
            }});
        }});
        approvalPermissionService.getCandidates(approvalInstanceId, enabled);
    }

    @Test
    public void testGetInstanceId2CandidateRoleIds() {
        Collection<Long> approvalInstanceIds = new ArrayList<Long>() {{
            add(1L);
        }};
        when(userTaskCandidateRepository.findByApprovalInstanceIds(approvalInstanceIds))
                .thenReturn(new ArrayList<UserTaskInstanceCandidateEntity>() {{
                    add(new UserTaskInstanceCandidateEntity() {{
                        setRoleId(1L);
                        setApprovalInstanceId(1L);
                    }});
                }});
        Map<Long, Set<Long>> instanceId2CandidateRoleIds = approvalPermissionService.getInstanceId2CandidateRoleIds(approvalInstanceIds);
        org.junit.Assert.assertEquals(1, instanceId2CandidateRoleIds.size());
        org.junit.Assert.assertEquals(new HashSet<Long>() {{
            add(1L);
        }}, instanceId2CandidateRoleIds.get(1L));
    }

    @Test
    public void testGetInstanceId2CandidateResourceRoleIdentifierIds() {
        Collection<Long> approvalInstanceIds = new ArrayList<Long>() {{
            add(1L);
        }};
        when(userTaskCandidateRepository.findByApprovalInstanceIds(approvalInstanceIds))
                .thenReturn(new ArrayList<UserTaskInstanceCandidateEntity>() {{
                    add(new UserTaskInstanceCandidateEntity() {{
                        setResourceRoleIdentifier("identifier1");
                        setApprovalInstanceId(1L);
                    }});
                }});
        Map<Long, Set<String>> instanceId2CandidateResourceRoleIdentifierIds = approvalPermissionService
                .getInstanceId2CandidateResourceRoleIdentifierIds(approvalInstanceIds);
        org.junit.Assert.assertEquals(1, instanceId2CandidateResourceRoleIdentifierIds.size());
        org.junit.Assert.assertEquals(new HashSet<String>() {{
            add("identifier1");
        }}, instanceId2CandidateResourceRoleIdentifierIds.get(1L));
    }

    @Test
    public void testGetCandidateUserIds() {
        Collection<Long> approvalInstanceIds = new ArrayList<Long>() {{
            add(1L);
        }};
        when(userTaskCandidateRepository.findByApprovalInstanceIds(approvalInstanceIds))
                .thenReturn(new ArrayList<UserTaskInstanceCandidateEntity>() {{
                    add(new UserTaskInstanceCandidateEntity() {{
                        setRoleId(1L);
                        setApprovalInstanceId(1L);
                    }});
                    add(new UserTaskInstanceCandidateEntity() {{
                        setResourceRoleIdentifier("identifier1");
                        setApprovalInstanceId(1L);
                    }});
                }});
        when(userRoleRepository.findByRoleIdIn(new HashSet<Long>() {{
            add(1L);
        }})).thenReturn(new ArrayList<UserRoleEntity>() {{
            add(new UserRoleEntity() {{
                setUserId(1L);
            }});
        }});
        when(userResourceRoleRepository.findByResourceIdsAndResourceRoleIdsIn(new HashSet<String>() {{
            add("identifier1");
        }}, new HashSet<Long>() {{
            add(1L);
        }})).thenReturn(new ArrayList<UserResourceRoleEntity>() {{
            add(new UserResourceRoleEntity() {{
                setUserId(1L);
            }});
        }});
        approvalPermissionService.getCandidateUserIds(approvalInstanceIds);
    }

    @Test
    public void testGetCandidateRoles() {
        Long approvalInstanceId = 1L;
        Boolean enabled = true;
        when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId))
                .thenReturn(new ArrayList<UserTaskInstanceCandidateEntity>() {{
                    add(new UserTaskInstanceCandidateEntity() {{
                        setRoleId(1L);
                        setApprovalInstanceId(1L);
                    }});
                }});
        when(roleRepository.findByRoleIdsAndEnabled(new HashSet<Long>() {{
            add(1L);
        }}, enabled)).thenReturn(new ArrayList<RoleEntity>() {{
            add(new RoleEntity() {{
                setId(1L);
            }});
        }});
        approvalPermissionService.getCandidateRoles(approvalInstanceId, enabled);
    }

    @Test
    public void testIsApprovable() {
        Long approvalInstanceId = 1L;
        when(userTaskInstanceRepository.findByApprovalInstanceId(approvalInstanceId))
                .thenReturn(new ArrayList<UserTaskInstanceEntity>() {{
                    add(new UserTaskInstanceEntity() {{
                        setId(1L);
                    }});
                }});
        approvalPermissionService.isApprovable(approvalInstanceId);
    }

    @Test
    public void testGetApprovableApprovalInstances() {
        List<UserTaskInstanceEntity> userTaskInstanceEntities = new ArrayList<UserTaskInstanceEntity>() {{
            add(new UserTaskInstanceEntity() {{
                setId(1L);
            }});
        }};
        when(userTaskInstanceRepository.findByStatus(FlowNodeStatus.EXECUTING))
                .thenReturn(userTaskInstanceEntities);
        approvalPermissionService.getApprovableApprovalInstances();
    }

    @Test
    public void testGetApprovableApprovalInstances_userId() {
        long userId = 1L;
        List<UserTaskInstanceEntity> userTaskInstanceEntities = new ArrayList<UserTaskInstanceEntity>() {{
            add(new UserTaskInstanceEntity() {{
                setId(1L);
            }});
        }};
        when(userService.getUserRoleIds(userId)).thenReturn(new HashSet<Long>() {{
            add(1L);
        }});
        when(resourceRoleService.getResourceRoleIdentifiersByUserId(userId))
                .thenReturn(new HashSet<String>() {{
                    add("identifier1");
                }});
        when(userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, new HashSet<Long>() {{
            add(1L);
        }})).thenReturn(userTaskInstanceEntities);
        when(userTaskInstanceRepository.findByCandidateUserId(userId))
                .thenReturn(new ArrayList<UserTaskInstanceEntity>());
        when(userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(
                userId, new HashSet<String>() {{
                    add("identifier1");
                }}))
                .thenReturn(new ArrayList<UserTaskInstanceEntity>());
        approvalPermissionService.getApprovableApprovalInstances(userId);
    }

    @Test
    public void testSetCandidateResourceRole() {
        Long approvalInstanceId = 1L;
        String resourceRoleIdentifier = "identifier1";
        approvalPermissionService.setCandidateResourceRole(approvalInstanceId, resourceRoleIdentifier);
    }

    @Test
    public void testListApprovableExternalInstances() {
        List<UserTaskInstanceEntity> userTaskInstanceEntities = new ArrayList<UserTaskInstanceEntity>() {{
            add(new UserTaskInstanceEntity() {{
                setId(1L);
            }});
        }};
        when(userTaskInstanceRepository.findByStatus(FlowNodeStatus.EXECUTING))
                .thenReturn(userTaskInstanceEntities);
        when(userTaskInstanceRepository.findByStatus(FlowNodeStatus.EXECUTING)
                .stream().filter(entity -> entity.getExternalApprovalId() != null
                        && entity.getExternalFlowInstanceId() != null))
                .thenReturn(userTaskInstanceEntities.stream()
                        .filter(entity -> entity.getExternalApprovalId() != null
                                && entity.getExternalFlowInstanceId() != null)
                        .collect(Collectors.toList()));
        approvalPermissionService.listApprovableExternalInstances();
    }

    @Test
    public void testGetCandidatesByFlowInstanceIds() {
        Collection<Long> flowInstanceIds = new ArrayList<Long>() {{
            add(1L);
        }};

        Map<Long, Long> approvalInstanceId2FlowInstanceId = new HashMap<Long, Long>() {{
            put(1L, 1L);
        }};

        Map<Long, Set<String>> instanceId2CandidateResourceRoleIdentifierIds = new HashMap<Long, Set<String>>() {{
            put(1L, new HashSet<String>() {{
                add("identifier1");
            }});
        }};

        Set<String> resourceRoleIdentifiers = new HashSet<String>() {{
            add("identifier1");
        }};

        when(userResourceRoleRepository.findByResourceIdsAndResourceRoleIdsIn(resourceRoleIdentifiers))
                .thenReturn(new ArrayList<UserResourceRoleEntity>() {{
                    add(new UserResourceRoleEntity() {{
                        setUserId(1L);
                    }});
                }});

        Map<String, Set<Long>> identifier2UserIds = new HashMap<String, Set<Long>>() {{
            put("identifier1:resourceRoleId", new HashSet<Long>() {{
                add(1L);
            }});
        }};

        Map<Long, Set<Long>> instanceId2UserIds = new HashMap<Long, Set<Long>>() {{
            put(1L,
                    new HashSet<Long>() {{
                        add(1L);
                    }});
        }};

        Map<Long, UserEntity> userId2User = new HashMap<Long, UserEntity>() {{
            put(1L, new UserEntity() {{
                setId(1L);
            }});
        }};

        Map<Long, Set<UserEntity>> instanceId2Candidates = new HashMap<Long, Set<UserEntity>>() {{
            put(1L, new HashSet<UserEntity>() {{
                add(new UserEntity() {{
                    setId(1L);
                }});
            }});
        }};

        when(userRepository.findByUserIds(new HashSet<Long>() {{
            add(1L);
        }})).thenReturn(new ArrayList<UserEntity>() {{
            add(new UserEntity() {{
                setId(1L);
            }});
        }});

        when(userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(
                flowInstanceIds,
                FlowNodeStatus.EXECUTING)).thenReturn(
                new ArrayList<UserTaskInstanceEntity>() {{
                    add(new UserTaskInstanceEntity() {{
                        setId(1L);
                        setFlowInstanceId(1L);
                        setStatus(FlowNodeStatus.EXECUTING);
                    }});
                }});

        when(userTaskCandidateRepository.findByApprovalInstanceIdAndResourceRoleIdentifier(1L, "identifier1"))
                .thenReturn(new UserTaskInstanceCandidateEntity() {{
                    setId(1L);
                    setApprovalInstanceId(1L);
                    setResourceRoleIdentifier("identifier1");
                }});

        approvalPermissionService.getCandidatesByFlowInstanceIds(flowInstanceIds);
    }
}
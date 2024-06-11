
package com.oceanbase.odc.metadb.flow;

import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.metadb.entity.NodeInstanceEntity;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class NodeInstanceEntityRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Test
    @DisplayName("Test findByInstanceIdAndInstanceTypeAndFlowableElementType")
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START_EVENT;
        FlowableElementType flowableElementType = FlowableElementType.USER_TASK;

        List<NodeInstanceEntity> nodeInstances = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);

        assertThat(nodeInstances.size()).isGreaterThan(0);
    }

    @Test
    @DisplayName("Test findByFlowInstanceId")
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = 1L;

        List<NodeInstanceEntity> nodeInstances = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);

        assertThat(nodeInstances.size()).isGreaterThan(0);
    }

    @Test
    @DisplayName("Test findByIds")
    public void testFindByIds() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L);

        List<NodeInstanceEntity> nodeInstances = nodeInstanceEntityRepository.findByIds(ids);

        assertThat(nodeInstances.size()).isEqualTo(ids.size());
    }
}

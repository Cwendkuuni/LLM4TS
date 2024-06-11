
import com.oceanbase.odc.metadb.iam.entity.RoleEntity;
import com.oceanbase.odc.metadb.iam.repository.RoleRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void testFindByIdIn() {
        Collection<Long> ids = new ArrayList<>();
        ids.add(1L);
        ids.add(2L);
        List<RoleEntity> roleEntities = roleRepository.findByIdIn(ids);
        assertEquals(2, roleEntities.size());
        assertEquals(1L, roleEntities.get(0).getId().longValue());
        assertEquals(2L, roleEntities.get(1).getId().longValue());
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        Optional<RoleEntity> roleEntity = roleRepository.findByNameAndOrganizationId("test", 1L);
        assertTrue(roleEntity.isPresent());
        assertEquals("test", roleEntity.get().getName());
        assertEquals(1L, roleEntity.get().getOrganizationId().longValue());
    }

    @Test
    public void testUpdate() {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setId(1L);
        roleEntity.setName("test");
        int result = roleRepository.update(roleEntity);
        assertEquals(1, result);
        RoleEntity updatedRoleEntity = roleRepository.findById(1L).get();
        assertEquals("test", updatedRoleEntity.getName());
    }
}

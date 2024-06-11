import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.session.web.http.CookieSerializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.i18n.I18nOutputSerializer;
import com.oceanbase.odc.common.json.JacksonModules;
import com.oceanbase.odc.common.json.JacksonModules.CustomOutputSerializer;
import com.oceanbase.odc.core.shared.exception.UnsupportedException;
import com.oceanbase.odc.service.connection.CloudMetadataClient;
import com.oceanbase.odc.service.connection.model.OBDatabaseUser;
import com.oceanbase.odc.service.connection.model.OBInstance;
import com.oceanbase.odc.service.connection.model.OBTenant;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ BCryptPasswordEncoder.class, ObjectMapper.class, SensitivePropertyHandler.class })
public class BeanConfigurationTest {

    @InjectMocks
    private BeanConfiguration beanConfiguration;

    private SensitivePropertyHandler sensitivePropertyHandler;

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        sensitivePropertyHandler = PowerMockito.mock(SensitivePropertyHandler.class);

        PowerMockito.mockStatic(BCryptPasswordEncoder.class);
        PowerMockito.when(BCryptPasswordEncoder.getInstance()).thenReturn(new BCryptPasswordEncoder());

        CustomOutputSerializer customOutputSerializer = new CustomOutputSerializer().addSerializer(
                Internationalizable.class, new I18nOutputSerializer());
        objectMapper = JacksonFactory.unsafeJsonMapper().registerModule(
                JacksonModules.sensitiveInputHandling(sensitivePropertyHandler::decrypt))
                        .registerModule(JacksonModules.customOutputHandling(customOutputSerializer));

        PowerMockito.mockStatic(ObjectMapper.class);
        PowerMockito.when(ObjectMapper.class, "unsafeJsonMapper").thenReturn(JacksonFactory.unsafeJsonMapper());
        PowerMockito.when(ObjectMapper.class, "customOutputHandling", CustomOutputSerializer.class)
                .thenReturn(customOutputSerializer);
        PowerMockito.when(ObjectMapper.class, "sensitiveInputHandling", sensitivePropertyHandler::decrypt)
                .thenReturn(JacksonFactory.unsafeJsonMapper());
    }

    @Test
    public void testPasswordEncoder() {
        PasswordEncoder passwordEncoder = beanConfiguration.passwordEncoder();
        assertNotNull(passwordEncoder);
        assertEquals(true, passwordEncoder instanceof BCryptPasswordEncoder);
    }

    @Test
    public void testObjectMapper() {
        ObjectMapper mapper = beanConfiguration.objectMapper(sensitivePropertyHandler);
        assertNotNull(mapper);
        assertEquals(true, mapper.equals(objectMapper));
    }

    @Test
    public void testDefaultHttpFirewall() {
        HttpFirewall defaultHttpFirewall = beanConfiguration.defaultHttpFirewall();
        assertNotNull(defaultHttpFirewall);
        assertEquals(true, defaultHttpFirewall instanceof DefaultHttpFirewall);
    }

    @Test
    public void testCloudMetadataClient() {
        CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
        assertNotNull(cloudMetadataClient);
        assertEquals(true, cloudMetadataClient instanceof NullCloudMetadataClient);
    }

    @Test
    public void testCookieSerializer() {
        CookieSerializer cookieSerializer = beanConfiguration.cookieSerializer();
        assertNotNull(cookieSerializer);
        assertEquals("JSESSIONID", cookieSerializer.getCookieName());
    }

    @Test
    public void testNullCloudMetadataClient() {
        NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
        assertNotNull(nullCloudMetadataClient);
    }

    @Test
    public void testUnsupportedException() {
        NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
        assertThrows(UnsupportedException.class, () -> nullCloudMetadataClient.listInstances());
        assertThrows(UnsupportedException.class, () -> nullCloudMetadataClient.listTenants("instanceId"));
        assertThrows(UnsupportedException.class, () -> nullCloudMetadataClient.getTenant("instanceId",
                "tenantId"));
        assertThrows(UnsupportedException.class,
                () -> nullCloudMetadataClient.listDatabaseUsers("instanceId", "tenantId"));
        assertThrows(UnsupportedException.class, () -> nullCloudMetadataClient.getTenantEndpoint("instanceId",
                "tenantId"));
        assertThrows(UnsupportedException.class, () -> nullCloudMetadataClient.getSysTenantUser("instanceId"));
    }

    @Test
    public void testObTenantEndpoint() {
        NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
        OBTenantEndpoint endpoint = nullCloudMetadataClient.getTenantEndpoint("instanceId", "tenantId");
        assertNotNull(endpoint);
        assertEquals(OceanBaseAccessMode.DIRECT, endpoint.getAccessMode());
    }

    @Test
    public void testListDatabaseUsers() {
        NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
        List<OBDatabaseUser> users = new ArrayList<>();
        PowerMockito.when(nullCloudMetadataClient, "listDatabaseUsers", "instanceId", "tenantId")
                .thenReturn(users);

        List<OBDatabaseUser> dbUsers = nullCloudMetadataClient.listDatabaseUsers("instanceId", "tenantId");
        assertNotNull(dbUsers);
        assertEquals(0, dbUsers.size());
    }

    @Test
    public void testListInstances() {
        NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
        List<OBInstance> instances = new ArrayList<>();
        PowerMockito.when(nullCloudMetadataClient, "listInstances").thenReturn(instances);

        List<OBInstance> dbInstances = nullCloudMetadataClient.listInstances();
        assertNotNull(dbInstances);
        assertEquals(0, dbInstances.size());
    }

    @Test
    public void testListTenants() {
        NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
        List<OBTenant> tenants = new ArrayList<>();
        PowerMockito.when(nullCloudMetadataClient, "listTenants", "instanceId").thenReturn(tenants);

        List<OBTenant> dbTenants = nullCloudMetadataClient.listTenants("instanceId");
        assertNotNull(dbTenants);
        assertEquals(0, dbTenants.size());
    }

}
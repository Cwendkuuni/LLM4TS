import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.session.web.http.CookieSerializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.i18n.I18nOutputSerializer;
import com.oceanbase.odc.common.json.JacksonFactory;
import com.oceanbase.odc.common.json.JacksonModules;
import com.oceanbase.odc.common.json.JacksonModules.CustomOutputSerializer;
import com.oceanbase.odc.core.shared.exception.UnsupportedException;
import com.oceanbase.odc.service.connection.CloudMetadataClient;
import com.oceanbase.odc.service.connection.model.OBDatabaseUser;
import com.oceanbase.odc.service.connection.model.OBInstance;
import com.oceanbase.odc.service.connection.model.OBTenant;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;

public class BeanConfigurationTest {

    private BeanConfiguration beanConfiguration;
    private SensitivePropertyHandler sensitivePropertyHandler;
    private ObjectMapper objectMapper;
    private CloudMetadataClient cloudMetadataClient;

    @Before
    public void setup() {
        sensitivePropertyHandler = mock(SensitivePropertyHandler.class);
        doNothing().when(sensitivePropertyHandler).decrypt(anyString());

        CustomOutputSerializer customOutputSerializer = new CustomOutputSerializer()
                .addSerializer(Internationalizable.class, new I18nOutputSerializer());
        objectMapper = JacksonFactory.unsafeJsonMapper()
                .registerModule(JacksonModules.sensitiveInputHandling(sensitivePropertyHandler::decrypt))
                .registerModule(JacksonModules.customOutputHandling(customOutputSerializer));

        cloudMetadataClient = mock(CloudMetadataClient.class);

        beanConfiguration = new BeanConfiguration() {
            @Override
            public ObjectMapper objectMapper(SensitivePropertyHandler handler) {
                return BeanConfigurationTest.this.objectMapper;
            }

            @Override
            public CloudMetadataClient cloudMetadataClient() {
                return BeanConfigurationTest.this.cloudMetadataClient;
            }
        };
    }

    @Test
    public void testPasswordEncoder() {
        PasswordEncoder passwordEncoder = beanConfiguration.passwordEncoder();
        String encodedPassword = passwordEncoder.encode("test");
        assertTrue(passwordEncoder.matches("test", encodedPassword));
        assertFalse(passwordEncoder.matches("anotherTest", encodedPassword));
    }

    @Test
    public void testObjectMapper() {
        ObjectMapper mapper = beanConfiguration.objectMapper(sensitivePropertyHandler);
        assertNotNull(mapper);
    }

    @Test
    public void testDefaultHttpFirewall() {
        HttpFirewall firewall = beanConfiguration.defaultHttpFirewall();
        assertNotNull(firewall);
        assertFalse(firewall.getAllowedHttpMethods().isEmpty());
    }

    @Test
    public void testCloudMetadataClient() {
        when(cloudMetadataClient.supportsCloudMetadata()).thenReturn(false);
        when(cloudMetadataClient.supportsTenantInstance()).thenReturn(false);
        when(cloudMetadataClient.needsOBTenantName()).thenReturn(false);
        when(cloudMetadataClient.needsSysTenantUser()).thenReturn(false);
        when(cloudMetadataClient.includeClusterNameForJdbcUsername())
                .thenReturn(true);
        when(cloudMetadataClient.oceanbaseAccessMode())
                .thenReturn(OceanBaseAccessMode.DIRECT);
        when(cloudMetadataClient.listInstances())
                .thenAnswer(new Answer<List<OBInstance>>() {
                    @Override
                    public List<OBInstance> answer(InvocationOnMock invocation) throws Throwable {
                        return new ArrayList<>();
                    }
                });

        when(cloudMetadataClient.listTenants(anyString()))
                .thenAnswer(new Answer<List<OBTenant>>() {
                    @Override
                    public List<OBTenant> answer(InvocationOnMock invocation) throws Throwable {
                        return new ArrayList<>();
                    }
                });

        when(cloudMetadataClient.getTenant(anyString(), anyString()))
                .thenAnswer(new Answer<OBTenant>() {
                    @Override
                    public OBTenant answer(InvocationOnMock invocation) throws Throwable {
                        return new OBTenant();
                    }
                });

        when(cloudMetadataClient.listDatabaseUsers(anyString(), anyString()))
                .thenAnswer(new Answer<List<OBDatabaseUser>>() {
                    @Override
                    public List<OBDatabaseUser> answer(InvocationOnMock invocation) throws Throwable {
                        return new ArrayList<>();
                    }
                });

        when(cloudMetadataClient.getTenantEndpoint(anyString(), anyString()))
                .thenReturn(new OBTenantEndpoint());

        when(cloudMetadataClient.getSysTenantUser(anyString()))
                .thenThrow(new UnsupportedException("CloudMetadata not supported"));

        beanConfiguration.cloudMetadataClient();
    }

    @Test
    public void testCookieSerializer() {
        CookieSerializer serializer = beanConfiguration.cookieSerializer();
        assertNotNull(serializer);
        assertEquals("JSESSIONID", serializer.getCookieName());
    }

    @Test
    public void testNullCloudMetadataClient() {
        BeanConfiguration beanConfigurationWithNullClient = new BeanConfiguration() {
            @Override
            public CloudMetadataClient cloudMetadataClient() {
                return new NullCloudMetadataClient();
            }
        };

        BCryptPasswordEncoder passwordEncoder = beanConfigurationWithNullClient.passwordEncoder();
        assertNotNull(passwordEncoder);

        ObjectMapper mapper = beanConfigurationWithNullClient.objectMapper(sensitivePropertyHandler);
        assertNotNull(mapper);

        HttpFirewall firewall = beanConfigurationWithNullClient.defaultHttpFirewall();
        assertNotNull(firewall);

        CookieSerializer serializer = beanConfigurationWithNullClient.cookieSerializer();
        assertNotNull(serializer);
    }

    @Test
    public void testNullCloudMetadataClientMethods() {
        BeanConfiguration beanConfigurationWithNullClient = new BeanConfiguration() {
            @Override
            public CloudMetadataClient cloudMetadataClient() {
                return new NullCloudMetadataClient();
            }
        };

        NullCloudMetadataClient nullClient = beanConfigurationWithNullClient.cloudMetadataClient();
        assertFalse(nullClient.supportsCloudMetadata());
        assertFalse(nullClient.supportsTenantInstance());
        assertFalse(nullClient.needsOBTenantName());
        assertFalse(nullClient.needsSysTenantUser());
        assertTrue(nullClient.includeClusterNameForJdbcUsername());
        assertEquals(OceanBaseAccessMode.DIRECT, nullClient.oceanbaseAccessMode());

        assertThrows(UnsupportedException.class, () -> nullClient.listInstances());
        assertThrows(UnsupportedException.class, () -> nullClient.listTenants("instanceId"));
        assertThrows(UnsupportedException.class, () -> nullClient.getTenant("instanceId", "tenantId"));
        assertThrows(UnsupportedException.class, () -> nullClient.listDatabaseUsers("instanceId", "tenantId"));
        assertNull(nullClient.getTenantEndpoint("instanceId", "tenantId"));
        assertThrows(UnsupportedException.class, () -> nullClient.getSysTenantUser("instanceId"));
    }
}
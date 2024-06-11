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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

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
import com.oceanbase.odc.service.connection.model.OceanBaseAccessMode;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;

@RunWith(SpringRunner.class)
public class BeanConfigurationTest {

    @TestConfiguration
    static class ContextConfiguration {

        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }

        @Bean
        public ObjectMapper objectMapper(SensitivePropertyHandler sensitivePropertyHandler) {
            CustomOutputSerializer customOutputSerializer = new CustomOutputSerializer()
                    .addSerializer(Internationalizable.class, new I18nOutputSerializer());
            return JacksonFactory.unsafeJsonMapper()
                    .registerModule(JacksonModules.sensitiveInputHandling(sensitivePropertyHandler::decrypt))
                    .registerModule(JacksonModules.customOutputHandling(customOutputSerializer));
        }

        @Bean
        public HttpFirewall defaultHttpFirewall() {
            DefaultHttpFirewall defaultHttpFirewall = new DefaultHttpFirewall();
            defaultHttpFirewall.setAllowUrlEncodedSlash(true);
            return defaultHttpFirewall;
        }

        @Bean
        @ConditionalOnMissingBean
        public CloudMetadataClient cloudMetadataClient() {
            return new NullCloudMetadataClient();
        }

        @Bean
        @Primary
        public CookieSerializer cookieSerializer() {
            DefaultCookieSerializer serializer = new DefaultCookieSerializer();
            serializer.setCookieName("JSESSIONID");
            return serializer;
        }

        @Bean
        public MethodValidationPostProcessor methodValidationPostProcessor() {
            return new MethodValidationPostProcessor();
        }

    }

    @InjectMocks
    private BeanConfiguration beanConfiguration;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private HttpFirewall httpFirewall;

    @Autowired
    private CloudMetadataClient cloudMetadataClient;

    @Autowired
    private CookieSerializer cookieSerializer;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testPasswordEncoder() {
        String password = "test";
        String encodedPassword = passwordEncoder.encode(password);
        assertNotNull(encodedPassword);
        boolean isPasswordMatch = passwordEncoder.matches(password, encodedPassword);
        assertEquals(true, isPasswordMatch);
    }

    @Test
    public void testObjectMapper() {
        String inputJson = "{\"key\":\"value\"}";
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Object obj = objectMapper.readValue(inputJson, Object.class);
            assertNotNull(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDefaultHttpFirewall() {
        String url = "/test/%25";
        httpFirewall.getAllowedUrlEncodedSlashes().add("%");
        boolean allowed = httpFirewall.checkUrlEncodedSlash(url);
        assertEquals(true, allowed);
    }

    @Test
    public void testCloudMetadataClient() {
        assertThrows(UnsupportedException.class, () -> {
            cloudMetadataClient.listInstances();
        });
    }

    @Test
    public void testCookieSerializer() {
        String serializedCookie = cookieSerializer.serialize("sessionId");
        assertNotNull(serializedCookie);
    }

}
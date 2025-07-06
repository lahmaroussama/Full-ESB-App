package tn.besoftilys.messages.config;


import org.apache.camel.CamelContext;
import org.apache.camel.component.micrometer.messagehistory.MicrometerMessageHistoryFactory;
import org.apache.camel.component.micrometer.routepolicy.MicrometerRoutePolicyFactory;
import org.apache.camel.spring.boot.CamelContextConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class CamelConfiguration {
// routage des messages entre les applications et la liste d'attentes kafka
    @Bean
    public CamelContextConfiguration camelContextConfiguration() {

        return new CamelContextConfiguration() {
            @Override
            public void beforeApplicationStart(CamelContext camelContext) {
                camelContext.addRoutePolicyFactory(new MicrometerRoutePolicyFactory());
                camelContext.setMessageHistoryFactory(new MicrometerMessageHistoryFactory());

            }

            @Override
            public void afterApplicationStart(CamelContext camelContext) {

            }
        };
    }

}

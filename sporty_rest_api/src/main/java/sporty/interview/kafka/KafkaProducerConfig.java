package sporty.interview.kafka;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.LongSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import sporty.interview.model.EventOutcome;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {

	
	@Value("${spring.kafka.bootstrap-servers}")
	private String bootstrapServerConfig;
	
	@Value("${topic.name}")
	private String topicName;
	
	@Bean
	public ProducerFactory<Long, EventOutcome> producerFactory() {
		Map<String, Object> configProps = new HashMap<>();
		configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServerConfig);
		    configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class);
		    configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
		    return new DefaultKafkaProducerFactory<>(configProps);
	}
	
    @Bean
    public KafkaTemplate<Long, EventOutcome> kafkaTemplate() {
        return new KafkaTemplate<Long, EventOutcome>(producerFactory());
    }
    
    @Bean 
    public KafkaAdmin admin()
  {
      Map<String, Object> configs = new HashMap<>();
      configs.put(
          AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG,
          bootstrapServerConfig);
      return new KafkaAdmin(configs);
  }

  @Bean 
    public NewTopic topic1()
  {
      return TopicBuilder.name(topicName)
          .partitions(1)
          .replicas(1)
          .build();
  }

}

package sporty.interview.kafka;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.LongSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import sporty.interview.model.EventOutcome;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfig {

	
	@Value("${spring.kafka.bootstrap-servers}")
	private String bootstrapServerConfig;
	
	@Value("${topic.name}")
	private String topicName;
	
	@Value("${spring.kafka.consumer.group-id}")
	private String groupId;
    
	
	@Bean
    public ConsumerFactory<Long, EventOutcome> consumerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServerConfig);
        configProps.put(ConsumerConfig.GROUP_ID_CONFIG, "group-events-outcome");
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,LongDeserializer.class);
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        configProps.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        return new DefaultKafkaConsumerFactory<>(configProps);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<Long, EventOutcome> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<Long, EventOutcome> factory = new ConcurrentKafkaListenerContainerFactory<Long, EventOutcome>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
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

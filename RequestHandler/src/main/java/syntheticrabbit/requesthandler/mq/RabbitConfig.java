package syntheticrabbit.requesthandler.mq;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class RabbitConfig {

    @Value("${syntheticrabbit.url}")
    private String url;

    @Value("${syntheticrabbit.queue.getuser}")
    private String getUserQueue;

    @Value("${syntheticrabbit.queue.createuser}")
    private String createUserQueue;

    @Bean
    public ConnectionFactory connectionFactory() {
        return new CachingConnectionFactory(url);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpAdmin amqpAdmin() {
        return new RabbitAdmin(connectionFactory());
    }

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange("syntheticExchange");
    }

    @Bean
    public Queue idQueue() {
        return new Queue(getUserQueue);
    }

    @Bean
    public Queue userQueue() {
        return new Queue(createUserQueue);
    }

    @Bean
    public Binding idBinding() {
        return BindingBuilder.bind(idQueue()).to(directExchange()).with("id");
    }

    @Bean
    public Binding userBinding() {
        return BindingBuilder.bind(userQueue()).to(directExchange()).with("user");
    }

    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory());
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }

}

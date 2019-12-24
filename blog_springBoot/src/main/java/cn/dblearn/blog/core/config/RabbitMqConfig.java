package cn.dblearn.blog.core.config;

import cn.dblearn.blog.core.common.constants.RabbitMqConstants;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * ActiveMqConfig
 *
 * @author bobbi
 * @date 2019/03/16 21:59
 * @email 571002217@qq.com
 * @description
 */
@Configuration
public class RabbitMqConfig {

    @Bean
    public Queue queue() {
        return new Queue( RabbitMqConstants.REFRESH_ES_INDEX_QUEUE);
    }
}

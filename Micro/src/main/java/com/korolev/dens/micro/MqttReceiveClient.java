package com.korolev.dens.micro;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

/**
 *〈MQTT receive message processing〉
 * **/
@Slf4j
@Configuration
@IntegrationComponentScan
public class MqttReceiveClient {

    @Autowired
    private MqttReceiveHandle mqttReceiveHandle;

    @Value("${spring.mqtt.username}")
    private String username;

    @Value("${spring.mqtt.password}")
    private String password;

    @Value("${spring.mqtt.url}")
    private String hostUrl;

    @Value("${spring.mqtt.client.id}")
    private String clientId;

    //Subscribe topic (#Subscribe topic, separate multiple with ‘,’, such as topic,YIHON_WQ_M)
    @Value("${spring.mqtt.default.topic}")
    private String defaultTopic;

    @Value("${spring.mqtt.completionTimeout}")
    private int completionTimeout; //Connection timeout


    @Bean(value = "getMqttConnectOptions")
    public MqttConnectOptions getMqttConnectOptions() {
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setUserName(username);
        mqttConnectOptions.setPassword(password.toCharArray());
        mqttConnectOptions.setServerURIs(new String[]{hostUrl});
        //cleanSession is set to true; when the client is offline; the server will clear the client session;
        // after reconnecting, the client will have a new session, cleanSession
        // Set to false, the server will not clear the session after the client is disconnected.
        // After reconnecting, it can receive messages from the previous subscription topic.
        // When the client goes online, it will receive messages from the time it was offline
        mqttConnectOptions.setCleanSession(false);
        // Set the session heartbeat time in seconds
        mqttConnectOptions.setKeepAliveInterval(2);
        return mqttConnectOptions;
    }

    @Bean
    public MqttPahoClientFactory mqttClientFactory() {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        factory.setConnectionOptions(getMqttConnectOptions());
        return factory;
    }

    //Receive channel
    @Bean
    public MessageChannel mqttInputChannel() {
        return new DirectChannel();
    }

    //Configure client, listening topic
    @Bean
    public MessageProducer inbound() {
        MqttPahoMessageDrivenChannelAdapter adapter =
                new MqttPahoMessageDrivenChannelAdapter(clientId + "_inbound2", mqttClientFactory(), defaultTopic.trim().split(","));
        adapter.setCompletionTimeout(completionTimeout);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(2);
        adapter.setOutputChannel(mqttInputChannel());
        return adapter;
    }

    //Get data through the channel
    @Bean
    @ServiceActivator(inputChannel = "mqttInputChannel")
    public MessageHandler handler() {
        return new MessageHandler() {
            @Override
            public void handleMessage(Message<?> message) throws MessagingException {
                //Processing the message
                mqttReceiveHandle.handle(message);
            }
        };
    }
}

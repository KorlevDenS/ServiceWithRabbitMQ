package com.korolev.dens.servicewithrabbitmq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SendController {

    @Autowired
    MqttConfig.MqttDataSenderGateway mqttDataSenderGateway;

    @PostMapping("/send")
    public String sendMessage(@RequestBody String message) {
        mqttDataSenderGateway.sendToMqtt(message, "bindingKey");
        return "success";
    }

}

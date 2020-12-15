package GAD_TempGenerator;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.Timer;
import java.util.TimerTask;


class GAD_TempGenerator {

    GAD_TempGenerator() {
        String topic = "sensor/KYH/GAD1";
        String content = (int) (Math.random() * 10 +15 ) + "°C" ;
        int qos = 2;
        String broker = "tcp://broker.hivemq.com:1883";
        String clientId = "JavaSample";
        MemoryPersistence persistence = new MemoryPersistence();

        try {
            MqttClient sampleClient = new MqttClient(broker, clientId, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            System.out.println("Connecting to broker: " + broker);
            sampleClient.connect(connOpts);
            System.out.println("Connected");
            System.out.println("Publishing message: " + content);
            MqttMessage message = new MqttMessage(content.getBytes());
            message.setQos(qos);
            sampleClient.publish(topic, message);
            System.out.println("Message published");

        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    Timer timer;

    GAD_TempGenerator(int seconds) {
        timer = new Timer();
        timer.schedule(new RemindTask(), seconds*1000);
    }

    class RemindTask extends TimerTask {
        public void run() {
            timer.cancel();
            new GAD_TempGenerator();
            new GAD_TempGenerator(60);
        }
    }

    public static void main(String[] args) {
        new GAD_TempGenerator(60);
        new GAD_TempGenerator();
    }
}

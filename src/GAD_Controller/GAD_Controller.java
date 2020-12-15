package GAD_Controller;


import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class GAD_Controller {
    String topic1 = "sensor/KYH/GAD1";
    String topic2 = "sensor/KYH/GAD2";
    String broker = "tcp://broker.hivemq.com:1883";
    String clientId = "GAD_Controller";
    MqttClient sampleClient;

    GAD_Controller() {
        MemoryPersistence persistence = new MemoryPersistence();
        try {
            sampleClient = new MqttClient(broker, clientId, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            System.out.println("Connecting to broker: " + broker);
            sampleClient.connect(connOpts);
            System.out.println("Connected");
            sampleClient.subscribe(topic1, new MqttPostPropertyMessageListener());
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    class MqttPostPropertyMessageListener implements IMqttMessageListener {
        @Override
        public void messageArrived(String var1, MqttMessage var2) throws Exception {
            String incomingData = var2.toString();
            System.out.println(incomingData);
            scanIncomingData(incomingData);
            System.out.println("---------");
        }
    }

    public void scanIncomingData(String incomingData) {
        try {
            int x = Integer.parseInt(incomingData.substring(0, 2));

            if (x >= 22) {
                String ctrl = "Ctrl -";
                MqttMessage message = new MqttMessage(ctrl.getBytes());
                sampleClient.publish(topic2, message);
                System.out.println(ctrl);
            } else {
                String ctrl = "Ctrl +";
                MqttMessage message = new MqttMessage(ctrl.getBytes());
                sampleClient.publish(topic2, message);
                System.out.println(ctrl);
            }
        } catch (Exception e) {
            System.err.println("Wrong format!");
        }
    }

    public static void main(String[] args) {
        new GAD_Controller();
    }
}
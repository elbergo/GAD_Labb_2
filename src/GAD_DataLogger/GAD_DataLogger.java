package GAD_DataLogger;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.Scanner;


public class GAD_DataLogger {
    String topic1 = "sensor/KYH/GAD1";
    String topic2 = "sensor/KYH/GAD2";
    String broker = "tcp://broker.hivemq.com:1883";
    String clientId = "GAD_DataLog";

    GAD_DataLogger() {
        MemoryPersistence persistence = new MemoryPersistence();
        try {
            MqttClient sampleClient = new MqttClient(broker, clientId, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            System.out.println("Connecting to broker: " + broker);
            sampleClient.connect(connOpts);
            System.out.println("Connected");
            Scanner sc = new Scanner(System.in);
            sampleClient.subscribe(topic1, new MqttPostPropertyMessageListener());
            sampleClient.subscribe(topic2, new MqttPostPropertyMessageListener());

        } catch (MqttException me) {
            System.out.println("reason " + me.getReasonCode());
            System.out.println("msg " + me.getMessage());
            System.out.println("loc " + me.getLocalizedMessage());
            System.out.println("cause " + me.getCause());
            System.out.println("excep " + me);
            me.printStackTrace();
        }
    }

    class MqttPostPropertyMessageListener implements IMqttMessageListener {
        @Override
        public void messageArrived(String var1, MqttMessage var2) throws Exception {
            Date currentDate = new Date();
            String incomingData = var2.toString();
            System.out.println("-------------------------------------");
            System.out.println(currentDate + ": " + incomingData);
            Logger dl = new Logger();
            dl.Log(incomingData);

        }
    }

    class Logger {
        public void Log(String incomingData) throws IOException {
            Date currentDate = new Date();
            File DataLog = new File("GAD_Labb_2_Git\\src\\DataLogFiles\\TempDataLog.txt");
            FileWriter fw = new FileWriter(DataLog, true);
            if (incomingData.startsWith("C")){
                fw.write(currentDate + ": " + incomingData + "\n" + "-------------------------------------" + "\n");
                fw.close();

            }else {
                fw.write(currentDate + ": " + incomingData + "\n");
                fw.close();
            }

        }
    }

    public static void main(String[] args) {
        new GAD_DataLogger();
    }
}

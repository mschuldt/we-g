import org.wiigee.device.Device;
import org.wiigee.event.ButtonPressedEvent;
import java.util.Random;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class test_raw{

    static Device device;

    static int trainButton;
    static int closeGestureButton;
    static int recognitionButton;

    public static void train_gesture(int gesture_id, String dir, int n_gestures)
        throws Exception{
        String filename;
        String line;
        String[] data;
        BufferedReader br;
        double[] event;
        for (int i = 0; i < n_gestures; i++){
            filename = dir + i + ".csv";
            br = new BufferedReader(new FileReader(filename));
            line = br.readLine(); //discard

            device.fireButtonPressedEvent(trainButton);//start gesture

            while ((line = br.readLine()) != null){
                data = line.split(",");
                event = new double[3];
                for (int j = 0; j < 3 ; j++){
                    event[j] = Double.parseDouble(data[j+1]);
                }
                device.fireAccelerationEvent(event);
            }
            device.fireButtonReleasedEvent(trainButton);//stop gesture
        }

        //signal end of gesture training
        device.fireButtonPressedEvent(closeGestureButton);
        device.fireButtonReleasedEvent(closeGestureButton);
    }


    public static void recognize_gesture(String filename)
        throws Exception{
        String line;
        String[] data;
        BufferedReader br;
        double[] event;

        br = new BufferedReader(new FileReader(filename));
        line = br.readLine(); //discard

        device.fireButtonPressedEvent(recognitionButton);//start gesture

        while ((line = br.readLine()) != null){
            data = line.split(",");
            event = new double[3];
            for (int j = 0; j < 3 ; j++){
                event[j] = Double.parseDouble(data[j+1]);
            }
            device.fireAccelerationEvent(event);
        }
        device.fireButtonReleasedEvent(recognitionButton);//stop gesture
    }

    public static void main(String[] args) throws Exception{
        //WiimoteWiigee wiigee = new WiimoteWiigee();

        device = new Device(true); //autofiltering=true

        trainButton = ButtonPressedEvent.BUTTON_A;
        closeGestureButton = ButtonPressedEvent.BUTTON_HOME;
        recognitionButton = ButtonPressedEvent.BUTTON_B;

        device.setTrainButton(trainButton);
        device.setCloseGestureButton(closeGestureButton);
        device.setRecognitionButton(recognitionButton);

        //train
        train_gesture(0, "raw_gesture_recordings_converted/circle/", 19);
        //train_gesture(1, "raw_gesture_recordings_converted/square/", 19);
        train_gesture(2, "raw_gesture_recordings_converted/up-down/", 13);
        train_gesture(2, "raw_gesture_recordings_converted/flip-roll/", 19);

        //save
        device.saveGesture(0, "raw_gesture_recordings_converted/circle/model");
        //device.saveGesture(1, "raw_gesture_recordings_converted/square/model");
        device.saveGesture(2, "raw_gesture_recordings_converted/up-down/model");
        device.saveGesture(2, "raw_gesture_recordings_converted/flip-roll/model");

        //test
        recognize_gesture("raw_gesture_recordings_converted/circle/19.csv");

        //recognize_gesture("raw_gesture_recordings_converted/square/19.csv");
        recognize_gesture("raw_gesture_recordings_converted/up-down/19.csv");
        recognize_gesture("raw_gesture_recordings_converted/flip-roll/19.csv");

        device.write_c_file("models.c");
        device.write_cll_file("models.cll");
    }
}

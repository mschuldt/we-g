import org.wiigee.device.Device;
import org.wiigee.event.ButtonPressedEvent;
import java.util.Random;

public class test{

    static Device device;
        
    static int trainButton;
    static int closeGestureButton;
    static int recognitionButton;


    //base gestures
    static double[][] g1;
    static double[][] g2;
    static double[][] g3;
    static double[][] g4;

    static Random random;
    
    public static double[][] makeGesture(){
        double [][] g = new double[10][];    
        for (int i = 0; i < 10; i++){
            g[i] = new double[3];
            g[i][0] = 2*random.nextDouble();
            g[i][1] = 2*random.nextDouble();
            g[i][2] = 2*random.nextDouble();
        }
        return g;
    }

    public static double[][] makeVariation(double[][] g){
        double[][] new_g = new double[10][];
        for (int i = 0; i < 10; i++){
            new_g[i] = new double[3];
            new_g[i][0] = g[i][0] + g[i][0] * random.nextDouble();
            new_g[i][1] = g[i][1] + g[i][1] * random.nextDouble();
            new_g[i][2] = g[i][2] + g[i][2] * random.nextDouble();
        }
        return new_g;
    }

    public static void train_g(double [][] g){
        double[][] variation;
        for (int j=0; j<15; j++){
            variation = makeVariation(g);
            device.fireButtonPressedEvent(trainButton);
            for (int i = 0; i <10; i++){
                device.fireAccelerationEvent(variation[i]);
            }
            device.fireButtonReleasedEvent(trainButton);
        }

        device.fireButtonPressedEvent(closeGestureButton);
        device.fireButtonReleasedEvent(closeGestureButton);
    }

    public static void train(){
        train_g(g1);
        train_g(g2);
        train_g(g3);
    }

    public static void recognize_g(double[][] g){
        device.fireButtonPressedEvent(recognitionButton);
        double[][]variation = makeVariation(g);
        for (int i = 0; i< 10;i++){
            device.fireAccelerationEvent(variation[i]);
        }
        device.fireButtonReleasedEvent(recognitionButton);
    }
    public static void recognize(){
        recognize_g(g1);
        recognize_g(g2);
        recognize_g(g3);
        recognize_g(makeGesture());
        recognize_g(makeGesture());
        recognize_g(makeGesture());
        recognize_g(makeGesture());
    }

    public static void main(String[] args){
        //WiimoteWiigee wiigee = new WiimoteWiigee();

        device = new Device(true); //autofiltering=true

        trainButton = ButtonPressedEvent.BUTTON_A;
        closeGestureButton = ButtonPressedEvent.BUTTON_HOME;
        recognitionButton = ButtonPressedEvent.BUTTON_B;

        device.setTrainButton(trainButton);
        device.setCloseGestureButton(closeGestureButton);
        device.setRecognitionButton(recognitionButton);        

        random = new Random();
        random.setSeed(1);
            
        g1 = makeGesture();
        g2 = makeGesture();
        g3 = makeGesture();
        g4 = makeGesture();        

        train();

        recognize();

        System.out.println("ok.\n");
    }
}

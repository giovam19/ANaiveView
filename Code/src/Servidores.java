import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Servidores extends Thread{
    private final int PUERTO = 1234;
    private final String HOST = "localhost";
    private Socket socket;
    private DataInputStream input;
    private DataOutputStream out;
    private String message;
    private int type;
    private int value;

    public Servidores(int type) {
        try {
            this.type = type;
            socket = new Socket(HOST, PUERTO);
            message = "null";
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void run() {
        try {
            input = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            // Dependiendo del tipo, hacemos lectura o cambiamos el valor
            if (type == 1) {
                for (int i=0; i<10; i++) {
                    value = getCurrentValue();
                    updateValue(value+1);
                    Thread.sleep(1000);
                }
            } else {
                for (int i=0; i<10; i++) {
                    value = getCurrentValue();
                    Thread.sleep(1000);
                }
            }

            System.out.println("Fin de la conexión");
            socket.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private int getCurrentValue() throws IOException {
        out.writeUTF("R-0");
        return Integer.parseInt(input.readUTF());
    }

    private void updateValue(int value) throws IOException {
        out.writeUTF("U-" + value);
    }
}

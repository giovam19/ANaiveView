import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Servidores {
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
            socket = new Socket(HOST, PUERTO);
            message = "null";
            this.type = type;
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void startServer() {
        try {
            while (!socket.isConnected()){}

            input = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            System.out.println("inicia thread.");
            // Dependiendo del tipo, hacemos lectura o cambiamos el valor
            if (type == 1) {
                System.out.println("es update.");
                for (int i=0; i<10; i++) {
                    value = getCurrentValue();
                    updateValue(value+1);
                    System.out.println("Value updated to " + value + ".");
                    Thread.sleep(1000);
                }
            } else {
                System.out.println("es read.");
                for (int i=0; i<10; i++) {
                    value = getCurrentValue();
                    System.out.println("Read value = " + value + ".");
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
        out.flush();
        message = input.readUTF();
        return Integer.parseInt(message);
    }

    private void updateValue(int value) throws IOException {
        message = "U-"+value;
        out.writeUTF(message);
        out.flush();
    }
}

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Servidores {
    private final int PUERTO = 1234;
    private final String HOST = "localhost";
    private Socket socket;
    private BufferedReader input;
    private PrintWriter out;
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

            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
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
        out.print("R-0");
        message = input.readLine();
        return Integer.parseInt(message);
    }

    private void updateValue(int value) {
        message = "U-"+value;
        out.print(message);
    }
}

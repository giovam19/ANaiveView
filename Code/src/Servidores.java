import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Servidores {
    private final int PUERTO = 1234;
    private final String HOST = "localhost";
    private Socket socket;
    private DataInputStream input;
    private DataOutputStream out;
    private String message;

    public Servidores() {
        try {
            socket = new Socket(HOST, PUERTO);
            message = "null";
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void startServer() {
        try {
            Scanner scanner = new Scanner(System.in);
            input = new DataInputStream(System.in);
            out = new DataOutputStream(socket.getOutputStream());

            while (!message.equals("fin")) {
                System.out.print("Mensaje: ");
                message = scanner.nextLine();
                out.writeUTF(message);
            }

            System.out.println("Fin de la conexión");
            socket.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}

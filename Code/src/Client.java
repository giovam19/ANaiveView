import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class Client extends Thread{
    private SocketChannel client;
    private int type;
    private int id;
    private String message;
    private final int READ = 0;
    private final int UPDATE = 1;

    public Client(int id, int type) {
        this.id = id;
        this.type = type;
    }

    @Override
    public void run() {
        try {
            int value;

            client = SocketChannel.open(new InetSocketAddress("localhost", 8089));
            System.out.println("Client " + id + " connected");

            if (type == UPDATE) {
                for (int i = 0; i < 10; i++) {
                    requestPriority();
                    value = getCurrentValue();
                    updateCurrentValue(value + 1);
                    freePriority();
                    System.out.println("Client " + id + " Value updated to " + (value+1) + ".\n");
                    Thread.sleep(1000);
                }
            } else if (type == READ) {
                for (int i = 0; i < 10; i++) {
                    requestPriority();
                    value = getCurrentValue();
                    System.out.println("Client " + id + " Value read: " + value + ".\n");
                    freePriority();
                    Thread.sleep(1000);
                }
            }

            client.close();
            System.out.println("Fin de la conexión Client " + id);
        } catch (Exception e) {
            System.out.println("Exception client"+id+": " + e.getCause() + " " + e.getMessage());
        }
    }

    private void writeToServer(String message)   {
        try {
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            buffer.put(message.getBytes());
            buffer.flip();
            client.write(buffer);
            Thread.sleep(0, 15);
        } catch (Exception e) {
            System.out.println("write server - " + e.getMessage());
        }
    }

    private String readFromServer()   {
        try {
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            client.read(buffer);
            return new String(buffer.array()).trim();
        } catch (Exception e) {
            System.out.println("read server - " + e.getMessage());
            return null;
        }
    }

    private int getCurrentValue()   {
        try {
            String t = "R-0";
            writeToServer(t);

            String data = readFromServer();

            return Integer.parseInt(data);
        } catch (Exception e) {
            System.out.println("get value - " + e.getMessage());
            return 0;
        }
    }

    private void updateCurrentValue(int value)   {
        try {
            String t = "U-" + value;
            writeToServer(t);
        } catch (Exception e) {
            System.out.println("update value - " + e.getMessage());
        }
    }

    private void requestPriority()   {
        try {
            String res = "KO";
            while (res.equals("KO")) {
                String t = "P-" + id;
                writeToServer(t);
                res = readFromServer();
            }
        } catch (Exception e) {
            System.out.println("priority - " + e.getMessage());
        }
    }

    private void freePriority()   {
        try {
            String t = "F-" + id;
            writeToServer(t);
        } catch (Exception e) {
            System.out.println("free - " + e.getMessage());
        }
    }
}

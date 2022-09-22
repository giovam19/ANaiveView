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
                    value = getCurrentValue();
                    updateCurrentValue(value + 1);
                    System.out.println("Client " + id + " Value updated to " + (value+1) + ".\n");
                    Thread.sleep(1000);
                }
            } else if (type == READ) {
                for (int i = 0; i < 10; i++) {
                    value = getCurrentValue();
                    System.out.println("Client " + id + " Value read: " + value + ".\n");
                    Thread.sleep(1000);
                }
            }

            client.close();
            System.out.println("Fin de la conexión Client " + id);
        } catch (Exception e) {
            System.out.println("Exception cl: " + e.getMessage());
        }
    }

    private int getCurrentValue() throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        message = "R-0";
        buffer.put(message.getBytes());
        buffer.flip();
        client.write(buffer);

        buffer = ByteBuffer.allocate(1024);
        client.read(buffer);
        String data = new String(buffer.array()).trim();

        return Integer.parseInt(data);
    }

    private void updateCurrentValue(int value) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        message = "U-"+value;
        buffer.put(message.getBytes());
        buffer.flip();
        client.write(buffer);
    }
}

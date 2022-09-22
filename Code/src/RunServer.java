import java.util.ArrayList;

public class RunServer {
    public static void main(String[] args) throws InterruptedException {
        Thread.sleep(1000);

        Client client1 = new Client(1, 0);
        Client client2 = new Client(2, 1);
        Client client3 = new Client(3, 0);
        Client client4 = new Client(4, 0);
        Client client5 = new Client(5, 1);

        client1.start();
        client2.start();
        client3.start();
        client4.start();
        client5.start();

        /*Servidores server = new Servidores(1);
        server.startServer();

        ArrayList<Servidores> clients = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            clients.add(new Servidores(i % 2));
        }

        for (Servidores s : clients) {
            s.start();
        }*/
    }
}

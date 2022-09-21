import java.util.ArrayList;

public class RunServer {
    public static void main(String[] args) {
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        ArrayList<Servidores> clients = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            clients.add(new Servidores(i % 2));
        }

        for (Servidores s : clients) {
            s.start();
        }
    }
}

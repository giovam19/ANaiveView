public class RunCentral {
    public static void main(String[] args) {
        ServidorCentral central = new ServidorCentral();
        System.out.println("Iniciando central\n");
        central.start();
        central.startCentralServer();
    }
}

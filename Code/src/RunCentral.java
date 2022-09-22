public class RunCentral {
    public static void main(String[] args) {
        System.out.println("Iniciando central\n");
        ServidorCentral central = new ServidorCentral();
        central.start();
        central.listenRequest();
    }
}

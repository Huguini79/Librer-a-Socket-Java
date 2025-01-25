// Ejemplo de cómo usar la librería

import libreriasocket.HuSocket;

public class Ejemplo {

    public static boolean iniciar = false;
    public static boolean multiple = false;

    public static void main(String[] args) {
        HuSocket husocket = new HuSocket();
        if(iniciar == false) {
            // Iniciar servidor
            if(multiple == true) {
                husocket.iniciar_servidor(8888, multiple);

            } else {
                husocket.iniciar_servidor(8888, multiple);
            }

        } else {
            // Conectar a un host, ejemplo: google.com, 443
            husocket.conectar_a_un_host("google.com", 443);
        }
    }
}
// Librería de Socket para crear servidores y hacer conexiones hacia servidores

package libreriasocket;

import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class HuSocket {

    public static ExecutorService poolHilos = Executors.newFixedThreadPool(10);  // Máximo 10 clientes simultáneos
    public static ServerSocket servidor_multiple;

    public void iniciar_multiple(int puerto) {
        try (ServerSocket servidor = new ServerSocket(puerto)) {
            System.out.println("Servidor múltiple encendido en el puerto "+ puerto);
            while(true) {
                try {
                Socket cliente = servidor.accept();
                System.out.println("Cliente conectado.");
                InetAddress ip = cliente.getInetAddress();
                System.out.println("La dirección ip del cliente es: "+ ip.getHostAddress()+ " y el nombre del host: "+ ip.getHostName());
                poolHilos.execute(new ManejadorCliente(cliente));

                } catch(IOException exx) {
                    System.err.println("Hubo un error al aceptar al cliente.");
                }
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public void iniciar_servidor(int puerto, boolean multiple) {
        try {

            if(multiple == false) {
                @SuppressWarnings("resource")
                ServerSocket servidor = new ServerSocket(puerto);
                System.out.println("Servidor inicializado exitosamente.");
                while(true) {
                    try {

                        Socket cliente = servidor.accept();
                        InetAddress ip = cliente.getInetAddress();
                        System.out.println("Nuevo cliente conectado.\n\n");
                        System.out.println("La dirección IP del cliente es: "+ ip.getHostAddress()+ ", nombre del host: "+ ip.getHostName());
                        BufferedReader entrada = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
                        while(true) {
                            String mensaje = entrada.readLine();
                            System.out.println("Mensaje de la IP: "+ ip.getHostAddress()+ " nombre del host: "+ ip.getHostName()+ " Mensaje: "+ mensaje);
                        }
                        
                    } catch(IOException ex) {
                        System.err.println("Hubo un error al aceptar al cliente: "+ ex.getMessage());
                    }
                }

            } else {
                iniciar_multiple(puerto);
            }
        } catch(IOException e) {
            System.err.println("Hubo un error al iniciar el servidor: "+ e.getMessage());
        }
    }

    public void conectar_a_un_host(String ip, int puerto) {
        try {

            Socket conexion = new Socket(ip, puerto);
            System.out.println("Conexión TCP exitosa al host.");
            conexion.close();

        } catch(IOException exx) {
            System.err.println("Hubo un error al conectarse al host especificado: "+ exx.getMessage());
        }
    }

}

class ManejadorCliente implements Runnable {
    private Socket cliente;
    
    public ManejadorCliente(Socket socket) {
        this.cliente = socket;
    }


    @Override

    public void run() {
        try(BufferedReader entrada = new BufferedReader(new InputStreamReader(cliente.getInputStream()))) {

            String mensaje;
        
            InetAddress ip = cliente.getInetAddress();

            while((mensaje = entrada.readLine()) != null) {
                System.out.println("Mensaje de un cliente con la IP: "+ ip.getHostAddress()+ " nombre del host: "+ ip.getHostName()+ "Mensaje: "+ mensaje);

                if(mensaje.equalsIgnoreCase("salir")) {
                    System.out.println("Cliente desconectado");
                    break;
                }
            }

        } catch(IOException e) {
            System.err.println("Hubo un error: "+ e.getMessage());

        } finally {
            try {
                cliente.close();

            } catch(IOException e) {
                System.err.println("hubo un error: "+ e.getMessage());
            }
        }
    }
}
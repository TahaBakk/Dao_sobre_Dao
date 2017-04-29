import java.io.IOException;
import java.util.Scanner;
import org.basex.api.client.ClientSession;

/**
 * Created by taha on 29/04/2017.
 */
public class DaodelDao {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("=== ServerCommands ===");

        System.out.println("\n* Create a client session.");

        try(ClientSession session = new ClientSession("localhost", 1984, "admin", "admin")) {
            do {
                System.out.println("Menu");
                System.out.println("-------------------------------");
                System.out.println("1. Crear BBDD");
                System.out.println("2. hacer consulta");
                System.out.println("3. Añadir un recurso a la coleccion");
                int constante = sc.nextInt();

                switch (constante) {
                    case 1:  crearBBDD(session);break;
                    case 2:  doConsulta(session);break;
                    case 3:  afegirRecursosColeccions();break;
                }
            }while (true);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        // Stop the server
        System.out.println("\n* Stop the server.");
        server.stop();
    }

    private static void doConsulta(ClientSession session) throws IOException {
        conexion.pedirConsulta();
        conexion.doConsulta(session);
    }

    private static void crearBBDD(ClientSession session) throws IOException {
        conexion.pedirBBDD();
        conexion.pedirRutaXML();
        conexion.crearBBDD(session);
    }

    private static void afegirRecursosColeccions() throws ClassNotFoundException, IllegalAccessException, InstantiationException{
        conexion.pedirNombreCollection();
        conexion.pedirNombreRescurso();
        conexion.afegirRecursosColeccions();
    }




}

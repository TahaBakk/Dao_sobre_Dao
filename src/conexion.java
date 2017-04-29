import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Database;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.CollectionManagementService;
import org.basex.api.client.ClientQuery;
import org.basex.api.client.ClientSession;
import org.basex.core.cmd.CreateDB;


import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 * Created by 45858000w on 03/04/17.
 */

/*
Para empezar debes habrir en el terminal :/home/user/Baixades/basex/bin == > basexhttp

En el explorer = > http://localhost:8984/dba/databases

Usuario : admin  Pasword : admin

//Para crear una Base de datos nueva : => mira AddExample2.java

Si peta Importar Librerias : => xmldb y Base X 86

*/



public class conexion {

    public static Scanner sc = new Scanner(System.in);

    public static String nameRecurso;
    public static String nameCollection;
    public static String rutaxml ;//"/media/45858000w/PenCristianJ/M06/ExamenUf3/UF3-ExamenF-Plantes.xml";
    public static String URI = "xmldb:exist://localhost:8080/exist/xmlrpc";
    public static String driver = "org.exist.xmldb.DatabaseImpl";
    public static String consulta;//"let $max := max(/CATALOG/PLANT/AVAILABILITY),      $nombre := /CATALOG/PLANT[AVAILABILITY=$max]/COMMON/text() return <resultado>  <nombre>{$nombre}</nombre> <max>{$max}</max>  </resultado>"
    public static String nameBBDD;//Plantas


    /**
     * Crear Conexion a la BBDD y hacer una consulta, añadir recursos y collecion
     * @throws IOException
     * @throws XMLDBException
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public static void conexion() throws IOException, XMLDBException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        System.out.println("=== ServerCommands ===");

        // Start server on default port 1984
        //BaseXServer server = new BaseXServer();

        // Create a client session with host name, port, user name and password
        System.out.println("\n* Create a client session.");

        try(ClientSession session = new ClientSession("localhost", 1984, "admin", "admin")) {

            crearBBDD(session);
            doConsulta(session);
        }
        // Stop the server
        System.out.println("\n* Stop the server.");

        //server.stop();

        afegirRecursosColeccions();
    }


    /**
     * Hacer la consulta
     * @param session Default -> ClientSession session = new ClientSession("localhost", 1984, "admin", "admin")
     * @throws IOException
     */
    static void doConsulta(ClientSession session) throws IOException {
        //Aqui se cambia la query
        System.out.println("--------------------------");
        System.out.println("Consulta.");
        System.out.println("--------------------------");
        try(ClientQuery query = session.query(consulta)) {
            System.out.println(query.execute());
        }
    }

    /**
     * Crear la BBDD
     * @param session Default -> ClientSession session = new ClientSession("localhost", 1984, "admin", "admin")
     * @throws IOException
     */
    static void crearBBDD(ClientSession session) throws IOException {

        // Create a database
        System.out.println("\n* Create a database.");
        //Aqui se cambia segun la base de datos
        session.execute(new CreateDB(nameBBDD, rutaxml));
    }

    /**
     * Añadir Recursos i collections y un archivo xml como datos
     * @throws XMLDBException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    static void afegirRecursosColeccions() throws XMLDBException, ClassNotFoundException, IllegalAccessException, InstantiationException{

        File f = new File(rutaxml);

        // initialize database driver
        Class cl = Class.forName(driver);
        Database database = (Database) cl.newInstance();
        database.setProperty("create-database", "true");

        // crear el manegador
        DatabaseManager.registerDatabase(database);

        // adquirir la col·lecció que volem tractar
        Collection col = DatabaseManager.getCollection(URI+"/db","admin","admin");
        System.out.println(col.getName());

        //Creem la col·lecció on guardarem el recurs
        CollectionManagementService colmgt = (CollectionManagementService) col.getService("CollectionManagementService", "1.0");
        //l'hi donem un nom a la nova col·lecció
        col = colmgt.createCollection(nameCollection);

        //afegir el recurs que farem servir
        Resource res = col.createResource(nameRecurso,"XMLResource");
        res.setContent(f);
        col.storeResource(res);



        //Creamos un nuevo recurso vacio dentro del programa y le damos el recurso mondial2.xml que esta dentro de la col·lección
        Resource res2 = col.getResource(nameRecurso);

        //Mostramos el contenido de un recurso por pantalla
        System.out.println( res2.getContent() );


    }

    /**
     * Pedir el nombre de la collection a agregar
     */
    static void pedirNombreCollection() {
        System.out.println("Dime el nombre de la collection a añadir");
        nameCollection= sc.nextLine();
    }

    /**
     * Pedir el nombre del Recurso a añadir
     */
    static void pedirNombreRescurso() {
        System.out.println("Dime el nombre del recurso a añadir");
        nameRecurso= sc.nextLine();
    }

    /**
     * Pedir el nombre de la BBDD que se quiere tener
     */
    static void pedirBBDD() {
        System.out.println("Dime el nombre de la BBDD");
        nameBBDD= sc.nextLine();
    }

    /**
     * Pedir cual es la consulta que se quiere hacer
     */
    static void pedirConsulta() {
        System.out.println("Dime la consulta a realizar con XQuery ( FLOWR )");
        consulta= sc.nextLine();
    }

    /**
     * Pedir la ruta del archivo xml donde contien los datos
     */
    static void pedirRutaXML() {
        System.out.println("Dime la ruta completa de donde esta el archivo xml");
        rutaxml =  sc.nextLine();
    }


}

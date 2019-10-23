import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Cliente {
    public static void main(String[]args){
        final String Host="127.0.0.1";
        final int puerto=5000;
        DataInputStream in;
        DataOutputStream out;
        try {

            Socket sc=new Socket(Host,puerto);
            System.out.println("servidor iniciado");
            in=new DataInputStream(sc.getInputStream());
            out=new DataOutputStream(sc.getOutputStream());
            out.writeUTF("holaa desde el cliente");
            String msj=in.readUTF();
            System.out.println(msj);
            sc.close();


        } catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE,null,ex);

        }
    }
}

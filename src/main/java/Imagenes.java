import java.awt.*;

public class Imagenes {

    public Image cargarTablero(){
        return Toolkit.getDefaultToolkit().createImage("/home/miguel/asd/src/main/java/Tablero.jpg");
          //resource s de ahi ver mañana
    }
    public Image cargarPortada(){
        return Toolkit.getDefaultToolkit().createImage("/home/miguel/asd/src/main/java/Portada.jpg");
    }
    public Image cargarTableroDuo(){
        return Toolkit.getDefaultToolkit().createImage("/home/miguel/asd/src/main/java/TableroDual.jpg");
    }
    public Image cargarPortadaDuo(){
        return Toolkit.getDefaultToolkit().createImage("/home/miguel/asd/src/main/java/PortadaDual.jpg");
    }
}
import java.awt.*;

public class Imagenes {
    public Image cargar(String sRuta){
        return Toolkit.getDefaultToolkit().createImage((getClass().getResource(sRuta)));
    }
}
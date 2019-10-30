import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

public class ReproduceAudio {

    public static void main(String[] args) {
        try {

            // Se obtiene un Clip de sonido
            Clip sonido = AudioSystem.getClip();

            // Se carga con un fichero wav
            sonido.open(AudioSystem.getAudioInputStream(new File("/home/miguel/asd/src/main/java/SoundTrack.wav")));

            // Comienza la reproducción
            sonido.start();

            // Espera mientras se esté reproduciendo.
            while (sonido.isRunning())
                Thread.sleep(1000);

            // Se cierra el clip.
            sonido.close();
        } catch (Exception e) {
            System.out.println("" + e);
        }
    }

}

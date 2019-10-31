import sun.audio.AudioPlayer;

import javax.swing.*;
import java.applet.AudioClip;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

public class JNavalDuo extends javax.swing.JFrame {

    Image portada;
    Image tablero;
/*
    Audio audio=new Audio();
*/
    int nEstado=0;

    int tableroMio[][]=new int[8][8];
    boolean bTableroMio[][]=new boolean[8][8];
    int tableroSuyo[][]=new int[8][8];
    boolean bTableroSuyo[][]=new boolean[8][8];
    int tableroMio2[][]=new int[8][8];
    boolean bTableroMio2[][]=new boolean[8][8];
    int tableroSuyo2[][]=new int[8][8];
    boolean bTableroSuyo2[][]=new boolean[8][8];

    int pFila=0;
    int pCol=0;
    int pTam=5;
    int pHor=0;


    public boolean celdaEstaEnTablero(int f, int c){
        if (f<0) return false;
        if (c<0) return false;
        if (f>=8) return false;
        if (c>=8) return false;
        return true;
    }

    public boolean puedePonerBarco(int tableroMio[][],int tableroSuyo2[][], int tam, int f, int c, int hor){
        int df=0,dc=0;
        if (hor==1) df=1;
        else dc=1;
        for (int c2=c;c2<=c+tam*dc;c2++){
            for (int f2=f;f2<=f+tam*df;f2++){
                if (!celdaEstaEnTablero(f2, c2)){
                    return false;
                }
            }
        }
        for (int f2=f-1;f2<=f+1+df*tam;f2++){
            for (int c2=c-1;c2<=c+1+dc*tam;c2++){
                if (celdaEstaEnTablero(f2,c2)){
                    if (tableroMio[f2][c2]!=0){
                        return false;
                    }
                    if (tableroSuyo2[f2][c2]!=0){
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public void ponerBarco(int tab[][], int tam){

        int f,c,hor;
        do{
            f=(int)(Math.random()*8);
            c=(int)(Math.random()*8);
            hor=(int)(Math.random()*2);
        }while(!puedePonerBarco(tableroMio, tam, f, c, hor));
        int df=0,dc=0;
        if (hor==1) df=1;
        else dc=1;
        for (int f2=f;f2<=f+(tam-1)*df;f2++){
            for (int c2=c;c2<=c+(tam-1)*dc;c2++){
                tableroMio[f2][c2]=tam;
                tableroSuyo2[f2][c2]=tam;

            }
        }
    }

    public void iniciarPartida(){
        for (int n=0;n<8;n++){
            for (int m=0;m<8;m++){
                tableroMio[n][m]=0;
                tableroSuyo[n][m]=0;
                bTableroMio[n][m]=false;
                bTableroSuyo[n][m]=false;
                tableroMio2[n][m]=0;
                tableroSuyo2[n][m]=0;
                bTableroMio2[n][m]=false;
                bTableroSuyo2[n][m]=false;
            }
        }
        for (int tam=5;tam>=1;tam--){
            ponerBarco(tableroSuyo, tam);
            ponerBarco(tableroMio2, tam);
        }
        pTam=5;
    }


    public void rectificarBarcoPoner(){
        int pDF=0;
        int pDC=0;
        if (pHor==1){
            pDF=1;
        }else{
            pDC=1;
        }
        if (pFila+pTam*pDF>=8){
            pFila=7-pTam*pDF;
        }
        if (pCol+pTam*pDC>=8){
            pCol=7-pTam*pDC;
        }
    }

    public boolean puedePonerBarco(){
        return puedePonerBarco(tablero, pTam, pFila, pCol, pHor);
    }

    public boolean victoria(int tab[][], boolean bTab[][]){
        for (int n=0;n<8;n++){
            for (int m=0;m<8;m++){
                if (bTab[n][m]==false && tab[n][m]!=0){
                    return false;
                }
            }
        }
        return true;
    }

   /* public void dispararEl(){
        int f,c;
        do{
            f=(int)(Math.random()*8);
            c=(int)(Math.random()*8);
        }while(bTableroMio[f][c]==true);
        bTableroMio[f][c]=true;
    }*/

    public JNavalDuo() {
        Imagenes i =new Imagenes();

//        AudioPlayer sountrack = audio.getAudio();
//        sountrack.run();
        portada=i.cargarPortadaDuo();
        tablero=i.cargarTableroDuo();
//        Audio audio = new Audio();
//        AudioClip audio1=audio.getAudio("/home/miguel/asd/src/main/java/SoundTrack.wav");
//        audio1.play();
        initComponents();
        setBounds(0,0,1600,600);
        addMouseListener(
                new MouseAdapter() {
                    public void mouseClicked(MouseEvent e) {
                        if (e.getModifiers() == MouseEvent.BUTTON3_MASK && nEstado==1){
                            pHor=1-pHor;
                            rectificarBarcoPoner();
                            repaint();
                            return;
                        }
                        if (nEstado==0){
                            nEstado=1;
                            iniciarPartida();
                            repaint();
                        }else if (nEstado==1){
                            if (puedePonerBarco()){
                                int pDF=0;
                                int pDC=0;
                                if (pHor==1){
                                    pDF=1;
                                }else{
                                    pDC=1;
                                }
                                for (int m=pFila;m<=pFila+(pTam-1)*pDF;m++){
                                    for (int n=pCol;n<=pCol+(pTam-1)*pDC;n++){
                                        tableroMio[m][n]=pTam;
                                        tableroSuyo2[m][n]=pTam;
                                    }
                                }
                                pTam--;
                                if (pTam==0){
                                    nEstado=2;
                                    repaint();
                                }
                            }
                        }else if (nEstado==2){
                            int f=(e.getY()-200)/30;
                            int c=(e.getX()-450)/30;
                            if (f!=pFila || c!=pCol){
                                pFila=f;
                                pCol=c;
                                if (celdaEstaEnTablero(f, c)){
                                    if (bTableroSuyo[f][c]==false){
                                        bTableroSuyo[f][c]=true;
                                        repaint();
                                        if (victoria(tableroSuyo, bTableroSuyo)){
                                            JOptionPane.showMessageDialog(null, "Has ganado");
                                            nEstado=0;
                                        }
//                                        dispararEl();
                                        repaint();
                                        if (victoria(tableroMio, bTableroMio)){
                                            JOptionPane.showMessageDialog(null, "Has perdido");
                                            nEstado=0;
                                        }
                                        repaint();
                                    }
                                }
                            }
                        }
                    }
                }
        );
        addMouseMotionListener(
                new MouseMotionAdapter() {
                    @Override
                    public void mouseMoved(MouseEvent e) {
                        int x=e.getX();
                        int y=e.getY();
                        if (nEstado==1 && x>=100 && y>=200 && x<100+30*8 && y<200+30*8){
                            int f=(y-200)/30;
                            int c=(x-100)/30;
                            if (f!=pFila || c!=pCol){
                                pFila=f;
                                pCol=c;
                                rectificarBarcoPoner();
                                repaint();
                            }
                        }
                    }
                }
        );

    }

    public boolean noHayInvisible(int tab[][], int valor, boolean bVisible[][]){
        for (int n=0;n<8;n++){
            for (int m=0;m<8;m++){
                if (bVisible[n][m]==false){
                    if (tab[n][m]==valor){
                        return false;
                    }
                }
            }
        }
        return true;
    }
    //pintar tablero 8 x 8 para color amarrillo toque ,rojo hundido , amarillo agua ,gris barcos ,
    public void pintarTablero(Graphics g, int tab[][], int x, int y, boolean bVisible[][]){
        for (int n=0;n<8;n++){
            for (int m=0;m<8;m++){
                if (tab[n][m]>0 && bVisible[n][m]){
                    g.setColor(Color.yellow);
                    if (noHayInvisible(tab, tab[n][m], bVisible)){
                        g.setColor(Color.red);
                    }
                    g.fillRect(x+m*30, y+n*30, 30, 30);
                }
                if (tab[n][m]==0 && bVisible[n][m]){
                    g.setColor(Color.cyan);
                    g.fillRect(x+m*30, y+n*30, 30, 30);
                }
                if (tab[n][m]>0 && tab==tableroMio && !bVisible[n][m]){
                    g.setColor(Color.gray);
                    g.fillRect(x+m*30, y+n*30, 30, 30);
                }
                g.setColor(Color.black);
                g.drawRect(x+m*30, y+n*30, 30, 30);
                if (nEstado==1 && tab==tableroMio && tab==tableroSuyo2){
                    int pDF=0;
                    int pDC=0;
                    if (pHor==1){
                        pDF=1;
                    }else{
                        pDC=1;
                    }
                    if (n>=pFila && m>=pCol && n<=pFila+(pTam-1)*pDF && m<=pCol+(pTam-1)*pDC){
                        g.setColor(Color.green);
                        g.fillRect(x+m*30, y+n*30, 30, 30);
                    }
                }
            }
        }
    }

    boolean bPrimeraVez=true;

    //carga tablero y portada si es por primera vez cargue todo a la vez
    public void paint(Graphics g){
        if (bPrimeraVez){
            g.drawImage(portada, 0,0,1,1,this);
            g.drawImage(tablero, 0,0,1,1,this);
            bPrimeraVez=false;
        }
        if (nEstado==0){
            g.drawImage(portada, 0, 0, this);
        }else {
            g.drawImage(tablero, 0, 0, this);
            pintarTablero(g, tableroMio, 100, 200, bTableroMio);
            pintarTablero(g, tableroSuyo, 450, 200, bTableroSuyo);
            pintarTablero(g, tableroMio, 900, 200, bTableroMio);
            pintarTablero(g, tableroSuyo, 1250, 200, bTableroSuyo);
        }
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();

    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new JNavalDuo().setVisible(true);
            }
        });
    }

}


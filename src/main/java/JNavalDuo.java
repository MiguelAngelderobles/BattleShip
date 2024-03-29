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
    //cree estas 2 variables para que fuera invisibles.
    int tableroMioIn[][] = new int[8][8];
    int tableroSuyoIn[][] = new int[8][8];

    boolean bPrimeraVez=true;


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
    // pongo barcos veo si esta fuera
    public boolean puedePonerBarco(int tab[][], int tam, int f, int c, int hor){
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
                    if (tab[f2][c2]!=0){
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public void iniciarPartida(){
        for (int n=0;n<8;n++){
            for (int m=0;m<8;m++){
                tableroMio[n][m]=0;
                tableroSuyo[n][m]=0;
                bTableroMio[n][m]=false;
                bTableroSuyo[n][m]=false;
            }
        }
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
        return puedePonerBarco(tableroMio, pTam, pFila, pCol, pHor);
    }
    //cree un poner barcos E para que actuara con el barco de jugador 2.
    public boolean puedePonerBarcoE(){
        return puedePonerBarco(tableroSuyo, pTam, pFila, pCol, pHor);
    }
    //cuantos true false de barcos estan acertados
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

    //cargo imagenes , creo ventana y con los estados
// controlo los mouse event 1 empezar juego
//2 pone barcos jugador 1
//3 pone barcos jugador 2
//4 quien gana
    public JNavalDuo() {
        Imagenes i =new Imagenes();
        portada=i.cargarPortadaDuo();
        tablero=i.cargarTableroDuo();
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
                                        tableroMioIn[m][n]=pTam;//agregue nada mas esto para que la flota invisible tuviera los mismos valores
                                    }
                                }
                                pTam--;
                                if (pTam==0){
                                    nEstado=2;
                                    pTam = 5;
                                    repaint();
                                }
                            }
                        }else if(e.getModifiers() == MouseEvent.BUTTON3_MASK && nEstado==2){
                            //puse los mismos condicionales en este if para que pudiera cambiar las filas y columnas cuando entre a estado 2
                            // (no, no trates de juntarlos en 1 por que explota).
                            pHor=1-pHor;
                            rectificarBarcoPoner();
                            repaint();
                            if(nEstado== 2){
                                if (puedePonerBarcoE()){
                                    int pDF=0;
                                    int pDC=0;
                                    if (pHor==1){
                                        pDF=1;
                                    }else{
                                        pDC=1;
                                    }
                                    for (int h=pFila;h<=pFila+(pTam-1)*pDF;h++){
                                        for (int j=pCol;j<=pCol+(pTam-1)*pDC;j++){
                                            tableroSuyo[h][j]=pTam;
                                            tableroSuyoIn[h][j]=pTam;//aqui esta la flota enemiga
                                        }
                                    }
                                    pTam--;
                                    if (pTam==0){
                                        nEstado=3;
                                        repaint();
                                    }
                                }

                            }
                        }else if (nEstado==3){
                            int f=(e.getY()-200)/30;
                            int c=(e.getX()-450)/30;
                            if (f!=pFila || c!=pCol){
                                pFila=f;
                                pCol=c;
                                if (celdaEstaEnTablero(f, c)){
                                    if (bTableroSuyo[f][c]==false){
                                        bTableroSuyo[f][c]=true;
                                        nEstado = 4;
                                        repaint();
                                        if (victoria(tableroSuyo, bTableroSuyo)){
                                            JOptionPane.showMessageDialog(null, "Jugador 1: Has ganado");
                                            nEstado=0;
                                        }
                                        repaint();
//                                        dispararEl();

                                    }
                                }
                            }
                        }else if (nEstado==4){
                            int f=(e.getY()-200)/30;
                            int c=(e.getX()-1250)/30;
                            if (f!=pFila || c!=pCol){
                                pFila=f;
                                pCol=c;
                                if (celdaEstaEnTablero(f, c)){
                                    if (bTableroMio[f][c]==false){
                                        bTableroMio[f][c]=true;
                                        nEstado = 3;
                                        repaint();
                                        if (victoria(tableroMio, bTableroMio)){
                                            JOptionPane.showMessageDialog(null, "Jugador 2: Has ganado");
                                            nEstado=0;
                                        }
//                                        dispararEl();
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
                        }//agregue un condicional para estado 2(que es obviamente jugador 2 por las dudas).
                        if (nEstado==2 && x>=900 && y>=200 && x<900+30*8 && y<200+30*8){
                            int f=(y-200)/30;
                            int c=(x-900)/30;
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
    //colores del tablero
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
                if (nEstado==1 && tab==tableroMio){
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
                }//agregue los condicinales para jugador 2/ estado 2
                if (tab[n][m]>0 && tab==tableroSuyo && !bVisible[n][m]){
                    g.setColor(Color.gray);
                    g.fillRect(x+m*30, y+n*30, 30, 30);
                }

                if (nEstado==2 && tab==tableroSuyo){
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
                g.setColor(Color.black);
                g.drawRect(x+m*30, y+n*30, 30, 30);

            }
        }
    }
    //el b primera vez es para que me cargue a la primera portada y tablero para que no se quede tildado
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
            pintarTablero(g, tableroSuyoIn, 450, 200, bTableroSuyo);//pintado jugador 2 invisible
            pintarTablero(g, tableroSuyo, 900, 200, bTableroSuyo);//pintado jugador 2
            pintarTablero(g, tableroMioIn, 1250, 200, bTableroMio);//pintado jugador 1 invisible
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

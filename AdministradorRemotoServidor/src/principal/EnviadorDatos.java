/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package principal;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.DataInputStream;
import java.io.DataOutputStream;

/**
 *
 * @author Carlos González <carlos85g at gmail.com>
 */
public class EnviadorDatos implements Runnable{
    private
        Thread
            Hilo;

    private    
        DataInputStream
            FlujoEntrada;
    
    private    
        DataOutputStream
            FlujoSalida;
    
    private
        boolean
            vivo;
    
    public EnviadorDatos(DataInputStream FlujoEntrada, DataOutputStream FlujoSalida) {
        /* Establecer los flujos de entrada y salida desde la conexión */
        this.FlujoEntrada = FlujoEntrada;
        this.FlujoSalida = FlujoSalida;
        
        /* Crear nuevo hilo */
        this.Hilo = new Thread(this);
        
        /* Iniciar proceso */
        this.Hilo.start();
    }

    /*
        Método para verificar que la clase sigue viva
    */
    public boolean isVivo(){
        return this.vivo;
    }
    
    @Override
    public void run() {
        Dimension
            Pantalla = Toolkit.getDefaultToolkit().getScreenSize();
        
        int
            ancho = (int)Pantalla.getWidth(),
            alto = (int)Pantalla.getHeight();
        
        try {
            /* Enviar el tamaño de pantalla */
            this.FlujoSalida.write(ancho);
            this.FlujoSalida.write(alto);
            this.FlujoSalida.flush();
            
            while(this.vivo){
                continue;
            }
            
            /* Hilo debe morir. Informar a cliente */
            this.FlujoSalida.write(-1);
            this.FlujoSalida.flush();
            
            /* Cerrar flujos */
            this.FlujoEntrada.close();
            this.FlujoSalida.close();
        } catch (Exception e) {
            System.out.println(this.getClass() + ": No fue posible continuar: " + e.getMessage());
        }
        
        /* Cerrar hilo */
        this.vivo = false;
    }
    
    
}
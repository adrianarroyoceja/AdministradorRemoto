package principal;

import java.awt.GraphicsEnvironment;
import java.awt.GraphicsDevice;
import java.awt.Robot;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Carlos González <carlos85g at gmail.com>
 */
public class Conexion {
    private
        ServerSocket
            ZocaloServidor;
    
    private volatile
        ReceptorComandos
            Cliente;
    
    private volatile
        EnviadorDatos
            Enviador;
    
    private volatile
        boolean
            vivo;

    public Conexion(ServerSocket ZocaloServidor){
        Socket
            Zocalo;
        
        Robot
            Automata;

        GraphicsDevice
            DispositivoGraficos;

        this.ZocaloServidor = ZocaloServidor;
        
        try{
            /* Obtener el dispositivo de gráficos a controlar */
            DispositivoGraficos = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();

            /* Crear el autómata con el dispositivo */
            Automata = new Robot(DispositivoGraficos);

            System.out.println("Esperando conexión del cliente...");
            
            /* Esperar cliente */
            Zocalo = this.ZocaloServidor.accept();

            /* Cliente se ha conectado. */
            System.out.println("Cliente conectado...");

            /* Crear el nuevo receptor de comandos */
            this.Cliente = new ReceptorComandos(
                Zocalo,
                Automata
            );
            
            /* Crear el nuevo enviador de datos */
            this.Enviador = new EnviadorDatos(
                Zocalo
            );
            
            /* Definir que está vivo */
            this.vivo = true;
        }catch (Exception e){
            /* Error. Conexión muerta */
            this.vivo = false;

            /* Informar en consola */
            System.out.println("Hubo un error en la creación de conexión: " + e.getMessage());
            
            e.printStackTrace();
        }
    }
    
    /*
        Verificar que conexión está viva
    */
    public boolean isVivo(){
        /* Si los enviadores están vivos, esta conexión está viva */
        if(!this.Cliente.isVivo() || !this.Enviador.isVivo()){
            this.vivo = false;
        }
        
        /* Retornar */
        return this.vivo;
    }
    
    /*
        Método para verificar que la clase está lista para usar
    */
    public boolean isListo(){       
        boolean
            retorno = false;
        
        if(this.Cliente != null && this.Enviador != null){
            retorno = (this.Cliente.isListo() && this.Enviador.isListo());
        }
        
        return retorno;
    }
    
    /*
        Método para recuperar el nombre del cliente
    */
    public String getNombre(){
        return this.Cliente.getNombre();
    }
    
    /*
        Método para recuperar los nombres inciales
    */
    public String getNombres(){
        return this.Cliente.getNombres();
    }
    
    /*
        Método para recuperar los apellidos
    */
    public String getApellidos(){
        return this.Cliente.getApellidos();
    }
    
    /*
        Método para recuperar el código
    */
    public String getCodigo(){
        return this.Cliente.getCodigo();
    }
    
    /*
        Método para recuperar la IP
    */
    public String getDireccionIP(){
        return this.Cliente.getDireccionIP();
    }
    
    /*
        Recuperar el cliente de esta conexión para control bidireccional
    */
    public ReceptorComandos getCliente(){
        return this.Cliente;
    }
    
    /*
        Método para matar el cliente
    */
    public void matar(){
        /* Matar el hilo local */
        this.vivo = false;
        
        /* Matar al cliente */
        this.Cliente.matar();
    }

    /*
        Método para bloquear o desbloquear al cliente
    */
    public void bloquear(boolean bloquear){
        /* Bloquear enviadores y receptores */
        this.Cliente.bloquear(bloquear);
        this.Enviador.bloquear(bloquear);
    }
}


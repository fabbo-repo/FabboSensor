/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sensorfabbobox;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JOptionPane;

/**
 *
 * @author Fabianno
 */
public class Bandeja{
    private JFrame ventana;
    private JFrame dispositivos;
    private PopupMenu popup = new PopupMenu();
    private Image image = null;
    private TrayIcon trayIcon = null;
    private Timer timer;
    private String msg;
    
    /**
     * Constructor de clase
     * @param frame
     */
    public Bandeja(JFrame frame) {
        this.ventana = frame;
        this.dispositivos = new Dispositivos();
        this.dispositivos.setLocationRelativeTo(this.ventana);
        ((Ventana) this.ventana).setDispositivos(this.dispositivos);
        
        try{
            this.image =new ImageIcon(getClass().getResource("/imagenes/icono.png")).getImage();
            this.ventana.setIconImage(image);
            this.dispositivos.setIconImage(image);
            this.trayIcon = new TrayIcon(image, "CPU:-, GPU:-", popup);
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(this.ventana, "Error: Faltan ficheros fuentes.");
            System.exit(0);
        }
        
        //comprueba si SystemTray es soportado en el sistema
        if (SystemTray.isSupported()){
            //obtiene instancia SystemTray
            SystemTray systemtray = SystemTray.getSystemTray();
            this.trayIcon.setImageAutoSize(true);
        
            //acciones del raton sobre el icono en la barra de tareas
            MouseListener mouseListener = new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent evt) {            
                    //Si se presiona el boton izquierdo en el icono
                    //si esta minimizado restaura JFrame
                    if(ventana.getExtendedState() == JFrame.ICONIFIED){
                        ventana.setVisible(true);
                        ventana.setExtendedState(JFrame.NORMAL);
                        ventana.repaint();
                    }
                }
                @Override
                public void mouseEntered(MouseEvent evt){/*nada*/}
                @Override
                public void mouseExited(MouseEvent evt){/*nada*/}
                @Override
                public void mousePressed(MouseEvent evt){/*nada*/}
                @Override
                public void mouseReleased(MouseEvent evt){/*nada*/}
            };

            /* ----------------- ACCIONES DEL MENU POPUP --------------------- */
            //Salir de aplicacion
            ActionListener exitListener = (ActionEvent e) -> {
                System.exit(0);
            };
            //Restaurar aplicacion
            ActionListener restoreListener = (ActionEvent e) -> {
                //si esta minimizado restaura JFrame
                if(this.ventana.getExtendedState() == JFrame.ICONIFIED){
                    this.ventana.setVisible(true);
                    this.ventana.setExtendedState(JFrame.NORMAL);
                    this.ventana.repaint();
                }
            };

            //Se crean los Items del menu PopUp y se añaden
            MenuItem exitAppItem = new MenuItem("Cerrar");
            exitAppItem.addActionListener(exitListener);
            this.popup.add(exitAppItem);

            MenuItem restoreAppItem = new MenuItem("Abrir");
            restoreAppItem.addActionListener(restoreListener);
            this.popup.add(restoreAppItem);

            /* ----------------- ACCIONES DEL MENU POPUP : END ---------------- */
            this.trayIcon.addMouseListener(mouseListener);
            //Añade el TrayIcon al SystemTray
            try{ systemtray.add(trayIcon); } 
            catch(AWTException e) {
                JOptionPane.showMessageDialog(this.ventana, "Error:" + e.getMessage());
            }
            
            /* --------------- Temperatura en BACKGROUND ------------------- */
            timer = new Timer();           
            timer.schedule(new TempSensor(this.ventana,this.dispositivos, this.trayIcon), 0, 700 );//Se ejecuta cada 0,7 segundo
        } 
        else{
            JOptionPane.showMessageDialog(this.ventana, "Error: SystemTray no es soportado");
            return;
        }

        //Cuando se minimiza JFrame, se oculta para que no aparesca en la barra de tareas
        this.ventana.addWindowListener(new WindowAdapter(){
            @Override
            public void windowIconified(WindowEvent e){
               ventana.setVisible(false);//Se oculta JFrame
            }
        });
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import com.profesorfalken.jsensors.JSensors;
import com.profesorfalken.jsensors.model.components.Components;
import com.profesorfalken.jsensors.model.components.Cpu;
import com.profesorfalken.jsensors.model.components.Gpu;
import com.profesorfalken.jsensors.model.sensors.Temperature;
import java.awt.TrayIcon;
import java.util.List;
import java.util.TimerTask;
import javax.swing.JFrame;

/**
 *
 * @author Fabianno
 */

public class TempSensor extends TimerTask{
    private JFrame ventana;
    private JFrame dispositivos;
    private TrayIcon trayIcon;
    private int temCPU = 0;
    private int temGPU = 0;
    private boolean done = false;
    
    public TempSensor(JFrame ventana, JFrame dispositivos, TrayIcon trayIcon){
        super();
        this.ventana = ventana;
        this.dispositivos = dispositivos;
        this.trayIcon = trayIcon;
        
    }
    
    @Override
    public void run() {
        this.actionBackground();
    }

    /**
     * accion a realizar cuando la aplicacion a sido minimizada
     */
    public void actionBackground(){
        if(!this.done) this.updateName();
        this.updateTemp();
        //((Ventana) ventana).updateCPU(this.temCPU);
        //((Ventana) ventana).updateGPU(this.temGPU);
        //if(((Dispositivos) this.dispositivos).isDevConnected())
        //    ((Dispositivos) this.dispositivos).send(this.temCPU+"-"+this.temGPU);
        this.trayIcon.setToolTip("CPU:"+this.temCPU+", GPU:"+this.temGPU);
    }
    
    public void updateName(){
        List<Cpu> cpus = JSensors.get.components().cpus;
        List<Gpu> gpus = JSensors.get.components().gpus;
        /*if(cpus != null && cpus.size() > 0)
            ((Ventana) ventana).updateCPUName(cpus.get(0).name);
        if(gpus != null && gpus.size() > 0)
            ((Ventana) ventana).updateGPUName(gpus.get(0).name);
        this.done = true; */
    }
        
    public void updateTemp() {
        List<Cpu> cpus = JSensors.get.components().cpus;
        
        if(cpus != null) {
            for(final Cpu cpu : cpus) {
                if(cpu.sensors != null) {
                    List<Temperature> temps = cpu.sensors.temperatures;
                    int max = 0;
                    for(final Temperature temp : temps) 
                        if(max < temp.value.intValue()) max = temp.value.intValue();
                    this.temCPU = max;
                }
            }
        }
        
        List<Gpu> gpus = JSensors.get.components().gpus;
        if(cpus != null) {
            for(final Gpu gpu : gpus) {
                if(gpu.sensors != null) {
                    List<Temperature> temps = gpu.sensors.temperatures;
                    int max = 0;
                    for(final Temperature temp : temps)
                        if(max < temp.value.intValue()) max = temp.value.intValue();
                    this.temGPU = max;
                }
            }
        }
    }
    
}

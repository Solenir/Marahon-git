

package com.maranhon;


/**
 *
 * @author solenir
 */
public class TransmissionControlThread extends Thread{
    private Communication communicationServerOperator;
    private String data;
    
    public TransmissionControlThread(Communication communicationServerOperator, String data){
        this.communicationServerOperator = communicationServerOperator;
        this.data = data;
    
    }
    
    @Override
    public void run (){
        try {
            communicationServerOperator.send(data);
            communicationServerOperator.receive();
        } catch (Exception ex) {
            System.err.println("Erro ao enviar informação");
        } 
        
        finally{
            return;            
        }
        
        
    }
    
}

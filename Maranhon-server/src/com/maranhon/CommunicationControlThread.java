

package com.maranhon;


/**
 *
 * @author solenir
 */
public class CommunicationControlThread extends Thread{
    private Communication communicationServerOperator;
    private String data;
    
    public CommunicationControlThread(Communication communicationServerOperator, String data){
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

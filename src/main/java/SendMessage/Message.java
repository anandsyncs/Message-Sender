/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SendMessage;

import CheckDatabase.Database;
import java.util.Calendar;
import java.util.GregorianCalendar;
import com.twilio.Twilio;
import com.twilio.type.PhoneNumber;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author anand
 */
public class Message implements Runnable {
//    public class Message{
    private final int SLEEP_START;
    private final int SLEEP_END;
    public static final String ACCOUNT_SID = "ACbc1ee2e41d241b9f5cde570d3ff4d7e3";
    public static final String AUTH_TOKEN = "617753af787ce4b1ed7fc2eb996e0e4c";
    public static String phoneNumber; 
   
    public Message(int start,int duration,String number){
         SLEEP_START=start+new GregorianCalendar().get(Calendar.HOUR);
         SLEEP_END=SLEEP_START+duration;
         phoneNumber=number;
        try {
            Database d=new Database();
            d.resetQuery();
            d.setStartTime();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Message.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Message.class.getName()).log(Level.SEVERE, null, ex);
        }
         
         System.out.println(start+" "+duration+" "+number);
    }
    
    @Override
     public void run(){
    
         Twilio.init(ACCOUNT_SID,AUTH_TOKEN);
        try{
            
            boolean sleep = SLEEP_START<SLEEP_END;
            int count=5;
            while(true) {
                
                int nowHour=new GregorianCalendar().get(Calendar.HOUR);
                boolean sleeping=(sleep && nowHour>SLEEP_START && nowHour<SLEEP_END) || (!sleep && (nowHour>SLEEP_START || nowHour<SLEEP_END));
                if(sleeping){
                    Thread.sleep(1000*60*60);
                    continue;
                }
                
                com.twilio.rest.api.v2010.account.Message message = com.twilio.rest.api.v2010.account.Message.create(
                new PhoneNumber(phoneNumber), // TO number
                new PhoneNumber("+18559532929"), // From Twilio number
                "Dude,Your Name is John"
        
                ).execute();
            
                if(isMessageSent(message) || count==0){
                    count=6;
                    Thread.sleep(1000*60*60);
                }
                count--;
            }
        
        }

        catch(Exception e){
            System.out.println(e);
        }
     }
           
     private boolean isMessageSent(com.twilio.rest.api.v2010.account.Message message){
         String status=String.valueOf(message.getStatus());
         while(!(status.equals("delivered") || status.equals("failed") || status.equals("undelivered"))){
             status=String.valueOf(message.getStatus());
         }
             if(status.equals("delivered")){
                 return true;
             }
             else{
                 logFailedMessage(message);
                 return false;
             }
         
         
     }
     
     private void logFailedMessage(com.twilio.rest.api.v2010.account.Message message){
        try {
            Database d=new Database();
            d.logFailedMessage(message.getSid(), (int) System.nanoTime());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Message.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Message.class.getName()).log(Level.SEVERE, null, ex);
        }
     }
     public static void main(String[] args) {
        Message m=new Message(235, 2356, "+919690640340");
        m.run();
    }
}

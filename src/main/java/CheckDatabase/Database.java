/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CheckDatabase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author anand
 */
public class Database {
    public static final String DATABASE_NAME="socialcops";
    public static final String ERROR_TABLE="errors"; //to store messages not sent
    public static final String START_TIME="time";
    Connection con;
    public Database() throws ClassNotFoundException, SQLException{
        
    }
    public void resetQuery() throws SQLException, ClassNotFoundException{
        Statement stmt;
        String url = "jdbc:mysql://localhost:3306";
//        Class.forName("com.mysql.jdbc.Driver");
        con = DriverManager.getConnection(url, "root", ""); 
        stmt = con.createStatement();
        stmt.execute("DROP DATABASE IF EXISTS "+ DATABASE_NAME);
        String sql="create database "+DATABASE_NAME;
        stmt.execute(sql);
        stmt.execute("USE "+DATABASE_NAME);
        sql="Create TABLE "+ERROR_TABLE+" ( ID VARCHAR(50) PRIMARY KEY, TIME varchar(15) );";
        stmt.execute(sql);
        sql="Create TABLE "+START_TIME+" ( START varchar(10) );";
        stmt.execute(sql);
        stmt.close();
        con.close();
    }
    
    public Statement getConnectionStatement() throws SQLException, ClassNotFoundException{
        Statement stmt;
        
        String url = "jdbc:mysql://localhost:3306/socialcops";
//        Class.forName("com.mysql.jdbc.Driver");
        con = DriverManager.getConnection(url, "root", ""); 
        stmt = con.createStatement();
        
        return stmt;
    }
    
    public String getNotSentMessages() throws SQLException, ClassNotFoundException{
        StringBuilder result=new StringBuilder();
        Statement stmt=getConnectionStatement();
        String sql="Select * from "+ERROR_TABLE+";";
        ResultSet rs=stmt.executeQuery(sql);
        while(rs.next()){
            result.append(rs.getString(1));
            result.append(" At time : ").append(rs.getString(2));
            result.append("\n");
        }
        stmt.close();
        con.close();
        return result.toString();
    }
    
    public void logFailedMessage(String id,int time) throws SQLException, ClassNotFoundException{
        Statement stmt=getConnectionStatement();
        String sql="insert into "+ERROR_TABLE+" values('"+id+"','"+time+"');";
        System.out.println(stmt.executeUpdate(sql));
        stmt.close();
        con.close();
    }
    
    public void setStartTime(){
        Statement stmt;
        try {
            stmt = getConnectionStatement();
            String sql="insert into "+START_TIME+" values('"+System.nanoTime()+"');";
            stmt.close();
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public int getStartTime() throws SQLException, ClassNotFoundException{
        Statement stmt=getConnectionStatement();
        String sql="Select * from "+START_TIME;
        ResultSet rs=stmt.executeQuery(sql);
        int time=Integer.parseInt(rs.getString(1));
        stmt.close();
        con.close();
        return time;
    }
    
    public String getRunningTime(){
        int time=0;
        try {
             time=((int) (System.nanoTime()-getStartTime()))/1000000000;
            
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
        return (time+" seconds");
    }
//    public ArrayList<>
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Database d=new Database();
        d.resetQuery();
        d.logFailedMessage("1234", 45);
        System.out.println(d.getNotSentMessages());
    }
    
}

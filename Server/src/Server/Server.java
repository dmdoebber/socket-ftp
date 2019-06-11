/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author daniel
 */
public class Server extends Thread{
    private final ServerSocket server;
    
    ServerView view;
    
    public Server() throws IOException{
        server = new ServerSocket(12345);
        
        view = new ServerView();
        view.setVisible(true);
    }
    
    @Override
    public void run(){
        while(true){
            try {
                Socket s = server.accept();
                Connection c = new Connection(s);
                c.start();

            } catch (IOException ex) {
                System.out.println("Erro!"+ex);
            }
        }
    }
    
    
    
    
    
}

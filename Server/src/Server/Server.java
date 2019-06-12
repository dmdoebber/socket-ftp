/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author daniel
 */
public class Server extends Thread{
    private final ServerSocket serverSocket;
    private final ServerView view;
    
    public Server(ServerView view) throws IOException{
        this.view = view;
        
        serverSocket = new ServerSocket(view.getPorta());
        view.addLog("Servidor iniciado na porta " + serverSocket.getLocalPort());
    }
    
    @Override
    public void run(){
        try {
             while(true){
                Socket socket = serverSocket.accept();
                Connection c = new Connection(socket, view);
                c.start();
             }
        } catch (IOException ex) {
            System.out.println("IOException (Server)"  + ex);
        }
    }
    
    public void StopServer() throws IOException{
        serverSocket.close();
        this.interrupt();
    }
    
}

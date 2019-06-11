/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 *
 * @author daniel
 */
public class Client extends Thread{
    private Socket socket;
    private final ClientView view;
    private FeedBuffer feedBuffer;
    
    String mode;
    
    public Client(ClientView view, String mode){
        this.view = view;
        this.mode = mode;
        
        if(mode.equals("SEND"))
        {
            feedBuffer = new FeedBuffer(view.getFile());
            feedBuffer.start();
        }
    }
    
    
    @Override
    public void run(){
        try{
            InetAddress ip = InetAddress.getByName(view.getIP());
            int porta = view.getPorta();
            
            socket = new Socket(ip, porta); 

            BufferedOutputStream output = new BufferedOutputStream(socket.getOutputStream());
            BufferedInputStream input = new BufferedInputStream(socket.getInputStream());
            
            // Testar conexão
            if (mode.equals("PING"))
            {
                byte[] header = new  byte[128];
                System.arraycopy(mode.getBytes(), 0, header, 0, mode.length());
                output.write(header, 0, header.length);
                GetResponse(input, 10, TimeUnit.SECONDS);
                return;
            }
            
            byte[] header = feedBuffer.getBytesHeader(mode, view.getFileName());
            output.write(header, 0, header.length);
            output.flush();
            
            List<Frame> frames = new ArrayList<>(10);
            List<Frame> resend = new ArrayList<>();
            
            while (!feedBuffer.IsEmpty())
            {
                frames.addAll(resend);
                
                while(frames.size() < 10 && !feedBuffer.IsEmpty())
                    frames.add(feedBuffer.Dequeue());
                
                for(int i = 0; i < frames.size(); i++)
                {
                    byte[] dataOUT = frames.get(i).getBytes();
                    
                    /*
                    if ( Math.random() * 100 < view.getNoisePercent()){
                        dataOUT[28] = (char)0;
                    }*/
                    
                    output.write(dataOUT, 0, dataOUT.length);
                    output.flush();
                }
                
                byte[] dataIN = GetResponse(input, 500, TimeUnit.MILLISECONDS);
                
                resend.clear();
                if (dataIN == null)
                {
                    resend.addAll(frames);
                }
                else
                {
                    for(int i = 0; i < frames.size(); i++)
                        if (dataIN[i] == '0')    
                            resend.add(frames.get(i));
                }
                
                frames.clear();
            }

            socket.close();
        }catch(IOException ex){
            System.out.println("Error (Server connection): " + ex);
        }
    }
    
    private byte[] GetResponse(BufferedInputStream input, int time, TimeUnit unit) 
    {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        Future<byte[]> handler = executor.submit(() -> {
            byte[] data = new byte[10];
            input.read(data, 0, data.length);
            return data;
        });
        
        byte[] data = null;
        try {    
            data = handler.get(time, unit);
        } catch (TimeoutException | InterruptedException | ExecutionException ex) {
            System.out.println("Erro (timeout): " + ex);
            handler.cancel(true);
        }
        executor.shutdownNow();
        
        return data;
    }
}

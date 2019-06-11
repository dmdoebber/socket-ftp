/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;

/**
 *
 * @author daniel
 */
public class Connection extends Thread{
    private final Socket socket;
    
    
    public Connection(Socket socket){
        this.socket = socket;
    }
    
    @Override
    public void run(){
        
        long start = System.currentTimeMillis();
        
        try {
            BufferedOutputStream output = new BufferedOutputStream(socket.getOutputStream());
            BufferedInputStream input = new BufferedInputStream(socket.getInputStream());
            
            byte[] header = new byte[128];
            input.read(header, 0, header.length);
            
            String mode = new String(ByteFunctions.subArray(header, 0, 4));
            
            
            // Testar conexão
            if(mode.equals("PING")) { output.write(new byte[1]); return; }
            // Testar conexão
            
        
            String filename  = new String(ByteFunctions.subArray(header, 4, 36));
            long size = ByteFunctions.ByteArrayToLong(ByteFunctions.subArray(header, 36, 44));
            int num_frames = ByteFunctions.ByteArrayToInteger(ByteFunctions.subArray(header, 44, 48));
            
            System.out.println("Header");
            System.out.println("name: " + filename);
            System.out.println("size: " + size);
            System.out.println("frames: " + num_frames);
            
            Frame[] frames = new  Frame[num_frames];
            
            int success_frames = 0;
            while(success_frames < num_frames)
            {
                byte[] dataOUT = new byte[10];
                for(int i = 0; i < 10 && success_frames < num_frames; i++)
                {
                    byte[] dataIN = new byte[1024];
                    input.read(dataIN, 0, dataIN.length);
                    Frame frame = new Frame(dataIN);

                    if (frame.ValidateChecksum())
                    {
                        if((frames[frame.num_frame] == null))
                        {
                            frames[frame.num_frame] = frame;
                            success_frames++;
                        }
                        dataOUT[i] = '1';
                    }
                    else
                    {
                        dataOUT[i] = '0';    
                    }
                    
                    System.out.println("Leu " + success_frames + " frames com sucesso!");
                }
                
                output.write(dataOUT, 0, dataOUT.length);
                output.flush();
                
                //System.out.println("escreveu para o client " + Arrays.toString(dataOUT));
            }
            long end = System.currentTimeMillis();
            // ad log (end - start)
            
        } catch (IOException ex) {
            System.out.println("Error (Client connection): " + ex);
            ex.printStackTrace();
        } 
    }
}

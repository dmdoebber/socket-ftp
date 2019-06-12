/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 *
 * @author daniel
 */
public class Connection extends Thread{
    private final Socket socket;
    private final ServerView view;
    
    public Connection(Socket socket, ServerView view){
        this.socket = socket;
        this.view = view;
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
            String filename  = new String(ByteFunctions.subArray(header, 4, 36)).replaceAll(""+(char)0, "");
            long size = ByteFunctions.ByteArrayToLong(ByteFunctions.subArray(header, 36, 44));
            int num_frames = ByteFunctions.ByteArrayToInteger(ByteFunctions.subArray(header, 44, 48));
            
            view.addLog("\nIniciando recebimento de arquivo! \n"
                    + "Nome: " + filename + "\n"
                    + "Tamanho: " + String.format("%.2f", size / 1024.0f) + " KB\n");
            
            Frame[] frames = new  Frame[num_frames];
            
            int success_frames = 0;
            int error_frames = 0;
            
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
                        error_frames++;
                    }
                }
                
                output.write(dataOUT, 0, dataOUT.length);
                output.flush();
            }
            long end = System.currentTimeMillis();
            
            SaveFile(filename, frames);
            
            view.addLog("\nArquivo recebido com sucesso!\n"
                    + "Tempo: " + (end - start) + "ms\n"
                    + "Salvo em 'FILES/" + filename + "'\n"
                    + "Total de frames: " + success_frames + "\n"
                    + "Frames recebidos com erro: " + error_frames + "\n");
            
        } catch (IOException ex) {
            view.addLog("Erro no recebimento do arquivo!");
            System.out.println("Error (Client connection): " + ex);
        } 
    }
    
    // Salva o arquivo
    private void SaveFile(String filename, Frame[] frames) throws IOException
    {
        File file = new File("FILES\\"+filename);
        if (!file.exists())
        {
            file.createNewFile();
            try (BufferedOutputStream fileWriter = new BufferedOutputStream(new FileOutputStream(file))) {
                for (Frame frame : frames)
                {
                    fileWriter.write(frame.data, 0, frame.data.length);
                    fileWriter.flush();
                }
            }
        }
    }
}

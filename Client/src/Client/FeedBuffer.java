/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author danie
 */

public class FeedBuffer extends Thread
{
    private final int FRAME_SIZE = 993;
    private final int BLOCK_SIZE = 10;

    private final Queue<Frame> buffer;
    private final File file;
    private final ClientView view;

    public FeedBuffer(File file, ClientView view){
        buffer = new LinkedList<>();
        this.file = file;
        this.view = view;
    }

    @Override
    public void run(){
        try {
            String IP = InetAddress.getLocalHost().getHostAddress();
            InputStream inputStream = new FileInputStream(file);
            int count_frames = 0;

            while (true) {
                byte[] bytes = new byte[FRAME_SIZE * BLOCK_SIZE];

                int lenght = inputStream.read(bytes);
               
                // End of file
                if(lenght == -1)
                    break;

                int remaing = lenght;
                
                for(int i = 0; i < BLOCK_SIZE && i * FRAME_SIZE <= lenght; i++){
                    byte[] data = Arrays.copyOfRange(bytes, i * FRAME_SIZE, (i+1)* FRAME_SIZE);
                    Enqueue(new Frame(count_frames++, IP, view.getPorta(), data, Math.min(FRAME_SIZE, remaing)));
                    remaing-= FRAME_SIZE;
                }
            }
        } catch (IOException ex) {
                System.out.println(ex);
        }  
    }

    // ENQUEUE = buffer.offer(element) | buffer.add(element)
    public synchronized void Enqueue(Frame frame){
        buffer.offer(frame);
    }

    // DEQUEUE = buffer.remove() | buffer.poll()
    public synchronized Frame Dequeue()
    {
        return buffer.poll();
    }

    // LOOK = buffer.element() | buffer.peek()
    public synchronized Frame Look()
    {
        return buffer.peek();
    }

    // Vazio se o inicio da fila for igual null
    public synchronized boolean IsEmpty()
    {
        return Look() == null;
    }
    
    // Numero de frames gerados
    public int GetNumFrames()
    {
        double count =  1.0 * file.length() / FRAME_SIZE;
        
        if (count - (int)count == 0)
            return (int) count;
        else
            return (int) count + 1;
    }
    
    //  [0]4   [4]32      [36]8      [44]4        [48]80
    // {[MODE] [FILENAME] [FILESIZE] [NUM FRAMES] [...]}
    public byte[] getBytesHeader(String mode, String filename){
        byte[] bytes = new byte[128];
        
        byte[] size_b = ByteFunctions.LongToByteArray(file.length());
        byte[] num_frames_b = ByteFunctions.IntegerToByteArray(GetNumFrames());
        
        System.arraycopy(mode.getBytes(),     0, bytes,   0, mode.length());
        System.arraycopy(filename.getBytes(), 0, bytes,   4, filename.length());
        System.arraycopy(size_b,              0, bytes,  36, size_b.length);
        System.arraycopy(num_frames_b,        0, bytes,  44, num_frames_b.length);
        
        return bytes;
    }
}


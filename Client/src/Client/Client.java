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
import java.util.Arrays;
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
            feedBuffer = new FeedBuffer(view.getFile(), view);
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
            
            // Envia o header do arquivo para ele preparar o recebimento
            byte[] header = feedBuffer.getBytesHeader(mode, view.getFileName());
            output.write(header, 0, header.length);
            output.flush();
            
            // Listas de frames enviados e reenviados
            List<Frame> frames = new ArrayList<>(10);
            List<Frame> resend = new ArrayList<>();
            
            // AGURDAR O LEITOR DE ARQUIVO ALIMENTAR O BUFFER DE LEITURA
            while(feedBuffer.IsEmpty()){}
            
            view.addLog("Incio do envio do arquivo!");
            view.setLimitProgressBar(0, feedBuffer.GetNumFrames());
            
            int frames_enviados = 0;
            
            while (!feedBuffer.IsEmpty() || !resend.isEmpty())
            {
                // adiciona os frames que devem ser reenviados
                frames.addAll(resend);
                
                // pega do buffer os frames seguintes
                while(frames.size() < 10 && !feedBuffer.IsEmpty())
                    frames.add(feedBuffer.Dequeue());
                
                frames_enviados += frames.size();
                
                // percorre os frames
                for(int i = 0; i < frames.size(); i++)
                {
                    byte[] dataOUT;
                    
                    // verifica se deve ser enviado com ruido ou não
                    if (view.sendWithNoise() && !feedBuffer.IsEmpty())
                        dataOUT = frames.get(i).getBytesWithNoise(view.getNoisePercent());
                    else
                        dataOUT = frames.get(i).getBytes();
                    
                    // manda para o servidor o frame
                    output.write(dataOUT, 0, dataOUT.length);
                    output.flush();
                }
                
                // recebe a resposta com timeout
                byte[] dataIN = GetResponse(input, 500, TimeUnit.MILLISECONDS);
                
                resend.clear();
                
                // verifica a resposta do servidor
                // se for nullo (estorou o tempo do timeout)
                if (dataIN == null)
                    resend.addAll(frames);
                
                // se não adiciona os frames incorretos para serem reenviados
                else
                {
                    for(int i = 0; i < frames.size(); i++)
                        if (dataIN[i] == '0')    
                            resend.add(frames.get(i));
                }
                
                frames.clear();
                
                frames_enviados -= resend.size();
                
                
                view.setValueProgressBar(frames_enviados);
            }
            view.addLog("Arquivo enviado com sucesso!");
            
            //socket.close();
        }catch(IOException ex){
            view.addLog("Erro no envio do arquivo!");
            System.out.println("Error (Server connection): " + ex);
        }
    }
    
    private byte[] GetResponse(BufferedInputStream input, int time, TimeUnit unit) 
    {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        Future<byte[]> handler = executor.submit(() -> {
            byte[] data = new byte[10];
            // faz a leitura da resposta do servidor
            input.read(data, 0, data.length);
            return data;
        });
        
        byte[] data = null;
        try { 
            // verifica o timeout
            data = handler.get(time, unit);
        } catch (TimeoutException | InterruptedException | ExecutionException ex) {
            System.out.println("Erro (timeout): " + ex);
            handler.cancel(true);
        }
        executor.shutdownNow();
        
        return data;
    }
}

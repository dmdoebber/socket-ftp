package Client;

// Client frame class

import java.util.Arrays;


public class Frame{
                      //FRAME    [1024]
    int num_frame;    //   4 bytes  [0]
    String ip_origem; //  15 bytes  [4]
    int porta;        //   4 bytes [19]
    int checksum;     //   4 bytes [23]
    byte[] data;      // 997 bytes [27]
    
    public Frame(int num_frame, String ip_origem, int porta, byte[] data)
    {
        this.num_frame = num_frame;
        this.ip_origem = ip_origem;
        this.porta = porta;
        this.data = data;
    }
    
    public byte[] getBytes()
    {
        byte[] bytes = new byte[1024];
        
        byte[] num_frame_b = ByteFunctions.IntegerToByteArray(num_frame);
        byte[] ip_origem_b = ip_origem.getBytes();
        byte[] porta_b     = ByteFunctions.IntegerToByteArray(porta);
        byte[] checksum_b  = ByteFunctions.IntegerToByteArray(generateChecksum(data)); 
        
        System.arraycopy(num_frame_b, 0, bytes,  0, num_frame_b.length);
        System.arraycopy(ip_origem_b, 0, bytes,  4, ip_origem_b.length);
        System.arraycopy(porta_b,     0, bytes, 19, porta_b.length);
        System.arraycopy(checksum_b,  0, bytes, 23, checksum_b.length);
        System.arraycopy(data,        0, bytes, 27, data.length);
        
        return bytes;
    }
    
    public byte[] getBytesWithNoise(int noisePercent)
    {
        // ADICIONAR PARTE DE RUIDO EM RELAÇÃO A PORCENTAGEM
        return getBytes();
    }
    
    public static int generateChecksum(byte[] bytes)
    {
        String hex_value;        
        int checksum = 0, i;
        
        // Soma feita a cada dois caracteres (reduz chance de erros)
        for (i = 0; i < bytes.length - 2;  i+=2)
        {
            // Converte para hexadecimal para realizar a soma
            hex_value = Integer.toHexString((bytes[i] & 0xFF)) + Integer.toHexString(bytes[i+1] & 0xFF);
            // Realiza a soma do checksum
            checksum += Integer.parseInt(hex_value, 16);
        }
        
        // caso tiver numero impar de bytes
        if(bytes.length % 2 != 0)
        {
            hex_value = Integer.toHexString((byte)0 & 0xFF) + Integer.toHexString(bytes[i] & 0xFF);
            checksum += Integer.parseInt(hex_value, 16);
        }
        
        // Converte a soma para hexadecimal
        hex_value = Integer.toHexString(checksum);
        
        // Se a soma for superior a 4
        while (hex_value.length() > 4)
        {   
            // Numero de 0's concatenados
            int length = 4 - (hex_value.length() % 4);
            
            // adiciona 0's para garantir o tamanho de cada substring
            for(int a = 0; a < length; a++) hex_value = "0" + hex_value;
            
            // reinicia o valor do checksum
            checksum = 0;
            
            // realiza a soma a cada 4 bytes evitando o overflow
            for(i = 0; i < (hex_value.length() / 4); i++)
            {
                String sub = hex_value.substring(i * 4, (i + 1) * 4);
                checksum += Integer.parseInt(sub, 16);
            }
            
            // converte para hexadecimal para verificar se não houve overflow
            hex_value = Integer.toHexString(checksum);
        }
        
        // Pega o complemento do checksum
        checksum = generateComplement(checksum);
        
        return checksum;
    }
 
    private static int generateComplement(int checksum)
    {
        return Integer.parseInt("ffff", 16) - checksum;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

/**
 *
 * @author danie
 */

// Server frame class
public class Frame {
    
    int num_frame;    //   4 bytes  [0] 
    String ip_origem; //  15 bytes  [4]
    int porta;        //   4 bytes [19]
    int checksum;     //   4 bytes [23]
    int lenght_data;  //   4 bytes [27]
    byte[] data;      // 993 bytes [31]
    
    public Frame(byte[] bytes)
    {
        num_frame   = ByteFunctions.ByteArrayToInteger(ByteFunctions.subArray(bytes, 0, 4));
        ip_origem   = new String(ByteFunctions.subArray(bytes, 4, 19));
        porta       = ByteFunctions.ByteArrayToInteger(ByteFunctions.subArray(bytes, 19, 23));
        checksum    = ByteFunctions.ByteArrayToInteger(ByteFunctions.subArray(bytes, 23, 27));
        lenght_data = ByteFunctions.ByteArrayToInteger(ByteFunctions.subArray(bytes, 27, 31));
        data        = ByteFunctions.subArray(bytes, 31, 1024);
    }
    
    @Override
    public String toString()
    {
        return "["+ip_origem+":"+porta+"]: {"+ Integer.toHexString(checksum)+"} "+ lenght_data;
    }
    
    public boolean ValidateChecksum()
    {      
        // Gera o checksum da string recebida
        int generated_checksum = generateChecksum(data);
        
        // Pega o complemento do checksum da string recebida
        generated_checksum = generateComplement(generated_checksum);
        
        // Calcula a distancia do checksum
        int distace = generated_checksum + checksum;
        
        // Se a distancia for 0, o checksum é valido
        return generateComplement(distace) == 0;               
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

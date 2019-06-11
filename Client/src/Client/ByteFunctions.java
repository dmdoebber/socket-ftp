/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 *
 * @author danie
 */
public class ByteFunctions {
    
    public static byte[] IntegerToByteArray(int i)
    {
        return new byte[]{(byte)(i >> 24), (byte)(i >> 16),(byte)(i >> 8), (byte) i};
    }
    
    public static int ByteArrayToInteger(byte[] b)
    {
        return (int)((0xff & b[0]) << 24 | (0xff & b[1]) << 16  | (0xff & b[2]) << 8 | (0xff & b[3]));
    }
    
    public static byte[] LongToByteArray(long l){
        return ByteBuffer.allocate(8).putLong(l).array();
    }
    
    public static long ByteArrayToLong(byte[] b){
        return ByteBuffer.wrap(b).getLong();
    }
    
    public static byte[] subArray(byte[] array, int beg, int end) 
    {
        return Arrays.copyOfRange(array, beg, end);
    }
}

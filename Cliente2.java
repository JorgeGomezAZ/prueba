import java.net.Socket;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.nio.ByteBuffer;

class Cliente2
{
    static void read(DataInputStream f, byte[] b,int posicion, int longitud)throws Exception
    {
        while (longitud>0){
        
            int n =f.read(b,posicion,longitud);
            posicion +=n;
            longitud -=n;
        }
        
    }
    public static void main(String[] args) throws Exception
    {
        Socket conexion = null;
        for(;;)
        {
            try 
            {
                conexion = new Socket("52.188.162.136",50000);
                break;
            } 
            catch (Exception e) 
            {
                Thread.sleep(100);
            }
        }
        try 
        {
            double TInicio, TFin, tiempo;
            double TInicioB, TFinB, tiempoB;
            
            DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
            DataInputStream entrada = new DataInputStream(conexion.getInputStream());

            salida.writeInt(123);
            salida.writeDouble(12123.123134);
            salida.write("Hola".getBytes());

            byte[] buffer = new byte[4];
            entrada.read(buffer,0,4);
            System.out.println(new String(buffer,"UTF-8"));

            ByteBuffer b = ByteBuffer.allocate(5*8);
            b.putDouble(1.1);
            b.putDouble(1.2);
            b.putDouble(1.3);
            b.putDouble(1.4);
            b.putDouble(1.5);

            byte[] a = b.array();
            salida.write(a);

            //writeDouble
            TInicio = System.currentTimeMillis();

            double d = 1.0;
            for(int i = 0; i<10000;i++){
                salida.writeDouble(d);
                d++;
            }
            TFin = System.currentTimeMillis();
            tiempo = TFin - TInicio;
            System.out.println("Tiempo de envio en milisegundos utilizando writeDouble: " + tiempo);
            
            d = 1.0;
            //ByteBuffer
            ByteBuffer bB = ByteBuffer.allocate(10000*8);
            TInicioB = System.currentTimeMillis();
            
            for(int i = 0; i<10000;i++){
                bB.putDouble(d);
                d++;
            }
            byte[] aB = bB.array();
            salida.write(aB);
            TFinB = System.currentTimeMillis();
            tiempoB = TFinB - TInicioB;
            System.out.println("Tiempo de envio en milisegundos utilizando ByteBuffer: " + tiempoB);

            salida.close();
            entrada.close();
            conexion.close();

        } 
        catch (Exception e) {
            System.out.println(e.getMessage());
           
        }
    }
}
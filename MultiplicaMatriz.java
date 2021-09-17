import java.net.ServerSocket;
import java.net.Socket;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;


class MultiplicaMatriz
{

    static long[][] A;
    static long[][] B;
    static long[][] C;
    static int N = 10;
    static int nodo;
    
    static void read(DataInputStream f, byte[] b,int posicion, int longitud)throws Exception
    {
    
        while (longitud>0){
        
            int n =f.read(b,posicion,longitud);
            posicion +=n;
            longitud -=n;
        }
        
    }

    static class Worker extends Thread
    {
        int nodoR;
        Worker(int nodoR)
        {
            this.nodoR = nodoR;
        }
        public void run()
        {
            try
            {
                ServerSocket servidor = new ServerSocket(30000+nodoR);
                Socket conexion = servidor.accept();
                DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
                DataInputStream entrada = new DataInputStream(conexion.getInputStream());

                int elementos = N*N/2;
                ByteBuffer b = ByteBuffer.allocate(elementos*8);
                int x,y;
                x = (nodoR < 3) ? 0 : N/2;
                for (int i = 0; i < (N/2); i++)
                    for (int j = 0; j < N; j++)
                        b.putLong(A[i+x][j]);

                byte[] a = b.array();
                salida.write(a);

                y = (nodoR % 2 == 0) ? N/2 : 0;
                b.position(0);
                for (int i = 0; i < (N/2); i++)
                    for (int j = 0; j < N; j++)
                        b.putLong(B[i+y][j]);

                byte[] a2 = b.array();
                salida.write(a2);

                byte[] bc = new byte[elementos/2*8];
                read(entrada,bc,0,bc.length);
                ByteBuffer c = ByteBuffer.wrap(bc);



                for (int i = 0; i < (N/2); i++)
                    for (int j = 0; j <(N/2); j++)
                        C[i+x][j+y] = c.getLong();
                salida.close();
                entrada.close();
                conexion.close();
            }
            catch (Exception e) 
            {
                System.out.println(e.getMessage());
            }

        }
    }
    
    public static void main(String[] args) throws Exception
    {
        if(args.length != 1)
        {
            System.err.println("Uso:");
            System.err.println("java MultiplicaMatriz <nodo>");
            System.exit(0);
        }
        int nodo = Integer.valueOf(args[0]);
        if(nodo == 0)
        {
            A = new long[N][N];
            B = new long[N][N];
            C = new long[N][N];
            for (int i = 0; i < N; i++)
                for (int j = 0; j < N; j++)
                {
                    A[i][j] = (long)(3*i+j);
                    B[i][j] = (long)(3*i-j);
                } 

            //trasnponer matriz B
            for (int i = 0; i < N; i++)
                for (int j = 0; j < i; j++)
                {
                    long x = B[i][j];
                    B[i][j] = B[j][i];
                    B[j][i] = x;
                }

            Worker[] v = new Worker[4];
            
            for(int i = 0;i<4;i++)
            {
                Worker w = new Worker(i+1);
                v[i] = w;
                v[i].start();
            }
            for(int i = 0;i<4;i++)
            {
                v[i].join();
            }

            if(N == 10)
                for (long[] fila : C)
                    System.out.println(Arrays.toString(fila)); 

            
            long checksum = 0;
            for (int i = 0; i < N; i++)
                for (int j = 0; j < N; j++)
                    checksum += C[i][j];

            System.out.println("\nChecksum de matriz resultante: "+checksum); 
        }

        else
        {
            A = new long[N/2][N];
            B = new long[N/2][N];
            C = new long[N/2][N/2];
            Socket conexion = null;
            for(;;)
            {
                try 
                {
                    conexion = new Socket("52.188.162.136",30000+nodo);
                    break;
                } 
                catch (Exception e) 
                {
                    Thread.sleep(100);
                }
            }
            
            DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
            DataInputStream entrada = new DataInputStream(conexion.getInputStream());

            int elementos = N*N/2;

            byte[] a = new byte[elementos*8];
            read(entrada,a,0,elementos*8);
            ByteBuffer ba = ByteBuffer.wrap(a);

            byte[] ab = new byte[elementos*8];
            read(entrada,ab,0,elementos*8);
            ByteBuffer bb = ByteBuffer.wrap(ab);

            for (int i = 0; i < (N/2); i++)
                for (int j = 0; j < N; j++)
                {
                    A[i][j] = ba.getLong();
                    B[i][j] = bb.getLong();
                }


            for (int i = 0; i < (N/2); i++)
                for (int j = 0; j < (N/2); j++)
                    for (int k = 0; k < N; k++)
                        C[i][j] += A[i][k] * B[j][k];
            

            ByteBuffer b = ByteBuffer.allocate(elementos/2*8);
            for (int i = 0; i < (N/2); i++)
                for (int j = 0; j < (N/2); j++)
                    b.putLong(C[i][j]);

            byte[] c = b.array();
            salida.write(c);


            salida.close();
            entrada.close();
            conexion.close();
        }
    }
}
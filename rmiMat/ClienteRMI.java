import java.rmi.Naming;
import java.util.Arrays;
public class ClienteRMI
{
    static int N = 9;
    public static void main(String[] args) throws Exception
    {

        String url1 = "rmi://10.0.0.5/prueba";
        InterfaceRMI nodo1 = (InterfaceRMI) Naming.lookup(url1);
        String url2 = "rmi://10.0.0.6/prueba";
        InterfaceRMI nodo2 = (InterfaceRMI) Naming.lookup(url2);
        String url3 = "rmi://10.0.0.7/prueba";
        InterfaceRMI nodo3 = (InterfaceRMI) Naming.lookup(url3);
        double[][] A = new double[N][N];
        double[][] B = new double[N][N];
        double[][] C = new double[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
            {
                A[i][j] = (4*i+j);
                B[i][j] = (i-3*j);
            }
        //trasnponer matriz B
        for (int i = 0; i < N; i++)
            for (int j = 0; j < i; j++)
            {
                double x = B[i][j];
                B[i][j] = B[j][i];
                B[j][i] = x;
            }

        double[][] A1 = separa_matriz(A,0);
        double[][] A2 = separa_matriz(A,N/3);
        double[][] A3 = separa_matriz(A,(N/3)*2);
        double[][] B1 = separa_matriz(B,0);
        double[][] B2 = separa_matriz(B,N/3);
        double[][] B3 = separa_matriz(B,(N/3)*2);

        double[][] C1 = nodo1.multiplica_matrces(A1,B1,N);
        double[][] C2 = nodo1.multiplica_matrces(A1,B2,N);
        double[][] C3 = nodo1.multiplica_matrces(A1,B3,N);
        double[][] C4 = nodo2.multiplica_matrces(A2,B1,N);
        double[][] C5 = nodo2.multiplica_matrces(A2,B2,N);
        double[][] C6 = nodo2.multiplica_matrces(A2,B3,N);
        double[][] C7 = nodo3.multiplica_matrces(A3,B1,N);
        double[][] C8 = nodo3.multiplica_matrces(A3,B2,N);
        double[][] C9 = nodo3.multiplica_matrces(A3,B3,N);
        

        acomoda_matriz(C,C1,0,0);
        acomoda_matriz(C,C2,0,N/3);
        acomoda_matriz(C,C3,0,N/3*2);
        acomoda_matriz(C,C4,N/3,0);
        acomoda_matriz(C,C5,N/3,N/3);
        acomoda_matriz(C,C6,N/3,N/3*2);
        acomoda_matriz(C,C7,N/3*2,0);
        acomoda_matriz(C,C8,N/3*2,N/3);
        acomoda_matriz(C,C9,N/3*2,N/3*2);

        if(N==9)
        {
            System.out.println("\nMatriz A");
            for (double[] fila : A)
                System.out.println(Arrays.toString(fila));
            System.out.println("\nMatriz B");
            for (double[] fila : B)
                System.out.println(Arrays.toString(fila));
            System.out.println("\nMatriz C");
            for (double[] fila : C)
                System.out.println(Arrays.toString(fila));
        }
        

        double checksum = 0;
            for (int i = 0; i < N; i++)
                for (int j = 0; j < N; j++)
                    checksum += C[i][j];

            System.out.println("\nChecksum de matriz resultante: "+checksum); 
        
    }
    static double[][] separa_matriz(double[][] A, int inicio) //throws RemoteException
    {
        double[][] M = new double[N/3][N];
        for (int i = 0; i < (N/3); i++)
            for (int j = 0; j < N; j++)
                M[i][j] = A[i+inicio][j];
        return M;
    }

    static void acomoda_matriz(double[][] C, double[][] c, int renglon, int columna) //throws RemoteException
    {
        for (int i = 0; i < (N/3); i++)
            for (int j = 0; j < (N/3); j++)
                C[i+renglon][j+columna] = c[i][j];
    }
}
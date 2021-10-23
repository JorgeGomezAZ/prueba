import java.rmi.RemoteException;
import java.rmi.Remote;

public interface InterfaceRMI extends Remote
{
    public double[][] multiplica_matrces(double[][] A,double[][] B, int N) throws RemoteException;
    // public double[][] separa_matriz(double[][] A, int inicio) throws RemoteException;
    // public void acomoda_matriz(double[][] C, double[][] c, int renglon, int columna) throws RemoteException;
}
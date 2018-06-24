package pt.ulisboa.tecnico.softeng.broker.domain;

import pt.ulisboa.tecnico.softeng.broker.exception.BrokerException;

public class Client {

    private final Broker BROKER;
    private final String IBAN;
    private final String NIF;
    private final String LICENSE;
    private int AGE;

    public Client(Broker man, String iban, String nif, String license, int age){
        checkArguments(man, iban, nif, license, age);
        this.BROKER = man;
        this.IBAN = iban;
        this.NIF = nif;
        this.LICENSE = license;
        this.AGE = age;
    }

    private void checkArguments(Broker man, String iban, String nif, String license, int age){
        if (man == null || !Broker.brokers.contains(man)){
            throw new BrokerException("Broker null or does not exist");
        }
        if (iban == null || iban.trim().equals("")){
            throw new BrokerException("IBAN cannot be null or empty");
        }
        if (nif == null || nif.trim().equals("") || nif.length() != 9){
            throw new BrokerException("NIF null, empty or in wrong format");
        }
        if (license == null || license.trim().equals("")){
            throw new BrokerException("License cannot be null or empty");
        }
        if (age < 0){
            throw new BrokerException("Age cannot be negative");
        }
    }

    //########################################AUXILIARY METHODS#########################################################

    public Broker getBROKER() { return BROKER; }

    public String getIBAN() { return IBAN; }

    public String getNIF() { return NIF; }

    public String getLICENSE() { return LICENSE; }

    public int getAGE() { return AGE; }
}

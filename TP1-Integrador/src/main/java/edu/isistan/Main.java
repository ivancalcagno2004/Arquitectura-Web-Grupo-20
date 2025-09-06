package edu.isistan;

import edu.isistan.utils.DerbyHelper;

public class Main {
    public static void main(String[] args) throws Exception {
        DerbyHelper dbDerby = new DerbyHelper();
        dbDerby.createTables();
    }
}
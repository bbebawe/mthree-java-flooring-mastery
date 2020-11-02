/*
 * @Copyright Beshoy Bebawe 2020.
 */
package com.mthree.flooringmastery.dao;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

/**
 * implements AuditDao using text file
 *
 * @author beshoy
 */
public class AuditDaoFIleImpl implements AuditDao {

    private String auditFile;

    public AuditDaoFIleImpl() {
    }

    public AuditDaoFIleImpl(String auditFile) {
        this.auditFile = auditFile;
    }

    @Override
    public void writeAuditEntry(String entry) throws DataPersistenceException {
        PrintWriter out;
        try {
            // open audit file and append to it
            out = new PrintWriter(new FileWriter(auditFile, true));
        } catch (IOException e) {
            throw new DataPersistenceException("Could not persist audit information.", e);
        }
        // get timestamp object to be used for entry log
        LocalDateTime timestamp = LocalDateTime.now();
        // write entry to file
        out.println(timestamp.toString() + " : " + entry);
        out.flush();// flush output
    }
}

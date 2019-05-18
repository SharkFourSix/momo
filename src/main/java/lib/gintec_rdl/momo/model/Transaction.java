package lib.gintec_rdl.momo.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import lib.gintec_rdl.momo.utils.LocaleUtils;

/**
 * <p>
 * This root class contains the bare minimum details of a mobile money
 * transaction.</p>
 * <p>
 * Each transaction has a transaction id or reference number which uniquely
 * identifies it from other transactions.</p>
 * <p>
 * Applications should extend this class as needed.</p>
 *
 * @author CK
 */
public class Transaction implements Serializable {

    private String transactionId;
    private Date date;

    public void setDate(Date date) {
        this.date = date;
    }

    public void setTransactionId(String txId) {
        this.transactionId = txId;
    }

    /**
     *
     * @return Returns the date or null if date is not available or was not set.
     */
    public Date getDate() {
        return date;
    }

    /**
     * @return Returns the uniquely identifying string for this transaction
     */
    public String getTransactionId() {
        return transactionId;
    }

    @Override
    public boolean equals(Object o) {
        return o != null && (o instanceof Transaction)
            ? (transactionId != null ? transactionId.equalsIgnoreCase(((Transaction) o).transactionId) : false)
            : false;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 11 * hash + Objects.hashCode(this.transactionId);
        hash = 11 * hash + Objects.hashCode(this.date);
        return hash;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return transactionId + (date != null ? " - " + LocaleUtils.formatDate(date) : "");
    }
}

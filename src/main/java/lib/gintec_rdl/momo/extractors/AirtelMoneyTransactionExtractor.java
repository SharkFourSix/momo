package lib.gintec_rdl.momo.extractors;

import java.util.Map;
import lib.gintec_rdl.momo.extraction.TransactionExtractor;
import lib.gintec_rdl.momo.model.Transaction;

public class AirtelMoneyTransactionExtractor extends TransactionExtractor {

    @Override
    public Transaction extract(String from, String input, Map<String, String> extras) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

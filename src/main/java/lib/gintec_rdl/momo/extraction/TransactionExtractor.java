package lib.gintec_rdl.momo.extraction;

import java.util.Map;
import lib.gintec_rdl.momo.model.Transaction;

public abstract class TransactionExtractor {

    /**
     *
     * @param serviceNumber Service number or short code this extractor
     * supports.
     * @param input SMS text to extract data from
     * @param extras Extra parameters to pass to the extractor. Whether or not
     * this is optional is up to the extractor.
     * @throws Exception If the method doesn't handle its exceptions, they will
     * be wrapped inside this one.
     * @return Returns a {@link Transaction} or null if no transaction could not
     * be extracted.
     */
    public abstract Transaction extract(String serviceNumber, String input, Map<String, String> extras) throws Exception;
}

package lib.gintec_rdl.momo.extraction;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import lib.gintec_rdl.momo.model.Transaction;

/**
 * <p>
 * This class provides the facility to register and recall transaction
 * extractors. It is not needed. It simply provides the convenience of
 * instantiating and caching transaction extractors.</p>
 * <p>
 * The following is an example of how to use the class</p>
 * <p>
 * First define your extractors. Your extractor classes must have a
 * zero-argument constructor otherwise use the {@link #registerExtractor(java.lang.String, lib.gintec_rdl.momo.extraction.TransactionExtractor)
 * } method to register an already instantiated extractor.</p>
 * <pre>
 *     public class TransactionExtractor1 extends TransactionExtractor {
 *         &#0064;Override
 *         public abstract Transaction extract(String serviceNumber, String input, Map&lt;String, String&gt; extras) throws Exception {
 *             Transaction tx = new Transaction();
 *             return tx;
 *         }
 *     }
 *     public class TransactionExtractor2 extends TransactionExtractor {
 *         &#0064;Override
 *         public abstract Transaction extract(String serviceNumber, String input, Map&lt;String, String&gt; extras) throws Exception {
 *             Transaction tx = new Transaction();
 *             return tx;
 *         }
 *     }
 * </pre>
 * <p>
 * Then, somewhere in your application, you must register the extractors</p>
 * <pre>
 *     ExtractionService svc = ExtractionService.getInstance();
 *     String serviceProvider1ShortCode = "12345";
 *     String serviceProvider2ShortCode = "AWSUM";
 *     svc.registerExtractor(serviceProvider1ShortCode, TransactionExtractor1.class)
 *        .registerExtractor(serviceProvider2ShortCode, new TransactionExtractor2());
 * </pre>
 * <p>
 * To use the extractors, pass the message from your desired provider</p>
 * <pre>
 *     String input1 = "...", input2 = "...";
 *     HashMapMap&lt;String, String&gt; extraOptions1 = new HashMap&lt;,&gt;, extraOptions2 = null;
 *     Transaction1 transaction1 = svc.extract(serviceProvider1ShortCode, input1, extraOptions1, Transaction1.class);
 *     Transaction2 transaction2 = svc.extract(serviceProvider2ShortCode, input2, extraOptions2, Transaction2.class);
 * </pre>
 *
 * @author CK
 */
public final class ExtractionService {

    private static ExtractionService mInstance;
    private final Map<String, TransactionExtractor> mExtractors;

    private ExtractionService() {
        mExtractors = Collections.synchronizedMap(new LinkedHashMap<String, TransactionExtractor>());
    }

    /**
     *
     * @return The singleton instance
     */
    public synchronized static ExtractionService getInstance() {
        synchronized (ExtractionService.class) {
            return mInstance == null ? (mInstance = new ExtractionService()) : mInstance;
        }
    }

    /**
     * <p>
     * Register an already instantiated extractor.</p>
     *
     * @param sender The service short code.
     * @param extractor The extractor instance to register
     * @return the same instance for chained calls.
     */
    public ExtractionService registerExtractor(String sender, TransactionExtractor extractor) {
        synchronized (mExtractors) {
            if (!mExtractors.containsKey(sender)) {
                mExtractors.put(sender, extractor);
            }
        }
        return this;
    }

    /**
     * <p>
     * Register an extractor type that will be instantiated my the service.</p>
     *
     * @param <T> The generic type of extractor
     * @param sender The service short code.
     * @param klazz The type of the extending extractor
     * @return the same instance for chained calls.
     */
    public <T extends TransactionExtractor> ExtractionService registerExtractor(String sender, Class<T> klazz) {
        try {
            return registerExtractor(sender, klazz.newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *
     * @param <T> Generic type of extractor
     * @param sender Service short code
     * @param klazz Type of extending extractor
     * @return An instance of an extractor previously added through {@link #registerExtractor(java.lang.String, java.lang.Class)
     * } or {@link #registerExtractor(java.lang.String, lib.gintec_rdl.momo.extraction.TransactionExtractor)
     * }
     */
    public <T extends TransactionExtractor> T getExtractor(String sender, Class<T> klazz) {
        synchronized (mExtractors) {
            final TransactionExtractor te = mExtractors.get(sender);
            if (te != null) {
                return klazz.cast(te);
            }
        }
        return null;
    }

    /**
     * @see #getExtractor(java.lang.String, java.lang.Class)
     * @param sender Service short code or provider service
     * @return Returns an extractor
     */
    public TransactionExtractor getExtractor(String sender) {
        synchronized (mExtractors) {
            return mExtractors.get(sender);
        }
    }

    /**
     * Extracts a transaction from the given input.
     *
     * @param <T> Extractor extension type
     * @param from Service short code.
     * @param input The input containing transaction details.
     * @param extras Extra options to pass to the underlying extractor. For more
     * info, refer to the TransactionExtractor interface
     * @param klazz The type of transaction that is expected to be returned.
     * @return A transaction or null if there was an error
     * @see TransactionExtractor#extract(java.lang.String, java.lang.String,
     * java.util.Map)
     */
    public <T extends Transaction> T extract(String from, String input, Map<String, String> extras, Class<T> klazz) {
        final TransactionExtractor te;
        Transaction transaction = null;
        synchronized (mExtractors) {
            te = mExtractors.get(from);
        }
        if (te != null) {
            try {
                transaction = te.extract(from, input, extras);
            } catch (Exception ignored) {
                // ignored.printStackTrace();
            }
        }
        return klazz.cast(transaction);
    }
}

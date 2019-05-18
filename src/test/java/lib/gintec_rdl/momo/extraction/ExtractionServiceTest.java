package lib.gintec_rdl.momo.extraction;

import lib.gintec_rdl.momo.extractors.MpambaTransactionExtractor;
import lib.gintec_rdl.momo.model.*;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExtractionServiceTest {

    public ExtractionServiceTest() {
        ExtractionService.getInstance().registerExtractor(
            "MPAMBA",
            MpambaTransactionExtractor.class
        );
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testCashIn() {
        final double delta = 0.0D;
        String sender = "MPAMBA";
        String input = "Cash In from 123456-JOHN DOE INVESTMENT OUTLET on 06/05/2019 14:00:50.\n"
            + "Amt: 2,000.00MWK\n"
            + "Fee: 0.00MWK\n"
            + "Ref: 1A2B8C4D7E\n"
            + "Bal: 2,000.00MWK";

        MpambaCashInTransaction transaction = ExtractionService.getInstance()
            .extract(sender, input, null, MpambaCashInTransaction.class);

        assertEquals("123456", transaction.getAgent().getAgentCode());
        assertEquals("JOHN DOE INVESTMENT OUTLET", transaction.getAgent().getAgentName());
        assertEquals(2000.00D, transaction.getAmount(), delta);
        assertEquals(0.00D, transaction.getFee(), delta);
        assertEquals("1A2B8C4D7E", transaction.getTransactionId());
        assertEquals(2000.00D, transaction.getBalance(), delta);
    }

    @Test
    public void testDebit() {
        final double delta = 0.0D;
        String sender = "MPAMBA";
        String input = "Money Received from 265888555555   on 10/05/2019 23:06:26. \n"
            + "Amount: 100.00MWK \n"
            + "Ref: E5D4C3B2A1 \n"
            + "Bal: 290.00MWK";

        MpambaDebitTransaction transaction = ExtractionService.getInstance()
            .extract(sender, input, null, MpambaDebitTransaction.class);

        assertEquals("265888555555", transaction.getSenderPhone());
        assertEquals("E5D4C3B2A1", transaction.getTransactionId());
        assertEquals(100.00D, transaction.getAmount(), delta);
        assertEquals(290.00D, transaction.getBalance(), delta);
        //assertEquals("JOHN DOE", transaction.getSenderName());
    }

    @Test
    public void testCredit() {
        final double delta = 0.0D;
        String sender = "MPAMBA";
        String input = "Money Sent to 0881555555   on 02/04/2019 17:09:19. \n"
            + "Amount: 10,000.00MWK \n"
            + "Fee: 100.00MWK \n"
            + "Ref: 1A2B3C4D5E \n"
            + "Bal: 204.00MWK";

        MpambaCreditTransaction transaction = ExtractionService.getInstance()
            .extract(sender, input, null, MpambaCreditTransaction.class);

        assertEquals("1A2B3C4D5E", transaction.getTransactionId());
        assertEquals("0881555555", transaction.getRecipientPhone());
        assertEquals(10000.00d, transaction.getAmount(), delta);
        assertEquals(100.00d, transaction.getFee(), delta);
        assertEquals(204.00d, transaction.getBalance(), delta);
        //assertEquals("JOHN DOE", transaction.getRecipientName());
    }

    @Test
    public void testDeposit() {
        final double delta = 0.0D;
        String sender = "MPAMBA";
        String input = "Deposit from National Bank on 11/05/2019 04:55:07. Amount: 201.00MWK Fee: 0.00MWK Ref: 1B1B1B1BJZ Available Balance: 491.00MWK.";

        MpambaDepositTransaction transaction = ExtractionService.getInstance()
            .extract(sender, input, null, MpambaDepositTransaction.class);

        assertEquals("1B1B1B1BJZ", transaction.getTransactionId());
        assertEquals("National Bank", transaction.getSource());
        assertEquals(201.00D, transaction.getAmount(), delta);
        assertEquals(0.00D, transaction.getFee(), delta);
        assertEquals(491.00D, transaction.getBalance(), delta);
    }

    @Test
    public void testCashOut(){
        final double delta = 0.0D;
        String sender = "MPAMBA";
        String input = "Cash Out to AGENT SMITH - 1234567 on 12/05/2019 12:12:07.\n" +
                "Amt: 7,200.00MWK \n" +
                "Fee: 380.00MWK. \n" +
                "Ref: 8GHABCGDTF \n" +
                "Bal: 1,581.00MWK";

        MpambaCashOutTransaction transaction = ExtractionService.getInstance()
                .extract(sender, input, null, MpambaCashOutTransaction.class);

        assertEquals("8GHABCGDTF", transaction.getTransactionId());
        assertEquals("1234567", transaction.getAgent().getAgentCode());
        assertEquals("AGENT SMITH", transaction.getAgent().getAgentName());
        assertEquals(7200, transaction.getAmount(), delta);
        assertEquals(380, transaction.getFee(), delta);
        assertEquals(1581, transaction.getBalance(), delta);
    }
}

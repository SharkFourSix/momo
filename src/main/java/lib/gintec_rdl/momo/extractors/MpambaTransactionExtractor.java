package lib.gintec_rdl.momo.extractors;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lib.gintec_rdl.momo.extraction.TransactionExtractor;
import lib.gintec_rdl.momo.model.*;
import lib.gintec_rdl.momo.utils.TextUtils;

public final class MpambaTransactionExtractor extends TransactionExtractor {

    @Override
    public Transaction extract(String from, String input, Map<String, String> properties) throws Exception {
        if (!"MPAMBA".equals(from) || input == null) {
            return null;
        }

        if (input.startsWith("Money Sent to ")) {
            return extractCreditTransaction(input);
        }

        if (input.startsWith("Money Received from ")) {
            return extractDebitTransaction(input);
        }

        if (input.startsWith("Trans ID: ") || input.startsWith("Cash In from ")) {
            return extractCashInTransaction(input);
        }

        if (input.startsWith("Deposit from ")) {
            return extractDepositTransaction(input);
        }

        if(input.startsWith("Cash Out to")){
            return extractCashOutTransaction(input);
        }

        return null;
    }

    private Transaction extractCashOutTransaction(String input) throws ParseException {
        final Pattern pattern = Pattern.compile(
                "^Cash Out to (.+) - (.+) on ([0-9]{2}/[0-9]{2}/[0-9]{4} [0-9]{2}:[0-9]{2}:[0-9]{2}).$|^Amt: ([,0-9]+\\.00)MWK.*$|^Fee: ([,0-9]+\\.00)MWK.*$|^Ref: ([A-Z0-9]+)\\s*$|^Bal: ([,0-9]+\\.00)MWK\\s*$",
                Pattern.MULTILINE
        );
        final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.US);
        final MpambaCashOutTransaction transaction = new MpambaCashOutTransaction();
        final Matcher matcher = pattern.matcher(input);
        if(matcher.find()){
            transaction.setAgent(new MobileMoneyAgent(matcher.group(2), matcher.group(1)));
            transaction.setDate(dateFormat.parse(matcher.group(3)));
        }
        if(matcher.find()){
            transaction.setAmount(TextUtils.currency(matcher.group(4)));
        }
        if(matcher.find()){
            transaction.setFee(TextUtils.currency(matcher.group(5)));
        }
        if(matcher.find()){
            transaction.setTransactionId(matcher.group(6));
        }
        if(matcher.find()){
            transaction.setBalance(TextUtils.currency(matcher.group(7)));
        }
        return transaction;
    }

    private Transaction extractDepositTransaction(String input) throws ParseException {
        final Pattern pattern = Pattern.compile(
            "^Deposit from (.+) on ([0-9]{2}/[0-9]{2}/[0-9]{4} [0-9]{2}:[0-9]{2}:[0-9]{2})\\. Amount: ([0-9,]+\\.[0-9]{2})MWK Fee: ([0-9,]+\\.[0-9]{2})MWK Ref: ([A-Z0-9]+) Available Balance: ([0-9,]+\\.[0-9]{2})MWK\\.$"
        );
        final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.US);
        final MpambaDepositTransaction transaction = new MpambaDepositTransaction();
        final Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            transaction.setSource(matcher.group(1));
            transaction.setDate(dateFormat.parse(matcher.group(2)));
            transaction.setAmount(TextUtils.currency(matcher.group(3)));
            transaction.setFee(TextUtils.currency(matcher.group(4)));
            transaction.setTransactionId(matcher.group(5));
            transaction.setBalance(TextUtils.currency(matcher.group(6)));
        }
        return transaction;
    }

    private Transaction extractCreditTransaction(String input) throws ParseException {
        final Pattern pattern = Pattern.compile(
            "^Money Sent to (08[0-9]{8}) (.+)? on ([0-9]{2}/[0-9]{2}/[0-9]{4} [0-9]{2}:[0-9]{2}:[0-9]{2}).\\s*$|^Amount: ([0-9,]+\\.[0-9]{2})MWK\\s*$|Fee: ([0-9,]+\\.[0-9]{2})MWK\\s*$|^Ref: ([A-Z0-9]+)\\s*$|Bal: ([0-9,]+\\.[0-9]{2})MWK$",
            Pattern.MULTILINE
        );
        final MpambaCreditTransaction transaction = new MpambaCreditTransaction();
        final Matcher matcher = pattern.matcher(input);
        final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.US);
        if (matcher.find()) {
            transaction.setRecipientPhone(matcher.group(1));
            transaction.setRecipientName(TextUtils.trimmedOrNull(matcher.group(2)));
            transaction.setDate(dateFormat.parse(matcher.group(3)));
        }
        if (matcher.find()) {
            transaction.setAmount(TextUtils.currency(matcher.group(4)));
        }
        if (matcher.find()) {
            transaction.setFee(TextUtils.currency(matcher.group(5)));
        }
        if (matcher.find()) {
            transaction.setTransactionId(matcher.group(6));
        }
        if (matcher.find()) {
            transaction.setBalance(TextUtils.currency(matcher.group(7)));
        }
        return transaction;
    }

    private Transaction extractDebitTransaction(String input) throws ParseException {
        final Pattern pattern = Pattern.compile(
            "^Money Received from ([0-9]{10,12}) (.+)? on ([0-9]{2}/[0-9]{2}/[0-9]{4} [0-9]{2}:[0-9]{2}:[0-9]{2}).\\s*$|^Amount: ([0-9,]+\\.[0-9]{2})MWK\\s*$|^Ref: ([A-Z0-9]+)\\s*$|Bal: ([0-9,]+\\.[0-9]{2})MWK$",
            Pattern.MULTILINE
        );
        final MpambaDebitTransaction transaction = new MpambaDebitTransaction();
        final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.US);
        final Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            transaction.setSenderPhone(matcher.group(1));
            transaction.setSenderName(TextUtils.trimmedOrNull(matcher.group(2)));
            transaction.setDate(dateFormat.parse(matcher.group(3)));
        }
        if (matcher.find()) {
            transaction.setAmount(TextUtils.currency((matcher.group(4))));
        }
        if (matcher.find()) {
            transaction.setTransactionId(matcher.group(5));
        }
        if (matcher.find()) {
            transaction.setBalance(TextUtils.currency(matcher.group(6)));
        }
        return transaction;
    }

    private Transaction extractCashInTransaction(String input) throws ParseException {
        if (input.startsWith("Trans ID: ")) {
            final Pattern pattern = Pattern.compile(
                "^Trans ID: ([A-Z0-9.]+): you have received MK([0-9]+\\.[0-9]{2}) from ([A-Z0-9]+), (.+)\\. your new balance is MK([0-9]+\\.[0-9]{2})"
            );
            final MpambaCashInTransaction transaction = new MpambaCashInTransaction();
            final Matcher matcher = pattern.matcher(input);
            if (matcher.find()) {
                transaction.setTransactionId(matcher.group(1));
                transaction.setAmount(TextUtils.currency(matcher.group(2)));
                transaction.setAgent(new MobileMoneyAgent(matcher.group(3), matcher.group(4)));
                transaction.setBalance(TextUtils.currency(matcher.group(5)));
                transaction.setFee(0);
                transaction.setDate(null);
            }
            return transaction;
        } else if (input.startsWith("Cash In from")) {
            final Pattern pattern = Pattern.compile(
                "^Cash In from ([0-9]+)-([0-9A-Z\\s]+) on (([0-9]{2,4}/?){3} ([0-9]{2}:?){3})\\.\\s*$|^Amt: ([0-9,]+\\.[0-9]{2})MWK$|^Fee: ([0-9,]+\\.[0-9]{2})MWK\\s*$|^Ref: ([A-Z0-9]+)$|Bal: ([0-9,]+\\.[0-9]{2})MWK$",
                Pattern.MULTILINE
            );
            final MpambaCashInTransaction transaction = new MpambaCashInTransaction();
            final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.US);
            final Matcher matcher = pattern.matcher(input);
            if (matcher.find()) {
                transaction.setAgent(new MobileMoneyAgent(matcher.group(1), matcher.group(2)));
                transaction.setDate(dateFormat.parse(matcher.group(3)));
            }
            if (matcher.find()) {
                transaction.setAmount(TextUtils.currency(matcher.group(6)));
            }
            if (matcher.find()) {
                transaction.setFee(TextUtils.currency(matcher.group(7)));
            }
            if (matcher.find()) {
                transaction.setTransactionId(matcher.group(8));
            }
            if (matcher.find()) {
                transaction.setBalance(TextUtils.currency(matcher.group(9)));
            }
            return transaction;
        }
        return null;
    }
}

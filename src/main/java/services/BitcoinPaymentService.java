package services;

import org.apache.log4j.Logger;
import org.bitcoinj.core.Address;
import org.bitcoinj.core.Coin;
import org.bitcoinj.core.InsufficientMoneyException;
import org.bitcoinj.core.PeerGroup;
import org.bitcoinj.wallet.Wallet;

public class BitcoinPaymentService {

    private static final Logger LOGGER = Logger.getLogger(BitcoinPaymentService.class);

    private static final int AMOUNT_IN_COINS = 1000;

    public void sendCoin(String recipientAddress, PeerGroup peerGroup, Wallet wallet) {
        try {
            Coin amount = Coin.valueOf(AMOUNT_IN_COINS);
            Wallet.SendResult sendResult = wallet.sendCoins(peerGroup, Address.fromString(wallet.getParams(), recipientAddress), amount);
            LOGGER.info("Transaction hash: " + sendResult.tx.getHashAsString());
        } catch (InsufficientMoneyException e) {
            LOGGER.error(e.getMessage());
        }
    }
}

import org.apache.log4j.Logger;
import org.bitcoinj.core.BlockChain;
import org.bitcoinj.core.PeerGroup;
import org.bitcoinj.wallet.Wallet;
import services.BitcoinPaymentService;
import services.BlockchainService;
import services.WalletService;
import services.BalanceService;

import java.util.Optional;
import java.util.Scanner;

public class BitcoinSender {

    private static final Logger LOGGER = Logger.getLogger(BitcoinSender.class);

    private static final String RECIPIENT_ADDRESS = "tb1q5tsjcyz7xmet07yxtumakt739y53hcttmntajq";

    public static void main(String[] args) throws Exception {
        // Шаг 1
        WalletService walletService = new WalletService();
        Wallet wallet = walletService.createWallet();
        LOGGER.info("Мнемоническая фраза: " + wallet.getKeyChainSeed().getMnemonicCode());
        LOGGER.info("Адрес кошелька: " + wallet.currentReceiveAddress());

        BlockchainService blockchainService = new BlockchainService();
        PeerGroup peerGroup = null;

        Optional<BlockChain> optionalBlockChain = blockchainService.createBlockChain(wallet);
        if (optionalBlockChain.isPresent()) {
            BlockChain blockChain = optionalBlockChain.get();
            peerGroup = blockchainService.createAndStartPeerGroup(blockChain, wallet);
        }

        BalanceService balanceService = new BalanceService(wallet);
        balanceService.saveBalance();

        // Шаг 2
        balanceService.startCheckingBalance();

        // Шаг 3
        processUserInput(peerGroup, wallet);

        balanceService.saveBalance();
    }

    private static void processUserInput(PeerGroup peerGroup, Wallet wallet) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            LOGGER.info("Вы действительно хотите отправить деньги? (Y/N)");
            String input = scanner.nextLine();

            if (input.equalsIgnoreCase("Y")) {
                if (peerGroup != null) {
                    BitcoinPaymentService bitcoinPaymentService = new BitcoinPaymentService();
                    bitcoinPaymentService.sendCoin(RECIPIENT_ADDRESS, peerGroup, wallet);
                }
            } else if (input.equalsIgnoreCase("N")) {
                LOGGER.info("Отправка отменена");
                break;
            } else {
                LOGGER.warn("Введена неправильная команда");
            }
        }
    }
}

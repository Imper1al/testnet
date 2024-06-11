package services;

import org.bitcoinj.wallet.Wallet;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;

public class BalanceService {

    private static final Logger LOGGER = Logger.getLogger(BalanceService.class);

    private static final int POOL_SIZE = 1;
    private static final int POOL_INITIAL_DELAY = 0;
    private static final int POOL_PERIOD = 1;
    private static final int DELAY_IN_MILLIS = 60000;

    private final Wallet wallet;
    private final ScheduledExecutorService executorService;

    public BalanceService(Wallet wallet) {
        this.wallet = wallet;
        this.executorService = Executors.newScheduledThreadPool(POOL_SIZE);
    }

    public void startCheckingBalance() {
        executorService.scheduleAtFixedRate(this::checkBalance, POOL_INITIAL_DELAY, POOL_PERIOD, TimeUnit.MINUTES);
    }

    protected void checkBalance() {
        long previousBalance = -1;

        while (true) {
            try {
                long balance = wallet.getBalance().getValue();
                LOGGER.info("Текущий баланс: " + balance);

                if (previousBalance != -1) {
                    printBalanceChange(previousBalance, balance);
                }

                previousBalance = balance;

                Thread.sleep(DELAY_IN_MILLIS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                LOGGER.error("Ошибка при проверке баланса: " + e.getMessage());
            }
        }
    }

    private void printBalanceChange(long previousBalance, long currentBalance) {
        long difference = currentBalance - previousBalance;
        if (difference > 0) {
            LOGGER.info("Баланс пополнен на: " + difference);
        } else if (difference < 0) {
            LOGGER.info("Баланс уменьшился на: " + Math.abs(difference));
        } else {
            LOGGER.info("Баланс не изменился");
        }
    }

    public void saveBalance() throws IOException {
        wallet.saveToFile(new java.io.File("wallet.dat"));
    }
}

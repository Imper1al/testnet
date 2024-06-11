package services;

import org.bitcoinj.core.Coin;
import org.bitcoinj.wallet.Wallet;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class BalanceServiceTest {

    @Mock
    private Wallet mockWallet;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCheckBalance() {
        ExecutorService mockExecutorService = Executors.newSingleThreadExecutor();
        BalanceService balanceService = new BalanceService(mockWallet);

        long initialBalance = 100000;
        long updatedBalance = 120000;
        when(mockWallet.getBalance()).thenReturn(Coin.valueOf(initialBalance), Coin.valueOf(updatedBalance));

        mockExecutorService.submit(balanceService::checkBalance);

        verify(mockWallet, atLeastOnce()).getBalance();
        verify(mockWallet).getBalance();
    }

    @Test
    public void testSaveBalance() throws IOException {
        BalanceService balanceService = new BalanceService(mockWallet);

        balanceService.saveBalance();

        verify(mockWallet).saveToFile(any(java.io.File.class));
    }
}
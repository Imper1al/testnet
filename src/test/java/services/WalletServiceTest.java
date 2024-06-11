package services;

import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.params.TestNet3Params;
import org.bitcoinj.wallet.DeterministicSeed;
import org.bitcoinj.wallet.Wallet;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class WalletServiceTest {

    @Test
    public void testCreateWallet() {
        WalletService walletService = new WalletService();
        Wallet wallet = walletService.createWallet();

        assertNotNull(wallet);
        assertNotNull(wallet.getKeyChainSeed());
        assertNotNull(wallet.currentReceiveAddress());
    }

    @Test
    public void testCreateMnemonicPhrase() {
        WalletService walletService = new WalletService();
        DeterministicSeed seed = walletService.createMnemonicPhrase();

        assertNotNull(seed);
        assertNotNull(seed.getMnemonicCode());
    }

    @Test
    public void testGetNetworkParams() {
        WalletService walletService = new WalletService();
        NetworkParameters networkParameters = walletService.getNetworkParams();

        assertNotNull(networkParameters);
        assertEquals(TestNet3Params.get(), networkParameters);
    }
}
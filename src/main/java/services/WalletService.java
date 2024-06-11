package services;

import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.params.TestNet3Params;
import org.bitcoinj.wallet.DeterministicSeed;
import org.bitcoinj.wallet.Wallet;

import java.security.SecureRandom;

public class WalletService {

    private static final int BITS_PER_BYTE = 8;

    public Wallet createWallet() {
        return Wallet.fromSeed(getNetworkParams(), createMnemonicPhrase());
    }

    protected DeterministicSeed createMnemonicPhrase() {
        SecureRandom random = new SecureRandom();
        byte[] entropy = new byte[DeterministicSeed.DEFAULT_SEED_ENTROPY_BITS / BITS_PER_BYTE];
        random.nextBytes(entropy);
        return new DeterministicSeed(entropy, "", System.currentTimeMillis());
    }

    protected NetworkParameters getNetworkParams() {
        return TestNet3Params.get();
    }
}

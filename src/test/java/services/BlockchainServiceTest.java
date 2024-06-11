package services;

import org.bitcoinj.core.BlockChain;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.PeerGroup;
import org.bitcoinj.store.BlockStore;
import org.bitcoinj.store.BlockStoreException;
import org.bitcoinj.store.MemoryBlockStore;
import org.bitcoinj.wallet.Wallet;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class BlockchainServiceTest {

    private Wallet wallet;

    @Before
    public void setUp() {
        wallet = new Wallet(NetworkParameters.fromID(NetworkParameters.ID_TESTNET));
    }

    @Test
    public void testCreateBlockChain_Success() {
        BlockchainService blockchainService = new BlockchainService();
        Optional<BlockChain> optionalBlockChain = blockchainService.createBlockChain(wallet);
        assertTrue(optionalBlockChain.isPresent());
    }

    @Test
    public void testCreateAndStartPeerGroup() throws BlockStoreException {
        NetworkParameters params = NetworkParameters.fromID(NetworkParameters.ID_TESTNET);
        BlockStore blockStore = new MemoryBlockStore(params);
        BlockChain chain = new BlockChain(params, wallet, blockStore);

        BlockchainService blockchainService = new BlockchainService();
        PeerGroup peerGroup = blockchainService.createAndStartPeerGroup(chain, wallet);

        assertNotNull(peerGroup);
        assertTrue(peerGroup.isRunning());
    }
}
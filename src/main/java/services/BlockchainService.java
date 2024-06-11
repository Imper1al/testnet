package services;

import org.apache.log4j.Logger;
import org.bitcoinj.core.BlockChain;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.PeerGroup;
import org.bitcoinj.store.BlockStore;
import org.bitcoinj.store.BlockStoreException;
import org.bitcoinj.store.MemoryBlockStore;
import org.bitcoinj.wallet.Wallet;

import java.util.Optional;

public class BlockchainService {

    private static final Logger LOGGER = Logger.getLogger(BlockchainService.class);

    public Optional<BlockChain> createBlockChain(Wallet wallet) {
        try {
            NetworkParameters params = wallet.getParams();
            BlockStore blockStore = new MemoryBlockStore(params);
            BlockChain blockChain = new BlockChain(params, wallet, blockStore);
            return Optional.of(blockChain);
        } catch (BlockStoreException e) {
            LOGGER.error(e.getMessage());
            return Optional.empty();
        }
    }

    public PeerGroup createAndStartPeerGroup(BlockChain chain, Wallet wallet) {
        PeerGroup peerGroup = new PeerGroup(wallet.getParams(), chain);
        peerGroup.addWallet(wallet);
        peerGroup.start();
        return peerGroup;
    }
}

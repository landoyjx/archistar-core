package at.ac.ait.archistar.integration;

import io.netty.channel.nio.NioEventLoopGroup;

import java.util.HashSet;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import at.ac.ait.archistar.backendserver.storageinterface.MemoryStorage;
import at.ac.ait.archistar.backendserver.storageinterface.StorageServer;
import at.ac.ait.archistar.engine.TestEngine;
import at.ac.ait.archistar.engine.crypto.PseudoMirrorCryptoEngine;
import at.ac.ait.archistar.engine.distributor.BFTDistributor;
import at.ac.ait.archistar.engine.distributor.Distributor;
import at.ac.ait.archistar.engine.distributor.TestServerConfiguration;
import at.ac.ait.archistar.engine.metadata.MetadataService;
import at.ac.ait.archistar.engine.metadata.SimpleMetadataService;
import at.archistar.crypto.CryptoEngine;

public class MemoryOnlyTest extends AbstractIntegrationTest {

    @BeforeClass
    public static void prepareServer() {
        /* test configuration */
        HashSet<StorageServer> servers = new HashSet<>();
        for(int i = 0; i < 4; i++) {
            servers.add(new MemoryStorage(i));
        }
        serverConfig = new TestServerConfiguration(servers);
        serverConfig.setupTestServer(1);

        CryptoEngine crypto = new PseudoMirrorCryptoEngine(4);
        Distributor distributor = new BFTDistributor(serverConfig, new NioEventLoopGroup());
        MetadataService metadata = new SimpleMetadataService(serverConfig, distributor, crypto);
        engine = new TestEngine(serverConfig, metadata, distributor, crypto);
    }

    @AfterClass
    public static void shutdownServers() {
        serverConfig.teardownTestServer();
    }
}

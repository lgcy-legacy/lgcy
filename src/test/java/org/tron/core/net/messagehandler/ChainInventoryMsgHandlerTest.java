package org.tron.core.net.messagehandler;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javafx.util.Pair;
import org.junit.Assert;
import org.junit.Test;
import org.tron.core.capsule.BlockCapsule.BlockId;
import org.tron.core.config.Parameter.NodeConstant;
import org.tron.core.exception.P2pException;
import org.tron.core.net.message.ChainInventoryMessage;
import org.tron.core.net.peer.PeerConnection;

public class ChainInventoryMsgHandlerTest {

  ChainInventoryMsgHandler handler = new ChainInventoryMsgHandler();
  ChainInventoryMessage msg = new ChainInventoryMessage(new ArrayList<>(), 0l);
  List<BlockId> blockIds = new ArrayList<>();
  PeerConnection peer = new PeerConnection();

  @Test
  public void test() {
    try {
      handler.processMessage(peer, msg);
    }catch (P2pException e) {
      Assert.assertTrue(e.getMessage().equals("not send syncBlockChainMsg"));
    }

    peer.setSyncChainRequested(new Pair<>(new LinkedList<>(), System.currentTimeMillis()));

    try {
      handler.processMessage(peer, msg);
    }catch (P2pException e) {
      Assert.assertTrue(e.getMessage().equals("blockIds is empty"));
    }


    long size = NodeConstant.SYNC_FETCH_BATCH_NUM + 2;
    for (int i = 0; i < size; i++) {
      blockIds.add(new BlockId());
    }
    msg = new ChainInventoryMessage(blockIds, 0l);

    try {
      handler.processMessage(peer, msg);
    }catch (P2pException e) {
      Assert.assertTrue(e.getMessage().equals("big blockIds size: " + size));
    }

    blockIds.clear();
    size = NodeConstant.SYNC_FETCH_BATCH_NUM / 100;
    for (int i = 0; i < size; i++) {
      blockIds.add(new BlockId());
    }
    msg = new ChainInventoryMessage(blockIds, 100l);

    try {
      handler.processMessage(peer, msg);
    }catch (P2pException e) {
      Assert.assertTrue(e.getMessage().equals("remain: 100, blockIds size: " + size));
    }
  }

}

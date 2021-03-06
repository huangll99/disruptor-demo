package com.hll.disruptor.diamond;

import com.hll.disruptor.model.OrderEvent;
import com.lmax.disruptor.EventHandler;

/**
 * Created by hll on 2016/8/1.
 */
public class AccessNameHandler implements EventHandler<OrderEvent> {
  @Override
  public void onEvent(OrderEvent event, long sequence, boolean endOfBatch) throws Exception {
    System.out.println("receiving user name: "+ event.getUsername());
    event.setUsername("passed: "+event.getUsername());
  }
}

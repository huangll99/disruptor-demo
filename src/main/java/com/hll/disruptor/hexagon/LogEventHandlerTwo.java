package com.hll.disruptor.hexagon;

import com.hll.disruptor.model.OrderEvent;
import com.lmax.disruptor.EventHandler;

/**
 * Created by hll on 2016/8/1.
 */
public class LogEventHandlerTwo implements EventHandler<OrderEvent> {
  @Override
  public void onEvent(OrderEvent event, long sequence, boolean endOfBatch) throws Exception {
    System.out.println("log -- "+event.getUsername());
  }
}

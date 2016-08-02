package com.hll.disruptor.commons;

import com.hll.disruptor.model.OrderEvent;
import com.lmax.disruptor.EventFactory;

/**
 * Created by hll on 2016/8/1.
 */
public class OrderEventFactory implements EventFactory<OrderEvent> {
  @Override
  public OrderEvent newInstance() {
    return new OrderEvent();
  }
}
